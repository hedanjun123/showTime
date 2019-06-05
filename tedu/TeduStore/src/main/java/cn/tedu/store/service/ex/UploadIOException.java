package cn.tedu.store.service.ex;

/**
 * 上传文件读写异常
 */
public class UploadIOException extends ServiceException {

	private static final long serialVersionUID = 5167978730556664196L;

	public UploadIOException() {
		super();
	}

	public UploadIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UploadIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadIOException(String message) {
		super(message);
	}

	public UploadIOException(Throwable cause) {
		super(cause);
	}
	
}
