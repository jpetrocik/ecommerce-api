package org.psoft.ecommerce.plugins.impl;

import java.util.HashMap;
import java.util.Map;

import org.psoft.ecommerce.plugins.AbstractPlugin;
import org.psoft.ecommerce.plugins.InventoryPlugin;

public class InStockInventoryPlugin extends AbstractPlugin implements InventoryPlugin {

	private static int DEFAULT_STOCK_LEVEL = 100;

	public int allocate(String sku, int qty) {
		return DEFAULT_STOCK_LEVEL;
	}

	public int available(String sku) {
		return DEFAULT_STOCK_LEVEL;
	}

	public String stock(String sku) {
		return "In Stock";
	}

	public String getName() {
		return "In-Stock Inventory";
	}

	public Map<String, Object> getSettings() {
		return new HashMap<String, Object>();
	}

}
