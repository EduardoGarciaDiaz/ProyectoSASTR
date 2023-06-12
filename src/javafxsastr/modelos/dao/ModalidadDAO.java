/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 20/05/2023
 * Descripción: DAO para realizar consultas de las modalidades de anteproyectos.
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Modalidad;
import javafxsastr.utils.Codigos;

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
            throw new DAOException("Error de consulta.", Codigos.ERROR_CONSULTA);
        }
        return modalidades;
    }
    
}
