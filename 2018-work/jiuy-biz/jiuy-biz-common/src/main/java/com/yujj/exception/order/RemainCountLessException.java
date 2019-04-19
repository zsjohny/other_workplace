package com.yujj.exception.order;

public class RemainCountLessException extends RuntimeException {

    private static final long serialVersionUID = 561193265320826772L;

    public RemainCountLessException() {
        super();
    }

    public RemainCountLessException(String message) {
        super(message);
    }

    public RemainCountLessException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemainCountLessException(Throwable cause) {
        super(cause);
    }
}
