/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase POJO de el Historial de cambios
 */

package javafxsastr.modelos.pojo;


public class HistorialCambios {
    
    private int idHistorialCambio;
    private String fechaDeModificacion;
    private String fechaAnterior;
    private String horaAnterior;
    private String fechaNueva;
    private String horaNueva;
    private int idActividad;

    public HistorialCambios() {
    }

    public HistorialCambios(int idHistorialCambio, String fechaDeModificacion, String fechaAnterior,
                        String horaAnterior, String fechaNueva, String horaNueva, int idActividad) {
        this.idHistorialCambio = idHistorialCambio;
        this.fechaDeModificacion = fechaDeModificacion;
        this.fechaAnterior = fechaAnterior;
        this.horaAnterior = horaAnterior;
        this.fechaNueva = fechaNueva;
        this.horaNueva = horaNueva;
        this.idActividad = idActividad;
    }

    public int getIdHistorialCambio() {
        return idHistorialCambio;
    }

    public void setIdHistorialCambio(int idHistorialCambio) {
        this.idHistorialCambio = idHistorialCambio;
    }

    public String getFechaDeModificacion() {
        return fechaDeModificacion;
    }

    public void setFechaDeModificacion(String fechaDeModificacion) {
        this.fechaDeModificacion = fechaDeModificacion;
    }

    public String getFechaAnterior() {
        return fechaAnterior;
    }

    public void setFechaAnterior(String fechaAnterior) {
        this.fechaAnterior = fechaAnterior;
    }

    public String getHoraAnterior() {
        return horaAnterior;
    }

    public void setHoraAnterior(String horaAnterior) {
        this.horaAnterior = horaAnterior;
    }

    public String getFechaNueva() {
        return fechaNueva;
    }

    public void setFechaNueva(String fechaNueva) {
        this.fechaNueva = fechaNueva;
    }

    public String getHoraNueva() {
        return horaNueva;
    }

    public void setHoraNueva(String horaNueva) {
        this.horaNueva = horaNueva;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

}