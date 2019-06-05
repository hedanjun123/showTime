-- 创建用户表
drop table if exists `t_user`;
CREATE TABLE t_user (
	id INT AUTO_INCREMENT COMMENT '用户id',
	username VARCHAR(20) UNIQUE NOT NULL COMMENT '用户名',
	password CHAR(32) NOT NULL COMMENT '密码',
	salt CHAR(36) COMMENT '盐值',
	gender INT COMMENT '性别，0-女性，1-男性',
	phone VARCHAR(20)  COMMENT '电话',
	email VARCHAR(50) COMMENT '邮箱',
	avatar VARCHAR(50) COMMENT '头像',
	is_delete INT COMMENT '是否删除，0-未删除，1-已删除',
	created_user VARCHAR(20) COMMENT '创建执行人',
	created_time DATETIME COMMENT '创建时间',
	modified_user VARCHAR(20) COMMENT '修改执行人',
	modified_time DATETIME COMMENT '修改时间',
	PRIMARY KEY (id)
) DEFAULT CHARSET=UTF8;

-- 创建收货地址表
drop table if exists `t_address`;
CREATE TABLE t_address (
	id INT AUTO_INCREMENT COMMENT '收货地址的id',
	uid INT NOT NULL COMMENT '所属用户的id',
	name VARCHAR(20) COMMENT '收货人姓名',
	province CHAR(6) COMMENT '省的代号',
	city CHAR(6) COMMENT '市的代号',
	area CHAR(6) COMMENT '区的代号',
	district VARCHAR(50) COMMENT '省市区的名称',
	zip CHAR(6) COMMENT '邮政编码',
	address VARCHAR(50) COMMENT '详细地址',
	phone VARCHAR(20) COMMENT '手机',
	tel VARCHAR(20) COMMENT '固话',
	tag VARCHAR(10) COMMENT '地址类型，如：家/公司/学校',
	is_default INT COMMENT '是否默认，0-非默认，1-默认',
	created_user VARCHAR(20) COMMENT '创建执行人',
	created_time DATETIME COMMENT '创建时间',
	modified_user VARCHAR(20) COMMENT '修改执行人',
	modified_time DATETIME COMMENT '修改时间',
	PRIMARY KEY(id)
) DEFAULT CHARSET=UTF8;

-- 购物车-加入购物车-数据库与数据表
drop table if exists `t_cart`;
CREATE TABLE t_cart (
	id INT AUTO_INCREMENT COMMENT '数据id',
	uid INT NOT NULL COMMENT '归属用户的id',
	gid BIGINT NOT NULL COMMENT '商品的id',
	num INT NOT NULL COMMENT '商品的数量',
	created_user VARCHAR(20) COMMENT '创建执行人',
	created_time DATETIME COMMENT '创建时间',
	modified_user VARCHAR(20) COMMENT '修改执行人',
	modified_time DATETIME COMMENT '修改时间',
	PRIMARY KEY(id)
) DEFAULT CHARSET=UTF8;

-- 创建订单表：
drop table if exists `t_order`;
CREATE TABLE t_order (
	id INT AUTO_INCREMENT COMMENT 'id',
	uid INT COMMENT '归属哪个用户',
	name VARCHAR(20) COMMENT '收货人',
	phone VARCHAR(20) COMMENT '收货电话',
	address VARCHAR(100) COMMENT '收货地址',
	status INT COMMENT '订单状态：0-未支付，1-已支付，2-已取消…………',
	price BIGINT COMMENT '商品总价',
	order_time DATETIME COMMENT '下单时间',
	pay_time DATETIME COMMENT '支付时间',
	created_user VARCHAR(20) COMMENT '创建执行人',
	created_time DATETIME COMMENT '创建时间',
	modified_user VARCHAR(20) COMMENT '修改执行人',
	modified_time DATETIME COMMENT '修改时间',
	PRIMARY KEY(id)
) DEFAULT CHARSET=UTF8;

-- 创建订单商品表：
drop table if exists `t_order_item`;
CREATE TABLE t_order_item (
	id INT AUTO_INCREMENT COMMENT 'id',
	oid INT COMMENT '归属的订单id',
	gid BIGINT COMMENT '商品id',
	title VARCHAR(100) COMMENT '商品名称',
	image VARCHAR(500) COMMENT '商品图片',
	price BIGINT COMMENT '商品单价',
	num INT COMMENT '购买数量',
	created_user VARCHAR(20) COMMENT '创建执行人',
	created_time DATETIME COMMENT '创建时间',
	modified_user VARCHAR(20) COMMENT '修改执行人',
	modified_time DATETIME COMMENT '修改时间',
	PRIMARY KEY(id)
) DEFAULT CHARSET=UTF8;