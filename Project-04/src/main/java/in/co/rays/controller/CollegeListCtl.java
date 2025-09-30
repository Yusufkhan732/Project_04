package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.CollegeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * CollegeListCtl class handles list, search, delete, pagination, and navigation
 * operations for College entities. It extends BaseCtl and interacts with
 * CollegeModel for database operations.
 * 
 * @author Yusuf khan
 */
@WebServlet(name = "CollegeListCtl", urlPatterns = { "/ctl/CollegeListCtl" })
public class CollegeListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CollegeListCtl.class);

	/**
	 * Loads the list of colleges from the model and sets it as a request attribute.
	 * This method is called before main request processing to preload dropdowns or
	 * reference data.
	 * 
	 * @param request HttpServletRequest object containing client request
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("CollegeListCtl preload started");
		CollegeModel collegeModel = new CollegeModel();

		try {
			List collegeList = collegeModel.list();
			request.setAttribute("collegeList", collegeList);
		} catch (ApplicationException e) {
			log.error("ApplicationException in preload", e);
			return;
		}
		log.debug("CollegeListCtl preload ended");
	}

	/**
	 * Populates a CollegeBean from HTTP request parameters. This bean is used for
	 * search and filtering operations.
	 * 
	 * @param request HttpServletRequest containing client parameters
	 * @return populated CollegeBean object
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("CollegeListCtl populateBean started");

		CollegeBean bean = new CollegeBean();

		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
		bean.setId(DataUtility.getLong(request.getParameter("collegeId")));

		log.debug("CollegeListCtl populateBean ended");
		return bean;
	}

	/**
	 * Handles HTTP GET requests. Loads the paginated list of colleges based on
	 * search criteria. Forwards the request to the list view.
	 * 
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CollegeListCtl doGet started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		CollegeBean bean = (CollegeBean) populateBean(request);
		CollegeModel model = new CollegeModel();

		try {
			List<CollegeBean> list = model.search(bean, pageNo, pageSize);
			List<CollegeBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error("ApplicationException in doGet", e);
		}

		log.debug("CollegeListCtl doGet ended");
	}

	/**
	 * Handles HTTP POST requests. Supports operations like Search, Next, Previous,
	 * New, Delete, Reset, and Back. Manages pagination, deletion, and forwarding to
	 * the list view.
	 * 
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CollegeListCtl doPost started");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		CollegeBean bean = (CollegeBean) populateBean(request);
		CollegeModel model = new CollegeModel();

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;
				if (ids != null && ids.length > 0) {

					CollegeBean deletebean = new CollegeBean();
					for (String id : ids) {

						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);
						ServletUtility.setSuccessMessage("Data is deleted successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo + 1, pageSize);

			if (!OP_DELETE.equalsIgnoreCase(op)) {

				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error("ApplicationException in doPost", e);
			return;
		}

		log.debug("CollegeListCtl doPost ended");
	}

	/**
	 * Returns the view page for the College List.
	 * 
	 * @return String representing the view path
	 */
	@Override
	protected String getView() {
		return ORSView.COLLEGE_LIST_VIEW;
	}
}
