### 11. 用户-登录-界面

在`login.html`中添加以下代码（从`register.html`中复制过来，并修改得到）：

	<script type="text/javascript">
	$("#btn-login").click(function(){
		$.ajax({
			"url":"/users/login",
			"data":$("#form-login").serialize(),
			"type":"POST",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					alert("登录成功！");
				} else {
					alert(json.message);
				}
			}
		});
	});
	</script>

然后，检查HTML代码，确保表单的id、登录按钮的id与以上代码保持一致，表单中的用户名、密码的输入框的name值与服务器端控制器要求提交的参数名称保持一致，且登录按钮的type是button。

### 12. 用户-修改密码-持久层

**1. 分析SQL语句**

修改密码时需要执行的SQL语句是：

	UPDATE 
		t_user 
	SET 
		password=?, modified_user=?, modified_time=? 
	WHERE 
		uid=?

以上SQL语句中，password的值应该是用户提交的新密码经过加密后的值，即：后续在操作时，需要先获取当前用户的盐值，才可以完成整个功能！包括在这个功能中，还应该先验证用户的原密码，则需要先获取用户的原密码的值！则需要查询功能：

	SELECT 
		password, salt, is_delete 
	FROM 
		t_user 
	WHERE 
		uid=?

**2. 接口与抽象方法**

在`UserMapper`接口中添加2个抽象方法：

	Integer updatePassword(
		@Param("uid") Integer uid, 
		@Param("password") String password, 
		@Param("modifiedUser") String modifiedUser, 
		@Param("modifiedTime") Date modifiedTime);

	User findByUid(Integer uid);

**3. 配置映射**

在`UserMapper.xml`中配置以上2个抽象方法对应的映射：

	<!-- 更新用户密码 -->
	<!-- Integer updatePassword(
		    @Param("uid") Integer uid, 
		    @Param("password") String password, 
		    @Param("modifiedUser") String modifiedUser, 
		    @Param("modifiedTime") Date modifiedTime) -->
	<update id="updatePassword">
		UPDATE 
			t_user 
		SET 
			password=#{password}, 
			modified_user=#{modifiedUser}, 
			modified_time=#{modifiedTime} 
		WHERE 
			uid=#{uid}
	</update>

	<!-- 根据用户id查询用户数据 -->
	<!-- User findByUid(Integer uid) -->
	<select id="findByUid"
		resultType="cn.tedu.store.entity.User">
		SELECT 
			password, salt, 
			is_delete AS isDelete
		FROM 
			t_user 
		WHERE 
			uid=#{uid}
	</select>

完成后，在`UserMapperTestCase`中编写并执行以上2个抽象方法的单元测试：

	@Test
	public void updatePassword() {
		Integer uid = 9;
		String password = "8888";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updatePassword(uid, password, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}

	@Test
	public void findByUid() {
		Integer uid = 9;
		User result = mapper.findByUid(uid);
		System.err.println(result);
	}

### 13. 用户-修改密码-业务层

**1. 设计异常**

当修改密码时，应该先执行查询操作，可能涉及的异常有`UserNotFoundException`（例如用户登录之后，数据被后台管理员删除，或者标记为删除）。

在执行修改之前，还应该验证原密码是否正确，则可能出现`PasswordNotMatchException`。

最后，在执行修改时，如果受影响的行数不是1，则应该抛出`UpdateException`，该异常尚不存在，则需要创建。

> 所有的增删改操作都是有匹配的异常的！

**2. 接口与抽象方法**

在`IUserService`接口中添加抽象方法：

	void changePassword(Integer uid, String username, String oldPassword, String newPassword) throws UserNotFoundException, PasswordNotMatchException, UpdateException;

> 抽象方法的名称应该与持久层接口中的任何方法的名称都不一样！

**3. 实现**

首先，将持久层新添加的2个方法复制到业务层实现类，声明为私有，并实现，在实现修改操作时，应该将方法的返回值改为`void`，在方法体中需要判断操作返回值，不是预期值时抛出异常：

	/**
	 * 更新用户密码
	 * @param uid 用户的id
	 * @param password 新密码
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 */
	private void updatePassword(
			Integer uid, String password, 
		    String modifiedUser, Date modifiedTime) {
		Integer rows = userMapper.updatePassword(uid, password, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException(
				"修改用户数据时出现未知错误！");
		}
	}

	/**
	 * 根据用户id查询用户数据
	 * @param uid 用户id
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	private User findByUid(Integer uid) {
		return userMapper.findByUid(uid);
	}

> 只要在持久层接口中声明了新的抽象方法（新的数据访问功能），在业务层的实现类中应该添加与之匹配的私有方法！

然后，实现接口中定义的抽象方法：

	public void changePassword(Integer uid, String username, String oldPassword, String newPassword) throws UserNotFoundException, PasswordNotMatchException, UpdateException {
		// 根据uid查询用户数据
		// 判断查询结果是否为null
		// 是：抛出UserNotFoundException

		// 判断查询结果中isDelete是否为1
		// 是：抛出UserNotFoundException

		// 从查询结果中获取盐值
		// 将oldPassword结合盐值加密，得到oldMd5Password
		// 判断查询结果中的密码（用户当前的真实密码）与oldMd5Password是否不匹配
		// 是：抛出PasswordNotMatchException

		// 将newPassword结合盐值加密，得到newMd5Password

		// 创建时间对象now
		// 执行修改密码：updatePassword(uid, newMd5Password, username, now)
	}

实现代码：

	@Override
	public void changePassword(Integer uid, String username, String oldPassword, String newPassword)
			throws UserNotFoundException, PasswordNotMatchException, UpdateException {
		// 根据uid查询用户数据
		User result = findByUid(uid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改密码失败！尝试访问的用户不存在！");
		}

		// 判断查询结果中isDelete是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改密码失败！尝试访问的用户不存在！");
		}

		// 从查询结果中获取盐值
		String salt = result.getSalt();
		// 将oldPassword结合盐值加密，得到oldMd5Password
		String oldMd5Password = getMd5Password(oldPassword, salt);
		// 判断查询结果中的密码（用户当前的真实密码）与oldMd5Password是否不匹配
		if (!result.getPassword().equals(oldMd5Password)) {
			// 是：抛出PasswordNotMatchException
			throw new PasswordNotMatchException(
				"修改密码失败！原密码错误！");
		}

		// 将newPassword结合盐值加密，得到newMd5Password
		String newMd5Password = getMd5Password(newPassword, salt);
		// 创建时间对象now
		Date now = new Date();
		// 执行修改密码：updatePassword(uid, newMd5Password, username, now)
		updatePassword(uid, newMd5Password, username, now);
	}

最后，在`UserServiceTestCase`中编写并执行单元测试：

	@Test
	public void changePassword() {
		try {
			Integer uid = 1000;
			String username = "超级管理员";
			String oldPassword = "1234";
			String newPassword = "8888";
			service.changePassword(uid, username, oldPassword, newPassword);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 14. 用户-修改密码-控制器层

**1. 处理异常**

此次在业务层抛了新的异常`UpdateException`，则需要在`BaseController`中进行处理。

**2. 设计请求**

	请求路径：/users/change_password
	请求参数：String old_password, String new_password, HttpSession
	请求方式：POST
	响应数据：无

**3. 处理请求**

	@RequestMapping("/change_password")
	public ResponseResult<Void> changePassword(
		@RequestParam("old_password") String oldPassword,
		@RequestParam("new_password") String newPassword,
		HttpSession session) {
		// 从session中获取uid和username
		// 执行修改密码：service.changePassword(uid,username,oldPassword,newPassword)
		// 返回结果
	}

首先，因为项目中多处需要获取当前登录的用户的id，则在`BaseController`中添加方法，以简化获取用户id的代码：

	/**
	 * 从Session获取当前登录的用户id
	 * @param session HttpSession对象
	 * @return 当前登录的用户id
	 */
	protected final Integer getUidFromSession(HttpSession session) {
		return Integer.valueOf(session.getAttribute("uid").toString());
	}

在`UserController`中处理请求的代码如下：

	@RequestMapping("/change_password")
	public ResponseResult<Void> changePassword(
		@RequestParam("old_password") String oldPassword,
		@RequestParam("new_password") String newPassword,
		HttpSession session) {
		// 从session中获取uid和username
		Integer uid = getUidFromSession(session);
		String username = session.getAttribute("username").toString();
		// 执行修改密码：service.changePassword(uid,username,oldPassword,newPassword)
		userService.changePassword(uid, username, oldPassword, newPassword);
		// 返回结果
		return new ResponseResult<>(SUCCESS);
	}

> 处理请求的方法的名称可以与业务层中抽象方法的名称一致。

完成后，打开浏览器，先登录，然后通过`http://localhost:8080/users/change_password?old_password=1234&new_password=8888`进行测试。

### 15. 登录拦截器

首先，创建拦截器类：

	/**
	 * 登录拦截器
	 */
	public class LoginInterceptor implements HandlerInterceptor {
	
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			HttpSession session = request.getSession();
			if (session.getAttribute("uid") == null) {
				response.sendRedirect("/web/login.html");
				return false;
			}
			return true;
		}
	
	}

在SpringBoot项目中，并没有xml的配置文件，相关配置都是通过实现`WebMvcConfigurer`接口，并在配置类之前添加`@Configration`注解来实现的：

	@Configuration
	public class LoginInterceptorConfigurer
		implements WebMvcConfigurer {
	
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// 拦截路径：必须登录才可以访问
			List<String> patterns = new ArrayList<>();
			patterns.add("/**");
			
			// 白名单：在黑名单范围内，却不需要登录就可以访问
			List<String> excludePatterns = new ArrayList<>();
			excludePatterns.add("/bootstrap3/**");
			excludePatterns.add("/css/**");
			excludePatterns.add("/js/**");
			excludePatterns.add("/images/**");
			
			excludePatterns.add("/web/register.html");
			excludePatterns.add("/users/reg");
			excludePatterns.add("/web/login.html");
			excludePatterns.add("/users/login");
			
			// 注册拦截器
			registry
				.addInterceptor(new LoginInterceptor())
				.addPathPatterns(patterns)
				.excludePathPatterns(excludePatterns);
		}
		
	}

> 早期的做法是推荐继承自`WebMvcConfigurerAdapter`，这个抽象类是使用空实现的做法实现了`WebMvcConfigurer`接口中所有抽象方法，在Java 8开始，允许在接口中直接将方法声明为空实现的，所以，`WebMvcConfigurerAdapter`就被声明为已过期的类。