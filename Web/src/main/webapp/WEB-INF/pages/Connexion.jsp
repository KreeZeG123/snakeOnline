<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="css/connexion.css">
	<link rel="stylesheet" href="css/menu.css">
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