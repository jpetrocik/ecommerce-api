package org.psoft.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Image;
import org.psoft.ecommerce.data.model.ImageGallery;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Product;

public class ImageService extends AbstractService<Image> {
	private static final Log log = LogFactory.getLog(ImageService.class);

	public void createNewImage(Long sourceId, String group, String alt, String description, String filename, String mimeType,
			byte[] raw) {

		Session session = currentSession();

		session.setFlushMode(FlushMode.ALWAYS);

		Image imageData = new Image();

		imageData.setActive(true);
		imageData.setAlt(alt);
		imageData.setDescription(description);
		imageData.setFilename(filename);
		imageData.setMimeType(mimeType);
		imageData.setGroup(group);

		
		ImageGallery gallery = findSource(sourceId);
		Validate.notNull(gallery);

		session.saveOrUpdate(imageData);
		session.flush();

		gallery.addImage(imageData);

		
		//save image to file system
		
		session.saveOrUpdate(gallery);
		session.flush();

	}

	public void saveImage(Long sourceId, Long id, String description, int sequence) {
		Session session = currentSession();
		session.setFlushMode(FlushMode.ALWAYS);

		ImageGallery imageGallery = findSource(sourceId);

		Image image = byId(id);
		image.setDescription(description);

		imageGallery.removeImage(image);

		imageGallery.addImage(image, sequence);

		session.saveOrUpdate(imageGallery);
		session.flush();
	}

	public Image byId(Long id) {

		Session session = currentSession();

		return (Image) session.get(Image.class, id);
	}

	/**
	 * Find all images for a product, category, or item
	 * 
	 * @param id
	 * @return
	 */
	public List<Image> retrieveImages(Long id) {
		ImageGallery gallery = findSource(id);

		if (gallery == null) {
			return new ArrayList<Image>();
		}

		List<Image> results = gallery.getImages();

		return results;
	}

	public void removeImage(Long sourceId, Long id) {
		Session session = currentSession();

		session.setFlushMode(FlushMode.ALWAYS);

		ImageGallery gallery = findSource(sourceId);
		Image image = (Image) session.get(Image.class, id);

		gallery.removeImage(image);

		session.saveOrUpdate(gallery);
		session.flush();
	}

	protected ImageGallery findSource(Long id) {

		Session session = currentSession();

		ImageGallery gallery = (ImageGallery) session.get(Product.class, id);
		if (gallery == null)
			gallery = (ImageGallery) session.get(Category.class, id);
		if (gallery == null)
			gallery = (ImageGallery) session.get(Item.class, id);

		return gallery;

	}
}
