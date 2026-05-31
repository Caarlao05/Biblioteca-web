package controlador;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // obtiene sesion actual sin crear otra
        HttpSession session = request.getSession(false);
        // si existe, se destruye
        if (session != null) {
            session.invalidate();
        }
        // Redirecciona al login
        response.sendRedirect("login");
    }
}