<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Fattoria Web | Inserimento</title>
	</head>
	<body>
		<c:if test="${param.tipo == null}">
			<c:set var = "errore" value = "L'attributo 'tipo' non può essere null" scope = "request" />
			<jsp:forward page="errore.jsp" />
		</c:if>
		<c:if test="${elenco == null}">
			<c:set var = "errore" value = "L'attributo 'elenco' non può essere null" scope = "request" />
			<jsp:forward page="errore.jsp" />
		</c:if>
		<%@ include file = "menu.html" %>
		<form action = "ServletDelete">
			<input type = 'hidden' name = 'tipo' value = '${param.tipo}'>
			<select name = 'codice'>
				<c:forEach var = "elemento" items = "${elenco}">
					<option value = '${elemento.codice}'>${elemento.descr}</option>
				</c:forEach>
			</select>
		<input type = 'submit' value = 'Cancella'>
		</form>
	</body>
</html>