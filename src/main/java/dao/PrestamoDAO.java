package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import excepciones.ErrorValidacion;
import modelo.Prestamo;
import utilidades.ManejoErrores;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    
    private ConfiguracionDAO configDAO = new ConfiguracionDAO();
    
    public void registrarPrestamo(Prestamo p, int idRolUsuario) throws ErrorValidacion {
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                String sqlRol = "SELECT id_rol FROM usuarios WHERE id_usuario = ?";
                PreparedStatement psRol = con.prepareStatement(sqlRol);
                psRol.setInt(1, p.getIdUsuario());
                ResultSet rsRol = psRol.executeQuery();
                int rolDestinatario = 0;
                if (rsRol.next()) {
                    rolDestinatario = rsRol.getInt("id_rol");
                } else {
                    throw new ErrorValidacion("El usuario ingresado no existe.");
                }

                int maxDiasPermitidos = configDAO.obtenerMaxDias(rolDestinatario);
                long diffMillis = p.getFechaDevolucion().getTime() - p.getFechaPrestamo().getTime();
                long diffDays = diffMillis / (24L * 60L * 60L * 1000L);
                
                if (rolDestinatario != 1 && diffDays > maxDiasPermitidos) {
                    throw new ErrorValidacion("Este usuario solo puede prestar un máximo de " + maxDiasPermitidos + " días.");
                }

                String sqlMora = "SELECT COUNT(*) AS vencidos FROM prestamos WHERE id_usuario = ? AND estado = 'PRESTADO' AND fecha_devolucion < CURDATE()";
                PreparedStatement psMora = con.prepareStatement(sqlMora);
                psMora.setInt(1, p.getIdUsuario());
                ResultSet rsMora = psMora.executeQuery();
                if (rsMora.next() && rsMora.getInt("vencidos") > 0) {
                    throw new ErrorValidacion("El usuario tiene libros vencidos. Debe devolverlos y pagar mora antes de un nuevo préstamo.");
                }
                
                String sqlLimite = "SELECT COUNT(*) AS activos FROM prestamos WHERE id_usuario = ? AND estado = 'PRESTADO'";
                PreparedStatement psLimite = con.prepareStatement(sqlLimite);
                psLimite.setInt(1, p.getIdUsuario());
                ResultSet rsLimite = psLimite.executeQuery();
                if (rsLimite.next()) {
                    int activos = rsLimite.getInt("activos");
                    int maxPermitido = configDAO.obtenerMaxEjemplares(rolDestinatario);
                    if (rolDestinatario != 1 && activos >= maxPermitido) {
                        throw new ErrorValidacion("El usuario ha alcanzado el límite máximo de préstamos (" + maxPermitido + ").");
                    }
                }
                
                String sqlCheck = "SELECT disponibles FROM documentos WHERE id_documento = ?";
                PreparedStatement psCheck = con.prepareStatement(sqlCheck);
                psCheck.setInt(1, p.getIdDocumento());
                ResultSet rs = psCheck.executeQuery();
                
                if (rs.next()) {
                    int disp = rs.getInt("disponibles");
                    if (disp <= 0) {
                        throw new ErrorValidacion("No hay copias disponibles de este documento.");
                    }
                } else {
                    throw new ErrorValidacion("El documento no existe.");
                }
                
                String sqlInsert = "INSERT INTO prestamos (id_usuario, id_documento, fecha_prestamo, fecha_devolucion, estado) VALUES (?, ?, ?, ?, 'PRESTADO')";
                PreparedStatement psInsert = con.prepareStatement(sqlInsert);
                psInsert.setInt(1, p.getIdUsuario());
                psInsert.setInt(2, p.getIdDocumento());
                psInsert.setDate(3, new java.sql.Date(p.getFechaPrestamo().getTime()));
                psInsert.setDate(4, new java.sql.Date(p.getFechaDevolucion().getTime()));
                psInsert.executeUpdate();
                
                String sqlUpdate = "UPDATE documentos SET disponibles = disponibles - 1 WHERE id_documento = ?";
                PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
                psUpdate.setInt(1, p.getIdDocumento());
                psUpdate.executeUpdate();
                
                con.close();
            }
        } catch (ErrorValidacion ev) {
            throw ev; // se relanza para que la ventana muestre el mensaje
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al prestar: " + e.getMessage());
        }
    }

    public void registrarDevolucion(int idPrestamo, int idDocumento) {
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                String sqlMora = "SELECT fecha_devolucion, DATEDIFF(CURDATE(), fecha_devolucion) AS dias_retraso FROM prestamos WHERE id_prestamo = ?";
                PreparedStatement psMora = con.prepareStatement(sqlMora);
                psMora.setInt(1, idPrestamo);
                ResultSet rsMora = psMora.executeQuery();
                
                double moraCalculada = 0.0;
                if(rsMora.next()) {
                    int diasRetraso = rsMora.getInt("dias_retraso");
                    if(diasRetraso > 0) {
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        int anioActual = cal.get(java.util.Calendar.YEAR);
                        double moraDiaria = configDAO.obtenerMoraDiaria(anioActual);
                        moraCalculada = diasRetraso * moraDiaria;
                    }
                }

                String sqlUpd = "UPDATE prestamos SET estado = 'DEVUELTO', mora = ? WHERE id_prestamo = ?";
                PreparedStatement psUpd = con.prepareStatement(sqlUpd);
                psUpd.setDouble(1, moraCalculada);
                psUpd.setInt(2, idPrestamo);
                psUpd.executeUpdate();

                String sqlInv = "UPDATE documentos SET disponibles = disponibles + 1 WHERE id_documento = ?";
                PreparedStatement psInv = con.prepareStatement(sqlInv);
                psInv.setInt(1, idDocumento);
                psInv.executeUpdate();

                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error en devolución: " + e.getMessage());
        }
    }

    public List<String> listarPrestamos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT p.id_prestamo, u.nombre, u.apellidos, d.titulo, p.fecha_prestamo, p.fecha_devolucion, p.estado, " +
                     "DATEDIFF(CURDATE(), p.fecha_devolucion) as dias_retraso " +
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "JOIN documentos d ON p.id_documento = d.id_documento " +
                     "ORDER BY p.id_prestamo DESC";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    int dias = rs.getInt("dias_retraso");
                    String estado = rs.getString("estado");
                    String moraInfo = (estado.equals("PRESTADO") && dias > 0) ? " | Mora: " + dias + " días de retraso" : "";
                    String info = "ID: " + rs.getInt("id_prestamo") + " | " + rs.getString("nombre") + " " + rs.getString("apellidos") + 
                                  " | Doc: " + rs.getString("titulo") + " | Vence: " + rs.getDate("fecha_devolucion") +
                                  " | " + estado + moraInfo;
                    lista.add(info);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al listar prestamos: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Prestamo> obtenerPrestamosActivosPorUsuario(int idUsuario) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.*, d.titulo, d.codigo, " +
                     "DATEDIFF(CURDATE(), p.fecha_devolucion) AS dias_retraso " +
                     "FROM prestamos p " +
                     "JOIN documentos d ON p.id_documento = d.id_documento " +
                     "WHERE p.id_usuario = ? AND p.estado = 'PRESTADO' " +
                     "ORDER BY p.fecha_prestamo DESC";
        
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Prestamo p = new Prestamo();
                    p.setId(rs.getInt("id_prestamo"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setIdDocumento(rs.getInt("id_documento"));
                    p.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    p.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    p.setEstado(rs.getString("estado"));
                    
                    int dias = rs.getInt("dias_retraso");
                    p.setDiasRetraso(dias > 0 ? dias : 0);
                    
                    double moraAcumulada = rs.getDouble("mora");
                    if (dias > 0) {
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        int anioActual = cal.get(java.util.Calendar.YEAR);
                        double moraDiaria = configDAO.obtenerMoraDiaria(anioActual);
                        moraAcumulada = dias * moraDiaria;
                    }
                    p.setMora(moraAcumulada);
                    
                    p.setTituloDocumento(rs.getString("titulo"));
                    p.setCodigoDocumento(rs.getString("codigo"));
                    
                    lista.add(p);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al obtener prestamos activos por usuario: " + e.getMessage());
        }
        return lista;
    }

    public List<Prestamo> obtenerTodosLosPrestamosDetalle() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.*, d.titulo, d.codigo, CONCAT(u.nombre, ' ', u.apellidos) AS nombre_usuario, " +
                     "DATEDIFF(CURDATE(), p.fecha_devolucion) AS dias_retraso " +
                     "FROM prestamos p " +
                     "JOIN documentos d ON p.id_documento = d.id_documento " +
                     "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "ORDER BY p.id_prestamo DESC";
        
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Prestamo p = new Prestamo();
                    p.setId(rs.getInt("id_prestamo"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setIdDocumento(rs.getInt("id_documento"));
                    p.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    p.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    p.setEstado(rs.getString("estado"));
                    
                    int dias = rs.getInt("dias_retraso");
                    p.setDiasRetraso(dias > 0 && p.getEstado().equals("PRESTADO") ? dias : 0);
                    
                    double moraAcumulada = rs.getDouble("mora");
                    if (dias > 0 && p.getEstado().equals("PRESTADO")) {
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        int anioActual = cal.get(java.util.Calendar.YEAR);
                        double moraDiaria = configDAO.obtenerMoraDiaria(anioActual);
                        moraAcumulada = dias * moraDiaria;
                    }
                    p.setMora(moraAcumulada);
                    
                    p.setTituloDocumento(rs.getString("titulo"));
                    p.setCodigoDocumento(rs.getString("codigo"));
                    p.setNombreUsuario(rs.getString("nombre_usuario"));
                    
                    lista.add(p);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al obtener todos los préstamos detallados: " + e.getMessage());
        }
        return lista;
    }
}
