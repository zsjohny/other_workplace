package com.yujj.exception;

public class ParameterErrorException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1117749397662321870L;

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
