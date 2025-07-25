<%@page import="in.co.rays.controller.ORSView"%>
<%@page import="in.co.rays.controller.RoleCtl"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="<%=ORSView.ROLE_CTL%>" method="get">
		<%@include file="Header.jsp"%>
		<jsp:useBean id="bean" class="in.co.rays.bean.RoleBean"></jsp:useBean>
		<div align="center">
			<h1 align="left" style="color: red; margin-bottom: -15; color: navy;">
				<%
					if (bean != null && bean.getId() > 0) {
				%>Update<%
					} else {
				%>Add<%
					}
				%>
				Role
			</h1>

			<div align="center" style="height: 15px; margin-bottom: 12px;">
				<h1 align="center">
					<font style="color: green;"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h1>
				<h3 align="center">
					<font style="color: red;"><%=ServletUtility.getErrorMessage(request)%></font>
				</h3>
			</div>

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>>">
			<input type="hidden" name="modified"
				value="<%=bean.getModifiedBy()%>>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>>">

			<table>
				<tr>
					<th align="left">Name<span style="color: red;">*</span></th>
					<td align="center"><input type="text" name="name"
						placeholder="Enter name"
						value="<%=DataUtility.getStringData(bean.getName())%>>"></td>
					<td style="position: fixed;"><font style="color: red;"><%=ServletUtility.getErrorMessage("name", request)%></font>
					</td>
				</tr>
				<tr>
					<th align="left">Description<span style="color: red;">*</span></th>
					<td align="center"><input type="text" name="description"
						placeholder="Enter description"
						value="<%=DataUtility.getStringData(bean.getDescription())%>>"></td>
					<td style="position: fixed;"><font style="color: red;"><%=ServletUtility.getErrorMessage("description", request)%>></font>
					</td>
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
						name="operation" value="<%=RoleCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=RoleCtl.OP_CANCEL%>">
					</td>
					<%
						} else {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=RoleCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=RoleCtl.OP_RESET%>">

						<%
							}
						%>
			</table>
		</div>
	</form>
</html>