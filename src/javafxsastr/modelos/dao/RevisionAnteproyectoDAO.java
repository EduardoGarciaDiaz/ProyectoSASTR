package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.RevisionAnteproyecto;

/**
 *
 * @author Daniel García Arcos
 */
public class RevisionAnteproyectoDAO {
    private final String OBTENER_REVISION_ANTEPROYECTO = "SELECT * FROM sastr.revisiones_anteproyectos where idAnteproyecto = ?;";
    private final String GUARDAR_REVISION_ANTEPROYECTO = "INSERT INTO `sastr`.`revisiones_anteproyectos` (`comentarios`, `idRubrica`, `idAnteproyecto`) VALUES (?, ?, ?);";
    private final String MODIFICAR_REVISION_ANTEPROYECTO = "UPDATE `sastr`.`revisiones_anteproyectos` SET `comentarios` = ?, `idRubrica` = ?, `idAnteproyecto` = ?  WHERE (`idRevisionAnteproyecto` = ?);";

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
}
