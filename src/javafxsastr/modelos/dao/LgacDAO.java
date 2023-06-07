/*
 * Autor: Tritan Eduardo Suarez Santiago
 * Fecha de creación: 15/05/2023
 * Descripción: Clase DAO de la Lgac
 */
package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.utils.Codigos;

public class LgacDAO {

    private static String CONSULTAR_LGAC = "Select idLgac, nombreLgac, descripcionLgac from lgacs";
    private static String CONSULTAR_LGAC_UNICA = "Select idLgac, nombreLgac, descripcionLgac from lgacs where idLgac = ?";
    private static String CONSULTAR_LGAC_POR_CUERPO_ACADEMICO = "Select lgacs.idLgac, nombreLgac, descripcionLgac from lgacs \n"
            +"Inner join cuerpos_academicos_lgac On lgacs.idlgac = cuerpos_academicos_lgac.idLgac\n" +
            "Where cuerpos_academicos_lgac.idCuerpoAcademico = ?";
    private static String REGISTRAR_LGAC = "Insert into lgacs(nombreLgac, descripcionLgac) Values (?,?)";
    private static String ACTUALIZAR_LGAC = "Update lgacs set nombreLgac = ?, descripcionLgac = ? where idLgac = ?";
    private static String BORRAR_LGAC = "Delete from lgacs where idLgac = ?";
    private static String CONSULTA_LGACS_POR_CUERPO_ACADEMICO = "select lgacs.idLgac, nombreLgac, descripcionLgac "
            + "from lgacs "
            + "inner join cuerpos_academicos_lgac "
            + "on lgacs.idLgac = cuerpos_academicos_lgac.idLgac "
            + "inner join cuerpos_academicos "
            + "on cuerpos_academicos.idCuerpoAcademico = cuerpos_academicos_lgac.idCuerpoAcademico "
            + "where cuerpos_academicos.idCuerpoAcademico = ?; ";
    private final String OBTENER_LGACS_POR_ANTEPROYECTO = "select lgacs.idLgac, nombreLgac, descripcionLgac "
            + "from lgacs "
            + "inner join lgacs_anteproyectos "
            + "on lgacs.idLgac = lgacs_anteproyectos.idLgac "
            + "inner join anteproyectos "
            + "on anteproyectos.idAnteproyecto = lgacs_anteproyectos.idAnteproyecto "
            + "where anteproyectos.idAnteproyecto = ?; ";
    private final String CONSULTAR_LGAC_POR_ANTEPORYECTO = "select lgacs.idLgac, nombreLgac, descripcionLgac \n" +
            "from lgacs \n" +
            "inner join lgacs_anteproyectos on lgacs.idLgac = lgacs_anteproyectos.idLgac \n" +
            "where lgacs_anteproyectos.idAnteproyecto = ?";

    public ArrayList<Lgac> obtenerInformacionLGCAS() throws DAOException {
        ArrayList<Lgac> lgacConsultados = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(CONSULTAR_LGAC);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while (resultadoConsulta.next()) {
                // lgacConsultados = ;
                Lgac lgac = new Lgac();
                lgac.setIdLgac(resultadoConsulta.getInt("idLgac"));
                lgac.setNombreLgac(resultadoConsulta.getString("nombreLgac"));
                lgac.setDescripcionLgac(resultadoConsulta.getString("descripcionLgac"));
                lgacConsultados.add(lgac);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al consultar las LGAC's.", Codigos.ERROR_CONSULTA);
        }
        return lgacConsultados;
    }

    public ArrayList<Lgac> obtenerInformacionLGACsPorCuerpoAcademico(int idCuerpoAcademico) throws DAOException {
        ArrayList<Lgac> lgacConsultados = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(CONSULTA_LGACS_POR_CUERPO_ACADEMICO);
            sentencia.setInt(1, idCuerpoAcademico);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while (resultadoConsulta.next()) {
                Lgac lgac = new Lgac();
                lgac.setIdLgac(resultadoConsulta.getInt("idLgac"));
                lgac.setNombreLgac(resultadoConsulta.getString("nombreLgac"));
                lgac.setDescripcionLgac(resultadoConsulta.getString("descripcionLgac"));
                lgacConsultados.add(lgac);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al consultar las LGAC's.", Codigos.ERROR_CONSULTA);
        }
        return lgacConsultados;
    }

    public ArrayList<Lgac> obtenerInformacionLGACsPorAnteproyecto(int idAnteproyecto) throws DAOException {
        ArrayList<Lgac> lgacConsultados = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_LGACS_POR_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            while (resultadoConsulta.next()) {
                Lgac lgac = new Lgac();
                lgac.setIdLgac(resultadoConsulta.getInt("idLgac"));
                lgac.setNombreLgac(resultadoConsulta.getString("nombreLgac"));
                lgac.setDescripcionLgac(resultadoConsulta.getString("descripcionLgac"));
                lgacConsultados.add(lgac);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al consultar las LGAC's.", Codigos.ERROR_CONSULTA);
        }
        return lgacConsultados;
    }

    public Lgac obtenerInformacionLGAC(int idLgac) throws DAOException {
        Lgac lgac = new Lgac();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(CONSULTAR_LGAC_UNICA);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            sentencia.setInt(1, idLgac);
            if (resultadoConsulta.next()) {
                lgac.setIdLgac(resultadoConsulta.getInt("idLgac"));
                lgac.setNombreLgac(resultadoConsulta.getString("nombreLgac"));
                lgac.setDescripcionLgac(resultadoConsulta.getString("descripcionLgac"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar esta LGAC.", Codigos.ERROR_CONSULTA);
        }
        return lgac;
    }

    public ArrayList<Lgac> obtenerInformacionLGACPorCuerpoAcademico(int idCuerpoAcademico) throws DAOException {
        ArrayList<Lgac> lgacConsultados = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(CONSULTAR_LGAC_POR_CUERPO_ACADEMICO);
            sentencia.setInt(1, idCuerpoAcademico);
            ResultSet resultadoConsulta = sentencia.executeQuery();           
            while (resultadoConsulta.next()) {
                Lgac lgac = new Lgac();
                lgac.setIdLgac(resultadoConsulta.getInt("idLgac"));
                lgac.setNombreLgac(resultadoConsulta.getString("nombreLgac"));
                lgac.setDescripcionLgac(resultadoConsulta.getString("descripcionLgac"));
                lgacConsultados.add(lgac);
            }
                ConexionBD.cerrarConexionBD();  
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al consultar las LGAC's.", Codigos.ERROR_CONSULTA);
        }
        return lgacConsultados;
    }

    public int guardarLgac(Lgac lgacNuevo) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(REGISTRAR_LGAC,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, lgacNuevo.getNombreLgac());
            sentencia.setString(2, lgacNuevo.getDescripcionLgac());
            sentencia.executeUpdate();
            ResultSet resultadoOperacion = sentencia.getGeneratedKeys();
            if (resultadoOperacion.next()) {
                respuestaExito = resultadoOperacion.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al registrar este LGAC.", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }

    public int actualizarLgac(Lgac lgacEdicion) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_LGAC);
            sentencia.setString(1, lgacEdicion.getNombreLgac());
            sentencia.setString(2, lgacEdicion.getDescripcionLgac());
            sentencia.setInt(3, lgacEdicion.getIdLgac());
            sentencia.executeUpdate();
            respuestaExito = lgacEdicion.getIdLgac();
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al actualizar esta LGAC", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }

    public int eliminarLGAC(int idLgac) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(BORRAR_LGAC);
            sentencia.setInt(1, idLgac);
            respuestaExito = sentencia.executeUpdate();
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al borrarr esta LGAC.", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }

    public ArrayList<Lgac> obtenerInformacionLGACPorAnteproyecto(int idAnte) throws DAOException {
        ArrayList<Lgac> lgacConsultados = new ArrayList();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(CONSULTAR_LGAC_POR_ANTEPORYECTO);
            sentencia.setInt(1, idAnte);
            ResultSet resultadoConsulta = sentencia.executeQuery();
            if (resultadoConsulta.next()) {
                while (resultadoConsulta.next()) {
                    Lgac lgac = new Lgac();
                    lgac.setIdLgac(resultadoConsulta.getInt("idLgac"));
                    lgac.setNombreLgac(resultadoConsulta.getString("nombreLgac"));
                    lgac.setDescripcionLgac(resultadoConsulta.getString("descripcionLgac"));
                }
                ConexionBD.cerrarConexionBD();
            }
            return lgacConsultados;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al consultar las LGAC's.", Codigos.ERROR_CONSULTA);
        }
    }

}
