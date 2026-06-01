package modelo;

public class CD extends Documento {
    private String genero;           // Tipo de contenido (Educativo, Musical, Software, etc.)
    private int duracion;            // Cuanto dura en minutos
    private String formato;          // En que formato esta (CD-ROM, DVD, Blu-ray, USB)
    private String contenido;        // Descripcion de lo que contiene el disco
    private String sistemaRequerido; // Que se necesita para reproducirlo (ej: Lector de CD/DVD)

    @Override
    public String verDetalles() {
        return "CD - Género: " + genero + ", Duración: " + duracion + " mins" +
               ", Formato: " + formato + ", Contenido: " + contenido +
               ", Requisitos: " + sistemaRequerido;
    }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getSistemaRequerido() { return sistemaRequerido; }
    public void setSistemaRequerido(String sistemaRequerido) { this.sistemaRequerido = sistemaRequerido; }
}
