/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: Interfaz que funciona como puente entre la clase ConstructorInicio
 * y la clase FXMLInicioController. Se manda a llamar en los clics
 * de los botones contenidos en el menú del inicio para notificar al controlador
 * de este los cambios en la interfaz. 
 */


package javafxsastr.interfaces;

public interface INotificacionClicBotonInicio {
    
    public void notificarClicBotonUsuarios();
    
    public void notificarClicBotonAnteproyectos();
    
    public void notificarClicBotonAprobarAnteproyectos();
    
    public void notificarClicBotonGestionCA();
    
    public void notificarClicBotonActividades();
    public void notificarClicBotonCursos();
}
