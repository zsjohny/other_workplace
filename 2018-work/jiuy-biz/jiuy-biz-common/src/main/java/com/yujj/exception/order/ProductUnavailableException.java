package com.yujj.exception.order;

public class ProductUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 561193265320826772L;

    public ProductUnavailableException() {
        super();
    }

    public ProductUnavailableException(String message) {
        super(message);
    }

    public ProductUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductUnavailableException(Throwable cause) {
        super(cause);
    }
}
