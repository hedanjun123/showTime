package cn.tedu.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.store.entity.District;
import cn.tedu.store.entity.ResponseResult;
import cn.tedu.store.service.IDistrictService;

@Controller
@RequestMapping("/district")
public class DistrictController {

	@Autowired
	private IDistrictService districtService;
	
	@RequestMapping("/list.do")
	@ResponseBody
	public ResponseResult<List<District>> getList(
		@RequestParam("parent") String parent) {
		// 获取数据
		List<District> list
			= districtService.getList(parent);
		// 创建返回值对象
		ResponseResult<List<District>> rr
			= new ResponseResult<List<District>>();
		// 在返回对象中封装数据
		rr.setData(list);
		// 执行返回
		return rr;
	}
}








