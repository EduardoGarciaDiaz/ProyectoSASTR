/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: POJO que modela la información delos Bloques 
 * en un contexto académico.
 */

package javafxsastr.modelos.pojo;

/**
 *
 * @author Daniel García Arcos
 */
public class Bloque {
    private int idBloque;
    private String nombreBloque;

    public Bloque(int idBloque, String nombreBloque) {
        this.idBloque = idBloque;
        this.nombreBloque = nombreBloque;
    }

    public int getIdBloque() {
        return idBloque;
    }

    public void setIdBloque(int idBloque) {
        this.idBloque = idBloque;
    }

    public String getNombreBloque() {
        return nombreBloque;
    }

    public void setNombreBloque(String nombreBloque) {
        this.nombreBloque = nombreBloque;
    }

    @Override
    public String toString() {
        return nombreBloque;
    }
    
}
