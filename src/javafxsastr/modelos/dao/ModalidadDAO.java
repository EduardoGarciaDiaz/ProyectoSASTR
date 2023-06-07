/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 01/06/2023
 * Descripción: 
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Modalidad;
import javafxsastr.utils.Codigos;

/**
 *
 * @author Daniel García Arcos
 */
public class ModalidadDAO {
    
    private final String OBTENER_MODALIDADES = "SELECT * FROM sastr.modalidades;";
    
    public ArrayList<Modalidad> obtenerModalidades() throws DAOException {
        ArrayList<Modalidad> modalidades = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_MODALIDADES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                modalidades.add(new Modalidad(resultado.getInt("idModalidad"), resultado.getString("nombreModalidad")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un error al obtener las modalidades.", Codigos.ERROR_CONSULTA);
        }
        return modalidades;
    }
    
}
