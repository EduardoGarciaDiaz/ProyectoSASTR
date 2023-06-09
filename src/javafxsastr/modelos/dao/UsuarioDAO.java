/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 12/05/2023
 * Descripción: DAO para realizar consultas a la base de datos
 * correspoendientes de Usuarios. 
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Codigos;

public class UsuarioDAO {
    
    private final String OBTENER_USUARIO_POR_ID = "SELECT " +
        "idUsuario, nombreUsuario, primerApellidoUsuario, segundoApellidoUsuario, correoInstitucionalUsuario, " +
        "correoAlternoUsuario, contraseñaUsuario, esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " +
        "FROM sastr.usuarios " +
        "INNER JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario " +
        "WHERE idUsuario = ?;";
    private final String OBTENER_USUARIO = "SELECT " +
        "idUsuario, nombreUsuario, primerApellidoUsuario, segundoApellidoUsuario, correoInstitucionalUsuario, " +
        "correoAlternoUsuario, contraseñaUsuario, esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " +
        "FROM sastr.usuarios " +
        "inner JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario " +
        "WHERE correoInstitucionalUsuario = ? AND contraseñaUsuario = ?;";
    private final String OBTENER_USUARIOS = "SELECT " +
        "idUsuario, nombreUsuario, primerApellidoUsuario, segundoApellidoUsuario, correoInstitucionalUsuario, " +
        "correoAlternoUsuario, contraseñaUsuario, esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " +
        "FROM sastr.usuarios " +
        "inner JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario";
    private final String OBTENER_USUARIOS_POR_ESTADO = "SELECT" +
        "idUsuario, nombreUsuario, primerApellidoUsuario, segundoApellidoUsuario, correoInstitucionalUsuario, " +
        "correoAlternoUsuario, contraseñaUsuario, esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " + 
        "FROM sastr.usuarios " +
        "inner JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario " +
        "WHERE usuarios.idEstadoUsuario = ?;";
    private final String GUARDAR_USUARIO = "insert into usuarios "
            + "(nombreUsuario, primerApellidoUsuario, segundoApellidoUsuario, correoInstitucionalUsuario, "
            + "correoAlternoUsuario, contraseñaUsuario, esAdministrador, idEstadoUsuario) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private final String ACTUALIZAR_USUARIO = "UPDATE usuarios SET "+
            "nombreUsuario = ?, primerApellidoUsuario = ?, segundoApellidoUsuario = ?, correoInstitucionalUsuario = ?, "+ 
            "correoAlternoUsuario = ?, contraseñaUsuario = ?, esAdministrador = ?, idEstadoUsuario = ? "+
            "WHERE (idUsuario = ?);";
    private final String ELIMINAR_USUARIO = "DELETE FROM usuarios WHERE idUsuario = ?";
    private final String CONSULTAR_USUARIO_ESTUDIANTE_INEXTISTENTES = "SELECT u.idUsuario, u.nombreUsuario FROM usuarios u " +
            "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " +
            "WHERE u.correoInstitucionalUsuario = ? OR e.matriculaEstudiante = ?;";
    private final String CONSULTAR_USUARIO_ACADEMICO_INEXISTENTES = "SELECT u.idUsuario, u.nombreUsuario FROM usuarios u " +
            "INNER JOIN academicos a ON u.idUsuario = a.idUsuario " +
            "WHERE u.correoInstitucionalUsuario = ? OR a.numeroPersonalAcademico = ?;";
           
    public Usuario obtenerUsuarioPorId(int idUsuario) throws DAOException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_USUARIO_POR_ID);
            sentencia.setInt(1, idUsuario);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setNombre(resultado.getString("nombreUsuario"));
                usuario.setPrimerApellido(resultado.getString("primerApellidoUsuario"));
                usuario.setSegundoApellido(resultado.getString("segundoApellidoUsuario"));
                usuario.setCorreoInstitucional(resultado.getString("correoInstitucionalUsuario"));
                usuario.setCorreoAlterno(resultado.getString("correoAlternoUsuario"));
                usuario.setContraseña(resultado.getString("contraseñaUsuario"));
                usuario.setEsAdministrador(resultado.getBoolean("esAdministrador"));
                usuario.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                usuario.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion del usuario.", Codigos.ERROR_CONSULTA);
        }
        return usuario;
    }
    
    public Usuario obtenerUsuario(String correoInstitucional, String contraseña) throws DAOException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_USUARIO);
            sentencia.setString(1, correoInstitucional);
            sentencia.setString(2, contraseña);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setNombre(resultado.getString("nombreUsuario"));
                usuario.setPrimerApellido(resultado.getString("primerApellidoUsuario"));
                usuario.setSegundoApellido(resultado.getString("segundoApellidoUsuario"));
                usuario.setCorreoInstitucional(resultado.getString("correoInstitucionalUsuario"));
                usuario.setCorreoAlterno(resultado.getString("correoAlternoUsuario"));
                usuario.setContraseña(resultado.getString("contraseñaUsuario"));
                usuario.setEsAdministrador(resultado.getBoolean("esAdministrador"));
                usuario.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                usuario.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion del usuario.", Codigos.ERROR_CONSULTA);
        }
        return usuario;
    }
    
    public ArrayList<Usuario> obtenerUsuarios() throws DAOException {
        ArrayList<Usuario> usuarios = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_USUARIOS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                usuarios.add(new Usuario(
                        resultado.getInt("idUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("primerApellidoUsuario"),
                        resultado.getString("segundoApellidoUsuario"),
                        resultado.getString("correoInstitucionalUsuario"),
                        resultado.getString("correoAlternoUsuario"),
                        resultado.getString("contraseñaUsuario"),
                        resultado.getBoolean("esAdministrador"),
                        resultado.getInt("idEstadoUsuario"),
                        resultado.getString("nombreEstadoUsuario")
                ));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al obtener la informacion de los usuarios.", Codigos.ERROR_CONSULTA);
        }
        return usuarios;
    }
    
    public ArrayList<Usuario> obtenerUsuariosPorEstado(int idEstadoUsuario) throws DAOException {
        ArrayList<Usuario> usuarios = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_USUARIOS_POR_ESTADO);
            sentencia.setInt(1, idEstadoUsuario);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                usuarios.add(new Usuario(
                        resultado.getInt("idUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("primerApellidoUsuario"),
                        resultado.getString("segundoApellidoUsuario"),
                        resultado.getString("correoInstitucionalUsuario"),
                        resultado.getString("correoAlternoUsuario"),
                        resultado.getString("contraseñaUsuario"),
                        resultado.getBoolean("esAdministrador"),
                        resultado.getInt("idEstadoUsuario"),
                        resultado.getString("nombreEstadoUsuario")
                ));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al obtener la informacion de los usuarios.", Codigos.ERROR_CONSULTA);
        }
        return usuarios;
    }
    
    public boolean verificarUsuarioEstudianteInexistente(String correoInstitucional, String matricula) throws DAOException {
        boolean esExistente = false;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(CONSULTAR_USUARIO_ESTUDIANTE_INEXTISTENTES);
            sentencia.setString(1, correoInstitucional);
            sentencia.setString(2, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                esExistente = true;
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setNombre(resultado.getString("nombreUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion del usuario.", Codigos.ERROR_CONSULTA);
        }
        return esExistente;
    }
    
    public boolean verificarUsuarioAcademicoInexistente(String correoInstitucional, String noPersonal) throws DAOException {
        boolean esExistente = false;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(CONSULTAR_USUARIO_ACADEMICO_INEXISTENTES);
            sentencia.setString(1, correoInstitucional);
            sentencia.setString(2, noPersonal);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                esExistente = true;
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setNombre(resultado.getString("nombreUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion del usuario.", Codigos.ERROR_CONSULTA);
        }
        return esExistente;
    }
    
    
    public int guardarUsuario(Usuario usuario) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_USUARIO, 
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getPrimerApellido());
            sentencia.setString(3, usuario.getSegundoApellido());
            sentencia.setString(4, usuario.getCorreoInstitucional());
            sentencia.setString(5, usuario.getCorreoAlterno());
            sentencia.setString(6, usuario.getContraseña());
            sentencia.setBoolean(7, usuario.getEsAdministrador());
            sentencia.setInt(8, usuario.getIdEstadoUsuario());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            while (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al registrar el usuario.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int actualizarUsuario(Usuario usuario) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_USUARIO);
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getPrimerApellido());
            sentencia.setString(3, usuario.getSegundoApellido());
            sentencia.setString(4, usuario.getCorreoInstitucional());
            sentencia.setString(5, usuario.getCorreoAlterno());
            sentencia.setString(6, usuario.getContraseña());
            sentencia.setBoolean(7, usuario.getEsAdministrador());
            sentencia.setInt(8, usuario.getIdEstadoUsuario());
            sentencia.setInt(9, usuario.getIdUsuario());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? usuario.getIdUsuario() : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al actualizar el usuario", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int eliminarUsuario(int idUsuario) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_USUARIO);
            sentencia.setInt(1, idUsuario);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idUsuario : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al eliminar el usuario.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
}

