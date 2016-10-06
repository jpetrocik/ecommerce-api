package org.psoft.ecommerce.data.model;

/**
 * @hibernate.class table="email_template"
 * 
 * @author jpetrocik
 * 
 */
public class EmailTemplate {

	private Long id;

	private String name;

	private String subject;

	private String mailFrom;

	private String plainTemplate;

	/**
	 * @hibernate.id column="id" generator-class="native"
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getPlainTemplate() {
		return plainTemplate;
	}

	public void setPlainTemplate(String plainTemplate) {
		this.plainTemplate = plainTemplate;
	}

}
