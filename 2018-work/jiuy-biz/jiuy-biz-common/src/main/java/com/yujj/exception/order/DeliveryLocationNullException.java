package com.yujj.exception.order;

public class DeliveryLocationNullException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3510467966228342121L;

    public DeliveryLocationNullException() {
        super();
    }

    public DeliveryLocationNullException(String message) {
        super(message);
    }

    public DeliveryLocationNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeliveryLocationNullException(Throwable cause) {
        super(cause);
    }
}
