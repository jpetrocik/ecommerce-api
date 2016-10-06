package org.psoft.ecommerce.util;

import org.apache.commons.lang.StringUtils;

public class StringTool {

	public boolean isEmpty(String value) {
		return StringUtils.isEmpty(value);
	}

	public boolean isNotEmpty(String value) {
		return !StringUtils.isEmpty(value);
	}
}
