/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase DAO de el NRC
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Nrc;
import javafxsastr.utils.Codigos;


public class NrcDAO {
    
    private final String OBTENER_NRCS = "Select idEntrega,comentarioAlumno, fehchaEntrega from entregas";
    private final String OBTENER_NRC = "Select idNrc, nombreNrc, idExperienciaEducativa from nrcs where idNrc = ?";
    private final String GUARDAR_NRC = "Insert into nrc(nombreNrc, idExperienciaEducativa) Values (?,?)";
    private final String ACTUALIZAR_NRC = "Update nrcs set nombreNrc = ?, idExperienciaEducativa = ? where idNrc = ?";
    private final String ELIMINAR_NRC = "Delete from nrcs where idNrc = ?";
    private final String OBTENER_NRCS_POR_EXPERIENCIA_EDUCATIVA = "SELECT * FROM sastr.nrcs where idExperienciaEducativa = ?;";
    private final String OBTENER_NRCS_DISPONIBLES_POR_EE_PERIODO_ACTUAL = "select * from nrcs " 
            + "inner join experiencias_educativas " 
            + "on experiencias_educativas.idExperienciaEducativa = nrcs.idExperienciaEducativa " 
            + "where nrcs.nombreNrc not in (SELECT nrcs.nombreNrc FROM sastr.nrcs " 
            + "inner join cursos on nrcs.idNrc = cursos.idNrc " 
            + "left join periodos_escolares on periodos_escolares.idPeriodoEscolar = cursos.idPeriodoEscolar " 
            + "left join experiencias_educativas on nrcs.idExperienciaEducativa = experiencias_educativas.idExperienciaEducativa " 
            + "where periodos_escolares.idPeriodoEscolar = 1 " 
            + "and experiencias_educativas.idExperienciaEducativa = ?) " 
            + "and experiencias_educativas.idExperienciaEducativa = ?;";

    public ArrayList<Nrc> obtenerInformacionNRCS() throws DAOException{
        ArrayList<Nrc> nrcConsultados = new ArrayList(); 
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_NRCS);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while (resultadoConsulta.next()) {
                Nrc nrc = new Nrc();
                nrc.setIdNrc(resultadoConsulta.getInt("idNrc"));
                nrc.setNombreNrc(resultadoConsulta.getString("nombreNrc"));
                nrc.setIdExperienciaEducativa(resultadoConsulta.getInt("idExperienciaEducativa"));
                nrcConsultados.add(nrc);
            }
            ConexionBD.cerrarConexionBD(); 
        } catch (SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al consultar los NRC's.", Codigos.ERROR_CONSULTA);
         }
        return nrcConsultados;  
    }
    
    public ArrayList<Nrc> obtenerNRCSPorExperienciaEducativa(int idExperienciaEducativa) throws DAOException{
        ArrayList<Nrc> nrcConsultados = new ArrayList(); 
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_NRCS_POR_EXPERIENCIA_EDUCATIVA);
            sentencia.setInt(1, idExperienciaEducativa);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while (resultadoConsulta.next()) {
                Nrc nrc = new Nrc();
                nrc.setIdNrc(resultadoConsulta.getInt("idNrc"));
                nrc.setNombreNrc(resultadoConsulta.getString("nombreNrc"));
                nrc.setIdExperienciaEducativa(resultadoConsulta.getInt("idExperienciaEducativa"));
                nrcConsultados.add(nrc);
            }
            ConexionBD.cerrarConexionBD(); 
        } catch (SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al consultar los NRCs.", Codigos.ERROR_CONSULTA);
         }
        return nrcConsultados;  
    }
    
    public ArrayList<Nrc> obtenerNRCSDisponiblesPorEE(int idExperienciaEducativa) throws DAOException{
        ArrayList<Nrc> nrcConsultados = new ArrayList(); 
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_NRCS_DISPONIBLES_POR_EE_PERIODO_ACTUAL);
            sentencia.setInt(1, idExperienciaEducativa);
            sentencia.setInt(2, idExperienciaEducativa);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while (resultadoConsulta.next()) {
                Nrc nrc = new Nrc();
                nrc.setIdNrc(resultadoConsulta.getInt("idNrc"));
                nrc.setNombreNrc(resultadoConsulta.getString("nombreNrc"));
                nrc.setIdExperienciaEducativa(resultadoConsulta.getInt("idExperienciaEducativa"));
                nrcConsultados.add(nrc);
            }
            ConexionBD.cerrarConexionBD(); 
        } catch (SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al consultar los NRCs.", Codigos.ERROR_CONSULTA);
         }
        return nrcConsultados;  
    }
    
    public Nrc obtenerNRC(int idNrc) throws DAOException {
        Nrc nrc = new Nrc();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_NRC);              
            sentencia.setInt(1, idNrc);
            ResultSet resultadoConsulta = sentencia.executeQuery(); 
            if (resultadoConsulta.next()) {
                nrc.setIdNrc(resultadoConsulta.getInt("idNrc"));
                nrc.setNombreNrc(resultadoConsulta.getString("nombreNrc"));
                nrc.setIdExperienciaEducativa(resultadoConsulta.getInt("idExperienciaEducativa"));
            }                     
            ConexionBD.cerrarConexionBD();   
        } catch (SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al consultar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return nrc;
    }
    
    public int guardarNrc(Nrc nrcNuevo) throws DAOException { 
        int respuestaExito = -1;
         try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_NRC);
            sentencia.setString(1, nrcNuevo.getNombreNrc());
            sentencia.setInt(2,nrcNuevo.getIdExperienciaEducativa());
            sentencia.executeUpdate(); 
            ResultSet resultadoOperacion = sentencia.getGeneratedKeys();
            if(resultadoOperacion.next()) {
                respuestaExito = resultadoOperacion.getInt(1);
            }
        ConexionBD.cerrarConexionBD();   
        } catch (SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al registrar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return respuestaExito;
    }
    
    public int actualizarNRC(Nrc nrcEdicion) throws DAOException {
        int respuestaExito = -1;
         try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_NRC);
            sentencia.setString(1, nrcEdicion.getNombreNrc());
            sentencia.setInt(2,nrcEdicion.getIdExperienciaEducativa());
            sentencia.setInt(3, nrcEdicion.getIdNrc());
            sentencia.executeUpdate();   
            respuestaExito = nrcEdicion.getIdNrc();                     
        ConexionBD.cerrarConexionBD();   
        } catch (SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al actualizar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return respuestaExito;
    }
    
    public int eliminarNRC(int idNrc) throws DAOException {
        int respuestaExito = -1;
         try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_NRC);
            sentencia.setInt(1, idNrc);
            respuestaExito = sentencia.executeUpdate();                     
            ConexionBD.cerrarConexionBD();   
        } catch (SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al borrar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return respuestaExito;
    }
    
}
