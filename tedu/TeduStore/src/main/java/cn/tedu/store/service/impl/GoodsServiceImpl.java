package cn.tedu.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.store.entity.Goods;
import cn.tedu.store.mapper.GoodsMapper;
import cn.tedu.store.service.IGoodsService;

@Service("goodsService")
public class GoodsServiceImpl implements IGoodsService {
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	public Goods getGoodsById(String id) {
		return findGoodsById(id);
	}
	
	public List<Goods> getHotGoodsList() {
		return getList(null, "priority DESC", 0, 4);
	}
	
	private List<Goods> getList(String where, String orderBy, Integer offset, Integer count) {
		return goodsMapper.getList(where, orderBy, offset, count);
	}
	
	private Goods findGoodsById(String id) {
		return goodsMapper.findGoodsById(id);
	}

}





