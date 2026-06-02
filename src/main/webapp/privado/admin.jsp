<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.Prestamo" %>
<%@ page import="dao.PrestamoDAO" %>
<%@ page import="dao.ConfiguracionDAO" %>

<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioActivo");
    if (usuarioLogueado == null) {
        session.setAttribute("error", "Debes iniciar sesión para acceder.");
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    if (usuarioLogueado.getIdRol() != 1) {
        session.setAttribute("error", "Acceso denegado. Se requiere rol de Administrador.");
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    PrestamoDAO prestamoDAO = new PrestamoDAO();
    ConfiguracionDAO configDAO = new ConfiguracionDAO();

    List<Prestamo> todosPrestamos = prestamoDAO.obtenerTodosLosPrestamosDetalle();
    List<String[]> configPrestamos = configDAO.listarConfigPrestamos();
    List<String[]> listadoMoras = configDAO.listarMoras();

    String exito = (String) session.getAttribute("exito");
    String error = (String) session.getAttribute("error");
    session.removeAttribute("exito");
    session.removeAttribute("error");
%>
<%@ include file="/header.jsp" %>

    <div>
        <h2>Panel de Administración - Biblioteca</h2>
        
        <% if (exito != null) { %>
            <div><%= exito %></div>
        <% } %>
        <% if (error != null) { %>
            <div><%= error %></div>
        <% } %>

        <div>
            <h3>Historial y Devolución de Préstamos</h3>
            <div>
                <table border="1" cellpadding="8" cellspacing="0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Usuario</th>
                            <th>Documento</th>
                            <th>Fecha Préstamo</th>
                            <th>Fecha Vencimiento</th>
                            <th>Estado</th>
                            <th>Mora</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (todosPrestamos.isEmpty()) { %>
                            <tr><td colspan="8">No hay registros de préstamos.</td></tr>
                        <% } else { %>
                            <% for (Prestamo p : todosPrestamos) { %>
                                <tr>
                                    <td>#<%= p.getId() %></td>
                                    <td><%= p.getNombreUsuario() %></td>
                                    <td>[<%= p.getCodigoDocumento() %>] <%= p.getTituloDocumento() %></td>
                                    <td><%= p.getFechaPrestamo().toString() %></td>
                                    <td><%= p.getFechaDevolucion().toString() %></td>
                                    <td>
                                        <span>
                                            <%= p.getEstado() %>
                                        </span>
                                    </td>
                                    <td>$<%= String.format("%.2f", p.getMora()) %></td>
                                    <td>
                                        <% if (p.getEstado().equals("PRESTADO")) { %>
                                            <form action="${pageContext.request.contextPath}/privado/devolver" method="POST" onsubmit="return confirm('¿Registrar la devolución física de este material?');">
                                                <input type="hidden" name="idPrestamo" value="<%= p.getId() %>">
                                                <input type="hidden" name="idDocumento" value="<%= p.getIdDocumento() %>">
                                                <button type="submit">Devolver</button>
                                            </form>
                                        <% } else { %>
                                            <span>Devuelto</span>
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <div>
            <div>
                <h3>Límites de Préstamo por Rol</h3>
                <table border="1" cellpadding="8" cellspacing="0">
                    <thead>
                        <tr><th>Rol</th><th>Límite Libros</th><th>Límite Días</th></tr>
                    </thead>
                    <tbody>
                        <% for (String[] c : configPrestamos) { %>
                            <tr>
                                <td><strong><%= c[0] %></strong></td>
                                <td><%= c[2] %> ejemplares</td>
                                <td><%= c[3] %> días</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>

                <h4>Modificar Límites</h4>
                <form action="${pageContext.request.contextPath}/privado/configurar" method="POST">
                    <input type="hidden" name="accion" value="limites">
                    <div>
                        <label>Rol:</label>
                        <select name="idRol" required>
                            <option value="2">PROFESOR</option>
                            <option value="3">ALUMNO</option>
                        </select>
                    </div>
                    <div>
                        <label>Máx. Libros:</label>
                        <input type="number" name="maxEjemplares" min="1" max="99" value="3" required>
                    </div>
                    <div>
                        <label>Máx. Días:</label>
                        <input type="number" name="maxDias" min="1" max="365" value="7" required>
                    </div>
                    <button type="submit">Actualizar Límites</button>
                </form>
            </div>

            <div>
                <h3>Tarifas de Mora Diaria</h3>
                <table border="1" cellpadding="8" cellspacing="0">
                    <thead>
                        <tr><th>Año</th><th>Mora por Día</th><th>Acción</th></tr>
                    </thead>
                    <tbody>
                        <% if (listadoMoras.isEmpty()) { %>
                            <tr><td colspan="3">No hay tarifas configuradas.</td></tr>
                        <% } else { %>
                            <% for (String[] m : listadoMoras) { %>
                                <tr>
                                    <td><%= m[0] %></td>
                                    <td>$<%= String.format("%.2f", Double.parseDouble(m[1])) %></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/privado/configurar" method="POST" onsubmit="return confirm('¿Eliminar tarifa del año <%= m[0] %>?');">
                                            <input type="hidden" name="accion" value="eliminar_mora">
                                            <input type="hidden" name="anio" value="<%= m[0] %>">
                                            <button type="submit">Eliminar</button>
                                        </form>
                                    </td>
                                </tr>
                            <% } %>
                        <% } %>
                    </tbody>
                </table>

                <h4>Establecer Tarifa</h4>
                <form action="${pageContext.request.contextPath}/privado/configurar" method="POST">
                    <input type="hidden" name="accion" value="guardar_mora">
                    <div>
                        <label>Año:</label>
                        <input type="number" name="anio" min="2020" max="2100" value="2026" required>
                    </div>
                    <div>
                        <label>Mora Diaria (USD):</label>
                        <input type="number" name="moraDiaria" min="0.01" max="10.00" step="0.01" value="0.50" required>
                    </div>
                    <button type="submit">Guardar Tarifa</button>
                </form>
            </div>
        </div>
    </div>

<%@ include file="/footer.jsp" %>
