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
        // leerá el ID que viene en la URL
        String idParam = request.getParameter("id");
        // valida que el ID llegó y sea un número
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("consulta");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            //busca doc en la BD
            DocumentoDAO dao = new DocumentoDAO();
            Documento doc = dao.buscarPorId(id);

            if (doc == null) {
                //si no existe el doc, redirecciona al catálogo
                response.sendRedirect("consulta");
                return;
            }

            // Guardar el documento en el request y mostrar el detalle
            request.setAttribute("doc", doc);
            request.getRequestDispatcher("detalle.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Si el ID no es un numero valido, regresar al catalogo
            response.sendRedirect("consulta");
        }
    }
}