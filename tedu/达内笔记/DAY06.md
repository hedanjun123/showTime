### 20. 用户-上传头像-持久层

**1. 分析SQL语句**

当用户上传头像时，服务器需要：

1. 存储用户上传的头像文件；

2. 将存储的文件的路径记录在数据库中。

对于持久层开发来说，需要做的就是将存储文件的路径更新到用户表中：

	UPDATE t_user 
	SET avatar=?, modified_user=?, modified_time=? 
	WHERE uid=?

**2. 接口与抽象方法**

在`UserMapper`接口中添加抽象方法：

	Integer updateAvatar(
		@Param("uid") Integer uid, 
		@Param("avatar") String avatar, 
		@Param("modifiedUser") String modifiedUser, 
		@Param("modifiedTime") Date modifiedTime);

**3. 配置映射**

配置的映射：

	<!-- 更新用户头像 -->
	<!-- Integer updateAvatar(
		    @Param("uid") Integer uid, 
		    @Param("avatar") String avatar, 
		    @Param("modifiedUser") String modifiedUser, 
		    @Param("modifiedTime") Date modifiedTime) -->
	<update id="updateAvatar">
		UPDATE 
			t_user 
		SET 
			avatar=#{avatar}, 
			modified_user=#{modifiedUser}, 
			modified_time=#{modifiedTime} 
		WHERE 
			uid=#{uid}
	</update>

单元测试：

	@Test
	public void updateAvatar() {
		Integer uid = 10;
		String avatar = "这里应该是头像的路径";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updateAvatar(uid, avatar, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}

### 21. 用户-上传头像-业务层

**1. 设计异常**

请参考：修改个人资料

**2. 接口与抽象方法**

请参考：修改个人资料

抽象方法的声明：

	/**
	 * 更新个人头像
	 * @param avatar 头像路径
	 * @throws UserNotFoundException 用户数据不存在
	 * @throws UpdateException 更新数据异常
	 */
	void changeAvatar(Integer uid, String avatar) 
			throws UserNotFoundException, 
				UpdateException;

**3. 实现**

请参考：修改个人资料

先私有化实现持久层中新添加的方法：

	/**
	 * 更新用户头像
	 * @param uid 用户的id
	 * @param avatar 头像的路径
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 */
	private void updateAvatar(
			Integer uid, String avatar, 
		    String modifiedUser, Date modifiedTime) {
		Integer rows = userMapper.updateAvatar(uid, avatar, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException(
				"修改用户数据时出现未知错误！");
		}
	}

实现的抽象方法：

	@Override
	public void changeAvatar(Integer uid, String avatar) throws UserNotFoundException, UpdateException {
		// 根据uid查询用户数据
		User result = findByUid(uid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改头像失败！尝试访问的用户不存在！");
		}

		// 判断查询结果中isDelete是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改头像失败！尝试访问的用户不存在！");
		}
						
		// 向user中封装modifiedUser和modifiedTime
		String modifiedUser = result.getUsername();
		Date modifiedTime = new Date();
		// 执行更新
		updateAvatar(uid, avatar, modifiedUser, modifiedTime);
	}

单元测试：

	@Test
	public void changeAvatar() {
		try {
			Integer uid = 10;
			String avatar = "新头像的路径";
			service.changeAvatar(uid, avatar);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 22. 用户-上传头像-控制器层

**1. 处理异常**

创建上传时可能涉及的异常类：

	RuntimeException
	-- cn.tedu.store.controller.ex.FileUploadException
	-- -- cn.tedu.store.controller.ex.FileEmptyException
	-- -- cn.tedu.store.controller.ex.FileSizeException
	-- -- cn.tedu.store.controller.ex.FileContentTypeException
	-- -- cn.tedu.store.controller.ex.FileIllegalStateException
	-- -- cn.tedu.store.controller.ex.FileIOException

然后，需要在`BaseController`中添加对以上5种实际抛出的异常（不包括FileUploadException）的处理，在处理之前，应该修改原有的处理异常的方法之前的注解：

	@ExceptionHandler({ServiceException.class, FileUploadException.class})

然后，在处理异常的方法中添加5组else if语句，对这5种异常进行处理！

**2. 设计请求**

	请求路径：/users/change_avatar
	请求参数：HttpServletRequest request, MultipartFile file
	请求方式：POST
	响应数据：ResponseResult<String>
	是否拦截：是，无需修改配置

**3. 处理请求**

	// 确定上传文件的名称：UPLOAD_DIR
	private static final String UPLOAD_DIR = "upload";
	// 确定上传文件的最大大小
	private static final long UPLOAD_MAX_SIZE = 1 * 1024 * 1024;
	// 确定允许上传的类型的列表
	private static final List<String> UPLOAD_CONTENT_TYPES
		= new ArrayList<>();

	static {
		UPLOAD_CONTENT_TYPES.add("xxx");
		UPLOAD_CONTENT_TYPES.add("xxx");
		UPLOAD_CONTENT_TYPES.add("xxx");
		UPLOAD_CONTENT_TYPES.add("xxx");
	}

	@RequestMapping("/change_avatar")
	public ResponseResult<String> changeAvatar(
		HttpServletRequest request, 
		@RequestParam("file") MultipartFile file) {
		// 检查文件是否为空
		// 为空：抛出异常：FileEmptyException

		// 检查文件大小
		// 超出范围(> UPLOAD_MAX_SIZE)：抛出异常：FileSizeException

		// 检查文件类型
		// 类型不符(contains()为false)：抛出异常：FileContentTypeException

		// 确定文件夹路径：request.getServletContext().getRealPath(UPLOAD_DIR);
		// 创建上传文件夹的File对象parent
		// 检查文件夹是否存在，如果不存在，则创建

		// 获取原文件名：file.getOriginalFilename()
		// 从原文件名中得到扩展名
		// 确定文件名：uuid/nanoTime/...

		// 创建dest对象：new File(parent, filename);
		// try
		// 执行上传：file.transferTo(dest);
		// catch:IllegalStateException：抛出FileIllegalStateException
		// catch:IOException：抛出FileIOException

		// 获取uid：getUidFromSession(request.getSession());
		// 生成avatar：/UPLOAD_DIR/文件名.扩展名

		// 执行更新：userService.changeAvatar(uid, avatar);
		// 返回成功
	}

### 23. 用户-上传头像-界面












### 【附】 基于SpringMVC的文件上传【续】

**6. 关于上传的文件夹**

在处理请求的方法中添加参数`HttpServletRequest`，然后调用该参数对象的`getServletContext().getRealPath("upload")`即可获取到`webapp`下的名为`upload`的文件夹的实际路径，例如`D:\apache-tomcat-7.0.54\wtpwebapps\SPRINGMVC-03-UPLOAD\upload\`。

因为上传的文件大多后续将需要通过http方式被访问，所以，上传文件夹是应该在`webapp`之下！则应该通过以上方式获取文件夹的位置。

**7. 关于上传的文件名**

如果同一个项目中，上传的所有文件都将在同一个文件夹中，则需要保证每个文件的文件名是不相同的，否则，将会出现后续上传的文件会覆盖前序上传的文件！

如果要保证文件名是唯一的，可以使用UUID，也可以使用System.nanoTime()，或使用时间与随机数的组合等等多种方式！

通常，上传的文件的扩展名还是由原文件来决定，即上传过程中，不改变文件的扩展名，通过`MultipartFile`的`getOriginalFilename()`方法可以获取上传的文件的原名（该文件在客户端计算机中的原始名称），然后通过String类的API即可获取文件的扩展名：

	String originalFilename = file.getOriginalFilename();
	int beginIndex = originalFilename.lastIndexOf(".");
	String suffix = "";
	if (beginIndex > 0) {
		suffix = originalFilename.substring(beginIndex);
	}

**8. 关于MultipartFile接口的常用方法**

- `String getOriginalFilename()`：获取原文件名，即文件在客户端计算机中的名称；

- `boolean isEmpty()`：判断上传的文件是否为空，如果在上传表单中没有选中文件，或选中的文件没有内容（0字节），将视为空，则返回true，否则返回false；

- `long getSize()`：获取上传的文件的大小，以字节为单位，可以用于限制上传的文件的大小；

- `String getContentType()`：获取上传的文件的文档类型，其值是文件的MIME类型，可以用于限制上传的文件的类型；

- `InputStream getInputStream()`：获取上传的文件的输入流，主要用于自定义存储文件的过程，当自定义存储时，就不再需要调用`transferTo()`方法了；

- `void transferTo(File dest)`：将上传的文件存储为指定的文件。

**9. 关于MultipartResolver的配置**

在Spring的配置文件中，配置`CommonsMultipartResolver`时，可以为某些属性注入值，来完成某些配置：

- `maxUploadSize`：最大上传尺寸，以字节为单位，假设设置值为10M，则无论一次性上传多少个文件，其总和不允许超过10M；

- `maxUploadSizePerFile`：上传的每个文件的最大尺寸，以字节为单位，假设设置值是10M，如果一次上传多个文件，则每个文件的大小都不可以超过10M，但是，多个文件的总和是可以超过10M的；

- `defaultEncoding`：默认编码。

