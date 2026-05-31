<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/header.jsp" %>
    <h1>¡Bienvenido, ${sessionScope.usuarioActivo.nombre}!</h1>
    <p>Has iniciado sesion correctamente.</p>
    <hr>
    <a href="${pageContext.request.contextPath}/consulta">Ver catalogo</a> |
    <a href="${pageContext.request.contextPath}/logout">Cerrar sesion</a>
<%@ include file="/footer.jsp" %>