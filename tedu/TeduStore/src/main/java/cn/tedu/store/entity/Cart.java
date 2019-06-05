package cn.tedu.store.entity;

public class Cart extends BaseEntity {

	private static final long serialVersionUID = 1390321288378166795L;

	private Integer id;
	private Integer uid;
	private String goodsId;
	private Integer goodsNum;

	public Cart() {
		super();
	}

	public Cart(Integer uid, String goodsId, Integer goodsNum) {
		super();
		this.uid = uid;
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
	}

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

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", uid=" + uid + ", goodsId=" + goodsId + ", goodsNum=" + goodsNum + "]";
	}

}
