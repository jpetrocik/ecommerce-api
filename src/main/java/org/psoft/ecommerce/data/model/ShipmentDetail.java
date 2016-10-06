package org.psoft.ecommerce.data.model;

/**
 * @hibernate.class table="shipment_detail"
 * 
 * @author jpetrocik
 * 
 */
public class ShipmentDetail {

	Long id;

	OrderDtl orderDetail;

	Integer quantity;

	Shipment shipment;
	
	String sku;
	
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
	 * @hibernate.many-to-one column="order_detail_id"
	 * 						 class="org.psoft.ecommerce.data.OrderDtl"
	 * 						 cascade="save-update"
	 * 
	 * @return
	 */
	public OrderDtl getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDtl orderDetail) {
		this.orderDetail = orderDetail;
	}

	/**
	 * @hibernate.property
	 */
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * @hibernate.many-to-one column="shipment_id"
	 *                        class="org.psoft.ecommerce.data.Shipment"
	 *                        not-null="true"
	 * 
	 * @return
	 */
	public Shipment getShipment(){
		return shipment;
	}
	
	protected void setShipment(Shipment shipment){
		this.shipment=shipment;
	}

	/**
	 * @hibernate.property
	 */
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
