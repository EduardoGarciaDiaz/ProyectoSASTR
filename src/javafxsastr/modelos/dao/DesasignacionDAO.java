/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 19/05/2023
 * Descripción: DAO de las desasignaciones
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Desasignacion;
import javafxsastr.utils.Codigos;


public class DesasignacionDAO {
    
    private final String OBTENER_DESASIGNACIONES = "SELECT d.idDesasignacion, d.motivo, d.comentarios, d.idEstudiante, d.idAnteproyecto, "
            + "CONCAT(u.nombreUsuario,' ',u.primerApellidoUsuario,' ',u.segundoApellidoUsuario) AS nombreCompletoEstudiante, "
            + "a.nombreTrabajoRecepcional "
            + "FROM desasignaciones d "
            + "INNER JOIN estudiantes e ON d.idEstudiante = e.idEstudiante "
            + "INNER JOIN usuarios u ON e.idUsuario = u.idUsuario "
            + "INNER JOIN anteproyectos a ON d.idAnteproyecto = a.idAnteproyecto";
    private final String OBTENER_DESASIGNACION = "SELECT d.idDesasignacion, d.motivo, d.comentarios, d.idEstudiante, d.idAnteproyecto, "
            + "CONCAT(u.nombreUsuario,' ',u.primerApellidoUsuario,' ',u.segundoApellidoUsuario) AS nombreCompletoEstudiante, "
            + "a.nombreTrabajoRecepcional " 
            + "FROM desasignaciones d "
            + "INNER JOIN estudiantes e ON d.idEstudiante = e.idEstudiante " 
            + "INNER JOIN usuarios u ON e.idUsuario = u.idUsuario " 
            + "INNER JOIN anteproyectos a ON d.idAnteproyecto = a.idAnteproyecto" 
            + "WHERE idDesasignacion = ?";
    private final String GUARDAR_DESASIGNACION = "INSERT INTO desasignaciones (motivo, comentarios, idEstudiante, idAnteproyecto) " 
            + "VALUES (?, ?, ?, ?)";
    private final String OBTENER_DESASIGNACIONES_POR_ANTEPROYECTO = "SELECT d.idDesasignacion, d.motivo, d.comentarios, d.idEstudiante, "
            + "d.idAnteproyecto, "
            + "CONCAT(u.nombreUsuario,' ',u.primerApellidoUsuario,' ',u.segundoApellidoUsuario) AS nombreCompletoEstudiante, " 
            + "a.nombreTrabajoRecepcional " 
            + "FROM desasignaciones d " 
            + "INNER JOIN estudiantes e ON d.idEstudiante = e.idEstudiante " 
            + "INNER JOIN usuarios u ON e.idUsuario = u.idUsuario " 
            + "INNER JOIN anteproyectos a ON d.idAnteproyecto = a.idAnteproyecto " 
            + "WHERE d.idAnteproyecto = ?;";
    
    public ArrayList<Desasignacion> obtenerDesasignaciones() throws DAOException {
        ArrayList<Desasignacion> desasignaciones = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_DESASIGNACIONES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Desasignacion desasignacion = new Desasignacion();
                desasignacion.setIdDesasignacion(resultado.getInt("idDesasignacion"));
                desasignacion.setMotivo(resultado.getString("motivo"));
                desasignacion.setComentarios(resultado.getString("comentarios"));
                desasignacion.setIdEstudiante(resultado.getInt("idEstudiante"));
                desasignacion.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                desasignacion.setNombreEstudiante(resultado.getString("nombreCompletoEstudiante"));
                desasignacion.setNombreAnteproyecto(resultado.getString("nombreTrabajoRecepcional"));
                desasignaciones.add(desasignacion);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return desasignaciones;
    }
    
    public Desasignacion obtenerDesasignacion(int idDesasignacion) throws DAOException {
        Desasignacion desasignacion = new Desasignacion();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_DESASIGNACION);
            sentencia.setInt(1, idDesasignacion);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                desasignacion.setIdDesasignacion(resultado.getInt("idDesasignacion"));
                desasignacion.setMotivo(resultado.getString("motivo"));
                desasignacion.setComentarios(resultado.getString("comentarios"));
                desasignacion.setIdEstudiante(resultado.getInt("idEstudiante"));
                desasignacion.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                desasignacion.setNombreEstudiante(resultado.getString("nombreCompletoEstudiante"));
                desasignacion.setNombreAnteproyecto(resultado.getString("nombreTrabajoRecepcional"));
            }
            ConexionBD.cerrarConexionBD();           
        } catch (SQLException ex) {
           throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return desasignacion;
    }
    
    public int guardarDesasignacion(Desasignacion desasignacion) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_DESASIGNACION,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, desasignacion.getMotivo());
            sentencia.setString(2, desasignacion.getComentarios());
            sentencia.setInt(3, desasignacion.getIdEstudiante());
            sentencia.setInt(4, desasignacion.getIdAnteproyecto());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
           throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public ArrayList<Desasignacion> obtenerDesasignacionesPorIdAnteproyecto(int idAnteproyecto) throws DAOException {
        ArrayList<Desasignacion> desasignaciones = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_DESASIGNACIONES_POR_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Desasignacion desasignacion = new Desasignacion();
                desasignacion.setIdDesasignacion(resultado.getInt("idDesasignacion"));
                desasignacion.setMotivo(resultado.getString("motivo"));
                desasignacion.setComentarios(resultado.getString("comentarios"));
                desasignacion.setIdEstudiante(resultado.getInt("idEstudiante"));
                desasignacion.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                desasignacion.setNombreEstudiante(resultado.getString("nombreCompletoEstudiante"));
                desasignacion.setNombreAnteproyecto(resultado.getString("nombreTrabajoRecepcional"));
                desasignaciones.add(desasignacion);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return desasignaciones;
    }

}