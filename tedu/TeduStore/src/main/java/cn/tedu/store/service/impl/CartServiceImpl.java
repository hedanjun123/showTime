package cn.tedu.store.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.store.entity.Cart;
import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.ex.CartNotFoundException;
import cn.tedu.store.service.ex.GoodsNumLimitException;
import cn.tedu.store.service.ex.InsertDataException;
import cn.tedu.store.service.ex.UpdateDataException;
import cn.tedu.store.vo.CartVO;

@Service("cartService")
public class CartServiceImpl implements ICartService {
	
	@Autowired
	private CartMapper cartMapper;

	public void addToCart(Cart cart) {
		// 根据cart参数中封装的uid和goodsId执行查询
		Cart data = findCartByUidAndGoodsId(
				cart.getUid(), 
				cart.getGoodsId());
		// 判断结果是否为null
		if (data == null) {
			// 是：该用户此前没有添加该商品，则执行插入数据
			insert(cart);
		} else {
			// 否：该用户已经添加该商品，获取原有数量
			Integer n = data.getGoodsNum();
			// 从cart参数中获取此次的增量，并计算得到新的数量
			Integer newGoodsNum = cart.getGoodsNum() + n;
			// 更新商品数量
			updateGoodsNum(data.getId(), newGoodsNum);
		}
	}

	public List<CartVO> getCartListByUid(Integer uid) {
		return getList(uid);
	}
	
	public List<CartVO> getListByIds(
			Integer uid,
			Integer[] ids) {
		List<CartVO> list = getListByIds(ids);
		Iterator<CartVO> it = list.iterator();
		while(it.hasNext()) {
			if (!it.next().getUid().equals(uid)) {
				it.remove();
			}
		}
		return list;
	}
	
	private List<CartVO> getListByIds(Integer[] ids) {
		return cartMapper.getListByIds(ids);
	}
	
	public void addNum(Integer id) {
		Cart cart = findCartById(id);
		if (cart == null) {
			throw new CartNotFoundException(
				"尝试访问的购物车数据不存在！");
		}
		Integer num = cart.getGoodsNum() + 1;
		updateGoodsNum(id, num);
	}
	
	public void reduceNum(Integer id) {
		Cart cart = findCartById(id);
		if (cart == null) {
			throw new CartNotFoundException(
				"尝试访问的购物车数据不存在！");
		}
		if(cart.getGoodsNum() <= 1) {
			throw new GoodsNumLimitException(
				"尝试修改的购物车数据的商品数量招出限制！");
		}
		Integer num = cart.getGoodsNum() - 1;
		updateGoodsNum(id, num);
	}

	/**
	 * 插入数据
	 * @param cart
	 * @return
	 */
	private Cart insert(Cart cart) {
		Integer rows = cartMapper.insert(cart);
		if (rows == 1) {
			return cart;
		} else {
			throw new InsertDataException(
				"向购物车表中添加新数据时出现未知错误，请联系系统管理员！");
		}
	}

	/**
	 * 查询
	 * @param uid
	 * @param goodsId
	 * @return
	 */
	private Cart findCartByUidAndGoodsId(
		Integer uid, String goodsId) {
		return cartMapper.findCartByUidAndGoodsId(
				uid, goodsId);
	}

	/**
	 * 修改数量
	 * @param id
	 * @param goodsNum
	 * @return
	 */
	private void updateGoodsNum(
		Integer id, Integer goodsNum) {
		Integer rows = cartMapper.updateGoodsNum(id, goodsNum);
		if (rows != 1) {
			throw new UpdateDataException(
					"修改购物车中的商品数量时出现未知错误，请联系系统管理员！");
		}
	}

	private List<CartVO> getList(Integer uid) {
		return cartMapper.getList(uid);
	}
	
	private Cart findCartById(Integer id) {
		return cartMapper.findCartById(id);
	}

	
}
