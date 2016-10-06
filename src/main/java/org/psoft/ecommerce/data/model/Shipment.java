package org.psoft.ecommerce.data.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * @hibernate.class table="shipment"
 * 
 * @author jpetrocik
 * 
 */
public class Shipment {

	Long id;

	Double amount;

	String trackingNumber;

	Date shipDate;

	Set<ShipmentDetail> shipmentDetails = new HashSet<ShipmentDetail>();
	
	Order order;
	
	String orderNum;

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
	 * @hibernate.property
	 */
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @hibernate.property
	 */
	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	/**
	 * @hibernate.property
	 */
	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}
	
	/**
	 * @hibernate.set table="shipment_dtl" inverse="true" access="field"
	 *                 cascade="all-delete-orphan"
	 * @hibernate.key column="shipment_id"
	 * @hibernate.one-to-many class="org.psoft.ecommerce.data.ShipmentDetail"
	 */
	public Set<ShipmentDetail> getShipmentDetails(){
		return Collections.unmodifiableSet(shipmentDetails);
	}

	public void addShipmentDetail(ShipmentDetail shipmentDetail) {
	
		if (shipmentDetail == null)
			return;
	
		//check for duplicate association
		//shipmentDetails.contains(shipmentDetail);
		
		//set reverse association
		shipmentDetail.setShipment(this);
		//create association

		this.shipmentDetails.add(shipmentDetail);
	}
	
	/**
	 * @hibernate.many-to-one column="order_id"
	 *                        class="org.psoft.ecommerce.data.Order"
	 *                        access="field"
	 * @return
	 */
	public Order getOrder() {
		return order;
	}

	protected void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @hibernate.property
	 */
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
}
