package org.psoft.ecommerce.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class AttributeName {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable=false)
	String name;

	@Column(nullable=false)
	String classType;

}
