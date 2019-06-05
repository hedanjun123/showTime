package cn.tedu.store.util;

/**
 * 文本验证工具类
 */
public final class TextValidator {
	
	/**
	 * 私有化构造方法，不希望被创建对象
	 */
	private TextValidator() {
		super();
	}
	
	/**
	 * 验证用户名的正则表达式
	 */
	private static final String REGEX_USERNAME 
		= "[a-zA-Z]{1}[a-zA-Z0-9_]{3,15}";
	/**
	 * 验证手机号码的正则表达式
	 */
	private static final String REGEX_PHONE 
		= "[0-9]{11}";
	/**
	 * 验证电子邮箱的正则表达式
	 */
	private static final String REGEX_EMAIL 
		= "[@]{1}[a-zA-Z0-9]+[.]+[a-z]+";

	/**
	 * 验证用户名格式
	 * @param username 用户名
	 * @return 返回true时表示符合格式要求，返回false时表示不符合格式要求
	 */
	public static boolean checkUsername(String username) {
		if (username == null) {
			return false;
		}
		return username.matches(REGEX_USERNAME);
	}
	
	/**
	 * 验证密码格式
	 * @param password 密码
	 * @return 返回true时表示符合格式要求，返回false时表示不符合格式要求
	 */
	public static boolean checkPassword(String password) {
		if (password == null) {
			return false;
		}
		return password.length() >= 4 
				&& password.length() <= 16;
	}
	
	/**
	 * 验证手机号码格式
	 * @param phone 手机号码
	 * @return 返回true时表示符合格式要求，返回false时表示不符合格式要求
	 */
	public static boolean checkPhone(String phone) {
		if (phone == null) {
			return false;
		}
		return phone.matches(REGEX_PHONE);
	}
	
	/**
	 * 验证电子邮箱格式
	 * @param email 电子邮箱
	 * @return 返回true时表示符合格式要求，返回false时表示不符合格式要求
	 */
	public static boolean checkEmail(String email) {
		if (email == null) {
			return false;
		}
		return email.matches(REGEX_EMAIL);
	}
	
}
