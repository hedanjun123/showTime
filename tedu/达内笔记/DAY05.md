### 16. 用户-个人资料-持久层

**1. 分析SQL语句**

执行修改个人资料的SQL语句是：

	UPDATE 
		t_user 
	SET 
		phone=?,email=?,
		gender=?,
		modified_user=?,modified_time=?
	WHERE 
		uid=?

在操作数据之前，还是应该执行相关的检查，检查数据是否存在，检查是否被标记为删除，当然，这些检查可以使用已有的查询功能来完成！

另外，在修改个人资料之前，还应该将当前登录的用户的资料显示在界面中，涉及的数据有：用户名、电话、邮箱、性别，则需要：

	SELECT username,phone,email,gender FROM t_user WHERE uid=?

当然，根据uid查询数据的功能已经存在，只需要在其映射的SQL语句中补充查询的字段即可！

**2. 接口与抽象方法**

关于修改个人资料的抽象方法：

	Integer updateInfo(User user);

**3. 配置映射**

更新个人资料的映射：

	<!-- 更新个人资料 -->
	<!-- Integer updateInfo(User user) -->
	<update id="updateInfo">
		UPDATE 
			t_user 
		SET 
			phone=#{phone},email=#{email},
			gender=#{gender},
			modified_user=#{modifiedUser},
			modified_time=#{modifiedTime}
		WHERE 
			uid=#{uid}
	</update>

另外，修改原有`findByUid`方法的映射，补充查询的字段：

	<!-- 根据用户id查询用户数据 -->
	<!-- User findByUid(Integer uid) -->
	<select id="findByUid"
		resultType="cn.tedu.store.entity.User">
		SELECT 
			username, phone,
			email, gender,
			password, salt, 
			is_delete AS isDelete
		FROM 
			t_user 
		WHERE 
			uid=#{uid}
	</select>

完成后，编写并执行单元测试：

	@Test
	public void updateInfo() {
		User user = new User();
		user.setUid(8);
		user.setGender(0);
		user.setPhone("13100131001");
		user.setEmail("root@tedu.cn");
		user.setModifiedUser("超级管理员");
		user.setModifiedTime(new Date());
		Integer rows = mapper.updateInfo(user);
		System.err.println("rows=" + rows);
	}

### 17. 用户-个人资料-业务层

**1. 设计异常**

修改个人资料之前，还是应该检查用户数据是否存在、是否被标记为删除，涉及`UserNotFoundException`，在执行更新时，涉及`UpdateException`。以上异常均已创建，无需创建新的异常。

**2. 接口与抽象方法**

	void changeInfo(User user) throws UserNotFoundException, UpdateException;

**3. 实现**

首先，将持久层新添加的方法在业务层中实现为私有方法：

然后，实现接口中新添加的抽象方法：

	public void changeInfo(User user) throws UserNotFoundException, UpdateException {
		// 根据user.getUid()查询用户数据
		// 判断查询结果是否为null
		// 是：抛出UserNotFoundException

		// 判断查询结果中isDelete是否为1
		// 是：抛出UserNotFoundException

		// 向user中封装modifiedUser和modifiedTime
		// 执行更新
	}

具体实现为：

	@Override
	public void changeInfo(User user) throws UserNotFoundException, UpdateException {
		// 根据uid查询用户数据
		User result = findByUid(user.getUid());
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
				
		// 向user中封装modifiedUser和modifiedTime
		user.setModifiedUser(result.getUsername());
		user.setModifiedTime(new Date());
		// 执行更新
		updateInfo(user);
	}

完成后，编写并执行单元测试：

	@Test
	public void changeInfo() {
		try {
			User user = new User();
			user.setUid(8);
			user.setGender(1);
			user.setPhone("13100131009");
			user.setEmail("root@qq.com");
			service.changeInfo(user);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 18. 用户-个人资料-控制器层

**1. 处理异常**

此次业务层没有抛出新的异常，则无需处理！

**2. 设计请求**

	请求路径：/users/change_info
	请求参数：User user, HttpSesion session
	请求方式：POST
	响应数据：Void
	是否拦截：是，无需修改配置，因为默认拦截 /**

**3. 处理请求**

	@RequestMapping("/change_info")
	public ResponseResult<Void> changeInfo(User user, HttpSession session) {
		// 封装uid
		Integer uid = getUidFromSession(session);
		user.setUid(uid);
		// 执行修改个人资料
		userService.changeInfo(user);
		// 返回
		return new ResponseResult<>(SUCCESS);
	}

完成后，可以通过`http://localhost:8080/users/change_info?phone=123456&email=hello@tedu.cn&gender=0`进行测试。

### 19. 用户-个人资料-界面

在“执行修改”之前，应该保证“当页面打开时就能显示当前登录的用户的信息”！

这些信息应该是打开页面时就直接向服务器端发出请求，由服务器端响应时提供的！

这就要求服务器提供“获取当前登录的用户的信息”功能！由于目前没有这个功能，所以需要从持久层开始开发这个功能！

**获取当前登录的用户数据-持久层**

直接使用已有的`findByUid()`即可！无需再次开发！

**获取当前登录的用户数据-业务层**

需要在`IUserService`接口中添加抽象方法：

	User getByUid(Integer uid);

然后，在`UserServiceImpl`类中实现这个方法：

	@Override
	public User getByUid(Integer uid) {
		// 根据uid查询用户数据
		User result = findByUid(uid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"获取用户信息失败！尝试访问的用户不存在！");
		}

		// 判断查询结果中isDelete是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"获取用户信息失败！尝试访问的用户不存在！");
		}
		
		// 在返回之前隐藏不向外提供的数据
		result.setPassword(null);
		result.setSalt(null);
		result.setIsDelete(null);
		
		// 执行返回
		return result;
	}

完成后，编写并执行单元测试：

	@Test
	public void getByUid() {
		try {
			Integer uid = 8;
			User data = service.getByUid(uid);
			System.err.println(data);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

**获取当前登录的用户数据-控制器层**

	@GetMapping("/info")
	public ResponseResult<User> getByUid(HttpSession session) {
		// 获取uid
		Integer uid = getUidFromSession(session);
		// 查询用户数据
		User data = userService.getByUid(uid);
		// 返回
		return new ResponseResult<User>(SUCCESS, data);
	}

完成后，在浏览器中通过`http://localhost:8080/users/info`直接访问即可测试。

**发出请求并处理结果**

当页面加载时，应该向服务器发出请求，获取当前登录的用户数据：

	<script type="text/javascript">
	$(document).ready(function(){
		$.ajax({
			"url":"/users/info",
			"type":"GET",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					// 将服务器响应的数据显示到各控件中
					$("#username").val(json.data.username);
					$("#phone").val(json.data.phone);
					$("#email").val(json.data.email);
					
					var genderRadio = json.data.gender == 0 
						? $("#gender-female") : $("#gender-male"); 
					genderRadio.attr("checked", "checked");
				} else {
					alert(json.message);
					// 退出登录
				}
			}
		});
	});
	</script>

当点击按钮时：

	<script type="text/javascript">
	$("#btn-change-info").click(function(){
		$.ajax({
			"url":"/users/change_info",
			"data":$("#form-change-info").serialize(),
			"type":"POST",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					alert("修改成功！");
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

### 【附】 基于SpringMVC的文件上传

**1. 创建项目**

创建新项目：

- Group Id：cn.tedu.spring

- Artifact Id：SPRINGMVC-03-UPLOAD

- Packaging：war

然后，按照传统方式完成项目的创建（添加web.xml；添加Tomcat Runtime，复制pom.xml中的依赖，复制web.xml中的配置，复制spring的配置文件。注意：检查spring的配置文件是否存在多余且不可用的配置，如果有，则删除）。

**2. 制作上传页面**

上传文件的页面只要求是html页面即可，不需要是jsp页面(可以使用jsp页面)。

上传使用的控件是：

	<input type="file" />

并且，上传时的表单必须配置`enctype`属性，并且请求类型必须是`post`类型：

	<form method="post" enctype="multipart/form-data">

整体设计可以是：

	<h1>基于SpringMVC的文件上传</h1>
	<form enctype="multipart/form-data">
		<p>请选择您要上传的文件</p>
		<p><input type="file" /></p>
		<p><input type="submit" value="上传" />
	</form>

**3. 添加依赖**

基于SpringMVC的文件上传需要添加`spring-webmvc`依赖，并添加`commons-fileupload`依赖：

	<dependency>
		<groupId>commons-fileupload</groupId>
		<artifactId>commons-fileupload</artifactId>
		<version>1.4</version>
	</dependency>

**4. 配置MultipartResolver**

在spring的配置文件中添加：

	<!-- 文件上传：MultipartResolver -->
	<bean id="multipartResolver" 
		class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
	</bean>

注意：此项配置中，`<bean>`的`id`必须是`multipartResolver`！

该`<bean>`也可以添加更多的配置，但是，不是必须的！

此次项目中，需要开启注解驱动：

	<!-- 注解驱动 -->
	<mvc:annotation-driven />

**5. 开发控制器接收上传请求**

创建控制器类`cn.tedu.spring.FileUploadController`，并添加处理请求的方法：

	@Controller
	public class FileUploadController {
	
		@RequestMapping("/upload.do")
		public String handleUpload(
			@RequestParam("file") MultipartFile file) {
			return null;
		}
		
	}

注意：客户端提交的上传的文件需要声明为`MultipartFile`类型（这是一个接口类型，实际类型是`CommonsMultipartFile`），并且，需要添加`@RequestParam`注解，如果没有添加该注解，在某些版本中可能会出错。

当然，以上控制器的方法确定后，前端页面中`<form>`的`action`属性和上传控件的`name`属性应该与之保持一致：

	<form method="post" action="upload.do" enctype="multipart/form-data">
		<input type="file" name="file" />

在控制器中，处理请求的方法中的参数`MultipartFile`其实就是客户选择上传的文件，直接调用`transferTo()`方法将其保存到某个文件即可：

	@RequestMapping("/upload.do")
	public String handleUpload(
		@RequestParam("file") MultipartFile file) 
			throws IllegalStateException, IOException {
		File dest = new File("d:/12345.png");
		file.transferTo(dest);
		return null;
	}

明天需要使用到：

- File类的多种构造方法

- File类的mkdir() / mkdirs()

- File类的exists()
























