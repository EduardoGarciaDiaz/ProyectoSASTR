/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: POJO que modela la información de un periodo escolar. 
 */

package javafxsastr.modelos.pojo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PeriodoEscolar {
    
    private int idPeriodoEscolar;
    private String fechaInicioPeriodoEscolar;
    private String fechaFinPeriodoEscolar;
    private boolean esActual;
    private final DateTimeFormatter FORMATO_FECHA_PERIODO_ESCOLAR = DateTimeFormatter.ofPattern("MMM' 'yyyy",
            new Locale("es"));

    public PeriodoEscolar(int idPeriodoEscolar, String fechaInicioPeriodoEscolar, 
            String fechaFinPeriodoEscolar, boolean esActual) {
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.fechaInicioPeriodoEscolar = fechaInicioPeriodoEscolar;
        this.fechaFinPeriodoEscolar = fechaFinPeriodoEscolar;
        this.esActual = esActual;
    }

    public boolean esActual() {
        return esActual;
    }

    public void setEsActual(boolean esActual) {
        this.esActual = esActual;
    }

    public PeriodoEscolar() {
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
        LocalDate fechaInicio = LocalDate.parse(getFechaInicioPeriodoEscolar());
        LocalDate fechaFin = LocalDate.parse(getFechaFinPeriodoEscolar());
        String fechaInicioFormateada = fechaInicio.format(FORMATO_FECHA_PERIODO_ESCOLAR);
        String fechaFinFormateada = fechaFin.format(FORMATO_FECHA_PERIODO_ESCOLAR);
        return fechaInicioFormateada + " - " + fechaFinFormateada;
    }
    
}
