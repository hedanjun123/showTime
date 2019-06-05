package cn.tedu.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.store.entity.Goods;
import cn.tedu.store.entity.ResponseResult;
import cn.tedu.store.service.IGoodsService;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	private IGoodsService goodsService;
	
	@RequestMapping("/hot_list.do")
	@ResponseBody
	public ResponseResult<List<Goods>> getHotList() {
		ResponseResult<List<Goods>> rr
			= new ResponseResult<List<Goods>>();
		List<Goods> list = goodsService.getHotGoodsList();
		rr.setData(list);
		return rr;
	}
	
	@RequestMapping("/details.do")
	@ResponseBody
	public ResponseResult<Goods> getDetails(
		@RequestParam("id") String id) {
		Goods goods = goodsService.getGoodsById(id);
		ResponseResult<Goods> rr
			= new ResponseResult<Goods>();
		rr.setData(goods);
		return rr;
	}

}
