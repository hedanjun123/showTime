### 6. 用户-注册-控制器层

**[附] 异常**

异常的体系结构：

	Throwable
		Error
			OutOfMemoryError
		Exception
			IOException
				FileNotFoundException
			RuntimeException
				NullPointerException
				NumberFormatException
				ClassCastException
				ArithmeticException
				IndexOutOfBoundsException
					ArrayIndexOutOfBoundsException

关于异常的处理：从语法上，可以通过抛出（在方法体中使用`throw`抛出异常对象，并在方法签名中使用`throws`声明抛出）或捕获（使用`try...catch`语法包裹相关代码）这两种方式对异常进行处理！

抛出：在方法体中如果抛出了异常对象，在方法的签名中必须声明抛出，并且，如果是重写的方法，不可以抛出更多的异常；

捕获：捕获(`catch`)时，如果有多个异常，在捕获时可以不区分先后顺序，如果多个异常有继承关系，必须先捕获子级异常，然后再捕获父级异常。

如果需要处理的异常是`RuntimeException`或其子孙类异常，则不受以上处理的语法约束！主要因为：[1] 这些异常出现的频率可能极高；[2] 这些异常都可以通常事先的判断等操作，杜绝异常的发生。

关于异常的处理：首先，无论怎么处理，其实，该发生的异常已经发生了！所谓的处理，应该是“对已经发生的异常的补救”，通过这种处理，希望后续不再出现类似的“问题”。

在实际应用中，处理异常可能表现为“向用户提示错误信息”，因为，如果程序出现异常，类似于`NullPointerException`这种的信息是普通用户看不懂的，而专业人员可能从中分析出程序的流程或某些数据，所以，这类信息是不应该向外暴露给任何人的！所以，任何异常都是需要处理的！处理时，应该先考虑当前类或当前方法是否适合甚至能够`try...catch`进行处理，如果不行，则应该抛出！

**基于SpringMVC框架的异常处理**

可以自定义一个专门用于处理异常的方法，该方法要求：

1. 访问权限应该是`public`；

2. 返回值与普通处理请求的方法相同；

3. 方法的名称可以自定义；

4. 方法的参数必须包含1个异常类型，例如`Throwable`或`Exception`等，表示将会捕获到的异常对象；

5. 必须添加`@ExceptionHandler`注解，该注解要求SSM环境中添加`<mvc:annotation-driven />`。

然后，在方法之前使用`@ExceptionHandler`注解，并在注解参数中定义需要处理的异常的类型：

	@ExceptionHandler(ServiceException.class)
	public ResponseResult<Void> handleException(Throwable e) {
		ResponseResult<Void> rr
			= new ResponseResult<Void>();
		rr.setMessage(e.getMessage());
		
		if (e instanceof UsernameDuplicateException) {
			// 400-用户名冲突
			rr.setState(400);
		} else if (e instanceof InsertException) {
			// 500-插入数据异常
			rr.setState(500);
		}
		
		return rr;
	}

这种处理异常的方法，只能作用于当前控制器类中的处理请求的方法，即当前类中的代码出现异常才可以被处理！为了统一处理，应该创建`BaseController`基类，把处理异常的方法放在基类中，然后，当前项目中所有的控制器类都应该继承自这个基类！

### 7. 用户-注册-前端界面

### 8. 用户-登录-持久层

**1. 分析SQL语句**

登录应该是根据用户名查询用户数据，且根据查询结果中的密码和用户输入的密码进行对比，以判断登录成功与否。

	SELECT
		uid, username, password, salt, avatar, is_delete
	FROM 
		t_user 
	WHERE 
		username=?

> 在SQL语句中，不区分英文大小写，所以，在查询时，不应该把密码作为查询条件之一，而是应该把密码作为查询结果的一部分，后续通过Java程序的equals()方法来对比密码！

**2. 接口与抽象方法**

由于此前设计“注册”时，已经定义了相关方法，所以，无需再次开发！

**3. 配置映射**

由于此前设计“注册”时，已经配置了相关映射，所以，只需要添加查询的字段列表即可！

	<!-- 根据用户名查询用户数据 -->
	<!-- User findByUsername(String username) -->
	<select id="findByUsername"
		resultType="cn.tedu.store.entity.User">
		SELECT 
			uid, username,
			password, salt, 
			avatar,
			is_delete AS isDelete
		FROM 
			t_user 
		WHERE 
			username=#{username}
	</select>

由于修改了程序代码，应该重新执行单元测试（无需重新编写），以测试功能是否正常。

### 9. 用户-登录-业务层

**1. 设计异常**

此次“登录”操作可能出现的异常有：用户名不存在、用户数据已被标记为删除、密码错误。

则应该在`cn.tedu.store.service.ex`创建异常类：

	UserNotFoundException
	PasswordNotMatchException

以上2个异常都应该继承自`ServiceException`。

**2. 接口与抽象方法**

在`IUserService`接口中添加抽象方法：

	User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException;

**3. 实现**

在`UserServiceImpl`中重写以上抽象方法：

	public User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException {
		// 根据参数username查询用户：User findByUsername(String username)
		// 判断查询结果是否为null
		// 是：抛出UserNotFoundException

		// 判断is_delete是否标记为已删除：isDelete属性值是否为1
		// 是：抛出UserNotFoundException

		// 从查询结果中获取盐值
		// 对参数password执行加密
		// 判断查询结果中的密码与刚加密结果是否一致
		// 是：
		// -- 返回查询结果
		// 否：抛出PasswordNotMatchException
	}

具体的实现为：

	@Override
	public User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException {
		// 根据参数username查询用户：User findByUsername(String username)
		User result = findByUsername(username);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"登录失败！尝试登录的用户不存在！");
		}

		// 判断is_delete是否标记为已删除：isDelete属性值是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"登录失败！尝试登录的用户不存在！");
		}

		// 从查询结果中获取盐值
		String salt = result.getSalt();
		// 对参数password执行加密
		String md5Password = getMd5Password(password, salt);
		// 判断查询结果中的密码与刚加密结果是否一致
		if (result.getPassword().equals(md5Password)) {
			// 是：准备返回结果，先去除部分不需要对外使用的数据
			result.setPassword(null);
			result.setSalt(null);
			result.setIsDelete(null);
			// 返回查询结果
			return result;
		} else {
			// 否：抛出PasswordNotMatchException
			throw new PasswordNotMatchException(
				"登录失败！错误密码！");
		}
	}

完成后，应该在`UserServiceTestCase`中编写并执行单元测试：

	@Test
	public void login() {
		try {
			String username = "root";
			String password = "1234";
			User data = service.login(username, password);
			System.err.println(data);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 10. 用户-登录-控制器层

**1. 处理异常**

此次的业务抛出了2种新的异常，分别是`UserNotFoundException`和`PasswordNotMatchException`，则应该在`BaseController`中添加对这2个异常的处理！

**2. 设计请求**

	请求路径：/users/login
	请求参数：String username(*), String password(*)
	请求方式：POST
	响应数据：User

**3. 处理请求**

	@GetMapping("/login")
	public ResponseResult<User> login(
		@RequestParam("username") String username,
		@RequestParam("password") String password) {
		User data = userService.login(username, password);
		return new ResponseResult<>(SUCCESS, data);
	}

完成后，可以通过`http://localhost:8080/users/login?username=java&password=123456`进行测试，完成后，将方法之前的注解调整为`@PostMapping`

### 11. 用户-登录-界面


















###【附】内存溢出 / 内存泄漏 / Leak

内存溢出的原因主要是：程序已经正常或非正常（出现异常导致崩溃）结束，但是相关连接对象（例如流对象）并没有关闭，仍保持连接！由于程序已经结束，该对象已经无法继续使用，但是，在JVM尝试GC时，发现该对象仍保持连接，则不会将其视为垃圾数据，则不会回收，进而导致该对象将一直存在于内存中，却又无法再使用！

可以认为：少量的内存溢出，其实并没有明显的危害！但是，每个程序员编写代码时，都应该尽量的避免任何可能出现的内存溢出！

内存溢出达到一定的量之后，就会影响程序的运行，甚至撑满整个内存，并出现“溢出”的现象！

解决内存溢出的根本做法：当连接型对象使用完毕之后，应该及时释放其占用的内存资源，通常这些类型都有例如`close()`类似的方法，使用完毕之后调用即可。

###【附】关于static

`static`关键字是用于修饰类的成员的，例如添加在属性、方法、内部类的声明语句中。

`static`表现的特性有：唯一，常驻。



