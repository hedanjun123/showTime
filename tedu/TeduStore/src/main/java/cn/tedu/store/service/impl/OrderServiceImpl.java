package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.store.entity.Address;
import cn.tedu.store.entity.Order;
import cn.tedu.store.entity.OrderItem;
import cn.tedu.store.mapper.OrderMapper;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.IOrderService;
import cn.tedu.store.service.ex.InsertDataException;
import cn.tedu.store.vo.CartVO;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {

	@Autowired 
	private IAddressService addressService;
	
	@Autowired 
	private ICartService cartService;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Transactional
	public void createOrder(Integer uid, Integer addressId, Integer[] cartIds) {
		// 获取当前时间
		Date now = new Date();
		
		// 根据cartIds获取商品相关数据
		List<CartVO> carts = cartService.getListByIds(uid, cartIds);
		// 计算总金额
		Long pay = 0L;
		for (CartVO cartVO : carts) {
			pay += cartVO.getGoodsPrice() * cartVO.getGoodsNum();
		}

		// 根据addressId获取收货地址数据
		Address address = addressService.getAddressById(addressId);
		// 准备插入订单数据
		Order order = new Order();
		order.setUid(uid);
		order.setRecvName(address.getRecvName());
		order.setRecvAddress(address.getRecvAddress());
		order.setRecvDistrict(address.getRecvDistrict());
		order.setRecvPhone(address.getRecvPhone());
		order.setRecvZip(address.getRecvZip());
		order.setStatus(0); // 0-未支付
		order.setOrderTime(now);
		order.setPayTime(null);
		order.setPay(pay);

		// 执行：插入订单数据
		insertOrder(order);

		// 执行：插入订单商品数据
		for (CartVO cartVO : carts) {
			OrderItem item = new OrderItem();
			item.setOrderId(order.getId());
			item.setGoodsId(cartVO.getGoodsId());
			item.setGoodsImage(cartVO.getGoodsImage());
			item.setGoodsNum(cartVO.getGoodsNum());
			item.setGoodsPrice(cartVO.getGoodsPrice());
			item.setGoodsTitle(cartVO.getGoodsTitle());
			insertOrderItem(item);
		}
		
		// TODO 根据参数Integer[] cartIds读取到的goods_id和goods_num，更新t_goods表中商品的库存

		// TODO 根据参数Integer[] cartIds删除购物车中对应的数据
	}
	
	private void insertOrder(Order order) {
		Integer rows
			= orderMapper.insertOrder(order);
		if (rows != 1) {
			throw new InsertDataException(
				"");
		}
	}

	private void insertOrderItem(OrderItem orderItem) {
		Integer rows
			= orderMapper.insertOrderItem(orderItem);
		if (rows != 1) {
			throw new InsertDataException(
				"");
	}
	}

}
