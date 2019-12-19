package com.shicha.dianbiao.demon.netty;

import java.util.HashMap;

import io.netty.channel.ChannelHandlerContext;

public class Device {
	
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
	
	public static void queryCmd(String addr) {
		
	}
	
	public static void switchOffCmd(String addr) {
		
	}
	
	public static void switchOnCmd(String addr) {
		
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
