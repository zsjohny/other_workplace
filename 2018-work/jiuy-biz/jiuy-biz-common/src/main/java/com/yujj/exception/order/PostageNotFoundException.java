package com.yujj.exception.order;

public class PostageNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8119348209170776257L;
	
    public PostageNotFoundException() {
        super();
    }

    public PostageNotFoundException(String message) {
        super(message);
    }

    public PostageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostageNotFoundException(Throwable cause) {
        super(cause);
    }
}
