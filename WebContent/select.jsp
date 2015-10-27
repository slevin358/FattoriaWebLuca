<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Fattoria Web | Elenco</title>
	</head>
	<body>
		<c:if test="${colonne == null}">
			<c:set var = "errore" value = "L'attributo 'colonne' non può essere null" scope = "request" />
			<jsp:forward page="errore.jsp" />
		</c:if>
		<c:if test="${elenco == null}">
			<c:set var = "errore" value = "L'attributo 'elenco' non può essere null" scope = "request" />
			<jsp:forward page="errore.jsp" />
		</c:if>
		<%@ include file = "menu.html" %>
		<table>
			<tr>
				<c:forEach var = "colonna" items = "${colonne}">
					<th>${colonna}</th>
				</c:forEach>
			</tr>
			<c:forEach var = "elemento" items = "${elenco}">
				<tr>${elemento}</tr>
			</c:forEach>
		</table>
	</body>
</html>