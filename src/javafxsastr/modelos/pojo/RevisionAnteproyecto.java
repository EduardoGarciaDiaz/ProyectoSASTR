/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: POJO que modela la información de una revisión de anteproyecto.. 
 */

package javafxsastr.modelos.pojo;

public class RevisionAnteproyecto {
    
    private int idRevisionAnteproyecto;
    private String comentarios;
    private int idRubrica;
    private int idAnteproyecto;

    public RevisionAnteproyecto(int idRevisionAnteproyecto, String comentarios, int idRubrica, int idAnteproyecto) {
        this.idRevisionAnteproyecto = idRevisionAnteproyecto;
        this.comentarios = comentarios;
        this.idRubrica = idRubrica;
        this.idAnteproyecto = idAnteproyecto;
    }

    public RevisionAnteproyecto() {

    }

    public int getIdRevisionAnteproyecto() {
        return idRevisionAnteproyecto;
    }

    public void setIdRevisionAnteproyecto(int idRevisionAnteproyecto) {
        this.idRevisionAnteproyecto = idRevisionAnteproyecto;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getIdRubrica() {
        return idRubrica;
    }

    public void setIdRubrica(int idRubrica) {
        this.idRubrica = idRubrica;
    }

    public int getIdAnteproyecto() {
        return idAnteproyecto;
    }

    public void setIdAnteproyecto(int idAnteproyecto) {
        this.idAnteproyecto = idAnteproyecto;
    }
   
}
