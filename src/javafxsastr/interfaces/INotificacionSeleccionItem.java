/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: Interfaz que funciona como puente entre las clases que la 
 * implementen y los campos de busqueda creados en la clase CampoDeBusqueda.
 * Se manda a llamar cada vez que se selecciona un elemento de una ListView 
 * o cuando se pierde el foco de los campos de busqueda. 
 */


package javafxsastr.interfaces;

public interface INotificacionSeleccionItem<E> {
    
    public void notificarSeleccionItem(E itemSeleccionado);
    
    public void notificarPerdidaDelFoco();
}
