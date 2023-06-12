/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase POJO del Archivo
 */

package javafxsastr.modelos.pojo;

public class Archivo {
    
    private int idArchivo;
    private String nombreArchivo;
    private byte[] archivo;
    private Boolean esEntrega;

    public Boolean getEsEntrega() {
        return esEntrega;
    }

    public void setEsEntrega(Boolean esEntrega) {
        this.esEntrega = esEntrega;
    }
    private int idEntrega;

    public Archivo() {
    }

    public Archivo(int idArchivo, String nombreArchivo, byte[] archivo, Boolean esEntrega, int idEntrega) {
        this.idArchivo = idArchivo;
        this.nombreArchivo = nombreArchivo;
        this.archivo = archivo;
        this.esEntrega = esEntrega;
        this.idEntrega = idEntrega;
    }  

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }
    

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    } 
    
}
