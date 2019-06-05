package cn.tedu.store.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.tedu.store.entity.ResponseResult;
import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.RequestArgumentException;
import cn.tedu.store.service.ex.UploadFileContentTypeException;
import cn.tedu.store.service.ex.UploadFileSizeLimitException;
import cn.tedu.store.service.ex.UploadIOException;
import cn.tedu.store.service.ex.UploadStateException;
import cn.tedu.store.util.TextValidator;
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	/**
	 * 用户上传的头像的最大尺寸，单位：字节
	 */
	private static final long AVATAR_MAX_SIZE = 1 * 1024 * 1024;
	/**
	 * 头像类型白名单
	 */
	private static final List<String> AVATAR_TYPE_WHITE_LIST = new ArrayList<String>();
	
	@PostConstruct
	
	public void init() {
		AVATAR_TYPE_WHITE_LIST.add("image/jpeg");
		AVATAR_TYPE_WHITE_LIST.add("image/png");
	}
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value="/handle_reg.do", method=RequestMethod.POST)
	@ResponseBody
	/**
	 * 当前方法的返回值中的泛型表示需要给客户端的结果中，除了操作状态和提示信息以外，还给什么数据
	 * @param user
	 * @return
	 */
	public ResponseResult<Void> handleReg(
			User user) {
		/**
		 *  验证数据格式，如果不符合，则直接响应，提示错误
		 */
		if (!TextValidator.checkUsername(user.getUsername())) {
			return new ResponseResult<Void>(301, "用户名格式不正确！");
		}
		if (!TextValidator.checkPassword(user.getPassword())) {
			return new ResponseResult<Void>(302, "密码格式不正确！");
		}
				
		/**
		 *  调用业务层对象实现注册
		 */
		userService.reg(user);
		/**
		 *  执行返回
		 */
		return new ResponseResult<Void>();
	}
	
	@RequestMapping(value="/handle_login.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleLogin(
		@RequestParam("username") String username,
		@RequestParam("password") String password,
		HttpSession session) {
		/**
		 *  验证数据格式，如果不符合，则直接响应，提示错误
		 */
		if (!TextValidator.checkUsername(username)) {
			return new ResponseResult<Void>(301, "用户名格式不正确！");
		}
		if (!TextValidator.checkPassword(password)) {
			return new ResponseResult<Void>(302, "密码格式不正确！");
		}
		
		/**
		 * 调用业务层对象的login()方法，并获取返回值
		 */
		User user = 
				userService.login(
					username, password);
		/**
		 *  将用户id和用户名封装到session中
		 */
		session.setAttribute("uid", user.getId());
		session.setAttribute("username", user.getUsername());
		/**
		 * 返回
		 */
		return new ResponseResult<Void>();
	}
	
	@RequestMapping(value="/change_password.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleChangePassword(
		@RequestParam("old_password") String oldPassword,
		@RequestParam("new_password") String newPassword,
		HttpSession session) {
		/**
		 *  验证密码格式
		 */
		if (!TextValidator.checkPassword(oldPassword)) {
			return new ResponseResult<Void>(302, "原密码格式错误！");
		}
		if (!TextValidator.checkPassword(newPassword)) {
			return new ResponseResult<Void>(302, "新密码格式错误！");
		}

		/**
		 *  从Session中获取当前用户的id
		 */
		Integer id = getUidFromSession(session);
		/**
		 *  通过业务执行修改密码
		 */
		userService.changePassword(id, oldPassword, newPassword);
		/**
		 * 返回
		 */
		return new ResponseResult<Void>();
	} 
	
	@RequestMapping(value="/change_info.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> 
		handleChangeInfo(
		User user, HttpSession session) {
		/**
		 *  获取id
		 */
		Integer uid = getUidFromSession(session);
		/**
		 *  执行
		 */
		user.setId(uid);
		userService.changeInfo(user);
		/**
		 *  返回
		 */
		return new ResponseResult<Void>();
	}
	
	/*
	 * 跳转界面的同时，显示用户资料信息
	 * */
	@RequestMapping("/info.do")
	@ResponseBody
	public ResponseResult<User> getInfo(HttpSession session) {
		/**
		 *  获取uid
		 */
		Integer uid = getUidFromSession(session);
		/**
		 *  查询
		 */
		User user = userService.findUserById(uid);
		/**
		 *  创建返回值对象
		 */
		ResponseResult<User> rr
			= new ResponseResult<User>();
		/**
		 * 把查询结果封装到返回值对象的data属性中
		 */
		rr.setData(user);
		/**
		 *  返回
		 */
		return rr;
	}
	
	@RequestMapping(value="/upload.do",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<String> handleUpload(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam("file") CommonsMultipartFile file) {
		/**
		 *  检查是否上传了文件
		 */
		if (file.isEmpty()) {
			throw new RequestArgumentException(
				"没有选择上传的文件，或上传的文件的内容为空！");
		}
		/**
		 *  检查文件大小
		 */
		long fileSize = file.getSize();
		if (fileSize > AVATAR_MAX_SIZE) {
			throw new UploadFileSizeLimitException(
				"上传的文件大小超出限制！限制值为" + (AVATAR_MAX_SIZE / 1024) + "KByte。");
		}
		/**
		 *  检查文件类型
		 */
		String contentType = file.getContentType();
		if (!AVATAR_TYPE_WHITE_LIST
				.contains(contentType)) {
			throw new UploadFileContentTypeException(
				"上传文件类型错误！允许的文件类型：" + AVATAR_TYPE_WHITE_LIST);
		}
		
		/**
		 * 获取当前登录的用户的id
		 */
		Integer id = getUidFromSession(session);
		
		/**
		 *  用户上传的文件存储到的文件夹的名称
		 */
		String uploadDirName = "upload";
		/**
		 * 用户上传的文件存储到的文件夹的路径
		 */
		String parentDirPath
			= request.getServletContext()
				.getRealPath(uploadDirName);
		/**
		 *  用户上传的文件存储到的文件夹
		 */
		File parentDir = new File(parentDirPath);
		/**
		 *  确保文件夹存在
		 */
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		
		/**
		 *  获取原始文件名
		 */
		String originalFileName = file.getOriginalFilename();
		/*
		 *  获取原始文件的扩展名
		 */
		int beginIndex = originalFileName.lastIndexOf(".");
		String suffix = originalFileName.substring(beginIndex);
		/*
		 *  用户上传的文件存储的文件名
		 */
		String fileName = getFileName(id) + suffix;
		/*
		 *  确定用户上传的文件在服务器端的路径
		 */
		String avatar = uploadDirName + "/" + fileName;
		
		/*
		 *  用户上传的文件存储到服务器端的文件对象
		 */
		File dest = new File(parentDir, fileName);
		
		/*
		 * 将用户上传的文件存储到指定文件夹
		 */
		
		try {
			file.transferTo(dest);
		} catch (IllegalStateException e) {
			throw new UploadStateException("读取文件中断，文件路径可能已经发生变化！");
		} catch (IOException e) {
			throw new UploadIOException("读取数据出错！文件可能已被移动、删除，或网络连接中断！");
		}
		/**
		 *  将用户的头像数据更新到数据表
		 */
		userService.changeAvatar(id, avatar);
		
		/*
		 *  返回
		 */
		ResponseResult<String> rr
			= new ResponseResult<String>();
		rr.setData(avatar);
		return rr;
	}
	
	/**
	 * 获取上传文件的文件名，文件名的命名规则是：uid-yyyyMMddHHmmss
	 * @param uid 用户id
	 * @return 匹配格式的字符串
	 */
	private String getFileName(Integer uid) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		return uid + "-" + sdf.format(date);
	}
	
}







