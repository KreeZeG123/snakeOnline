<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>SnakeGame</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
		<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=logout" />
	</head>
	<body>
		<%@ include file="../partials/Menu.jsp" %>
		<div class="mx-auto flex-column text-break justify-content-center align-items-center w-75">
			<div class="text-center py-4 bg-dark text-white rounded">
				<div class="d-flex flex-row justify-content-end px-4">
					<a class="btn btn-light btn-lg material-symbols-outlined" href="<c:url value="deconnexion" />">logout</a>
				</div>
				<div>
			        <h1 class="mb-3"><c:out value="Profil de ${joueur.username}"/></h1>
			        <h3><c:out value="Pieces : ${joueur.nbPieces}"/></h3>
			        <h3><c:out value="Score : ${joueur.score}"/></h3>
			        <h2>Collection de skins</h2>
		        </div>
		        <hr class="border border-info border-4 opacity-100 rounded mx-3">
			    <div class="row">
			        <c:forEach var="item" items="${items}">
			            <div class="col-md-4 mb-2">
			                <div class="card border-0 bg-dark text-white d-flex flex-column" style="height: 400px;">
			                    <div class="card-body text-center d-flex flex-column">
			                    	<div class="d-flex justify-content-center">
			                    		<div class="bg-light text-center" style="width: 210px; height: 210px; overflow: hidden; border-radius: 15px">
					                    	<img src="<c:url value="${item.image}"/>" class="card-img-top" alt="Item 1" style="height: 200px; width: auto; object-fit: contain;">
					                    </div>
			                    	</div>
			                        <div class="mt-5">
			                        	<h5 class="card-title">${item.title}</h5>
			                        	<p class="card-text">${item.description}</p>
			                        </div>
			                    </div>
			                </div>
			            </div>
			        </c:forEach>
			    </div>
		    </div>
	   	</div>
	</body>
</html>