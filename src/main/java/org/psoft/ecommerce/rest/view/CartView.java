package org.psoft.ecommerce.rest.view;

import java.util.List;

public class CartView {

	Double total;

	Double tax;

	Double shipping;

	Double subtotal;

	Double discount;

	List<OrderDtlView> items;

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getShipping() {
		return shipping;
	}

	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public List<OrderDtlView> getItems() {
		return items;
	}

	public void setItems(List<OrderDtlView> items) {
		this.items = items;
	}

}
