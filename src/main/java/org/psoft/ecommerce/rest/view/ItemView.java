package org.psoft.ecommerce.rest.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.psoft.ecommerce.data.model.AttributeName;
import org.psoft.ecommerce.data.model.AttributeValue;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Pricing;

public class ItemView {

	Item item;

	public static ItemView create(Item item){
		return new ItemView(item);
	}
	
	private ItemView(Item item) {
		this.item = item;
	}

	public Long getId() {
		return item.getId();
	}

	public String getKeywords() {
		return item.getKeywords();
	}

	public String getLongDescr() {
		return item.getLongDescr();
	}

	public String getPartNum() {
		return item.getPartNum();
	}

	public String getShortDescr() {
		return item.getShortDescr();
	}

	public String getName() {
		return item.getName();
	}

	public boolean isActive() {
		return item.isActive();
	}

	public String getUpc() {
		return item.getUpc();
	}

	@SuppressWarnings("unchecked")
	public List<PricingView> getPricing() {
		Set<Pricing> pricing = item.getPricing();

		return (List<PricingView>) CollectionUtils.collect(pricing, new Transformer() {

			public Object transform(Object input) {
				Pricing pricing = (Pricing) input;
				return PricingView.create(pricing);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<AttributeValueView> getAttributeValues() {
		Set<AttributeValue> attributeValues = item.getAttributeValues();

		return (List<AttributeValueView>) CollectionUtils.collect(attributeValues, new Transformer() {

			public Object transform(Object input) {
				AttributeValue pricing = (AttributeValue) input;
				return new AttributeValueView(pricing);
			}
		});
	}

	/**
	 * Returns all attributes aasociate to product
	 * 
	 * @return
	 */
	public List<AttributeNameView> getAvailableAttributes() {
		List<AttributeNameView> attributes = new ArrayList<AttributeNameView>();

		Set<AttributeName> allAttributes = item.getProduct().getAttributes();
		for (AttributeName attribute : allAttributes) {
			attributes.add(new AttributeNameView(attribute));
		}

		return attributes;
	}
}
