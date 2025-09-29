<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.controller.EmployeeCtl"%>
<%@page import="in.co.rays.util.HTMLUtility"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@page import="in.co.rays.bean.EmployeeBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<form action="<%=ORSView.EMPLOYEE_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.bean.EmployeeBean"
			scope="request"></jsp:useBean>

		<%
			 HashMap hmap = (HashMap)request.getAttribute("map");
		%>

		<div align="center">
			<h1 align="center" style="margin-bottom: -15; color: navy">
				<%
					if (bean != null && bean.getId() > 0) {
				%>Update<%
					} else {
				%>Add<%
					}
				%>
				Employee
			</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<H3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</H3>

				<H3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</H3>
			</div>

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>
				<tr>
					<th align="left">Employee Name<span style="color: red">*</span></th>
					<td><input type="text" name="employeeName"
						placeholder="Enter Employee Name"
						value="<%=DataUtility.getStringData(bean.getEmployeeName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("employeeName", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Last Name<span style="color: red">*</span></th>
					<td><input type="text" name="lastName"
						placeholder="Enter Last Name"
						value="<%=DataUtility.getStringData(bean.getLastName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("lastName", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Date of Birth<span style="width: 98%"
						style="color: red">*</span></th>
					<td><input type="text" name="dob" id="udate"
						value="<%=DataUtility.getStringData(bean.getDob())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("dob", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Department<span style="color: red">*</span></th>
					<td><%=HTMLUtility.getList("department", bean.getDepartment(), hmap)%></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("department", request)%></font></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=EmployeeCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=EmployeeCtl.OP_CANCEL%>">
						<%
							} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=EmployeeCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=EmployeeCtl.OP_RESET%>">
						<%
							}
						%>
				</tr>
			</table>
		</div>
	</form>
	<%@ include file="Footer.jsp"%>
</body>

</body>
</html>