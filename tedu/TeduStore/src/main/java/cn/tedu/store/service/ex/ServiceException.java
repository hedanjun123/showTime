package cn.tedu.store.service.ex;

/**
 * 业务异常，是当前项目中所有异常类的基类，通常，不直接抛出此类异常
 */
public class ServiceException 
	extends RuntimeException {

	private static final long serialVersionUID = 980104530291206274L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}
