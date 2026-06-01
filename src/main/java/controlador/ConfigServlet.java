package controlador;

import dao.ConfiguracionDAO;
import modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/privado/configurar")
public class ConfigServlet extends HttpServlet {
    private ConfiguracionDAO configDAO = new ConfiguracionDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioActivo");
        if (usuarioLogueado == null || usuarioLogueado.getIdRol() != 1) {
            session.setAttribute("error", "Acceso denegado. Se requieren permisos de administrador.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            session.setAttribute("error", "Acción no especificada.");
            response.sendRedirect(request.getContextPath() + "/privado/admin.jsp");
            return;
        }

        try {
            if ("limites".equalsIgnoreCase(accion)) {
                int idRol = Integer.parseInt(request.getParameter("idRol"));
                int maxEjemplares = Integer.parseInt(request.getParameter("maxEjemplares"));
                int maxDias = Integer.parseInt(request.getParameter("maxDias"));

                boolean ok = configDAO.actualizarConfigPrestamo(idRol, maxEjemplares, maxDias);
                if (ok) {
                    session.setAttribute("exito", "Configuración de límites de préstamo actualizada correctamente.");
                } else {
                    session.setAttribute("error", "No se pudo actualizar la configuración de límites.");
                }
                
            } else if ("guardar_mora".equalsIgnoreCase(accion)) {
                int anio = Integer.parseInt(request.getParameter("anio"));
                double moraDiaria = Double.parseDouble(request.getParameter("moraDiaria"));

                boolean ok = configDAO.guardarMora(anio, moraDiaria);
                if (ok) {
                    session.setAttribute("exito", "Tarifa de mora diaria para el año " + anio + " guardada correctamente.");
                } else {
                    session.setAttribute("error", "No se pudo guardar la tarifa de mora.");
                }
                
            } else if ("eliminar_mora".equalsIgnoreCase(accion)) {
                int anio = Integer.parseInt(request.getParameter("anio"));

                boolean ok = configDAO.eliminarMora(anio);
                if (ok) {
                    session.setAttribute("exito", "Tarifa de mora del año " + anio + " eliminada correctamente.");
                } else {
                    session.setAttribute("error", "No se pudo eliminar la tarifa de mora.");
                }
            } else {
                session.setAttribute("error", "Acción desconocida.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Datos numéricos inválidos.");
        } catch (Exception e) {
            session.setAttribute("error", "Error al procesar configuración: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/privado/admin.jsp");
    }
}
