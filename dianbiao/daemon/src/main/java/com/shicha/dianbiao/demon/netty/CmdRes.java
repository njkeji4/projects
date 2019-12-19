package com.shicha.dianbiao.demon.netty;

//message from device after send a request
public class CmdRes {
	
	int status;
	String message;
	Object data;
	int ctlCode;
	String response;
	String addr;
	
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
		for(int i = 6; i >=0; i--) {
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
		String addr = getAddr(buf);
		
		res.setCtlCode(ctlCode);
		res.setAddr(addr);
		res.setResponse(Utils.byte2str(buf));
		
		switch(ctlCode) {
		
		case 0x91://query ok
			res.setStatus(status_ok);
			res.setData(new MeterData(buf));
			break;
		
		case 0xd1://query fail
			res.setStatus(status_fail);
			res.setMessage("error code:"+ (buf[10] & 0xff));
			break;
		
		case 0x9c://onoff ok
			res.setStatus(status_ok);
			break;
		
		case 0xdc://onff fail
			res.setStatus(status_fail);
			res.setMessage("error code:"+ (buf[10] & 0xff));
			
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
}
