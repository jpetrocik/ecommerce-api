package org.psoft.ecommerce.coupons;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.psoft.ecommerce.data.model.CouponValues;
import org.psoft.ecommerce.service.CartService;
import org.psoft.ecommerce.service.UserException;

public class FreeShippingCoupon extends AbstractCoupon {

	public FreeShippingCoupon() {
	}

	public FreeShippingCoupon(CouponValues values) {
		super(values);
	}

	public String getCouponType() {
		return "Free Shipping";
	}

	public void validate() {
		super.validate();

		Validate.notNull(getValue1(), "MinTotal (Value1) null");
		Validate.notNull(getValue2(), "ShippingLevel (Value2) null");
	}

	protected void applyDiscount(CartService cart) throws UserException {
		if (cart.getSubtotal() < getMinTotal()) {
			throw new UserException("coupon.invalid.free.shipping.min", new Object[] { getMinTotal() });
		}

		if (!getShippingLevel().equals(cart.getShippingLevel().getId())) {
			throw new UserException("coupon.invalid.free.shipping.method", new Object[] { getShippingLevel() });
		}

		cart.setShipping(NumberUtils.DOUBLE_ZERO);

	}

	public Double getMinTotal() {
		return new Double(getValue1());
	}

	public Long getShippingLevel() {
		
		if (StringUtils.isBlank(getValue2()))
			return null;
		
		return new Long(getValue2());
	}

}
