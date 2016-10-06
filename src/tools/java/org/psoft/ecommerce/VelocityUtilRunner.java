package org.psoft.ecommerce;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.psoft.ecommerce.util.VelocityUtil;


public class VelocityUtilRunner extends TestCase {

	public void testParse() throws Exception{
		
		String template = "/generic.vm";
		Map data = new HashMap() {{ put("name","John Petrocik"); put("address","2671 Tulae"); }};
		
		String body = VelocityUtil.parse(template, data);
		
		System.out.println(body);
	}
}
