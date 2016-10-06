package org.psoft.ecommerce.data.model;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.crypt.CipherUtil;
import org.psoft.ecommerce.plugins.impl.Net30PaymentPlugin;

/**
 * @hibernate.class table="payment"
 * 
 * @author jpetrocik
 * 
 */
public class Payment {
	private static Log log = LogFactory.getLog(Payment.class);

	public static final String NEW = "new";

	public static final String AUTH = "authorized";

	public static final String FAILED = "failed";

	public static final String CAPTURED = "captured";

	public static final String EXPIRED = "expired";

	public static final String ERROR = "error";

	public static final String CVV = "cvv";

	private Long id;

	private String type;

	private byte[] account;

	private String cvn;

	private String expires;

	private String status;

	private Order order;

	private Double amount;

	private Double capturedAmount;

	private String authCode;

	private String refNum;

	private String avs;

	private String cvnResp;

	private String responseCode;

	private String message;

	public Payment(){
		capturedAmount=NumberUtils.DOUBLE_ZERO;
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
	 * @hibernate.property not-null="true" access="field" type="byte[]"
	 */
	public String getAccount() {
		try {
			return CipherUtil.decrypt(account);
		} catch (Exception e) {
			log.error("Unable to decrypt account number", e);
			throw new RuntimeException("Unable to process account number");
		}
	}

	public void setAccount(String account) {
		try {
			this.account = CipherUtil.encrypt(account);
		} catch (Exception e) {
			log.error("Unable to encrypt account number", e);
			throw new RuntimeException("Unable to process account number");
		}
	}

	public String getSafeAccount() {
		String raw = getAccount();
		if (getType().equals(Net30PaymentPlugin.NET30))
			return raw;
		return "XXXXXXXXXXXX" + raw.substring(12);
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	/**
	 * @hibernate.many-to-one column="order_id"
	 *                        class="org.psoft.ecommerce.data.Order"
	 *                        access="field"
	 * @return
	 */
	public Order getOrder() {
		return order;
	}

	protected void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @hibernate.property
	 */
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * @hibernate.property
	 */
	public String getAvs() {
		return avs;
	}

	public void setAvs(String avs) {
		this.avs = avs;
	}

	public String getCvn() {
		return cvn;
	}

	public void setCvn(String cvv) {
		this.cvn = cvv;
	}

	/**
	 * @hibernate.property
	 */
	public String getRefNum() {
		return refNum;
	}

	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}

	/**
	 * @hibernate.property
	 */
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @hibernate.property
	 */
	public Double getCapturedAmount() {
		return capturedAmount;
	}

	public void setCapturedAmount(Double capAmount) {
		this.capturedAmount = capAmount;
	}

	/**
	 * @hibernate.property
	 */
	public String getCvnResp() {
		return cvnResp;
	}

	public void setCvnResp(String cvnResp) {
		this.cvnResp = cvnResp;
	}

	/**
	 * @hibernate.property
	 */
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @hibernate.property
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Payment duplicate(){
		Payment newPayment = new Payment();
		
		newPayment.setAccount(getAccount());
		newPayment.setAmount(amount-capturedAmount);
		newPayment.setCvn(cvn);
		newPayment.setExpires(expires);
		newPayment.setStatus(NEW);
		newPayment.setType(type);

		getOrder().addPayment(newPayment);
		
		return newPayment;
	}
	
}
