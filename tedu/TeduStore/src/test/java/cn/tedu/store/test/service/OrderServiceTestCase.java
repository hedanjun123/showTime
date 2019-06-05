package cn.tedu.store.test.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.service.IOrderService;
import cn.tedu.store.service.ex.ServiceException;

public class OrderServiceTestCase {
	
	private AbstractApplicationContext ac;
	private IOrderService service;
	
	@Test
	public void createOrder() {
		try {
			Integer uid = 3;
			Integer addressId = 8;
			Integer[] cartIds = {9,11,12};
			service.createOrder(uid, addressId, cartIds);
		System.out.println("OK");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml", "spring-service.xml");
		service = ac.getBean(
			"orderService",
			IOrderService.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
