/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 19/05/2023
 * Descripción: POJO de Actividad para modelar su información
 */

package javafxsastr.modelos.pojo;

public class Actividad {
    
    private int idActividad;
    private String nombreActividad;
    private String detallesActividad;
    private String fechaInicioActividad;
    private String fechaFinActividad;
    private String horaInicioActividad;
    private String horaFinActividad;
    private String fechaCreaciónActividad;
    private int idAnteproyecto;
    private int idEstadoActividad;
    private int idEstudiante;
    private String estadoActividad;

    public Actividad() {
    }

    public Actividad(int idActividad, String nombreActividad, String detallesActividad, String fechaInicioActividad, 
            String fechaFinActividad, String horaInicioActividad, String horaFinActividad, String fechaCreaciónActividad,
            int idAnteproyecto, int idEstadoActividad, int idEstudiante, String estadoActividad) {
        this.idActividad = idActividad;
        this.nombreActividad = nombreActividad;
        this.detallesActividad = detallesActividad;
        this.fechaInicioActividad = fechaInicioActividad;
        this.fechaFinActividad = fechaFinActividad;
        this.horaInicioActividad = horaInicioActividad;
        this.horaFinActividad = horaFinActividad;
        this.fechaCreaciónActividad = fechaCreaciónActividad;
        this.idAnteproyecto = idAnteproyecto;
        this.idEstadoActividad = idEstadoActividad;
        this.idEstudiante = idEstudiante;
        this.estadoActividad = estadoActividad;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }
    
    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getDetallesActividad() {
        return detallesActividad;
    }

    public void setDetallesActividad(String detallesActividad) {
        this.detallesActividad = detallesActividad;
    }

    public String getFechaInicioActividad() {
        return fechaInicioActividad;
    }

    public void setFechaInicioActividad(String fechaInicioActividad) {
        this.fechaInicioActividad = fechaInicioActividad;
    }

    public String getFechaFinActividad() {
        return fechaFinActividad;
    }

    public void setFechaFinActividad(String fechaFinActividad) {
        this.fechaFinActividad = fechaFinActividad;
    }

    public String getHoraInicioActividad() {
        return horaInicioActividad;
    }

    public void setHoraInicioActividad(String horaInicioActividad) {
        this.horaInicioActividad = horaInicioActividad;
    }

    public String getHoraFinActividad() {
        return horaFinActividad;
    }

    public void setHoraFinActividad(String horaFinActividad) {
        this.horaFinActividad = horaFinActividad;
    }

    public String getFechaCreaciónActividad() {
        return fechaCreaciónActividad;
    }

    public void setFechaCreaciónActividad(String fechaCreaciónActividad) {
        this.fechaCreaciónActividad = fechaCreaciónActividad;
    }

    public int getIdAnteproyecto() {
        return idAnteproyecto;
    }

    public void setIdAnteproyecto(int idAnteproyecto) {
        this.idAnteproyecto = idAnteproyecto;
    }

    public int getIdEstadoActividad() {
        return idEstadoActividad;
    }

    public void setIdEstadoActividad(int idEstadoActividad) {
        this.idEstadoActividad = idEstadoActividad;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(String estadoActividad) {
        this.estadoActividad = estadoActividad;
    } 

}
