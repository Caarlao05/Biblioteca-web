<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/header.jsp" %>
    <meta charset="UTF-8">
    <title>Iniciar sesion - Biblioteca</title>
</head>
<body>
    <h1>Biblioteca Colegio Amigos de Don Bosco</h1>
    <h2>Iniciar sesion</h2>
    <hr>
    <%-- Muestra mensaje de error si existe --%>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    <form action="login" method="POST">
        <label>Correo:</label><br>
        <input type="email" name="correo" required><br><br>
        <label>Contraseña:</label><br>
        <input type="password" name="password" required><br><br>
        <button type="submit">Entrar</button>
    </form>
    <hr>
    <a href="consulta">Ver catalogo sin iniciar sesion</a>
<%@ include file="/footer.jsp" %>