package com.shicha.dianbiao.demon.netty;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shicha.dianbiao.demon.controller.CommandRequestController;
import com.shicha.dianbiao.demon.domain.Command;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;


//send message to device
public class Device {

	private static final Logger log = LoggerFactory.getLogger(Device.class);

	private static HashMap<String, Device>map = new HashMap<String, Device>();
	private static HashMap<ChannelHandlerContext, String>ctxmap = new HashMap<ChannelHandlerContext, String>();
	
	int status;   	//0-login  1-normal 
	String addr;
	int type;		//0 -danxiang  1-sanxiang
	ChannelHandlerContext ctx;
	CmdRes cmdRes;
	boolean busy;   //0:available  1:busy
	
	public Device() {}
	
	public Device(ChannelHandlerContext ctx, int status, String addr, int type) {
		this.status = status;
		this.addr = addr;
		this.type = type;
		this.ctx = ctx;
	}
	
	public static Device getDevice(String addr) {
		return map.get(addr);
	}
	
	public static void setDeviceStatus(String addr, int status) {
		if(map.get(addr) != null) {
			map.get(addr).setStatus(status);
		}
	}
	
	public static void add(ChannelHandlerContext ctx, int status, String addr, int type) {
		map.put(addr, new Device(ctx,status,addr,type));
		ctxmap.put(ctx,addr);
	}	
	
	public static String remove(ChannelHandlerContext ctx) {
		String addr = ctxmap.get(ctx);
		if(ctxmap.get(ctx) != null) {
			map.remove(ctxmap.get(ctx));
			ctxmap.remove(ctx);
		}		
		return addr;
	}
	
	//////////////////// read command	
	public static int readData(String addr, String dataType) {
		int type = 0;
		if(map.get(addr) != null)
			type = map.get(addr).getType();
		
		byte[] code = null;
		if(type == 0) {
			code = Command.readCmdMap.get(dataType);
		}else {
			code = Command.readCmdMap3.get(dataType);
		}
		
		if(code == null) {
			return CommandRequestController.CMD_UNSUPPORT;
		}
		
		return readCmd(addr, code);
	}
	
	public static int writeData(String addr, String dataType, byte[] buf) {
		int type = 0;
		if(map.get(addr) != null)
			type = map.get(addr).getType();
		
		byte[] code = null;
		if(type == 0) {
			code = Command.readCmdMap.get(dataType);
		}else {
			code = Command.readCmdMap3.get(dataType);
		}
		
		if(code == null) {
			return CommandRequestController.CMD_UNSUPPORT;
		}
		
		for(int i = 0; i < buf.length; i++) {
			buf[i] = (byte)(buf[i] + 0x33);
		}
		
		return writeCmd(addr, code, buf);
	}
	
	/*
	//read data
	public static int readMeter(String addr) {
		
		int type = 1;
		if(map.get(addr) != null)
			type = map.get(addr).getType();
		
		byte[] code = {0x04, 0x60, 0x10, 0x01};
		if(type == 1) {
			code = new byte[]{0x04, 0x60, 0x12, 0x06};
		}
		
		return readCmd(addr, code);
	}
	
	public static int readDate(String addr) {
		byte[] code = {0x04, 0x00, 0x01, 0x01};
		
		return readCmd(addr, code);
	}
	
	public static int readTime(String addr) {
		byte[] code = {0x04, 0x00, 0x01, 0x02};
		
		return readCmd(addr, code);
	}	
	
	public static int readAutoOnOff(String addr) {
		byte[] code = {0x04, 0x02, 0x00, 0x01};
		
		return readCmd(addr, code);
	}
	
	public static int readPeriod(String addr) {
		byte[] code = {0x04, 0x04, 0x04, 0x00};
		
		int type = 1;
		if(map.get(addr) != null)
			type = map.get(addr).getType();		
		
		if(type == 1) {
			code = new byte[]{0x04, 0x00, 0x01, 0x0d};
		}
		
		return readCmd(addr, code);
	}
	*/
	
	public static int switchOffCmd(String addr) {
		byte[] buff = {(byte)0xFE ,(byte)0xFE ,(byte)0xFE ,(byte)0xFE ,(byte)0x68 ,(byte)0x04 ,(byte)0x34 ,(byte)0x93 ,(byte)0x21 ,(byte)0x02 ,(byte)0x44 ,(byte)0x68 ,
				(byte)0x1C ,(byte)0x10 ,(byte)0x35 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x4D ,(byte)0x33 ,(byte)0x46 ,(byte)0x59 ,(byte)0x42 ,(byte)0x45 ,(byte)0x3F ,(byte)0x16 ,(byte)0xC3 ,(byte)0x16};
				
		return cmd(addr,buff);
	}
	
	public static int switchOnCmd(String addr) {
		byte[] buff = {(byte)0xFE ,(byte)0xFE ,(byte)0xFE ,(byte)0xFE ,(byte)0x68 ,(byte)0x04 ,(byte)0x34 ,(byte)0x93 ,(byte)0x21 ,(byte)0x02 ,(byte)0x44 ,(byte)0x68 ,
				(byte)0x1C ,(byte)0x10 ,(byte)0x35 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x4F ,(byte)0x33 ,(byte)0x41 ,(byte)0x5A ,(byte)0x42 ,(byte)0x45 ,(byte)0x3F ,(byte)0x16 ,(byte)0xC1 ,(byte)0x16};
		
		
		return cmd(addr,buff);
	}
	
	public static int setAutoSwitchOnOff(String addr, int[]offon) {//off on off on off on off on
		
		//fill to 8 bytes
		int[] times = new int[8];
		for(int i = 0; i < offon.length; i++)
			times[i] = offon[i];
		
		if(offon.length < 8) {
			for(int i = offon.length; i < 8; i++) {
				times[i] = 0x636363;//0x999999;
			}
		}
		
		byte[]dataid= {0x04,0x02,0x0,0x01};
		
		byte[]data = new byte[24];
		int idx = 0;
		for(int i = 0; i < 8; i++) {
			int second = times[i] & 0xff;
			int minute = (times[i] &0xff00) >> 8;
			int hour = (times[i] & 0xff0000) >> 16;
			
			//bcd
			hour = hour / 10 * 16 + hour % 10; 
			minute = minute / 10 * 16 + minute % 10;
			second = second / 10 * 16 + second % 10;
			
			data[idx++] = (byte)(0x33 + second);
			data[idx++] = (byte)(0x33 + minute);
			data[idx++] = (byte)(0x33 + hour);			
		}	
		
		return writeCmd(addr, dataid, data);
	}	
	
	
	public static int readCmd(String addr, byte[] opcode) {
		int fixLen = 20;
		byte[] buf = new byte[fixLen];
		
		for(int i = 0; i < 4; i++)
			buf[i]=(byte)0xfe;
		
		buf[4]=(byte)0x68;
		
		//buf[5-10]=address
		
		buf[11]=(byte)0x68;
		buf[12]=(byte)0x11;		//0x11 -- read
		buf[13]=(byte)(0x4);	//length		
		
		//buf[14-17]=data id
		for(int i = 0 ; i < opcode.length; i++)
			buf[14+i] = (byte)(opcode[3-i] + 0x33);
		
		buf[buf.length-1] = 0x16;
		
		return cmd(addr, buf);
	}
	
	public static int writeCmd(String addr, byte[] opcode, byte[] data) {
		//fe fe fe fe 68 addr1 ... addr6 68 14 L op0-op4 password user n1-nm cs 16
		int fixLen = 28;
		byte[] buf = new byte[fixLen + data.length];
		
		for(int i = 0; i < 4; i++)
			buf[i]=(byte)0xfe;
		
		buf[4]=(byte)0x68;
		
		//buf[5-10]=address
		
		buf[11]=(byte)0x68;
		buf[12]=(byte)0x14;					//0x14 -- write
		buf[13]=(byte)(12 + data.length);	//length		
		
		//buf[14-17]=data id
		for(int i = 0 ; i < opcode.length; i++)
			buf[14+i] = (byte)(opcode[3-i] + 0x33);
		
		//password
		buf[18]= 0x35;buf[19]= 0x33;buf[20]= 0x33;buf[21]= 0x33;
		
		//user id
		buf[22]= 0x63;buf[23]= 0x63;buf[24]= 0x63;buf[25]= 0x63;
		
		for(int i = 0; i < data.length; i++) {
			buf[26+i] = data[i];
		}
		
		buf[buf.length-1] = 0x16;
		
		return cmd(addr, buf);
	}
	
	public static int cmd(String addr, byte[] buf) {
		
		Device d = Device.getDevice(addr);
		if(d == null || d.getStatus() == 0 || d.getCtx() == null) {
			log.info("try to send comamnd to device:" + addr + " but , it  is offline or busy, just return -1");
			return 1;
		}
		if(d.isBusy()) {
			log.info("try to send another command to device:" + addr + "but, it is busy with another command");
			return 2;
		}
		
		encodeAddress(buf, addr);		
		calcCS(buf);
		
		d.setCmdRes(null);
		d.setBusy(true);		
		
		log.info("send request:" +  addr + "  " + Utils.byte2str(buf));
		
		d.getCtx().writeAndFlush(Unpooled.copiedBuffer(buf));
		
		return 0;
	}	
	
	public static void encodeAddress(byte[] buf, String addr) {
		while(addr.length() < 12) {
			addr= "0"+ addr;
		}
		
		int idx = 10;
		for(int i = 0; i < addr.length(); i+=2) {
			buf[idx--]= (byte)(Integer.parseInt(addr.substring(i, i+2), 16));
		}
	}
	
	public static void calcCS(byte[] buf) {
		int sum = 0;
		for(int i = 4; i < buf.length-2; i++) {
			sum += buf[i];
			sum = sum % 256;
		}
		log.info("cs="+( (byte)sum));
		buf[buf.length - 2] = (byte)sum;
	}
	
	
	//////////////////// set get
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public CmdRes getCmdRes() {
		return cmdRes;
	}

	public void setCmdRes(CmdRes cmdRes) {
		this.cmdRes = cmdRes;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}	
}
