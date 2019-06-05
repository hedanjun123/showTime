package cn.tedu.store.service;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.ex.InsertDataException;
import cn.tedu.store.service.ex.PasswordFormatException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateDataException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameConflictException;
import cn.tedu.store.service.ex.UsernameFormatException;

public interface IUserService {

	/**
	 * 用户注册
	 * @param user 用户数据
	 * @return 成功注册的用户数据，且包含用户的id
	 * @throws UsernameConflictException 用户名被占用
	 * @throws InsertDataException 插入数据错误
	 * @throws UsernameFormatException 用户名格式错误
	 * @throws PasswordFormatException 密码格式错误
	 */
	User reg(User user) 
			throws UsernameConflictException, 
				InsertDataException,
				UsernameFormatException,
				PasswordFormatException;
	
	/**
	 * 用户登录
	 * @param username 用户名
	 * @param password 密码
	 * @return 成功登录的用户信息
	 * @throws UserNotFoundException 用户名不存在
	 * @throws PasswordNotMatchException 密码错误 
	 * @throws UsernameFormatException 用户名格式错误
	 * @throws PasswordFormatException 密码格式错误
	 */
	User login(String username, String password) 
			throws UserNotFoundException, 
				PasswordNotMatchException,
				UsernameFormatException,
				PasswordFormatException;
	
	/**
	 * 修改用户密码
	 * @param id 用户id
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @throws UserNotFoundException 用户数据不存在
	 * @throws PasswordNotMatchException 原密码不匹配
	 * @throws PasswordFormatException 密码格式错误
	 * @throws UpdateDataException 更新数据错误
	 */
	void changePassword(
			Integer id, String oldPassword, 
			String newPassword) 
			throws UserNotFoundException,
				PasswordNotMatchException,
				PasswordFormatException,
				UpdateDataException; 
	
	/**
	 * 修改用户资料
	 * @param user 用户数据
	 * @throws UpdateDataException 更新错误
	 * @throws UserNotFoundException 用户数据不存在
	 */
	void changeInfo(User user)
			throws UpdateDataException,
				UserNotFoundException;
	
	/**
	 * 根据用户id查询用户数据
	 * @param id 用户id
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	User findUserById(Integer id);
	
	/**
	 * 修改用户头像
	 * @param id 用户id
	 * @param avatar 用户头像的路径
	 * @throws UserNotFoundException 用户数据不存在
	 * @throws UpdateDataException 更新数据错误
	 */
	void changeAvatar(Integer id, String avatar) 
			throws UserNotFoundException, 
				UpdateDataException;
	
}




