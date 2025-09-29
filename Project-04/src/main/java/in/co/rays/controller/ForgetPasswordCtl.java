package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * ForgetPasswordCtl Controller to handle forget password functionality.
 * 
 * @author Yusuf khan
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/ForgetPasswordCtl" })
public class ForgetPasswordCtl extends BaseCtl {

	/** Logger instance */
	private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);

	/**
	 * Validates the input data from the forget password form.
	 *
	 * @param request the HttpServletRequest
	 * @return true if data is valid, otherwise false
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("ForgetPasswordCtl validate started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Email Id"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
			pass = false;
		}

		log.debug("ForgetPasswordCtl validate ended with result = " + pass);
		return pass;
	}

	/**
	 * Populates the UserBean with request parameters.
	 *
	 * @param request the HttpServletRequest
	 * @return a populated UserBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("ForgetPasswordCtl populateBean started");

		UserBean bean = new UserBean();

		bean.setLogin(DataUtility.getString(request.getParameter("login")));

		log.debug("ForgetPasswordCtl populateBean ended with login = " + bean.getLogin());
		return bean;
	}

	/**
	 * Displays the forget password view (HTTP GET).
	 *
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("ForgetPasswordCtl doGet started");
		ServletUtility.forward(getView(), request, response);
		log.debug("ForgetPasswordCtl doGet ended");
	}

	/**
	 * Handles forget password operation (HTTP POST).
	 *
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("ForgetPasswordCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));

		UserModel model = new UserModel();

		if (OP_GO.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			try {

				log.debug("ForgetPasswordCtl doPost operation = GO, login = " + bean.getLogin());
				boolean flag = model.forgetPassword(bean.getLogin());
				if (flag) {
					ServletUtility.setSuccessMessage("Password has been sent to your email id", request);
				}
			} catch (RecordNotFoundException e) {
				log.error("Record not found exception", e);
				ServletUtility.setErrorMessage(e.getMessage(), request);
			} catch (ApplicationException e) {
				log.error("Application exception", e);
				ServletUtility.setErrorMessage("Please check your internet connection..!!", request);
			}
			ServletUtility.forward(getView(), request, response);
		}

		log.debug("ForgetPasswordCtl doPost ended");
	}

	/**
	 * Returns the view path of the forget password page.
	 *
	 * @return view page path
	 */
	@Override
	protected String getView() {
		log.debug("ForgetPasswordCtl getView called");
		return ORSView.FORGET_PASSWORD_VIEW;
	}
}
