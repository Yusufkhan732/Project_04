package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.util.ServletUtility;

/**
 * FrontController acts as a filter to perform authentication check for all
 * incoming requests to the application for secured resources under "/ctl/*" and
 * "/doc/*".
 * 
 * If a user is not logged in, it redirects them to the login page with an error
 * message.
 * 
 * @author Yusuf khan
 */
@WebFilter(urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {

	/** The logger instance for logging. */
	private static Logger log = Logger.getLogger(FrontController.class);

	/**
	 * This method performs filtering of all matching requests. If the user session
	 * does not exist, it redirects to the login page.
	 *
	 * @param req   the ServletRequest
	 * @param resp  the ServletResponse
	 * @param chain the FilterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		log.debug("FrontController doFilter started");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		request.setAttribute("uri", uri);

		if (session.getAttribute("user") == null) {
			log.warn("Unauthorized access attempt on URI: " + uri);
			request.setAttribute("error", "Your session has been expired. Please Login again!");
			ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
			return;
		} else {
			log.debug("User session found, proceeding with chain for URI: " + uri);
			
			chain.doFilter(request, response);
		}

		log.debug("FrontController doFilter ended");
	}

	/**
	 * Initialization method for the filter.
	 *
	 * @param filterConfig the filter configuration
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("FrontController filter initialized");
	}

	/**
	 * Destroy method for the filter.
	 */
	@Override
	public void destroy() {
		log.debug("FrontController filter destroyed");
	}
}
