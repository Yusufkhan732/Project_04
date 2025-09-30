package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.model.CourseModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * SubjectListCtl is a controller servlet responsible for handling the
 * operations related to listing, searching, pagination, and deletion of Subject
 * records.
 * 
 * It interacts with the SubjectModel to fetch data and manages user inputs from
 * the Subject list view.
 * 
 * Supports operations: Search, Next, Previous, New, Delete, Reset, Back.
 * 
 * @author Yusuf Khan
 */
@WebServlet(name = "SubjectListCtl", urlPatterns = { "/ctl/SubjectListCtl" })
public class SubjectListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(SubjectListCtl.class);

	/**
	 * Loads data needed for dropdowns or other preload requirements before the page
	 * is rendered.
	 * 
	 * @param request HttpServletRequest object
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("SubjectListCtl preload started");

		SubjectModel subjectModel = new SubjectModel();
		CourseModel courseModel = new CourseModel();
		try {
			List subjectList = subjectModel.list();
			request.setAttribute("subjectList", subjectList);

			List courseList = courseModel.list();
			request.setAttribute("courseList", courseList);
		} catch (Exception e) {
			log.error("Error in preload of SubjectListCtl", e);
		}

		log.debug("SubjectListCtl preload ended");
	}

	/**
	 * Populates a SubjectBean from the HTTP request parameters.
	 * 
	 * @param request HttpServletRequest object
	 * @return populated SubjectBean instance
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("SubjectListCtl populateBean started");

		SubjectBean bean = new SubjectBean();
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setCourseName(DataUtility.getString(request.getParameter("courseName")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		bean.setId(DataUtility.getLong(request.getParameter("subjectId")));

		log.debug("SubjectListCtl populateBean ended");
		return bean;
	}

	/**
	 * Handles HTTP GET requests to load and display the list of subjects with
	 * pagination.
	 * 
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("SubjectListCtl doGet started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		SubjectBean bean = (SubjectBean) populateBean(request);
		SubjectModel model = new SubjectModel();
		try {
			List<SubjectBean> list = model.search(bean, pageNo, pageSize);
			List<SubjectBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null && list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setBean(bean, request);
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			request.setAttribute("nextListSize", next.size());
		} catch (Exception e) {
			log.error("Error in doGet of SubjectListCtl", e);
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("SubjectListCtl doGet ended");
	}

	/**
	 * Handles HTTP POST requests for search, pagination, new record, delete, reset,
	 * and back operations.
	 * 
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("SubjectListCtl doPost started");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		SubjectBean bean = (SubjectBean) populateBean(request);
		SubjectModel model = new SubjectModel();

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		try {
			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
					pageNo--;
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					SubjectBean deletebBean = new SubjectBean();
					for (String id : ids) {
						deletebBean.setId(DataUtility.getLong(id));
						model.delete(deletebBean);
						ServletUtility.setSuccessMessage("Data Successfully Deleted", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.SUBJECT_LIS_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.SUBJECT_LIS_CTL, request, response);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo + 1, pageSize);

			if (!OP_DELETE.equalsIgnoreCase(op)) {
				if (list == null && list.size() > 0) {
					ServletUtility.setErrorMessage("No record found", request);
				}
			}

			ServletUtility.setBean(bean, request);
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			request.setAttribute("nextListSize", next.size());
			ServletUtility.forward(getView(), request, response);

		} catch (Exception e) {
			log.error("Error in doPost of SubjectListCtl", e);
		}

		log.debug("SubjectListCtl doPost ended");
	}

	/**
	 * Returns the path to the Subject list view JSP.
	 * 
	 * @return the Subject list JSP path
	 */
	@Override
	protected String getView() {
		return ORSView.SUBJECT_LIST_VIEW;
	}
}