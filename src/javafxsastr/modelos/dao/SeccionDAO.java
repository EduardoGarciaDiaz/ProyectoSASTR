/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 12/05/2023
 * Descripción: DAO para realizar las consultas a la base de datos
 * correspondientes a las secciones. 
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Seccion;
import javafxsastr.utils.Codigos;

public class SeccionDAO {
    
    private final String OBTENER_SECCIONES = "SELECT * FROM sastr.secciones;";
    
    public ArrayList<Seccion> obtenerSecciones() throws DAOException {
        ArrayList<Seccion> secciones = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_SECCIONES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                secciones.add(new Seccion(resultado.getInt("idSeccion"), resultado.getString("nombreSeccion")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return secciones;
    }
}
