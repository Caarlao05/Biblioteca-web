<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca Don Bosco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <header>
        <h2>Biblioteca Colegio Amigos de Don Bosco</h2>
        <nav>
            <a href="${pageContext.request.contextPath}/">Inicio</a>
            <a href="${pageContext.request.contextPath}/consulta">Catalogo</a>
            <c:choose>
                <c:when test="${not empty sessionScope.usuarioActivo}">
                    <a href="${pageContext.request.contextPath}/privado/inicio.jsp">
                        ${sessionScope.usuarioActivo.nombre}
                    </a>
                    <a href="${pageContext.request.contextPath}/logout">Cerrar sesion</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Iniciar sesion</a>
                </c:otherwise>
            </c:choose>
        </nav>
        <hr>
    </header>
    <main>