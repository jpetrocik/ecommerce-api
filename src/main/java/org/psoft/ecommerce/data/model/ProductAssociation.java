package org.psoft.ecommerce.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class ProductAssociation {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable=false)
	private String type;

	@ManyToOne
	@JoinColumn(nullable=false)
	private Product product;

}
