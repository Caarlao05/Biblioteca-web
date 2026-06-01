package controlador;

import dao.DocumentoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import modelo.Documento;

@WebServlet("/detalle")
public class DetalleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("consulta");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            DocumentoDAO dao = new DocumentoDAO();
            Documento doc = dao.buscarPorId(id);

            if (doc == null) {
                response.sendRedirect("consulta");
                return;
            }

            request.setAttribute("doc", doc);
            request.getRequestDispatcher("detalle.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("consulta");
        }
    }
}