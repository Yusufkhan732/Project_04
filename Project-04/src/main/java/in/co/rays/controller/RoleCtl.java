package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.RoleModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet("/RoleCtl")
public class RoleCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			isValid = false;

		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Invalid name");
			isValid = false;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			isValid = false;

		}
		return isValid;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		RoleBean bean = new RoleBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		RoleModel model = new RoleModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 && op != null) {

			RoleBean bean;
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

		long id = DataUtility.getLong(request.getParameter("id"));
		RoleModel model = new RoleModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			RoleBean bean = (RoleBean) populateBean(request);

			try {

				throw new ApplicationException("Error");
//				long pk = model.add(bean);
//				ServletUtility.setBean(bean, request);
//				ServletUtility.setSuccessMessage("Data Successfully saved", request);

			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);

				return;

//			} catch (DuplicateRecordException e) {
//				ServletUtility.setBean(bean, request);
//
//				ServletUtility.setErrorMessage("Role alredy exists", request);
//				return;
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			RoleBean bean = (RoleBean) populateBean(request);

			try {
				if (id > 0) {

					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data is Successfuly Update", request);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Role alredy exists", request);
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {

		return ORSView.ROLE_VIEW;
	}

}