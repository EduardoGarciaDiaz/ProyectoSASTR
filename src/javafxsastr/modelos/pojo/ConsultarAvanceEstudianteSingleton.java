/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 25/05/2023
 * Descripción: El controlador de avance del estudiante puede ser accedido desde dos 
 * ventanas y tiene navegabilidad interior. Resulta necesario recordar el origen del que vino
 * asi como ciertos atributos privados para el funcionamientos de ventanas anteriores 
 * a ella en navegabilidad. Esta clase singleton mantiene esos datos. 
 */

package javafxsastr.modelos.pojo;

import javafxsastr.utils.CodigosVentanas;

public class ConsultarAvanceEstudianteSingleton {
    
    private Academico academico;
    private Estudiante estudiante;
    private CodigosVentanas ventanaOrigen;
    private Curso curso;
    private static ConsultarAvanceEstudianteSingleton consultarAvanceEstudiante;
    
    public static ConsultarAvanceEstudianteSingleton obtenerConsultarAvanceEstudiante(
            Academico academico,
            Estudiante estudiante,
            CodigosVentanas ventanaOrigen,
            Curso curso) {
        if (consultarAvanceEstudiante == null) {
            consultarAvanceEstudiante = 
                    new ConsultarAvanceEstudianteSingleton(academico, estudiante, ventanaOrigen, curso);
        }
        return consultarAvanceEstudiante;
    }
    
    private ConsultarAvanceEstudianteSingleton(
            Academico academico,
            Estudiante estudiante,
            CodigosVentanas ventanaOrigen,
            Curso curso) {
        
        this.academico = academico;
        this.estudiante = estudiante;
        this.ventanaOrigen = ventanaOrigen;
        this.curso = curso;
    }

    public Academico getAcademico() {
        return academico;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public CodigosVentanas getVentanaOrigen() {
        return ventanaOrigen;
    }

    public Curso getCurso() {
        return curso;
    }

    public static void setConsultarAvanceEstudiante(ConsultarAvanceEstudianteSingleton consultarAvanceEstudiante) {
        ConsultarAvanceEstudianteSingleton.consultarAvanceEstudiante = consultarAvanceEstudiante;
    }

}
