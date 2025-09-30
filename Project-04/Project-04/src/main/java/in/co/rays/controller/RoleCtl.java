package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.RoleModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * RoleCtl is a controller servlet class that handles HTTP requests for Role
 * operations such as add, update, and view roles in the application.
 * 
 * It performs validation of input data and interacts with RoleModel for
 * database operations.
 * 
 * Author: Yusuf Khan Date: 05-08-2025
 */
@WebServlet(name = "RoleCtl", urlPatterns = { "/ctl/RoleCtl" })
public class RoleCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(RoleCtl.class);

	/**
	 * Validates Role input fields.
	 * 
	 * @param request HttpServletRequest
	 * @return boolean true if input is valid
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("RoleCtl validate method start");

		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			isValid = false;
			log.error("Validation failed: Name is required");

		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Invalid name");
			isValid = false;
			log.error("Validation failed: Invalid Name format");
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			isValid = false;
			log.error("Validation failed: Description is required");

		}
		log.debug("RoleCtl validate method end");
		return isValid;
	}

	/**
	 * Populates RoleBean from request parameters.
	 * 
	 * @param request HttpServletRequest
	 * @return populated RoleBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("RoleCtl populateBean method start");

		RoleBean bean = new RoleBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);
		log.debug("RoleCtl populateBean method end");
		return bean;
	}

	/**
	 * Handles GET request for Role page. If an ID is provided, fetches the role
	 * record and populates the form.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("RoleCtl doGet method start");

		RoleModel model = new RoleModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {

			RoleBean bean;
			try {
				bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (Exception e) {
				log.error("Exception in RoleCtl doGet", e);
				e.printStackTrace();
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("RoleCtl doGet method end");
	}

	/**
	 * Handles POST requests for Save, Update, Reset, and Cancel operations.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("RoleCtl doPost method start");

		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));
		RoleModel model = new RoleModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			RoleBean bean = (RoleBean) populateBean(request);

			try {
				long pk = model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Data Successfully saved", request);
				log.info("Role added successfully with ID: " + pk);

			} catch (ApplicationException e) {
				log.error("ApplicationException in RoleCtl doPost Save", e);
				ServletUtility.handleException(e, request, response);
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Role already exists", request);
				log.error("Duplicate Role found while adding");
				return;
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			RoleBean bean = (RoleBean) populateBean(request);

			try {
				if (id > 0) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data is Successfully Updated", request);
					log.info("Role updated successfully with ID: " + bean.getId());
				}
			} catch (ApplicationException e) {
				log.error("ApplicationException in RoleCtl doPost Update", e);
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Role already exists", request);
				log.error("Duplicate Role found while updating");
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
			log.debug("Operation Cancel - Redirecting to Role List");
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
			log.debug("Operation Reset - Redirecting to RoleCtl");
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("RoleCtl doPost method end");
	}

	/**
	 * Returns the view for the role form.
	 * 
	 * @return ROLE_VIEW path
	 */
	@Override
	protected String getView() {
		return ORSView.ROLE_VIEW;
	}

}