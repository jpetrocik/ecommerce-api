package org.psoft.ecommerce.plugins;

public interface InventoryPlugin extends Plugin {

	public int available(String sku);

	public int allocate(String sku, int qty);

	public String stock(String sku);

}
