package org.psoft.ecommerce.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.Coupon;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.data.model.OrderDtl;
import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.data.model.Pricing;
import org.psoft.ecommerce.data.model.ShippingLevel;
import org.psoft.ecommerce.data.model.ShippingRate;
import org.psoft.ecommerce.util.CollectionUtils;
import org.psoft.ecommerce.util.DateServerUtil;
import org.psoft.ecommerce.util.NumberUtils;

public class CartService extends AbstractService<Order> {
	private static Log log = LogFactory.getLog(CartService.class);

	private TaxService taxService;

	private ShippingService shippingService;

	private PaymentService paymentService;

	private CouponService couponService;

	private ItemService itemService;

	private List<OrderDtl> items = new ArrayList<OrderDtl>();

	private Address billingAddress;

	private Address shippingAddress;

	private ShippingLevel _shippingLevel;

	private Double shippingCharges = NumberUtils.DOUBLE_ZERO;

	private Double tax = NumberUtils.DOUBLE_ZERO;

	private Double subtotal = NumberUtils.DOUBLE_ZERO;

	private Double discount = NumberUtils.DOUBLE_ZERO;

	private boolean submitted = false;

	private Account account;

	private List<Coupon> coupons = new ArrayList<Coupon>();

	public CartService() {
		log.info("Creating new shopping cart");
	}

	/**
	 * The cart needs to be initialized to re-associate detached objects with
	 * session
	 * 
	 */
	public void merge() {

		if (getShippingLevel() != null)
			currentSession().update(getShippingLevel());

		if (account != null) {
			currentSession().update(account);
		}

		for (OrderDtl orderDtl : items) {
			currentSession().update(orderDtl.getItem());
		}

	}

	public void clear() {
		items.clear();
		billingAddress = null;
		shippingAddress = null;
		setShippingLevel((ShippingLevel)null);
		shippingCharges = NumberUtils.DOUBLE_ZERO;
		tax = NumberUtils.DOUBLE_ZERO;
		subtotal = NumberUtils.DOUBLE_ZERO;
		discount = NumberUtils.DOUBLE_ZERO;
		account = null;
		coupons.clear();
		submitted = false;
	}

	public OrderDtl add(Long itemId, Integer qty) {
		Item item = itemService.retrieveById(itemId);

		Validate.notNull(item, "Unknown item itemId:" + itemId);

		return add(item, qty);
	}

	public OrderDtl add(String sku, Integer qty) {
		Item item = itemService.retreiveByPartNumber(sku);

		Validate.notNull(item, "No item with sku " + sku);

		return add(item, qty);
	}

	public void addOrder(Long orderId) {
		Order order = (Order) currentSession().get(Order.class, orderId);

		Validate.notNull(order, "Unable to locate order " + orderId + " to reorder");

		setAccount(order.getAccount());
		
		addOrder(order);
		
	}

	public void addOrder(Order order) {

		List<OrderDtl> orderDetails = order.getDetails();

		for (OrderDtl orderDetail : orderDetails) {
			add(orderDetail.getItem(), orderDetail.getQty());
		}
	}

	/**
	 * Adds a new item to the cart or updates the existing quantity. If the qty
	 * is 0 then the item is removed.
	 * 
	 * @param item
	 * @param price
	 * @param qty
	 */
	public OrderDtl add(final Item item, Integer qty) {

		if (!item.getIsActive()) {
			log.info("Not allowed to added inactive items to cart");
			return null;
		}

		// Look for existing item in cart already
		OrderDtl orderDtl = (OrderDtl) CollectionUtils.find(items, new Predicate() {
			public boolean evaluate(Object arg0) {
				OrderDtl orderDtl = (OrderDtl) arg0;
				if (orderDtl.getItem().equals(item))
					return true;
				return false;
			}
		});

		// remove existing item from cart
		if (orderDtl != null && qty == 0) {
			items.remove(orderDtl);

		} else if (qty > 0) {

			//validate the quantity
			validateQuantity(item, qty);

			// adding new item
			if (orderDtl == null) {
				orderDtl = new OrderDtl();
				orderDtl.setItem(item);
				items.add(orderDtl);
			}

			orderDtl.setQty(qty);
		}

		// Recalculate after cart is updated
		calculate();

		return orderDtl;
	}

	public void addCoupon(String code) throws UserException {
		Coupon coupon = couponService.getCouponByCode(code);

		if (coupon == null) {
			throw new UserException("coupon.invalid", new String[] { code });
		}

		if (!coupon.isValid()) {
			throw new UserException("coupon.invalid", new String[] { code });
		}

		if (!acceptCoupon(coupon)) {
			throw new UserException("coupon.exculsive", null);
		}

		//see if coupon applies
		coupon.apply(this);

		coupons.add(coupon);

		// Recalculate after applying coupon
		calculate();
	}
	
	public List<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * Iterates over the applied coupons to determine if this coupon can be
	 * accepted.
	 * 
	 * @param coupon
	 * @return
	 */
	private boolean acceptCoupon(final Coupon coupon) {

		try {
			CollectionUtils.forAllDo(coupons, new Closure() {

				public void execute(Object object) {
					Coupon appliedCoupon = (Coupon) object;

					if (coupon.isExculsive() && appliedCoupon.isExculsive()) {
						throw new RuntimeException("Error coupon is exculsive");
					}

					if (coupon.getCode().equals(appliedCoupon.getCode())) {
						throw new RuntimeException("Error duplicate coupon");
					}

				}

			});
		} catch (RuntimeException re) {
			log.debug("Coupon not accpeted", re);
			return false;
		}

		return true;
	}

	/**
	 * Returns a list of OrderDtls add to the cart
	 * 
	 * @return
	 */
	public List<OrderDtl> getItems() {
		return Collections.unmodifiableList(items);
	}

	public int getItemCount() {
		return items.size();
	}

	/**
	 * Returns the billing address
	 * 
	 * @return
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * Sets the billing address
	 * 
	 * @param billingAddress
	 */
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * Gets the shipping address
	 * 
	 * @return
	 */
	public Address getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * Sets the shipping address
	 * 
	 * @param shippingAddress
	 */
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;

		// Recalculate if shipping address changes
		calculate();
	}

	/**
	 * Shipping level
	 * 
	 * @return
	 */
	public ShippingLevel getShippingLevel() {
		return _shippingLevel;
	}

	/**
	 * Sets the shipping level
	 * 
	 * @param service
	 * @return
	 */
	public void setShippingLevel(String service) {
		Session session = currentSession();

		Criteria c = session.createCriteria(ShippingLevel.class, service);
		ShippingLevel shippingLevel = (ShippingLevel) get(c);

		if (shippingLevel == null)
			return;

		setShippingLevel(shippingLevel);

	}

	/**
	 * Gets the shipping level
	 * 
	 * @param shippingLevel
	 */
	public void setShippingLevel(ShippingLevel shippingLevel) {
		this._shippingLevel = shippingLevel;
		
		//Recalculate after shipping level changes
		calculate();
	}

	/**
	 * Calculates the sub total
	 * 
	 * @return
	 */
	public Double getSubtotal() {
		return subtotal;
	}

	/**
	 * Calculates the order total
	 * 
	 * @return
	 */
	public Double getTotal() {
		try {
			return NumberUtils.round(getSubtotal() + getShipping() + getTax() - getDiscount(), 2);
		} catch (Exception e) {
			log.error("Unable to calculate total", e);
		}
		return new Double(0);

	}

	/**
	 * Discount
	 * 
	 * @return
	 */
	public Double getDiscount() {

		if (discount > getSubtotal()) {
			return getSubtotal();
		}

		return discount;
	}

	/**
	 * Called from coupons to set discount
	 * 
	 * @param amount
	 */
	public void addDiscount(Double amount) {
		discount += amount;
	}

	/**
	 * Calculates the tax using the configured tax service
	 * 
	 * @return
	 */
	public Double getTax() {
		return tax;
	}

	/**
	 * Calculates the shipping rate using the configured shipping service
	 * 
	 * @return
	 */
	public Double getShipping() {
		return shippingCharges;
	}

	/**
	 * Call from coupons to clear shipping.
	 * 
	 * @param shipping
	 */
	public void setShipping(Double shipping) {
		shippingCharges = shipping;
	}

	/**
	 * Recalculated the order totals
	 * 
	 */
	private void calculate() {

		calculateSubTotal();
		calculatTax();
		calculateShipping();
		applyCoupons();
	}

	private void calculateSubTotal() {

		AccountType accountType = null;
		if (account != null) {
			accountType = account.getAccountType();
		}

		subtotal = new Double(0);

		for (OrderDtl orderDtl : items) {
			Pricing pricing = itemService.retireveCurrentPricing(orderDtl.getItem(), accountType);
			Double price = pricing.getPrice();

			//calculate price before checking mins
			orderDtl.setPrice(price);
			subtotal = subtotal + orderDtl.getExtPrice();
		}
	}

	public void validateQrder() throws OrderValidationException {
		
		OrderValidationException orderValidationException = null;
		
		for(OrderDtl orderDtl : this.items){
			try {
				validateQuantity(orderDtl.getItem(), orderDtl.getQty());
			} catch (Exception e) {
				if (orderValidationException == null)
					orderValidationException = new OrderValidationException();
				
				orderValidationException.addException(e);
			}
		}
		
		if (orderValidationException != null)
			throw orderValidationException;
	}
	
	private void validateQuantity(Item item, Integer qty){
		AccountType accountType = null;
		if (account != null) {
			accountType = account.getAccountType();
		}

		Pricing pricing = itemService.retireveCurrentPricing(item, accountType);

		/**
		 * Based on the pricing level check that the 
		 * ordered qty is valid for the min and multiplier 
		 */
		Integer minQty = pricing.getMinimum();
		Integer mulitpler = pricing.getMultiplier();
		if (qty<minQty){
			throw new InvalidOrderQty(item,pricing, qty);
		}
		if (qty%mulitpler>0){
			throw new InvalidOrderQty(item,pricing, qty);
		}
		
	}

	private void calculatTax() {
		tax = taxService.calculateTax(this).doubleValue();
	}

	private void calculateShipping() {
		if (shippingAddress == null) {
			log.info("Shipping address is required to calculate shipping");
			shippingCharges = NumberUtils.DOUBLE_ZERO;
			return;
		}

		Double subtotal = getSubtotal();
		if (subtotal.equals(NumberUtils.DOUBLE_ZERO)) {
			log.info("Shipping subtotal is zero");
			shippingCharges = NumberUtils.DOUBLE_ZERO;
			return;
		}

		List<ShippingRate> rates = shippingService.lookupShippingOptions(account, shippingAddress, null, subtotal);

		// if shipping level is set return shipping level rate
		if (getShippingLevel() != null) {
			for (ShippingRate rate : rates) {
				if (rate.getShippingLevel().equals(getShippingLevel())) {
					shippingCharges = rate.getRate();
					break;
				}
			}
		} else {

			ShippingRate lowestRate = (ShippingRate) CollectionUtils.firstElement(rates);
			for (ShippingRate rate : rates) {
				if (rate.getRate().compareTo(lowestRate.getRate()) < 0) {
					lowestRate = rate;
				}
			}

			shippingCharges = lowestRate.getRate();
			setShippingLevel(lowestRate.getShippingLevel());

		}
	}

	/**
	 * Applies or Re-Applies the coupons
	 */
	private void applyCoupons() {

		discount = NumberUtils.DOUBLE_ZERO;

		Iterator<Coupon> iter = coupons.iterator();
		
		while (iter.hasNext()) {
			Coupon coupon = iter.next();
			try {
				coupon.apply(this);
			} catch (Exception e) {
				log.info("Coupon " + coupon.getCode() + " not currently valid, removing");
				iter.remove();
			}
		}
	}

	/**
	 * Returns the available shipping options using the configured shipping
	 * service
	 * 
	 * @return
	 */
	public List<ShippingRate> getShippingOptions() {
		return shippingService.lookupShippingOptions(account, shippingAddress, null, getSubtotal());
	}

	/**
	 * Completes the order process and saves the order
	 * 
	 * @param account
	 * @return
	 * @throws OrderValidationException 
	 */
	public Order placeOrder(Payment payment, String salesRep) throws OrderValidationException {

		//validate order before placing order
		validateQrder();
		
		if (submitted == true) {
			throw new RuntimeException("Order has already been submited");
		}

		submitted = true;

		Order order;
		try {
			order = new Order();
			order.setAccount(account);
			order.addAllDetails(items);
			order.setBillingAddress(billingAddress);
			order.setShippingAddress(shippingAddress);
			order.setTax(getTax());
			order.setDiscount(getDiscount());
			order.setTotal(getTotal());
			order.setSubtotal(getSubtotal());
			order.setShipping(getShipping());
			order.setShippingLevel(getShippingLevel());
			order.setCreatedOn(DateServerUtil.currentDate().getTime());
			
			if (StringUtils.isNotBlank(salesRep))
				order.setCreatedBy(salesRep);

			order.addPayment(payment);

			for (Coupon coupon : coupons){
				order.addCoupon(coupon);
			}
			
			currentSession().save(order);
			currentSession().flush();

			order.generateOrderNum();

			currentSession().save(order);
			currentSession().flush();
		} catch (RuntimeException re) {
			submitted = false;
			throw re;
		}

		// authorize
		try {
			paymentService.processAuthorization(payment);
		} catch (Exception e) {
			log.error("Unable to process authorization request", e);
			submitted = false;
		}

		try {
			if (payment.getStatus().equals(Payment.AUTH)) {

				order.setStatus(Order.STATUS_APPROVED);

//				getEmailService().sendPlainTextEmail(order.getAccount().getEmail(), "/order_confirm.vm", order);

			} else {

				order.setStatus(Order.STATUS_DECLINED);

				submitted = false;
			}
		} catch (Exception e) {
			log.error("Unable to send order confirmation email", e);
		}

		currentSession().flush();
		currentSession().update(order);

		return order;

	}

	public boolean isSubmitted() {
		return submitted;
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public Account getAccount() {
		return account;
	}

	public Account setAccount(Long id) {
		Account account = (Account) currentSession().get(Account.class, id);

		//If the account changes reset the billing and
		//shipping addresses
		billingAddress = account.getBillingAddress().copy();
		shippingAddress = account.getShippingAddress().copy();

		setAccount(account);

		return account;
	}

	public void setAccount(Account account) {
		this.account = account;

		//Recalculate if account type changes
		calculate();
	}

	/**
	 * Sets the shipping service
	 * 
	 * @return
	 */
	public ShippingService getShippingService() {
		return shippingService;
	}

	/**
	 * Gets the shipping service
	 * 
	 * @param shippingService
	 */
	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

	/**
	 * Sets the tax service
	 * 
	 * @return
	 */
	public TaxService getTaxService() {
		return taxService;
	}

	/**
	 * Gets the tax service
	 * 
	 * @param taxService
	 */
	public void setTaxService(TaxService taxService) {
		this.taxService = taxService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public CouponService getCouponService() {
		return couponService;
	}

	public void setCouponService(CouponService couponService) {
		this.couponService = couponService;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

}
