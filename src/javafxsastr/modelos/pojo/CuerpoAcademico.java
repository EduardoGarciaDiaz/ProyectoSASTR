/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 28/05/2023
 * Descripción: POJO que modela la información de un Cuerpo Académico. 
 */

package javafxsastr.modelos.pojo;

public class CuerpoAcademico {
    
    private int idCuerpoAcademico;
    private String nombreCuerpoAcademico;
    private String disciplinaCuerpoAcademico;
    private String descripcion;
    private String area;
    private int idArea;
    private int idAcademico;
    private String nombreResponsableCA;

    public CuerpoAcademico() {
    }

    public CuerpoAcademico(int idCuerpoAcademico, String nombreCuerpoAcademico, String disciplinaCuerpoAcademico, String descripcion, String area, int idArea, int idAcademico, String nombreResponsableCA) {
        this.idCuerpoAcademico = idCuerpoAcademico;
        this.nombreCuerpoAcademico = nombreCuerpoAcademico;
        this.disciplinaCuerpoAcademico = disciplinaCuerpoAcademico;
        this.descripcion = descripcion;
        this.area = area;
        this.idArea = idArea;
        this.idAcademico = idAcademico;
        this.nombreResponsableCA = nombreResponsableCA;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }   

    public int getIdCuerpoAcademico() {
        return idCuerpoAcademico;
    }

    public void setIdCuerpoAcademico(int idCuerpoAcademico) {
        this.idCuerpoAcademico = idCuerpoAcademico;
    }

    public String getNombreCuerpoAcademico() {
        return nombreCuerpoAcademico;
    }

    public void setNombreCuerpoAcademico(String nombreCuerpoAcademico) {
        this.nombreCuerpoAcademico = nombreCuerpoAcademico;
    }

    public String getDisciplinaCuerpoAcademico() {
        return disciplinaCuerpoAcademico;
    }

    public void setDisciplinaCuerpoAcademico(String disciplinaCuerpoAcademico) {
        this.disciplinaCuerpoAcademico = disciplinaCuerpoAcademico;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getNombreResponsableCA() {
        return nombreResponsableCA;
    }

    public void setNombreResponsableCA(String nombreResponsableCA) {
        this.nombreResponsableCA = nombreResponsableCA;
    }

    @Override
    public String toString() {
        return nombreCuerpoAcademico;
    }
        
}
