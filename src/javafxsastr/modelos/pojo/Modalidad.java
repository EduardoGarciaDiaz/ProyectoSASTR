/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 01/06/2023
 * Descripción: POJO que modela la información de las modalidades
 * permitidas para el desarrollo de un anteproyecto.
 */

package javafxsastr.modelos.pojo;

public class Modalidad {
    
    private int idModalidad;
    private String nombreModalidad;

    public Modalidad(int idModalidad, String nombreModalidad) {
        this.idModalidad = idModalidad;
        this.nombreModalidad = nombreModalidad;
    }

    public int getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }

    public String getNombreModalidad() {
        return nombreModalidad;
    }

    public void setNombreModalidad(String nombreModalidad) {
        this.nombreModalidad = nombreModalidad;
    }

    @Override
    public String toString() {
        return nombreModalidad;
    }
    
}
