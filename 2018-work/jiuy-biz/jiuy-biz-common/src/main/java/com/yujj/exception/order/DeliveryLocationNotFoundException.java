package com.yujj.exception.order;

public class DeliveryLocationNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4302372136281526954L;
	
    public DeliveryLocationNotFoundException() {
        super();
    }

    public DeliveryLocationNotFoundException(String message) {
        super(message);
    }

    public DeliveryLocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeliveryLocationNotFoundException(Throwable cause) {
        super(cause);
    }
}
