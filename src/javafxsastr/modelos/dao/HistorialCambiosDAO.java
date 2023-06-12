/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase DAO de el Historial de cambios
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.HistorialCambios;
import javafxsastr.utils.Codigos;

public class HistorialCambiosDAO {
    
    private final String Consulta_Historial_Cambios = "Select idHistorialCambio, fechaDeModificacion, DATE(fechaAnterior) "
            + "AS fechaAnterior, TIME(fechaAnterior) AS horaAnterior, DATE(fechaNueva) "
            + "AS fechaNueva, TIME(fechaNueva) AS horaNueva FROM historial_cambios WHERE idActividad = ?";
    private final String Guardar_Historial_Cambios_Nuevo ="INSERT INTO historial_cambios SET fechaDeModificacion = ?, " 
            + "fechaAnterior = CONCAT (? ,' ',?), fechaNueva = CONCAT (?,' ', ?), idActividad = ?";
    
    public ArrayList<HistorialCambios> obtenerInformacionHistorialCambios(int idActividad) throws DAOException {
        ArrayList<HistorialCambios> historialCambiosConsultados= new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Consulta_Historial_Cambios);
            sentencia.setInt(1, idActividad);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while(resultadoConsulta.next()) {
                HistorialCambios historialCambios = new HistorialCambios();
                historialCambios.setFechaDeModificacion(resultadoConsulta.getDate("fechaDeModificacion").toString());
                historialCambios.setFechaAnterior(resultadoConsulta.getDate("fechaAnterior").toString());
                historialCambios.setHoraAnterior(resultadoConsulta.getTime("horaAnterior").toString());
                historialCambios.setFechaNueva(resultadoConsulta.getDate("fechaNueva").toString());
                historialCambios.setHoraNueva(resultadoConsulta.getTime("horaNueva").toString());
               historialCambiosConsultados.add(historialCambios);              
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al Consultar el historial de modificaciones.", Codigos.ERROR_CONSULTA);
        }
        return historialCambiosConsultados;
    }
    
    public int guardarHistorialCambios(HistorialCambios historialNuevo) throws DAOException{
        int respuestaExito = -1;
        
            PreparedStatement sentencia;
        try {
            sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Guardar_Historial_Cambios_Nuevo, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, historialNuevo.getFechaDeModificacion());
            sentencia.setString(2, historialNuevo.getFechaAnterior());
            sentencia.setString(3, historialNuevo.getHoraAnterior());
            sentencia.setString(4, historialNuevo.getFechaNueva());
            sentencia.setString(5, historialNuevo.getHoraNueva());
            sentencia.setInt(6, historialNuevo.getIdActividad());
            sentencia.executeUpdate();
            ResultSet resultadoRegistroNuevo = sentencia.getGeneratedKeys();
            if (resultadoRegistroNuevo.next()) {
                respuestaExito = resultadoRegistroNuevo.getInt(1);
            }
            ConexionBD.cerrarConexionBD();            
        }catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al registrar esta modificacion", Codigos.ERROR_CONSULTA);
         }
        return respuestaExito;
    }
}
