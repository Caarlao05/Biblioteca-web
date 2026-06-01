package controlador;

import dao.PrestamoDAO;
import modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/privado/devolver")
public class DevolverServlet extends HttpServlet {
    private PrestamoDAO prestamoDAO = new PrestamoDAO();

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

        try {
            int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
            int idDocumento = Integer.parseInt(request.getParameter("idDocumento"));

            prestamoDAO.registrarDevolucion(idPrestamo, idDocumento);
            
            session.setAttribute("exito", "Devolución física registrada exitosamente. Se ha devuelto la copia al inventario y se liquidó la mora (si aplicaba).");
            
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Parámetros de devolución inválidos.");
        } catch (Exception e) {
            session.setAttribute("error", "Error al procesar la devolución: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/privado/admin.jsp");
    }
}
