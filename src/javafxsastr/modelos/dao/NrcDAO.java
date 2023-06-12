/*
 * Autor: Tritan Eduardo Suarez Santiago
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
    
    private final String Consulta_Nrc = "Select idEntrega,comentarioAlumno, fehchaEntrega from entregas";
    private final String Consulta_Nrc_Unico = "Select idNrc, nombreNrc, idExperienciaEducativa from nrcs where idNrc = ?";
    private final String Registrar_Nrc = "Insert into nrc(nombreNrc, idExperienciaEducativa) Values (?,?)";
    private final String Actualizar_Nrc = "Update nrcs set nombreNrc = ?, idExperienciaEducativa = ? where idNrc = ?";
    private final String Borrar_Nrc = "Delete from nrcs where idNrc = ?";
    private final String OBTENER_NRCS_POR_EXPERIENCIA_EDUCATIVA = "SELECT * FROM sastr.nrcs where idExperienciaEducativa = ?;";
     
    public ArrayList<Nrc> obtenerInformacionNRCS() throws DAOException{
        ArrayList<Nrc> nrcConsultados = new ArrayList(); 
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Consulta_Nrc);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while(resultadoConsulta.next()) {
                Nrc nrc = new Nrc();
                nrc.setIdNrc(resultadoConsulta.getInt("idNrc"));
                nrc.setNombreNrc(resultadoConsulta.getString("nombreNrc"));
                nrc.setIdExperienciaEducativa(resultadoConsulta.getInt("idExperienciaEducativa"));
                nrcConsultados.add(nrc);
            }
            ConexionBD.cerrarConexionBD(); 
        }catch(SQLException ex){
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
            while(resultadoConsulta.next()) {
                Nrc nrc = new Nrc();
                nrc.setIdNrc(resultadoConsulta.getInt("idNrc"));
                nrc.setNombreNrc(resultadoConsulta.getString("nombreNrc"));
                nrc.setIdExperienciaEducativa(resultadoConsulta.getInt("idExperienciaEducativa"));
                nrcConsultados.add(nrc);
            }
            ConexionBD.cerrarConexionBD(); 
        }catch(SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al consultar los NRCs.", Codigos.ERROR_CONSULTA);
         }
        return nrcConsultados;  
    }
    
    public Nrc obtenerNRC(int idNrc) throws DAOException {
        Nrc nrc = new Nrc();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Consulta_Nrc_Unico);              
            sentencia.setInt(1, idNrc);
            ResultSet resultadoConsulta = sentencia.executeQuery(); 
            if(resultadoConsulta.next()) {
                nrc.setIdNrc(resultadoConsulta.getInt("idNrc"));
                nrc.setNombreNrc(resultadoConsulta.getString("nombreNrc"));
                nrc.setIdExperienciaEducativa(resultadoConsulta.getInt("idExperienciaEducativa"));
            }                     
            ConexionBD.cerrarConexionBD();   
        }catch(SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al consultar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return nrc;
    }
    
    public int guardarNrc(Nrc nrcNuevo) throws DAOException { 
        int respuestaExito = -1;
         try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Registrar_Nrc);
            sentencia.setString(1, nrcNuevo.getNombreNrc());
            sentencia.setInt(2,nrcNuevo.getIdExperienciaEducativa());
            sentencia.executeUpdate(); 
            ResultSet resultadoOperacion = sentencia.getGeneratedKeys();
            if(resultadoOperacion.next()) {
                respuestaExito = resultadoOperacion.getInt(1);
            }
        ConexionBD.cerrarConexionBD();   
        }catch(SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al registrar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return respuestaExito;
    }
    
    public int actualizarNRC(Nrc nrcEdicion) throws DAOException {
        int respuestaExito = -1;
         try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Actualizar_Nrc);
            sentencia.setString(1, nrcEdicion.getNombreNrc());
            sentencia.setInt(2,nrcEdicion.getIdExperienciaEducativa());
            sentencia.setInt(3, nrcEdicion.getIdNrc());
            sentencia.executeUpdate();   
            respuestaExito = nrcEdicion.getIdNrc();                     
        ConexionBD.cerrarConexionBD();   
        }catch(SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al actualizar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return respuestaExito;
    }
    
    public int eliminarNRC(int idNrc) throws DAOException {
        int respuestaExito = -1;
         try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Borrar_Nrc);
            sentencia.setInt(1, idNrc);
            respuestaExito = sentencia.executeUpdate();                     
            ConexionBD.cerrarConexionBD();   
        }catch(SQLException ex){
            throw new DAOException("Lo sentimos, hubo un problema al borrar este NRC.", Codigos.ERROR_CONSULTA);
         }
        return respuestaExito;
    }
    
}
