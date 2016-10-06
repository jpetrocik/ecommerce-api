package org.psoft.ecommerce.data.model;

/**
 * @hibernate.class table="order_dtl"
 * 
 * @author jpetrocik
 * 
 */
public class OrderDtl {

	private Long id;

	private Item item;

	private Integer lineNumber;

	private Integer qty = new Integer(0);

	private Double price = new Double(0);

	private Order order;

	/**
	 * @hibernate.id column="id" generator-class="native"
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	/**
	 * @hibernate.many-to-one column="item_id"
	 *                        class="org.psoft.ecommerce.data.Item"
	 *                        not-null="true"
	 * 
	 * @return
	 */
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @hibernate.many-to-one column="order_id"
	 *                        class="org.psoft.ecommerce.data.Order"
	 *                        not-null="true"
	 * 
	 * @return
	 */
	public Order getOrder() {
		return order;
	}

	protected void setOrder(Order order) {
		this.order = order;
	}

	public Double getExtPrice() {
		return price * qty;
	}

}
