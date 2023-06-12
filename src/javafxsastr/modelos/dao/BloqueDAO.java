/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 03/06/2023
 * Descripción: DAO para realizar las consultas a la base de datos
 * correspondientes a un bloque.
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Bloque;
import javafxsastr.utils.Codigos;

public class BloqueDAO {
    
    private final String OBTENER_BLOQUES = "SELECT * FROM sastr.bloques;";
    
    public ArrayList<Bloque> obtenerBloques() throws DAOException {
        ArrayList<Bloque> bloques = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_BLOQUES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                bloques.add(new Bloque(resultado.getInt("idBloque"), resultado.getString("nombreBloque")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return bloques;
    }
}
