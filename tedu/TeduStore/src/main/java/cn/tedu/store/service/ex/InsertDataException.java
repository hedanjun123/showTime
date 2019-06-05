package cn.tedu.store.service.ex;

/**
 * 插入数据异常，通常，在获取持久层INSERT操作后返回值异常时抛出
 */
public class InsertDataException 
	extends ServiceException {

	private static final long serialVersionUID = -7724774432291553025L;

	public InsertDataException() {
		super();
	}

	public InsertDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InsertDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsertDataException(String message) {
		super(message);
	}

	public InsertDataException(Throwable cause) {
		super(cause);
	}

}
