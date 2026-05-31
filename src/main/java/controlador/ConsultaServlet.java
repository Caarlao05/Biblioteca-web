package controlador;

import dao.DocumentoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import modelo.Documento;

@WebServlet("/consulta")
public class ConsultaServlet extends HttpServlet {

    // con get, muestra el formulario vacio con los documentos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DocumentoDAO dao = new DocumentoDAO();
        List<Documento> documentos = dao.listarTodos();
        request.setAttribute("documentos", documentos);
        request.getRequestDispatcher("consulta.jsp").forward(request, response);
    }
    // el usuario envió el formulario con filtros, post
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //lee filtros del formulario
        String titulo = request.getParameter("titulo");
        String autor  = request.getParameter("autor");
        String tipo   = request.getParameter("tipo");
        String idioma = request.getParameter("idioma");

        // busca con filtros
        DocumentoDAO dao = new DocumentoDAO();
        List<Documento> documentos = dao.buscar(titulo, autor, tipo, idioma);

        // Guardar resultados y filtros en el request
        request.setAttribute("documentos", documentos);
        request.setAttribute("titulo", titulo);
        request.setAttribute("autor", autor);
        request.setAttribute("tipo", tipo);

        request.getRequestDispatcher("consulta.jsp").forward(request, response);
    }
}