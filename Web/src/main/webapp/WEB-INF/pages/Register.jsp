<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscription</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</head>
<body>
    <div>
        <%@ include file="../partials/Menu.jsp" %>
    </div>
    <div class="container d-flex justify-content-center align-items-center" style="height: 75vh;">
        <div class="card bg-dark text-light p-4 shadow-lg" style="width: 400px;">
            <form action="inscription" method="POST">
                <h2 class="text-center mb-4">Inscription</h2>
                
                <p class="text-center mb-4 ${empty form.erreurs ? 'text-success' : 'text-danger'}">${form.resultat}</p>

                <!-- Champ Nom d'utilisateur -->
                <div class="mb-3">
                    <label for="username" class="form-label">Nom d'utilisateur :</label>
                    <input type="text" id="username" name="username" class="form-control" required
                    	placeholder="Entrez votre nom d'utilisateur"
                    	value="<c:out value="${joueur.username}"/>"
                   	>
                   	<span class="text-danger">${form.erreurs['username']}</span>
                </div>

                <!-- Champ Email -->
                <div class="mb-3">
                    <label for="email" class="form-label">Email :</label>
                    <input type="email" id="email" name="email" class="form-control"
                    	required placeholder="Entrez votre email"
                    	value="<c:out value="${joueur.email}"/>"
                   	>
                 	<span class="text-danger">${form.erreurs['email']}</span>
                </div>

                <!-- Champ Mot de passe -->
                <div class="mb-3">
                    <label for="password" class="form-label">Mot de passe :</label>
                    <input type="password" id="password" name="password" class="form-control" required
                    	placeholder="Entrez votre mot de passe"
                    	value=""
                   	>
                   	<span class="text-danger">${form.erreurs['password']}</span>
                </div>

                <!-- Champ Confirmation du mot de passe -->
                <div class="mb-3">
                    <label for="confirm-password" class="form-label">Confirmez le mot de passe :</label>
                    <input type="password" id="confirm-password" name="confirm-password" class="form-control" required
						placeholder="Confirmez votre mot de passe"
						value=""
					>
					<span class="text-danger">${form.erreurs['confirm-password']}</span>
                </div>

                <!-- Bouton d'inscription -->
                <button type="submit" class="btn btn-info w-100">S'inscrire</button>

                <!-- Lien vers la connexion -->
                <p class="text-center mt-3">Déjà un compte ?  
                    <a href="connexion" class="text-decoration-none text-info">Se connecter</a>
                </p>
            </form>
        </div>
    </div>
</body>
</html>