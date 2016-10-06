package org.psoft.ecommerce.coupons;

import java.math.BigDecimal;

import org.apache.commons.lang.Validate;
import org.psoft.ecommerce.data.model.CouponValues;
import org.psoft.ecommerce.service.CartService;
import org.psoft.ecommerce.service.UserException;

public class DiscountPcntCoupon extends AbstractCoupon {

	public DiscountPcntCoupon() {
	}

	public DiscountPcntCoupon(CouponValues values) {
		super(values);
	}

	public String getCouponType() {
		return "Discount Precentage";
	}

	public void validate() {
		super.validate();

		Validate.notNull(getMinTotal(), "minTotal (Value1) null");
		Validate.notNull(getPrecentage(), "precentage (Value2) null");
	}

	protected void applyDiscount(CartService cart) throws UserException {
		Double subTotal = cart.getSubtotal();
		if (subTotal < getMinTotal()) {
			throw new UserException("coupon.invalid.order.min", new Object[] { getMinTotal() });
		}

		BigDecimal discount = new BigDecimal(subTotal * (getPrecentage() / 100));

		discount = discount.setScale(2, BigDecimal.ROUND_HALF_UP);

		cart.addDiscount(discount.doubleValue());
	}

	public Double getMinTotal() {
		return new Double(getValue1());
	}

	public Double getPrecentage() {
		return new Double(getValue2());
	}

}
