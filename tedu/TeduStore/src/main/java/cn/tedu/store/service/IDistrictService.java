package cn.tedu.store.service;

import java.util.List;

import cn.tedu.store.entity.District;

/**
 * 省、市、区的数据业务层
 */
public interface IDistrictService {

	/**
	 * 根据省、市、区的代号获取详细数据
	 * @param code 省、市、区的代号
	 * @return 省、市、区的详细数据
	 */
	District getDistrictByCode(String code);
	
	/**
	 * 获取省的列表/某省的市的列表/某市的区的列表
	 * @param parent 父级单位的代号，如果需要获取省的列表，则父级单位的代号固定是86
	 * @return 省列表/市列表/区列表
	 */
	List<District> getList(String parent);
	
}
