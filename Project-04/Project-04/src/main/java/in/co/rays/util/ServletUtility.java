package in.co.rays.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.controller.BaseCtl;
import in.co.rays.controller.ORSView;

/**
 * Utility class to handle common servlet operations such as forwarding,
 * redirecting, setting/getting messages, beans, lists, pagination, and
 * exception handling.
 * 
 * Author: Yusuf Khan Version: 1.0
 */
public class ServletUtility {

	/**
	 * Forwards request to the given page.
	 * 
	 * @param page     the JSP or servlet page to forward to
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void forward(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
	}

	/**
	 * Redirects response to the given page.
	 * 
	 * @param page     the URL to redirect to
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void redirect(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendRedirect(page);
	}

	/**
	 * Returns the error message for a specific attribute.
	 * 
	 * @param property the attribute name
	 * @param request  HttpServletRequest object
	 * @return error message string or empty if null
	 */
	public static String getErrorMessage(String property, HttpServletRequest request) {
		String val = (String) request.getAttribute(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * Returns the message for a specific attribute.
	 * 
	 * @param property the attribute name
	 * @param request  HttpServletRequest object
	 * @return message string or empty if null
	 */
	public static String getMessage(String property, HttpServletRequest request) {
		String val = (String) request.getAttribute(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * Sets an error message in request.
	 * 
	 * @param msg     the error message
	 * @param request HttpServletRequest object
	 */
	public static void setErrorMessage(String msg, HttpServletRequest request) {
		request.setAttribute(BaseCtl.MSG_ERROR, msg);
	}

	/**
	 * Returns the error message set in request.
	 * 
	 * @param request HttpServletRequest object
	 * @return error message string or empty if null
	 */
	public static String getErrorMessage(HttpServletRequest request) {
		String val = (String) request.getAttribute(BaseCtl.MSG_ERROR);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * Sets a success message in request.
	 * 
	 * @param msg     the success message
	 * @param request HttpServletRequest object
	 */
	public static void setSuccessMessage(String msg, HttpServletRequest request) {
		request.setAttribute(BaseCtl.MSG_SUCCESS, msg);
	}

	/**
	 * Returns the success message set in request.
	 * 
	 * @param request HttpServletRequest object
	 * @return success message string or empty if null
	 */
	public static String getSuccessMessage(HttpServletRequest request) {
		String val = (String) request.getAttribute(BaseCtl.MSG_SUCCESS);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * Stores a bean in the request.
	 * 
	 * @param bean    BaseBean object to store
	 * @param request HttpServletRequest object
	 */
	public static void setBean(BaseBean bean, HttpServletRequest request) {
		request.setAttribute("bean", bean);
	}

	/**
	 * Returns the bean stored in the request.
	 * 
	 * @param request HttpServletRequest object
	 * @return BaseBean object
	 */
	public static BaseBean getBean(HttpServletRequest request) {
		return (BaseBean) request.getAttribute("bean");
	}

	/**
	 * Returns a parameter value from request.
	 * 
	 * @param property the parameter name
	 * @param request  HttpServletRequest object
	 * @return parameter value or empty if null
	 */
	public static String getParameter(String property, HttpServletRequest request) {
		String val = (String) request.getParameter(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * Stores a list in the request.
	 * 
	 * @param list    List of objects
	 * @param request HttpServletRequest object
	 */
	public static void setList(List list, HttpServletRequest request) {
		request.setAttribute("list", list);
	}

	/**
	 * Returns a list stored in the request.
	 * 
	 * @param request HttpServletRequest object
	 * @return List of objects
	 */
	public static List getList(HttpServletRequest request) {
		return (List) request.getAttribute("list");
	}

	/**
	 * Stores the current page number in request.
	 * 
	 * @param pageNo  current page number
	 * @param request HttpServletRequest object
	 */
	public static void setPageNo(int pageNo, HttpServletRequest request) {
		request.setAttribute("pageNo", pageNo);
	}

	/**
	 * Returns the page number stored in request.
	 * 
	 * @param request HttpServletRequest object
	 * @return current page number
	 */
	public static int getPageNo(HttpServletRequest request) {
		return (Integer) request.getAttribute("pageNo");
	}

	/**
	 * Stores the page size in request.
	 * 
	 * @param pageSize number of records per page
	 * @param request  HttpServletRequest object
	 */
	public static void setPageSize(int pageSize, HttpServletRequest request) {
		request.setAttribute("pageSize", pageSize);
	}

	/**
	 * Returns the page size stored in request.
	 * 
	 * @param request HttpServletRequest object
	 * @return number of records per page
	 */
	public static int getPageSize(HttpServletRequest request) {
		return (Integer) request.getAttribute("pageSize");
	}

	/**
	 * Handles exceptions by setting them in request and redirecting to error page.
	 * 
	 * @param e        Exception object
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setAttribute("exception", e);
		response.sendRedirect(ORSView.ERROR_CTL);
	}
}
