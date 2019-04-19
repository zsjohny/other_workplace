package com.jiuy.core.exception;

/***************
 * 服务器未知错误
 * @author zhuliming
 *
 */
public class ServerUnknownException extends RuntimeException {

	private static final long serialVersionUID = -4119579995770420749L;

	public ServerUnknownException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerUnknownException(String message) {
		super(message);
	}

	public ServerUnknownException(Throwable cause) {
		super(cause);
	}	

}
