package org.yajug.users.service;

import javax.validation.ConstraintViolationException;

public class ValidationException extends DataException {

	private static final long serialVersionUID = 5447681997325522722L;

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Throwable t){
		super(message, t);
	}
	
	public ValidationException(ConstraintViolationException cve){
		super(cve.toString(), cve);
	}
}
