package com.yujj.exception.order;

public class UserCoinLessException extends RuntimeException {

    private static final long serialVersionUID = 561193265320826772L;

    public UserCoinLessException() {
        super();
    }

    public UserCoinLessException(String message) {
        super(message);
    }

    public UserCoinLessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCoinLessException(Throwable cause) {
        super(cause);
    }
}
