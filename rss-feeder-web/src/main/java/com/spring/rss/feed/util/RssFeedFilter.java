package com.spring.rss.feed.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RssFeedFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		addResponseHeaders(req, res);
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * Adds the response headers.
	 *
	 * @param res
	 *            the res
	 */
	private void addResponseHeaders(HttpServletRequest req, HttpServletResponse res) {
		res.addHeader("X-UA-Compatible", "IE=edge");
		res.addHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
		res.addHeader("Pragma", "no-cache");
		res.addHeader("X-XSS-Protection", "1; mode=block;");
		res.addHeader("X-Content-Type-Options", "nosniff");
		res.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
		res.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		res.setHeader("Access-Control-Allow-Origin", "*");
	}

}
