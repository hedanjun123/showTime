### 5. 用户-注册-业务层【续】

密码加密的必要性：如果直接并密码使用明文方式存储，可能存在内部泄露或入侵盗取的风险。

通常，对密码进行加密时，并不会使用通用的加密算法，因为这些加密算法都是可以被逆向运算的，即：只要能够获得加密过程中的所有参数，就可以将密码逆向运算得到原始密码。

真正应用于密码加密的是“消息摘要算法”，消息摘要算法的特性：

1. 原文相同，则摘要数据一定相同；

2. 摘要算法不变，则摘要数据的长度不变，无视原文长度；

3. 原文不同，则摘要数据几乎不会相同。

常见的消息摘要算法有：SHA-128、SHA-256、SHA-384、SHA-512、MD2、MD4、MD5……

关于MD5的破解：

- 消息摘要算法不存在逆向运算，所谓的消息摘要算法的破解，是如何找到2个或更多不同的原文，却可以得到相同的摘要，以MD5为例，理论上运算2的128次方的次数就可以找到，而破解的核心在于运算更少的次数来找到这样的数据；

- 在线破解：本质是记录了原文与摘要数据的对应关系，当输入摘要数据时，查询得到原文，这些在线破解只能“破解”原文比较简单的数据。

进一步加强密码的安全性：

1. 加强原始密码的复杂程度；

2. 使用位数更长的加密算法；

3. 加盐；

4. 多重加密；

5. 综合以上用法。

在实际应用中，应该在业务层添加一个用于加密的方法，后续在注册、登录及其它需要验证密码的场合都可以直接调用该方法：

	/**
	 * 获取执行MD5加密后的密码
	 * @param password 原密码
	 * @param salt 盐值
	 * @return 加密后的密码
	 */
	private String getMd5Password(
			String password, String salt) {
		// 加密规则：使用“盐+密码+盐”作为原始数据，执行5次加密
		String result = salt + password + salt;
		for (int i = 0; i < 5; i++) {
			result = DigestUtils
				.md5DigestAsHex(result.getBytes()).toUpperCase();
		}
		return result;
	}

并且，在注册过程中，需要执行加密，并且，将加密后的密码、生成的盐值都封装到执行注册的user对象中，以将这些数据插入到数据表中：

	// 生成随机盐
	String salt = UUID.randomUUID().toString().toUpperCase();
	// 执行密码加密，得到加密后的密码
	String md5Password = getMd5Password(user.getPassword(), salt);
	// 将盐和加密后的密码封装到user中
	user.setPassword(md5Password);
	user.setSalt(salt);

注意：由于应用了新的密码功能，所以，此前产生的测试数据已经无法使用，应该将此前产生的测试数据全部删除，避免后续使用时出现错误。

### 6. 用户-注册-控制器层

创建控制器类`cn.tedu.store.controller.UserController`，添加`@RestController`和`@RequestMapping("/users")`注解，在类中添加业务层对象`@Autowired private IUserService userService;`：

	@RestController
	@RequestMapping("/users")
	public class UserController {
	
		@Autowired
		private IUserService userService;
		
	}

**1. 设计请求**

	请求路径：/users/reg
	请求参数：User
	请求方式：POST
	响应数据：无

**2. 处理请求**

首先，应该创建用于响应操作结果的`cn.tedu.store.util.ResponseResult`类：

	/**
	 * 用于向客户端响应操作结果的类型
	 * @param <T> 操作结果中包含的数据的类型
	 */
	public class ResponseResult<T> implements Serializable {
	
		private static final long serialVersionUID = -5368505763231357265L;
	
		private Long state;
		private String message;
		private T data;
	
		// SET/GET

	}

然后在控制器类中添加处理请求的方法：

	@GetMapping("/reg")
	public ResponseResult<Void> reg(User user) {
		ResponseResult<Void> rr
			= new ResponseResult<Void>();
		
		try {
			userService.reg(user);
			rr.setState(1);
		} catch (ServiceException e) {
			rr.setState(2);
			rr.setMessage(e.getMessage());
		}
		
		return rr;
	}

完成后，启动项目，打开浏览器，通过`http://localhost:8080/users/reg?username=root&password=1234`执行测试。

测试完成后，将`@GetMapping`调整为`@PostMapping`。

### 7. 用户-注册-前端界面