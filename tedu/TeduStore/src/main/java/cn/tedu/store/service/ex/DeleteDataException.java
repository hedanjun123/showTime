package cn.tedu.store.service.ex;

/**
 * É¾³ýÊý¾ÝÒì³£
 */
public class DeleteDataException 
	extends ServiceException {

	private static final long serialVersionUID = -792199268035034026L;

	public DeleteDataException() {
		super();
	}

	public DeleteDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DeleteDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeleteDataException(String message) {
		super(message);
	}

	public DeleteDataException(Throwable cause) {
		super(cause);
	}

}
