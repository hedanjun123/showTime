package cn.tedu.store.service.ex;

/**
 * √‹¬Î∏Ò Ω“Ï≥£
 * 
 * @see TextFormatException
 */
public class PasswordFormatException
	extends TextFormatException {

	private static final long serialVersionUID = 2586082648292753165L;

	public PasswordFormatException() {
		super();
	}

	public PasswordFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PasswordFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordFormatException(String message) {
		super(message);
	}

	public PasswordFormatException(Throwable cause) {
		super(cause);
	}

}
