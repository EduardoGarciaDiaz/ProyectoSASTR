/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 12/05/2023
 * Descripción: DAO de Academico para modelar su información
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.utils.Codigos;

public class AcademicoDAO {
    
    private final String OBTENER_ACADEMICO_POR_ID = "SELECT idAcademico, usuarios.idUsuario, nombreUsuario AS nombreAcademico, "
            + "primerApellidoUsuario AS primerApellidoAcademico, segundoApellidoUsuario AS segundoApellidoAcademico, "
            + "numeroPersonalAcademico, correoInstitucionalUsuario AS correoInstitucionalAcademico, " 
            + "correoAlternoUsuario AS correoAlternoAcademico, contraseñaUsuario AS contraseñaAcademico, " 
            + "esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " 
            + "FROM sastr.usuarios " 
            + "INNER JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario "
            + "inner join sastr.academicos acad on usuarios.idUsuario = acad.idUsuario " 
            + "where idAcademico = ?;";
    private final String OBTENER_ACADEMICO_POR_ID_USUARIO = "SELECT idAcademico, usuarios.idUsuario, nombreUsuario AS nombreAcademico, "
            + "primerApellidoUsuario AS primerApellidoAcademico, segundoApellidoUsuario AS segundoApellidoAcademico, "
            + "numeroPersonalAcademico, correoInstitucionalUsuario AS correoInstitucionalAcademico, " 
            + "correoAlternoUsuario AS correoAlternoAcademico, contraseñaUsuario AS contraseñaAcademico, " 
            + "esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " 
            + "FROM sastr.usuarios " 
            + "INNER JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario "
            + "inner join sastr.academicos acad on usuarios.idUsuario = acad.idUsuario " 
            + "where acad.idUsuario = ?;";
    private final String OBTENER_ACADEMICOS = "SELECT idAcademico, usuarios.idUsuario, nombreUsuario AS nombreAcademico, "
            + "primerApellidoUsuario AS primerApellidoAcademico, segundoApellidoUsuario AS segundoApellidoAcademico, "
            + "numeroPersonalAcademico, correoInstitucionalUsuario AS correoInstitucionalAcademico, "
            + "correoAlternoUsuario AS correoAlternoAcademico, contraseñaUsuario AS contraseñaAcademico, "
            + "esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario "
            + "FROM sastr.usuarios "
            + "INNER JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario "
            + "inner join sastr.academicos acad on usuarios.idUsuario = acad.idUsuario;";
    private final String OBTENER_CODIRECTORES = "SELECT acad.idAcademico, usuarios.idUsuario, nombreUsuario AS nombreAcademico, "
            + "primerApellidoUsuario AS primerApellidoAcademico, numeroPersonalAcademico, " 
            + "segundoApellidoUsuario AS segundoApellidoAcademico, correoInstitucionalUsuario AS correoInstitucionalAcademico, " 
            + "correoAlternoUsuario AS correoAlternoAcademico, contraseñaUsuario AS contraseñaAcademico, " 
            + "esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " 
            + "FROM sastr.usuarios " 
            + "INNER JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario " 
            + "inner join sastr.academicos acad on usuarios.idUsuario = acad.idUsuario " 
            + "inner join codirectores_anteproyectos ca " 
            + "on ca.idAcademico = acad.idAcademico " 
            + "where ca.idAnteproyecto = ?;";
    private final String ELIMINAR_ACADEMICO = "DELETE FROM academicos WHERE idAcademico = ?";
    private final String ACTUALIZAR_ACADEMICO = "UPDATE academicos set numeroPersonalAcademico = ?, idUsuario = ? where idAcademico = ?;";
    private final String GUARDAR_ACADEMICO = "insert into sastr.academicos (numeroPersonalAcademico, idUsuario) values ( ?, ?);";
    private final String OBTENER_CODIRECTORES_POR_ANTEPROYECTO = "SELECT acad.idAcademico, usuarios.idUsuario, nombreUsuario AS nombreAcademico, "
            + "primerApellidoUsuario AS primerApellidoAcademico, numeroPersonalAcademico," 
            + "segundoApellidoUsuario AS segundoApellidoUsuario, correoInstitucionalUsuario AS correoInstitucionalAcademico, " 
            + "correoAlternoUsuario AS correoAlternoAcademico, contraseñaUsuario AS contraseñaAcademico, " 
            + "esAdministrador, usuarios.idEstadoUsuario, estados_usuario.nombreEstadoUsuario " 
            + "FROM sastr.usuarios " 
            + "INNER JOIN sastr.estados_usuario on usuarios.idEstadoUsuario = estados_usuario.idEstadoUsuario " 
            + "inner join sastr.academicos acad on usuarios.idUsuario = acad.idUsuario " 
            + "inner join codirectores_anteproyectos ca " 
            + "on ca.idAcademico = acad.idAcademico " 
            + "where ca.idAnteproyecto = ?;";
    
    public Academico obtenerAcademicoPorId(int idAcademico) throws DAOException{
        Academico academico = new Academico();
        academico.setIdAcademico(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ACADEMICO_POR_ID) ;
            sentencia.setInt(1, idAcademico);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                academico.setIdAcademico(resultado.getInt("idAcademico"));
                academico.setIdUsuario(resultado.getInt("idUsuario"));
                academico.setNombre(resultado.getString("nombreAcademico"));
                academico.setPrimerApellido(resultado.getString("primerApellidoAcademico"));
                academico.setSegundoApellido(resultado.getString("segundoApellidoAcademico"));
                academico.setCorreoInstitucional(resultado.getString("correoInstitucionalAcademico"));
                academico.setCorreoAlterno(resultado.getString("correoAlternoAcademico"));
                academico.setContraseña(resultado.getString("contraseñaAcademico"));
                academico.setEsAdministrador(resultado.getBoolean("esAdministrador"));
                academico.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                academico.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
                academico.setNumeroPersonal(resultado.getInt("numeroPersonalAcademico"));      
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error en la consulta", Codigos.ERROR_CONSULTA);
        }
        return academico;
    }
    
    public Academico obtenerAcademicoPorIdUsuario(int idUsuario) throws DAOException{
        Academico academico = new Academico();
        academico.setIdAcademico(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ACADEMICO_POR_ID_USUARIO) ;
            sentencia.setInt(1, idUsuario);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                academico.setIdAcademico(resultado.getInt("idAcademico"));
                academico.setIdUsuario(resultado.getInt("idUsuario"));
                academico.setNombre(resultado.getString("nombreAcademico"));
                academico.setPrimerApellido(resultado.getString("primerApellidoAcademico"));
                academico.setSegundoApellido(resultado.getString("segundoApellidoAcademico"));
                academico.setCorreoInstitucional(resultado.getString("correoInstitucionalAcademico"));
                academico.setCorreoAlterno(resultado.getString("correoAlternoAcademico"));
                academico.setContraseña(resultado.getString("contraseñaAcademico"));
                academico.setEsAdministrador(resultado.getBoolean("esAdministrador"));
                academico.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                academico.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
                academico.setNumeroPersonal(resultado.getInt("numeroPersonalAcademico"));      
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error en la consulta", Codigos.ERROR_CONSULTA);
        }
        return academico;
    }
    
    public ArrayList<Academico> obtenerAcademicos() throws DAOException {
        ArrayList<Academico> academicos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ACADEMICOS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                academicos.add(new Academico(
                        resultado.getInt("idAcademico"), 
                        resultado.getInt("numeroPersonalAcademico"), 
                        resultado.getInt("idUsuario"), 
                        resultado.getString("nombreAcademico"), 
                        resultado.getString("primerApellidoAcademico"), 
                        resultado.getString("segundoApellidoAcademico"), 
                        resultado.getString("correoInstitucionalAcademico"), 
                        resultado.getString("correoAlternoAcademico"), 
                        resultado.getString("contraseñaAcademico"), 
                        resultado.getBoolean("esAdministrador"), 
                        resultado.getInt("idEstadoUsuario"), 
                        resultado.getString("nombreEstadoUsuario")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return academicos;
    }
    
    public ArrayList<Academico> obtenerCodirectores(int idAnteproyecto) throws DAOException {
        ArrayList<Academico> academicos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CODIRECTORES);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                academicos.add(new Academico(
                        resultado.getInt("idAcademico"), 
                        resultado.getInt("numeroPersonalAcademico"), 
                        resultado.getInt("idUsuario"), 
                        resultado.getString("nombreAcademico"), 
                        resultado.getString("primerApellidoAcademico"), 
                        resultado.getString("segundoApellidoAcademico"), 
                        resultado.getString("correoInstitucionalAcademico"), 
                        resultado.getString("correoAlternoAcademico"), 
                        resultado.getString("contraseñaAcademico"), 
                        resultado.getBoolean("esAdministrador"), 
                        resultado.getInt("idEstadoUsuario"), 
                        resultado.getString("nombreEstadoUsuario")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return academicos;
    }
    
    public int guardarAcademico(Academico academico) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_ACADEMICO,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, academico.getNumeroPersonal());
            sentencia.setInt(2, academico.getIdUsuario());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            while (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error en la consulta", Codigos.ERROR_CONSULTA);
        } 
        return respuesta;
    }
    
    public int actualizarAcademico(Academico academico) throws DAOException {
        System.out.println("idacad"+academico.getIdAcademico());
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_ACADEMICO);
            sentencia.setInt(1, academico.getNumeroPersonal());
            sentencia.setInt(2, academico.getIdUsuario());
            sentencia.setInt(3, academico.getIdAcademico());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? academico.getIdAcademico(): -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error en la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int eliminarAcademico (int idAcademico) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_ACADEMICO);
            sentencia.setInt(1, idAcademico);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idAcademico : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo siento, para la otra", Codigos. ERROR_CONSULTA);
        }
        return respuesta;
    }
    
       public ArrayList<Academico> obtenerCodirectoresProAnteproyecto(int idAnteproyecto) throws DAOException {
        ArrayList<Academico> academicos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CODIRECTORES_POR_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                academicos.add(new Academico(
                        resultado.getInt("idAcademico"), 
                        resultado.getInt("numeroPersonalAcademico"), 
                        resultado.getInt("idUsuario"), 
                        resultado.getString("nombreAcademico"), 
                        resultado.getString("primerApellidoAcademico"), 
                        resultado.getString("segundoApellidoAcademico"), 
                        resultado.getString("correoInstitucionalAcademico"), 
                        resultado.getString("correoAlternoAcademico"), 
                        resultado.getString("contraseñaAcademico"), 
                        resultado.getBoolean("esAdministrador"), 
                        resultado.getInt("idEstadoUsuario"), 
                        resultado.getString("nombreEstadoUsuario")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return academicos;
    }
    
}
