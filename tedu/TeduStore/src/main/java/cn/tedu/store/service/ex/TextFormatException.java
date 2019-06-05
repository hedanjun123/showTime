package cn.tedu.store.service.ex;

/**
 * 文本格式异常
 * 
 * @see UsernameFormatException
 * @see PasswordFormatException
 */
public class TextFormatException
	extends ServiceException {

	private static final long serialVersionUID = -3027205226159738868L;

	public TextFormatException() {
		super();
	}

	public TextFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TextFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public TextFormatException(String message) {
		super(message);
	}

	public TextFormatException(Throwable cause) {
		super(cause);
	}

}
