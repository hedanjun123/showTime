### 38. 收货地址-删除-持久层

**1. 分析SQL语句**

执行删除的SQL语句：

	DELETE FROM t_address WHERE aid=?

与“设为默认”相同，在执行删除之前还应该检查数据是否存在，及数据归属是否正确，该功能已经完成，无需再次开发。

另外，如果删除的收货地址是默认收货地址，则：如果仍有收货地址数据，将最后一次修改的收货地址设置为默认。对应：

- 如何判断刚才删除的是默认收货地址？由于删除之前本来就会查询，则在原有的查询功能中，添加查询is_default字段即可！

- 如何判断删除后是否仍有收货地址数据？通过原有的`countByUid(uid)`，如果统计结果为0，则没有数据了，即刚才删除的就是最后一条，如果统计结果不为0，则表示删除后仍有数据！

- 如何得到最后一次修改的收货地址？对应的SQL语句是：`SELECT aid FROM t_address WHERE uid=? ORDER BY modified_time DESC, aid DESC LIMIT 0,1`

- 如何把某地址设为默认？通过现有的`updateNonDefault(uid)`和`updateDefault(aid)`即可实现。

**2. 接口与抽象方法**

在持久层接口中添加新的抽象方法：

	Integer deleteByAid(Integer aid);

	Address findLastModified(Integer uid);

**3. 配置映射**

	<!-- 根据id删除收货地址数据 -->
	<!-- Integer deleteByAid(Integer aid) -->
	<delete id="deleteByAid">
		DELETE FROM t_address WHERE aid=#{aid}
	</delete>

	<!-- 获取某用户最后修改的收货地址数据 -->
	<!-- Address findLastModified(Integer uid) -->
	<select id="findLastModified"
		resultType="cn.tedu.store.entity.Address">
		SELECT 
			aid
		FROM 
			t_address 
		WHERE 
			uid=#{uid}
		ORDER BY
			modified_time DESC, aid DESC
		LIMIT
			0,1
	</select>

单元测试：

	@Test
	public void deleteByAid() {
		Integer aid = 8;
		Integer rows = mapper.deleteByAid(aid);
		System.err.println("rows=" + rows);
	}

	@Test
	public void findLastModified() {
		Integer uid = 8;
		Address data = mapper.findLastModified(uid);
		System.err.println(data);
	}

### 39. 收货地址-删除-业务层

**1. 设计异常**

删除业务的大致流程应该是：先检查数据是否存在 > 检查数据归属是否正确 > 执行删除 > 检查删除的数据是否是默认地址 > 检查是否还有数据 > 获取最后修改的数据 > 将全部数据设置为非默认 > 将最后修改的数据设置为默认。

在以上操作中可能涉及的异常：

- 先检查数据是否存在：`AddressNotFoundException`

- 检查数据归属是否正确：`AccessDeniedException`

- 执行删除：`DeleteException`

- 检查删除的数据是否是默认地址：无

- 检查是否还有数据：无

- 获取最后修改的数据：无

- 将全部数据设置为非默认：`UpdateException`

- 将最后修改的数据设置为默认：`UpdateException`

**2. 接口与抽象方法**

	void delete(Integer uid, Integer aid) throws AddressNotFoundException, AccessDeniedException, DeleteException, UpdateException;

**3. 实现**

先私有化添加持久层中的新方法：

	/**
	 * 根据id删除收货地址数据
	 * @param aid 将删除的收货地址数据的id
	 * @return 受影响的行数
	 */
	private void deleteByAid(Integer aid) {
		Integer rows = addressMapper.deleteByAid(aid);
		if (rows != 1) {
			throw new DeleteException(
				"删除收货地址时出现未知错误！");
		}
	}

	/**
	 * 获取某用户最后修改的收货地址数据
	 * @param uid 用户的id
	 * @return 该用户最后修改的收货地址数据
	 */
	private Address findLastModified(Integer uid) {
		return addressMapper.findLastModified(uid);
	}

重写接口中的抽象方法：

	@Transactional
	public void delete(Integer uid, Integer aid) throws AddressNotFoundException, AccessDeniedException, DeleteException, UpdateException {
		// 根据aid查询即将删除的数据
		// 判断查询结果是否为null
		// 是：AddressNotFoundException

		// 检查数据归属是否不正确
		// 是：AccessDeniedException

		// 执行删除

		// 检查此前的查询结果中的isDefault是否为0
		// return;

		// 获取当前用户的收货地址数据的数量
		// 判断数量是否为0
		// return;

		// 获取当前用户最后修改的收货地址数据
		// 将全部数据设置为非默认
		// 将最后修改的数据设置为默认。
	}

具体实现：

	@Override
	@Transactional
	public void delete(Integer uid, Integer aid)
			throws AddressNotFoundException, AccessDeniedException, DeleteException, UpdateException {
		// 根据aid查询即将删除的数据
		Address result = findByAid(aid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：AddressNotFoundException
			throw new AddressNotFoundException(
				"删除收货地址失败！尝试访问的数据不存在！");
		}

		// 检查数据归属是否不正确
		if (!result.getUid().equals(uid)) {
			// 是：AccessDeniedException
			throw new AccessDeniedException(
				"删除收货地址失败！数据归属错误！");
		}

		// 执行删除
		deleteByAid(aid);

		// 检查此前的查询结果中的isDefault是否为0
		if (result.getIsDefault().equals(0)) {
			return;
		}

		// 获取当前用户的收货地址数据的数量
		Integer count = countByUid(uid);
		// 判断数量是否为0
		if (count.equals(0)) {
			return;
		}

		// 获取当前用户最后修改的收货地址数据
		Address lastModifed = findLastModified(uid);
		// 将全部数据设置为非默认
		updateNonDefault(uid);
		// 将最后修改的数据设置为默认。
		updateDefault(lastModifed.getAid());
	}

单元测试：

	@Test
	public void delete() {
		try {
			Integer uid = 8;
			Integer aid = 6; 
			service.delete(uid, aid);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 40. 收货地址-删除-控制器层

**1. 处理异常**

此次业务层抛出了新的`DeleteException`，需要在`BaseController`中处理。

**2. 设计请求**

	请求路径：/addresses/{aid}/delete
	请求参数：@PathVariable("aid") Integer aid, HttpSession session
	请求方式：POST
	响应数据：ResponseResult<Void>

**3. 处理请求**

	@RequestMapping("/{aid}/delete")
	public ResponseResult<Void> delete(
		@PathVariable("aid") Integer aid, HttpSession session) {
		// 获取uid
		// 执行
		// 返回
	}

具体实现为：

	@RequestMapping("/{aid}/delete")
	public ResponseResult<Void> delete(
	    @PathVariable("aid") Integer aid, HttpSession session) {
	    // 获取uid
		Integer uid = getUidFromSession(session);
	    // 执行
		addressService.delete(uid, aid);
	    // 返回
		return new ResponseResult<>(SUCCESS);
	}

通过`http://localhost:8080/addresses/10/delete`测试。

### 41. 收货地址-删除-界面

	function deleteByAid(aid) {
		$.ajax({
			"url":"/addresses/" + aid + "/delete",
			"type":"POST",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					showAddressList();
				} else {
					alert(json.message);
				}
			},
			"error":function() {
				alert("您的登录信息已经过期，请重新登录！");
				location.href = "login.html";
			}
		});
	}

### 42. 主页-显示热销排行-实体类

创建`cn.tedu.store.entity.Goods`实体类：

	/**
	 * 商品数据的实体类
	 */
	public class Goods extends BaseEntity {
	
		private static final long serialVersionUID = 5960164494648879998L;
	
		private Long id;
		private Long categoryId;
		private String itemType;
		private String title;
		private String sellPoint;
		private Long price;
		private Integer num;
		private String barcode;
		private String image;
		private Integer status;
		private Integer priority;

		// SET/GET/toString

	}

### 43. 主页-显示热销排行-持久层

**1. 分析SQL语句**

	SELECT 
		id, title, price, image
	FROM 
		t_goods 
	WHERE 
		status=1 AND num>10
	ORDER BY
		priority DESC
	LIMIT 
		0,4

**2. 接口与抽象方法**

创建`cn.tedu.store.mapper.GoodsMapper`接口，添加抽象方法：

	List<Goods> findHotGoods();

**3. 配置映射**

复制得到`GoodsMapper.xml`文件，修改根节点的`namespace`，并配置以上方法的映射：

	<mapper namespace="cn.tedu.store.mapper.GoodsMapper">
		
		<!-- 处理商品数据的持久层接口 -->
		<!-- List<Goods> findHotGoods() -->
		<select id="findHotGoods"
			resultType="cn.tedu.store.entity.Goods">
			SELECT 
				id, title, price, image
			FROM 
				t_goods 
			WHERE 
				status=1 AND num>10
			ORDER BY
				priority DESC
			LIMIT 
				0,4
		</select>
		
	</mapper>

创建新的测试类，编写并执行单元测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class GoodsMapperTestCase {
	
		@Autowired
		public GoodsMapper mapper;
		
		@Test
		public void findHotGoods() {
			List<Goods> list = mapper.findHotGoods();
			System.err.println("BEGIN:");
			for (Goods data : list) {
				System.err.println(data);
			}
			System.err.println("END.");
		}
		
	}

### 44. 主页-显示热销排行-业务层

**1. 设计异常**

无

**2. 接口与抽象方法**

创建`cn.tedu.store.service.IGoodsService`接口，添加抽象方法：

	List<Goods> getHotGoods();

**3. 实现**

创建`cn.tedu.store.service.impl.GoodsServiceImpl`类，实现`IGoodsService`接口，在类之前添加`@Service`，在类中添加持久层对象`@Autowired private GoodsMapper goodsMapper;`。

将持久层的方法私有化实现，并重写接口中的抽象方法：

	/**
	 * 处理商品数据的业务层实现类
	 */
	@Service
	public class GoodsServiceImpl implements IGoodsService {
	
		@Autowired
		private GoodsMapper goodsMapper;
		
		@Override
		public List<Goods> getHotGoods() {
			return findHotGoods();
		}
		
		/**
		 * 获取热销商品列表
		 * @return 热销商品列表
		 */
		private List<Goods> findHotGoods() {
			return goodsMapper.findHotGoods();
		}
	
	}

创建单元测试类，编写并执行单元测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class GoodsServiceTestCase {
	
		@Autowired
		public IGoodsService service;
		
		@Test
		public void getHotGoods() {
			List<Goods> list = service.getHotGoods();
			System.err.println("BEGIN:");
			for (Goods data : list) {
				System.err.println(data);
			}
			System.err.println("END.");
		}
		
	}

### 45. 主页-显示热销排行-控制器层

**1. 处理异常**

无

**2. 设计请求**

	请求路径：/goods/hot
	请求参数：无
	请求方式：GET
	响应数据：ResponseResult<List<Goods>>
	是否拦截：否，需要在配置中添加白名单：/goods/**

**3. 处理请求**

首先，在拦截器的配置中添加白名单：`/goods/**`

创建`cn.tedu.store.controller.GoodsController`，继承自`BaseController`，在类之前添加`@RestController`和`@RequestMapping("/goods")`注解，在类中添加业务层对象`@Autowired private IGoodsService goodsService;`。

添加处理请求的方法：

	@GetMapping("/hot")
	public ResponseResult<List<Goods>> getHotGoods() {
		// 获取数据
		// 返回
	}

具体实现：

	@RestController
	@RequestMapping("/goods")
	public class GoodsController extends BaseController {
		
		@Autowired
		private IGoodsService goodsService;
		
		@GetMapping("/hot")
		public ResponseResult<List<Goods>> getHotGoods() {
			// 获取数据
			List<Goods> data = goodsService.getHotGoods();
			// 返回
			return new ResponseResult<>(SUCCESS, data);
		}
		
	}

### 46. 主页-显示热销排行-界面

### 47. 显示商品详情-持久层

**1. 分析SQL语句**

	SELECT 
		image, title, sell_point, price, num, status
	FROM 
		t_goods 
	WHERE 
		id=?

**2. 接口与抽象方法**

在`GoodsMapper`接口中添加抽象方法：

	Goods findById(Long id);

**3. 配置映射**

	<!-- 根据id查询商品详情 -->
	<!-- Goods findById(Long id) -->
	<select id="findById"
		resultType="cn.tedu.store.entity.Goods">
		SELECT 
			image, title, 
			sell_point AS sellPoint, 
			price, num, 
			status
		FROM 
			t_goods 
		WHERE 
			id=#{id}
	</select>

测试：

	@Test
	public void findById() {
		Long id = 10000017L;
		Goods data = mapper.findById(id);
		System.err.println(data);
	}

### 48. 显示商品详情-业务层

**1. 设计异常**

无

**2. 接口与抽象方法**

	Goods getById(Long id);

**3. 实现**

私有化实现：

	/**
	 * 根据id查询商品详情
	 * @param id 商品的id
	 * @return 匹配的商品的详情，如果没有匹配的数据，则返回null
	 */
	private Goods findById(Long id) {
		return goodsMapper.findById(id);
	}

重写抽象方法：

	@Override
	public Goods getById(Long id) {
		return findById(id);
	}
	
测试：

	@Test
	public void getById() {
		Long id = 10000017L;
		Goods data = service.getById(id);
		System.err.println(data);
	}

### 49. 显示商品详情-控制器层

**1. 处理异常**

无

**2. 设计请求**

	请求路径：/goods/{id}/details
	请求参数：@PathVariable("id") Long id
	请求方式：GET
	响应数据：ResponseResult<Goods>
	是否拦截：否，已完成配置

**3. 处理请求**

	@GetMapping("/{id}/details")
	public ResponseResult<Goods> getById(
		@PathVariable("id") Long id) {
		// 执行
		Goods data = goodsService.getById(id);
		// 返回
		return new ResponseResult<>(SUCCESS, data);
	}

### 50 显示商品详情-界面