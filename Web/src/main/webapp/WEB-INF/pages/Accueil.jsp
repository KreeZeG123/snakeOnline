<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		<div class="d-flex flex-column justify-content-center align-items-center">
			<div class="text-center p-5 bg-dark text-white rounded">
		        <h1>Bienvenue sur SnakeOnline</h1>
		        <p>Connectez-vous pour jouer et grimper dans les classements !</p>
		        
		        <div class="container text-center mt-4">
			        <a href="connexion" class="btn btn-primary">Se connecter</a>
			    </div>
		    </div>
	   	</div>
	</body>
</html>