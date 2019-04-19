package com.jiuy.core.exception;

public class ParameterErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2519534275916365312L;
	
    public ParameterErrorException() {
        super();
    }

    public ParameterErrorException(String message) {
        super(message);
    }

    public ParameterErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterErrorException(Throwable cause) {
        super(cause);
    }
	
}
