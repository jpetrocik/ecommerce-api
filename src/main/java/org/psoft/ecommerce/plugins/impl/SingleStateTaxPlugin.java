package org.psoft.ecommerce.plugins.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.plugins.AbstractPlugin;
import org.psoft.ecommerce.plugins.TaxPlugin;
import org.psoft.ecommerce.service.CartService;

public class SingleStateTaxPlugin extends AbstractPlugin implements TaxPlugin {
	private static Log log = LogFactory.getLog(SingleStateTaxPlugin.class);

	private static String TAXABLE_STATE_SETTING = "taxable_state";

	private static String TAXABLE_RATE_SETTING = "taxable_rate";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.psoft.ecommerce.service.plugins.impl.TaxPlugin#getName()
	 */
	public String getName() {
		return "Single State Tax";
	}

	public Map<String, Object> getSettings() {
		Map<String, Object> settings = new HashMap<String, Object>();

		settings.put(TAXABLE_RATE_SETTING, getApplicationSettings().getDouble(SingleStateTaxPlugin.TAXABLE_RATE_SETTING,
				NumberUtils.DOUBLE_ZERO));
		settings.put(TAXABLE_STATE_SETTING, getApplicationSettings().getString(SingleStateTaxPlugin.TAXABLE_STATE_SETTING, null));

		return settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.psoft.ecommerce.service.plugins.impl.TaxPlugin#calculateTax(java.
	 * lang.Double, org.psoft.ecommerce.data.Address)
	 */
	public BigDecimal calculateTax(CartService cartService){
		Double amt = cartService.getSubtotal() - cartService.getDiscount();
		Address address = cartService.getShippingAddress();
		Account account = cartService.getAccount();

		BigDecimal tax = BigDecimal.ZERO;

		//Skip tax on wholesaler
		if (account != null){
			 AccountType accountType = account.getAccountType();
			 if (isWholesaler(accountType)) {
				 log.warn("No tax calculated for wholesaler accounts");
				 return BigDecimal.ZERO;
			 }
		}

		if (address == null) {
			log.warn("To calculate tax the address must not be null");
			return tax;
		}

		tax = new BigDecimal(amt * getTaxRate(address));

		return tax.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.psoft.ecommerce.service.plugins.impl.TaxPlugin#getTaxRate(org.psoft
	 * .ecommerce.data.Address)
	 */
	public Double getTaxRate(Address address) {

		Double rate = NumberUtils.DOUBLE_ZERO;

		if (address != null && StringUtils.isNotBlank(address.getState())) {
			String applicableState = getApplicationSettings().getString(SingleStateTaxPlugin.TAXABLE_STATE_SETTING, null);

			if (address.getState().toUpperCase().equals(applicableState)) {
				rate = getApplicationSettings().getDouble(SingleStateTaxPlugin.TAXABLE_RATE_SETTING, NumberUtils.DOUBLE_ZERO);
			}
		}

		return rate;
	}

}
