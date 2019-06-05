package cn.tedu.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tedu.store.entity.Goods;

public interface GoodsMapper {
	
	/**
	 * 根据id查询商品数据
	 * @param id 商品的id
	 * @return 匹配的商品数据，如果没有匹配的数据，则返回null
	 */
	Goods findGoodsById(String id);

	/**
	 * 获取商品列表
	 * @param where 查询语句中的WHERE子句，不包括WHERE关键字
	 * @param orderBy 查询语句中的ORDER BY子句，不包括ORDER BY关键字
	 * @param offset 查询时，在所有的查询结果中，跳过前多少条再开始获取数据
	 * @param count 最多查询多少条数据
	 * @return 商品列表
	 */
	List<Goods> getList(
		@Param("where") String where,
		@Param("orderBy") String orderBy,
		@Param("offset") Integer offset,
		@Param("count") Integer count
	);
	
}
