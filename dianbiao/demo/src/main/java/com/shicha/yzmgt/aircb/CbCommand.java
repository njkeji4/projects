package com.shicha.yzmgt.aircb;

import java.util.HashMap;

public class CbCommand {

	int cmd;
	Object param;
	
	public CbCommand() {
		
	}
	
	public CbCommand(int cmd, String param) {
		this.cmd = cmd;
		this.param = param;
	}
	
	public CbCommand(int cmd, String addr, double data) {
		this.cmd = cmd;
		HashMap<String, Object> t = new HashMap<String, Object>();
		t.put("addr", addr);
		t.put("data", data);
		param = t;
	}
	
	
	public CbCommand(int cmd, String addr, String data) {
		this.cmd = cmd;
		HashMap<String, Object> t = new HashMap<String, Object>();
		t.put("addr", addr);
		t.put("data", data);
		param = t;
	}
	

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}
	
}
