package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.model.TimetableModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * TimetableCtl is a controller responsible for handling operations related to
 * creating, updating, validating, and displaying timetable entries.
 *
 * It communicates with TimetableModel to perform database operations.
 * 
 * @author Yusuf Khan
 */
@WebServlet(name = "TimetableCtl", urlPatterns = { "/ctl/TimetableCtl" })
public class TimetableCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TimetableCtl.class);

	/**
	 * Preloads subject and course lists for use in dropdowns on the Timetable form.
	 * 
	 * @param request the HttpServletRequest object
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("TimetableCtl preload started");

		SubjectModel subjectModel = new SubjectModel();
		CourseModel courseModel = new CourseModel();

		try {
			List subjectList = subjectModel.list();
			request.setAttribute("subjectList", subjectList);
			log.debug("Loaded subject list size: " + (subjectList != null ? subjectList.size() : 0));

			List courseList = courseModel.list();
			request.setAttribute("courseList", courseList);
			log.debug("Loaded course list size: " + (courseList != null ? courseList.size() : 0));

		} catch (Exception e) {
			log.error("Error in preload", e);
		}
		log.debug("TimetableCtl preload ended");
	}

	/**
	 * Validates user input from the Timetable form.
	 * 
	 * @param request the HttpServletRequest
	 * @return true if input is valid; false otherwise
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("TimetableCtl validate started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("semester"))) {
			request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.require", "Date of Exam"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.date", "Date of Exam"));
			pass = false;
		} else if (DataValidator.isSunday(request.getParameter("examDate"))) {
			request.setAttribute("examDate", "Exam should not be on Sunday");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("examTime"))) {
			request.setAttribute("examTime", PropertyReader.getValue("error.require", "Exam Time"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("courseId"))) {
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("subjectId"))) {
			request.setAttribute("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
			pass = false;
		}

		log.debug("TimetableCtl validate ended with status: " + pass);
		return pass;
	}

	/**
	 * Populates a TimetableBean with request parameters.
	 * 
	 * @param request the HttpServletRequest
	 * @return a populated TimetableBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("TimetableCtl populateBean started");

		TimetableBean bean = new TimetableBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setSemester(DataUtility.getString(request.getParameter("semester")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setExamTime(DataUtility.getString(request.getParameter("examTime")));
		bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));

		populateDTO(bean, request);

		log.debug("TimetableCtl populateBean ended");
		return bean;
	}

	/**
	 * Handles GET requests. Loads existing Timetable data for editing.
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimetableCtl doGet started");

		long id = DataUtility.getLong(request.getParameter("id"));

		TimetableModel model = new TimetableModel();

		if (id > 0) {
			try {
				TimetableBean bean = model.findByPk(id);
				if (bean != null) {
					ServletUtility.setBean(bean, request);
					log.debug("Timetable found with id: " + id);
				} else {
					log.warn("No timetable found with id: " + id);
				}
			} catch (ApplicationException e) {
				log.error("Error in doGet while fetching timetable by PK", e);
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("TimetableCtl doGet ended");
	}

	/**
	 * Handles POST requests for Save, Update, Reset, and Cancel operations.
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimetableCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));
		TimetableModel model = new TimetableModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			log.debug("Operation = SAVE");
			TimetableBean bean = (TimetableBean) populateBean(request);

			try {
				TimetableBean bean1 = model.checkByCourseName(bean.getCourseId(), bean.getExamDate());
				TimetableBean bean2 = model.checkBySubjectName(bean.getCourseId(), bean.getSubjectId(),
						bean.getExamDate());
				TimetableBean bean3 = model.checkBySemester(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(),
						bean.getExamDate());

				if (bean1 == null && bean2 == null && bean3 == null) {
					long pk = model.add(bean);
					log.info("New Timetable added successfully, ID: " + pk);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Timetable added successfully", request);
				} else {
					log.warn("Duplicate timetable entry detected on SAVE");
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Timetable already exist!", request);
				}

			} catch (ApplicationException e) {
				log.error("ApplicationException in SAVE", e);
			} catch (DuplicateRecordException e) {
				log.error("DuplicateRecordException in SAVE", e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Timetable already exist!", request);
			} catch (Exception e) {
				log.error("Unexpected exception in SAVE", e);
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			log.debug("Operation = UPDATE");
			TimetableBean bean = (TimetableBean) populateBean(request);

			try {
				TimetableBean bean4 = model.checkByExamTime(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(),
						bean.getExamDate(), bean.getExamTime(), bean.getDescription());

				if (id > 0 && bean4 == null) {
					model.update(bean);
					log.info("Timetable updated successfully, ID: " + id);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Timetable updated successfully", request);
				} else {
					log.warn("Duplicate timetable entry detected on UPDATE");
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Timetable already exist!", request);
				}
			} catch (ApplicationException e) {
				log.error("ApplicationException in UPDATE", e);
			} catch (DuplicateRecordException e) {
				log.error("DuplicateRecordException in UPDATE", e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Timetable already exist!", request);
			} catch (Exception e) {
				log.error("Unexpected exception in UPDATE", e);
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			log.debug("Operation = CANCEL");
			ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			log.debug("Operation = RESET");
			ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("TimetableCtl doPost ended");
	}

	/**
	 * Returns the view page path for Timetable form.
	 * 
	 * @return the path to timetable JSP
	 */
	@Override
	protected String getView() {
		return ORSView.TIMETABLE_VIEW;
	}
}
