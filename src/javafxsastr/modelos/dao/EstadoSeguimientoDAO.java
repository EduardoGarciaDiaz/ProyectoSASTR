//TODO@Daniel

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxsastr.modelos.ConexionBD;

public class EstadoSeguimientoDAO {
 
    private final String OBTENER_ID_ESTADO_SEGUIMIENTO = "SELECT * FROM sastr.estados_seguimiento where nombreEstadoSeguimiento = ?";
    
    public int obtenerIdEstadoSeguimiento(String nombreEstadoSeguimiento) throws DAOException {
        int idEstadoSeguimiento = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ID_ESTADO_SEGUIMIENTO);
            sentencia.setString(1, nombreEstadoSeguimiento);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                idEstadoSeguimiento = resultado.getInt("idEstadoSeguimiento");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            
        }
        return idEstadoSeguimiento;
    }
}
