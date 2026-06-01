<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/header.jsp" %>

<div style="background-color: #f9f9f9; padding: 5px; margin-top: 10px; border: 1px solid #ddd; font-size: 12px;">
    <span style="background-color: #cddc39; width: 10px; display: inline-block;">&nbsp;</span>
    <b style="color: #999;">CITA COMPLETA</b>
</div>

<div style="margin-left: 50px; font-size: 12px; margin-top: 20px;">
    <table border="0" cellpadding="4" cellspacing="0" style="font-size: 12px;">
        <tr>
            <td align="right" valign="top" width="120"><b>Autor:</b></td>
            <td>${doc.autor}</td>
        </tr>
        <tr>
            <td align="right" valign="top"><b>Título:</b></td>
            <td>${doc.titulo}</td>
        </tr>
        <tr>
            <td align="right" valign="top"><b>Clasificación:</b></td>
            <td>${doc.tipo} ${doc.codigo}</td>
        </tr>
        <tr>
            <td align="right" valign="top"><b>Publisher:</b></td>
            <td>Ediciones UDB, ${doc.anioPublicacion}</td>
        </tr>
        <tr>
            <td align="right" valign="top"><b>Descripción:</b></td>
            <td>Ejemplares Totales: ${doc.ejemplares}</td>
        </tr>
        <tr>
            <td align="right" valign="top"><b>Estado Físico:</b></td>
            <td>${doc.estadoFisico}</td>
        </tr>
    </table>

    <br>
    <b>Ubicación de copias:</b><br>
    <div style="margin-left: 20px; margin-top: 10px;">
        BCA. CENTRAL - Item: ${doc.codigo} - 
        <c:choose>
            <c:when test="${doc.disponibles > 0}">
                (DISPONIBLE) 
                <c:if test="${not empty sessionScope.usuarioActivo and sessionScope.usuarioActivo.idRol != 1}">
                    &nbsp; <a href="${pageContext.request.contextPath}/privado/prestamos.jsp?prestar_id=${doc.id}" style="color: #1a0dab; text-decoration: none;">🔍 Reservar</a>
                </c:if>
                <c:if test="${empty sessionScope.usuarioActivo}">
                    &nbsp; <a href="${pageContext.request.contextPath}/login" style="color: #1a0dab; text-decoration: none;">🔍 Iniciar sesión para prestar</a>
                </c:if>
            </c:when>
            <c:otherwise>
                (NO DISPONIBLE)
            </c:otherwise>
        </c:choose>
    </div>
</div>

<br><hr style="border: 0; border-top: 1px solid #ccc;">
<div align="right" style="font-size: 11px;">
    <a href="consulta" style="color: #1a0dab; text-decoration: none;">Nueva búsqueda</a>
</div>

<%@ include file="/footer.jsp" %>
