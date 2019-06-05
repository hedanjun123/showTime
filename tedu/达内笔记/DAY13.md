### 创建订单-数据表

创建订单表：

	CREATE TABLE t_order (
		oid INT AUTO_INCREMENT COMMENT 'id',
		uid INT COMMENT '归属哪个用户',
		name VARCHAR(20) COMMENT '收货人',
		phone VARCHAR(20) COMMENT '收货电话',
		address VARCHAR(100) COMMENT '收货地址',
		status INT COMMENT '订单状态：0-未支付，1-已支付，2-已取消…………',
		price BIGINT COMMENT '商品总价',
		order_time DATETIME COMMENT '下单时间',
		pay_time DATETIME COMMENT '支付时间',
		created_user VARCHAR(20) COMMENT '创建执行人',
		created_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '修改执行人',
		modified_time DATETIME COMMENT '修改时间',
		PRIMARY KEY(oid)
	) DEFAULT CHARSET=UTF8;

创建订单商品表：

	CREATE TABLE t_order_item (
		id INT AUTO_INCREMENT COMMENT 'id',
		oid INT COMMENT '归属的订单id',
		gid BIGINT COMMENT '商品id',
		title VARCHAR(100) COMMENT '商品名称',
		image VARCHAR(500) COMMENT '商品图片',
		price BIGINT COMMENT '商品单价',
		num INT COMMENT '购买数量',
		created_user VARCHAR(20) COMMENT '创建执行人',
		created_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '修改执行人',
		modified_time DATETIME COMMENT '修改时间',
		PRIMARY KEY(id)
	) DEFAULT CHARSET=UTF8;

### 创建订单-实体类

	/**
	 * 订单数据的实体类
	 */
	public class Order extends BaseEntity {
	
		private static final long serialVersionUID = -2949246051611079678L;
	
		private Integer oid;
		private Integer uid;
		private String name;
		private String phone;
		private String address;
		private Integer status;
		private Long price;
		private Date orderTime;
		private Date payTime;

		// SET/GET/toString

	}

	/**
	 * 订单商品的实体类
	 */
	public class OrderItem extends BaseEntity {
	
		private static final long serialVersionUID = -3906850018555473314L;
	
		private Integer id;
		private Integer oid;
		private Long gid;
		private String title;
		private String image;
		private Long price;
		private Integer num;

		// SET/GET/toString

	}

### 创建订单-持久层

创建`cn.tedu.store.mapper.OrderMapper`，用于处理以上2张表的数据，则应该添加抽象方法：

	/**
	 * 处理订单和订单商品数据的持久层接口
	 */
	public interface OrderMapper {
	
		/**
		 * 插入订单数据
		 * @param order 订单数据
		 * @return 受影响的行数
		 */
		Integer insertOrder(Order order);
	
		/**
		 * 插入订单商品数据
		 * @param orderItem 订单商品数据
		 * @return 受影响的行数
		 */
		Integer insertOrderItem(OrderItem orderItem);
		
	}

配置的映射：

	<mapper namespace="cn.tedu.store.mapper.OrderMapper">
	
		<!-- 插入订单数据 -->
		<!-- Integer insertOrder(Order order) -->
		<insert id="insertOrder">
			INSERT INTO t_order (
				uid, name,
				phone, address,
				status, price,
				order_time, pay_time,
				created_user, created_time,
				modified_user, modified_time
			) VALUES (
				#{uid}, #{name},
				#{phone}, #{address},
				#{status}, #{price},
				#{orderTime}, #{payTime},
				#{createdUser}, #{createdTime},
				#{modifiedUser}, #{modifiedTime}
			)
		</insert>
		
		<!-- 插入订单商品数据 -->
		<!-- Integer insertOrderItem(OrderItem orderItem) -->
		<insert id="insertOrderItem">
			INSERT INTO t_order_item (
				oid, gid, 
				title, image, 
				price, num,
				created_user, created_time,
				modified_user, modified_time
			) VALUES (
				#{oid}, #{gid}, 
				#{title}, #{image}, 
				#{price}, #{num},
				#{createdUser}, #{createdTime},
				#{modifiedUser}, #{modifiedTime}
			)
		</insert>
		
	</mapper>

单元测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class OrderMapperTestCase {
	
		@Autowired
		public OrderMapper mapper;
		
		@Test
		public void insertOrder() {
			Order order = new Order();
			order.setUid(8);
			order.setName("小张同学");
			Integer rows = mapper.insertOrder(order);
			System.err.println("rows=" + rows);
		}
		
		@Test
		public void insertOrderItem() {
			OrderItem orderItem = new OrderItem();
			orderItem.setOid(1);
			orderItem.setTitle("笔记本电脑");
			orderItem.setPrice(6555L);
			orderItem.setNum(3);
			Integer rows = mapper.insertOrderItem(orderItem);
			System.err.println("rows=" + rows);
		}
		
	}

### 创建订单-业务层

首先，在`IAddressService`中添加新的抽象方法并实现：

	Address getByAid(Integer aid);

创建业务层接口`cn.tedu.store.service.IOrderService`，并添加抽象方法：

	Order createOrder(Integer uid, String username, Integer aid, Integer[] cids) throws InsertException;

创建业务层实现类`cn.tedu.store.service.impl.OrderServiceImpl`，添加注解，声明持久层对象，声明收货地址业务层对象（`@Autowired private IAddressService addressService;`），声明购物车业务层对象（`@Autowired private ICartService cartService;`），并私有化实现持久层的2个方法，然后重写接口中的抽象方法：

	public Order createOrder(Integer uid, String username, Integer aid, Integer[] cids) throws InsertException {
		// 创建当前时间对象now

		// 调用cartService方法根据cids获取数据：List<CartVO> getByCids(Integer[] cids);
		// 遍历计算商品总价

		// 创建订单数据对象：new Order()
		// 封装订单数据的属性：uid
		// 调用addressService获取收货地址数据：getByAid(Integer aid)
		// 封装订单数据的属性：name, phone, address
		// 封装订单数据的属性：status:0
		// 封装订单数据的属性：price:null
		// 封装订单数据的属性：orderTime:now
		// 封装订单数据的属性：payTime:null
		// 封装4项日志属性
		// 插入订单数据：insertOrder(Order order)

		// 遍历以上查询结果
		// -- 创建订单商品数据对象：new OrderItem()
		// -- 封装订单商品数据的属性：oid
		// -- 封装订单商品数据的属性：gid,title,image,price,num
		// -- 封装4项日志属性
		// -- 插入订单商品数据：insertOrderItem(OrderItem orderItem)
	}

具体实现为：

	/**
	 * 处理订单和订单商品数据的业务层实现类
	 */
	@Service
	public class OrderServiceImpl implements IOrderService {
		
		@Autowired
		private OrderMapper orderMapper;
		@Autowired
		private IAddressService addressService;
		@Autowired
		private ICartService cartService;
	
		@Override
		public Order createOrder(Integer uid, String username, Integer aid, Integer[] cids) throws InsertException {
			// 创建当前时间对象now
			Date now = new Date();
	
			// 调用cartService方法根据cids获取数据：List<CartVO> getByCids(Integer[] cids);
			List<CartVO> carts = cartService.getByCids(cids);
			// 遍历计算商品总价
			Long price = 0L;
			for (CartVO cartVO : carts) {
				price += cartVO.getPrice() * cartVO.getNum();
			}
	
			// 创建订单数据对象：new Order()
			Order order = new Order();
			// 封装订单数据的属性：uid
			order.setUid(uid);
			// 调用addressService获取收货地址数据：getByAid(Integer aid)
			Address address = addressService.getByAid(aid);
			// 封装订单数据的属性：name, phone, address
			if (address == null) {
				throw new AddressNotFoundException(
					"创建订单失败！选择的收货地址不存在！");
			}
			if (!address.getUid().equals(uid)) {
				throw new AccessDeniedException(
					"创建订单失败！收货地址数据归属有误！");
			}
			order.setName(address.getName());
			order.setPhone(address.getPhone());
			order.setAddress(address.getDistrict() + " " + address.getAddress());
			// 封装订单数据的属性：status:0
			order.setStatus(0);
			// 封装订单数据的属性：price
			order.setPrice(price);
			// 封装订单数据的属性：orderTime:now
			order.setOrderTime(now);
			// 封装订单数据的属性：payTime:null
			order.setPayTime(null);
			// 封装4项日志属性
			order.setCreatedUser(username);
			order.setCreatedTime(now);
			order.setModifiedUser(username);
			order.setModifiedTime(now);
			// 插入订单数据：insertOrder(Order order)
			insertOrder(order);
	
			// 遍历以上查询结果
			for (CartVO cart : carts) {
				// -- 创建订单商品数据对象：new OrderItem()
				OrderItem item = new OrderItem();
				// -- 封装订单商品数据的属性：oid
				item.setOid(order.getOid());
				// -- 封装订单商品数据的属性：gid,title,image,price,num
				item.setGid(cart.getGid());
				item.setTitle(cart.getTitle());
				item.setImage(cart.getImage());
				item.setPrice(cart.getPrice());
				item.setNum(cart.getNum());
				// -- 封装4项日志属性
				item.setCreatedUser(username);
				item.setCreatedTime(now);
				item.setModifiedUser(username);
				item.setModifiedTime(now);
				// -- 插入订单商品数据：insertOrderItem(OrderItem orderItem)
				insertOrderItem(item);
			}
			
			return order;
		}
		
		/**
		 * 插入订单数据
		 * @param order 订单数据
		 */
		private void insertOrder(Order order) {
			Integer rows = orderMapper.insertOrder(order);
			if (rows != 1) {
				throw new InsertException(
					"创建订单失败！插入数据时出现未知错误[1]！");
			}
		}
	
		/**
		 * 插入订单商品数据
		 * @param orderItem 订单商品数据
		 */
		private void insertOrderItem(OrderItem orderItem) {
			Integer rows = orderMapper.insertOrderItem(orderItem);
			if (rows != 1) {
				throw new InsertException(
					"创建订单失败！插入数据时出现未知错误[2]！");
			}
		}
	
	}

### 创建订单-控制器层

创建`OrderController`控制器类，继承自`BaseController`，添加注解，声明业务层对象，并添加处理请求的方法：

	@PostMapping("/create")
	public ResponseResult<Order> createOrder(
		Integer aid, Integer[] cids, HttpSession session) {
		
	}

具体实现：

	@RestController
	@RequestMapping("/orders")
	public class OrderController extends BaseController {
	
		@Autowired
		private IOrderService orderService;
		
		@RequestMapping("/create")
		public ResponseResult<Order> createOrder(
			Integer aid, Integer[] cids, HttpSession session) {
			// 从session中获取uid
			Integer uid = getUidFromSession(session);
			// 从session中获取username
			String username = session.getAttribute("username").toString();
			// 执行
			Order data = orderService.createOrder(uid, username, aid, cids);
			// 返回
			return new ResponseResult<>(SUCCESS, data);
		}
		
	}

完成后可通过`http://localhost:8080/orders/create?aid=16&cids=7&cids=8`执行测试。

### 再来看Spring

解耦：解除类与类之间的依赖关系。解决了例如`UserServiceImpl userService = new UserServiceImpl()`这样的问题，则例如`UserController`不再依赖于`UserServiceImpl`类，而是仅依赖于`IUserService`接口，需要这个对象时，只需要`@Autowired private IUserService service;`即可，且在`UserServiceImpl`类中添加`@Service`并保证在组件扫描范围内。

IoC与DI：Inversion of Control和Dependency Injection，前者表示“控制反转”，后者表示“依赖注入”。控制反转指的是对象的创建过程不再由开发者的代码来进行控制，而是把这部分的控制权交给框架，简单的说，就是开发者不会自己去new某些对象，而是通过配置XML或添加注解等方式让框架去创建对象！依赖注入指的是为对象中的某些属性赋予值！IoC是Spring框架实现的目标，而DI是实现该目标时需要使用的手段！

AOP：面向切面编程，AOP并不是Spring独有的特性，只是Spring对AOP的支持非常好，可以简单的实现AOP。

	登录			Controller	>	切面	>	Service.login()	>	Mapper	
	注册			Controller	>	切面	>	Service.reg()	>	Mapper	
	改密			Controller	>	切面	>	Service	>	Mapper	

### Spring AOP

首先，需要添加AOP相关的依赖：

	<dependency>
		<groupId>aspectj</groupId>
		<artifactId>aspectj-tools</artifactId>
		<version>1.0.6</version>
	</dependency>

	<dependency>
		<groupId>aspectj</groupId>
		<artifactId>aspectjweaver</artifactId>
		<version>1.5.4</version>
	</dependency>

本次目标是：所有service方法都可以统计方法的执行时间。

统计功能应该在某个方法中，而方法应该在某个类中，这样的类，通常定义为切面类！则创建`cn.tedu.store.aspect.TimeElapsedAspect`类，由于它将是被Spring管理的组件，则添加`@Component`注解，且它是一个切面类，则添加`@Aspect`注解：

	@Aspect
	@Component
	// @Component, @Controller, @Service, @Repository
	public class TimeElapsedAspect {
	
	}

接下来，在类中定义切面执行方法：

	@Aspect
	@Component
	// @Component, @Controller, @Service, @Repository
	public class TimeElapsedAspect {
	
		// 切面方法的名称可以自由定义
		// 切面方法必须添加参数ProceedingJoinPoint
		// 参数对象调用proceed()相当于执行了切面对应的方法
		// @Around注解表示在切面对应的方法之前和之后都会执行某些代码
		@Around("execution(* cn.tedu.store.service.impl.*.*(..))")
		public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
			// 记录开始时间
			long start = System.currentTimeMillis();
			
			// 执行切面对应的方法
			Object result = pjp.proceed();
			
			// 记录结束时间
			long end = System.currentTimeMillis();
			
			// 计算得到耗时
			System.err.println("耗时：" + (end - start));
			
			// 返回执行切面方法的业务方法的返回结果
			return result;
		}
		
	}

注意事项：

1. 使用`@Around`注解表示在切面将在某方法之前和之后均执行部分代码；

2. 表达式`"execution(* cn.tedu.store.service.impl.*.*(..))"`表示切面将应用于哪些方法；

3. 如果需要应用到可能有返回值的方法，则切面方法需要声明`ProcedingJoinPoint`类型的参数，且需要使用`Object`类型的返回值，且在切面方法内部，需要获取`pjp.proceed()`的返回值，最终返回，否则，被应用的切面方法将无法正常返回结果；