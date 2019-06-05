package cn.tedu.store.test.service;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.store.entity.Address;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.ex.ServiceException;

public class AddressServiceTestCase {
	
	private AbstractApplicationContext ac;
	private IAddressService addressService;
	
	@Test
	public void delete() {
		try {
			Integer id = 1;
			Integer uid = 3;
			addressService.delete(id, uid);
			System.out.println("OK");
		} catch(ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void setDefaultAddress() {
		try {
			Integer id = 40;
			Integer uid = 3;
			addressService.setDefaultAddress(
				id, uid);
			System.out.println("设置成功！");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void getList() {
		Integer uid = 30;
		List<Address> addresses
			= addressService.getList(uid);
		
		System.out.println("BEGIN:");
		for (Address address : addresses) {
			System.out.println(address);
		}
		System.out.println("END.");
	}
	
	@Test
	public void addnew() {
		Address address = new Address();
		address.setUid(4);
		address.setRecvName("小刘同学");
		address.setRecvProvince("130000");
		address.setRecvCity("130100");
		address.setRecvArea("130103");
		Address result
			= addressService.addnew(address);
		System.out.println(result);
	}
	
	@Before
	public void doBefore() {
		ac = new ClassPathXmlApplicationContext(
			"spring-dao.xml", "spring-service.xml");
		addressService = ac.getBean(
			"addressService",
			IAddressService.class);
	}
	
	@After
	public void doAfter() {
		ac.close();
	}
}
