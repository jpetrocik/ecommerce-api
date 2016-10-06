package org.psoft.ecommerce.data.model;

import java.util.List;

public interface ImageGallery {

	public List<Image> getImages();

	public void addImage(Image image);

	public void addImage(Image image, int sequence);

	public void removeImage(Image image);
}
