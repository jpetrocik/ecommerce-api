package org.psoft.ecommerce.data.model;

import java.lang.reflect.Constructor;
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @hibernate.class table="coupon_values"
 * 
 * @author jpetrocik
 * 
 */
public class CouponValues implements Comparable<CouponValues>{

	private Long id;

	private String code;

	private String descr;

	private String couponClass;

	private Date startDate;

	private Date endDate;

	private Boolean active = Boolean.TRUE;

	private Boolean exculsive = Boolean.TRUE;

	private AccountType accountType;

	private String value1;

	private String value2;

	private String value3;

	private String value4;

	private String value5;

	private String value6;

	private String value7;

	private String value8;

	private String value9;

	private String value10;

	public static Coupon createCoupon(CouponValues couponValues) throws Exception {
		String type = couponValues.getCouponClass();
		Class clazz = Class.forName(type);
		Constructor constructor = clazz.getConstructor(new Class[] { CouponValues.class });
		Coupon coupon = (Coupon) constructor.newInstance(new Object[] { couponValues });

		return coupon;
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
		CouponValues rhs = (CouponValues) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(id, rhs.getId()).append(couponClass,
				rhs.getCouponClass()).append(code, rhs.getCode()).append(descr, rhs.getDescr()).append(startDate,
				rhs.getStartDate()).append(endDate, rhs.getEndDate()).append(BooleanUtils.isTrue(active), rhs.isActive()).append(
				BooleanUtils.isTrue(exculsive), rhs.isExculsive()).append(value1, rhs.getValue1())
				.append(value2, rhs.getValue2()).append(value3, rhs.getValue3()).append(value4, rhs.getValue4()).append(value5,
						rhs.getValue5()).append(value6, rhs.getValue6()).append(value7, rhs.getValue7()).append(value8,
						rhs.getValue8()).append(value9, rhs.getValue9()).append(value10, rhs.getValue10()).isEquals();
	}

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
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @hibernate.property
	 */
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * @hibernate.property
	 */
	public String getCouponClass() {
		return couponClass;
	}

	public void setCouponClass(String couponType) {
		this.couponClass = couponType;
	}

	/**
	 * @hibernate.property type="timestamp"
	 */
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date end) {
		this.endDate = end;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isActive() {
		return BooleanUtils.isTrue(active);
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isExculsive() {
		return BooleanUtils.isTrue(exculsive);
	}

	public void setExculsive(Boolean exculsive) {
		this.exculsive = exculsive;
	}

	/**
	 * @hibernate.many-to-one column="account_type_id"
	 *                        class="org.psoft.ecommerce.data.AccountType"
	 *                        not-null="true"
	 * 
	 * @return
	 */
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	/**
	 * @hibernate.property type="timestamp"
	 */
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date start) {
		this.startDate = start;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue6() {
		return value6;
	}

	public void setValue6(String value6) {
		this.value6 = value6;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue7() {
		return value7;
	}

	public void setValue7(String value7) {
		this.value7 = value7;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue8() {
		return value8;
	}

	public void setValue8(String value8) {
		this.value8 = value8;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue9() {
		return value9;
	}

	public void setValue9(String value9) {
		this.value9 = value9;
	}

	/**
	 * @hibernate.property
	 */
	public String getValue10() {
		return value10;
	}

	public void setValue10(String value10) {
		this.value10 = value10;
	}

	public int compareTo(CouponValues cv) {
		String code = cv.getCode();
		
		if (StringUtils.isBlank(code))
			return 1;
		
		if (StringUtils.isBlank(this.code))
			return -1;
		
		return this.code.compareTo(code);
	}

}
