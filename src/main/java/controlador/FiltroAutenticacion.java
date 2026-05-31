package controlador;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Este filtro intercepta TODAS las peticiones a /privado/*
// Si el usuario no tiene sesion activa, lo manda al login
@WebFilter("/privado/*")
public class FiltroAutenticacion implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // se ejecuta cuando tomcat carga el filtro
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // convierte a http para usar sesiones y redirecciones
        HttpServletRequest request   = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // corrobora si hay sesion activa
        HttpSession session = request.getSession(false);
        boolean autenticado = (session != null &&
                session.getAttribute("usuarioActivo") != null);
        if (autenticado) {
            // si tiene sesion deja pasar al servlet/jsp
            chain.doFilter(req, res);
        } else {
            // no tiene sesion, manda a login
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
    @Override
    public void destroy() {
    }
}