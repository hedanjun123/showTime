package cn.tedu.store.service;

public interface IOrderService {
	
	/**
	 * 
	 * @param uid
	 * @param addressId
	 * @param cartIds
	 */
	void createOrder(
			Integer uid, 
			Integer addressId, 
			Integer[] cartIds);

}
