package cn.tedu.store.entity;

import java.util.Date;

public class Order extends BaseEntity {

	private static final long serialVersionUID = -5603210776330169858L;

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

	@Override
	public String toString() {
		return "Order [id=" + id + ", uid=" + uid + ", recvName=" + recvName + ", recvPhone=" + recvPhone
				+ ", recvDistrict=" + recvDistrict + ", recvAddress=" + recvAddress + ", recvZip=" + recvZip + ", pay="
				+ pay + ", status=" + status + ", orderTime=" + orderTime + ", payTime=" + payTime + "]";
	}

}
