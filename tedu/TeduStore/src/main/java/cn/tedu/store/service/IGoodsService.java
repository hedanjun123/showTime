package cn.tedu.store.service;

import java.util.List;

import cn.tedu.store.entity.Goods;

public interface IGoodsService {
	
	/**
	 * 根据id查询商品数据
	 * @param id 商品的id
	 * @return 匹配的商品数据，如果没有匹配的数据，则返回null
	 */
	Goods getGoodsById(String id);

	/**
	 * 获取热销商品列表
	 * @return 热度最高的4件商品的列表
	 */
	List<Goods> getHotGoodsList();
}
