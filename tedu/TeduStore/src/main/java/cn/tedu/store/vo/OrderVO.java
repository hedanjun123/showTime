package cn.tedu.store.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.tedu.store.entity.OrderItem;

public class OrderVO implements Serializable {

	private static final long serialVersionUID = 310521494455105831L;

	private Integer id;
	private Integer uid;
	private String recvName;
	private String recvPhone;
	private String recvDistrict;
	private String recvAddress;
	private String recvZip;
	private Long pay;
	private Integer status;
	private Date orderTime;
	private Date payTime;
	private List<OrderItem> orderItems;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}

	public String getRecvPhone() {
		return recvPhone;
	}

	public void setRecvPhone(String recvPhone) {
		this.recvPhone = recvPhone;
	}

	public String getRecvDistrict() {
		return recvDistrict;
	}

	public void setRecvDistrict(String recvDistrict) {
		this.recvDistrict = recvDistrict;
	}

	public String getRecvAddress() {
		return recvAddress;
	}

	public void setRecvAddress(String recvAddress) {
		this.recvAddress = recvAddress;
	}

	public String getRecvZip() {
		return recvZip;
	}

	public void setRecvZip(String recvZip) {
		this.recvZip = recvZip;
	}

	public Long getPay() {
		return pay;
	}

	public void setPay(Long pay) {
		this.pay = pay;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public List<OrderItem> getOrderItem() {
		return orderItems;
	}

	public void setOrderItem(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public String toString() {
		return "OrderVO [id=" + id + ", uid=" + uid + ", recvName=" + recvName + ", recvPhone=" + recvPhone
				+ ", recvDistrict=" + recvDistrict + ", recvAddress=" + recvAddress + ", recvZip=" + recvZip + ", pay="
				+ pay + ", status=" + status + ", orderTime=" + orderTime + ", payTime=" + payTime + ", orderItems="
				+ orderItems + "]";
	}

}
