package com.shicha.dianbiao.demon.netty;

import com.shicha.dianbiao.demon.domain.Command;

/**
 * receive response message from device
 * 
 * after receive response message:
 *   1. parse it
 *   2. post it back to host
*/
public class CmdRes {
	
	int status;
	String message;
	Object data;
	int ctlCode;
	String response;
	String addr;
	int cmdCode;
	
	public static int status_ok=0;
	public static int status_fail=1;
	
	public static int headLength=10;
	public static int tailLength=2;
	
	public static boolean hasEnoughBytes(byte[] buff) {
		if(buff.length < headLength)
			return false;
		
		int dataLen = buff[9] & 0xff;
		if(buff.length < dataLen + headLength +tailLength) {
			return false;
		}
		return true;
	}
	
	public static int getBodyLen(byte[] buff) {
		if(buff.length < headLength)
			return -1;
		
		int dataLen = buff[9] & 0xff;
		return dataLen;
	}
	
	public static String getAddr(byte[] buf) {
		if(buf.length < 7)
			return null;
		String h="";
		for(int i = 6; i >=1; i--) {
			String t = Integer.toHexString(buf[i] & 0xff);
			if(t.length() == 1)
				t = "0" + t;
			h += t;
		}
		return h;
	}
	
	public static CmdRes parse(byte[] buf) {
		if(!hasEnoughBytes(buf)) {
			return null;
		}
		
		CmdRes res = new CmdRes();
		int ctlCode = buf[8] & 0xff;
		int cmdCode = Command.getCmdCode(buf);
		String addr = getAddr(buf);
		
		res.setCtlCode(ctlCode);
		res.setAddr(addr);
		res.setResponse(Utils.byte2str(buf));
		
		int type = 1;
		Device d = Device.getDevice(addr);
		if(d != null )
			type = d.getType();
		
		switch(ctlCode) {
		
		case 0x91://read ok
			res.setStatus(status_ok);
			if(cmdCode == 0) {
				res.setData(new MeterData(buf, type));
				res.setCmdCode(Command.READ_METER);
			}else {
				res.setCmdCode(cmdCode);
				res.setData(buf);
			}
			break;
		
		case 0xd1://read fail
			res.setStatus(status_fail);
			res.setMessage("error code:"+ (buf[10] & 0xff));
			res.setCmdCode(Command.READ_METER);
			break;
			
		case 0x94:	//write ok
			res.setStatus(status_ok);
			res.setCmdCode(Command.WRITE_METER);
			break;
			
		case 0xd4: //write fail
			res.setStatus(status_fail);
			res.setMessage("error code:"+ (buf[10] & 0xff));
			res.setCmdCode(Command.WRITE_METER);
			break;
		
		case 0x9c://onoff ok
			res.setStatus(status_ok);
			res.setCmdCode(Command.OFF);
			break;
		
		case 0xdc://onff fail
			res.setStatus(status_fail);
			res.setMessage("error code:"+ (buf[10] & 0xff));	
			res.setCmdCode(Command.OFF);
			break;
			
		default:
			break;
		}
		
		
		return res;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getCtlCode() {
		return ctlCode;
	}

	public void setCtlCode(int ctlCode) {
		this.ctlCode = ctlCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getCmdCode() {
		return cmdCode;
	}

	public void setCmdCode(int cmdCode) {
		this.cmdCode = cmdCode;
	}
}
