/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 13/05/2023
 * Descripción: Excepcion personalizada para ser lanzada en caso de situaciones de excepcion
 * en las operaciones realizadas por los DAO (Data Access Object).
 */

package javafxsastr.modelos.dao;

import javafxsastr.utils.Codigos;

public class DAOException extends Exception {
    
    private final Codigos codigo;
    
    public DAOException(String mensaje, Codigos codigo) {
        super(mensaje);
        this.codigo = codigo;
    }
    
    public Codigos getCodigo() {
        return codigo;
    }
    
}
