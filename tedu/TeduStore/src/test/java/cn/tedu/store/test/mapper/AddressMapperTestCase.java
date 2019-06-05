package cn.tedu.store.test.mapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.entity.Address;
import cn.tedu.store.mapper.AddressMapper;

public class AddressMapperTestCase {

	private AbstractApplicationContext ac;
	private AddressMapper mapper;
	
	public void test() {
		AbstractApplicationContext ac
			= new ClassPathXmlApplicationContext("spring-mvc.xml");
		ac.getBean("");
		ac.close();
	}
	
	@Test
	public void getCountByUid() {
		Integer count
			= mapper.getCountByUid(3);
		System.out.println("count=" + count);
	}
	
	@Test
	public void insert() {
		Address address = new Address();
		address.setUid(3);
		address.setRecvName("刘老师");
		address.setIsDefault(0);
		address.setRecvProvince("新疆省");
		address.setRecvCity("乌鲁木齐");
		address.setRecvArea("阿克苏");
		address.setRecvDistrict("新疆省，乌鲁木齐，阿克苏");
		address.setRecvPhone("5660522");
		address.setRecvTag("家");
		address.setRecvZip("450000");
		address.setRecvTel("12345677654");
		address.setRecvAddress("紫金花园1210");
		Integer rows = mapper.insert(address);
		System.out.println("rows=" + rows);
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml");
		mapper = ac.getBean("addressMapper",
			AddressMapper.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
