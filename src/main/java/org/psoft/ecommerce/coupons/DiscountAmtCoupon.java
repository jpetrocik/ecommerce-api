package org.psoft.ecommerce.coupons;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.psoft.ecommerce.data.model.CouponValues;
import org.psoft.ecommerce.service.CartService;
import org.psoft.ecommerce.service.UserException;

public class DiscountAmtCoupon extends AbstractCoupon {

	public DiscountAmtCoupon() {
	}

	public DiscountAmtCoupon(CouponValues values) {
		super(values);
	}

	public String getCouponType() {
		return "Discount Amount";
	}

	public void validate() {
		super.validate();

		Validate.notNull(getValue1());
		Validate.notNull(getValue2());

		if (StringUtils.isNotBlank(getValue3())) {
			Validate.notNull(getValue4());
		}

		if (StringUtils.isNotBlank(getValue5())) {
			Validate.notNull(getValue6());
		}

		if (StringUtils.isNotBlank(getValue7())) {
			Validate.notNull(getValue8());
		}
	}

	protected void applyDiscount(CartService cart) throws UserException {
		Double subTotal = cart.getSubtotal();
		if (subTotal < getMinTotal1()) {
			throw new UserException("coupon.invalid.order.min", new Object[] { getMinTotal1() });
		}

		// apply discount
		if (getMinTotal4() != null && subTotal >= getMinTotal4())
			cart.addDiscount(getDiscount4());
		else if (getMinTotal3() != null && subTotal >= getMinTotal3())
			cart.addDiscount(getDiscount3());
		else if (getMinTotal2() != null && subTotal >= getMinTotal2())
			cart.addDiscount(getDiscount2());
		else if (getMinTotal1() != null && subTotal >= getMinTotal1())
			cart.addDiscount(getDiscount1());
	}

	public Double getMinTotal1() {
		return new Double(getValue1());
	}

	public Double getDiscount1() {
		return new Double(getValue2());
	}

	public Double getMinTotal2() {
		if (StringUtils.isBlank(getValue3()))
			return null;
		return new Double(getValue3());
	}

	public Double getDiscount2() {
		return new Double(getValue4());
	}

	public Double getMinTotal3() {
		if (StringUtils.isBlank(getValue5()))
			return null;
		return new Double(getValue5());
	}

	public Double getDiscount3() {
		return new Double(getValue6());
	}

	public Double getMinTotal4() {
		if (StringUtils.isBlank(getValue7()))
			return null;
		return new Double(getValue7());
	}

	public Double getDiscount4() {
		return new Double(getValue8());
	}

}
