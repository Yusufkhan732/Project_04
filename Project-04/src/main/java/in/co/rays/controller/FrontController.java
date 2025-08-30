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

import in.co.rays.util.ServletUtility;

/**
 * FrontController acts as a filter to perform authentication check for all
 * incoming requests to the application for secured resources under "/ctl/*" and "/doc/*".
 * 
 * If a user is not logged in, it redirects them to the login page with an error message.
 * 
 * @author Yusuf khan
 */
@WebFilter(urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {


    /**
     * This method performs filtering of all matching requests.
     * If the user session does not exist, it redirects to the login page.
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
		//System.out.println("Do filter Method");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		request.setAttribute("uri", uri);

		if (session.getAttribute("user") == null) {
			request.setAttribute("error", "Your session has been expired. Please Login again!");

			ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
			return;
		} else {
			
			chain.doFilter(request, response);
		}
	}


    /**
     * Initialization method for the filter.
     *
     * @param filterConfig the filter configuration
     * @throws ServletException
     */
		@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	 /**
     * Destroy method for the filter.
     */
	@Override
	public void destroy() {

	}
}
