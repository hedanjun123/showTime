package cn.tedu.store.mapper;

import java.util.List;

import cn.tedu.store.entity.Address;

public interface AddressMapper {
	
	/**
	 * 插入收货地址数据
	 * @param address 收货地址数据
	 * @return 受影响的行数
	 */
	Integer insert(Address address);
	
	/**
	 * 获取某用户的收货地址数据的数量
	 * @param uid 用户的id
	 * @return 用户的收货地址数据的数量
	 */
	Integer getCountByUid(Integer uid);

	/**
	 * 获取某用户的收货地址列表
	 * @param uid 用户的id
	 * @return 收货地址列表，如果没有匹配的数据，则返回无元素的空列表
	 */
	List<Address> getList(Integer uid);
	
	/**
	 * 将某用户的所有收货地址设置为非默认收货地址
	 * @param uid 用户id
	 * @return 受影响的行数
	 */
	Integer setNonDefault(Integer uid);

	/**
	 * 将指定id的收货地址设置为默认收货地址
	 * @param id 收货地址数据id
	 * @return 受影响的行数
	 */
	Integer setDefault(Integer id);

	/**
	 * 根据id获取收货地址数据
	 * @param id 收货地址数据id
	 * @return 收货地址数据，如果没有匹配的数据，则返回null。
	 */
	Address findAddressById(Integer id);
	
	/**
	 * 根据id删除用户地址
	 * @param id 收货地址数据id
	 * @return 受影响的行数
	 */
	Integer deleteById(Integer id);

	
	/**
	 * 根据用户uid找到id最大的收货地址
	 * @param uid
	 * @return
	 */
	Integer getMaxId(Integer uid);
}




