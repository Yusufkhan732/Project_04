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
<!-- Include jQuery -->
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<!-- Include jQuery UI -->
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
<!-- Include jQuery UI CSS -->
<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script src="/Project-04/js/checkbox.js"></script>
<script src="/Project-04/js/datepicker.js"></script>
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
	<a href="MyProfileCtl"><b>My Profile</b></a>
	<b>|</b>
	<a href="ChangePasswordCtl"><b>Change Password</b></a>
	<b>|</b>
	<a href="GetMarksheetCtl"><b>Get Marksheet</b></a>
	<b>|</b>
	<a href="MarksheetMeritListCtl"><b>Marksheet Merit List</b></a>
	<b>|</b>
	<a href="UserCtl"><b>Add User</b></a>
	<b>|</b>
	<a href="UserListCtl"><b>User List</b></a>
	<b>|</b>
	<a href="RoleCtl"><b>Add Role</b></a>
	<b>|</b>
	<a href="RoleListCtl"><b>Role List</b></a>
	<b>|</b>
	<a href="CollegeCtl"><b>Add College</b></a>
	<b>|</b>
	<a href="CollegeListCtl"><b>College List</b></a>
	<b>|</b>
	<a href="StudentCtl"><b>Add Student</b></a>
	<b>|</b>
	<a href="StudentListCtl"><b>Student List</b></a>
	<b>|</b>
	<a href="MarksheetCtl"><b>Add Marksheet</b></a>
	<b>|</b>
	<a href="MarksheetListCtl"><b>Marksheet List</b></a>
	<b>|</b>
	<a href="CourseCtl"><b>Add Course</b></a>
	<b>|</b>
	<a href="CourseListCtl"><b>Course List</b></a>
	<b>|</b>
	<a href="SubjectCtl"><b>Add Subject</b></a>
	<b>|</b>
	<a href="SubjectListCtl"><b>Subject List</b></a>
	<b>|</b>
	<a href="TimetableCtl"><b>Add Timetable</b></a>
	<b>|</b>
	<a href="TimetableListCtl"><b>Timetable List</b></a>
	<b>|</b>
	<a href="FacultyCtl"><b>Add Faculty</b></a>
	<b>|</b>
	<a href="FacultyListCtl"><b>Faculty List</b></a>
	<b>|</b>
	<a href="<%=ORSView.JAVA_DOC_VIEW%>" target="blank"><b>Java Doc</b></a>
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