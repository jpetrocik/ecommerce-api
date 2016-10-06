package org.psoft.ecommerce.util;

import org.apache.commons.lang.Validate;

public class CreditCardUtils {

	public static String CREDIT_CARD_PREFIX_VISA = "4";

	public static String CREDIT_CARD_PREFIX_DISCOVER = "6";

	public static String CREDIT_CARD_PREFIX_MASTERCARD = "5";

	public static String CREDIT_CARD_PREFIX_ENTERTAINMENT_CARD = "3";

	public static String CREDIT_CARD_PREFIX_AMERICAN_EXPRESS = CREDIT_CARD_PREFIX_ENTERTAINMENT_CARD + "7";

	public static String CREDIT_CARD_PREFIX_DINERS_CLUB = CREDIT_CARD_PREFIX_ENTERTAINMENT_CARD + "6";

	public static String CREDIT_CARD_PREFIX_CARTE_BLACHE = CREDIT_CARD_PREFIX_DINERS_CLUB;

	public static String CREDIT_CARD_PREFIX_JCB_CLUB = CREDIT_CARD_PREFIX_ENTERTAINMENT_CARD + "5";

	public enum CCType {MC, VISA, AMEX, DISC, DC, CB, JCB};
	
	public static CCType creditCardType(String account){
		
		Validate.notNull(account);
		
		if (account.startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_VISA))
			return CCType.VISA;
		
		if (account.startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_DISCOVER))
			return CCType.DISC;

		if (account.startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_MASTERCARD))
			return CCType.MC;

		if (account.startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_AMERICAN_EXPRESS))
			return CCType.AMEX;

		if (account.startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_DINERS_CLUB))
			return CCType.DC;

		if (account.startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_CARTE_BLACHE))
			return CCType.CB;

		if (account.startsWith(CreditCardUtils.CREDIT_CARD_PREFIX_JCB_CLUB))
			return CCType.JCB;

		throw new RuntimeException("Unkown credit card");
		
	}
}
