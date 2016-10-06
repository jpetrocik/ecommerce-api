package org.psoft.ecommerce.plugins.impl;

import java.util.Map;

import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.plugins.AbstractPlugin;
import org.psoft.ecommerce.plugins.CreditCardPlugin;
import org.psoft.ecommerce.service.PaymentException;

public class PaymentechService extends AbstractPlugin implements CreditCardPlugin {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Map<String, Object> getSettings() {
		return null;
	}

	@Override
	public boolean accepts(Payment payment) {
		return false;
	}

	@Override
	public void processAuthorization(Payment payment) throws PaymentException {
	}

	@Override
	public void processCapture(Payment payment, Double amount) throws PaymentException {
	}
	
//	private static Log log = LogFactory.getLog(PaymentechService.class);
//
//	// This program looks for the configFile in
//	public final static String configFile = "linehandler.properties";
//
//	private final static int OP_AUTH = 1;
//
//	private final static int OP_CAPTURE = 2;
//
//	// Global variables
//	private ConfiguratorIF configurator;
//
//	private TransactionProcessorIF tp;
//
//	private String merchantId;
//
//	public String getName() {
//		return "Paymentech Payment Processor";
//	}
//
//	public Map<String, Object> getSettings() {
//		Map<String, Object> settings = new HashMap<String, Object>();
//
//		return settings;
//	}
//
//	public void initialize() throws Exception {
//
//		// Create a configurator
//		// The configurator is a "singleton" object that contains system
//		// configurations.
//		// It loads its properties from the configuration file, that is for
//		// historical
//		// reasons commonly named "linehandler.properties"
//		// The configurator also initializes and contains references to the
//		// Paymentech
//		// loggers (eCommerceLogger and engineLogger).
//		try {
//			configurator = Configurator.getInstance(configFile);
//		} catch (InitializationException ie) {
//			log.error("Configurator initialization failed.", ie);
//			throw ie;
//		}
//
//		// Create a Transaction Processor
//		// The Transaction Processor acquires and releases resources and
//		// executes transactions.
//		// It configures a pool of protocol engines, then uses the pool to
//		// execute transactions.
//		try {
//			tp = new TransactionProcessor();
//		} catch (InitializationException iex) {
//			log.error("TransactionProcessor failed to initialize", iex);
//			throw iex;
//		}
//
//	}
//
//	public void processAuthorization(Payment payment) throws PaymentException {
//
//		// Create a request object
//		RequestIF request = null;
//		try {
//			request = new Request(RequestIF.NEW_ORDER_TRANSACTION);
//
//			// Basic Auth Fields
//			request.setFieldValue("IndustryType", "EC");
//			request.setFieldValue("MessageType", "A");
//			request.setFieldValue("MerchantID", merchantId);
//			request.setFieldValue("BIN", "000002");
//			request.setFieldValue("OrderID", payment.getOrder().getOrderNum());
//			request.setFieldValue("AccountNum", payment.getAccount());
//			request.setFieldValue("Amount", formatAmount(payment.getAmount()));
//			request.setFieldValue("Exp", payment.getExpires());
//
//			// AVS Information
//			request.setFieldValue("AVSname", prepareField(payment.getOrder().getBillingAddress().getName(), 30));
//			request.setFieldValue("AVSaddress1", prepareField(payment.getOrder().getBillingAddress().getAddress1(), 30));
//			request.setFieldValue("AVScity", prepareField(payment.getOrder().getBillingAddress().getCity(), 20));
//			request.setFieldValue("AVSstate", prepareField(payment.getOrder().getBillingAddress().getState(), 2));
//			request.setFieldValue("AVSzip", prepareField(payment.getOrder().getBillingAddress().getPostalCode(), 10));
//
//			// Additional Information
//			request.setFieldValue("Comments", "Estore Transaction");
//
//			// Uncomment the line below and modify to add a card
//			// security value (CVV2, CVC2 or CID)
//			if (!GenericValidator.isBlankOrNull(payment.getCvn()))
//				request.setFieldValue("CardSecVal", payment.getCvn());
//
//			if (payment.getAccount().startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_VISA)
//					|| payment.getAccount().startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_MASTERCARD)) {
//				if (!GenericValidator.isBlankOrNull(payment.getCvn()))
//					request.setFieldValue("CardSecValInd", "1");
//			}
//
//			// Retry logic
//			request.setTraceNumber(calculateTraceNumber(payment, OP_AUTH));
//
//			// Display the request
//			log.debug("\nAuth Request:\n" + request.getXML());
//		} catch (InitializationException ie) {
//			log.error("Unable to initialize request object", ie);
//			throw new PaymentException("Unable to initialize request object", ie);
//		} catch (FieldNotFoundException fnfe) {
//			log.error("Unable to find XML field in template", fnfe);
//			throw new PaymentException("Unable to find XML field in template", fnfe);
//		} catch (Exception e) {
//			log.error("Unknown exception process authorization", e);
//			throw new PaymentException("Unknown exception process authorization", e);
//		}
//
//		// Process the transaction
//		// If the resources required by the Transaction Processor have
//		// been exhausted, this code will block until the resources become
//		// available.
//		ResponseIF response = null;
//		try {
//			response = tp.process(request);
//		} catch (TransactionException tex) {
//			log.error("Transaction failed, including retries and failover", tex);
//			throw new PaymentException("Transaction failed, including retries and failover", tex);
//		}
//
//		log.debug("\nResponse:\n" + response.toXmlString() + "\n");
//
//		payment.setResponseCode(response.getResponseCode());
//		payment.setMessage(response.getMessage());
//
//		if (response.isError()) {
//			payment.setStatus(Payment.ERROR);
//			log.error("Paymentech reporting error processing card");
//			return;
//		}
//
//		if (response.isApproved()) {
//			payment.setStatus(Payment.AUTH);
//			payment.getOrder().setStatus(Order.STATUS_APPROVED);
//		} else {
//			payment.setStatus(Payment.FAILED);
//			payment.getOrder().setStatus(Order.STATUS_DECLINED);
//		}
//
//		payment.setAuthCode(response.getAuthCode());
//		payment.setRefNum(response.getTxRefNum());
//		payment.setAvs(response.getAVSResponseCode());
//		payment.setCvnResp(response.getCVV2RespCode());
//
//	}
//
//	public void processCapture(Payment payment, Double amount) throws PaymentException {
//
//		// Create a request object
//		RequestIF request = null;
//		try {
//
//			request = new Request(RequestIF.MARK_FOR_CAPTURE_TRANSACTION);
//
//			request.setFieldValue("MerchantID", merchantId);
//			request.setFieldValue("BIN", "000002");
//			request.setFieldValue("TxRefNum", payment.getRefNum());
//			request.setFieldValue("OrderID", payment.getOrder().getOrderNum());
//			request.setFieldValue("Amount", formatAmount(amount));
//
//			// Retry logic
//			request.setTraceNumber(calculateTraceNumber(payment, OP_CAPTURE));
//
//		} catch (InitializationException ie) {
//			log.error("Unable to initialize request object", ie);
//			throw new PaymentException("Unable to initialize request object", ie);
//		} catch (FieldNotFoundException fnfe) {
//			log.error("Unable to find XML field in template", fnfe);
//			throw new PaymentException("Unable to find XML field in template", fnfe);
//		} catch (Exception e) {
//			log.error("Unknown exception process authorization", e);
//			throw new PaymentException("Unknown exception process authorization", e);
//		}
//
//		// Process the transaction
//		// If the resources required by the Transaction Processor have
//		// been exhausted, this code will block until the resources become
//		// available.
//		ResponseIF response = null;
//		try {
//			response = tp.process(request);
//		} catch (TransactionException tex) {
//			log.error("Transaction failed, including retries and failover", tex);
//			throw new PaymentException("Transaction failed, including retries and failover", tex);
//		}
//
//		log.debug("\nResponse:\n" + response.toXmlString() + "\n");
//
//		// payment.setResponseCode(response.getResponseCode());
//		// payment.setRefNum(response.getTxRefNum());
//
//		if (response.isGood()) {
//			payment.setStatus(Payment.CAPTURED);
//		} else {
//			payment.setStatus(Payment.ERROR);
//			log.error("Paymentech reporting error processing card");
//			return;
//			// throw new PaymentException("Paymentech responded with error");
//		}
//
//	}
//
//	protected String calculateTraceNumber(Payment payment, int trans) {
//		String[] pieces = payment.getOrder().getOrderNum().split("\\W");
//
//		StringBuffer trace = new StringBuffer("4");
//		for (String part : pieces) {
//			trace.append(part);
//		}
//
//		trace.append(trans);
//
//		System.out.println(trace.toString());
//		return trace.toString();
//
//	}
//
//	protected String formatAmount(Double amount) {
//		DecimalFormat dFormat = new DecimalFormat("#0.00");
//		String value = String.valueOf(dFormat.format(amount));
//		return value.replaceAll("\\.", "");
//	}
//
//	protected String prepareField(String value, int length) {
//
//		String subValue = StringUtils.substring(value, 0, length);
//
//		return StringEscapeUtils.escapeXml(subValue);
//	}
//
//	public String getMerchantId() {
//		return merchantId;
//	}
//
//	public void setMerchantId(String merchantId) {
//		this.merchantId = merchantId;
//	}
//
//	public boolean accepts(Payment payment) {
//		String ccType = payment.getType();
//		
//		try {
//			CCType.valueOf(ccType);
//		} catch (Exception e){
//			return false;
//		}
//		
//		return true;
//	}

}
