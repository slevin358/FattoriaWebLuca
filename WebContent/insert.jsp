<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Fattoria Web | Inserimento</title>
	</head>
	<body>
		<%@ include file = "menu.html" %>
		<form action = "ServletInsert">
			<input type = 'hidden' name = 'tipo' value = "${param.tipo}">
			<c:choose>
				<c:when test="${param.tipo == 'animale'}">
					<label>Nome: <input type = 'text' name = 'nome'></label><br>
					<label>Datanas: <input type = 'text' name = 'datanas'></label><br>
					Sesso: <label><input type = 'radio' name = 'sesso' value = 'M'> Maschio</label>
					<label><input type = 'radio' name = 'sesso' value = 'F'> Femmina</label><br>
					Vaccino: <label><input type = 'radio' name = 'vaccino' value = '1'> Si</label>
					<label><input type = 'radio' name = 'vaccino' value = '0'> No</label><br>
					<select name = 'specie'>
						<option selected disabled></option>
						<c:forEach var = "elemento" items = "${elenco}">
							<option value = '${elemento}'>${elemento}</option>
						</c:forEach>
					</select><br>
				</c:when>
				<c:when test="${param.tipo == 'cliente'}">
					<label>Nome: <input type = 'text' name = 'nome'></label><br>
					<label>Cognome: <input type = 'text' name = 'cognome'></label><br>
					<label>Recapito: <input type = 'text' name = 'recapito'></label><br>
					<label>Datareg: <input type = 'text' name = 'datareg'></label><br>
					<label>Città: <input type = 'text' name = 'citta'></label><br>
				</c:when>
				<c:when test="${param.tipo == 'acquisto'}">
					Cliente: <select name = 'codcli'>
						<c:forEach var = "elemento" items = "${elenco_cliente}">
					<option value = '${elemento.codice}'>${elemento.descr}</option>
					</c:forEach>
					</select><br>
					Animale: <select name = 'codice'>
						<c:forEach var = "elemento" items = "${elenco_animale}">
					<option value = '${elemento.codice}'>${elemento.descr}</option>
					</c:forEach>
					</select><br>
					<label>Prezzo: <input type = 'text' name = 'prezzo'></label><br>
				</c:when>
			</c:choose>
			<input type = 'submit' value = 'Inserisci'>
		</form>
	</body>
</html>