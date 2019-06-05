package cn.tedu.store.vo;

import java.io.Serializable;

/**
 * Cart Value Object
 */
public class CartVO implements Serializable {

	private static final long serialVersionUID = -4226267978903049502L;
	
	private Integer cartId;
	private Integer uid;
	private String goodsId;
	private String goodsTitle;
	private String goodsImage;
	private Long goodsPrice;
	private Integer goodsNum;

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public Long getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Long goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	@Override
	public String toString() {
		return "CartVO [cartId=" + cartId + ", uid=" + uid + ", goodsId=" + goodsId + ", goodsTitle=" + goodsTitle
				+ ", goodsImage=" + goodsImage + ", goodsPrice=" + goodsPrice + ", goodsNum=" + goodsNum + "]";
	}

}
