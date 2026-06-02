<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.Prestamo" %>
<%@ page import="modelo.Documento" %>
<%@ page import="dao.PrestamoDAO" %>
<%@ page import="dao.DocumentoDAO" %>
<%@ page import="dao.ConfiguracionDAO" %>

<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioActivo");
    if (usuarioLogueado == null) {
        session.setAttribute("error", "Debes iniciar sesión para acceder.");
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    if (usuarioLogueado.getIdRol() == 1) {
        response.sendRedirect(request.getContextPath() + "/privado/admin.jsp");
        return;
    }

    PrestamoDAO prestamoDAO = new PrestamoDAO();
    DocumentoDAO documentoDAO = new DocumentoDAO();
    ConfiguracionDAO configDAO = new ConfiguracionDAO();

    List<Prestamo> prestamosActivos = prestamoDAO.obtenerPrestamosActivosPorUsuario(usuarioLogueado.getId());
    int maxEjemplares = configDAO.obtenerMaxEjemplares(usuarioLogueado.getIdRol());
    int maxDias = configDAO.obtenerMaxDias(usuarioLogueado.getIdRol());

    boolean tieneMora = false;
    double totalMora = 0.0;
    for (Prestamo p : prestamosActivos) {
        if (p.getDiasRetraso() > 0) {
            tieneMora = true;
            totalMora += p.getMora();
        }
    }

    List<Documento> documentosDisponibles = documentoDAO.listarTodos();
    String prestarIdParam = request.getParameter("prestar_id");
    int preselectedId = -1;
    if (prestarIdParam != null) {
        try {
            preselectedId = Integer.parseInt(prestarIdParam);
        } catch (Exception e) {}
    }

    String exito = (String) session.getAttribute("exito");
    String error = (String) session.getAttribute("error");
    session.removeAttribute("exito");
    session.removeAttribute("error");
%>
<%@ include file="/header.jsp" %>

    <div>
        <h2>Portal de Préstamos - Mis Solicitudes</h2>

        <% if (exito != null) { %>
            <div><%= exito %></div>
        <% } %>
        <% if (error != null) { %>
            <div><%= error %></div>
        <% } %>

        <div>
            <div>
                <h3>Resumen de Usuario</h3>
                <table border="1" cellpadding="8" cellspacing="0">
                    <tr><th>Nombre Completo</th><td><%= usuarioLogueado.getNombre() %> <%= usuarioLogueado.getApellidos() %></td></tr>
                    <tr><th>Tipo de Cuenta</th><td><%= usuarioLogueado.getIdRol() == 2 ? "Profesor" : "Estudiante" %></td></tr>
                    <tr><th>Préstamos activos</th><td><%= prestamosActivos.size() %> / <%= maxEjemplares %> (Límite máximo)</td></tr>
                    <tr><th>Plazo Máximo</th><td><%= maxDias %> días</td></tr>
                    <tr><th>Mora Acumulada</th><td><spantext-danger bold" : "text-success" %>">$<%= String.format("%.2f", totalMora) %></span></td></tr>
                </table>
            </div>

            <div>
                <h3>Solicitar Préstamo</h3>
                <% if (tieneMora) { %>
                    <div>No puedes solicitar préstamos porque tienes mora acumulada. Debe devolver los libros vencidos primero.</div>
                <% } else if (prestamosActivos.size() >= maxEjemplares) { %>
                    <div>Has alcanzado tu límite máximo de préstamos activos (<%= maxEjemplares %> ejemplares). Devuelve algún documento para prestar otro.</div>
                <% } else { %>
                    <form action="${pageContext.request.contextPath}/privado/prestar" method="POST">
                        <div>
                            <label>Seleccionar Material del Catálogo:</label>
                            <select name="idDocumento" required>
                                <option value="" disabled <%= preselectedId == -1 ? "selected" : "" %>>-- Selecciona un documento disponible --</option>
                                <% for (Documento doc : documentosDisponibles) { %>
                                    <% if (doc.getDisponibles() > 0) { %>
                                        <option value="<%= doc.getId() %>" <%= doc.getId() == preselectedId ? "selected" : "" %>>
                                            [<%= doc.getCodigo() %>] <%= doc.getTitulo() %> (<%= doc.getDisponibles() %> disponibles)
                                        </option>
                                    <% } %>
                                <% } %>
                            </select>
                        </div>
                        <div>
                            <label>Días de préstamo (máximo <%= maxDias %> días):</label>
                            <input type="number" name="dias" min="1" max="<%= maxDias %>" value="<%= maxDias %>" required>
                        </div>
                        <button type="submit">Confirmar Solicitud</button>
                    </form>
                <% } %>
            </div>
        </div>

        <div>
            <h3>Mis Préstamos Activos</h3>
            <div>
                <table border="1" cellpadding="8" cellspacing="0">
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Documento</th>
                            <th>Fecha Préstamo</th>
                            <th>Fecha Vencimiento</th>
                            <th>Estado</th>
                            <th>Retraso</th>
                            <th>Mora Estimada</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (prestamosActivos.isEmpty()) { %>
                            <tr><td colspan="7">No tienes préstamos activos en este momento.</td></tr>
                        <% } else { %>
                            <% for (Prestamo p : prestamosActivos) { %>
                                <tr>
                                    <td><%= p.getCodigoDocumento() %></td>
                                    <td><strong><%= p.getTituloDocumento() %></strong></td>
                                    <td><%= p.getFechaPrestamo().toString() %></td>
                                    <td><%= p.getFechaDevolucion().toString() %></td>
                                    <td>
                                        <span><%= p.getEstado() %></span>
                                    </td>
                                    <td>
                                        <% if (p.getDiasRetraso() > 0) { %>
                                            <span><%= p.getDiasRetraso() %> días</span>
                                        <% } else { %>
                                            <span>Al día</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <spantext-danger bold" : "" %>">
                                            $<%= String.format("%.2f", p.getMora()) %>
                                        </span>
                                    </td>
                                </tr>
                            <% } %>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

<%@ include file="/footer.jsp" %>
