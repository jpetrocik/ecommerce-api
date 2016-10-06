/*
 * ThumbNailProcessor.java
 *
 * Created on July 30, 2002, 8:13 AM
 */

package org.psoft.ecommerce.util;

import java.awt.Dimension;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageUtilities {
	private static Log log = LogFactory.getLog(ImageUtilities.class);

	/**
	 * Convience method to scale images from a byte array.
	 * 
	 * @param data
	 *            byte array containing the encoded data for an image. Data can
	 *            be encoded in any format supported by ImageIO.
	 * @param thumbWidth
	 *            Width of the image to reneder.
	 * @param thumbHeight
	 *            Height of the image to render.
	 * @param observer
	 * @throws IOException
	 * @return
	 */
	public static byte[] scaleImage(byte[] data, int width, int height) throws Throwable {
		//TODO Reimplement scaleImage
		throw new RuntimeException("Unimplemented");
//		try {
//			ImageInfo info = new ImageInfo();
//			MagickImage image = new MagickImage(info, data);
//			log.debug("Resizing image to " + width + "x" + height);
//			Dimension newDim = new Dimension(width, height);
//			Dimension resizeDim = mantainAspectRation(image.getDimension(), newDim);
//			MagickImage newImage = image.scaleImage((int) resizeDim.getWidth(), (int) resizeDim.getHeight());
//			byte[] newData = newImage.imageToBlob(info);
//
//			return newData;
//		} catch (Throwable e) {
//			log.debug("Error scaling image.", e);
//			throw e;
//		}
	}

	/**
	 * @param image
	 *            A image.
	 * @param thumbWidth
	 *            Width of the image to render.
	 * @param thumbHeight
	 *            Height of the image to render.
	 * @param observer
	 * @return
	 */
	private static Dimension mantainAspectRation(Dimension curSize, Dimension newSize) {

		log.debug("Resizing image to " + newSize.getWidth() + "x" + newSize.getHeight() + " from " + curSize.getWidth() + "x"
				+ curSize.getHeight());
		double newWidth = newSize.getWidth();
		double newHeight = newSize.getHeight();
		double curWidth = curSize.getWidth();
		double curHeight = curSize.getHeight();
		double newRatio = newWidth / newHeight;
		double curRatio = curWidth / curHeight;

		if (newRatio < curRatio) {
			newHeight = (int) (newWidth / curRatio);
		} else {
			newWidth = (int) (newHeight * curRatio);
		}

		log.debug("News dimesions " + newWidth + "x" + newHeight + " to maintain aspect ration.");
		return new Dimension((int) newWidth, (int) newHeight);
	}

}
