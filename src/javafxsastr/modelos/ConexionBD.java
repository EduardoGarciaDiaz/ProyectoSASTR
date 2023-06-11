/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 12/05/2023
 * Descripción: Clase que permite establecer conexión con la base de datos
 */

package javafxsastr.modelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.utils.Codigos;

public class ConexionBD {
    
    private static Connection conexion;
    
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String NOMBRE_BASE = "sastr";
    private static final String HOSTNAME = "localhost";
    private static final String PUERTO = "3306";                     //TODO: Crear usuario y contraseña
    private static final String USUARIO = "tristanBD";
    private static final String PASSWORD = "wachiturroloco1";
    
    private static final String URL_CONEXION = "jdbc:mysql://" + HOSTNAME + ":" + PUERTO + "/" + NOMBRE_BASE 
            + "?allowPublicKeyRetrieval=true&useSSL=false";
    
    private static Connection abrirConexionBD() throws SQLException {
        Connection newConnection = DriverManager.getConnection(URL_CONEXION, USUARIO, PASSWORD);
        return newConnection;
    }
      
    public static Connection obtenerConexionBD() throws DAOException {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = abrirConexionBD();
            }
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Intente de nuevo o hágalo más tarde", 
                    Codigos.ERROR_CONEXION_BD);
        }
        return conexion;
    }
    
    public static boolean cerrarConexionBD() throws DAOException {
        boolean conexionCerrada = false;
        try {
            if (conexion != null) {
                conexion.close();
            }
            conexionCerrada = true;
        } catch (SQLException ex) {
            throw new DAOException("Ha ocurrido un error.", Codigos.ERROR_CONEXION_BD);
        }
        return conexionCerrada;
    }
    
}
