/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase POJO de la LGAC
 */
package javafxsastr.modelos.pojo;

public class Lgac {
    
    private int idLgac;
    private String nombreLgac;
    private String descripcionLgac;

    public Lgac() {
    }

    public Lgac(int idLgac, String nombreLgac, String descripcionLgac) {
        this.idLgac = idLgac;
        this.nombreLgac = nombreLgac;
        this.descripcionLgac = descripcionLgac;
    }

    public int getIdLgac() {
        return idLgac;
    }

    public void setIdLgac(int idLgac) {
        this.idLgac = idLgac;
    }

    public String getNombreLgac() {
        return nombreLgac;
    }

    public void setNombreLgac(String nombreLgac) {
        this.nombreLgac = nombreLgac;
    }

    public String getDescripcionLgac() {
        return descripcionLgac;
    }

    public void setDescripcionLgac(String descripcionLgac) {
        this.descripcionLgac = descripcionLgac;
    }

    @Override
    public String toString() {
        return nombreLgac;
    }
    
    
    
}
