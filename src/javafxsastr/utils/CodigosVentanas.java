/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 13/05/2023
 * Descripción: Asigna un codigo a diferentes ventanas para poder
 * distinguirlas en la navegabilidad.
 */

package javafxsastr.utils;

public enum CodigosVentanas {
    GEsTION_DE_USUARIOS("10"),
    GESTION_DE_CA_LGAC("11"),
    MODIFICAR_CUERPO_ACADEMICO("12"),
    CREAR_CUERPO_ACADEMICO("13"),
    ESTUDIANTES_ASIGNADOS("14"),
    CONSULTAR_AVANCES_ESTUDIANTES("15"),
    CONSULTAR_AVANCE_DE_ESTUDIANTE("16"),
    MIS_ANTEPROYECTOS("17"),
    VALIDAR_ANTEPROYECTOS("18"),
    MI_ANTEPROYECTO("19");
    
 
    private final String codigo;

    private CodigosVentanas(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
