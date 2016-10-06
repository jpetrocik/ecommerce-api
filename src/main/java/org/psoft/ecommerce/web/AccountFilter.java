package org.psoft.ecommerce.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.service.AccountService;
import org.psoft.ecommerce.util.Constants;
import org.springframework.web.context.WebApplicationContext;

public class AccountFilter implements Filter {
	private static Log log = LogFactory.getLog(AccountFilter.class);

	FilterConfig config;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		try {

			HttpSession session = ((HttpServletRequest) request).getSession();
			Account account = (Account) session.getAttribute(Constants.ACCOUNT);

			// Reassociates the account with the session
			AccountService accountService = (AccountService) getWebApplicationContext().getBean("accountService");

//			// multiple requests for the same session causes error.
//			// typical triggered by multiple image requests
//			try {
//				account = accountService.merge(account);
//			} catch (Exception e) {
//				log.debug("Account is associated with more then one session");
//			}

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
