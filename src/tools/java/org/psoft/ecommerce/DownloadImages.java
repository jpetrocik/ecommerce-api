package org.psoft.ecommerce;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.psoft.ecommerce.data.ImageData;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;


@HibernateSessionFactory("hibernate.cfg.xml")
public class DownloadImages extends UnitilsJUnit4 {  
	private static Log log = LogFactory.getLog(DownloadImages.class);
	
	@TestDataSource
	DataSource dataSource;
	
	@HibernateSessionFactory
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Test
	public void download() throws Exception{
	
		Session session = sessionFactory.getCurrentSession();

		List<ImageData> results = session.createCriteria(ImageData.class).add(Restrictions.isNull("raw")).list();
		
		int i = 0;
		for (ImageData data : results){
			
			try {
				String filename = data.getFilename();

				String spec = "http://www.oreoriginals.com/gallery/"+filename.replace(" ", "%20");
				URL url = new URL(spec);
				
				log.info(url.toString());
				
				InputStream inStream = url.openStream();
				
				byte[] raw = IOUtils.toByteArray(inStream);
				
				data.setRaw(raw);

				session.saveOrUpdate(data);
				
				i++;
				
				if (i%10==0)
					session.flush();
					
			} catch (Exception e) {
				log.error("Download error", e);
			}
			
		}
		
		session.flush();
		
	}

	@Test
	public void downloadThumbnails() throws Exception{
		
		Session session = sessionFactory.getCurrentSession();

		List<ImageData> results = session.createCriteria(ImageData.class).add(Restrictions.isNull("raw")).list();
		
		int i = 0;
		for (ImageData data : results){
			
			try {
				String filename = data.getFilename();

				String spec = "http://www.oreoriginals.com/gallery/thumbnails/"+filename.replace(" ", "%20");
				URL url = new URL(spec);
				
				log.info(url.toString());
				
				InputStream inStream = url.openStream();
				
				byte[] raw = IOUtils.toByteArray(inStream);
				
				data.setRaw(raw);

				session.saveOrUpdate(data);
				
				i++;
				
				if (i%10==0)
					session.flush();
					
			} catch (Exception e) {
				log.error("Download error", e);
			}
			
		}
		
		session.flush();
		
	}

}
