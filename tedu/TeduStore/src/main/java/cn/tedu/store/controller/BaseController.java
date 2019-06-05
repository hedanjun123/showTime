package cn.tedu.store.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.store.entity.ResponseResult;
import cn.tedu.store.service.ex.AddressAccessException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.CartNotFoundException;
import cn.tedu.store.service.ex.DeleteDataException;
import cn.tedu.store.service.ex.GoodsNumLimitException;
import cn.tedu.store.service.ex.InsertDataException;
import cn.tedu.store.service.ex.PasswordFormatException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.RequestArgumentException;
import cn.tedu.store.service.ex.ServiceException;
import cn.tedu.store.service.ex.UpdateDataException;
import cn.tedu.store.service.ex.UploadFileContentTypeException;
import cn.tedu.store.service.ex.UploadFileSizeLimitException;
import cn.tedu.store.service.ex.UploadIOException;
import cn.tedu.store.service.ex.UploadStateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameConflictException;
import cn.tedu.store.service.ex.UsernameFormatException;

/**
 * 控制器类的基类
 */
public abstract class BaseController {

	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public ResponseResult<Void>
		handleException(Exception e) {
		// 判断异常类型，并处理
		if(e instanceof RequestArgumentException) {
			// 300-请求参数异常
			return new ResponseResult<Void>(300, e);
		} else if (e instanceof UsernameFormatException) {
			// 301-用户名格式错误
			return new ResponseResult<Void>(301, e);
		} else if (e instanceof PasswordFormatException) {
			// 302-密码格式错误
			return new ResponseResult<Void>(302, e);
		} else if (e instanceof UploadFileSizeLimitException) {
			// 303-上传文件大小超出限制
			return new ResponseResult<Void>(303, e);
		} else if (e instanceof UploadFileContentTypeException) {
			// 304-上传文件类型异常
			return new ResponseResult<Void>(304, e);
		} else if (e instanceof UploadStateException) {
			// 305-上传状态异常
			return new ResponseResult<Void>(305, e);
		} else if (e instanceof UploadIOException) {
			// 306-上传文件读写异常
			return new ResponseResult<Void>(306, e);
		} else if (e instanceof CartNotFoundException) {
			// 307-购物车数据不存在
			return new ResponseResult<Void>(307, e);
		} else if (e instanceof GoodsNumLimitException) {
			// 308-购物车中商品数据超出限制
			return new ResponseResult<Void>(308, e);
		} else if (e instanceof UsernameConflictException) {
			// 401-用户名冲突异常
			return new ResponseResult<Void>(401, e);
		} else if (e instanceof UserNotFoundException) {
			// 402-用户不存在
			return new ResponseResult<Void>(402, e);
		} else if (e instanceof PasswordNotMatchException) {
			// 403-密码错误
			return new ResponseResult<Void>(403, e);
		} else if (e instanceof AddressNotFoundException) {
			// 404-收货地址数据不存在
			return new ResponseResult<Void>(404, e);
		} else if (e instanceof AddressAccessException) {
			// 405-收货地址数据访问异常，可能是非法访问
			return new ResponseResult<Void>(405, e);
		} else if (e instanceof InsertDataException) {
			// 501-插入数据异常
			return new ResponseResult<Void>(501, e);
		} else if (e instanceof UpdateDataException) {
			// 502-更新数据异常
			return new ResponseResult<Void>(502, e);
		} else if (e instanceof DeleteDataException) {
			// 503-删除数据异常
			return new ResponseResult<Void>(503, e);
		} 
		return null;
	}
	
	/**
	 * 从Session中获取当前登录的用户的id
	 * @param session HttpSession对象
	 * @return 当前登录的用户的id
	 */
	protected Integer getUidFromSession(HttpSession session) {
		return Integer.valueOf(session.getAttribute("uid").toString());
	}
	
}











