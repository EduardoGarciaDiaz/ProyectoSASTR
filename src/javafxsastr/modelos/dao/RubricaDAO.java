/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 12/05/2023
 * Descripción: DAO para realizar las consultas a la base de datos
 * correspondientes a las rubricas. 
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Rubrica;
import javafxsastr.utils.Codigos;

public class RubricaDAO {
    
    private final String OBTENER_RUBRICA= "SELECT * FROM sastr.rubricas WHERE (idRubrica = ?);";
    private final String GUARDAR_RUBRICA = "INSERT INTO `sastr`.`rubricas` "
            + "(`lineasGeneracionAplicacionConocimiento`, `nombreTrabajoRecepcional`, `descripcionTrabajoRecepcional`, "
            + "`requisitosAnteproyecto`, `resultadosEsperados`, `bibliografiasRecomendadas`, `redaccion`) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);";
    private final String ACTUALIZAR_RUBRICA = "UPDATE `sastr`.`rubricas` SET `lineasGeneracionAplicacionConocimiento` = ?, "
            + "`nombreTrabajoRecepcional` = ?, `descripcionTrabajoRecepcional` = ?, `requisitosAnteproyecto` = ?, "
            + "`resultadosEsperados` = ?, `bibliografiasRecomendadas` = ?, `redaccion` = ? WHERE (`idRubrica` = ?);";
    private final String ELIMINAR_RUBRICA = "DELETE FROM `sastr`.`rubricas` WHERE (`idRubrica` = '?');";
    private final String VALIDAR_EXISTENCIA_RUBRICA = "Select "
            + "idRubrica from revisiones_anteproyectos where idAnteproyecto =  ?";
    
    public Rubrica obtenerRubricaPorId(int idRubrica) throws DAOException {
        Rubrica rubrica = new Rubrica();
        rubrica.setIdRubrica(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_RUBRICA);
            sentencia.setInt(1, idRubrica);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                rubrica.setIdRubrica(resultado.getInt("idRubrica"));
                rubrica.setValorLineasGeneracionAplicacionConocimiento(resultado.getInt("lineasGeneracionAplicacionConocimiento"));
                rubrica.setValorNombreTrabajoRecepcional(resultado.getInt("nombreTrabajoRecepcional"));
                rubrica.setValorDescripcionTrabajoRecepcional(resultado.getInt("descripcionTrabajoRecepcional"));
                rubrica.setValorRequisitosAnteproyecto(resultado.getInt("requisitosAnteproyecto"));
                rubrica.setValorResultadosEsperados(resultado.getInt("resultadosEsperados"));
                rubrica.setValorBibliografiasRecomendadas(resultado.getInt("bibliografiasRecomendadas"));
                rubrica.setValorRedaccion(resultado.getInt("redaccion"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException( "Lo sentimos, hubo un problema.", Codigos.ERROR_CONSULTA);
        }
        return rubrica;
    }
    
    public int guardarRubrica(Rubrica rubrica) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(GUARDAR_RUBRICA, Statement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, rubrica.getValorLineasGeneracionAplicacionConocimiento());
            sentencia.setInt(2, rubrica.getValorNombreTrabajoRecepcional());
            sentencia.setInt(3, rubrica.getValorDescripcionTrabajoRecepcional());
            sentencia.setInt(4, rubrica.getValorRequisitosAnteproyecto());
            sentencia.setInt(5, rubrica.getValorResultadosEsperados());
            sentencia.setInt(6, rubrica.getValorBibliografiasRecomendadas());
            sentencia.setInt(7, rubrica.getValorRedaccion());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException( "Error de consulta.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int actualizarRubrica(Rubrica rubrica) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_RUBRICA);
            sentencia.setInt(1, rubrica.getValorLineasGeneracionAplicacionConocimiento());
            sentencia.setInt(2, rubrica.getValorNombreTrabajoRecepcional());
            sentencia.setInt(3, rubrica.getValorDescripcionTrabajoRecepcional());
            sentencia.setInt(4, rubrica.getValorRequisitosAnteproyecto());
            sentencia.setInt(5, rubrica.getValorResultadosEsperados());
            sentencia.setInt(6, rubrica.getValorBibliografiasRecomendadas());
            sentencia.setInt(7, rubrica.getValorRedaccion());
            sentencia.setInt(8, rubrica.getIdRubrica());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? rubrica.getIdRubrica() : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int eliminarRubrica(int idRubrica) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_RUBRICA);
            sentencia.setInt(1, idRubrica);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idRubrica : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
      public int obtenerRubricaAnteproyecto(int idAnteproyecto) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(VALIDAR_EXISTENCIA_RUBRICA);
            sentencia.setInt(1, idAnteproyecto);            
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
             throw new DAOException("Error de consulta.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
      
}
