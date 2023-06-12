/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 20/05/2023
 * Descripción: DAO para realizar las consultas a la base de datos
 * correspondientes de Experiencias Educativas.
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.ExperienciaEducativa;
import javafxsastr.utils.Codigos;

public class ExperienciaEducativaDAO {
    
    private final String GUARDAR_EXPERIENCIA_EDUCATIVA = "INSERT INTO `sastr`.`experiencias_educativas` "
            + "(`nombreExperienciaEducativa`, `creditosExperienciaEducativa`) VALUES (?, ?);";
    private final String ACTUALIZAR_EXPERIENCIA_EDUCATIVA = "UPDATE `sastr`.`experiencias_educativas` "
            + "SET `nombreExperienciaEducativa` = ?, `creditosExperienciaEducativa` = ? "
            + "WHERE (`idExperienciaEducativa` = ?);";
    private final String OBTENER_EXPERIENCIA_EDUCATIVA_POR_ID = "SELECT * FROM sastr.experiencias_educativas "
            + "WHERE (`idExperienciaEducativa` = ?);";
    private final String OBTENER_EXPERIENCIAS_EDUCATIVAS = "SELECT * FROM sastr.experiencias_educativas;";
    private final String ELIMINAR_EXPERIENCIA_EDUCATIVA = "DELETE FROM `sastr`.`experiencias_educativas` "
            + "WHERE (`idExperienciaEducativa` = ?);";
    
    public int guardarExperienciaEducatiba(ExperienciaEducativa experienciaEducativa) throws DAOException{
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_EXPERIENCIA_EDUCATIVA,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, experienciaEducativa.getNombreExperienciaEducativa());
            sentencia.setInt(2, experienciaEducativa.getNumeroCreditosExperienciaEducativa());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            while (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int actualizarRubrica(ExperienciaEducativa experienciaEducativa) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_EXPERIENCIA_EDUCATIVA);
            sentencia.setString(1, experienciaEducativa.getNombreExperienciaEducativa());
            sentencia.setInt(2, experienciaEducativa.getNumeroCreditosExperienciaEducativa());
            sentencia.setInt(3, experienciaEducativa.getIdExperienciaEducativa());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? experienciaEducativa.getIdExperienciaEducativa(): -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public ExperienciaEducativa obtenerExperienciaEducativaPorId(int idExperienciaEducativa) throws DAOException {
        ExperienciaEducativa experienciaEducativa = new ExperienciaEducativa();
        experienciaEducativa.setIdExperienciaEducativa(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_EXPERIENCIA_EDUCATIVA_POR_ID);
            sentencia.setInt(1, idExperienciaEducativa);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
              experienciaEducativa.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
              experienciaEducativa.setNombreExperienciaEducativa(resultado.getString("nombreExperienciaEducativa"));
              experienciaEducativa.setNumeroCreditosExperienciaEducativa(resultado.getInt("creditosExperienciaEducativa"));
            } 
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return experienciaEducativa;
    }
    
    public ArrayList<ExperienciaEducativa> obtenerExperienciasEducativas() throws DAOException {
        ArrayList<ExperienciaEducativa> experienciasEducativas = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_EXPERIENCIAS_EDUCATIVAS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                experienciasEducativas.add(new ExperienciaEducativa(
                    resultado.getInt("idExperienciaEducativa"), 
                    resultado.getString("nombreExperienciaEducativa"),
                    resultado.getInt("creditosExperienciaEducativa")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta.", Codigos.ERROR_CONSULTA);
        }
        return experienciasEducativas;
    }
    
    public int eliminarExperienciaEducativa(int idExperienciaEducativa) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_EXPERIENCIA_EDUCATIVA);
            sentencia.setInt(1, idExperienciaEducativa);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idExperienciaEducativa : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
}
