### 购物车-增加数量-持久层

点击页面中的加号和减号可以分别将购物车中商品的数量增加1或减少1，以增加为例，需要使用的SQL语句是：

	UPDATE 
		t_cart 
	SET 
		num=?, modified_user=?, modified_time=? 
	WHERE 
		cid=?

该SQL语句及对应的持久层功能已经存在，无需再开发。

在执行增加之前，应该先检查该数据是否存在，且数据归属是否正确（不允许修改别人的购物车中的数据），且还要获取原有数量，将原有数量增加1，得到新的数量值，则需要：

	SELECT uid, num FROM t_cart WHERE cid=?

该功能尚不存在，需要开发。

所以，需要在`CartMapper`接口中添加：

	Cart findByCid(Integer cid);

然后，在`CartMapper.xml`中配置映射：

	<!-- 根据购物车数据id获取购物车数据 -->
	<!-- Cart findByCid(Integer cid) -->
	<select id="findByCid"
		resultType="cn.tedu.store.entity.Cart">
		SELECT 
			uid, num 
		FROM 
			t_cart 
		WHERE 
			cid=#{cid}
	</select>

并编写、执行单元测试：

	@Test
	public void findByCid() {
		Integer cid = 6;
		Cart cart = mapper.findByCid(cid);
		System.err.println(cart);
	}

### 购物车-增加数量-业务层

此次操作的流程大致是：根据尝试增加数量的数据id去查询数据，并检查数据是否存在、数据归属是否正确，然后基于查询结果中的商品数量再增加1，最后执行更新。

如果查询不到匹配的数据，应该抛出：`CartNotFoundException`

如果数据归属有误，应该抛出：`AccessDeniedException`

如果执行更新出错，应该抛出：`UpdateException`

所以，需要创建`cn.tedu.store.service.ex.CartNotFoundException`异常类。

在业务层接口`ICartService`中添加抽象方法：

	void addNum(Integer uid, String username, Integer cid);

然后，在业务层实现类中，先私有化实现接口中新添加的方法：

再重写接口中的抽象方法：

	public void addNum(Integer uid, String username, Integer cid) {
		// 根据参数cid查询数据：findByCid(cid)
		// 判断查询结果是否为null
		// 是：CartNotFoundException

		// 判断查询结果中的uid与当前登录的用户id(参数uid)是否不一致
		// 是：AccessDeniedException

		// 暂不实现：判断商品的状态、库存等，即某商品是否可以存在于购物车中

		// 将查询结果中的商品数量加1
		// 执行更新：updateNum(cid, num, modifiedUser, modifiedTime)
	}

具体实现为：

	@Override
	public void addNum(Integer uid, String username, Integer cid)
			throws CartNotFoundException, AccessDeniedException, UpdateException {
		// 根据参数cid查询数据：findByCid(cid)
		Cart result = findByCid(cid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：CartNotFoundException
			throw new CartNotFoundException(
				"增加商品数量错误！尝试访问的数据不存在！");
		}

		// 判断查询结果中的uid与当前登录的用户id(参数uid)是否不一致
		if (!result.getUid().equals(uid)) {
			// 是：AccessDeniedException
			throw new CartNotFoundException(
				"增加商品数量错误！数据归属错误！");
		}

		// 暂不实现：判断商品的状态、库存等，即某商品是否可以存在于购物车中

		// 将查询结果中的商品数量加1
		Integer num = result.getNum() + 1;
		// 执行更新：updateNum(cid, num, modifiedUser, modifiedTime)
		Date now = new Date();
		updateNum(cid, num, username, now);
	}

测试：

	@Test
	public void addNum() {
		try {
			Integer uid = 8;
			String username = "系统管理员";
			Integer cid = 8;
			service.addNum(uid, username, cid);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 购物车-增加数量-控制器层

此次业务层抛了新的`CartNotFoundException`需要处理。

在`CartController`中添加处理请求的方法：

	@RequestMapping("/{id}/add_num")
	public ResponseResult<Void> addNum(
		@PathVariable("id") Integer cid,
		HttpSession session) {
		// 从session中获取uid和username
		// 执行
		// 返回
	}

可以通过`http://localhost:8080/carts/6/add_num`执行测试。

### 显示确认订单页-持久层

首先，在确认订单页需要显示当前登录的用户的收货地址列表，该功能已经全部完成，用户登录后，通过`http://localhost:8080/addresses/`即可获取收货地址数据，后续，在实现界面时，获取数据并显示即可！所以，无需考虑该部分功能的持久层、业务层、控制器层的开发！

在确认订单页面，还需要显示用户勾选的即将购买的商品，则客户端会提交所勾选的数据的id，这些数据是购物车中的数据，如果要显示这些数据，应该通过客户端提交的（用户勾选的）一系列id来执行查询，对应的SQL语句应该是：

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
		cid IN (?,?,?)
	ORDER BY 
		t_cart.modified_time DESC, cid DESC

则对应的抽象方法应该是：

	List<CartVO> findByCids(Integer[] cids);

映射应该是：

	<select id="findByCids" resultType="cn.tedu.store.vo.CartVO">
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
			cid IN (
				<foreach collection="array"
					item="cid" separator=",">
					#{cid}
				</foreach>
			)
		ORDER BY 
			t_cart.modified_time DESC, cid DESC
	</select>

单元测试：

	