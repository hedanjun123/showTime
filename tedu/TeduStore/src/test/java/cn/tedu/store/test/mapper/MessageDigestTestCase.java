package cn.tedu.store.test.mapper;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class MessageDigestTestCase {

	public static void main(String[] args) {
		String password = "1234";
		
		String salt = UUID.randomUUID().toString();
		System.out.println("salt=" + salt + ", length=" + salt.length());
		salt = UUID.randomUUID().toString();
		System.out.println("salt=" + salt + ", length=" + salt.length());
		salt = UUID.randomUUID().toString();
		System.out.println("salt=" + salt + ", length=" + salt.length());
		salt = UUID.randomUUID().toString();
		System.out.println("salt=" + salt + ", length=" + salt.length());
		salt = UUID.randomUUID().toString();
		System.out.println("salt=" + salt + ", length=" + salt.length());
		
		String md5 = DigestUtils.md5Hex(password + salt).toUpperCase();
		System.out.println("第1次加密：" + md5);
		md5 = DigestUtils.md5Hex(md5).toUpperCase();
		System.out.println("第2次加密：" + md5);
		md5 = DigestUtils.md5Hex(md5).toUpperCase();
		System.out.println("第3次加密：" + md5);
		md5 = DigestUtils.md5Hex(md5).toUpperCase();
		System.out.println("第4次加密：" + md5);
		md5 = DigestUtils.md5Hex(md5).toUpperCase();
		System.out.println("第5次加密：" + md5);
		
		System.out.println(70L * 70 * 70 * 70 * 70 * 70);
		System.out.println(70L * 70 * 70 * 70 * 70 * 70 * 70);
	}
	
	// 70 * 70 * 70 * 70 * 70 * 70
	
	
	
}
