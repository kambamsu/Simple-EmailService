package com.praveen.emailservice.exceptions;

public class IllegalRequestException extends RuntimeException {
	String message;
	
	public IllegalRequestException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7991505046663930387L;

	public String getMessage() {
		return message;
	}

}
