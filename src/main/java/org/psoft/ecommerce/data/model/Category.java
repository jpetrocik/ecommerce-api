package org.psoft.ecommerce.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import lombok.Data;

/**
 * Category is a way to organize products.  You can have many different top level categories which allow you navigate products
 * in many different ways.   
 * 
 */
@Entity
@Data
public class Category implements ImageGallery {
	private static Log log = LogFactory.getLog(Category.class);

	@Id
    @GeneratedValue( strategy = GenerationType.TABLE, generator = "masterSeq" )
	@TableGenerator( name = "masterSeq", table = "catalog_seq", pkColumnValue="catalog", pkColumnName = "sequence", initialValue=100, allocationSize=1)
	private Long id;

	private Boolean active = Boolean.TRUE;

	@Column(nullable=false)
	private String name;

	private String shortDescr;

	private String longDescr;

	private String keywords;

	@ManyToMany(mappedBy = "subcategories")
	private Set<Category> parents = new HashSet<Category>();
	
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name="category_to_category", 
			joinColumns={@JoinColumn(name="from_category_id")}, 
			inverseJoinColumns={@JoinColumn(name="to_category_id")})
	@OrderColumn(name="sequence", nullable=false)
	private List<Category> subcategories = new ArrayList<Category>();

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name="category_to_product", 
			joinColumns={@JoinColumn(name="category_id")}, 
			inverseJoinColumns={@JoinColumn(name="product_id")})
	@OrderColumn(name="sequence", nullable=false)
	private List<Product> products = new ArrayList<Product>();

	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(foreignKey=@ForeignKey(name="foreign_id"))
	@OrderColumn(name="sequence", nullable=false)
	private List<Image> images = new ArrayList<Image>();

	public List<Category> getSubcategories() {
		return Collections.unmodifiableList(subcategories);
	}

	public boolean addSubcategory(Category category) {
		return addSubcategory(category, this.subcategories.size());
	}

	public boolean addSubcategory(Category category, int index) {

		//check for duplicate association
		if (this.subcategories.contains(category)) {
			log.warn(category + " is already assigned to " + this);
			return false;
		}

		//set reverse association
		category.addParent(this);
		
		//create association
		this.subcategories.add(index, category);

		return true;

	}

	public void removeSubcategory(Category category) {

		category.removeParent(this);

		if (!this.subcategories.contains(category)) {
			log.warn(category + " is not assigned to " + this);
			return;
		}

		this.subcategories.remove(category);
	}

	/**
	 * Returns an unmodifiable list of active subcategories.
	 * 
	 */
	public List<Category> getActiveSubcategories() {
		List<Category> active = new ArrayList<Category>();

		for (Category category : subcategories) {
			if (category.isActive())
				active.add(category);
		}

		return Collections.unmodifiableList(active);
	}

	public Set<Category> getParents() {
		return Collections.unmodifiableSet(parents);
	}

	/**
	 * Use Category.addSubcategory().
	 */
	protected void addParent(Category parent) {

		if (parents.contains(parent))
			return;

		parents.add(parent);
	}

	/**
	 * Use Category.addSubcategory().
	 */
	protected void removeParent(Category parent) {

		if (!parents.contains(parent))
			return;

		parents.remove(parent);

	}


	public List<Product> getAllProducts(boolean includeInactice) {
		List<Product> allProducts = new ArrayList<Product>();
		
		List<Category>subCategories = null;
		
		if(includeInactice)
			subCategories = getSubcategories();
		else
			subCategories = getActiveSubcategories();
		
		for (Category category : subCategories){
			allProducts.addAll(category.getAllProducts(includeInactice));
		}
		
		if(includeInactice)
			allProducts.addAll(getProducts());
		else
			allProducts.addAll(getActiveProducts());
		
		//removes any dups
		CollectionUtils.filter(allProducts, PredicateUtils.uniquePredicate());
		
		return allProducts;
	}
	
	public List<Product> getProducts() {
		return Collections.unmodifiableList(products);
	}

	public List<Product> getActiveProducts() {
		List<Product> active = new ArrayList<Product>();

		for (Product product : products) {
			if (product.isActive())
				active.add(product);
		}

		return Collections.unmodifiableList(active);
	}

	public boolean addProduct(Product product) {
		return addProduct(product, 0);
	}

	public boolean addProduct(Product product, int index) {

		//check for duplicate association
		if (this.products.contains(product)) {
			log.warn(product + " is already assigned to " + this);
			return false;
		}

		//set reverse association
		product.addCategory(this);
		
		//create association
		products.add(index, product);

		return true;
	}

	public void removeProduct(Product product) {
		products.remove(product);
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
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}
