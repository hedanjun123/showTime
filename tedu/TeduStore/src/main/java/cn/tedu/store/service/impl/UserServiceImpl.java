package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.InsertDataException;
import cn.tedu.store.service.ex.PasswordFormatException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateDataException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameConflictException;
import cn.tedu.store.service.ex.UsernameFormatException;
import cn.tedu.store.util.TextValidator;

/**
 * 用户数据的业务层
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;

	public User reg(User user) 
			throws UsernameConflictException, 
				InsertDataException {
		/*
		 *  验证用户名与密码的格式是否正确
		 */
		if (!TextValidator.checkUsername(user.getUsername())) {
			throw new UsernameFormatException("尝试登录的用户名(" + user.getUsername() + ")格式不正确！");
		}
		if (!TextValidator.checkPassword(user.getPassword())) {
			throw new PasswordFormatException("尝试登录的密码格式不正确！");
		}
				
		/*
		 * 定位：组织业务，保证数据的安全性
		 */
		/*
		 *  根据尝试注册的用户名查询用户数据
		 */
		User data = 
			findUserByUsername(user.getUsername());
		/*
		 * 判断是否查询到数据
		 */
		if (data != null) {
			/*
			 *  是：查询到数据，即用户名被占用，则抛出UsernameConflictException异常
			 */
			throw new UsernameConflictException(
				"尝试注册的用户名(" 
					+ user.getUsername() 
					+ "have been occupied！");
		} else {
			/*
			 *  否：没有查询到数据，即用户名没有被占用，则执行插入用户数据，获取返回值
			 */
			User result = insert(user);
			/*
			 *  执行返回
			 */
			return result;
		}
	}
	
	public User login(
		String username, String password) 
			throws UserNotFoundException, 
				PasswordNotMatchException {
		/*
		 *  验证用户名与密码的格式是否正确
		 */
		if (!TextValidator.checkUsername(username)) {
			throw new UsernameFormatException("尝试登录的用户名(" + username + ")格式不正确！");
		}
		if (!TextValidator.checkPassword(password)) {
			throw new PasswordFormatException("尝试登录的密码格式不正确！");
		}
		
		/*
		 *  根据用户名查询用户数据
		 */
		User user = findUserByUsername(username);
	    /*
	     *  判断是否查询到数据
	     */
		if (user != null) {
	        /*
	         *  是：查询到与用户名匹配的数据，获取盐值
	         */
			String salt = user.getSalt();
			/*
			 * 基于参数密码与盐值进行加密
			 */
			String md5Password 
				= getEncrpytedPassword(password, salt);
			/*
			 * 判断加密结果与用户数据中的密码是否匹配
			 */
			if (user.getPassword().equals(md5Password)) {
				/*
				 *  是：返回用户数据,隐藏用户数据
				 */
				user.setPassword(null);
				user.setSalt(null);
				return user;
			} else {
				/*
				 *  否：密码不正确，抛出PasswordNotMacthException异常
				 */
				throw new PasswordNotMatchException(
					"密码错误！");
			}
		} else {
	        /*
	         * 否：没有与用户名匹配的数据，则抛出UserNotFoundException异常
	         */
			throw new UserNotFoundException(
				"尝试登录的用户名(" + username + ")不存在！");
		}
	}

	public void changePassword(
			Integer id, 
			String oldPassword, 
			String newPassword)
			throws UserNotFoundException, 
				PasswordNotMatchException, 
				PasswordFormatException,
				UpdateDataException {
		/*
		 *  验证数据格式
		 */
		if (!TextValidator.checkPassword(oldPassword)) {
			throw new PasswordFormatException("原密码格式错误！");
		}
		if (!TextValidator.checkPassword(newPassword)) {
			throw new PasswordFormatException("新密码格式错误！");
		}
		
		/*
		 * 根据id查询用户数据
		 */
		User user = findUserById(id);
		/*
		 *  判断用户数据是否存在（可能用户登录后数据被删除）
		 */
		if (user != null) {
			/*
			 *  是：用户数据存在，获取盐值
			 */
			String salt = user.getSalt();
			/*
			 * 将oldPassword加密
			 */
			String oldMd5Password 
				= getEncrpytedPassword(oldPassword, salt);
			/*
			 *  将加密后的密码，与刚才查询结果中的密码对比
			 */
			if (user.getPassword().equals(oldMd5Password)) {
				/*
				 *  是：基于盐和newPassword加密
				 */
				String newMd5Password
					= getEncrpytedPassword(newPassword, salt);
				/*
				 *  更新密码
				 */
				updatePassword(id, newMd5Password);
			} else {
				/*
				 * 否：原密码错误，抛出PasswordNotMatchException
				 */
				throw new PasswordNotMatchException("原密码错误！");
			}
		} else {
			/*
			 * 否：用户数据不存在，抛出UserNotFoundException
			 */
			throw new UserNotFoundException(
				"尝试访问的用户数据不存在！可能已经被删除！");
		}
	}

	public void changeInfo(User user) throws UpdateDataException, UserNotFoundException {
		/*
		 *  检查是否有id
		 */
		if (user.getId() == null) {
			throw new UpdateDataException(
				"更新用户数据失败，缺少必要参数：用户id。");
		}
		
		/*
		 * 检查手机号码、电子邮件的格式
		 */
		
		/*
		 *  检查用户数据是否存在
		 */
		User data = findUserById(user.getId());
		if (data == null) {
			throw new UserNotFoundException(
				"尝试访问的用户数据不存在！");
		}
		
		/*
		 * 补全数据
		 */
		user.setModifiedTime(new Date());
		user.setModifiedUser(data.getUsername());
		
		/*
		 *  执行更新
		 */
		updateInfo(user);
	}

	public void changeAvatar(
		Integer id, String avatar) 
			throws UserNotFoundException, 
				UpdateDataException {
		if (findUserById(id) == null) {
			throw new UserNotFoundException(
				"尝试访问的用户数据不存在！");
		}
		updateAvatar(id, avatar);
	}

	public User findUserById(Integer id) {
		return userMapper.findUserById(id);
	}
	
	/**
	 * 根据用户名查询用户数据
	 * @param username 用户名
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	private User findUserByUsername(String username) {
		return userMapper.findUserByUsername(username);
	}
	
	/**
	 * 插入用户数据
	 * @param user 用户数据
	 * @return 成功插入的用户数据
	 */
	private User insert(User user) {
		// 在参数user中封装那些不由外部提供的数据：
		// 1. 生成随机盐，并封装到user中
		String salt = UUID.randomUUID().toString().toUpperCase();
		user.setSalt(salt);
		// 2. 取出user中原密码执行加密，并封装回user中
		String oldPassword = user.getPassword();
		String md5Password = getEncrpytedPassword(oldPassword, salt);
		user.setPassword(md5Password);
		// 3. 设置isDelete为0
		user.setIsDelete(0);
		// 4. 日志的4项
		Date now = new Date();
		user.setCreatedTime(now);
		user.setModifiedTime(now);
		user.setCreatedUser("[System]");
		user.setModifiedUser("[System]");
		
		// 插入用户数据
		Integer rows = userMapper.insert(user);
		if (rows == 1) {
			return user;
		} else {
			throw new InsertDataException(
				"增加用户数据时发生未知错误！请联系系统管理员！");
		}
	}
	
	/**
	 * 更新密码
	 * @param id 用户id
	 * @param password 新密码
	 * @return 受影响的行数
	 */
	private Integer updatePassword(
			Integer id, String password) {
		Integer rows 
			= userMapper.updatePassword(id, password);
		if (rows == 1) {
			return 1; // return rows;
		} else {
			throw new UpdateDataException(
				"更新密码时出现未知错误，请联系系统管理员！");
		}
	}
	
	/**
	 * 更新用户数据
	 * @param user 用户数据
	 */
	private void updateInfo(User user) {
		Integer rows = userMapper.updateInfo(user);
		if (rows != 1) {
			throw new UpdateDataException(
				"更新用户数据时出现未知错误，请联系系统管理员！");
		}
	}
	
	/**
	 * 更新用户头像
	 * @param id 用户id
	 * @param avatar 用户头像的路径
	 */
	private void updateAvatar(Integer id, String avatar) {
		Integer rows = userMapper.updateAvatar(id, avatar);
		if (rows != 1) {
			throw new UpdateDataException(
				"更新用户数据时出现未知错误，请联系系统管理员！");
		}
	}
	
	/**
	 * 获取加密后的密码
	 * @param password 密码原文
	 * @param salt 盐值
	 * @return 加密后的密码
	 */
	private String getEncrpytedPassword(String password, String salt) {
		// 将原密码加密
		String str1 = DigestUtils.md5Hex(password).toUpperCase();
		// 将盐加密
		String str2 = DigestUtils.md5Hex(salt).toUpperCase();
		// 将以上2个加密结果拼接
		String result = str1 + str2;
		// 循环5次加密
		for (int i = 0; i < 5; i++) {
			result = DigestUtils.md5Hex(result).toUpperCase();
		}
		// 返回
		return result;
	}


}




















