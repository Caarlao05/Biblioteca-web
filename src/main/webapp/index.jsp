<%@ include file="/header.jsp" %>
    <h2>Bienvenido al Sistema de Biblioteca</h2>
    <p>Selecciona una opcion para continuar:</p>
    <ul>
        <li>
            <a href="${pageContext.request.contextPath}/consulta">Consultar catalogo</a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/login">Iniciar sesion</a>
        </li>
    </ul>
<%@ include file="/footer.jsp" %>