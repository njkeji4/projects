package com.shicha.dianbiao.demon.netty;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shicha.dianbiao.demon.controller.CommandRequestController;

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
	
	//statis
	int heartbeatCount;
	int loginCount;
	int cmdCount;
	int resokCount;
	int resfailCount;
	
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
	
	
	
	public static int queryCmd(String addr) {
	
		//danxiang 0
		byte[] buff0 = {(byte)0xFE, (byte)0xFE ,(byte)0xFE ,(byte)0xFE ,0x68 ,0x04 ,(byte)0x34 ,(byte)0x93 ,(byte)0x21 ,(byte)0x02 ,(byte)0x44 ,(byte)0x68 ,(byte)0x11 ,(byte)0x04 ,(byte)0x34 ,(byte)0x43 ,(byte)0x93 ,(byte)0x37 ,(byte)0x5f ,(byte)0x16};
		
		//san xiang 1
		byte[] buff1 = {(byte)0xFE, (byte)0xFE ,(byte)0xFE ,(byte)0xFE ,0x68 ,0x04 ,(byte)0x34 ,(byte)0x93 ,(byte)0x21 ,(byte)0x02 ,(byte)0x44 ,(byte)0x68 ,(byte)0x11 ,(byte)0x04 ,(byte)0x39 ,(byte)0x45 ,(byte)0x93 ,(byte)0x37 ,(byte)0x5f ,(byte)0x16};
		
		int type = 1;
		if(map.get(addr) != null)
			type = map.get(addr).getType();
		
		byte[] buff = type == 0? buff0 : buff1;
		
		return cmd(addr,buff);
	}
	
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
		
		int[] times = new int[8];
		for(int i = 0; i < offon.length; i++)
			times[i] = offon[i];
		
		if(offon.length < 8) {
			for(int i = offon.length; i < 8; i++) {
				times[i] = 0x999999;
			}
		}
		
		byte[] buf = {(byte)0xFE ,(byte)0xFE ,(byte)0xFE ,(byte)0xFE ,(byte)0x68 ,(byte)0x04 ,(byte)0x34 ,(byte)0x93 ,(byte)0x21 ,(byte)0x02 ,(byte)0x44 ,(byte)0x68 ,(byte)0x14   ,(byte)0x24 
						,(byte)0x34 ,(byte)0x33 ,(byte)0x35 ,(byte)0x37  ,(byte)0x35 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x63 ,(byte)0x63 ,(byte)0x63 ,(byte)0x63 
						,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x33 ,(byte)0x34 ,(byte)0x33 ,(byte)0x33 ,(byte)0x35 ,(byte)0x33 ,(byte)0x33 ,(byte)0x36 ,(byte)0x33 ,(byte)0x33 ,(byte)0x37 ,(byte)0x33 ,(byte)0x33 ,(byte)0x38 ,(byte)0x33 ,(byte)0x33 ,(byte)0x39 ,(byte)0x33 ,(byte)0x33 ,(byte)0x3A 
						,(byte)0x4B ,(byte)0x16 };
		
		
		//encode time
		int start = 26;
		for(int i = 0; i < 8; i++) {
			int second = times[i] & 0xff;
			int minute = (times[i] &0xff00) >> 8;
			int hour = (times[i] & 0xff0000) >> 16;
			
			buf[start++] = (byte)(0x33 + second);
			buf[start++] = (byte)(0x33 + minute);
			buf[start++] = (byte)(0x33 + hour);			
		}
		
		
		return cmd(addr,buf);
		
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
	
	public static int cmd(String addr, byte[] buf) {
		
		encodeAddress(buf, addr);		
		calcCS(buf);
		
		log.info("send request:" +  addr + "  " + Utils.byte2str(buf));
		
		if(map.get(addr)== null || map.get(addr).getStatus() == 0 || map.get(addr).getCtx() == null) {
			log.info("try to send comamnd to device:" + addr + " but , it  is offline, just return -1");
			return -1;
		}
		
		
		
		map.get(addr).getCtx().writeAndFlush(Unpooled.copiedBuffer(buf));
		
		return 0;
	}
	
	public static String remove(ChannelHandlerContext ctx) {
		String addr = ctxmap.get(ctx);
		if(ctxmap.get(ctx) != null) {
			map.remove(ctxmap.get(ctx));
			ctxmap.remove(ctx);
		}		
		return addr;
	}

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

	public int getHeartbeatCount() {
		return heartbeatCount;
	}

	public void setHeartbeatCount(int heartbeatCount) {
		this.heartbeatCount = heartbeatCount;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public int getCmdCount() {
		return cmdCount;
	}

	public void setCmdCount(int cmdCount) {
		this.cmdCount = cmdCount;
	}

	public int getResokCount() {
		return resokCount;
	}

	public void setResokCount(int resokCount) {
		this.resokCount = resokCount;
	}

	public int getResfailCount() {
		return resfailCount;
	}

	public void setResfailCount(int resfailCount) {
		this.resfailCount = resfailCount;
	}
}
