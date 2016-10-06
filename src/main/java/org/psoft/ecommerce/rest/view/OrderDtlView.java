package org.psoft.ecommerce.rest.view;

import org.psoft.ecommerce.data.model.OrderDtl;

public class OrderDtlView {

	OrderDtl orderDtl;

	public OrderDtlView(OrderDtl orderDtl) {
		this.orderDtl = orderDtl;
	}

	public Long getItemId() {
		return orderDtl.getItem().getId();
	}

	public Double getExtPrice() {
		return orderDtl.getExtPrice();
	}

	public String getPartNum() {
		return orderDtl.getItem().getPartNum();
	}

	public String getTitle() {
		return orderDtl.getItem().getTitle();
	}

	public Integer getLineNumber() {
		return orderDtl.getLineNumber();
	}

	public Double getPrice() {
		return orderDtl.getPrice();
	}

	public Integer getQty() {
		return orderDtl.getQty();
	}

}
