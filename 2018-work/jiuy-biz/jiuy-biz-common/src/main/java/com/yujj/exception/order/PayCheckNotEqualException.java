package com.yujj.exception.order;

public class PayCheckNotEqualException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2887395164906238437L;
	
    public PayCheckNotEqualException() {
        super();
    }

    public PayCheckNotEqualException(String message) {
        super(message);
    }

    public PayCheckNotEqualException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayCheckNotEqualException(Throwable cause) {
        super(cause);
    }
}
