package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * SubjectCtl is a controller servlet that handles CRUD operations for Subject.
 * It loads required data for the view, validates inputs, populates bean from
 * request, and manages save/update/cancel/reset operations.
 * 
 * @author Yusuf Khan
 */
@WebServlet(name = "SubjectCtl", urlPatterns = { "/ctl/SubjectCtl" })
public class SubjectCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(SubjectCtl.class);

	/**
	 * Loads list of courses to be displayed in the dropdown in the view.
	 * 
	 * @param request HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("SubjectCtl preload started");

		CourseModel model = new CourseModel();

		try {
			List<CourseBean> courseList = model.list();
			request.setAttribute("courseList", courseList);
		} catch (Exception e) {
			log.error("Exception in preload", e);
		}

		log.debug("SubjectCtl preload ended");
	}

	/**
	 * Validates form input parameters for Subject form.
	 * 
	 * @param request HttpServletRequest
	 * @return boolean true if all inputs are valid, false otherwise
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("SubjectCtl validate started");

		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "SubjectName"));
			isValid = false;
		}
		if (DataValidator.isNull(request.getParameter("courseId"))) {
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "CourseName"));
			isValid = false;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			isValid = false;
		}

		log.debug("SubjectCtl validate ended");
		return isValid;
	}

	/**
	 * Populates SubjectBean from HTTP request parameters.
	 * 
	 * @param request HttpServletRequest
	 * @return populated SubjectBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("SubjectCtl populateBean started");

		SubjectBean bean = new SubjectBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);

		log.debug("SubjectCtl populateBean ended");
		return bean;
	}

	/**
	 * Handles HTTP GET request to load subject data for editing if id is provided.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("SubjectCtl doGet started");

		String op = DataUtility.getStringData(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		SubjectModel model = new SubjectModel();

		if (id > 0 || op != null) {
			try {
				SubjectBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (Exception e) {
				log.error("Exception in doGet", e);
			}
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("SubjectCtl doGet ended");
	}

	/**
	 * Handles HTTP POST requests for Save, Update, Cancel, and Reset operations.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("SubjectCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));
		SubjectModel model = new SubjectModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {
			SubjectBean bean = (SubjectBean) populateBean(request);
			try {
				long pk = model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Data Successfully saved", request);
				log.info("Subject saved successfully with ID = " + pk);
			} catch (ApplicationException e) {
				log.error("ApplicationException in Save", e);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Subject already exists", request);
				log.error("Duplicate subject found", e);
			} catch (Exception e) {
				log.error("Exception in Save", e);
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			SubjectBean bean = (SubjectBean) populateBean(request);
			try {
				if (bean.getId() > 0) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data Successfully Updated", request);
					log.info("Subject updated successfully with ID = " + bean.getId());
				}
			} catch (ApplicationException e) {
				log.error("ApplicationException in Update", e);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Subject already exists", request);
				log.error("Duplicate subject found during update", e);
			} catch (Exception e) {
				log.error("Exception in Update", e);
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SUBJECT_LIS_CTL, request, response);
			log.debug("Operation Cancelled - Redirect to Subject List");
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
			log.debug("Operation Reset - Redirect to Subject Form");
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("SubjectCtl doPost ended");
	}

	/**
	 * Returns the path of the Subject view JSP.
	 * 
	 * @return Subject view path
	 */
	@Override
	protected String getView() {
		return ORSView.SUBJECT_VIEW;
	}
}