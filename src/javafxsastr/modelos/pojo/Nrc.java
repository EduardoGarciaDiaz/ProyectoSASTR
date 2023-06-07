package javafxsastr.modelos.pojo;

/**
 *
 * @author tristan
 */
public class Nrc {
    private int idNrc;
    private String nombreNrc;
    private int idExperienciaEducativa;

    public Nrc() {
    }

    public Nrc(int idNrc, String nombreNrc, int idExperienciaEducativa) {
        this.idNrc = idNrc;
        this.nombreNrc = nombreNrc;
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public int getIdNrc() {
        return idNrc;
    }

    public void setIdNrc(int idNrc) {
        this.idNrc = idNrc;
    }

    public String getNombreNrc() {
        return nombreNrc;
    }

    public void setNombreNrc(String nombreNrc) {
        this.nombreNrc = nombreNrc;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    @Override
    public String toString() {
        return nombreNrc;
    }  
    
}
