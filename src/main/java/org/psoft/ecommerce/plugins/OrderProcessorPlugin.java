package org.psoft.ecommerce.plugins;

import java.util.Collection;
import java.util.List;

import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.data.model.Shipment;

public interface OrderProcessorPlugin {

	public void submitOrders(List<Order> orders) throws Exception;
}
