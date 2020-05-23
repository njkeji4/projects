package com.shicha.dianbiao.demon.netty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shicha.dianbiao.demon.controller.INotifyHost;
import com.shicha.dianbiao.demon.domain.Command;
import com.shicha.dianbiao.demon.domain.MeterData;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DeviceMessageDecoder extends  ByteToMessageDecoder {

	private static final Logger log = LoggerFactory.getLogger(DeviceMessageDecoder.class);
	
	private static byte[] AC1_HEAD = {0x55,(byte)0xaa,0x55,(byte)0xaa};	//AC1
	private static byte[] AC3_HEAD = {0x66,(byte)0xaa,0x66,(byte)0xaa};  //AC3
	private static byte[] DC_HEAD = {0x66,(byte)0xbb,0x66,(byte)0xbb}; //DC	
	
	INotifyHost notifyhost;
	
	public DeviceMessageDecoder(INotifyHost notifyhost){
		this.notifyhost = notifyhost;
	}
	
	public String getAddr(byte[] bytes, int start) {
		if(bytes.length < start+6 ) {			
			return null;
		}
		
		String addr="";
		for(int i = start; i < start+6; i++) {
			  int v = bytes[i] & 0xFF;
		        String hv = Integer.toHexString(v);
		        if(hv.length() < 2)
		        	hv = "0"+hv;
		        addr += hv;
		}
		
		return addr;
	}
	
	//根据抄表消息来确定表的类型：直流，单相，三相
	public static int deviceType(int periodcmd){

		if(periodcmd == Command.READ_DC) {			
			return Device.DEVICE_DC;
		}
		if(periodcmd == Command.READ_METER) {
			return Device.DEVICE_AC1;		
		}
		
		return Device.DEVICE_AC3;
	}
	
	private boolean isHeartBeatOrLoginMessage(ChannelHandlerContext ctx, ByteBuf in) {
		
		in.markReaderIndex();
		
		ByteBuf head = in.readBytes(4);
		
		if(
				head.compareTo(Unpooled.copiedBuffer(AC1_HEAD)) == 0  
				||head.compareTo(Unpooled.copiedBuffer(AC3_HEAD)) == 0 
				|| head.compareTo(Unpooled.copiedBuffer(DC_HEAD)) == 0 ) {	
			
			if(in.readableBytes() < 9) {
				in.resetReaderIndex();
				return true;
			}
			
			int type = Device.DEVICE_DC;
			if(head.compareTo(Unpooled.copiedBuffer(AC3_HEAD)) == 0 ) {
				type = Device.DEVICE_AC3;
			}
			if(head.compareTo(Unpooled.copiedBuffer(AC1_HEAD)) == 0 ) {
				type = Device.DEVICE_AC1;
			}
			
			ByteBuf content = in.readBytes(9);			
			byte[] response = new byte[13];
			head.readBytes(response, 0, 4);			
			content.readBytes(response,4, 9);
			head.release();
			content.release();
			
			String addr = getAddr(response, 6);  
			
			if(response[5] == 1) { //login
				Device.add(ctx, 0, addr, type);
				
				log.info(addr + " login");
				
			}else if(response[5]== 2) {	//heartbeat
				if(Device.getDevice(addr) != null) {
					Device.setDeviceStatus(addr, 1);
					log.info(addr + " heartbeat");					
				}else {
					log.info(addr + "heartbeat");
					Device.add(ctx, 1, addr, type);
				}
				
				try{				
					notifyhost.postHeartBeat(addr);					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			ctx.writeAndFlush(Unpooled.copiedBuffer(response));	
			
			return true;
		}
		
		in.resetReaderIndex();
		
		return false;
	}	
	
	private void otherMessage(ChannelHandlerContext ctx, ByteBuf in) {
		in.markReaderIndex();		
		
		int count = 0;
		while(in.readByte() != 0x68) {		
			count++;			
		}
		//log.info("before 0x68:"+count);
		
		//response started: addr(6) + 68 + c
		if(in.readableBytes() < CmdRes.headLength - 1) {
			in.resetReaderIndex();			
			return;
		}
		
		//read message head  addr+0x68 +control code+datalen
		byte[] resHead = new byte[CmdRes.headLength];
		resHead[0] = 0x68;
		in.readBytes(resHead,1,CmdRes.headLength - 1);
		
		int dataLen = CmdRes.getBodyLen(resHead);
		
		if(in.readableBytes() < dataLen + CmdRes.tailLength) {
			in.resetReaderIndex();
			return;
		}
		
		in.resetReaderIndex();		
		byte[]message = new byte[CmdRes.headLength + dataLen + CmdRes.tailLength];
		in.readBytes(message);
		
		log.info("receive message:" + Utils.byte2str(message));
				
		try{		
			
			CmdRes res = CmdRes.parse(message);			
			log.info(res.getAddr() + " got response: " +  res.getResponse());
			
			Device d = Device.getDevice(res.getAddr());
			
			//抄表上报消息
			if(res.getCmdCode() == Command.READ_METER || 
					res.getCmdCode() == Command.READ_METER3 ||
					res.getCmdCode() == Command.READ_DC) {
				
				if(Device.getDevice(res.getAddr()) == null) {
					Device.add(ctx, 1, res.getAddr(), deviceType(res.getCmdCode()));
					d = Device.getDevice(res.getAddr());
				}
				
				Device.setDeviceStatus(res.getAddr(), 1);
				
				if(d != null) {
					d.setType( deviceType(res.getCmdCode()));	
				}else{
					log.warn("get period messsage,but device is null:"+ res.getAddr());
				}
				
				try{
					notifyhost.postPeriod((MeterData)res.getData());
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				//return heartbeat message
				byte[] response = new byte[] {0x66,(byte)0xbb,0x66,(byte)0xbb,0x68,0x02,0,0,0,0,0,0,0x16};
				ctx.writeAndFlush(Unpooled.copiedBuffer(response));	
				
				return;
			}
			
			//其他主机发送的请求消息
			if(d != null && d.isBusy()) {
				d.setCmdRes(res);				
				synchronized(d) {
					d.notifyAll();
				}				
			}else {
				log.warn("receive response, but it is too late, ignor this response");
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();			
		}
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
				
		if (in.readableBytes() < 4) {
            return; 
        }
		
		if(isHeartBeatOrLoginMessage(ctx, in)) {
			return ;
		}
		
		otherMessage(ctx, in);
		
	}	
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("--- A New Client ---");
    }
	 
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	  
	 String addr = Device.remove(ctx);
	 
	 log.warn("---disconnect  ---"+addr);
	 
	 ctx.close();
	  
	 ctx = null;
     
    }	
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		
        String addr = Device.remove(ctx);
		 
        log.error("---exceptionCaught  ---"+addr);
		 
		ctx.close();
		  
		ctx = null;
    }

}
