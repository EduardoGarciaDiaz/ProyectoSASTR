/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 13/05/2023
 * Descripción: POJO que modela los valores asignados
 * a la rubrica para la validacion de un anteproyecto. 
 */

package javafxsastr.modelos.pojo;

public class Rubrica {
    
    private int idRubrica;
    private int valorLineasGeneracionAplicacionConocimiento;
    private int valorNombreTrabajoRecepcional;
    private int valorDescripcionTrabajoRecepcional;
    private int valorRequisitosAnteproyecto;
    private int valorResultadosEsperados;
    private int valorBibliografiasRecomendadas;
    private int valorRedaccion;
    
    public Rubrica() {
        
    }

    public Rubrica(int idRubrica, int valorLineasGeneracionAplicacionConocimiento, int valorNombreTrabajoRecepcional, 
            int valorDescripcionTrabajoRecepcional, int valorRequisitosAnteproyecto, int valorResultadosEsperados, 
            int valorBibliografiasRecomendadas, int valorRedaccion) {
        this.idRubrica = idRubrica;
        this.valorLineasGeneracionAplicacionConocimiento = valorLineasGeneracionAplicacionConocimiento;
        this.valorNombreTrabajoRecepcional = valorNombreTrabajoRecepcional;
        this.valorDescripcionTrabajoRecepcional = valorDescripcionTrabajoRecepcional;
        this.valorRequisitosAnteproyecto = valorRequisitosAnteproyecto;
        this.valorResultadosEsperados = valorResultadosEsperados;
        this.valorBibliografiasRecomendadas = valorBibliografiasRecomendadas;
        this.valorRedaccion = valorRedaccion;
    }

    public int getIdRubrica() {
        return idRubrica;
    }

    public void setIdRubrica(int idRubrica) {
        this.idRubrica = idRubrica;
    }

    public int getValorLineasGeneracionAplicacionConocimiento() {
        return valorLineasGeneracionAplicacionConocimiento;
    }

    public void setValorLineasGeneracionAplicacionConocimiento(int valorLineasGeneracionAplicacionConocimiento) {
        this.valorLineasGeneracionAplicacionConocimiento = valorLineasGeneracionAplicacionConocimiento;
    }

    public int getValorNombreTrabajoRecepcional() {
        return valorNombreTrabajoRecepcional;
    }

    public void setValorNombreTrabajoRecepcional(int valorNombreTrabajoRecepcional) {
        this.valorNombreTrabajoRecepcional = valorNombreTrabajoRecepcional;
    }

    public int getValorDescripcionTrabajoRecepcional() {
        return valorDescripcionTrabajoRecepcional;
    }

    public void setValorDescripcionTrabajoRecepcional(int valorDescripcionTrabajoRecepcional) {
        this.valorDescripcionTrabajoRecepcional = valorDescripcionTrabajoRecepcional;
    }

    public int getValorRequisitosAnteproyecto() {
        return valorRequisitosAnteproyecto;
    }

    public void setValorRequisitosAnteproyecto(int valorRequisitosAnteproyecto) {
        this.valorRequisitosAnteproyecto = valorRequisitosAnteproyecto;
    }

    public int getValorResultadosEsperados() {
        return valorResultadosEsperados;
    }

    public void setValorResultadosEsperados(int valorResultadosEsperados) {
        this.valorResultadosEsperados = valorResultadosEsperados;
    }

    public int getValorBibliografiasRecomendadas() {
        return valorBibliografiasRecomendadas;
    }

    public void setValorBibliografiasRecomendadas(int valorBibliografiasRecomendadas) {
        this.valorBibliografiasRecomendadas = valorBibliografiasRecomendadas;
    }

    public int getValorRedaccion() {
        return valorRedaccion;
    }

    public void setValorRedaccion(int valorRedaccion) {
        this.valorRedaccion = valorRedaccion;
    }
    
      
    
}
