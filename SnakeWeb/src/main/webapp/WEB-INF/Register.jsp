<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>SnakeGame</title>
	<link rel="stylesheet" href="css/menu.css">
	<link rel="stylesheet" href="css/register.css">
</head>
<div>
	<%@ include file="Menu.jsp" %>
</div>
	<div class="register-div">
		<div class="register-container">
	        <form action="/enregistrement" method="POST" class="register-form">
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