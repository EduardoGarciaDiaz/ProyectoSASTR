/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase POJO de el Historial de cambios
 */
package javafxsastr.modelos.pojo;

import java.sql.Date;
import java.sql.Time;

public class HistorialCambios {
    
    private int idHistorialCambio;
    private Date fechaDeModificacion;
    private Date fechaAnterior;
    private Time horaAnterior;
    private Date fechaNueva;
    private Time horaNueva;
    private int idActividad;

    public HistorialCambios() {
    }

    public HistorialCambios(int idHistorialCambio, Date fechaDeModificacion, Date fechaAnterior, Time horaAnterior, Date fechaNueva, Time horaNueva, int idActividad) {
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

    public Date getFechaDeModificacion() {
        return fechaDeModificacion;
    }

    public void setFechaDeModificacion(Date fechaDeModificacion) {
        this.fechaDeModificacion = fechaDeModificacion;
    }

    public Date getFechaAnterior() {
        return fechaAnterior;
    }

    public void setFechaAnterior(Date fechaAnterior) {
        this.fechaAnterior = fechaAnterior;
    }

    public Time getHoraAnterior() {
        return horaAnterior;
    }

    public void setHoraAnterior(Time horaAnterior) {
        this.horaAnterior = horaAnterior;
    }

    public Date getFechaNueva() {
        return fechaNueva;
    }

    public void setFechaNueva(Date fechaNueva) {
        this.fechaNueva = fechaNueva;
    }

    public Time getHoraNueva() {
        return horaNueva;
    }

    public void setHoraNueva(Time horaNueva) {
        this.horaNueva = horaNueva;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }
    
    
}
