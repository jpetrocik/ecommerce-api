package org.psoft.ecommerce.util;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;

public class VelocityUtil {
	private static Log logger = LogFactory.getLog(VelocityUtil.class);

	public static String parse(String template, Object data) {

		try {
			// Build the context.
			VelocityContext context = new VelocityContext();

			if (data instanceof Map) {
				Map<String, Object> map = (Map) data;
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					context.put(entry.getKey(), entry.getValue());
				}
			} else if (data instanceof List) {
				context.put("list", data);
			} else if (data != null) {
				String className = data.getClass().getName().toLowerCase();
				String key = className.substring(className.lastIndexOf(".") + 1);
				context.put(key, data);
			}

			// TODO replace with xmltoolbox loader
			// init velocity tools
			context.put("number", new NumberTool());
			context.put("dateTool", new DateTool());
			context.put("stringTool", new StringTool());

			// Load the template
			Reader reader = new StringReader(template);

			// Generate the email
			Writer writer = new StringWriter();
			Velocity.init();
			Velocity.evaluate(context, writer, "email", reader);

			String content = writer.toString();
			Pattern UNIX = Pattern.compile("([^\\r])(\\n)", Pattern.MULTILINE);
			return UNIX.matcher(content).replaceAll("$1\r\n");

		} catch (ParseErrorException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Velocity parse error on " + template, e);
		} catch (MethodInvocationException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Method invocation failure " + template, e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Exception in parsing template " + template, e);
		}

	}
}
