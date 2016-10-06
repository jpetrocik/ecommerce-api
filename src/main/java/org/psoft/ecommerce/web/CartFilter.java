package org.psoft.ecommerce.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.service.CartService;
import org.psoft.ecommerce.util.Constants;
import org.springframework.web.context.WebApplicationContext;

public class CartFilter implements Filter {
	private static Log log = LogFactory.getLog(AccountFilter.class);

	FilterConfig config;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		try {

			// create new shopping cart
			CartService cart = (CartService) getWebApplicationContext().getBean(Constants.CART_PROTOTYPE);

			// multiple requests for the same session causes error.
			// typical triggered by multiple image requests
			try {
				// reassociate the cart with the hibernate session
				cart.merge();
			} catch (Exception e) {
				log.debug("Account is associated with more then one session");
			}

			// place in request for later use
			request.setAttribute(Constants.CART, cart);

		} finally {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
	}

	private WebApplicationContext getWebApplicationContext() {
		WebApplicationContext webApplicationContext = (WebApplicationContext) config.getServletContext().getAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		return webApplicationContext;
	}
}
