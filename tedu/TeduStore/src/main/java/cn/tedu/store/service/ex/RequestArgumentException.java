package cn.tedu.store.service.ex;

/**
 * 请求参数异常
 */
public class RequestArgumentException extends ServiceException {

	private static final long serialVersionUID = 7010868404336943576L;

	public RequestArgumentException() {
		super();
	}

	public RequestArgumentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RequestArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestArgumentException(String message) {
		super(message);
	}

	public RequestArgumentException(Throwable cause) {
		super(cause);
	}

}
