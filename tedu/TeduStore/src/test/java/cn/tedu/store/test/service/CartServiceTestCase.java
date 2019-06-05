package cn.tedu.store.test.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.entity.Cart;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.ex.ServiceException;

public class CartServiceTestCase {
	
	private AbstractApplicationContext ac;
	private ICartService service;
	
	@Test
	public void addToCart() {
		try {
			Cart cart = new Cart();
			cart.setUid(100);
			cart.setGoodsId("g2");
			cart.setGoodsNum(3);
			service.addToCart(cart);
			System.out.println("OK");
		}catch(ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml", "spring-service.xml");
		service = ac.getBean(
			"cartService",
			ICartService.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
