package org.psoft.ecommerce.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

import org.apache.commons.lang.StringUtils;

import lombok.Data;

@Entity
@Data
public class Product implements ImageGallery {

	@Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = "masterSeq" )
	@TableGenerator( name = "masterSeq", table = "catalog_seq", pkColumnValue="catalog", pkColumnName = "sequence", initialValue=100, allocationSize=1)
	private Long id;

	@Column(nullable=false)
	private Boolean active = Boolean.TRUE;

	@Column(nullable=false)
	private String name;

	private String shortDescr;

	private String longDescr;

	private String keywords;

	private String productCode;

	//TODO Implement product class to taxable, shippable, etc
	private String productClass;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="product_id")
	@OrderColumn(name="sequence", nullable=false)
	private List<Item> items = new ArrayList<Item>();

	@ManyToMany(mappedBy = "products")
	private Set<Category> categories = new HashSet<Category>();

	@OneToMany(orphanRemoval=true)
	@JoinColumn(name="foreign_id")
	@OrderColumn(name="sequence", nullable=false)
	private List<Image> images = new ArrayList<Image>();

	@OneToMany(orphanRemoval=true, cascade = CascadeType.ALL)
	@JoinColumn(name="product_id")
	private Set<AttributeName> attributes = new HashSet<AttributeName>();

	@OneToMany(orphanRemoval=true)
	@JoinColumn(name="from_product_id")
	private Set<ProductAssociation> associatedProducts = new HashSet<ProductAssociation>();
	
	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	public boolean addItem(Item item) {

		//check for duplicate association
		if (items.contains(item))
			return false;

		//set reverse association
		item.setProduct(this);
		
		//create association
		return items.add(item);
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public Set<Category> getCategories() {
		return Collections.unmodifiableSet(categories);
	}

	/**
	 * Use Category.addProduct()
	 */
	protected void addCategory(Category category) {

		if (this.categories.contains(category)) {
			return;
		}

		this.categories.add(category);
	}

	/**
	 * Use Category.removeProduct()
	 */
	protected void removeCategory(Category category) {

		if (!this.categories.contains(category)) {
			return;
		}

		this.categories.remove(category);
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

	public Set<AttributeName> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}

	public void addAttributeName(AttributeName attributeName) {
		attributes.add(attributeName);
	}

	public void removeAttributeName(AttributeName attributeName) {
		attributes.remove(attributeName);
	}

	public Set<ProductAssociation> getAssociatedProducts() {
		return Collections.unmodifiableSet(associatedProducts);
	}

	public ProductAssociation addAssociatedProduct(Product product, String type) {
		ProductAssociation productAssociation = new ProductAssociation();
		productAssociation.setProduct(product);
		productAssociation.setType(type);

		associatedProducts.add(productAssociation);

		return productAssociation;
	}

	public void removeAssociatedProduct(Product product, String type) {
		Iterator<ProductAssociation> iter = associatedProducts.iterator();

		while (iter.hasNext()) {
			ProductAssociation p = iter.next();

			if (p.getProduct().equals(product) && p.getType().equals(type)) {
				iter.remove();
			}
		}

	}

	/**
	 * Compares code for equality.  Any two products
	 * with the same code are consider equal.  If either
	 * code is NULL, returns false.
	 *   
	 */
	@Override
	public boolean equals(Object obj) {
		Product rh = (Product) obj;

		if (StringUtils.isNotBlank(rh.getProductCode()) &&
			StringUtils.isNotBlank(getProductCode())){
				return getProductCode().equals(rh.getProductCode());
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		if (StringUtils.isBlank(getProductCode()))
			return super.hashCode();
		
		return getProductCode().hashCode();
	}

	public boolean isActive() {
		if (active == null || !active)
			return false;
		return true;
	}
}
