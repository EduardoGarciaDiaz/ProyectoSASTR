/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 21/05/2023
 * Descripción: DAO de Cuerpo Academico.
 */

package javafxsastr.modelos.pojo;

public class Area {
    private int idArea;
    private String nombreArea;

    public Area() {
    }

    public Area(int idArea, String nombreArea) {
        this.idArea = idArea;
        this.nombreArea = nombreArea;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
    
    @Override
    public String toString() {
        return nombreArea;
    }
   
 }
