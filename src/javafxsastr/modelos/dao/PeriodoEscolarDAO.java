//TODO@Daniel

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.PeriodoEscolar;
import javafxsastr.utils.Codigos;

public class PeriodoEscolarDAO {
    
    private final String OBTENER_PERIODOS_ESCOLARES = "SELECT * FROM sastr.periodos_escolares;";
    
    public ArrayList<PeriodoEscolar> obtenerPeriodosEscolares() throws DAOException {
        ArrayList<PeriodoEscolar> periodos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_PERIODOS_ESCOLARES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                periodos.add(new PeriodoEscolar(
                        resultado.getInt("idPeriodoEscolar"),
                        resultado.getString("fechaInicioPeriodoEscolar"),
                        resultado.getString("fechaFinPeriodoEscolar")));
                
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un error al obtener los periodos.", Codigos.ERROR_CONSULTA);
        }
        return periodos;
    }
}
