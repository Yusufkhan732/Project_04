package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.MarksheetBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.MarksheetModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * GetMarksheetCtl Controller: Handles request to fetch a student's marksheet
 * using their roll number.
 * 
 * @author Yusuf khan
 */
@WebServlet(name = "GetMarksheetCtl", urlPatterns = { "/ctl/GetMarksheetCtl" })
public class GetMarksheetCtl extends BaseCtl {

	/**
	 * Validates the input field (Roll Number).
	 *
	 * @param request HttpServletRequest containing user input
	 * @return true if validation passes, otherwise false
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll Number"));
			pass = false;
		}

		return pass;
	}

	/**
	 * Populates the MarksheetBean with request data.
	 *
	 * @param request HttpServletRequest
	 * @return populated MarksheetBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		MarksheetBean bean = new MarksheetBean();

		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

		return bean;
	}

	/**
	 * Handles HTTP GET method. Forwards to the Get Marksheet View.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles HTTP POST method. Processes the 'GO' operation to retrieve the
	 * marksheet using roll number.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		MarksheetModel model = new MarksheetModel();

		MarksheetBean bean = (MarksheetBean) populateBean(request);

		if (OP_GO.equalsIgnoreCase(op)) {
			try {
				bean = model.findByRoll(bean.getRollNo());
				if (bean != null) {

					ServletUtility.setBean(bean, request);
				} else {

					ServletUtility.setErrorMessage("RollNo Does Not exists", request);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view page for Get Marksheet.
	 *
	 * @return the view path as a String
	 */
	@Override
	protected String getView() {
		return ORSView.GET_MARKSHEET_VIEW;
	}
}
