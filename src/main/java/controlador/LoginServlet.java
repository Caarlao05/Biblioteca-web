package controlador;

import dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import java.io.IOException;
import modelo.Usuario;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioActivo") != null) {
            response.sendRedirect("index.jsp");
            return;
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo   = request.getParameter("correo");
        String password = request.getParameter("password");
        if (correo == null || correo.isEmpty() ||
                password == null || password.isEmpty()) {
            request.setAttribute("error", "Debe ingresar correo y contraseña.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.login(correo, password);
        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioActivo", usuario);
            response.sendRedirect(request.getContextPath() + "/privado/inicio.jsp");;
        } else {
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}