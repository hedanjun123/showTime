package cn.tedu.store.test.mapper;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.vo.CartVO;

public class CartMapperTestCase {

	private AbstractApplicationContext ac;
	private CartMapper mapper;
	
	@Test
	public void getListByIds() {
		Integer[] ids = { 9, 11, 12 };
		List<CartVO> list 
			= mapper.getListByIds(ids);
		System.out.println("Begin:");
		for (CartVO cartVO : list) {
			System.out.println(cartVO);
		}
		System.out.println("End.");
	}
	
	@Test
	public void getList() {
		Integer uid = 3;
		List<CartVO> list = mapper.getList(uid);
		System.out.println("Begin:");
		for (CartVO cartVO : list) {
			System.out.println(cartVO);
		}
		System.out.println("End.");
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml");
		mapper = ac.getBean("cartMapper",
			CartMapper.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
