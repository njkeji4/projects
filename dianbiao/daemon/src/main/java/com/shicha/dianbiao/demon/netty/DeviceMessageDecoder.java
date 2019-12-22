package com.shicha.dianbiao.demon.netty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shicha.dianbiao.demon.controller.INotifyHost;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DeviceMessageDecoder extends  ByteToMessageDecoder {

	private static final Logger log = LoggerFactory.getLogger(DeviceMessageDecoder.class);
	
	private static byte[] reg = {0x55,(byte)0xaa,0x55,(byte)0xaa};
	private static byte[] reg2 = {0x66,(byte)0xaa,0x66,(byte)0xaa};
	
	
	//public DeviceMessageDecoder(){}
	
	public DeviceMessageDecoder(INotifyHost notifyhost){
		this.notifyhost = notifyhost;
	}
	
	INotifyHost notifyhost;
	
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
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
				
		if (in.readableBytes() < 4) {
            return; 
        }
		in.markReaderIndex();
		
		ByteBuf head = in.readBytes(4);
		
		//heart beat or login message
		if(head.compareTo(Unpooled.copiedBuffer(reg)) == 0 || 
				head.compareTo(Unpooled.copiedBuffer(reg2)) == 0 ) {	
			
			if(in.readableBytes() < 9) {
				in.resetReaderIndex();
				return;
			}
			
			int type = 0;
			if(head.compareTo(Unpooled.copiedBuffer(reg2)) == 0 ) {
				type = 1;
			}
			
			ByteBuf content = in.readBytes(9);			
			byte[] response = new byte[13];
			head.readBytes(response, 0, 4);			
			content.readBytes(response,4, 9);
			head.release();
			content.release();
			
			String addr = getAddr(response, 6);  
			
			if(addr.equals("440221933405"))type=1;  //only for xinjiang testing
			
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
			
			return;			
		}
		
		//receive response message from device
		
		//reset the first 4		
		in.resetReaderIndex();
		
		in.markReaderIndex();
		
		int count = 0;
		while(in.readByte() != 0x68) {
			count++;
		}
		
		//response started: addr + 68 + c
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
		
		CmdRes res = CmdRes.parse(message);
		
		log.info(res.getAddr() + " got response: " +  res.getResponse());
		
		try{			
			notifyhost.postCmdResult(res);			
		}catch(Exception ex) {
			ex.printStackTrace();			
		}
		
	}
	
	/*
	//buf:addr+68+c+....
	private void handleReponse(ByteBuf buf) {
		
		//System.out.println(Utils.current() + " response: 68" + Utils.byte2str(buf) );
		
		if(buf.readableBytes() == 0) {
			return;
		}
		
		byte[] content = new byte[buf.readableBytes()];
		buf.readBytes(content);
		
		String addr = getAddr2(content, 0);
		if(!addr.equals("440221933404")) {
			return;
		}
		
		System.out.println(Utils.current() + " response: 68" + Utils.byte2str(content) );
		
		int command = content[7] & 0xff;
		if(command == 0x91) {//query ok
			App.cmdresok[0]++;
			System.out.println(Utils.current() + addr+ " query ok:"+App.cmdresok[0]);
		}
		
		if(command == 0xD1) {//query fail
			App.cmdresfail[0]++;
			System.out.println(Utils.current() + "  " + addr+ " query fail:"+App.cmdresfail[0]);
		}
		
		if(command == 0x9c) {//switch on or off ok
			App.cmdresok[1]++;
			System.out.println(Utils.current() + addr+ " switch  ok:" + App.cmdresok[1]);
		}
		
		if(command == 0xdc) {//switch on or off fail
			App.cmdresfail[1]++;
			System.out.println(Utils.current() + " switch  fail:" + App.cmdresfail[1]);
		}
		
	}*/
	
	
	 @Override
	    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	       

	        log.info("--- A New Client ---");
	        

	    }
	 
	 @Override
	    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		  
		 String addr = Device.remove(ctx);
		 
		 //String addr = Device.reApp.get(ctx);
		 
		 log.info("---disconnect  ---"+addr);
		  
		  //App.remove(ctx);
		  //App.remove(addr);
		  
		  //App.ctxstatus.remove(ctx);
		  
		  ctx.close();
		  
		  ctx = null;
	     
	    }	
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //cause.printStackTrace();
        

        String addr = Device.remove(ctx);
		 
        log.info("---exceptionCaught  ---"+addr);
		 
		  ctx.close();
		  
		  ctx = null;
    }

}
