package com.shicha.yzmgt.aircb;

public class AirResult {

	int cmd;
	boolean cmdStat;
	String msg;
	Object data;
		
	public AirResult() {
		
	}
	
	public AirResult(int cmd) {
		this.cmd = cmd;
		cmdStat = true;
		msg="ok";
	}
	
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public boolean isCmdStat() {
		return cmdStat;
	}
	public void setCmdStat(boolean cmdStat) {
		this.cmdStat = cmdStat;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
