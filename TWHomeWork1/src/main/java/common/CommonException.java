package common;

/**
 * description
 *
 * @author: hdj
 * @date: 2019-05-09 13:07
 */
public class CommonException extends RuntimeException{

    private String code;
    private String msg;

    /**
     * 默认未知异常
     */
    public CommonException(Throwable e) {
        this(ErrorCode.SYSTEM_EXCEPTION.getValue(), ErrorCode.SYSTEM_EXCEPTION.getDesc(), e);
    }

    /**
     * 枚举值异常
     */
    public CommonException(ErrorCode errorCode, Object... params) {
        this(errorCode.getValue(), errorCode.formatDesc(params), null);
    }

    /**
     * 根据错误码，模板参数和异常信息构造错误对象
     *
     * @param code
     * @param message
     * @param exception
     */
    public CommonException(String code, String message, Throwable exception) {
        super(message, exception);
        message = (message == null ? (exception == null ? null : exception.getMessage()) : message);
        setCode(code);
        setMsg(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
