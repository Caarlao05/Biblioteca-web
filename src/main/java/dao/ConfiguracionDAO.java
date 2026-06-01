package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utilidades.ManejoErrores;

public class ConfiguracionDAO {


    public double obtenerMoraDiaria(int anio) {
        double mora = 0.50;
        String sql = "SELECT mora_diaria FROM configuracion_mora WHERE anio = ?";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, anio);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    mora = rs.getDouble("mora_diaria");
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al obtener mora diaria: " + e.getMessage());
        }
        return mora;
    }

    public List<String[]> listarMoras() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT anio, mora_diaria FROM configuracion_mora ORDER BY anio";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String[] fila = new String[2];
                    fila[0] = String.valueOf(rs.getInt("anio"));
                    fila[1] = String.valueOf(rs.getDouble("mora_diaria"));
                    lista.add(fila);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al listar moras: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardarMora(int anio, double moraDiaria) {
        String sql = "INSERT INTO configuracion_mora (anio, mora_diaria) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE mora_diaria = ?";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, anio);
                ps.setDouble(2, moraDiaria);
                ps.setDouble(3, moraDiaria);
                ps.executeUpdate();
                con.close();
                return true;
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al guardar mora: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarMora(int anio) {
        String sql = "DELETE FROM configuracion_mora WHERE anio = ?";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, anio);
                ps.executeUpdate();
                con.close();
                return true;
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al eliminar mora: " + e.getMessage());
        }
        return false;
    }


    public int obtenerMaxEjemplares(int idRol) {
        int max = 3;
        String sql = "SELECT max_ejemplares FROM configuracion_prestamos WHERE id_rol = ?";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idRol);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    max = rs.getInt("max_ejemplares");
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al obtener max ejemplares: " + e.getMessage());
        }
        return max;
    }

    public int obtenerMaxDias(int idRol) {
        int max = 7;
        String sql = "SELECT max_dias FROM configuracion_prestamos WHERE id_rol = ?";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idRol);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    max = rs.getInt("max_dias");
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al obtener max dias: " + e.getMessage());
        }
        return max;
    }

    public List<String[]> listarConfigPrestamos() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT r.nombre, cp.id_rol, cp.max_ejemplares, cp.max_dias " +
                     "FROM configuracion_prestamos cp " +
                     "JOIN roles r ON cp.id_rol = r.id_rol " +
                     "ORDER BY cp.id_rol";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String[] fila = new String[4];
                    fila[0] = rs.getString("nombre");
                    fila[1] = String.valueOf(rs.getInt("id_rol"));
                    fila[2] = String.valueOf(rs.getInt("max_ejemplares"));
                    fila[3] = String.valueOf(rs.getInt("max_dias"));
                    lista.add(fila);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al listar config prestamos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarConfigPrestamo(int idRol, int maxEjemplares, int maxDias) {
        String sql = "INSERT INTO configuracion_prestamos (id_rol, max_ejemplares, max_dias) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE max_ejemplares = ?, max_dias = ?";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idRol);
                ps.setInt(2, maxEjemplares);
                ps.setInt(3, maxDias);
                ps.setInt(4, maxEjemplares);
                ps.setInt(5, maxDias);
                ps.executeUpdate();
                con.close();
                return true;
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al actualizar config prestamos: " + e.getMessage());
        }
        return false;
    }
}
