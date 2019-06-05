package cn.tedu.store.service.ex;

public class AddressAccessException 
	extends ServiceException {

	private static final long serialVersionUID = -1903708170445545003L;

	public AddressAccessException() {
		super();
	}

	public AddressAccessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AddressAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddressAccessException(String message) {
		super(message);
	}

	public AddressAccessException(Throwable cause) {
		super(cause);
	}

}
