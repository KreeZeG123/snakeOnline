<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Accueil</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
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
		<c:if test="${not empty form and form.result eq 'Connexion acceptée'}">
			<p><c:out value="${form.result}"/></p>
		</c:if>
		<p>Bonjour</p>
		
</body>

</html>