<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Accueil</title>
</head>
<body>
	<p>
		Bonjour ${joueurs[1].username}
	</p>
	<p>
		Vous avez ${joueurs[1].nbPieces} pièces
	</p>
	
	<p><c:out value="Vous avez maintenant ${joueurs[1].nbPieces}"/></p>
	
	<c:forEach items="${joueurs}" var="joueur">
		<p>
			<c:out value="${joueur.username}"/>
		</p>
	</c:forEach>
	<c:if test="${not empty form and form.result eq 'Connexion acceptée'}"><p><c:out value="${form.result}"/></p></c:if>
	<p>Bonjour</p>
	
</body>

</html>