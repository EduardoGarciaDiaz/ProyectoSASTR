/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 14/05/2023
 * Descripción: POJO de Usuario para modelar su información
 */

package javafxsastr.modelos.pojo;

public class Usuario {
    
    private int idUsuario;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String correoInstitucional;
    private String correoAlterno;
    private String contraseña;
    private boolean esAdministrador;
    private int idEstadoUsuario;
    private String estadoUsuario;

    public Usuario() {
    }

    public Usuario(int idUsuario,  
            String nombre, 
            String primerApellido, 
            String segundoApellido, 
            String correoInstitucional, 
            String correoAlterno, 
            String contraseña, 
            boolean esAdministrador,  
            int idEstadoUsuario,
            String estadoUsuario) {
        
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correoInstitucional = correoInstitucional;
        this.correoAlterno = correoAlterno;
        this.contraseña = contraseña;
        this.esAdministrador = esAdministrador;
        this.idEstadoUsuario = idEstadoUsuario;
        this.estadoUsuario = estadoUsuario;
    }

    public Usuario(String nombre, String primerApellido, String segundoApellido, String correoInstitucional, String contraseña, boolean esAdministrador) {
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correoInstitucional = correoInstitucional;
        this.contraseña = contraseña;
        this.esAdministrador = esAdministrador;
    }
    
    

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    
    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getCorreoAlterno() {
        return correoAlterno;
    }

    public void setCorreoAlterno(String correoAlterno) {
        this.correoAlterno = correoAlterno;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public boolean getEsAdministrador() {
        return esAdministrador;
    }

    public void setEsAdministrador(boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }

    public int getIdEstadoUsuario() {
        return idEstadoUsuario;
    }

    public void setIdEstadoUsuario(int idEstadoUsuario) {
        this.idEstadoUsuario = idEstadoUsuario;
    }

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    @Override
    public String toString() {
        String nombre = "";
        String primerApellido = "";
        String segundoApellido = "";
        if (this.nombre != null) {
            nombre = this.nombre;
        }
        if (this.primerApellido != null) {
            primerApellido = this.primerApellido;
        } 
        if (this.segundoApellido != null) {
            segundoApellido = this.segundoApellido;
        }
        return nombre + " " + primerApellido + " " + segundoApellido;

    }
    
}
