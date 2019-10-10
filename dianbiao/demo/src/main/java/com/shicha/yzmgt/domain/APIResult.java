package com.shicha.yzmgt.domain;

public class APIResult {

	int status;
	String message;
	Object data;
	
	public APIResult() {}
	
	public APIResult(int s) {
		status = s;
		message = null;
		data = null;
	}
	
	public APIResult(int s,String message) {
		status = s;
		this.message = message;
		data = null;
	}
	
	public APIResult(int s, String m, Object d) {
		status = s;
		message = m;
		data = d;
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
}
