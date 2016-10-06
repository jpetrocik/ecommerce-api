package org.psoft.ecommerce.plugins.impl;

import java.util.Map;

import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.plugins.AbstractPlugin;
import org.psoft.ecommerce.plugins.CreditCardPlugin;
import org.psoft.ecommerce.service.PaymentException;

/**
 * 
 * Current API Login ID: 6D7t9xEbzu2U Current Transaction Key: 69N8p2UTuBzdU33r
 * 
 * @author jpetrocik
 * 
 */
public class AuthorizeNetPaymentPlugin extends AbstractPlugin implements CreditCardPlugin {

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
//	private static Log log = LogFactory.getLog(AuthorizeNetPaymentPlugin.class);
//
//	private static String AUTHORIZENET_API_LOGIN_SETTING = "authorizeNet_apiLogin";
//
//	private static String AUTHORIZENET_TRANSACTION_KEY_SETTING = "authorizeNet_transactionKey";
//
//	private static String AUTHORIZENET_TEST_MODE = "authorizeNet_testMode";
//
//	public static final String RESPONSE_CODE_APPROVED = "1";
//
//	public static final String RESPONSE_CODE_DECLINED = "2";
//
//	public static final String RESPONSE_CODE_ERROR = "3";
//
//	String version = "3.1";
//
//	String url = "https://secure.authorize.net/gateway/transact.dll";
//
//	public String getName() {
//		return "Authorize.net";
//	}
//
//	public Map<String, Object> getSettings() {
//		Map<String, Object> settings = new HashMap<String, Object>();
//
//		settings.put(AUTHORIZENET_API_LOGIN_SETTING, getApplicationSettings().getString(AUTHORIZENET_API_LOGIN_SETTING, null));
//		settings.put(AUTHORIZENET_TRANSACTION_KEY_SETTING, getApplicationSettings().getString(
//				AUTHORIZENET_TRANSACTION_KEY_SETTING, null));
//		settings.put(AUTHORIZENET_TEST_MODE, getApplicationSettings().getString(AUTHORIZENET_TEST_MODE, null));
//
//		return settings;
//	}
//
//	private String decrypt(String base64) throws Exception {
//		byte[] encrypted = Base64.decodeBase64(base64.getBytes());
//		String value = CipherUtil.decrypt(encrypted);
//		return value;
//	}
//
//	public void processAuthorization(Payment payment) throws PaymentException {
//
//		try {
//			Address billingAddress = payment.getOrder().getBillingAddress();
//			String name = billingAddress.getName();
//			String firstName = StringUtils.substring(name, 0, 50);
//			String lastName = StringUtils.substring(name, 50, 100);
//
//			NameValuePair[] data = {
//					// AIM
//					new NameValuePair("x_version", version),
//					new NameValuePair("x_delim_data", "True"),
//					new NameValuePair("x_relay_response", "False"),
//					new NameValuePair("x_login",
//							decrypt(getApplicationSettings().getString(AUTHORIZENET_API_LOGIN_SETTING, null))),
//					new NameValuePair("x_tran_key", decrypt(getApplicationSettings().getString(
//							AUTHORIZENET_TRANSACTION_KEY_SETTING, null))),
//					new NameValuePair("x_amount", payment.getAmount().toString()),
//					new NameValuePair("x_card_num", payment.getAccount()),
//					new NameValuePair("x_exp_date", payment.getExpires()),
//					new NameValuePair("x_type", "AUTH_ONLY"),
//					new NameValuePair("x_card_code", payment.getCvn()),
//					new NameValuePair("x_method", "CC"),
//
//					new NameValuePair("x_test_request", getApplicationSettings().getString(AUTHORIZENET_TEST_MODE, null)),
//					// new NameValuePair("x_duplicate_window", "120"),
//
//					// Billing
//					new NameValuePair("x_first_name", firstName), new NameValuePair("x_last_name", lastName),
//					new NameValuePair("x_company", StringUtils.substring(billingAddress.getCompany(), 0, 50)),
//					new NameValuePair("x_address", StringUtils.substring(billingAddress.getAddress1(), 0, 60)),
//					new NameValuePair("x_city", StringUtils.substring(billingAddress.getCity(), 0, 40)),
//					new NameValuePair("x_state", StringUtils.substring(billingAddress.getState(), 0, 40)),
//					new NameValuePair("x_zip", StringUtils.substring(billingAddress.getPostalCode(), 0, 20)),
//					new NameValuePair("x_phone", StringUtils.substring(billingAddress.getPhone(), 0, 25)),
//
//					// Invoice
//					new NameValuePair("x_invoice_num", payment.getOrder().getOrderNum()),
//
//					new NameValuePair("x_description", "EStore Order: " + payment.getOrder().getOrderNum())
//			};
//
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod(url);
//			post.setRequestBody(data);
//
//			int responseCode = client.executeMethod(post);
//			String response = post.getResponseBodyAsString();
//			log.debug(response);
//
//			String[] ccrep = response.split(",");
//			log.debug("Response Code: ");
//			log.debug(ccrep[0]);
//			log.debug("Human Readable Response Code: ");
//			log.debug(ccrep[3]);
//			log.debug("Approval Code: ");
//			log.debug(ccrep[4]);
//			log.debug("Trans ID: ");
//			log.debug(ccrep[6]);
//			log.debug("MD5 Hash Server: ");
//			log.debug(ccrep[37]);
//
//			payment.setResponseCode(ccrep[1] + ":" + ccrep[2]);
//			payment.setMessage(ccrep[3]);
//
//			if (ccrep[0].equals(AuthorizeNetPaymentPlugin.RESPONSE_CODE_ERROR)) {
//				payment.setStatus(Payment.ERROR);
//				log.error("AuthorizeNet reporting error processing card");
//				return;
//			}
//
//			if (ccrep[0].equals(AuthorizeNetPaymentPlugin.RESPONSE_CODE_APPROVED)) {
//				payment.setStatus(Payment.AUTH);
//				// payment.getOrder().setStatus(Order.STATUS_APPROVED);
//			} else {
//				payment.setStatus(Payment.FAILED);
//				// payment.getOrder().setStatus(Order.STATUS_DECLINED);
//			}
//
//			payment.setAuthCode(ccrep[4]);
//			payment.setRefNum(ccrep[6]);
//			payment.setAvs(ccrep[5]);
//			// payment.setCvnResp(ccrep[38]); //Not getting returned by gateway
//
//		} catch (Exception e) {
//			log.error("Unable to process credit card authoziation", e);
//			payment.setStatus(Payment.ERROR);
//		}
//	}
//
//	public void processCapture(Payment payment, Double amount) throws PaymentException {
//		try {
//
//			NameValuePair[] data = {
//					// AIM
//					new NameValuePair("x_version", version),
//					new NameValuePair("x_delim_data", "True"),
//					new NameValuePair("x_relay_response", "False"),
//					new NameValuePair("x_login",
//							decrypt(getApplicationSettings().getString(AUTHORIZENET_API_LOGIN_SETTING, null))),
//					new NameValuePair("x_tran_key", decrypt(getApplicationSettings().getString(
//							AUTHORIZENET_TRANSACTION_KEY_SETTING, null))), new NameValuePair("x_amount", amount.toString()),
//					new NameValuePair("x_type", "PRIOR_AUTH_CAPTURE"), new NameValuePair("x_trans_id", payment.getRefNum()),
//					new NameValuePair("x_method", "CC"),
//
//					new NameValuePair("x_test_request", getApplicationSettings().getString(AUTHORIZENET_TEST_MODE, null)),
//			// new NameValuePair("x_duplicate_window", "120"),
//
//			};
//
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod(url);
//			post.setRequestBody(data);
//
//			int responseCode = client.executeMethod(post);
//			String response = post.getResponseBodyAsString();
//			log.debug(response);
//
//			String[] ccrep = response.split(",");
//			log.debug("Response Code: ");
//			log.debug(ccrep[0]);
//			log.debug("Human Readable Response Code: ");
//			log.debug(ccrep[3]);
//			log.debug("Approval Code: ");
//			log.debug(ccrep[4]);
//			log.debug("Trans ID: ");
//			log.debug(ccrep[6]);
//			log.debug("MD5 Hash Server: ");
//			log.debug(ccrep[37]);
//
//			payment.setResponseCode(ccrep[1] + ":" + ccrep[2]);
//			payment.setMessage(ccrep[3]);
//
//			if (ccrep[0].equals(AuthorizeNetPaymentPlugin.RESPONSE_CODE_ERROR)) {
//				payment.setStatus(Payment.ERROR);
//				log.error("AuthorizeNet reporting error processing card");
//				return;
//			}
//
//			if (ccrep[0].equals(AuthorizeNetPaymentPlugin.RESPONSE_CODE_APPROVED)) {
//				payment.setStatus(Payment.CAPTURED);
//				payment.setCapturedAmount(amount);
//				// payment.getOrder().setStatus(Order.STATUS_APPROVED);
//			} else {
//				payment.setStatus(Payment.FAILED);
//				// payment.getOrder().setStatus(Order.STATUS_DECLINED);
//			}
//
//		} catch (Exception e) {
//			log.error("Unable to process credit card authoziation", e);
//			payment.setStatus(Payment.ERROR);
//		}
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
