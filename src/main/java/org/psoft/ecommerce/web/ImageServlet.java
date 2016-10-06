package org.psoft.ecommerce.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.psoft.ecommerce.util.CollectionUtils;
import org.psoft.ecommerce.util.ImageUtilities;
import org.springframework.web.context.WebApplicationContext;

import com.sun.scenario.effect.ImageData;

@SuppressWarnings("serial")
public class ImageServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(ImageServlet.class);

	SessionFactory sessionFactory;

	@Override
	public void init() throws ServletException {
		super.init();

		WebApplicationContext applicationContext = (WebApplicationContext) getServletContext().getAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Session session = sessionFactory.getCurrentSession();

		Integer width = null;
		String x = req.getParameter("x");
		if (NumberUtils.isDigits(x)) {
			width = new Integer(x);
		}

		Integer height = null;
		String y = req.getParameter("y");
		if (NumberUtils.isDigits(y)) {
			height = new Integer(y);

		}

		ImageData image = null;

		// check for direct image request
		String id = req.getParameter("id");
		if (NumberUtils.isDigits(id)) {
			image = (ImageData) session.get(ImageData.class, new Long(id));

			// check for catalog entry request
		} else {

			String foreignId = req.getParameter("itemId");
			if (StringUtils.isBlank(foreignId)) {
				foreignId = req.getParameter("productId");
			}
			if (StringUtils.isBlank(foreignId)) {
				foreignId = req.getParameter("categoryId");
			}

			if (NumberUtils.isDigits(foreignId)) {
				Query query = session.createQuery("from ImageData where foreign_id = :foreignId");
				query.setLong("foreignId", new Long(foreignId));
				image = (ImageData) CollectionUtils.firstElement(query.list());
			}
		}

//		if (image != null && image.getRaw() != null) {
//			sendImage(resp, image, width, height);
//
//		} else {
			sendMissing(resp, width, height);
			return;
//		}

	}

//	protected void sendImage(HttpServletResponse resp, ImageData image, Integer width, Integer height) throws IOException {
//		ServletOutputStream oStream = resp.getOutputStream();
//
//		resp.setContentType(image.getMimeType());
//		resp.setHeader("Content-Disposition", "inline; filename=" + image.getFilename());
//
//		byte[] raw = image.getRaw();
//		if (width != null && height != null) {
//			try {
//				raw = ImageUtilities.scaleImage(raw, width, height);
//			} catch (Throwable e) {
//				log.error("Failed scaling image, check imagick is installed");
//			}
//		}
//		oStream.write(raw);
//
//		oStream.flush();
//
//	}

	protected void sendMissing(HttpServletResponse resp, Integer width, Integer height) throws IOException {
		// resp.sendError(404);

		resp.setContentType("image/gif");
		resp.setHeader("Content-Disposition", "inline; filename=missing.gif");

		ServletOutputStream oStream = resp.getOutputStream();
		InputStream inStream = ImageServlet.class.getResourceAsStream("/missing.gif");

		byte[] raw = IOUtils.toByteArray(inStream);
		if (width != null && height != null) {
			try {
				raw = ImageUtilities.scaleImage(raw, width, height);
			} catch (Throwable e) {
				log.error("Failed scaling image, check imagick is installed");
			}
		}
		oStream.write(raw);

		oStream.flush();

	}

}
