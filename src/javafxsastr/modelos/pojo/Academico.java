/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 20/05/2023
 * Descripción: POJO de Academico para modelar su información
 */


package javafxsastr.modelos.pojo;

public class Academico extends Usuario {
    private int idAcademico;
    int numeroPersonal;
    
    public Academico() {
        
    }

    public Academico(int idAcademico, int numeroPersonal, int idUsuario, String nombre, String primerApellido, 
            String segundoApellido, String correoInstitucional, String correoAlterno, String password, 
            boolean esAdministrador, int idEstadoUsuario, String estadoUsuario) {
        
        super(idUsuario, nombre, primerApellido, segundoApellido, correoInstitucional, 
                correoAlterno, password, esAdministrador, idEstadoUsuario, estadoUsuario);
        this.idAcademico = idAcademico;
        this.numeroPersonal = numeroPersonal;
    }

    public int getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(int numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
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
        return nombre + " " + primerApellido + " " + segundoApellido;
    }
    
    
}
