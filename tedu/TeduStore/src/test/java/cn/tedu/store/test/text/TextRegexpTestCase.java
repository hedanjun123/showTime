package cn.tedu.store.test.text;

import org.junit.Test;

public class TextRegexpTestCase {
	
	@Test
	public void testUsername() {
		String username = "_a12";
		String regexp = "[a-zA-Z]{1}[a-zA-Z0-9_]{3,15}";
		boolean result = username.matches(regexp);
		System.out.println(result);
	}

}



