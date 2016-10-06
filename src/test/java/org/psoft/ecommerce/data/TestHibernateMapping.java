package org.psoft.ecommerce.data;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.orm.hibernate.HibernateUnitils;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@HibernateSessionFactory("hibernate.cfg.xml")
public class TestHibernateMapping extends UnitilsJUnit4 {

	@Test
	public void testMappingToDatabase() {
		HibernateUnitils.assertMappingWithDatabaseConsistent();
	}

}
