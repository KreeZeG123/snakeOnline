<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	<link href="css/style.css" rel="stylesheet" />
</head>
<body>
    <div>
        <%@ include file="../partials/Menu.jsp" %>
    </div>
    <div class="container d-flex justify-content-center align-items-center" style="height: 75vh;">
        <div class="card p-4 shadow-lg text-center" style="width: 400px;">
            <c:if test="${empty sessionScope.joueurID}">
            	<form action="connexion" method="POST">
	                <h2 class="text-center mb-4">Connexion</h2>
	
	                <p class="text-center mb-4 ${empty form.erreurs ? 'text-success' : 'text-danger'}">${form.resultat}</p>
	
	                <!-- Champ Nom d'utilisateur -->
	                <div class="mb-3">
	                    <label for="username" class="form-label">Nom d'utilisateur :</label>
	                    <input type="text" id="username" name="username" class="form-control" required
	                    	placeholder="Entrez votre nom d'utilisateur"
							value="<c:out value="${joueur.username}"/>"
	                   	>
	                   	<span class="erreur">${ (form.erreurs['username'] == null) ? form.erreurs['usernameAndPassword'] : form.erreurs['username']}</span>
	                </div>
	
	                <!-- Champ Mot de passe -->
	                <div class="mb-3">
	                    <label for="password" class="form-label">Mot de passe :</label>
	                    <input type="password" id="password" name="password" class="form-control" required
	                    	placeholder="Entrez votre mot de passe"
							value=""
	                   	>
	                   	<span class="erreur">${ empty form.erreurs['password'] ? form.erreurs['usernameAndPassword'] : form.erreurs['password']}</span>
	                </div>
	
	                <!-- Bouton de soumission -->
	                <button type="submit" class="btn btn-primary w-100">Se connecter</button>
	                <p class="text-center mt-3">Pas encore de compte ?  
	                    <a href="inscription" class="text-decoration-none">S'inscrire</a>
	                </p>
	            </form>
            </c:if>
            <c:if test="${not empty sessionScope.joueurID}">
            	<h2>Bonjour ${sessionScope.joueurUsername } 👋</h2>
            	<br/>
            	<h4>Tu es déjà connecté !</h4>
          	</c:if>
        </div>
    </div>
</body>
</html>
