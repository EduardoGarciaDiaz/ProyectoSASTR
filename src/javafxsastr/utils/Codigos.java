/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 13/05/2023
 * Descripción: Define los codigos de respuesta que representan a las diferentes situaciones
 * que podrian ocurrir al realizar una operacion. 
 */

package javafxsastr.utils;

public enum Codigos {
    
    EXITO("200"),
    ERROR_CONSULTA("400"),
    ERROR_CONEXION_BD("500");

    private final String codigo;

    private Codigos(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
