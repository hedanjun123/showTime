package cn.tedu.store.mapper;

import cn.tedu.store.entity.Order;
import cn.tedu.store.entity.OrderItem;
import cn.tedu.store.vo.OrderVO;

public interface OrderMapper {

	/**
	 * 创建订单
	 * @param order
	 * @return
	 */
	Integer insertOrder(Order order);

	/**
	 * 创建订单商品
	 * @param orderItem
	 * @return
	 */
	Integer insertOrderItem(OrderItem orderItem);
	
	/**
	 * 根据订单id查询订单详情
	 * @param orderId
	 * @return
	 */
	OrderVO getOrderById(Integer orderId);
	
}
