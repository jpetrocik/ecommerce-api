package org.psoft.ecommerce.rest;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.psoft.ecommerce.data.model.Image;
import org.psoft.ecommerce.rest.view.ImageView;
import org.psoft.ecommerce.service.ImageService;

public class ImageRpcService {

	ImageService imageService;

	ImageViewTransformer imageViewTransformer = new ImageViewTransformer();

	public void createNewImage(Long sourceId, String group, String alt, String description, String filename, String mimeType,
			byte[] raw) {
		imageService.createNewImage(sourceId, group, alt, description, filename, mimeType, raw);
	}

	public void saveImage(Long sourceId, Long id, String description, int sequence) {
		imageService.saveImage(sourceId, id, description, sequence);
	}

	@SuppressWarnings("unchecked")
	public List<ImageView> retrieveImages(Long id) {
		List<Image> results = imageService.retrieveImages(id);

		return (List<ImageView>) CollectionUtils.collect(results, imageViewTransformer);
	}

	public void removeImage(Long sourceId, Long id) {
		imageService.removeImage(sourceId, id);
	}

	public ImageService getImageService() {
		return imageService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public class ImageViewTransformer implements Transformer {

		public Object transform(Object object) {

			Image image = (Image) object;
			ImageView categoryView = new ImageView(image);

			return categoryView;
		}

	}
}
