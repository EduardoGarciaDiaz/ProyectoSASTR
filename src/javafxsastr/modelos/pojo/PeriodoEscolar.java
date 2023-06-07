/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: POJO que modela la información de un periodo escolar. 
 */

package javafxsastr.modelos.pojo;

public class PeriodoEscolar {
    
    private int idPeriodoEscolar;
    private String fechaInicioPeriodoEscolar;
    private String fechaFinPeriodoEscolar;

    public PeriodoEscolar(int idPeriodoEscolar, String fechaInicioPeriodoEscolar, String fechaFinPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.fechaInicioPeriodoEscolar = fechaInicioPeriodoEscolar;
        this.fechaFinPeriodoEscolar = fechaFinPeriodoEscolar;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public String getFechaInicioPeriodoEscolar() {
        return fechaInicioPeriodoEscolar;
    }

    public void setFechaInicioPeriodoEscolar(String fechaInicioPeriodoEscolar) {
        this.fechaInicioPeriodoEscolar = fechaInicioPeriodoEscolar;
    }

    public String getFechaFinPeriodoEscolar() {
        return fechaFinPeriodoEscolar;
    }

    public void setFechaFinPeriodoEscolar(String fechaFinPeriodoEscolar) {
        this.fechaFinPeriodoEscolar = fechaFinPeriodoEscolar;
    }

    @Override
    public String toString() {
        return fechaInicioPeriodoEscolar + " - " + fechaFinPeriodoEscolar;
    }
    
}
