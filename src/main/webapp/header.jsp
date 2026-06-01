<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca Don Bosco</title>
</head>
<body style="margin: 0; font-family: Arial, sans-serif; font-size: 13px;">

<table width="100%" border="0" cellpadding="10" cellspacing="0" style="min-height: 500px;">
<tr>
    <td width="200" valign="top" style="border-right: 1px solid #ccc; padding: 15px;">
        <div style="text-align: center; margin-bottom: 20px;">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="UDB Logo" width="120" style="border-radius: 10px;">
        </div>
        
        <div style="border: 1px solid #cddc39; padding: 10px; margin-bottom: 15px;">
            <b style="color: #666; font-size: 12px;">Tipos de búsqueda</b><br>
            <hr style="border: 0; border-top: 1px solid #cddc39; margin: 5px 0;">
            <a href="${pageContext.request.contextPath}/consulta" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Básica</a><br>
            <a href="#" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Avanzada</a>
        </div>
        
        <div style="border: 1px solid #cddc39; padding: 10px; margin-bottom: 15px;">
            <b style="color: #666; font-size: 12px;">Idiomas</b><br>
            <hr style="border: 0; border-top: 1px solid #cddc39; margin: 5px 0;">
            <a href="#" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Español</a><br>
            <a href="#" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Inglés</a>
        </div>
        
        <div style="border: 1px solid #cddc39; padding: 10px;">
            <b style="color: #666; font-size: 12px;">Estudiante</b><br>
            <hr style="border: 0; border-top: 1px solid #cddc39; margin: 5px 0;">
            <c:choose>
                <c:when test="${not empty sessionScope.usuarioActivo}">
                    <a href="${pageContext.request.contextPath}/privado/inicio.jsp" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Mi Perfil</a><br>
                    <c:if test="${sessionScope.usuarioActivo.idRol == 1}">
                        <a href="${pageContext.request.contextPath}/privado/admin.jsp" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Administración</a><br>
                    </c:if>
                    <c:if test="${sessionScope.usuarioActivo.idRol != 1}">
                        <a href="${pageContext.request.contextPath}/privado/prestamos.jsp" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Mis Préstamos</a><br>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/logout" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Cerrar Sesión</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" style="text-decoration:none; color:#1a0dab; font-size: 12px;">&rarr; Iniciar sesión</a>
                </c:otherwise>
            </c:choose>
        </div>
    </td>
    <td valign="top" style="padding: 20px;">
