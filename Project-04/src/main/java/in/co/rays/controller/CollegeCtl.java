package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CollegeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet("/CollegeCtl")
public class CollegeCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			isValid = false;

		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "invalid name");
			isValid = false;

		}
		if (DataValidator.isNull(request.getParameter("address"))) {
			request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
			isValid = false;

		}
		if (DataValidator.isNull(request.getParameter("city"))) {
			request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
			isValid = false;

		}
		if (DataValidator.isNull(request.getParameter("state"))) {
			request.setAttribute("state", PropertyReader.getValue("error.require", "State"));
			isValid = false;

		}
		if (DataValidator.isNull(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", PropertyReader.getValue("error.require", "PhoneNo"));
			isValid = false;

		} else if (!DataValidator.isPhoneLength(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", "Phone no must have 10 digit");
			isValid = false;

		} else if (!DataValidator.isPhoneNo(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", "Invalid Phone No");
			isValid = false;

		}

		return isValid;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		CollegeBean bean = new CollegeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
		bean.setState(DataUtility.getString(request.getParameter("state")));
		bean.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));

		populateDTO(bean, request);
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		CollegeModel model = new CollegeModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 || op != null) {

			CollegeBean bean;
			try {
				bean = model.findByPk(id);

				ServletUtility.setBean(bean, request);

			} catch (Exception e) {

				e.printStackTrace();
				return;
			}

		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		CollegeModel model = new CollegeModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			CollegeBean bean = (CollegeBean) populateBean(request);

			try {

				long pk = model.add(bean);

				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Data is Successfully saved", request);

			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id Alredy exists", request);
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			CollegeBean bean = (CollegeBean) populateBean(request);
			try {
				if (bean.getId() > 0) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data is Successfully Update", request);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id alredy exists", request);
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.COLLEGE_VIEW;
	}
}
