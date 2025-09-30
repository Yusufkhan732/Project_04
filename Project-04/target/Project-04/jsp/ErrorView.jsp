<%@page import="in.co.rays.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Error</title>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16"/></head>
</head>
<body>

	<h1 align="center">
		<img src="<%=ORSView.APP_CONTEXT%>/img/oops-404-error-design.jpg" 
     style="height: 300px; width: 500px;">

	</h1>
	<div align="center">
		<h1 align="center">Oops! Something went wrong.</h1>
		<br> <font style="color: red; font-size: 25px"><b>Check
				the network cables, modem and router</b></font>
	</div>
	<h4 align="center">
		<font size="5px" color="black"> <a
			href="<%=ORSView.WELCOME_CTL%>" style="color: deepskyblue;">*Please
				click here to Go Back*</a></font>
	</h4>
</body>
</html>