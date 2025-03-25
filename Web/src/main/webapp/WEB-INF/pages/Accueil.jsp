<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>SnakeGame</title>
		<!-- <link rel="stylesheet" href="css/accueil.css"> -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
		<!-- <link rel="stylesheet" href="css/menu.css"> -->
	</head>
	<body>
		<%@ include file="../partials/Menu.jsp" %>
	<div class="d-flex flex-column justify-content-center align-items-center">
		<h2 class="text-center">Classement</h2>
		<div class="card w-25">
        	<div class="card-body">
        		<ul class="list-unstyled">
		            <c:forEach items="${ utilisateurs }" var="utilisateur" varStatus="status">
					    <li>N°<c:out value="${ status.count }" /> : <c:out value="${ utilisateur.username }" /> !</li>
					</c:forEach>
		        </ul>
        	</div>
    	</div>
   	</div>
	</body>
</html>