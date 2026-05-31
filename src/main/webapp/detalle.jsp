<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/header.jsp" %>
    <h1>Detalle del documento</h1>
    <hr>

    <table border="2" cellpadding="8" cellspacing="0">
        <tr>
            <th>Codigo</th>
            <td>${doc.codigo}</td>
        </tr>
        <tr>
            <th>Titulo</th>
            <td>${doc.titulo}</td>
        </tr>
        <tr>
            <th>Autor</th>
            <td>${doc.autor}</td>
        </tr>
        <tr>
            <th>Tipo</th>
            <td>${doc.tipo}</td>
        </tr>
        <tr>
            <th>Año de publicacion</th>
            <td>${doc.anioPublicacion}</td>
        </tr>
        <tr>
            <th>Clasificacion</th>
            <td>${doc.clasificacion}</td>
        </tr>
        <tr>
            <th>Ubicacion</th>
            <td>${doc.ubicacion}</td>
        </tr>
        <tr>
            <th>Ejemplares disponibles</th>
            <td>${doc.disponibles} de ${doc.total}</td>
        </tr>
        <tr>
            <th>Estado fisico</th>
            <td>${doc.estadoFisico}</td>
        </tr>
    </table>

    <hr>
    <a href="consulta">Volver al catalogo</a>
<%@ include file="/footer.jsp" %>