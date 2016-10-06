package org.psoft.ecommerce.rest.view;

import org.psoft.ecommerce.data.model.Image;

public class ImageView {

	private Image image;

	public ImageView(Image image) {
		this.image = image;
	}

	public String getAlt() {
		return image.getAlt();
	}

	public String getDescription() {
		return image.getDescription();
	}

	public String getFilename() {
		return image.getFilename();
	}

	public Long getId() {
		return image.getId();
	}

	public String getMimeType() {
		return image.getMimeType();
	}

	public int getSequence() {
		return image.getSequence();
	}

	public boolean isActive() {
		return image.isActive();
	}

	public String getGroup() {
		return image.getGroup();
	}

}
