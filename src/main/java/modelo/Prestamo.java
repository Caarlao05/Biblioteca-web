package modelo;

import java.util.Date;

public class Prestamo {
    private int id;
    private int idUsuario;
    private int idDocumento;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private String estado;
    private double mora;

    private String tituloDocumento;
    private String codigoDocumento;
    private String nombreUsuario;
    private int diasRetraso;

    public Prestamo() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdDocumento() { return idDocumento; }
    public void setIdDocumento(int idDocumento) { this.idDocumento = idDocumento; }
    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getMora() { return mora; }
    public void setMora(double mora) { this.mora = mora; }

    public String getTituloDocumento() { return tituloDocumento; }
    public void setTituloDocumento(String tituloDocumento) { this.tituloDocumento = tituloDocumento; }
    public String getCodigoDocumento() { return codigoDocumento; }
    public void setCodigoDocumento(String codigoDocumento) { this.codigoDocumento = codigoDocumento; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public int getDiasRetraso() { return diasRetraso; }
    public void setDiasRetraso(int diasRetraso) { this.diasRetraso = diasRetraso; }
}
