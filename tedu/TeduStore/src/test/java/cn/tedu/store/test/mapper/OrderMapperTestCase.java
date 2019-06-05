package cn.tedu.store.test.mapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.mapper.OrderMapper;
import cn.tedu.store.vo.OrderVO;

public class OrderMapperTestCase {

	private AbstractApplicationContext ac;
	private OrderMapper mapper;
	
	
	@Test
	public void getOrderById() {
		Integer orderId = 1;
		OrderVO orderVO 
			= mapper.getOrderById(orderId);
		System.out.println(orderVO);
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml");
		mapper = ac.getBean("orderMapper",
			OrderMapper.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
