package br.com.murilo.service.exception;

public class ObjectNotEnabledException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ObjectNotEnabledException(String message) {
		super(message);
	}

}
