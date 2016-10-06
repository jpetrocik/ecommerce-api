package org.psoft.ecommerce.data.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.util.DateServerUtil;

/**
 * @hibernate.class table="order_tbl"
 * 
 * @author jpetrocik
 * 
 */
public class Order {
	private static Log log = LogFactory.getLog(Order.class);
	
	public static final String STATUS_NEW = "New";

	public static final String STATUS_AUTH = "Authorized";

	public static final String STATUS_APPROVED = "Approved";

	public static final String STATUS_PROCESSING = "Processing";

	public static final String STATUS_SHIPPED = "Shipped";

	public static final String STATUS_SHIPPING = "Shipping";

	public static final String STATUS_COMPLETE = "Complete";

	public static final String STATUS_DECLINED = "Declined";

	public static final String STATUS_CANCELED = "Canceled";

	private static final SimpleDateFormat format = new SimpleDateFormat("-D-yy");

	private Long id;

	private String orderNum;

	private Date createdOn = DateServerUtil.currentDate().getTime();

	private String createdBy; 
	
	private Address shippingAddress;

	private Address billingAddress;

	private Account account;

	private Double subtotal = NumberUtils.DOUBLE_ZERO;

	private Double total = NumberUtils.DOUBLE_ZERO;

	private Double tax = NumberUtils.DOUBLE_ZERO;

	private Double discount = NumberUtils.DOUBLE_ZERO;

	private Double shipping = NumberUtils.DOUBLE_ZERO;

	private String comments;

	private String status;

	private List<OrderDtl> details = new ArrayList<OrderDtl>();

	private ShippingLevel shippingLevel;

	private Set<Payment> payments = new HashSet<Payment>();;

	private Set<Shipment> shipments = new HashSet<Shipment>();
	
	private Set<CouponValues> couponValues = new HashSet<CouponValues>();

	public Order() {
		status = Order.STATUS_NEW;
	}

	public void generateOrderNum() {

		if (id == null) {
			throw new RuntimeException("Order must be saved before generating an order number");
		}

		Date today = new Date();
		String dateStmp = format.format(today);

		setOrderNum(id + dateStmp);
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
	 * @hibernate.property type="timestamp"
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @hibernate.property
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	/**
	 * @hibernate.property
	 */
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Double getShipping() {
		return shipping;
	}

	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @hibernate.list table="order_dtl" inverse="true" access="field"
	 *                 cascade="all-delete-orphan"
	 * @hibernate.index column = "lineNumber"
	 * @hibernate.key column="order_id"
	 * @hibernate.one-to-many class="org.psoft.ecommerce.data.OrderDtl"
	 */
	public List<OrderDtl> getDetails() {
		return Collections.unmodifiableList(details);
	}

	public void addAllDetails(List<OrderDtl> details) {
		for (OrderDtl orderDtl : details){
			addDetail(orderDtl);
		}
	}

	public boolean addDetail(OrderDtl orderDtl) {

		//check for duplicate association
		if (details.contains(orderDtl))
			return false;

		//set reverse association
		orderDtl.setOrder(this);
		
		//create association
		Integer lineNumber = new Integer(details.size());
		orderDtl.setLineNumber(lineNumber);
		orderDtl.setOrder(this);

		return details.add(orderDtl);
	}

	/**
	 * @hibernate.many-to-one column="account_id"
	 *                        class="org.psoft.ecommerce.data.Account"
	 *                        not-null="true"
	 * @return
	 */
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @hibernate.many-to-one column="shipping_address_id" cascade="save-update"
	 *                        class="org.psoft.ecommerce.data.Address"
	 *                        not-null="true"
	 * @return
	 */
	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @hibernate.many-to-one column="billing_address_id" cascade="save-update"
	 *                        class="org.psoft.ecommerce.data.Address"
	 *                        not-null="true"
	 * @return
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @hibernate.many-to-one column="shipping_level_id"
	 *                        class="org.psoft.ecommerce.data.ShippingLevel"
	 *                        not-null="true"
	 * @return
	 */
	public ShippingLevel getShippingLevel() {
		return shippingLevel;
	}

	public void setShippingLevel(ShippingLevel shippingLevel) {
		this.shippingLevel = shippingLevel;
	}

	/**
	 * @hibernate.property
	 */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @hibernate.set table="payment" cascade="all-delete-orphan" access="field"
	 * @hibernate.key column="order_id"
	 * @hibernate.one-to-many class="org.psoft.ecommerce.data.Payment"
	 */
	public Set<Payment> getPayments() {
		return Collections.unmodifiableSet(payments);
	}

	public void addPayment(Payment payment) {
		
		//set reverse association
		payment.setOrder(this);
		
		//create association
		payments.add(payment);
	}

	/**
	 * @hibernate.set table="shipment" cascade="all-delete-orphan"
	 *                access="field"
	 * @hibernate.key column="order_id"
	 * @hibernate.one-to-many class="org.psoft.ecommerce.data.Shipment"
	 */
	public Set<Shipment> getShipments() {
		return Collections.unmodifiableSet(shipments);
	}

	public void addShipment(Shipment shipment) {
		//check for duplicate association
		if (shipments.contains(shipment))
			return;

		//set reverse association
		shipment.setOrder(this);
		
		//create association
		shipments.add(shipment);
	}

	/**
	 * 
	 * @hibernate.set table="order_to_coupon" access="field" lazy="true"
	 * @hibernate.key column="order_id"
	 * @hibernate.many-to-many class="org.psoft.ecommerce.data.CouponValues"
	 *                         column="coupon_id"
	 * 
	 * @return
	 */
	protected Set<CouponValues> getCouponValues(){
		return couponValues;
	}
	
	public Collection<Coupon> getCoupons(){
		Set<Coupon> allCoupons = new HashSet<Coupon>();
		
		for (CouponValues cv : couponValues){
			try {
				allCoupons.add(CouponValues.createCoupon(cv));
			} catch (Exception e) {
				log.warn("Skipping coupon, unable to create",e);
			}
			
		}
		
		return Collections.unmodifiableSet(allCoupons);
	}
	
	public void addCoupon(Coupon coupon) {
		couponValues.add(coupon.update());
	}
	
	public String getCouponCodes(){
		String coupon = StringUtils.EMPTY; 
		
		for (CouponValues cv : couponValues){
			if (StringUtils.isNotBlank(coupon))
				coupon += ", ";
			coupon += cv.getCode();
		}
		
		return coupon;
	}

}
