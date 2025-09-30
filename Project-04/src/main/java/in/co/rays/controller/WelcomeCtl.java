package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.util.ServletUtility;

/**
 * WelcomeCtl servlet acts as a controller to forward users to the welcome view
 * of the application.
 * 
 * @author Yusuf Khan
 */
@WebServlet(name = "WelcomeCtl", urlPatterns = { "/WelcomeCtl" })
public class WelcomeCtl extends BaseCtl {

	/** Logger instance for logging */
	private static Logger log = Logger.getLogger(WelcomeCtl.class);

	/**
	 * Handles the HTTP GET request and forwards it to the welcome view.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("WelcomeCtl doGet started");

		ServletUtility.forward(getView(), request, response);

		log.debug("WelcomeCtl doGet ended");
	}

	/**
	 * Returns the view associated with this controller.
	 *
	 * @return a String containing the path to the welcome view
	 */
	@Override
	protected String getView() {

		log.debug("WelcomeCtl getView called");
		return ORSView.WELCOME_VIEW;
	}
}
