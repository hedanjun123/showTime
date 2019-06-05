package cn.tedu.store.test.mapper;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.entity.Goods;
import cn.tedu.store.mapper.GoodsMapper;

public class GoodsMapperTestCase {

	private AbstractApplicationContext ac;
	private GoodsMapper mapper;
	
	@Test
	public void getList() {
		String where = "id=10000027";
		String orderBy = "price DESC";
		Integer offset = 0;
		Integer count = 3;
		List<Goods> goodsList
			= mapper.getList(where, orderBy, offset, count);
		System.out.println("List(" + goodsList.size() + "):");
		for (Goods goods : goodsList) {
			System.out.println(goods);
		}
		System.out.println("End.");
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml");
		mapper = ac.getBean("goodsMapper",
			GoodsMapper.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
