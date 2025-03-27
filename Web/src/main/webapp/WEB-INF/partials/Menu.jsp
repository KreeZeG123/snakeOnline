<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String currentPage = request.getRequestURI();
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-2 mb-4">
  <div class="container-fluid text-light">
    <!-- Navbrand à gauche -->
    <a class="navbar-brand text-info fw-bold" href="accueil">SnakeOnline</a>

    <!-- Bouton responsive -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <!-- Liens de navigation centrés -->
    <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link fw-bold <%= currentPage.contains("Accueil") ? "text-info" : "text-light" %>" href="accueil">Accueil</a>
        </li>
        <li class="nav-item">
          <a class="nav-link fw-bold <%= currentPage.contains("Boutique") ? "text-info" : "text-light" %>" href="boutique">Boutique</a>
        </li>
        <li class="nav-item">
          <a class="nav-link fw-bold <%= currentPage.contains("Classement") ? "text-info" : "text-light" %>" href="classement">Classement</a>
        </li>
      </ul>
    </div>

    <!-- Profil / Connexion à droite -->
    <div class="d-flex">
      <c:if test="${empty sessionScope.joueurID}">
        <a class="nav-link fw-bold <%= currentPage.contains("Connexion") ? "text-info" : "text-light" %>" href="connexion">Se connecter</a>
      </c:if>
      <c:if test="${not empty sessionScope.joueurID}">
        <a class="nav-link fw-bold <%= currentPage.contains("Profil") ? "text-info" : "text-light" %>" href="profil">Profil</a>
      </c:if>
    </div>
  </div>
</nav>
