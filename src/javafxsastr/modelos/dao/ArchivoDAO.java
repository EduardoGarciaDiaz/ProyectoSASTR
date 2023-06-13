/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase DAO de los archivos
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Archivo;
import javafxsastr.utils.Codigos;

public class ArchivoDAO {
    
    private final String CONSULTA_ARCHIVOS = "Select idArchivo, nombreArchivo, archivo, esEntrega From archivos";
    private final String CONSULTA_ARCHIVO_UNICO = "Select idArchivo, nombreArchivo, archivo, esEntrega, From archivos " 
                            + "where idArchivo =? ";
    private final String REGISTRAR_ARCHIVO = "Insert into archivos(nombreArchivo, archivo, esEntrega, idEntrega) Values (?,?,?,?)";
    private final String ACTUALIZAR_ARCHIVO = "Update archivos set nombreArchivo = ?, archivo = ?, esEntrega = ?, idEntrega = ? " 
                            + " where idArchivo = ?";
    private final String ELIMINAR_ARCHIVO = "Delete from archivos where idEntrega = ? AND esEntrega = 0";
    private final String CONSULTAR_ARCHIVOS_POR_ENTREGA = "Select idArchivo, nombreArchivo, archivo, esEntrega, archivos.idEntrega " 
                            + "From archivos "
                            + "Inner Join entregas On archivos.idEntrega = entregas.idEntrega "
                            + "Where archivos.idEntrega = ?; ";
     private final String ELIMINAR_ARCHIVO_ENTREGA = "Delete from archivos where idArchivo = ? ";
    
    public ArrayList<Archivo> consultarArchivos() throws DAOException {
        ArrayList<Archivo> archivosConsultados = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(CONSULTA_ARCHIVOS);
            ResultSet resultadoConsultaArchivos = sentencia.executeQuery();
            while (resultadoConsultaArchivos.next()) {
                Archivo archivo = new Archivo();
                archivo.setIdArchivo(resultadoConsultaArchivos.getInt("idArchivo"));
                archivo.setNombreArchivo(resultadoConsultaArchivos.getString("nombreArchivo"));
                archivo.setArchivo(resultadoConsultaArchivos.getBytes("archivo"));
                archivo.setEsEntrega(resultadoConsultaArchivos.getBoolean("esEntrega"));
                archivo.setIdEntrega(resultadoConsultaArchivos.getInt("idArchivo"));
                archivosConsultados.add(archivo);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion de los archivos.",
                                     Codigos.ERROR_CONSULTA);
        }               
        return archivosConsultados;
    }
    
    public Archivo consultarArchivoUnico(int idArchivo) throws DAOException {
        Archivo archivo = new Archivo();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(CONSULTA_ARCHIVO_UNICO);
            sentencia.setInt(1, idArchivo);
            ResultSet resultadoConsultaArchivos = sentencia.executeQuery();
            if (resultadoConsultaArchivos.next()) {                
                archivo.setIdArchivo(resultadoConsultaArchivos.getInt("idArchivo"));
                archivo.setNombreArchivo(resultadoConsultaArchivos.getString("nombreArchivo"));
                archivo.setArchivo(resultadoConsultaArchivos.getBytes("archivo"));
                archivo.setEsEntrega(resultadoConsultaArchivos.getBoolean("esEntrega"));
                archivo.setIdEntrega(resultadoConsultaArchivos.getInt("idEntrega"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion del archivo.",
                                     Codigos.ERROR_CONSULTA);
        }
        return archivo;
    }
    
    public int guardarArchivo(Archivo archivoNuevo) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(REGISTRAR_ARCHIVO,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1,archivoNuevo.getNombreArchivo());
            sentencia.setBytes(2,archivoNuevo.getArchivo());
            sentencia.setBoolean(3,archivoNuevo.getEsEntrega());
            sentencia.setInt(4,archivoNuevo.getIdEntrega());
            sentencia.executeUpdate();
            ResultSet resultadoOperacion = sentencia.getGeneratedKeys();
            if (resultadoOperacion.next()) {
                respuestaExito = resultadoOperacion.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al registrar este archivo", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }
    
    public int actualizarArchivo(Archivo archivoEdicion) throws DAOException {
        int respuestaExito = -1;
        try {            
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_ARCHIVO);
            sentencia.setString(1, archivoEdicion.getNombreArchivo());
            sentencia.setBytes(2, archivoEdicion.getArchivo());
            sentencia.setBoolean(3, archivoEdicion.getEsEntrega());
            sentencia.setInt(4, archivoEdicion.getIdEntrega());
            sentencia.setInt(5, archivoEdicion.getIdArchivo());
            sentencia.executeUpdate();
            respuestaExito = archivoEdicion.getIdArchivo();
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al actualizar este Archivo", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }
    
    public int borrarArchivosRevision(int idArchivoBorrar) throws DAOException {
        int respuestaExito = -1;
        try {            
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_ARCHIVO);
            sentencia.setInt(1, idArchivoBorrar);
            respuestaExito = sentencia.executeUpdate();
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
           throw new DAOException("Lo sentimos, hubo un problema al borrar este Archivo.", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }
    
    public ArrayList<Archivo> consultarArchivosPorEntrega(int idEntrega) throws DAOException {
        ArrayList<Archivo> archivosEntrega = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(CONSULTAR_ARCHIVOS_POR_ENTREGA);
            sentencia.setInt(1, idEntrega);
            ResultSet resultadoConsultaArchivos = sentencia.executeQuery();
            while (resultadoConsultaArchivos.next()) {
                Archivo archivo = new Archivo();
                archivo.setIdArchivo(resultadoConsultaArchivos.getInt("idArchivo"));
                archivo.setNombreArchivo(resultadoConsultaArchivos.getString("nombreArchivo"));
                archivo.setArchivo(resultadoConsultaArchivos.getBytes("archivo"));
                archivo.setEsEntrega(resultadoConsultaArchivos.getBoolean("esEntrega"));
                archivo.setIdEntrega(resultadoConsultaArchivos.getInt("idEntrega"));
                System.out.println(archivo.getNombreArchivo());
                archivosEntrega.add(archivo);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar la informacion de los archivos.",
                                     Codigos.ERROR_CONSULTA);
        }               
        return archivosEntrega;
    }
    
     public int borrarArchivosEntrega(int idArchivoBorrar) throws DAOException {
        int respuestaExito = -1;
        try {            
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_ARCHIVO_ENTREGA);
            sentencia.setInt(1, idArchivoBorrar);
            respuestaExito = sentencia.executeUpdate();
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
           throw new DAOException("Lo sentimos, hubo un problema al borrar este Archivo.", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }
    
}
