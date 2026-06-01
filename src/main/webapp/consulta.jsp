<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/header.jsp" %>

<div style="border-bottom: 1px solid #ccc; margin-bottom: 10px;">
    <h3 style="margin: 0 0 5px 0; font-weight: normal; font-size: 18px;">Consulta al catálogo</h3>
</div>
<p style="font-size: 12px;">Ingrese su consulta y presione [Iniciar consulta]</p>

<form action="consulta" method="POST" style="font-size: 12px; margin-left: 50px;">
    <table border="0" cellpadding="5" cellspacing="0" style="font-size: 12px;">
        <tr>
            <td align="right"><b>Titulo:</b></td>
            <td><input type="text" name="titulo" value="${titulo}" style="width: 300px; border: 1px solid #999; background-color: #f8f8f8;"></td>
        </tr>
        <tr>
            <td align="right"><b>Autor(es):</b></td>
            <td><input type="text" name="autor" value="${autor}" style="width: 300px; border: 1px solid #999; background-color: #f8f8f8;"></td>
        </tr>
        <tr><td colspan="2"><hr style="border:0; border-top: 1px solid #ccc;"></td></tr>
        <tr>
            <td align="right"><b>Material:</b></td>
            <td>
                <select name="tipo" style="border: 1px solid #999; background-color: #f8f8f8;">
                    <option value="TODOS">Todos</option>
                    <option value="LIBRO"   ${tipo == 'LIBRO'   ? 'selected' : ''}>Libro</option>
                    <option value="REVISTA" ${tipo == 'REVISTA' ? 'selected' : ''}>Revista</option>
                    <option value="CD"      ${tipo == 'CD'      ? 'selected' : ''}>CD</option>
                    <option value="TESIS"   ${tipo == 'TESIS'   ? 'selected' : ''}>Tesis</option>
                </select>
            </td>
        </tr>
    </table>
    
    <div style="margin-top: 10px; margin-left: 320px;">
        <button type="submit" style="background-color: #4285f4; color: white; border: none; padding: 6px 15px; font-weight: bold; cursor: pointer;">Iniciar consulta</button>
    </div>
</form>

<c:choose>
    <c:when test="${not empty documentos}">
        <div style="background-color: #f9f9f9; padding: 5px; margin-top: 30px; border: 1px solid #ddd;">
            <span style="background-color: #cddc39; width: 10px; display: inline-block;">&nbsp;</span>
            <b style="color: #999;">RESULTADOS</b> 1 a ${documentos.size()}
        </div>
        <p style="font-size: 11px;">Página 1 de 1</p>
        
        <div style="margin-left: 50px; font-size: 12px; margin-top: 20px;">
            <c:forEach var="doc" items="${documentos}" varStatus="status">
                <p style="margin-bottom: 20px;">
                    <b>${status.count}. <a href="detalle?id=${doc.id}" style="color: #1a0dab; text-decoration: none;">${doc.titulo}</a></b><br>
                    ${doc.autor}<br><br>
                    ${doc.codigo} ${doc.anioPublicacion}
                </p>
            </c:forEach>
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${param.titulo != null or param.autor != null}">
            <p style="font-size: 12px; margin-top: 30px;">No se encontraron documentos con esos criterios.</p>
        </c:if>
    </c:otherwise>
</c:choose>

<%@ include file="/footer.jsp" %>
