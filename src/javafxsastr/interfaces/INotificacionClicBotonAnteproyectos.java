/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 04/06/2023
 * Descripción: Interfaz que funciona como puente entre la clase TarjetaAnteproyecto
 * y la clase FXMLAnteproyectosController. Se manda a llamar en los clics
 * de los botones contenidos en la Tarjeta para notificar al controlador los 
 * cambios en la interfaz. 
 */

package javafxsastr.interfaces;

import javafxsastr.modelos.pojo.Anteproyecto;

public interface INotificacionClicBotonAnteproyectos {
    
    public void notificarClicBotonVerDetallesAnteproyecto(Anteproyecto anteproyecto);
    
    public void notificarClicBotonModificarBorradorAnteproyecto(Anteproyecto anteproyecto);
    
    public void notificarClicBotonCorregirAnteproyectoe(Anteproyecto anteproyecto);
    
    public void notificarClicValidarAnteproyecto(Anteproyecto anteproyecto);
    
    public void notificarClicPublicarAnteproyecto(Anteproyecto anteproyecto);
    
    public void notificarClicDespublicarAnteproyecto(Anteproyecto anteproyecto);
    
}