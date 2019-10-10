package com.shicha.yzmgt.security;

import org.springframework.security.authentication.AccountStatusException;

public class AccoutExceptionWithStatus extends AccountStatusException{

	public AccoutExceptionWithStatus(String msg) {
		super(msg);
	}

	/**
	 * Constructs a <code>AccountExpiredException</code> with the specified message and
	 * root cause.
	 *
	 * @param msg the detail message
	 * @param t root cause
	 */
	public AccoutExceptionWithStatus(String msg, Throwable t) {
		super(msg, t);
	}
	
	public AccoutExceptionWithStatus(String msg, int status) {
		super(msg);
		this.status = status;
	}
	
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
