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
public class AttributeValue {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable=false)
	String value;

	@ManyToOne
	@JoinColumn(nullable=false)
	AttributeName attributeName;

}
