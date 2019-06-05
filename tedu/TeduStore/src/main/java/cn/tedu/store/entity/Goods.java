package cn.tedu.store.entity;

/**
 * 商品数据类
 */
public class Goods extends BaseEntity {

	private static final long serialVersionUID = -8594398692052384197L;

	private String id;
	private String categoryId;
	private String itemType;
	private String title;
	private String sellPoint;
	private Long price;
	private Integer num;
	private String barCode;
	private String image;
	private Integer status;
	private Integer priority;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSellPoint() {
		return sellPoint;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", categoryId=" + categoryId + ", itemType=" + itemType + ", title=" + title
				+ ", sellPoint=" + sellPoint + ", price=" + price + ", num=" + num + ", barCode=" + barCode + ", image="
				+ image + ", status=" + status + ", priority=" + priority + "]";
	}

}
