<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/header.jsp" %>
    <h1>Consulta el catálogo</h1>
    <p>Colegio Amigos de Don Bosco</p>
    <hr>

    <!-- Formulario de búsqueda -->

    <form action="consulta" method="POST">
        <label>Titulo:</label>
        <input type="text" name="titulo" value="${titulo}">
        &nbsp;
        <label>Autor:</label>
        <input type="text" name="autor" value="${autor}">
        &nbsp;
        <label>Material:</label>
        <select name="tipo">
            <option value="TODOS">Todos</option>
            <option value="LIBRO"   ${tipo == 'LIBRO'   ? 'selected' : ''}>Libro</option>
            <option value="REVISTA" ${tipo == 'REVISTA' ? 'selected' : ''}>Revista</option>
            <option value="CD"      ${tipo == 'CD'      ? 'selected' : ''}>CD</option>
            <option value="TESIS"   ${tipo == 'TESIS'   ? 'selected' : ''}>Tesis</option>
        </select>
        &nbsp;
        <button type="submit">Buscar</button>
        <a href="consulta">Limpiar</a>
    </form>
    <hr>
    <c:choose>
        <c:when test="${empty documentos}">
            <p>No se encontraron documentos con esos criterios.</p>
        </c:when>
        <c:otherwise>
            <p>Total: <strong>${documentos.size()}</strong> documentos encontrados.</p>

            <table border="1" cellpadding="8" cellspacing="0">
                <thead>
                    <tr>
                        <th>Codigo</th>
                        <th>Titulo</th>
                        <th>Autor</th>
                        <th>Tipo</th>
                        <th>Año</th>
                        <th>Disponibles</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="doc" items="${documentos}">
                        <tr>
                            <td>${doc.codigo}</td>
                            <td>
                                <a href="detalle?id=${doc.id}">${doc.titulo}</a>
                            </td>
                            <td>${doc.autor}</td>
                            <td>${doc.tipo}</td>
                            <td>${doc.anioPublicacion}</td>
                            <td>${doc.disponibles}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    <hr>
    <a href="index.jsp">Volver al inicio</a>
<%@ include file="/footer.jsp" %>