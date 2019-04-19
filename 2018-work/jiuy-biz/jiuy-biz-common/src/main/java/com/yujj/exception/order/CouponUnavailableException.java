package com.yujj.exception.order;

public class CouponUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 561193265320826772L;

    public CouponUnavailableException() {
        super();
    }

    public CouponUnavailableException(String message) {
        super(message);
    }

    public CouponUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponUnavailableException(Throwable cause) {
        super(cause);
    }
}
