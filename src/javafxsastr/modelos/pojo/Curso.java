/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 19/05/2023
 * Descripción: POJO de Curso para modelar su información
 */

package javafxsastr.modelos.pojo;

public class Curso {
    
    private int idCurso;
    private String nombreCurso;
    private String fechaInicioCurso;
    private String fechaFinCurso;
    private int idSeccion;
    private int idBloque;
    private int idExperienciaEducativa;
    private int idNRC;
    private int idPeriodoEscolar;
    private int idEstadoCurso;
    private int idAcademico;
    private String seccionCurso;
    private String bloqueCurso;
    private String experienciaEducativaCurso;
    private String nrcCurso;
    private String inicioPeriodoEscolar;
    private String finPeriodoEscolar;
    private String estadoCurso;
    private String academicoCurso;

    public Curso() {
    }

    public Curso(int idCurso, String nombreCurso, String fechaInicioCurso, String fechaFinCurso, int idSeccion, int idBloque,
            int idExperienciaEducativa, int idNRC, int idPeriodoEscolar, int idEstadoCurso, int idAcademico, String seccionCurso,
            String bloqueCurso, String experienciaEducativaCurso, String nrcCurso, String inicioPeriodoEscolar, String finPeriodoEscolar,
            String estadoCurso, String academicoCurso) {
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
        this.fechaInicioCurso = fechaInicioCurso;
        this.fechaFinCurso = fechaFinCurso;
        this.idSeccion = idSeccion;
        this.idBloque = idBloque;
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.idNRC = idNRC;
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.idEstadoCurso = idEstadoCurso;
        this.idAcademico = idAcademico;
        this.seccionCurso = seccionCurso;
        this.bloqueCurso = bloqueCurso;
        this.experienciaEducativaCurso = experienciaEducativaCurso;
        this.nrcCurso = nrcCurso;
        this.inicioPeriodoEscolar = inicioPeriodoEscolar;
        this.finPeriodoEscolar = finPeriodoEscolar;
        this.estadoCurso = estadoCurso;
        this.academicoCurso = academicoCurso;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getFechaInicioCurso() {
        return fechaInicioCurso;
    }

    public void setFechaInicioCurso(String fechaInicioCurso) {
        this.fechaInicioCurso = fechaInicioCurso;
    }

    public String getFechaFinCurso() {
        return fechaFinCurso;
    }

    public void setFechaFinCurso(String fechaFinCurso) {
        this.fechaFinCurso = fechaFinCurso;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdBloque() {
        return idBloque;
    }

    public void setIdBloque(int idBloque) {
        this.idBloque = idBloque;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public int getIdNRC() {
        return idNRC;
    }

    public void setIdNRC(int idNRC) {
        this.idNRC = idNRC;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public int getIdEstadoCurso() {
        return idEstadoCurso;
    }

    public void setIdEstadoCurso(int idEstadoCurso) {
        this.idEstadoCurso = idEstadoCurso;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getSeccionCurso() {
        return seccionCurso;
    }

    public void setSeccionCurso(String seccionCurso) {
        this.seccionCurso = seccionCurso;
    }

    public String getBloqueCurso() {
        return bloqueCurso;
    }

    public void setBloqueCurso(String bloqueCurso) {
        this.bloqueCurso = bloqueCurso;
    }

    public String getExperienciaEducativaCurso() {
        return experienciaEducativaCurso;
    }

    public void setExperienciaEducativaCurso(String experienciaEducativaCurso) {
        this.experienciaEducativaCurso = experienciaEducativaCurso;
    }

    public String getNrcCurso() {
        return nrcCurso;
    }

    public void setNrcCurso(String nrcCurso) {
        this.nrcCurso = nrcCurso;
    }

    public String getInicioPeriodoEscolar() {
        return inicioPeriodoEscolar;
    }

    public void setInicioPeriodoEscolar(String inicioPeriodoEscolar) {
        this.inicioPeriodoEscolar = inicioPeriodoEscolar;
    }

    public String getFinPeriodoEscolar() {
        return finPeriodoEscolar;
    }

    public void setFinPeriodoEscolar(String finPeriodoEscolar) {
        this.finPeriodoEscolar = finPeriodoEscolar;
    }

    public String getEstadoCurso() {
        return estadoCurso;
    }

    public void setEstadoCurso(String estadoCurso) {
        this.estadoCurso = estadoCurso;
    }

    public String getAcademicoCurso() {
        return academicoCurso;
    }

    public void setAcademicoCurso(String academicoCurso) {
        this.academicoCurso = academicoCurso;
    }

}