package util;

import common.CommonException;
import common.ErrorCode;

/**
 * description
 *
 * @author: hdj
 * @date: 2019-05-09 13:53
 */
public class StringUtils {
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }

    public static Integer strToInteger(String str) {
        if (StringUtils.isBlank(str)) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "空字符串无法转换成Integer");
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "字符串["+str+"]无法转换成Integer");
        }
    }
}
