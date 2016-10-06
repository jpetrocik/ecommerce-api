package org.psoft.ecommerce.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

import lombok.Data;

/**
 * Item represents the orderable item.  Most time customers browser products, like "Very Cool Shirt" and items are the variations when adding to the cart, like Large Green Very Cool Short.
 * 
 */
@Entity
@Data
public class Item implements ImageGallery {

	@Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = "masterSeq" )
	@TableGenerator( name = "masterSeq", table = "catalog_seq", pkColumnValue="catalog", pkColumnName = "sequence", initialValue=100, allocationSize=1)
	Long id;

	@Column(nullable=false)
	Boolean active = Boolean.TRUE;

	String name;

	String shortDescr;

	String longDescr;

	@Column(nullable=false)
	String partNum;

	String upc;

	String keywords;

	@ManyToOne
	@JoinColumn(nullable=false)
	Product product;

	@OneToMany(orphanRemoval=true)
	Set<Pricing> pricing = new HashSet<Pricing>();

	@OneToMany(orphanRemoval=true)
	@JoinColumn(name="item_id")
	Set<AttributeValue> attributeValues = new HashSet<AttributeValue>();

	@OneToMany(orphanRemoval=true)
	@JoinColumn(name="foreign_id")
	@OrderColumn(name="sequence", nullable=false)
	List<Image> images = new ArrayList<Image>();

	public Set<Pricing> getPricing() {
		return Collections.unmodifiableSet(pricing);
	}

	public void addPricing(Pricing price) {
		this.pricing.add(price);
	}

	public Set<Pricing> getActivePricing() {

		Set<Pricing> list = new HashSet<Pricing>();
		for (Pricing price : pricing) {
			if (price.isActive())
				list.add(price);
		}
		return Collections.unmodifiableSet(list);
	}

	public Set<AttributeValue> getAttributeValues() {
		return Collections.unmodifiableSet(attributeValues);
	}

	public void addAttributeValue(AttributeValue attributeValue) {
		attributeValues.add(attributeValue);
	}

	public void removeAttributeValue(AttributeValue attributeValue) {
		attributeValues.remove(attributeValue);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Item) {
			Item item = (Item) obj;

			return getPartNum().equals(item.getPartNum());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return partNum.hashCode();
	}

	public List<Image> getImages() {
		return Collections.unmodifiableList(images);
	}

	public void addImage(Image image) {
		images.add(image);

	}

	public void addImage(Image image, int sequence) {
		images.add(sequence, image);
	}

	public void removeImage(Image image) {
		images.remove(image);
	}

	public boolean isActive() {
		if (active == null || !active)
			return false;
		return true;
	}
	
}
