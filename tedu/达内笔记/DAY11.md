### 51. 购物车-加入购物车-数据库与数据表

	CREATE TABLE t_cart (
		cid INT AUTO_INCREMENT COMMENT '数据id',
		uid INT NOT NULL COMMENT '归属用户的id',
		gid BIGINT NOT NULL COMMENT '商品的id',
		num INT NOT NULL COMMENT '商品的数量',
		created_user VARCHAR(20) COMMENT '创建执行人',
		created_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '修改执行人',
		modified_time DATETIME COMMENT '修改时间',
		PRIMARY KEY(cid)
	) DEFAULT CHARSET=UTF8;

### 52. 购物车-加入购物车-实体类

创建`cn.tedu.store.entity.Cart`，继承自`BaseEntity`，

### 53. 购物车-加入购物车-持久层

**1. 分析SQL语句**

向购物车表中添加新的数据：

	INSERT INTO t_cart (除了cid以外的所有字段) VALUE (匹配的属性列表);

当用户尝试将某商品添加到购物车时，如果该用户此前已经将该商品添加到购物车，应该只更改商品的数量：

	UPDATE t_cart SET num=? WHERE cid=?

当然，还需要能够判断是否已添加：

	SELECT cid, num FROM t_cart WHERE uid=? AND gid=?

所以，后续业务的执行流程应该是先根据uid和gid去查询数据，并尝试获取数据中的cid和num，如果查询结果为null，表示该用户尚未添加该商品到购物车，则需要执行INSERT操作，如果查询结果不是null，表示该用户已将该商品添加到购物车，则只需要更改数量即可，应该是将查询结果中的数量与用户此次提交的数量相加，得到最终在购物车中的数量。

**2. 接口与抽象方法**

创建`cn.tedu.store.mapper.CartMapper`接口，添加抽象方法：

	/**
	 * 处理购物车数据的持久层接口
	 */
	public interface CartMapper {
	
		/**
		 * 插入购物车数据
		 * @param cart 购物车数据
		 * @return 受影响的行数
		 */
		Integer insert(Cart cart);
	
		/**
		 * 修改购物车数据中商品的数量 
		 * @param cid 购物车数据的id
		 * @param num 新的数量
		 * @param modifiedUser 修改执行人
		 * @param modifiedTime 修改时间
		 * @return 受影响的行数
		 */
		Integer updateNum(
			@Param("cid") Integer cid, 
			@Param("num") Integer num,
			@Param("modifiedUser") String modifiedUser, 
			@Param("modifiedTime") Date modifiedTime);
	
		/**
		 * 获取某用户在购物车中添加的指定商品的数据
		 * @param uid 用户的id
		 * @param gid 商品的id
		 * @return 匹配的购物车数据，如果没有匹配的数据，则返回null
		 */
		Cart findByUidAndGid(
			@Param("uid") Integer uid, 
			@Param("gid") Long gid);
		
	}

**3. 配置映射**

映射：

	<mapper namespace="cn.tedu.store.mapper.CartMapper">
	
		<!-- 插入购物车数据 -->
		<!-- Integer insert(Cart cart) -->
		<insert id="insert">
			INSERT INTO t_cart (
				uid, gid,
				num,
				created_user, created_time,
				modified_user, modified_time
			) VALUE (
				#{uid}, #{gid},
				#{num},
				#{createdUser}, #{createdTime},
				#{modifiedUser}, #{modifiedTime}
			)
		</insert>
		
		<!-- 修改购物车数据中商品的数量  -->
		<!-- Integer updateNum(
			@Param("cid") Integer cid, 
			@Param("num") Integer num) -->
		<update id="updateNum">
			UPDATE 
				t_cart 
			SET 
				num=#{num},
				modified_user=#{modifiedUser},
				modified_time=#{modifiedTime}
			WHERE 
				cid=#{cid}
		</update>
		
		<!-- 获取某用户在购物车中添加的指定商品的数据 -->
		<!-- Cart findByUidAndGid(
			@Param("uid") Integer uid, 
			@Param("gid") Long gid) -->
		<select id="findByUidAndGid"
			resultType="cn.tedu.store.entity.Cart">
			SELECT 
				cid, num 
			FROM 
				t_cart 
			WHERE 
				uid=#{uid} AND gid=#{gid}
		</select>
		
	</mapper>

测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class CartMapperTestCase {
	
		@Autowired
		public CartMapper mapper;
		
		@Test
		public void insert() {
			Cart cart = new Cart();
			cart.setUid(1);
			cart.setGid(2L);
			cart.setNum(3);
			Integer rows = mapper.insert(cart);
			System.err.println("rows=" + rows);
		}
		
		@Test
		public void updateNum() {
			Integer cid = 1;
			Integer num = 10;
			String modifiedUser = "Admin";
			Date modifiedTime = new Date();
			Integer rows = mapper.updateNum(cid, num, modifiedUser, modifiedTime);
			System.err.println("rows=" + rows);
		}
		
		@Test
		public void findByUidAndGid() {
			Integer uid = 1;
			Long gid = 5L;
			Cart cart = mapper.findByUidAndGid(uid, gid);
			System.err.println(cart);
		}
		
	}

### 54. 购物车-加入购物车-业务层

**1. 设计异常**

在加入购物车之前，会根据uid和gid去查询数据，无论是否查询到匹配的数据，都不会被视为错误！则不会抛出异常！

可能执行Insert操作，则可能抛出InsertException，也可能执行Update操作，所以，也可能抛出UpdateException。

**2. 接口与抽象方法**

创建`cn.tedu.store.service.ICartService`接口，并添加抽象方法：

	/**
	 * 处理购物车数据的业务层接口
	 */
	public interface ICartService {
	
		/**
		 * 将用户选中的商品添加到购物车
		 * @param username 当前登录的用户的用户名
		 * @param cart 购物车数据
		 * @throws InsertException 插入数据异常
		 * @throws UpdateException 更新数据异常
		 */
		void addToCart(String username, Cart cart) throws InsertException, UpdateException;
		
	}

**3. 实现**

创建`cn.tedu.store.service.impl.CartServiceImpl`类，实现`ICartService`接口，在类之前添加`@Service`，在类中添加持久层对象`@Autowired private CartMapper cartMapper;`。

然后，私有化实现与持久层对应的3个方法，并重写接口中的抽象方法：

	public void addToCart(String username, Cart cart) 
		throws InsertException, UpdateException {
		// 根据uid和gid去查询数据：findByUidAndGid(cart.getUid(), cart.getGid())
		// 判断查询结果是否为null
		// 是：向参数cart中封装日志数据
		// -- 插入数据：insert(cart)
		// 否：计算新的num值：num = cart.getNum() + result.getNum();
		// -- updateNum(result.getCid(), num)
	}

具体实现为：

	/**
	 * 处理购物车数据的业务层实现类
	 */
	@Service
	public class CartServiceImpl implements ICartService {
		
		@Autowired
		private CartMapper cartMapper;
	
		@Override
		public void addToCart(String username, Cart cart) throws InsertException, UpdateException {
			// 根据uid和gid去查询数据：findByUidAndGid(cart.getUid(), cart.getGid())
			Integer uid = cart.getUid();
			Long gid = cart.getGid();
			Cart result = findByUidAndGid(uid, gid);
			
			// 判断查询结果是否为null
			Date now = new Date();
			if (result == null) {
				// 是：向参数cart中封装日志数据
				cart.setCreatedUser(username);
				cart.setCreatedTime(now);
				cart.setModifiedUser(username);
				cart.setModifiedTime(now);
				// 插入数据：insert(cart)
				insert(cart);
			} else {
				// 否：计算新的num值：num = cart.getNum() + result.getNum();
				Integer num = cart.getNum() + result.getNum();
				// 执行更新：updateNum(result.getCid(), num, username, now)
				updateNum(result.getCid(), num, username, now);
			}
		}
	
		/**
		 * 插入购物车数据
		 * @param cart 购物车数据
		 */
		private void insert(Cart cart) {
			Integer rows = cartMapper.insert(cart);
			if (rows != 1) {
				throw new InsertException(
					"添加购物车数据出现未知错误！");
			}
		}
	
		/**
		 * 修改购物车数据中商品的数量 
		 * @param cid 购物车数据的id
		 * @param num 新的数量
		 * @param modifiedUser 修改执行人
		 * @param modifiedTime 修改时间
		 */
		private void updateNum(Integer cid, Integer num, String modifiedUser, Date modifiedTime) {
			Integer rows = cartMapper.updateNum(
					cid, num, modifiedUser, modifiedTime);
			if (rows != 1) {
				throw new UpdateException(
					"修改购物车中商品数量出现未知错误！");
			}
		}
	
		/**
		 * 获取某用户在购物车中添加的指定商品的数据
		 * @param uid 用户的id
		 * @param gid 商品的id
		 * @return 匹配的购物车数据，如果没有匹配的数据，则返回null
		 */
		private Cart findByUidAndGid(Integer uid, Long gid) {
			return cartMapper.findByUidAndGid(uid, gid);
		}
		
	}

测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class CartServiceTestCase {
	
		@Autowired
		public ICartService service;
		
		@Test
		public void addToCart() {
			try {
				String username = "ROOT";
				Cart cart = new Cart();
				cart.setUid(20);
				cart.setGid(21L);
				cart.setNum(3);
				service.addToCart(username, cart);
				System.err.println("OK.");
			} catch (ServiceException e) {
				System.err.println(e.getClass().getName());
				System.err.println(e.getMessage());
			}
		}
		
	}

### 55. 购物车-加入购物车-控制器层

**1. 处理异常**

无需再处理

**2. 设计请求**

	请求路径：/carts/add
	请求参数：Cart cart, HttpSession session
	请求方式：POST
	响应数据：ResponseResult<Void>
	是否拦截：是，无需修改配置

**3. 处理请求**

创建`cn.tedu.store.controller.CartController`，继承自`BaseController`，在类之前添加`@RestController`和`@RequestMapping("/carts")`注解，在类中添加业务层对象`@Autowired private ICartService cartService;`。

添加处理请求的方法：

	@RequestMapping("/add")
	public ResponseResult<Void> addToCart(Cart cart, HttpSession session) {
		// 注意：客户端提交的cart只会包含gid, num
		// 从session中获取uid
		// 从session中获取username
		// 将uid封装到cart中
		// 执行：service.addToCart(username, cart);
		// 返回
	}

完成后，先登录，通过`http://localhost:8080/carts/add?gid=100&num=3`进行测试。

### 56. 购物车-加入购物车-界面

### 57. 购物车-购物车列表-持久层

SQL语句：

	SELECT 
		uid, cid, gid, t_cart.num,
		title, image, price
	FROM 
		t_cart 
	LEFT JOIN
		t_goods
	ON
		t_cart.gid=t_goods.id
	WHERE 
		uid=?
	ORDER BY 
		t_cart.modified_time DESC, cid DESC

当前没有任何类可以作为此次查询的返回类型！则需要创建对应的VO类`cn.tedu.store.vo.CartVO`：

	/**
	 * 购物车数据的VO类
	 */
	public class CartVO implements Serializable {
	
		private static final long serialVersionUID = -1375921507047753775L;
	
		private Integer cid;
		private Integer uid;
		private Long gid;
		private Integer num;
		private String title;
		private String image;
		private Long price;

		// SET/GET/toString
	}

对应抽象方法：

	List<CartVO> findByUid(Integer uid);

映射：

	<!-- 获取某用户的购物车数据列表 -->
	<!-- List<CartVO> findByUid(Integer uid) -->
	<select id="findByUid"
		resultType="cn.tedu.store.vo.CartVO">
		SELECT 
			uid, cid, 
			gid, t_cart.num,
			title, image, 
			price
		FROM 
			t_cart 
		LEFT JOIN
			t_goods
		ON
			t_cart.gid=t_goods.id
		WHERE 
			uid=#{uid}
		ORDER BY 
			t_cart.modified_time DESC, cid DESC
	</select>

测试：

	@Test
	public void findByUid() {
		Integer uid = 8;
		List<CartVO> list = mapper.findByUid(uid);
		System.err.println("BEGIN:");
		for (CartVO data : list) {
			System.err.println(data);
		}
		System.err.println("END.");
	}