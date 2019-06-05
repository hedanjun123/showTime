package cn.tedu.store.service;

import java.util.List;

import cn.tedu.store.entity.Address;
import cn.tedu.store.service.ex.AddressAccessException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.DeleteDataException;
import cn.tedu.store.service.ex.UpdateDataException;

/**
 * 收货地址的数据业务层
 */
public interface IAddressService {

	/**
	 * 增加新的收货地址
	 * @param address 收货地址数据
	 * @return 成功增加的收货地址数据，包括数据的id
	 */
	Address addnew(Address address);
	
	/**
	 * 获取某用户的收货地址列表
	 * @param uid 用户的id
	 * @return 收货地址列表，如果没有匹配的数据，则返回无元素的空列表
	 */
	List<Address> getList(Integer uid);
	
	/**
	 * 设置默认收货地址
	 * @param id 默认的收货地址的id
	 * @param uid 地址信息归属的用户
	 * @throws AddressNotFoundException 尝试访问的收货地址数据不存在
	 * @throws AddressAccessException 收货地址数据归属错误
	 * @throws UpdateDataException 更新数据异常
	 */
	void setDefaultAddress(
		Integer id, Integer uid) 
			throws AddressNotFoundException,
				AddressAccessException, 
				UpdateDataException;
	
	/**
	 * 
	 * @param id
	 * @param uid
	 * @throws AddressNotFoundException
	 * @throws AddressAccessException
	 * @throws DeleteDataException
	 */
	void delete(Integer id, Integer uid)
		throws AddressNotFoundException,
			AddressAccessException,
			DeleteDataException;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Address getAddressById(Integer id);;
}




