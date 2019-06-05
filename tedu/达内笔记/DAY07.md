### 23. 用户-上传头像-界面

关于前端界面的处理，由于服务器端依然是将响应JSON数据，则前端还是应该通过AJAX发出异步请求，然后获取JSON数据，并处理结果。

相对传统的AJAX访问，关于文件上传时，区别在于：

- 提交到服务器端的数据应该是：`new FormData($("#form-change-avatar")[0])`

- 必须配置`"contentType":false,`和`"processData":false,`

完整代码例如：

	<script type="text/javascript">
	$("#btn-change-password").click(function(){
		$.ajax({
			"url":"/users/change_password",
			"data":$("#form-change-password").serialize(),
			"type":"POST",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					alert("修改成功！");
					$("#form-change-password")[0].reset();
				} else {
					alert(json.message);
				}
			},
			"error":function() {
				alert("您的登录信息已经过期，请重新登录！");
				location.href = "login.html";
			}
		});
	});
	</script>

关于在前端界面显示新上传的头像，首先，请检查服务器端的控制器是否返回了头像的路径，即：

	return new ResponseResult<String>(SUCCESS, avatar);

如果需要测试，可以在客户端通过`alert(json.data);`测试是否获取到了头像路径。

如果能够正确的获取到上传的文件的路径，则通过jQuery选择器，选中显示头像的`<img>`标签，修改其`src`属性即可：

	$("#img-avatar").attr("src", json.data);

至此，上传头像并显示新头像的功能已经完成，但是，刷新或重新打开页面时，并不会显示用户的头像图片！

由于显示界面使用的是html，所以，应该将头像数据存储在cookie中，则html可以随时访问到cookie中的头像数据！

关于cookie的访问，如果需要向cookie中存入数据，可以是：

	$.cookie("username", "root", {
		expire: 7
	});

反之，如果需要取出cookie中的数据，则应该是：

	$.cookie("username");

需要注意的是，这种使用cookie的方式是jQuery封装的函数，使用之前需要引入对应的文件：

	<script src="../bootstrap3/js/jquery.cookie.js" type="text/javascript" charset="utf-8"></script>

所以，需要实现“打开页面就能直接显示当前登录的用户的头像”，则应该：

1. 必须保证登录时的查询(findByUsername)对应的SQL查询中，已经查询了avatar字段；

2. 当用户成功登录时，将头像路径存入到cookie中；

3. 当加载“上传头像”页面时，从cookie中取出头像路径，如果值是有效的，则显示；

4. 当成功上传头像后，修改cookie中存放的头像路径；

最后，SpringBoot项目默认已经集成了上传所需的环境和配置，并且，限制了上传的文件大小，如果上传太大的文件，则会报告错误！通常，项目中的配置，是整个项目中任何上传功能都不允许超出文件大小范围，而在控制器中，限制的是特定的某个功能的上传文件大小的范围！整个项目的上传大小应该设置为比较大的值！

通常，可以在启动类中添加配置：

	@SpringBootApplication
	@Configuration
	@MapperScan("cn.tedu.store.mapper")
	public class StoreApplication {
	
		public static void main(String[] args) {
			SpringApplication.run(StoreApplication.class, args);
		}
	
		@Bean
		public MultipartConfigElement multipartConfigration() {
			MultipartConfigFactory mcf
				= new MultipartConfigFactory();
			DataSize maxFileSize = DataSize.ofBytes(100 * 1024 * 1024);
			DataSize maxRequestSize = DataSize.ofBytes(100 * 1024 * 1024);
			mcf.setMaxFileSize(maxFileSize);
			mcf.setMaxRequestSize(maxRequestSize);
			MultipartConfigElement mce
				= mcf.createMultipartConfig();
			return mce;
		}
		
	}

注意：由于配置的代码是写在启动类中的，所以，启动类需要添加`@Configuration`注解，且配置的方法需要添加`@Bean`注解。

注意：某些做法是在`application.properties`中进行配置，这种做法在某些版本中是无效的，推荐使用以上Java代码进行配置。

### 24. 阶段小结

- 掌握SpringBoot的基本使用；

- 理解MVC的编程理念，理解其中的各层的定位与作用，特别是Model中的Service；

- 掌握异常的作用，掌握抛出、处理异常的原则，掌握统一处理异常的做法；

- 掌握密码的加密；

- 掌握文件上传的做法；

### 25. 收货地址-增加收货地址-数据库与数据表

关于收货地址数据的处理，涉及：显示收货地址列表、增加收货地址、修改、删除、设为默认。

开发顺序应该是：增加收货地址 > 显示收货地址列表 > 设为默认 > 删除收货地址 > 修改收货地址。

创建“收货地址”的数据表：

	CREATE TABLE t_address (
		aid INT AUTO_INCREMENT COMMENT '收货地址的id',
		uid INT NOT NULL COMMENT '所属用户的id',
		name VARCHAR(20) COMMENT '收货人姓名',
		province CHAR(6) COMMENT '省的代号',
		city CHAR(6) COMMENT '市的代号',
		area CHAR(6) COMMENT '区的代号',
		district VARCHAR(50) COMMENT '省市区的名称',
		zip CHAR(6) COMMENT '邮政编码',
		address VARCHAR(50) COMMENT '详细地址',
		phone VARCHAR(20) COMMENT '手机',
		tel VARCHAR(20) COMMENT '固话',
		tag VARCHAR(10) COMMENT '地址类型，如：家/公司/学校',
		is_default INT COMMENT '是否默认，0-非默认，1-默认',
		created_user VARCHAR(20) COMMENT '创建执行人',
		created_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '修改执行人',
		modified_time DATETIME COMMENT '修改时间',
		PRIMARY KEY(aid)
	) DEFAULT CHARSET=UTF8;

### 25. 收货地址-增加收货地址-实体类

创建`cn.tedu.store.entity.Address`实体类，继承自`BaseEntity`：

	/**
	 * 收货地址的实体类
	 */
	public class Address extends BaseEntity {
	
		private static final long serialVersionUID = 8491523504331195543L;
	
		private Integer aid;
		private Integer uid;
		private String name;
		private String province;
		private String city;
		private String area;
		private String district;
		private String zip;
		private String address;
		private String phone;
		private String tel;
		private String tag;
		private Integer isDefault;

		// SET/GET/toString

	}

### 26. 收货地址-增加收货地址-持久层

**1. 分析SQL语句**

执行增加数据的SQL语句应该是：

	INSERT INTO t_address (除了aid以外的所有字段) VALUE (对应的属性列表);

后续，在“增加收货地址”的业务中，可以制定规则“当用户第1次增加收货地址时，该收货地址直接是默认的，后续增加的每一条都不是默认的”，可以通过“判断当前用户有没有收货地址”来实现，表现为“查询当前用户的收货地址数据的数量”，要保证该规则的应用，还需要：

	SELECT COUNT(aid) FROM t_address WHERE uid=?

**2. 接口与抽象方法**

创建`cn.tedu.store.mapper.AddressMapper`接口，添加抽象方法：

	/**
	 * 处理收货地址的持久层接口
	 */
	public interface AddressMapper {
	
		/**
		 * 增加收货地址数据
		 * @param address 收货地址数据
		 * @return 受影响的行数
		 */
		Integer insert(Address address);
	
		/**
		 * 统计指定用户的收货地址数据的数量
		 * @param uid 用户的id
		 * @return 用户的收货地址数据的数量
		 */
		Integer countByUid(Integer uid);
		
	}

**3. 配置映射**

复制得到`AddressMapper.xml`文件，修改根节点的`namespace`属性值，然后添加以上2个抽象方法的映射的配置：

	<mapper namespace="cn.tedu.store.mapper.AddressMapper">
		
		<!-- 增加收货地址数据 -->
		<!-- Integer insert(Address address) -->
		<insert id="insert">
			INSERT INTO t_address (
				uid, name,
				province, city,
				area, district,
				zip, address,
				phone, tel,
				tag, is_default,
				created_user, created_time,
				modified_user, modified_time
			) VALUE (
				#{uid}, #{name},
				#{province}, #{city},
				#{area}, #{district},
				#{zip}, #{address},
				#{phone}, #{tel},
				#{tag}, #{isDefault},
				#{createdUser}, #{createdTime},
				#{modifiedUser}, #{modifiedTime}
			)
		</insert>
		
		<!-- 统计指定用户的收货地址数据的数量 -->
		<!-- Integer countByUid(Integer uid) -->
		<select id="countByUid" 
			resultType="java.lang.Integer">
			SELECT 
				COUNT(aid) 
			FROM 
				t_address 
			WHERE 
				uid=#{uid}
		</select>
		
	</mapper>

完成后，创建新的单元测试类`cn.tedu.store.mapper.AddressMapperTestCase`，并对以上2个方法执行测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class AddressMapperTestCase {
	
		@Autowired
		public AddressMapper mapper;
		
		@Test
		public void insert() {
			Address address = new Address();
			address.setUid(8);
			address.setName("小李同学");
			Integer rows = mapper.insert(address);
			System.err.println("rows=" + rows);
		}
		
		@Test
		public void countByUid() {
			Integer uid = 8;
			Integer count = mapper.countByUid(uid);
			System.err.println("count=" + count);
		}
		
	}

### 27. 收货地址-增加收货地址-业务层

**1. 设计异常**

由于此次主要执行增加数据操作，可能涉及`InsertException`。

另外，还将查询某用户的数据量，但是，无论是否存在数据，都不会被视为错误操作，所以，不涉及异常。（如果增加规则“每个用户只允许创建??条收货地址”，则应该有对应的异常）

**2. 接口与抽象方法**

创建`cn.tedu.store.service.IAddressService`接口，并添加抽象方法：

	void addnew(Address address, String username) 
			throws InsertException;

**3. 实现**

创建`cn.tedu.store.service.impl.AddressServiceImpl`类，实现以上`IAddressService`接口，添加`@Service`注解，在类中添加持久层对象`@Autowired private AddressMapper addressMapper;`。

首先，将持久层中的2个方法在业务层实现类中实现为私有方法，其中，增加数据的方法需要判断其返回值，当返回值不为1时抛出异常，且该方法返回值类型为`void`。

然后，重写接口中的抽象方法：

	public void addnew(Address address, String username) throws InsertException {
		// 查询用户的收货地址的数量：countByUid(Integer uid)，参数值来自address.getUid();
		// 判断数量是否为0
		// 是：当前将增加第1条收货地址，则：address.setIsDefault(1)
		// 否：当前增加的不是第1条收货地址，则：address.setIsDefault(0)

		// TODO 处理district

		// 4项日志：时间是直接创建对象得到，用户名使用参数username

		// 执行增加：insert(Address address);
	}

具体实现为：

	/**
	 * 处理收货地址数据的业务层实现类
	 */
	@Service
	public class AddressServiceImpl implements IAddressService {
		
		@Autowired
		private AddressMapper addressMapper;
	
		@Override
		public void addnew(Address address, String username) throws InsertException {
			// 查询用户的收货地址的数量：countByUid(Integer uid)，参数值来自address.getUid();
			Integer count = countByUid(address.getUid());
			// 判断数量是否为0
			// 是：当前将增加第1条收货地址，则：address.setIsDefault(1)
			// 否：当前增加的不是第1条收货地址，则：address.setIsDefault(0)
			address.setIsDefault(count == 0 ? 1 : 0);
	
			// TODO 处理district
	
			// 4项日志：时间是直接创建对象得到，用户名使用参数username
			Date now = new Date();
			address.setCreatedUser(username);
			address.setCreatedTime(now);
			address.setModifiedUser(username);
			address.setModifiedTime(now);
	
			// 执行增加：insert(Address address);
			insert(address);
		}
		
		/**
		 * 增加收货地址数据
		 * @param address 收货地址数据
		 */
		private void insert(Address address) {
			Integer rows = addressMapper.insert(address);
			if (rows != 1) {
				throw new InsertException(
					"增加收货地址数据时出现未知错误！");
			}
		}
	
		/**
		 * 统计指定用户的收货地址数据的数量
		 * @param uid 用户的id
		 * @return 用户的收货地址数据的数量
		 */
		private Integer countByUid(Integer uid) {
			return addressMapper.countByUid(uid);
		}
	
	}

完成后创建`cn.tedu.store.service.AddressServiceTestCase`测试类，对以上功能进行测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class AddressServiceTestCase {
	
		@Autowired
		public IAddressService service;
		
		@Test
		public void addnew() {
			try {
				Address address = new Address();
				address.setUid(10);
				address.setName("小刘同学");
				String username = "小森同学";
				service.addnew(address, username);
				System.err.println("OK.");
			} catch (ServiceException e) {
				System.err.println(e.getClass().getName());
				System.err.println(e.getMessage());
			}
		}
		
	}

### 28. 收货地址-增加收货地址-控制器层

**1. 处理异常**

无，业务层没有抛出新的异常，则不需要处理。

**2. 设计请求**

	请求路径：/addresses/addnew
	请求参数：Address address, HttpSession session
	请求方式：POST
	响应数据：ResponseResult<Void>
	是否拦截：是，无需修改配置

**3. 处理请求**

创建`cn.tedu.store.controller.AddressController`，继承自`BaseController`，添加`@RestController`和`@RequestMapping("/addresses")`注解，在类中声明业务层对象`@Autowired private IAddressService addressService;`。

然后，添加处理请求的方法：

	@RequestMapping("/addnew")
	public ResponseResult<Void> addnew(Address address, HttpSession session) {
		// 从session中获取uid
		// 将uid封装到address中
		// 从session中获取username
		// 调用业务层对象执行：addressService.addnew(address, username);
		// 返回成功
	}

完成后，打开浏览器，先登录，通过`http://localhost:8080/addresses/addnew?name=Henry`进行测试。

### 29. 收货地址-增加收货地址-界面

与此前处理的基本一致：先复制得到ajax请求相关代码，修改按钮id、表单id、请求路径，并在HTML代码部分确定各id值，及表单中各控件的name值。