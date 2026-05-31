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

    // muestra el formulario del login con get
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // si hay sesion activa lo manda al inicio
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioActivo") != null) {
            response.sendRedirect("index.jsp");
            return;
        }
        //No hay sesion, muestra el formulario
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    // procesa formulario con post
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo   = request.getParameter("correo");
        String password = request.getParameter("password");
        //valida que no haya vacios
        if (correo == null || correo.isEmpty() ||
                password == null || password.isEmpty()) {
            request.setAttribute("error", "Debe ingresar correo y contraseña.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        // Valida credenciales en el DAO
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.login(correo, password);
        if (usuario != null) {
            // crea sesion y guarda el usuario si las credenciales son correctas
            HttpSession session = request.getSession();
            session.setAttribute("usuarioActivo", usuario);
            //redirecciona al inicio
            response.sendRedirect(request.getContextPath() + "/privado/inicio.jsp");;
        } else {
            // vuelve al formulario si las credenciales son incorrectas
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}