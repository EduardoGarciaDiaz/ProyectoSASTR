/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 12/05/2023
 * Descripción: POJO de Estudiante para modelar su información
 */

package javafxsastr.modelos.pojo;

public class Estudiante extends Usuario{
    
    private int idEstudiante;
    private String matriculaEstudiante;
    private int idAnteproyecto;
    private int idCurso;
    private String anteproyectoEstudiante;
    private String cursoEstudiante;

    public Estudiante() {
    }

    public Estudiante(int idEstudiante, String matriculaEstudiante, int idAnteproyecto, int idCurso, String anteproyectoEstudiante,
            String cursoEstudiante) {
        this.idEstudiante = idEstudiante;
        this.matriculaEstudiante = matriculaEstudiante;
        this.idAnteproyecto = idAnteproyecto;
        this.idCurso = idCurso;
        this.anteproyectoEstudiante = anteproyectoEstudiante;
        this.cursoEstudiante = cursoEstudiante;
    }

    public Estudiante(int idEstudiante, String matriculaEstudiante, int idAnteproyecto, int idCurso, String anteproyectoEstudiante,
            String cursoEstudiante, int idUsuario, String nombre, String primerApellido, String segundoApellido,
            String correoInstitucional, String correoAlterno, String password, boolean esAdministrador, int idEstadoUsuario,
            String estadoUsuario) {
        super(idUsuario, nombre, primerApellido, segundoApellido, correoInstitucional, correoAlterno, password, esAdministrador,
                idEstadoUsuario, estadoUsuario);
        this.idEstudiante = idEstudiante;
        this.matriculaEstudiante = matriculaEstudiante;
        this.idAnteproyecto = idAnteproyecto;
        this.idCurso = idCurso;
        this.anteproyectoEstudiante = anteproyectoEstudiante;
        this.cursoEstudiante = cursoEstudiante;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getMatriculaEstudiante() {
        return matriculaEstudiante;
    }

    public void setMatriculaEstudiante(String matriculaEstudiante) {
        this.matriculaEstudiante = matriculaEstudiante;
    }

    public int getIdAnteproyecto() {
        return idAnteproyecto;
    }

    public void setIdAnteproyecto(int idAnteproyecto) {
        this.idAnteproyecto = idAnteproyecto;
    }

    public String getAnteproyectoEstudiante() {
        return anteproyectoEstudiante;
    }

    public void setAnteproyectoEstudiante(String anteproyectoEstudiante) {
        this.anteproyectoEstudiante = anteproyectoEstudiante;
    }
    
    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getCursoEstudiante() {
        return cursoEstudiante;
    }

    public void setCursoEstudiante(String cursoEstudiante) {
        this.cursoEstudiante = cursoEstudiante;
    }

    @Override
    public String toString() {
        String nombre = "";
        String primerApellido = "";
        String segundoApellido = "";
        if (super.getNombre() != null) {
            nombre = super.getNombre();
        }
        if (super.getPrimerApellido() != null) {
            primerApellido = super.getPrimerApellido();
        } 
        if (super.getSegundoApellido() != null) {
            segundoApellido = super.getSegundoApellido();
        }
        return nombre + " " + primerApellido + " " + segundoApellido + " - " +matriculaEstudiante;
    }    
    
}
