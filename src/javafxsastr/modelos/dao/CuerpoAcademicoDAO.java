/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 21/05/2023
 * Descripción: DAO de Cuerpo Academico.
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Area;
import javafxsastr.modelos.pojo.CuerpoAcademico;
import javafxsastr.modelos.pojo.CuerpoAcademicoLgac;
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
            + "ca.idAcademico, ca.idArea, ca.descripcion "
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
    private final String AÑADIR_CUERPO_ACADEMICO = "Insert into cuerpos_academicos(nombreCuerpoAcademico, disciplinaCuerpoAcademico, "
            + "idArea, idAcademico, descripcion) values (?, ?, ?, ?, ?)";
    private final String AÑADIR_RELACION_LGACS_CA = "insert into cuerpos_academicos_lgac (idCuerpoAcademico, idLgac) values (?,?)";
    private final String VERIFICAR_SI_EL_AREA_EXISTE = "SELECT EXISTS (SELECT idArea FROM areas WHERE nombreArea = ?) as existeArea";
    private final String AÑADIR_AREA = "Insert into areas(nombreArea) values (?)";
    private final String VERIFICAR_SI_CA_EXISTE = "SELECT EXISTS"
            + "(SELECT idCuerpoAcademico FROM cuerpos_academicos WHERE nombreCuerpoAcademico = ?) as existeCA";
    private final String OBTENER_AREAS_CUERPOS_ACADEMICOS = "Select idArea, nombreArea from areas";
    private final String ACTUALIZA_CUERPO_ACADEMICO = "Update cuerpos_academicos set cuerpos_academicos.nombreCuerpoAcademico = ?, \n"+
            "cuerpos_academicos.disciplinaCuerpoAcademico = ?, cuerpos_academicos.idArea = ?,\n" +
            "cuerpos_academicos.idAcademico = ?, cuerpos_academicos.descripcion = ?\n" +
            "where cuerpos_academicos.idCuerpoAcademico = ?;";
    private final String VERIFICAR_RELACION_CA_LGAC = "Select exists(Select idCuerpoAcademicoLgac from cuerpos_academicos_lgac\n"+
            " where cuerpos_academicos_lgac.idLgac = ? and cuerpos_academicos_lgac.idCuerpoAcademico = ?)  as existeRelacion";
    private final String OBTENER_RELACIONES_LGAC_CA = "Select idCuerpoAcademicoLgac, idLgac, idCuerpoAcademico from cuerpos_academicos_lgac\n"
            +
            " where cuerpos_academicos_lgac.idCuerpoAcademico = ?";
    private final String Eliminar_Relacion_LGAC_CA = "delete from cuerpos_academicos_lgac where idCuerpoAcademicoLgac = ?";

    public boolean verificarSiAcademicoEsResponsableDeCA(int idAcademico) throws DAOException {
        boolean esResponsableDeCA = false;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(VERIFICAR_SI_ACADEMICO_ES_RCA);
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

    public ArrayList<CuerpoAcademico> obtenerCuerposAcademicos() throws DAOException {
        ArrayList<CuerpoAcademico> cuerposAcademicos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CUERPOS_ACADEMICOS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                cuerposAcademicos.add(new CuerpoAcademico(
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getString("nombreCuerpoAcademico"),
                        resultado.getString("disciplinaCuerpoAcademico"),
                        resultado.getString("descripcion"),
                        resultado.getString("nombreArea"),
                        resultado.getInt("idArea"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreResponsable")));
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
                       resultado.getString(" "),
                        resultado.getString("nombreArea"),
                        resultado.getInt("idArea"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreResponsable")             
                );
            }
        }catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return cuerpoAcademico;
    }

    public boolean verificarSiAreaExiste(String nombreArea) throws DAOException {
        boolean existeArea = false;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(VERIFICAR_SI_EL_AREA_EXISTE);
            sentencia.setString(1, nombreArea);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                existeArea = resultado.getBoolean("existeArea");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return existeArea;
    }

    public boolean verificarSiCuerpoAcademicoExiste(String nombreCA) throws DAOException {
        boolean existeCA = false;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(VERIFICAR_SI_CA_EXISTE);
            sentencia.setString(1, nombreCA);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                existeCA = resultado.getBoolean("existeCA");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return existeCA;
    }

    public int agregarCuerpoAcademico(CuerpoAcademico cuNuevo) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(AÑADIR_CUERPO_ACADEMICO,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, cuNuevo.getNombreCuerpoAcademico());
            sentencia.setString(2, cuNuevo.getDisciplinaCuerpoAcademico());
            sentencia.setInt(3, cuNuevo.getIdArea());
            sentencia.setInt(4, cuNuevo.getIdAcademico());
            sentencia.setString(5, cuNuevo.getDescripcion());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuestaExito = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);

        }
        return respuestaExito;
    }

    public int agregarArea(String area) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(AÑADIR_AREA,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, area);
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            respuestaExito = resultado.getInt(1);
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }

    public ArrayList<Area> obtenerAreas() throws DAOException {
        ArrayList<Area> areas = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_AREAS_CUERPOS_ACADEMICOS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                areas.add(new Area(resultado.getInt("idArea"),
                        resultado.getString("nombreArea")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return areas;
    }

    public int agregarRelacionCUconLgac(int idCA, int idLgac) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(AÑADIR_RELACION_LGACS_CA,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, idCA);
            sentencia.setInt(2, idLgac);
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuestaExito = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }

    public int actualizarCuerpoAcademico(CuerpoAcademico cuEdicion) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZA_CUERPO_ACADEMICO);
            sentencia.setString(1, cuEdicion.getNombreCuerpoAcademico());
            sentencia.setString(2, cuEdicion.getDisciplinaCuerpoAcademico());
            sentencia.setInt(3, cuEdicion.getIdArea());
            sentencia.setInt(4,cuEdicion.getIdAcademico());
            sentencia.setString(5, cuEdicion.getDescripcion());
            sentencia.setInt(6, cuEdicion.getIdCuerpoAcademico());
            int filas = sentencia.executeUpdate();
            if (filas != 0) {
                respuestaExito = filas;
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de Actualizaicion", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }

    public int verificarRelacionCaLgac(int idLgac, int idCa) throws DAOException {
        int respuesta = 0;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(VERIFICAR_RELACION_CA_LGAC);
            sentencia.setInt(1, idLgac);
            sentencia.setInt(2, idCa);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()) {
                respuesta = resultado.getByte("existeRelacion");
            }            
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();;
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public ArrayList<CuerpoAcademicoLgac> obtenerRelacionesLgacCa(int idCa) throws DAOException {
        ArrayList<CuerpoAcademicoLgac> relaciones = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_RELACIONES_LGAC_CA);
            sentencia.setInt(1, idCa);
            ResultSet reusltado = sentencia.executeQuery();
            while (reusltado.next()) {
                CuerpoAcademicoLgac ca = new CuerpoAcademicoLgac();
                ca.setIdCuerpoAcademicoLgac(reusltado.getInt("idCuerpoAcademicoLgac"));
                ca.setIdCuerpoAcademico(reusltado.getInt("idCuerpoAcademico"));
                ca.setLgac(reusltado.getInt("idLgac"));
                relaciones.add(ca);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return relaciones;
    }

    public void eliminarRelacionCuerpoLgac(int idRelacion) throws DAOException {
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(Eliminar_Relacion_LGAC_CA);
            sentencia.setInt(1, idRelacion);
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error de eliminacion", Codigos.ERROR_CONSULTA);
        }

    }
}
