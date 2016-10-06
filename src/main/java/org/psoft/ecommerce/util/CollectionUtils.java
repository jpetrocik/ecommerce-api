package org.psoft.ecommerce.util;

import java.util.Collection;
import java.util.List;

public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

	static public Object firstElement(Collection list) {

		if (list == null || list.isEmpty())
			return null;

		if (list instanceof List)
			return ((List) list).get(0);

		return list.iterator().next();
	}
}
