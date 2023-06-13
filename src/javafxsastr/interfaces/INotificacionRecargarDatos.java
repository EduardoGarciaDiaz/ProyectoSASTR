/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 03/06/2023
 * Descripción: Clase interfaz para notificar de modificaiones de datos en pantallas posoterirores.
 */

package javafxsastr.interfaces;

public interface INotificacionRecargarDatos {
    
    public void notificacionRecargarDatos();
    
    public void notificacionRecargarDatosPorEdicion(boolean fueEditado);
     
}