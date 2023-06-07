/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 21/05/2023
 * Descripción: DAO de Cuerpo Academico.
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.CuerpoAcademico;
import javafxsastr.utils.Codigos;

/**
 *
 * @author Daniel García Arcos
 */
public class CuerpoAcademicoDAO {
    
    private final String VERIFICAR_SI_ACADEMICO_ES_RCA = "SELECT EXISTS"
            + "(SELECT idAcademico FROM cuerpos_academicos WHERE idAcademico = ?) as esResponsableDeCA;";
    private final String OBTENER_CUERPOS_ACADEMICOS = "SELECT idCuerpoAcademico, nombreCuerpoAcademico, disciplinaCuerpoAcademico, "
        + "nombreArea, concat(nombreUsuario,' ', primerApellidoUsuario,' ',segundoApellidoUsuario) as nombreResponsable, "
        + "ca.idAcademico, ca.idArea " 
        + "FROM sastr.cuerpos_academicos ca "
        + "INNER JOIN areas a "
        + "ON ca.idArea = a.idArea "
        + "inner join academicos acad " 
        + "on acad.idAcademico = ca.idAcademico "
        + "inner join usuarios u "
        + "on u.idUsuario = acad.idUsuario; ";
    private final String OBTENER_CUERPO_ACADEMICO_POR_RCA = "SELECT idCuerpoAcademico, nombreCuerpoAcademico, disciplinaCuerpoAcademico, "
        + "nombreArea, concat(nombreUsuario,' ', primerApellidoUsuario,' ',segundoApellidoUsuario) as nombreResponsable, "
        + "ca.idAcademico, ca.idArea " 
        + "FROM sastr.cuerpos_academicos ca "
        + "INNER JOIN areas a "
        + "ON ca.idArea = a.idArea "
        + "inner join academicos acad " 
        + "on acad.idAcademico = ca.idAcademico "
        + "inner join usuarios u "
        + "on u.idUsuario = acad.idUsuario "
            + " where ca.idAcademico = ?; ";
    public boolean verificarSiAcademicoEsResponsableDeCA(int idAcademico) throws DAOException {
        boolean esResponsableDeCA = false;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(VERIFICAR_SI_ACADEMICO_ES_RCA);
            sentencia.setInt(1, idAcademico);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                esResponsableDeCA = resultado.getBoolean("esResponsableDeCA");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return esResponsableDeCA;
    }
    
    public ArrayList<CuerpoAcademico> obtenerCuerposAcademicos() throws DAOException{
        ArrayList<CuerpoAcademico> cuerposAcademicos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CUERPOS_ACADEMICOS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                cuerposAcademicos.add(new CuerpoAcademico(
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getString("nombreCuerpoAcademico"),
                        resultado.getString("disciplinaCuerpoAcademico"),
                        resultado.getString("nombreArea"),
                        resultado.getInt("idArea"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreResponsable")             
                ));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cuerposAcademicos;
    }
    
    public CuerpoAcademico obtenerCuerpoAcademicoPorResponsable(int idAcademico) throws DAOException {
        CuerpoAcademico cuerpoAcademico = new CuerpoAcademico();
        cuerpoAcademico.setIdCuerpoAcademico(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CUERPO_ACADEMICO_POR_RCA);
            sentencia.setInt(1, idAcademico);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
               cuerpoAcademico = new CuerpoAcademico(
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getString("nombreCuerpoAcademico"),
                        resultado.getString("disciplinaCuerpoAcademico"),
                        resultado.getString("nombreArea"),
                        resultado.getInt("idArea"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreResponsable")             
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return cuerpoAcademico;
    }
}
