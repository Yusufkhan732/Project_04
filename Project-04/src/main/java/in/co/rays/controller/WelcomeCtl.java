package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.util.ServletUtility;

@WebServlet("/WelcomeCtl")
public class WelcomeCtl extends BaseCtl {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("khan");
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		System.out.println("shdhdh");
		return ORSView.WELCOME_VIEW;
	}
}
