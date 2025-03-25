<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>SnakeGame</title>
	<link rel="stylesheet" href="css/menu.css">
	<link rel="stylesheet" href="css/register.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</head>
<div>
	<%@ include file="../partials/Menu.jsp" %>
</div>
	<div class="register-div">
		<div class="register-container">
	        <form method="POST" class="register-form">
	            <h2>Inscription</h2>
	
	            <!-- Champ Nom d'utilisateur -->
	            <div class="input-group">
	                <label for="username">Nom d'utilisateur :</label>
	                <input type="text" id="username" name="username" required placeholder="Entrez votre nom d'utilisateur">
	            </div>
	
	            <!-- Champ Email -->
	            <div class="input-group">
	                <label for="email">Email :</label>
	                <input type="email" id="email" name="email" required placeholder="Entrez votre email">
	            </div>
	
	            <!-- Champ Mot de passe -->
	            <div class="input-group">
	                <label for="password">Mot de passe :</label>
	                <input type="password" id="password" name="password" required placeholder="Entrez votre mot de passe">
	            </div>
	
	            <!-- Champ Confirmation du mot de passe -->
	            <div class="input-group">
	                <label for="confirm-password">Confirmez le mot de passe :</label>
	                <input type="password" id="confirm-password" name="confirm-password" required placeholder="Confirmez votre mot de passe">
	            </div>
	
	            <!-- Bouton d'inscription -->
	            <button type="submit" class="register-btn">S'inscrire</button>
	
	            <!-- Lien vers la connexion -->
	            <p class="login-text">Déjà un compte ?  
	                <a href="connexion" class="login-link">Se connecter</a>
	            </p>
	        </form>
	    </div>
	</div>
</html>