<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>SnakeOnline - Accueuil</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	</head>
	<body>
		<%@ include file="../partials/Menu.jsp" %>
		<div class="d-flex flex-column text-break justify-content-center align-items-center">
			<div class="text-center py-3 bg-dark text-white rounded" style="width: 60vw;" >
		        <h1 class="text-info">Bienvenue sur SnakeOnline</h1>
		        <h3>Connectez-vous pour jouer et grimper dans les classements !</h3>
		        
		        <div class="container text-center mt-4">
			        <a href="connexion" class="btn btn-info">Se connecter</a>
			    </div>
         		<ul class="list-unstyled">
       				<c:forEach items="${ utilisateurs }" var="utilisateur" varStatus="status">
			    		<li>N°<c:out value="${ status.count }" /> : <c:out value="${ utilisateur.username }" /> !</li>
			    	</c:forEach>
		        </ul>
		        <div class="m-4 px-5 ">
		        	<h5>Dans ce célèbre jeu en 2 dimensions, le but est de manger des pommes pour faire grandir votre snake 
		        	et ainsi tenter de manger votre adversaire. De plus, des items aléatoires peuvent apparaître sur la 
		        	carte pour vous aider ou peut-être vous pénaliser</h5>
		        </div>
		        <div class="text-center pt-3 bg-dark text-white rounded">
		       		<img src="<c:url value="/images/snakegame.png"/>" style="width: 70%; height: auto;">
		    	</div>
		    </div>
	   	</div>
	</body>
</html>