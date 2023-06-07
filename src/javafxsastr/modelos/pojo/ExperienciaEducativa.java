/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 20/05/2023
 * Descripción: POJO de Experiencia Educativa para modelar su informacion
 * y para funcionar como DTO en las consultas a la BB.DD. 
 */

package javafxsastr.modelos.pojo;

public class ExperienciaEducativa {
    private int idExperienciaEducativa;
    private String nombreExperienciaEducativa;
    private int numeroCreditosExperienciaEducativa;
    
    public ExperienciaEducativa() {
        
    }

    public ExperienciaEducativa(int idExperienciaEducativa, String nombreExperienciaEducativa, int numeroCreditosExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
        this.numeroCreditosExperienciaEducativa = numeroCreditosExperienciaEducativa;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public String getNombreExperienciaEducativa() {
        return nombreExperienciaEducativa;
    }

    public void setNombreExperienciaEducativa(String nombreExperienciaEducativa) {
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
    }

    public int getNumeroCreditosExperienciaEducativa() {
        return numeroCreditosExperienciaEducativa;
    }

    public void setNumeroCreditosExperienciaEducativa(int numeroCreditosExperienciaEducativa) {
        this.numeroCreditosExperienciaEducativa = numeroCreditosExperienciaEducativa;
    }

    @Override
    public String toString() {
        return nombreExperienciaEducativa;
    }
    
    
}
