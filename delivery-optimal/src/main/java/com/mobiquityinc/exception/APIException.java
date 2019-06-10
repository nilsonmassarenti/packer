package com.mobiquityinc.exception;

public class APIException extends RuntimeException {

	private static final long serialVersionUID = 5199629890017767754L;

	public APIException (String message) {
		super(message);
	}
	
	public APIException(String message, Exception e) {
		super(message, e);

	}
}
