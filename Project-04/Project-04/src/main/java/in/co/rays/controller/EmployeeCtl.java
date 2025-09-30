package in.co.rays.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.EmployeeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.EmployeeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet(name = "EmployeeCtl", urlPatterns = { "/ctl/EmployeeCtl" })
public class EmployeeCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("IT", "IT");
		map.put("HR", "HR");
		map.put("Sale", "Sale");
		map.put("Marketing", "Marketing");
		map.put("Finance", "Finance");
		request.setAttribute("map", map);
	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		String dob = request.getParameter("dob");

		if (DataValidator.isNull(request.getParameter("employeeName"))) {

			request.setAttribute("employeeName", PropertyReader.getValue("error.require", "Employee Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("employeeName"))) {
			request.setAttribute("employeeName", "Invalid Employee Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("lastName"))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("lastName"))) {
			request.setAttribute("lastName", "Invalid Last Name");
			pass = false;
		}

		if (DataValidator.isNull(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
			pass = false;

		} else if (!DataValidator.isDate(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("department"))) {

			request.setAttribute("department", PropertyReader.getValue("error.require", "Department"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		EmployeeBean bean = new EmployeeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setEmployeeName(DataUtility.getString(request.getParameter("employeeName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setDepartment(DataUtility.getString(request.getParameter("department")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));

		populateDTO(bean, request);

		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		EmployeeModel model = new EmployeeModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {

			EmployeeBean bean;

			try {

				bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);

			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		EmployeeModel model = new EmployeeModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			EmployeeBean bean = (EmployeeBean) populateBean(request);

			try {

				long pk = model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Data is Successfully Saved", request);

			} catch (ApplicationException e) {
				e.printStackTrace();
				return;

			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			EmployeeBean bean = (EmployeeBean) populateBean(request);

			try {

				if (bean.getId() > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, request);

				ServletUtility.setSuccessMessage("Data is successfully updated", request);

			} catch (ApplicationException e) {
				e.printStackTrace();
				return;

			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EMPLOYEE_LIS_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EMPLOYEE_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		return ORSView.EMPLOYEE_VIEW;
	}
}
