/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 19/05/2023
 * Descripción: POJO de Desasignacion de un anteproyecto para modelar su información
 */

package javafxsastr.modelos.pojo;

public class Desasignacion {
    
    private int idDesasignacion;
    private String motivo;
    private String comentarios;
    private int idEstudiante;
    private int idAnteproyecto;
    private String nombreEstudiante;
    private String nombreAnteproyecto;

    public Desasignacion() {
    }

    public Desasignacion(int idDesasignacion, String motivo, String comentarios, int idEstudiante, int idAnteproyecto, String nombreEstudiante, String nombreAnteproyecto) {
        this.idDesasignacion = idDesasignacion;
        this.motivo = motivo;
        this.comentarios = comentarios;
        this.idEstudiante = idEstudiante;
        this.idAnteproyecto = idAnteproyecto;
        this.nombreEstudiante = nombreEstudiante;
        this.nombreAnteproyecto = nombreAnteproyecto;
    }

    public int getIdDesasignacion() {
        return idDesasignacion;
    }

    public void setIdDesasignacion(int idDesasignacion) {
        this.idDesasignacion = idDesasignacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdAnteproyecto() {
        return idAnteproyecto;
    }

    public void setIdAnteproyecto(int idAnteproyecto) {
        this.idAnteproyecto = idAnteproyecto;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreAnteproyecto() {
        return nombreAnteproyecto;
    }

    public void setNombreAnteproyecto(String nombreAnteproyecto) {
        this.nombreAnteproyecto = nombreAnteproyecto;
    }
    
}
