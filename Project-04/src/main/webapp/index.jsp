<%@page import="in.co.rays.bean.UserBean"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@page import="in.co.rays.controller.ORSView"%>
<html>
<title>Online Result System</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<br>
	<br>
	<%
		UserBean userBean = (UserBean) session.getAttribute("user");

		boolean userLoggedIn = userBean != null;

		if (userLoggedIn) {
			ServletUtility.forward(ORSView.WELCOME_VIEW, request, response);
		}
	%>
	<marquee behavior="alternate" scrollamount="200" loop="1">
		<div align="center">
			<img src="img/customLogo.jpg" align="middle" width="318" height="127"
				border="0">
		</div>
	</marquee>

	<br>
	<br>
	
		<h1 align="center">
			<font size="10px" color="red"> <a
				href="<%=ORSView.WELCOME_CTL%>">Online Result System</a></font>
		</h1>
</body>
</html>