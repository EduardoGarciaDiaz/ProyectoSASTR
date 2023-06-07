
package javafxsastr.modelos.pojo;


public class CuerpoAcademicoLgac {
    private int idCuerpoAcademicoLgac;
    private int idCuerpoAcademico;
    private int idLgac;

    public CuerpoAcademicoLgac() {
    }

    public CuerpoAcademicoLgac(int idCuerpoAcademicoLgac, int idCuerpoAcademico, int Lgac) {
        this.idCuerpoAcademicoLgac = idCuerpoAcademicoLgac;
        this.idCuerpoAcademico = idCuerpoAcademico;
        this.idLgac = Lgac;
    }

    public int getIdCuerpoAcademicoLgac() {
        return idCuerpoAcademicoLgac;
    }

    public void setIdCuerpoAcademicoLgac(int idCuerpoAcademicoLgac) {
        this.idCuerpoAcademicoLgac = idCuerpoAcademicoLgac;
    }

    public int getIdCuerpoAcademico() {
        return idCuerpoAcademico;
    }

    public void setIdCuerpoAcademico(int idCuerpoAcademico) {
        this.idCuerpoAcademico = idCuerpoAcademico;
    }

    public int getLgac() {
        return idLgac;
    }

    public void setLgac(int Lgac) {
        this.idLgac = Lgac;
    }
    
    
}
