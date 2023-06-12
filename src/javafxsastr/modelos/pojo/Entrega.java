/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase pojo de las Entregas
 */

package javafxsastr.modelos.pojo;

public class Entrega {
    
    private int idEntrega;
    private String comentarioAlumno;
    private String fechaEntrega;
    private String horaEntrega;
    private String comentarioDirector;
    private String fechaRevision;
    private String horaRevision;
    private int idActividad;
    private int idAcademico;
    private Archivo[] archivos;

    public Entrega() {
    }
    
    public Entrega(int idEntrega, String comentarioAlumno, String fechaEntrega, String horaEntrega, String comentarioDirector, String fechaRevision, String horaRevision, int idActividad, int idAcademico) {
        this.idEntrega = idEntrega;
        this.comentarioAlumno = comentarioAlumno;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.comentarioDirector = comentarioDirector;
        this.fechaRevision = fechaRevision;
        this.horaRevision = horaRevision;
        this.idActividad = idActividad;
        this.idAcademico = idAcademico;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getComentarioAlumno() {
        return comentarioAlumno;
    }

    public void setComentarioAlumno(String comentarioAlumno) {
        this.comentarioAlumno = comentarioAlumno;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getComentarioDirector() {
        return comentarioDirector;
    }

    public void setComentarioDirector(String comentarioDirector) {
        this.comentarioDirector = comentarioDirector;
    }

    public String getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(String fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getHoraRevision() {
        return horaRevision;
    }

    public void setHoraRevision(String horaRevision) {
        this.horaRevision = horaRevision;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
    }
    
}
