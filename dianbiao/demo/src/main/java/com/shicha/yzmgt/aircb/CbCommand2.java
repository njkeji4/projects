package com.shicha.yzmgt.aircb;

import java.util.HashMap;

public class CbCommand2 {

	int cmd;
	HashMap<String,Object> param;
	
	public CbCommand2() {
		
	}
	
	public CbCommand2(int cmd, String addr, double data) {
		this.cmd = cmd;
		param = new HashMap<String, Object>();
		param.put("addr", addr);
		param.put("data", data);
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public HashMap<String, Object> getParam() {
		return param;
	}

	public void setParam(HashMap<String, Object> param) {
		this.param = param;
	}
	
	
}
