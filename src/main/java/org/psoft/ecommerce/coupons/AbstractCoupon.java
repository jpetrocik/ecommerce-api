package org.psoft.ecommerce.coupons;

import java.util.Date;

import org.apache.commons.lang.Validate;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Coupon;
import org.psoft.ecommerce.data.model.CouponValues;
import org.psoft.ecommerce.service.CartService;
import org.psoft.ecommerce.service.UserException;

abstract public class AbstractCoupon implements Coupon {

	CouponValues values;

	public AbstractCoupon() {
		values = new CouponValues();
		values.setCouponClass(getClass().getName());
	}

	public AbstractCoupon(CouponValues values) {
		this.values = values;
	}

	public void validate() {
		Validate.notNull(getCode());
		Validate.notNull(getEndDate());
		Validate.notNull(getStartDate());
	}

	public CouponValues update() {
		return values;
	}

	/**
	 * Generates the admin html for modified the coupon values
	 * 
	 */
	public String getTemplate() {
		String className = this.getClass().getSimpleName();
		String packageName = this.getClass().getPackage().getName().replace('.', '/');

		return packageName + "/html/" + className + ".html";
	}

	/**
	 * Called when the coupon is applied to the order, part of the coupon
	 * interface. The class then delegate to applyDiscount for subclass.
	 */
	public final void apply(CartService cart) throws UserException {

		Account account = cart.getAccount();
		if (account != null) {
			AccountType accountType = account.getAccountType();

			if (!accountType.equals(getAccountType())) {
				throw new UserException("coupon.invalid.account.type", new String[] { accountType.getName() });

			}

		}

		applyDiscount(cart);
	}

	/**
	 * Call when the coupon is applied to the order, called after apply is
	 * called
	 * 
	 * @param cart
	 * @throws UserException
	 */
	abstract protected void applyDiscount(CartService cart) throws UserException;

	public Long getId() {
		return values.getId();
	}

	public void setId(Long id) {
		values.setId(id);
	}

	public boolean isValid() {
		Date today = new Date();

		if (!isActive())
			return false;

		if (today.before(getStartDate()) || today.after(getEndDate()))
			return false;

		return true;
	}

	public String getCode() {
		return values.getCode();
	}

	public void setCode(String code) {
		values.setCode(code);
	}

	public String getDescr() {
		return values.getDescr();
	}

	public void setDescr(String descr) {
		values.setDescr(descr);
	}

	public Date getEndDate() {
		return values.getEndDate();
	}

	public void setEndDate(Date endDate) {
		values.setEndDate(endDate);
	}

	public Date getStartDate() {
		return values.getStartDate();
	}

	public void setStartDate(Date startDate) {
		values.setStartDate(startDate);
	}

	public boolean isActive() {
		return values.isActive();
	}

	public void setActive(boolean isActive) {
		values.setActive(isActive);
	}

	public boolean isExculsive() {
		return values.isExculsive();
	}

	public void setExculsive(boolean isExculsive) {
		values.setExculsive(isExculsive);
	}

	public AccountType getAccountType() {
		return values.getAccountType();
	}

	public void setAccountType(AccountType accountType) {
		values.setAccountType(accountType);
	}

	public String getValue1() {
		return values.getValue1();
	}

	public void setValue1(String value) {
		values.setValue1(value);
	}

	public String getValue2() {
		return values.getValue2();
	}

	public void setValue2(String value) {
		values.setValue2(value);
	}

	public String getValue3() {
		return values.getValue3();
	}

	public void setValue3(String value) {
		values.setValue3(value);
	}

	public String getValue4() {
		return values.getValue4();
	}

	public void setValue4(String value) {
		values.setValue4(value);
	}

	public String getValue5() {
		return values.getValue5();
	}

	public void setValue5(String value) {
		values.setValue5(value);
	}

	public String getValue6() {
		return values.getValue6();
	}

	public void setValue6(String value) {
		values.setValue6(value);
	}

	public String getValue7() {
		return values.getValue7();
	}

	public void setValue7(String value) {
		values.setValue7(value);
	}

	public String getValue8() {
		return values.getValue8();
	}

	public void setValue8(String value) {
		values.setValue8(value);
	}

	public String getValue9() {
		return values.getValue9();
	}

	public void setValue9(String value) {
		values.setValue9(value);
	}

	public String getValue10() {
		return values.getValue10();
	}

	public void setValue10(String value) {
		values.setValue10(value);
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}

		AbstractCoupon rhs = (AbstractCoupon) obj;
		return values.equals(rhs.values);
	}
}
