package org.psoft.ecommerce.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Coupon;
import org.psoft.ecommerce.data.model.CouponValues;

public class CouponService extends AbstractService {
	private Log log = LogFactory.getLog(CouponService.class);

	public Coupon retriveById(Long id) {
		Session session = currentSession();

		log.debug("Fetching coupon by id " + id);
		try {
			CouponValues result = (CouponValues) session.get(CouponValues.class, id);

			Validate.notNull(result, "No such coupon id:" + id);

			Coupon coupon = CouponValues.createCoupon(result);

			return coupon;
		} catch (Exception e) {
			log.warn("Error retrieving coupon " + id, e);
		}

		return null;

	}

	public Coupon getCouponByCode(String code) {
		Session session = currentSession();

		log.debug("Fetching coupon " + code);
		try {
			Criteria c = session.createCriteria(CouponValues.class).add(Restrictions.eq("code", code));
			List<CouponValues> results = c.list();

			if (CollectionUtils.isEmpty(results))
				return null;

			CouponValues couponValues = results.get(0);
			Coupon coupon = CouponValues.createCoupon(couponValues);

			return coupon;
		} catch (Exception e) {
			log.warn("Error retrieving coupon " + code, e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Coupon> searchCoupon(String code, boolean includeInactive) {
		Session session = currentSession();

		log.debug("Searching for " + (includeInactive ? "all" : "active") + " coupon " + code);
		try {
			Criteria c = session.createCriteria(CouponValues.class);

			if (StringUtils.isNotBlank(code))
				c.add(Restrictions.ilike("code", code));

			if (!includeInactive)
				c.add(Restrictions.eq("active", Boolean.TRUE));

			List<CouponValues> results = c.list();

			log.debug("Found " + results.size() + " coupon");

			if (CollectionUtils.isEmpty(results))
				return new ArrayList<Coupon>();

			List<Coupon> coupons = (List<Coupon>) CollectionUtils.collect(results, new Transformer() {

				public Object transform(Object object) {
					CouponValues values = (CouponValues) object;

					try {
						return CouponValues.createCoupon(values);
					} catch (Exception e) {
						throw new RuntimeException("Unable to retrieve coupon " + values.getCode(), e);
					}

				}

			});

			if (!includeInactive) {
				CollectionUtils.filter(coupons, new Predicate() {

					public boolean evaluate(Object object) {
						Coupon coupon = (Coupon) object;
						return coupon.isValid();
					}

				});
			}
			return coupons;
		} catch (Exception e) {
			log.warn("Error retrieving coupon " + code, e);
		}

		return null;
	}

	public void saveCoupon(String type, Long id, String code, String descr, Date startDate, Date endDate, Boolean status,
			Boolean exculsive, Long accountTypeId, String value1, String value2, String value3, String value4, String value5,
			String value6, String value7, String value8, String value9, String value10) {

		Session session = currentSession();
		session.setFlushMode(FlushMode.AUTO);

		Coupon coupon;

		// creating new coupon
		if (id == null) {

			Coupon existingCoupon = this.getCouponByCode(code);
			if (existingCoupon != null) {
				throw new RuntimeException("Coupon " + code + " already exists");
			}

			try {
				coupon = (Coupon) Class.forName(type).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Not such coupon " + type, e);
			}
		} else {
			coupon = retriveById(id);
		}

		AccountType accountType = (AccountType) session.get(AccountType.class, accountTypeId);
		Validate.notNull(accountType, "No such account type");

		coupon.setCode(code);
		coupon.setAccountType(accountType);
		coupon.setDescr(descr);
		coupon.setStartDate(startDate);
		coupon.setEndDate(endDate);
		coupon.setActive(BooleanUtils.toBoolean(status));
		coupon.setExculsive(BooleanUtils.toBoolean(exculsive));
		coupon.setValue1(value1);
		coupon.setValue2(value2);
		coupon.setValue3(value3);
		coupon.setValue4(value4);
		coupon.setValue5(value5);
		coupon.setValue6(value6);
		coupon.setValue7(value7);
		coupon.setValue8(value8);
		coupon.setValue9(value9);
		coupon.setValue10(value10);
		coupon.validate();

		log.debug("Saving coupon " + coupon.getCode());

		CouponValues couponValues = coupon.update();

		session.saveOrUpdate(couponValues);
		session.flush();

	}

}
