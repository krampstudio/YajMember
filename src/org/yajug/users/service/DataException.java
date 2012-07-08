package org.yajug.users.service;

public class DataException extends Exception {

	private static final long serialVersionUID = 9213425072541292816L;

	public DataException(String message){
		super(message);
	}
	
	public DataException(String message, Throwable t){
		super(message, t);
	}
}
