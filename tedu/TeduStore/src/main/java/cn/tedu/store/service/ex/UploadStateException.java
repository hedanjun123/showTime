package cn.tedu.store.service.ex;

/**
 * ÉÏ´«×´Ì¬Òì³£
 */
public class UploadStateException
	extends ServiceException {

	private static final long serialVersionUID = -863949202052276565L;

	public UploadStateException() {
		super();
	}

	public UploadStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UploadStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadStateException(String message) {
		super(message);
	}

	public UploadStateException(Throwable cause) {
		super(cause);
	}

}
