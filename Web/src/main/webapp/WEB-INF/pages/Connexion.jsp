<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="css/connexion.css">
	<link rel="stylesheet" href="css/menu.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</head>
<body>
	<div>
		<%@ include file="../partials/Menu.jsp" %>
	</div>
	<div class="div-form">
		<div class="login-container">
	        <form action="/connexion" method="POST" class="login-form">
	            <h2>Connexion</h2>
	
	            <!-- Champ Nom d'utilisateur -->
	            <div class="input-group">
	                <label for="username">Nom d'utilisateur :</label>
	                <input type="text" id="username" name="username" required placeholder="Entrez votre nom d'utilisateur">
	            </div>
	
	            <!-- Champ Mot de passe -->
	            <div class="input-group">
	                <label for="password">Mot de passe :</label>
	                <input type="password" id="password" name="password" required placeholder="Entrez votre mot de passe">
	            </div>
	
	            <!-- Bouton de soumission -->
	            <button type="submit" class="login-btn">Se connecter</button>
	            <p class="register-text">Pas encore de compte ?  
                	<a href="inscription" class="register-link">S'inscrire</a>
            	</p>
	        </form>
        </div>
    </div>
</body>
</html>