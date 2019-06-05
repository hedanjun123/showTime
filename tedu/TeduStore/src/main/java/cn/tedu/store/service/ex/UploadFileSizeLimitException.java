package cn.tedu.store.service.ex;

/**
 * 上传文件大小超出限制
 */
public class UploadFileSizeLimitException 
	extends ServiceException {

	private static final long serialVersionUID = -3863019659720600823L;

	public UploadFileSizeLimitException() {
		super();
	}

	public UploadFileSizeLimitException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UploadFileSizeLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadFileSizeLimitException(String message) {
		super(message);
	}

	public UploadFileSizeLimitException(Throwable cause) {
		super(cause);
	}

}
