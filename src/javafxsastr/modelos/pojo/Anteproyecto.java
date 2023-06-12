/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 12/05/2023
 * Descripción: POJO de Anteproyecto para modelar la información necesaria para este.
 */

package javafxsastr.modelos.pojo;

public class Anteproyecto {
    
    int idAnteproyecto;
    int idCuerpoAcademico;
    int idModalidad;
    String fechaCreacion;
    String ciudadCreacion;
    String nombreProyectoInvestigacion;
    String lineaInvestigacion;
    String nombreTrabajoRecepcional;
    String requisitos;
    String descripcionProyectoInvestigacion;
    String descripcionTrabajoRecepcional;
    String resultadosEsperadosAnteproyecto;
    String bibliografiaRecomendada;
    String duracionAproximada;
    int numeroMaximoAlumnosParticipantes;
    String notasExtras;
    String nombreDirector;
    int idAcademico;
    String estadoSeguimiento;
    int idEstadoSeguimiento;
    String nombreModalidad;
    String nombreCuerpoAcademico;
    
    public Anteproyecto() {
        
    }

    public Anteproyecto(int idAnteproyecto, 
            int idCuerpoAcademico, 
            int idModalidad,
            String fechaCreacion, 
            String ciudadCreacion,
            String nombreProyectoInvestigacion, 
            String lineaInvestigacion, 
            String nombreTrabajoRecepcional, 
            String requisitos, 
            String descripcionProyectoInvestigacion, 
            String descripcionTrabajoRecepcional, 
            String resultadosEsperadosAnteproyecto, 
            String bibliografiaRecomendada, 
            String duracionAproximada, 
            int numeroMaximoAlumnosParticipantes, 
            String notasExtras, 
            String nombreDirector, 
            int idAcademico, 
            String estadoSeguimiento, 
            int idEstadoSeguimiento,
            String nombreModalidad,
            String nombreCuerpoAcademico) {
        
        this.idAnteproyecto = idAnteproyecto;
        this.idCuerpoAcademico = idCuerpoAcademico;
        this.idModalidad = idModalidad;
        this.fechaCreacion = fechaCreacion;
        this.ciudadCreacion = ciudadCreacion;
        this.nombreProyectoInvestigacion = nombreProyectoInvestigacion;
        this.lineaInvestigacion = lineaInvestigacion;
        this.nombreTrabajoRecepcional = nombreTrabajoRecepcional;
        this.requisitos = requisitos;
        this.descripcionProyectoInvestigacion = descripcionProyectoInvestigacion;
        this.descripcionTrabajoRecepcional = descripcionTrabajoRecepcional;
        this.resultadosEsperadosAnteproyecto = resultadosEsperadosAnteproyecto;
        this.bibliografiaRecomendada = bibliografiaRecomendada;
        this.duracionAproximada = duracionAproximada;
        this.numeroMaximoAlumnosParticipantes = numeroMaximoAlumnosParticipantes;
        this.notasExtras = notasExtras;
        this.nombreDirector = nombreDirector;
        this.idAcademico = idAcademico;
        this.estadoSeguimiento = estadoSeguimiento;
        this.idEstadoSeguimiento = idEstadoSeguimiento;
        this.nombreModalidad = nombreModalidad;
        this.nombreCuerpoAcademico = nombreCuerpoAcademico;
    }

    public int getIdAnteproyecto() {
        return idAnteproyecto;
    }

    public void setIdAnteproyecto(int idAnteproyecto) {
        this.idAnteproyecto = idAnteproyecto;
    }

    public int getIdCuerpoAcademico() {
        return idCuerpoAcademico;
    }

    public void setIdCuerpoAcademico(int idCuerpoAcademico) {
        this.idCuerpoAcademico = idCuerpoAcademico;
    }

    public int getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreModalidad() {
        return nombreModalidad;
    }

    public void setNombreModalidad(String nombreModalidad) {
        this.nombreModalidad = nombreModalidad;
    }
    

    public String getNombreProyectoInvestigacion() {
        return nombreProyectoInvestigacion;
    }

    public void setNombreProyectoInvestigacion(String nombreProyectoInvestigacion) {
        this.nombreProyectoInvestigacion = nombreProyectoInvestigacion;
    }

    public String getLineaInvestigacion() {
        return lineaInvestigacion;
    }

    public void setLineaInvestigacion(String lineaInvestigacion) {
        this.lineaInvestigacion = lineaInvestigacion;
    }

    public String getNombreTrabajoRecepcional() {
        return nombreTrabajoRecepcional;
    }

    public void setNombreTrabajoRecepcional(String nombreTrabajoRecepcional) {
        this.nombreTrabajoRecepcional = nombreTrabajoRecepcional;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getDescripcionProyectoInvestigacion() {
        return descripcionProyectoInvestigacion;
    }

    public void setDescripcionProyectoInvestigacion(String descripcionProyectoInvestigacion) {
        this.descripcionProyectoInvestigacion = descripcionProyectoInvestigacion;
    }

    public String getDescripcionTrabajoRecepcional() {
        return descripcionTrabajoRecepcional;
    }

    public void setDescripcionTrabajoRecepcional(String descripcionTrabajoRecepcional) {
        this.descripcionTrabajoRecepcional = descripcionTrabajoRecepcional;
    }

    public String getResultadosEsperadosAnteproyecto() {
        return resultadosEsperadosAnteproyecto;
    }

    public void setResultadosEsperadosAnteproyecto(String resultadosEsperadosAnteproyecto) {
        this.resultadosEsperadosAnteproyecto = resultadosEsperadosAnteproyecto;
    }

    public String getBibliografiaRecomendada() {
        return bibliografiaRecomendada;
    }

    public void setBibliografiaRecomendada(String bibliografiaRecomendada) {
        this.bibliografiaRecomendada = bibliografiaRecomendada;
    }

    public String getDuracionAproximada() {
        return duracionAproximada;
    }

    public void setDuracionAproximada(String duracionAproximada) {
        this.duracionAproximada = duracionAproximada;
    }

    public int getNumeroMaximoAlumnosParticipantes() {
        return numeroMaximoAlumnosParticipantes;
    }

    public void setNumeroMaximoAlumnosParticipantes(int numeroMaximoAlumnosParticipantes) {
        this.numeroMaximoAlumnosParticipantes = numeroMaximoAlumnosParticipantes;
    }

    public String getNotasExtras() {
        return notasExtras;
    }

    public void setNotasExtras(String notasExtras) {
        this.notasExtras = notasExtras;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getEstadoSeguimiento() {
        return estadoSeguimiento;
    }

    public void setEstadoSeguimiento(String estadoSeguimiento) {
        this.estadoSeguimiento = estadoSeguimiento;
    }

    public int getIdEstadoSeguimiento() {
        return idEstadoSeguimiento;
    }

    public void setIdEstadoSeguimiento(int idEstadoSeguimiento) {
        this.idEstadoSeguimiento = idEstadoSeguimiento;
    }

    public String getCiudadCreacion() {
        return ciudadCreacion;
    }

    public void setCiudadCreacion(String ciudadCreacion) {
        this.ciudadCreacion = ciudadCreacion;
    }

    public String getNombreCuerpoAcademico() {
        return nombreCuerpoAcademico;
    }

    public void setNombreCuerpoAcademico(String nombreCuerpoAcademico) {
        this.nombreCuerpoAcademico = nombreCuerpoAcademico;
    }
            
}
