/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 19/05/2023
 * Descripción: DAO de las actividades
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.utils.Codigos;

public class ActividadDAO {
    
    private final String OBTENER_ACTIVIDADES = "SELECT * FROM actividades a " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad";
    private final String OBTENER_ACTIVIDADES_POR_ESTUDIANTE = "SELECT * FROM actividades a " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad "
            + "WHERE a.idEstudiante = ?";
    private final String OBTENER_ACTIVIDADES_POR_ESTADO = "SELECT * FROM actividades a " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad " +
            "WHERE a.idEstadoActividad = ?";
    private final String OBTENER_ACTIVIDAD = "SELECT * FROM actividades a " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad " +
            "WHERE idActividad = ?";
    private final String GUARDAR_ACTIVIDAD = "INSERT INTO actividades (nombreActividad, detallesActividad, fechaInicioActividad, " +
            " fechaFinActividad, horaInicioActividad, horaFinActividad, fechaCreacionActividad, idAnteproyecto, idEstadoActividad,"
            + " idEstudiante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String ACTUALIZAR_ACTIVIDAD = "UPDATE actividades SET nombreActividad = ?, detallesActividad = ?," +
            " fechaInicioActividad = ?, fechaFinActividad = ?, horaInicioActividad = ?, horaFinActividad = ?, " +
            " fechaCreacionActividad = ?, idAnteproyecto = ?, idEstadoActividad = ?, idEstudiante = ? "
            + "WHERE idActividad = ?";
    private final String ELIMINAR_ACTIVIDAD = "DELETE FROM actividades WHERE idActividad = ?";
    private final String OBTENER_NUMERO_ACTIVIDADES_POR_ESTUDIANTE = "SELECT COUNT(a.idActividad) AS numeroActividades " +
            "FROM actividades a " +
            "INNER JOIN estudiantes e ON a.idEstudiante = e.idEstudiante " +
            "WHERE e.idEstudiante = ? ;";
    private final String OBTENER_NUMERO_ACTIVIDADES_COMPLETADAS = "SELECT COUNT(a.idActividad) AS numeroActividades " +
            "FROM actividades a " +
            "INNER JOIN estudiantes e ON a.idEstudiante = e.idEstudiante " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad " +
            "WHERE e.idEstudiante = ? AND a.idEstadoActividad = 1;";
    private final String OBTENER_NUMERO_ACTIVIDADES_NO_COMPLETADAS = "SELECT COUNT(a.idActividad) AS numeroActividades " +
            "FROM actividades a " +
            "INNER JOIN estudiantes e ON a.idEstudiante = e.idEstudiante " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad " +
            "WHERE e.idEstudiante = ? AND a.idEstadoActividad = 2;";
        private final String OBTENER_NUMERO_ACTIVIDADES_POR_ANTEPROYECTO = "SELECT COUNT(a.idActividad) AS numeroActividades " +
            "FROM actividades a " +
            "INNER JOIN anteproyectos ante ON a.idAnteproyecto= ante.idAnteproyecto " +
            "WHERE a.idAnteproyecto = ?;";
        private final String OBTENER_NUMERO_ACTIVIDADES_COMPLETADAS_ANTEPROYECTO = "SELECT COUNT(a.idActividad) AS numeroActividades " +
            "FROM actividades a " +
            "INNER JOIN anteproyectos ante ON a.idAnteproyecto= ante.idAnteproyecto " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad " +
            "WHERE ante.idAnteproyecto = ? AND a.idEstadoActividad = 1;	";
         private final String OBTENER_NUMERO_ACTIVIDADES_NO_COMPLETADAS_ANTEPROYECTO = "SELECT COUNT(a.idActividad) AS numeroActividades " +
            "FROM actividades a " +
            "INNER JOIN anteproyectos ante ON a.idAnteproyecto= ante.idAnteproyecto " +
            "INNER JOIN estados_actividad ea ON a.idEstadoActividad = ea.idEstadoActividad " +
            "WHERE ante.idAnteproyecto = ? AND a.idEstadoActividad = 2;	";
        
    public ArrayList<Actividad> obtenerActividades() throws DAOException{
        ArrayList<Actividad> actividades = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ACTIVIDADES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Actividad actividad = new Actividad();
                actividad.setIdActividad(resultado.getInt("idActividad"));
                actividad.setNombreActividad(resultado.getString("nombreActividad"));
                actividad.setDetallesActividad(resultado.getString("detallesActividad"));
                actividad.setFechaInicioActividad(resultado.getString("fechaInicioActividad"));
                actividad.setFechaFinActividad(resultado.getString("fechaFinActividad"));
                actividad.setHoraInicioActividad(resultado.getString("horaInicioActividad"));
                actividad.setHoraFinActividad(resultado.getString("horaFinActividad"));
                actividad.setFechaCreaciónActividad(resultado.getString("fechaCreacionActividad"));
                actividad.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                actividad.setIdEstadoActividad(resultado.getInt("idEstadoActividad"));
                actividad.setEstadoActividad(resultado.getString("nombreEstadoActividad"));
                actividades.add(actividad);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return actividades;
    }
    
    public ArrayList<Actividad> obtenerActividadesPorEstudiante(int idEstudiante) throws DAOException{
        ArrayList<Actividad> actividades = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ACTIVIDADES_POR_ESTUDIANTE);
            sentencia.setInt(1, idEstudiante);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Actividad actividad = new Actividad();
                actividad.setIdActividad(resultado.getInt("idActividad"));
                actividad.setNombreActividad(resultado.getString("nombreActividad"));
                actividad.setDetallesActividad(resultado.getString("detallesActividad"));
                actividad.setFechaInicioActividad(resultado.getString("fechaInicioActividad"));
                actividad.setFechaFinActividad(resultado.getString("fechaFinActividad"));
                actividad.setHoraInicioActividad(resultado.getString("horaInicioActividad"));
                actividad.setHoraFinActividad(resultado.getString("horaFinActividad"));
                actividad.setFechaCreaciónActividad(resultado.getString("fechaCreacionActividad"));
                actividad.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                actividad.setIdEstadoActividad(resultado.getInt("idEstadoActividad"));
                actividad.setEstadoActividad(resultado.getString("nombreEstadoActividad"));
                actividades.add(actividad);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return actividades;
    }
    
    public ArrayList<Actividad> obtenerActividadesPorEstado(int idEstadoActividad) throws DAOException{
        ArrayList<Actividad> actividades = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ACTIVIDADES_POR_ESTADO);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Actividad actividad = new Actividad();
                actividad.setIdActividad(resultado.getInt("idActividad"));
                actividad.setNombreActividad(resultado.getString("nombreActividad"));
                actividad.setDetallesActividad(resultado.getString("detallesActividad"));
                actividad.setFechaInicioActividad(resultado.getString("fechaInicioActividad"));
                actividad.setFechaFinActividad(resultado.getString("fechaFinActividad"));
                actividad.setHoraInicioActividad(resultado.getString("horaInicioActividad"));
                actividad.setHoraFinActividad(resultado.getString("horaFinActividad"));
                actividad.setFechaCreaciónActividad(resultado.getString("fechaCreacionActividad"));
                actividad.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                actividad.setIdEstadoActividad(resultado.getInt("idEstadoActividad"));
                actividad.setEstadoActividad(resultado.getString("nombreEstadoActividad"));
                actividades.add(actividad);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return actividades;
    }
    
    public int obtenerNumeroActividadesPorEstudiante(int idEstudiante) throws DAOException{
        int numeroActividades = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_NUMERO_ACTIVIDADES_POR_ESTUDIANTE);
            sentencia.setInt(1, idEstudiante);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                numeroActividades = resultado.getInt("numeroActividades");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return numeroActividades;
    }
        
    public int obtenerNumeroActividadesCompletadas(int idEstudiante) throws DAOException{
        int numeroActividades = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_NUMERO_ACTIVIDADES_COMPLETADAS);
            sentencia.setInt(1, idEstudiante);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                numeroActividades = resultado.getInt("numeroActividades");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return numeroActividades;
    }
    
    public int obtenerNumeroActividadesNoCompletadas(int idEstudiante) throws DAOException{
        int numeroActividades = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_NUMERO_ACTIVIDADES_NO_COMPLETADAS);
            sentencia.setInt(1, idEstudiante);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                numeroActividades = resultado.getInt("numeroActividades");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return numeroActividades;
    }
    
        public int obtenerNumeroActividadesPorAnteproyecto(int idAnteproyecto) throws DAOException{
        int numeroActividades = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_NUMERO_ACTIVIDADES_POR_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                numeroActividades = resultado.getInt("numeroActividades");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return numeroActividades;
    }
    
    public int obtenerNumeroActividadesCompletadasPorAnteproyecto(int idAnteproyecto) throws DAOException{
        int numeroActividades = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().
                    prepareStatement(OBTENER_NUMERO_ACTIVIDADES_COMPLETADAS_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                numeroActividades = resultado.getInt("numeroActividades");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return numeroActividades;
    }
    
    public int obtenerNumeroActividadesNoCompletadasPorAnteproyecto(int idAnteproyecto) throws DAOException{
        int numeroActividades = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().
                    prepareStatement(OBTENER_NUMERO_ACTIVIDADES_NO_COMPLETADAS_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                numeroActividades = resultado.getInt("numeroActividades");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return numeroActividades;
    }
    
    public Actividad obtenerActividad (int idActividad) throws DAOException {
        Actividad actividad = new Actividad();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ACTIVIDAD);
            sentencia.setInt(1, idActividad);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                actividad.setIdActividad(resultado.getInt("idActividad"));
                actividad.setNombreActividad(resultado.getString("nombreActividad"));
                actividad.setDetallesActividad(resultado.getString("detallesActividad"));
                actividad.setFechaInicioActividad(resultado.getString("fechaInicioActividad"));
                actividad.setFechaFinActividad(resultado.getString("fechaFinActividad"));
                actividad.setHoraInicioActividad(resultado.getString("horaInicioActividad"));
                actividad.setHoraFinActividad(resultado.getString("horaFinActividad"));
                actividad.setFechaCreaciónActividad(resultado.getString("fechaCreacionActividad"));
                actividad.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                actividad.setIdEstadoActividad(resultado.getInt("idEstadoActividad"));
                actividad.setEstadoActividad(resultado.getString("nombreEstadoActividad"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return actividad;
    }
    
    public int guardarActividad(Actividad actividad) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_ACTIVIDAD,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, actividad.getNombreActividad());
            sentencia.setString(2, actividad.getDetallesActividad());
            sentencia.setString(3, actividad.getFechaInicioActividad());
            sentencia.setString(4, actividad.getFechaFinActividad());
            sentencia.setString(5, actividad.getHoraInicioActividad());
            sentencia.setString(6, actividad.getHoraFinActividad());
            sentencia.setString(7, actividad.getFechaCreaciónActividad());
            sentencia.setInt(8, actividad.getIdAnteproyecto());
            sentencia.setInt(9, actividad.getIdEstadoActividad());
            sentencia.setInt(10, actividad.getIdEstudiante());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int actualizarActividad(Actividad actividad) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_ACTIVIDAD);
            sentencia.setString(1, actividad.getNombreActividad());
            sentencia.setString(2, actividad.getDetallesActividad());
            sentencia.setString(3, actividad.getFechaInicioActividad());
            sentencia.setString(4, actividad.getFechaFinActividad());
            sentencia.setString(5, actividad.getHoraInicioActividad());
            sentencia.setString(6, actividad.getHoraFinActividad());
            sentencia.setString(7, actividad.getFechaCreaciónActividad());
            sentencia.setInt(8, actividad.getIdAnteproyecto());
            sentencia.setInt(9, actividad.getIdEstadoActividad());
            sentencia.setInt(10, actividad.getIdEstudiante());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? actividad.getIdActividad() : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public int eliminarActividad(int idActividad) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_ACTIVIDAD);
            sentencia.setInt(1, idActividad);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idActividad : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("No se pudo conectar con la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                    Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
}
