<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnakeOnline - Classement</title>
    <link rel="stylesheet" href="css/menu.css">
    <link rel="stylesheet" href="css/accueil.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</head>
<body>
    <%-- Inclure le menu --%>
    <%@ include file="../partials/Menu.jsp" %>

    <div class="container mt-5">
        <h2 class="text-center">Classement</h2>

        <%-- Tableau pour afficher le classement --%>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th scope="col">Rang</th>
                    <th scope="col">Joueur</th>
                    <th scope="col">Score</th>
                </tr>
            </thead>
            <tbody>
                <%-- Boucle sur les classements passés dans le modèle --%>
                <c:forEach var="classement" varStatus="varStatus" items="${classements}">
                    <tr>
                        <td>${varStatus.index + 1}</td>
                        <td>${classement.username}</td>
                        <td>${classement.score}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</body>
</html>
