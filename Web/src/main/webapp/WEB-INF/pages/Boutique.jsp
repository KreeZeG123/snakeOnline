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
</head>
<body>
	<%@ include file="../partials/Menu.jsp" %>

	<div>
	    <div class="container mt-5">
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
		                        <form action="acheterItem" method="POST">
		                            <input type="hidden" name="itemId" value="${item.id}">
		                            <button type="submit" class="btn btn-primary">
		                                Acheter - ${item.price} Pièces
		                            </button>
		                        </form>
		                    </div>
		                </div>
		            </div>
		        </c:forEach>
		    </div>
		</div>
	</div>
</body>
</html>