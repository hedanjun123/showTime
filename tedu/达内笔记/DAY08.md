### 29. 收货地址-增加收货地址-获取省市区数据

在“增加收货地址”的页面处理中，需要以下功能：

- 获取全国所有省的列表：SELECT * FROM t_dict_district WHERE parent='86'

- 获取某个省所有市的列表：SELECT * FROM t_dict_district WHERE parent=省code

- 获取某个市所有区的列表：SELECT * FROM t_dict_district WHERE parent=市code

可以发现，以上3个功能对应的SQL语句是一样的：

	SELECT * FROM t_dict_district WHERE parent=?

在开发时，应该先创建`cn.tedu.store.entity.District`实体类：

	/**
	 * 省/市/区数据的实体类
	 */
	public class District implements Serializable {
	
		private static final long serialVersionUID = -2777570570541589252L;
	
		private Integer id;
		private String parent;
		private String code;
		private String name;

		// SET/GET/toString
	}

然后，创建`cn.tedu.store.mapper.DistrictMapper`持久层接口，并添加抽象方法：

	List<District> findByParent(String parent);

再复制得到`DistrictMapper.xml`文件，配置以上抽象方法的映射：

	<mapper namespace="cn.tedu.store.mapper.DistrictMapper">
		
		<!-- 获取所有省/某省所有市/某市所有区的列表 -->
		<!-- List<District> findByParent(String parent) -->
		<select id="findByParent"
			resultType="cn.tedu.store.entity.District">
			SELECT 
				code, name
			FROM 
				t_dict_district 
			WHERE 
				parent=#{parent}
			ORDER BY 
				code ASC
		</select>
		
	</mapper>

完成后，创建测试类，编写并执行测试方法：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class DistrictMapperTestCase {
	
		@Autowired
		public DistrictMapper mapper;
		
		@Test
		public void findByParent() {
			String parent = "999999";
			List<District> list = mapper.findByParent(parent);
			System.err.println("BEGIN:");
			for (District data : list) {
				System.err.println(data);
			}
			System.err.println("END.");
		}
		
	}

接下来应该是业务层，通常，查询功能没有太多业务，也没有相关的异常，则直接创建`cn.tedu.store.service.IDistrictService`业务层接口，并添加抽象方法：

	List<District> getByParent(String parent);

再创建`cn.tedu.store.service.impl.DistrictServiceImpl`业务层实现类，添加`@Service`注解，在类中添加持久层对象`@Autowired private DistrictMapper districtMapper`，私有化实现持久层方法，该类将实现`IDistrictService`接口，重写抽象方法：

	/**
	 * 处理省/市/区数据的业务层实现类
	 */
	@Service
	public class DistrictServiceImpl implements IDistrictService {
		
		@Autowired
		private DistrictMapper distrctMapper;
	
		@Override
		public List<District> getByParent(String parent) {
			return findByParent(parent);
		}
		
		/**
		 * 获取所有省/某省所有市/某市所有区的列表
		 * @param parent 获取省列表时，使用86；获取市列表时，使用省的代号；获取区列表时，使用市的代号
		 * @return 所有省/某省所有市/某市所有区的列表
		 */
		private List<District> findByParent(String parent) {
			return distrctMapper.findByParent(parent);
		}
	
	}

完成后，仍创建测试类，编写并执行测试方法：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class DistrictServiceTestCase {
	
		@Autowired
		public IDistrictService service;
		
		@Test
		public void getByParent() {
			String parent = "86";
			List<District> list = service.getByParent(parent);
			System.err.println("BEGIN:");
			for (District data : list) {
				System.err.println(data);
			}
			System.err.println("END.");
		}
		
	}

最后，还需要创建`cn.tedu.store.controller.DistrictController`控制器类，继承自`BaseController`，添加`@RestController`和`@RequestMapping("/districts")`注解，在类中声明`@Autowired private IDistrictService districtService;`业务层对象，然后，分析处理请求的方法：

	请求路径：/districts/
	请求参数：String parent(*)
	请求方式：GET
	响应数据：ResponseResult<List<District>>
	是否拦截：否，需要添加白名单

所以，先在`LoginInterceptorConfigurer`中添加白名单：

	excludePatterns.add("/districts/**");

然后在控制器类中添加处理请求的方法：

	@RestController
	@RequestMapping("/districts")
	public class DistrictController extends BaseController {
	
		@Autowired
		private IDistrictService districtService;
		
		@GetMapping("/")
		public ResponseResult<List<District>> 
			getByParent(@RequestParam("parent") String parent) {
			List<District> data
				= districtService.getByParent(parent);
			return new ResponseResult<>(SUCCESS, data);
		}
		
	}

最后，打开浏览器，无需登录，直接通过`http://localhost:8080/districts/?parent=86`进行测试。

关于前端界面的代码：

	<script type="text/javascript">
	$(document).ready(function(){
		showProvinceList();
		
		$("#city").append('<option value="0">----- 请选择 -----</option>');
		$("#area").append('<option value="0">----- 请选择 -----</option>');
	});
	
	$("#province").change(function() {
		showCityList();
		
		$("#area").empty();
		$("#area").append('<option value="0">----- 请选择 -----</option>');
	});
	
	$("#city").change(function() {
		showAreaList();
	});
	
	function showProvinceList() {
		$("#province").append('<option value="0">----- 请选择 -----</option>');
		
		$.ajax({
			"url":"/districts/",
			"data":"parent=86",
			"type":"GET",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					var list = json.data;
					for (var i = 0; i < list.length; i++) {
						console.log(list[i].name);
						var op = '<option value="' + list[i].code + '">' + list[i].name + '</option>';
						$("#province").append(op);
					}
				} else {
					alert(json.message);
				}
			}
		});
	}
	
	function showCityList() {
		$("#city").empty();
		
		$("#city").append('<option value="0">----- 请选择 -----</option>');
		
		if ($("#province").val() == 0) {
			return;
		}
		
		$.ajax({
			"url":"/districts/",
			"data":"parent=" + $("#province").val(),
			"type":"GET",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					var list = json.data;
					for (var i = 0; i < list.length; i++) {
						console.log(list[i].name);
						var op = '<option value="' + list[i].code + '">' + list[i].name + '</option>';
						$("#city").append(op);
					}
				} else {
					alert(json.message);
				}
			}
		});
	}
	
	function showAreaList() {
		$("#area").empty();
		
		$("#area").append('<option value="0">----- 请选择 -----</option>');
		
		if ($("#city").val() == 0) {
			return;
		}
		
		$.ajax({
			"url":"/districts/",
			"data":"parent=" + $("#city").val(),
			"type":"GET",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					var list = json.data;
					for (var i = 0; i < list.length; i++) {
						console.log(list[i].name);
						var op = '<option value="' + list[i].code + '">' + list[i].name + '</option>';
						$("#area").append(op);
					}
				} else {
					alert(json.message);
				}
			}
		});
	}
	</script>
		
	<script type="text/javascript">
	$("#btn-addnew").click(function(){
		$.ajax({
			"url":"/addresses/addnew",
			"data":$("#form-addnew").serialize(),
			"type":"POST",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					alert("增加成功！");
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
	
后续，为了得到省、市、区的中文名，还应该设计“根据代号得到名称”的功能！则先应该在`DistrictMapper`持久层接口中添加抽象方法：

	District findByCode(String code);

然后配置映射：

	<!-- 根据代号获取省/市/区的信息 -->
	<!-- District findByCode(String code) -->
	<select id="findByCode"
		resultType="cn.tedu.store.entity.District">
		SELECT 
			name
		FROM 
			t_dict_district 
		WHERE 
			code=#{code}
	</select>

编写并执行单元测试：

	@Test
	public void findByCode() {
		String code = "320102";
		District data = mapper.findByCode(code);
		System.err.println(data);
	}

持久层开发完毕后，再完成业务层，仍然先在业务层接口中添加抽象方法：

	/**
	 * 根据代号获取省/市/区的信息
	 * @param code 省/市/区的代号
	 * @return 匹配的省/市/区的信息，如果没有匹配的信息，则返回null
	 */
	District getByCode(String code);

在业务层实现类中实现以上方法：

	@Override
	public District getByCode(String code) {
		return findByCode(code);
	}

	/**
	 * 根据代号获取省/市/区的信息
	 * @param code 省/市/区的代号
	 * @return 匹配的省/市/区的信息，如果没有匹配的信息，则返回null
	 */
	private District findByCode(String code) {
		return distrctMapper.findByCode(code);
	}

最后，编写并执行测试：

	@Test
	public void getByCode() {
		String code = "330102";
		District data = service.getByCode(code);
		System.err.println(data);
	}

### 30. 收货地址-显示收货地址列表-持久层

**1. 分析SQL语句**

	SELECT 
		aid, name, district, address, phone, is_default, tag
	FROM 
		t_address 
	WHERE 
		uid=? 
	ORDER BY 
		is_default DESC, modified_time DESC

**2. 接口与抽象方法**

	List<Address> findByUid(Integer uid);

**3. 配置映射**

### 31. 收货地址-显示收货地址列表-业务层

**1. 设计异常**

无

**2. 接口与抽象方法**

	List<Address> getByUid(Integer uid);

**3. 实现**

### 32. 收货地址-显示收货地址列表-控制器层

**1. 处理异常**

无

**2. 设计请求**

	请求路径：/addresses/
	请求参数：HttpSession session
	请求方式：GET
	响应数据：ResponseResult<List<Address>>
	是否拦截：是

**3. 处理请求**

### 33. 收货地址-显示收货地址列表-界面












	

	