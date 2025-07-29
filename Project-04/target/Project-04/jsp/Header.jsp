<%@page import="in.co.rays.controller.LoginCtl"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@page import="in.co.rays.bean.UserBean"%>
<%@page import="in.co.rays.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
	<%
		UserBean user = (UserBean) session.getAttribute("user");
	%>
	<%
		if (user != null) {
	%>
	<h3>
		Hi,
		<%=user.getFirstName()%>
		(<%=session.getAttribute("role")%>)
	</h3>
	<a href="UserCtl"><b>Add User</b></a>
	<b>|</b>
	<a href="UserListCtl"><b>User List</b></a>
	<b>|</b>
	<a href="RoleCtl">Add Role</a>
	<b>|</b>
	<a href="RoleListCtl">Role List</a>
	<b>|</b>
	<a href="CollegeCtl">Add College</a>
	<b>|</b>
	<a href="CollegeListCtl">College List</a>
	<b>|</b>
	<a href="StudentCtl">Add Student</a>
	<b>|</b>
	<a href="StudentListCtl">Student List</a>
	<b>|</b>
	<a href="SubjectCtl">Add Subject</a>
	<b>|</b>
	<a href="SubjectListCtl">Subject List</a>
	<b>|</b>
	<a href="MarksheetCtl">Add Marksheet</a>
	<b>|</b>
	<a href="MarksheetListCtl">Marksheet List</a>
	<b>|</b>
	<b>|</b>
	<a href="CourseCtl">Add Course</a>
	<b>|</b>
	<a href="CourseListCtl">Course List</a>
	<b>|</b>
	<a href="LoginCtl?operation=<%=LoginCtl.OP_LOG_OUT%>"><b>Logout</b></a>
	<%
		} else {
	%>
	<h3>Hi, Guest</h3>
	<a href="WelcomeCtl"><b>Welcome</b></a> |
	<a href="LoginCtl"><b>Login</b></a>
	<%
		}
	%>
	<hr>
</body>
</html>