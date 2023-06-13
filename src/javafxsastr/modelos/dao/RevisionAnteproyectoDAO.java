/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 12/05/2023
 * Descripción: DAO para realizar las consultas a la base de datos
 * correspondientes a las revisiones de anteproyectos. 
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.RevisionAnteproyecto;
import javafxsastr.utils.Codigos;

public class RevisionAnteproyectoDAO {
    private final String OBTENER_REVISION_ANTEPROYECTO = "SELECT * FROM sastr.revisiones_anteproyectos where idAnteproyecto = ?;";
    private final String GUARDAR_REVISION_ANTEPROYECTO = "INSERT INTO `sastr`.`revisiones_anteproyectos` "
            + "(`comentarios`, `idRubrica`, `idAnteproyecto`) VALUES (?, ?, ?);";
    private final String MODIFICAR_REVISION_ANTEPROYECTO = "UPDATE sastr.revisiones_anteproyectos "
            + "SET comentarios = ? "
            + "WHERE idRevisionAnteproyecto = ? ";

    public RevisionAnteproyecto obtenerRevisionAnteproyecto(int idAnteproyecto) throws DAOException {
        RevisionAnteproyecto revisionAnteproyecto = new RevisionAnteproyecto();
        revisionAnteproyecto.setIdRevisionAnteproyecto(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_REVISION_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                revisionAnteproyecto.setIdRevisionAnteproyecto(resultado.getInt("idRevisionAnteproyecto"));
                revisionAnteproyecto.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                revisionAnteproyecto.setIdRubrica(resultado.getInt("idRubrica"));
                revisionAnteproyecto.setComentarios(resultado.getString("comentarios"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            
        }
        return revisionAnteproyecto;
    }
    
    public int guardarRelacionRubricaAnteproyecto(String comentarios, int idAnteproyecto, int idRubrica) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_REVISION_ANTEPROYECTO, 
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1,comentarios);
            sentencia.setInt(2,idRubrica);
            sentencia.setInt(3, idAnteproyecto);
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al eliminar el usuario.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int actualizarRelacionRubricaAnteproyecto(int idRevisionAnteporyecto, String comentarios) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(MODIFICAR_REVISION_ANTEPROYECTO);           
            sentencia.setString(1,comentarios);   
            sentencia.setInt(2,idRevisionAnteporyecto);                     
            int resultado = sentencia.executeUpdate();
            if (resultado != 0) {
                respuesta = resultado;
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al eliminar el usuario.", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
}
