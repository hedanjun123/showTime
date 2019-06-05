package cn.tedu.store.test.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.ServiceException;

public class UserServiceTestCase {
	
	private AbstractApplicationContext ac;
	private IUserService userService;
	
	@Test
	public void changeInfo() {
		try {
			User user = new User();
			user.setId(50);
			user.setPhone("13000130001");
			userService.changeInfo(user);
			System.out.println("修改成功！");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void changePassword() {
		try {
			Integer id = 3;
			String oldPassword = "8888";
			String newPassword = "1234";
		userService.changePassword(
				id, oldPassword, newPassword);
		System.out.println("修改成功！");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void login() {
		try {
			String username = "hedanjun";
			String password = "1235";
			User user = userService.login(username, password);
			System.out.println("user=" + user);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void reg() {
		try {
			User user = new User();
			user.setUsername("aspect");
			user.setPassword("1234");
			User result = userService.reg(user);
			System.out.println(result);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml", "spring-service.xml",
			"spring-aop.xml");
		userService = ac.getBean("userService",
			IUserService.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
