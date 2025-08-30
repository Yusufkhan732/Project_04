package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.ServletUtility;

/**
 * BaseCtl is an abstract servlet controller class that provides common
 * functionalities for all servlet controllers such as operation constants,
 * validation, preloading data, bean population, and request handling.
 * 
 * This class must be extended by other specific controller servlets.
 * 
 * @author Yusuf Khan
 */
@WebServlet(name = "BaseCtl", urlPatterns = { "/ctl/BaseCtl" })
public abstract class BaseCtl extends HttpServlet {

	/** Operation constant for Save */
	public static final String OP_SAVE = "Save";

	/** Operation constant for Update */
	public static final String OP_UPDATE = "Update";

	/** Operation constant for Cancel */
	public static final String OP_CANCEL = "Cancel";

	/** Operation constant for Delete */
	public static final String OP_DELETE = "Delete";

	/** Operation constant for List */
	public static final String OP_LIST = "List";

	/** Operation constant for Search */
	public static final String OP_SEARCH = "Search";

	/** Operation constant for View */
	public static final String OP_VIEW = "View";

	/** Operation constant for Next */
	public static final String OP_NEXT = "Next";

	/** Operation constant for Previous */
	public static final String OP_PREVIOUS = "Previous";

	/** Operation constant for New */
	public static final String OP_NEW = "New";

	/** Operation constant for Go */
	public static final String OP_GO = "Go";

	/** Operation constant for Back */
	public static final String OP_BACK = "Back";

	/** Operation constant for Reset */
	public static final String OP_RESET = "Reset";

	/** Operation constant for Logout */
	public static final String OP_LOG_OUT = "Logout";

	/** Message key for success messages */
	public static final String MSG_SUCCESS = "success";

	/** Message key for error messages */
	public static final String MSG_ERROR = "error";

	/**
	 * Validates the HTTP request parameters. This method should be overridden by
	 * subclasses to provide specific validation logic.
	 * 
	 * @param request the HttpServletRequest object
	 * @return true if validation passes, false otherwise
	 */
	protected boolean validate(HttpServletRequest request) {
		return true;
	}

	/**
	 * Preloads data required before processing the request. This method can be
	 * overridden by subclasses to preload lists or other data needed for UI.
	 * 
	 * @param request the HttpServletRequest object
	 */
	protected void preload(HttpServletRequest request) {
		System.out.println("ParentPreload");
	}

	/**
	 * Populates and returns a BaseBean object from request parameters. Should be
	 * overridden by subclasses to return specific bean types.
	 * 
	 * @param request the HttpServletRequest object
	 * @return a populated BaseBean object or null
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		return null;
	}

	/**
	 * Populates createdBy, modifiedBy, createdDatetime, and modifiedDatetime fields
	 * of the DTO (Data Transfer Object) from request and session data.
	 * 
	 * @param dto     the BaseBean object to populate
	 * @param request the HttpServletRequest object
	 * @return the populated BaseBean object
	 */
	protected BaseBean populateDTO(BaseBean dto, HttpServletRequest request) {

		String createdBy = request.getParameter("createdBy");
		String modifiedBy = null;

		UserBean userbean = (UserBean) request.getSession().getAttribute("user");

		if (userbean == null) {
			createdBy = "root";
			modifiedBy = "root";
		} else {
			modifiedBy = userbean.getLogin();

			if ("null".equalsIgnoreCase(createdBy) || DataValidator.isNull(createdBy)) {
				createdBy = modifiedBy;
			}
		}

		dto.setCreatedBy(createdBy);
		dto.setModifiedBy(modifiedBy);

		long cdt = DataUtility.getLong(request.getParameter("createdDatetime"));

		if (cdt > 0) {
			dto.setCreatedDatetime(DataUtility.getTimestamp(cdt));
		} else {
			dto.setCreatedDatetime(DataUtility.getCurrentTimestamp());
		}

		dto.setModifiedDatetime(DataUtility.getCurrentTimestamp());

		return dto;
	}

	/**
	 * Overrides the service method to include preloading data, validation of
	 * operation parameter, and forwarding in case of validation failure before
	 * calling the superclass service.
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("service method");

		preload(request);

		String op = DataUtility.getString(request.getParameter("operation"));
		System.out.println("operation");

		if (DataValidator.isNotNull(op) && !OP_CANCEL.equalsIgnoreCase(op) && !OP_VIEW.equalsIgnoreCase(op)
				&& !OP_DELETE.equalsIgnoreCase(op) && !OP_RESET.equalsIgnoreCase(op)) {

			if (!validate(request)) {

				BaseBean bean = populateBean(request);
				ServletUtility.setBean(bean, request);
				ServletUtility.forward(getView(), request, response);
				return;

			}
		}
		System.out.println("SuperService");
		super.service(request, response);

	}
	
	/**
	 * Returns the view (JSP page) for the controller. Must be implemented by
	 * subclasses.
	 * 
	 * @return the view page as a String
	 */
	protected abstract String getView();
}