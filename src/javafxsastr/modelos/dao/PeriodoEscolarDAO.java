/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: DAO de Periodo Escolar
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.PeriodoEscolar;
import javafxsastr.utils.Codigos;

public class PeriodoEscolarDAO {
    
    private final String OBTENER_PERIODOS_ESCOLARES = "SELECT * FROM sastr.periodos_escolares;";
    private final String OBTENER_PERIODO_ACTUAL = "SELECT * FROM sastr.periodos_escolares where esActual = 1;";
    private final String OBTENER_PERIODO_POR_ID = "SELECT * FROM sastr.periodos_escolares where idPeriodoEscolar = ?;";
    
    public ArrayList<PeriodoEscolar> obtenerPeriodosEscolares() throws DAOException {
        ArrayList<PeriodoEscolar> periodos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_PERIODOS_ESCOLARES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                periodos.add(new PeriodoEscolar(
                        resultado.getInt("idPeriodoEscolar"),
                        resultado.getString("fechaInicioPeriodoEscolar"),
                        resultado.getString("fechaFinPeriodoEscolar"),
                        resultado.getBoolean("esActual")
                ));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un error al obtener los periodos.", Codigos.ERROR_CONSULTA);
        }
        return periodos;
    }
    
    public PeriodoEscolar obtenerPeriodoActual() throws DAOException {
        PeriodoEscolar periodoEscolar = new PeriodoEscolar();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_PERIODO_ACTUAL);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                periodoEscolar.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                periodoEscolar.setFechaInicioPeriodoEscolar(resultado.getString("fechaInicioPeriodoEscolar"));
                periodoEscolar.setFechaFinPeriodoEscolar(resultado.getString("fechaFinPeriodoEscolar"));
                periodoEscolar.setEsActual(resultado.getBoolean("esActual"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta al recuperar el periodo actual", Codigos.ERROR_CONSULTA);
        }
        return periodoEscolar;
    }
    
    public PeriodoEscolar obtenerPeriodoPorId(int idPeriodoEscolar) throws DAOException {
        PeriodoEscolar periodoEscolar = null;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_PERIODO_POR_ID);
            sentencia.setInt(1, idPeriodoEscolar);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                periodoEscolar = new PeriodoEscolar(
                        resultado.getInt("idPeriodoEscolar"),
                        resultado.getString("fechaInicioPeriodoEscolar"),
                        resultado.getString("fechaFinPeriodoEscolar"),
                        resultado.getBoolean("esActual")
                );
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta al recuperar el periodo actual", Codigos.ERROR_CONSULTA);
        }
        return periodoEscolar;
    }
    
}