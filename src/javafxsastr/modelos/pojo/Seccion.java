/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: POJO que modela la información de una sección. 
 */

package javafxsastr.modelos.pojo;

public class Seccion {
    
    private int idSeccion;
    private String nombreSeccion;

    public Seccion(int idSeccion, String nombreSeccion) {
        this.idSeccion = idSeccion;
        this.nombreSeccion = nombreSeccion;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    @Override
    public String toString() {
        return nombreSeccion;
    }
        
}
