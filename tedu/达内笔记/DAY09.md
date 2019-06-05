### 34. 收货地址-设为默认-持久层

**1. 分析SQL语句**

如果要将指定的收货地址设置为默认：

	UPDATE xx SET is_default=1 WHERE aid=?

为了保证将原有的默认收货地址设置为非默认，还应该在执行以上操作之前：

	UPDATE xx SET is_default=0 WHERE uid=?

即：先将当前用户的所有收货地址都设置为非默认，再将指定的那一条设置为默认！这样操作，就可以不必关心此前哪一条才是默认收货地址！

当然，在执行这些操作之前，还需要确保“被操作的数据是存在的，并且，是归属当前用户的”！则需要执行的查询：

	SELECT uid FROM xx WHERE aid=? 

**2. 接口与抽象方法**

以上SQL语句对应的抽象方法可以是：

	Address findByAid(Integer aid);

	Integer updateNonDefault(Integer uid);

	Integer updateDefault(Integer aid);

**3. 配置映射**

配置的映射：

	<!-- 将指定用户的所有收货地址设置为非默认 -->
	<!-- Integer updateNonDefault(Integer uid) -->
	<update id="updateNonDefault">
		UPDATE
			t_address
		SET
			is_default=0
		WHERE 
			uid=#{uid}
	</update>
	
	<!-- 将指定的收货地址设置为默认 -->
	<!-- Integer updateDefault(Integer aid) -->
	<update id="updateDefault">
		UPDATE
			t_address
		SET
			is_default=1
		WHERE 
			aid=#{aid}
	</update>

	<!-- 根据收货地址id查询匹配的数据 -->
	<!-- Address findByAid(Integer aid) -->
	<select id="findByAid"
		resultType="cn.tedu.store.entity.Address">
		SELECT 
			uid
		FROM 
			t_address 
		WHERE 
			aid=#{aid} 
	</select>

对应的单元测试：

	@Test
	public void updateNonDefault() {
		Integer uid = 8;
		Integer rows = mapper.updateNonDefault(uid);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updateDefault() {
		Integer aid = 10;
		Integer rows = mapper.updateDefault(aid);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void findByAid() {
		Integer aid = 9;
		Address data = mapper.findByAid(aid);
		System.err.println(data);
	}
	
### 35. 收货地址-设为默认-业务层

**1. 设计异常**

整个数据的操作流程应该是：先检查，再全部设置为非默认，再把指定的那条设置为默认！

在检查时，可能出现“查询不到匹配的数据”的问题，此时，应该抛出`AddressNotFoundException`异常；并且，也可能出现非法访问，数据的uid与当前登录的用户的uid不相符，即尝试访问他人的数据，此时，应该抛出`AccessDeniedException`异常。

后续还会执行2次更新，都可能涉及`UpdateException`。

所以，此次需要创建2个新的异常类：`AddressNotFoundException`、`AccessDeniedException`。

**2. 接口与抽象方法**

	void setDefault(Integer uid, Integer aid) throws AddressNotFoundException, AccessDeniedException, UpdateException;

**3. 实现**

步骤分析为：

	@Transactional
	public void setDefault(Integer uid, Integer aid) throws AddressNotFoundException, AccessDeniedException, UpdateException {
		// 根据aid查询数据
		// 判断数据是否为null
		// 是：AddressNotFoundException

		// 判断参数uid与查询结果中的uid是否不一致
		// 是：AccessDeniedException

		// 全部设置为非默认

		// 把指定的设置为默认
	}

具体实现为：

	@Override
	@Transactional
	public void setDefault(Integer uid, Integer aid)
			throws AddressNotFoundException, AccessDeniedException, UpdateException {
		// 根据aid查询数据
		Address result = findByAid(aid);
		// 判断数据是否为null
		if (result == null) {
			// 是：AddressNotFoundException
			throw new AddressNotFoundException(
				"设置默认收货地址失败！尝试访问的数据不存在！");
		}

		// 判断参数uid与查询结果中的uid是否不一致
		if (!result.getUid().equals(uid)) {
			// 是：AccessDeniedException
			throw new AccessDeniedException(
				"设置默认收货地址失败！数据归属错误！");
		}

		// 全部设置为非默认
		updateNonDefault(uid);

		// 把指定的设置为默认
		updateDefault(aid);
	}

### 36. 收货地址-设为默认-控制器层

**1. 处理异常**

此次抛了2个新的异常：`AddressNotFoundException`、`AccessDeniedException`，应该在`BaseController`中进行处理。

**2. 设计请求**

	请求路径：/addresses/{aid}/set_default
	请求参数：HttpSession session
	请求方式：POST
	响应数据：ResponseResult<Void>

**3. 处理请求**

处理请求的方法应该是：

	@RequestMapping("/{aid}/set_default")
	public ResponseResult<Void> setDefault(
		@PathVariable("aid") Integer aid,
		HttpSession session) {

	}

具体实现为：

	@RequestMapping("/{aid}/set_default")
	public ResponseResult<Void> setDefault(
		@PathVariable("aid") Integer aid,
		HttpSession session) {
		// 从session中获取uid
		Integer uid = getUidFromSession(session);
		// 调用业务层对象执行
		addressService.setDefault(uid, aid);
		// 返回
		return new ResponseResult<>(SUCCESS);
	}

完成后，可以通过`http://localhost:8080/addresses/7/set_default`进行测试。

### 37. 收货地址-设为默认-界面

	<script type="text/javascript">
	$(document).ready(function(){
		showAddressList();
	});
	
	function showAddressList() {
		$.ajax({
			"url":"/addresses/",
			"type":"GET",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					var list = json.data;
					$("#address-list").empty();
					for (var i = 0; i < list.length; i++) {
						console.log(list[i].name);
						var html = '<tr>'
								+ '<td>#{tag}</td>'
								+ '<td>#{name}</td>'
								+ '<td>#{district}#{address}</td>'
								+ '<td>#{phone}</td>'
								+ '<td><a class="btn btn-xs btn-info" ><span class="fa fa-edit"></span> 修改</a></td>'
								+ '<td><a class="btn btn-xs add-del btn-info" ><span class="fa fa-trash-o"></span> 删除</a></td>'
								+ '<td><a class="btn btn-xs add-def btn-default" onclick="setDefault(#{aid})">设为默认</a></td>'
								+ '</tr>';
								
						html = html.replace("#{aid}", list[i].aid);	
						html = html.replace("#{tag}", list[i].tag);	
						html = html.replace("#{name}", list[i].name);	
						html = html.replace("#{district}", list[i].district);	
						html = html.replace("#{address}", list[i].address);	
						html = html.replace("#{phone}", list[i].phone);	
								
						$("#address-list").append(html);
					}
					$(".add-def:eq(0)").hide();
				} else {
					alert(json.message);
				}
			}
		});
	}
	
	function setDefault(aid) {
		$.ajax({
			"url":"/addresses/" + aid + "/set_default",
			"type":"POST",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					// alert("修改成功！");
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
	</script>

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

### 39. 收货地址-删除-业务层

**1. 设计异常**

**2. 接口与抽象方法**

**3. 实现**

### 40. 收货地址-删除-控制器层

**1. 处理异常**

**2. 设计请求**

	请求路径：
	请求参数：
	请求方式：
	响应数据：

**3. 处理请求**

### 41. 收货地址-删除-界面









###【附】基于SpringJDBC的事务

关于事务：使用事务可以保证同一个业务中的多条SQL语句产生的数据修改（增删改）要么全部成功，要么全部失败！

所以，可以认为：**如果某个业务中涉及超过1次的增、删、改操作（例如需要执行2次Update，或1次Update加上1次Delete），则应该使用事务！**

在使用了SpringJDBC后（无视使用哪种数据库，或使用哪种持久层框架），当需要使用事务来保障数据操作时，只需要在相关的业务方法之前添加`@Transactional`注解即可！

> 该注解也可以添加在业务类之前，表示该业务类中所有业务方法都是有事务的保障的！

在SpringJDBC中，事务的处理过程大致是：

	开启事务：begin

	try {
		执行若干条SQL
		提交：commit
	} catch (RuntimeException e) {
		回滚：rollback
	}

也就是说，框架在处理时，一旦执行的SQL语句抛出了`RuntimeException`，就会导致事务回滚。

所以，在开发时，应该保证：**所有的增、删、改操作都应该获取其受影响的行数，如果不是预期值，就必须抛出RuntimeException或其子孙类异常，则后续该操作被应用于事务中时，才可以实现回滚效果！**

所以，关于SpringJDBC中事务的应用小结：

1. 所有的增、删、改操作都应该获取其受影响的行数，如果不是预期值，就必须抛出RuntimeException或其子孙类异常，则后续该操作被应用于事务中时，才可以实现回滚效果！

2. 如果某个业务中涉及超过1次的增、删、改操作（例如需要执行2次Update，或1次Update加上1次Delete），则应该在业务方法之前添加`@Transactional`！

> 如果使用的不是SpringBoot，而是传统的SSM框架，则需要在Spring的配置文件中配置`<tx:annotation-driven transaction-manager="xxx" />`和`<bean  id="xxx" class="DataSourceTransactionManager"><property name="dataSource" ref="xxxxx" /></bean>`。
