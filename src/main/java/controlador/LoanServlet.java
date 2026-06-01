package controlador;

import dao.PrestamoDAO;
import modelo.Prestamo;
import modelo.Usuario;
import excepciones.ErrorValidacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebServlet("/privado/prestar")
public class LoanServlet extends HttpServlet {
    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioActivo");
        if (usuarioLogueado == null || usuarioLogueado.getIdRol() == 1) {
            session.setAttribute("error", "Acceso no autorizado.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int idDocumento = Integer.parseInt(request.getParameter("idDocumento"));
            int dias = Integer.parseInt(request.getParameter("dias"));

            Prestamo p = new Prestamo();
            p.setIdUsuario(usuarioLogueado.getId());
            p.setIdDocumento(idDocumento);
            
            Date hoy = new Date();
            p.setFechaPrestamo(hoy);
            
            Date fechaDevolucion = new Date(hoy.getTime() + (dias * 24L * 60L * 60L * 1000L));
            p.setFechaDevolucion(fechaDevolucion);
            p.setEstado("PRESTADO");

            prestamoDAO.registrarPrestamo(p, usuarioLogueado.getIdRol());

            session.setAttribute("exito", "¡Préstamo registrado correctamente! Fecha límite de devolución: " + fechaDevolucion.toString());
            
        } catch (ErrorValidacion ev) {
            session.setAttribute("error", ev.getMessage());
        } catch (NumberFormatException nfe) {
            session.setAttribute("error", "Datos ingresados inválidos.");
        } catch (Exception e) {
            session.setAttribute("error", "Error inesperado al registrar el préstamo: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/privado/prestamos.jsp");
    }
}
