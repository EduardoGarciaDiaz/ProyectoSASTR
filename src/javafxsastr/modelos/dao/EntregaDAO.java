/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase DAO de las Entregas
 */
package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.utils.Codigos;


public class EntregaDAO {
    
    private static final String OBTENER_ENTREGAS = "Select idEntrega, comentarioAlumno, fechaEntrega, horaEntrega, comentariosDirector, " +
                                              "fechaRevision, horaRevision, idActividad, idAcademico form entregas";
    private static final String OBTENER_ENTREGAS_UNICAS = "Select comentarioAlumno, fechaEntrega, horaEntrega, comentariosDirector "+
                                              "fechaRevision, horaRevision, idActividad from entregas where idEntrega = ?";
    private static final String GUARDAR_ENTREGA = "Insert into entregas(comentarioAlumno, fechaEntrega, horaEntrega, comentariosDirector, "+
                                              "fechaRevision, horaRevision, idActividad, idAcademico) Values (?,?,?,?,?,?,?,?)";
    private static final String ACTUALIZAR_ENTREGA = "Update entregas set comentarioAlumno = ?, fechaEntrega = ?, horaEntrega = ?, "+
                                               "comentariosDirector = ?, fechaRevision = ?, horaRevision = ?, idActividad = ?, "+
                                               "idAcademico = ? where idEntrega = ?";
    private static final String ELIMINAR_ENTREGA = "Delete from entregas where idEntrega = ?";
    private static final String OBTENER_ENTREGAS_POR_ACTIVIDAD = "SELECT e.idEntrega, e.comentarioAlumno, e.fechaEntrega, e.horaEntrega, " +
                                                " e.comentariosDirector, " +
                                                "e.fechaRevision, e.horaRevision, e.idActividad, e.idAcademico FROM entregas e " +
                                                "INNER JOIN actividades a ON e.idActividad = a.idActividad " +
                                                "WHERE a.idActividad = ?;";
    
    public ArrayList<Entrega> consultarEntregas() throws DAOException {
        ArrayList<Entrega> entregasConsultadas = new ArrayList();
        try {            
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ENTREGAS);
            ResultSet resultadoConsultaEntregas = sentencia.executeQuery();
            while(resultadoConsultaEntregas.next()) {
                Entrega entrega = new Entrega();
                entrega.setIdEntrega(resultadoConsultaEntregas.getInt("idEntrega"));
                entrega.setComentarioAlumno(resultadoConsultaEntregas.getString("comentarioAlumno"));
                entrega.setFechaEntrega(resultadoConsultaEntregas.getString("fechaEntrega"));
                entrega.setHoraEntrega(resultadoConsultaEntregas.getString("horaEntrega"));
                entrega.setComentarioDirector(resultadoConsultaEntregas.getString("comentarioDirector"));
                entrega.setFechaRevision(resultadoConsultaEntregas.getString("fechaRevision"));
                entrega.setHoraRevision(resultadoConsultaEntregas.getString("horaEntrega"));
                entrega.setIdActividad(resultadoConsultaEntregas.getInt("idActividad"));
                entrega.setIdAcademico(resultadoConsultaEntregas.getInt("idAcademico"));
                entregasConsultadas.add(entrega);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
             throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion de las entregas.",
                                     Codigos.ERROR_CONSULTA);
        }
        return entregasConsultadas;        
    }
    
    public Entrega consultarEntregaUnica(int idEntrega) throws DAOException {
        Entrega entregaConsultada = new Entrega();;
        try {            
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ENTREGAS_UNICAS);
            sentencia.setInt(1, idEntrega);
            ResultSet resultadoConsultaEntregaUnica = sentencia.executeQuery();
            if(resultadoConsultaEntregaUnica.next()) {               
                entregaConsultada.setIdEntrega(idEntrega);
                entregaConsultada.setComentarioAlumno(resultadoConsultaEntregaUnica.getString("comentarioAlumno"));
                entregaConsultada.setFechaEntrega(resultadoConsultaEntregaUnica.getString("fechaEntrega"));
                entregaConsultada.setHoraEntrega(resultadoConsultaEntregaUnica.getString("horaEntrega"));
                entregaConsultada.setComentarioDirector(resultadoConsultaEntregaUnica.getString("comentarioDirector"));
                entregaConsultada.setFechaRevision(resultadoConsultaEntregaUnica.getString("fechaRevision"));
                entregaConsultada.setHoraRevision(resultadoConsultaEntregaUnica.getString("horaEntrega"));
                entregaConsultada.setIdActividad(resultadoConsultaEntregaUnica.getInt("idActividad"));
                entregaConsultada.setIdAcademico(resultadoConsultaEntregaUnica.getInt("idAcademico"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion de esta entrega.",
                                     Codigos.ERROR_CONSULTA);
        }
        return entregaConsultada;
    }
    
    public ArrayList<Entrega> consultarEntregasPorActividad(int idActividad) throws DAOException {
        ArrayList<Entrega> entregas = new ArrayList<>();
        try {            
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ENTREGAS_POR_ACTIVIDAD);
            sentencia.setInt(1, idActividad);
            ResultSet resultadoConsultaEntregaUnica = sentencia.executeQuery();
            while(resultadoConsultaEntregaUnica.next()) {               
                Entrega entregaConsultada = new Entrega();
                entregaConsultada.setIdEntrega(resultadoConsultaEntregaUnica.getInt("idEntrega"));
                entregaConsultada.setComentarioAlumno(resultadoConsultaEntregaUnica.getString("comentarioAlumno"));
                entregaConsultada.setFechaEntrega(resultadoConsultaEntregaUnica.getString("fechaEntrega"));
                entregaConsultada.setHoraEntrega(resultadoConsultaEntregaUnica.getString("horaEntrega"));
                entregaConsultada.setComentarioDirector(resultadoConsultaEntregaUnica.getString("comentariosDirector"));
                entregaConsultada.setFechaRevision(resultadoConsultaEntregaUnica.getString("fechaRevision"));
                entregaConsultada.setHoraRevision(resultadoConsultaEntregaUnica.getString("horaEntrega"));
                entregaConsultada.setIdActividad(resultadoConsultaEntregaUnica.getInt("idActividad"));
                entregaConsultada.setIdAcademico(resultadoConsultaEntregaUnica.getInt("idAcademico"));
                entregas.add(entregaConsultada);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion de esta entrega.",
                                     Codigos.ERROR_CONSULTA);
        }
        return entregas;
    }
    
    public int registrarEntrega(Entrega entregaNueva) throws DAOException {
        int respuestaExito =-1;
        try {            
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_ENTREGA,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1,entregaNueva.getComentarioAlumno());
            sentencia.setString(2,entregaNueva.getFechaEntrega());
            sentencia.setString(3,entregaNueva.getHoraEntrega());
            sentencia.setString(4,entregaNueva.getComentarioDirector());
            sentencia.setString(5,entregaNueva.getFechaRevision());
            sentencia.setString(6,entregaNueva.getHoraRevision());
            sentencia.setInt(7,entregaNueva.getIdActividad());
            sentencia.setInt(8,entregaNueva.getIdAcademico());
            sentencia.executeUpdate();
            ResultSet resultadoRegistro  = sentencia.getGeneratedKeys();
            if(resultadoRegistro.next()) {
                respuestaExito = resultadoRegistro.getInt(1);
            }                
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
           throw new DAOException("Lo sentimos, hubo un problema al registrar la entrega.",
                                     Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }
    
    public int actualizarEntrega(Entrega entregaEdicion) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_ENTREGA);
            sentencia.setString(1,entregaEdicion.getComentarioAlumno());
            sentencia.setString(2,entregaEdicion.getFechaEntrega());
            sentencia.setString(3,entregaEdicion.getHoraEntrega());
            sentencia.setString(4,entregaEdicion.getComentarioDirector());
            sentencia.setString(5,entregaEdicion.getFechaRevision());
            sentencia.setString(6,entregaEdicion.getHoraRevision());
            sentencia.setInt(7,entregaEdicion.getIdActividad());
            sentencia.setInt(8,entregaEdicion.getIdAcademico());
            sentencia.setInt(9,entregaEdicion.getIdEntrega());
            int filasAfectadas = sentencia.executeUpdate();
            if(filasAfectadas == 1) {
                respuestaExito = entregaEdicion.getIdEntrega();
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al actualizarla entrega.",
                                     Codigos.ERROR_CONSULTA);
        }    
        return respuestaExito;
    }
    
    public int eliminarEntrega(int idEntrega) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_ENTREGA);
            sentencia.setInt(1,idEntrega);
            int filasAfectadas = sentencia.executeUpdate();
            if(filasAfectadas == 1) {
                respuestaExito = 1;
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al eliminar la entrega.",
                                     Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    } 
    
}
