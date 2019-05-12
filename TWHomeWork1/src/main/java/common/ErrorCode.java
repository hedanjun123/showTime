package common;

/**
 * description
 *
 * @author: hdj
 * @date: 2019-05-09 13:10
 */
public enum  ErrorCode {

    SYSTEM_EXCEPTION    ("400", "系统自定义异常！"),
    FILE_NOT_FOUND      ("401", "文件[%s]未找到！"),
    SYSTEM_ERROR        ("402", "系统错误[%s]！"),
    FAIL_CLOSE_FILE     ("403", "关闭文件失败！"),
    PARAM_IS_NULL       ("404", "参数不能为空！"),

    ;

    /**
     * code值
     */
    private final String value;
    /**
     *  code值文字说明
     */
    private final String desc;

    ErrorCode(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getValue() {
        return value;
    }

    public String formatDesc(Object... params){
        return String.format(this.desc, params);
    }
}
