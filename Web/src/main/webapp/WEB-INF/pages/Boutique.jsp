<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>SnakeOnline - Boutique</title>
	<link rel="stylesheet" href="css/menu.css">
	<link rel="stylesheet" href="css/accueil.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	<!-- Script pour afficher la popup -->
	<script>
	    function showLoginPopup() {
	        var myModal = new bootstrap.Modal(document.getElementById('loginModal'));
	        myModal.show();
	    }
	</script>
</head>
<body>
	<%@ include file="../partials/Menu.jsp" %>

	<div>
	    <div class="container mt-5">
	    	<c:if test="${not empty form}" >
	    		<div class="card bg-dark text-light p-2 text-center">
		    		<h5 class="m-0">${form.resultat}</h5>
		    		<c:if test="${not empty form.erreurs['item']}" >${form.erreurs['item'] }</c:if>
		    		<c:if test="${not empty form.erreurs['joueur']}" >${form.erreurs['joueur'] }</c:if>
		    	</div>
	    	</c:if>
		    <h2 class="text-center">Boutique</h2>
		    <br/>
		    <div class="row">
		        <c:forEach var="item" items="${items}">
		            <div class="col-md-4 mb-2">
		                <div class="card bg-dark text-white d-flex flex-column" style="height: 400px;">
		                    <div class="card-body text-center flex-grow-1 d-flex flex-column justify-content-between gap-3">
		                    	<div class="d-flex justify-content-center">
		                    		<div class="bg-light text-center" style="width: 210px; height: 210px; overflow: hidden; border-radius: 15px">
				                    	<img src="<c:url value="${item.image}"/>" class="card-img-top" alt="Item 1" style="height: 200px; width: auto; object-fit: contain;">
				                    </div>
		                    	</div>
		                        <div>
		                        	<h5 class="card-title">${item.title}</h5>
		                        	<p class="card-text">${item.description}</p>
		                        </div>
		                        <c:if test="${not empty sessionScope.joueurID }">
		                        	<form action="acheterItem" method="POST">
			                            <input type="hidden" name="itemId" value="${item.id}">
			                            <button type="submit" class="btn btn-info">
			                                Acheter - ${item.price} Pièces
			                            </button>
			                        </form>
		                        </c:if>
		                        <c:if test="${empty sessionScope.joueurID }">
		                        	<button type="button" class="btn btn-info" onclick="showLoginPopup()">Acheter - ${item.price} Pièces</button>
		                        </c:if>
		                    </div>
		                </div>
		            </div>
		        </c:forEach>
		    </div>
		</div>
	</div>
	
	<!-- La popup de connexion avec Bootstrap -->
	<div id="loginModal" class="modal fade" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content bg-dark text-light">
	            <div class="modal-header">
	                <h5 class="modal-title" id="loginModalLabel">Connexion requise</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </div>
	            <div class="modal-body">
	                <p>Veuillez vous connecter pour acheter cet article.</p>
	            </div>
	            <div class="modal-footer">
	                <a href="connexion" class="btn btn-info">Se connecter</a>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>