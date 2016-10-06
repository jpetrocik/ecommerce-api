package org.psoft.ecommerce.data.model;

import java.util.Date;

import org.psoft.ecommerce.service.CartService;
import org.psoft.ecommerce.service.UserException;

public interface Coupon {

	/**
	 * Called when the coupon is applied to the order
	 * 
	 * @param cart
	 * @throws UserException
	 */
	public void apply(CartService cart) throws UserException;

	public CouponValues update();

	public String getTemplate();

	public String getCouponType();

	public void validate();

	public boolean isValid();

	public String getCode();

	public void setCode(String code);

	public String getDescr();

	public void setDescr(String descr);

	public Date getStartDate();

	public void setStartDate(Date startDate);

	public Date getEndDate();

	public void setEndDate(Date endDate);

	public boolean isActive();

	public void setActive(boolean isActive);

	public boolean isExculsive();

	public void setExculsive(boolean isExculsive);

	public AccountType getAccountType();

	public void setAccountType(AccountType accountType);

	public String getValue1();

	public void setValue1(String value);

	public String getValue2();

	public void setValue2(String value);

	public String getValue3();

	public void setValue3(String value);

	public String getValue4();

	public void setValue4(String value);

	public String getValue5();

	public void setValue5(String value);

	public String getValue6();

	public void setValue6(String value);

	public String getValue7();

	public void setValue7(String value);

	public String getValue8();

	public void setValue8(String value);

	public String getValue9();

	public void setValue9(String value);

	public String getValue10();

	public void setValue10(String value);

}
