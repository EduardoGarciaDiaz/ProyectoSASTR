/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 12/05/2023
 * Descripción: DAO de los estudiantes
 */

package javafxsastr.modelos.dao;

//TODO

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Codigos;

public class EstudianteDAO {
    private final String OBTENER_ESTUDIANTES = "SELECT e.idEstudiante, u.nombreUsuario AS nombreEstudiante, " 
            + "u.primerApellidoUsuario AS primerApellidoEstudiante, u.segundoApellidoUsuario AS segundoApellidoEstudiante, "
            + "e.matriculaEstudiante, u.correoInstitucionalUsuario AS correoInstitucionalEstudiante, " 
            + "u.correoAlternoUsuario as correoAlternoEstudiante, u.contraseñaUsuario, u.idUsuario, e.idAnteproyecto, " 
            + "a.nombreTrabajoRecepcional, c.idCurso, c.nombreCurso, u.idEstadoUsuario, eu.nombreEstadoUsuario, " 
            + "u.esAdministrador " 
            + "FROM usuarios u " 
            + "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " 
            + "LEFT JOIN anteproyectos a ON e.idAnteproyecto = a.idAnteproyecto " 
            + "LEFT JOIN cursos_estudiantes ce ON e.idEstudiante = ce.idEstudiante " 
            + "LEFT JOIN cursos c ON ce.idCurso = c.idCurso " 
            + "INNER JOIN estados_usuario eu ON u.idEstadoUsuario = eu.idEstadoUsuario ";
    private final String OBTENER_ESTUDIANTE = "SELECT e.idEstudiante, u.nombreUsuario AS nombreEstudiante, " 
            + "u.primerApellidoUsuario AS primerApellidoEstudiante, u.segundoApellidoUsuario AS segundoApellidoEstudiante, "
            + "e.matriculaEstudiante, u.correoInstitucionalUsuario AS correoInstitucionalEstudiante, " 
            + "u.correoAlternoUsuario as correoAlternoEstudiante, u.contraseñaUsuario, u.idUsuario, e.idAnteproyecto, " 
            + "a.nombreTrabajoRecepcional, c.idCurso, c.nombreCurso, u.idEstadoUsuario, eu.nombreEstadoUsuario, " 
            + "u.esAdministrador " 
            + "FROM usuarios u " 
            + "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " 
            + "LEFT JOIN anteproyectos a ON e.idAnteproyecto = a.idAnteproyecto "
            + "LEFT JOIN cursos_estudiantes ce ON e.idEstudiante = ce.idEstudiante " 
            + "LEFT JOIN cursos c ON ce.idCurso = c.idCurso " 
            + "INNER JOIN estados_usuario eu ON u.idEstadoUsuario = eu.idEstadoUsuario " 
            + "WHERE e.idEstudiante = ?";
    private final String OBTENER_ESTUDIANTE_POR_MATRICULA = "SELECT e.idEstudiante, u.nombreUsuario AS nombreEstudiante, "
            + "u.primerApellidoUsuario AS primerApellidoEstudiante, u.segundoApellidoUsuario AS segundoApellidoEstudiante, "
            + "e.matriculaEstudiante, u.correoInstitucionalUsuario AS correoInstitucionalEstudiante, " 
            + "u.correoAlternoUsuario as correoAlternoEstudiante, u.contraseñaUsuario, u.idUsuario, e.idAnteproyecto, " 
            + "a.nombreTrabajoRecepcional, c.idCurso, c.nombreCurso, u.idEstadoUsuario, eu.nombreEstadoUsuario, " 
            + "u.esAdministrador " 
            + "FROM usuarios u " 
            + "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " 
            + "LEFT JOIN anteproyectos a ON e.idAnteproyecto = a.idAnteproyecto "
            + "LEFT JOIN cursos_estudiantes ce ON e.idEstudiante = ce.idEstudiante " 
            + "LEFT JOIN cursos c ON ce.idCurso = c.idCurso " 
            + "INNER JOIN estados_usuario eu ON u.idEstadoUsuario = eu.idEstadoUsuario " 
            + "WHERE e.matriculaEstudiante = ?";
    private final String OBTENER_ESTUDIANTE_POR_CORREO = "SELECT e.idEstudiante, u.nombreUsuario AS nombreEstudiante, "
            + "u.primerApellidoUsuario AS primerApellidoEstudiante, u.segundoApellidoUsuario AS segundoApellidoEstudiante, "
            + "e.matriculaEstudiante, u.correoInstitucionalUsuario AS correoInstitucionalEstudiante, " 
            + "u.correoAlternoUsuario as correoAlternoEstudiante, u.contraseñaUsuario, u.idUsuario, e.idAnteproyecto, " 
            + "a.nombreTrabajoRecepcional, c.idCurso, c.nombreCurso, u.idEstadoUsuario, eu.nombreEstadoUsuario, " 
            + "u.esAdministrador " 
            + "FROM usuarios u " 
            + "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " 
            + "LEFT JOIN anteproyectos a ON e.idAnteproyecto = a.idAnteproyecto " 
            + "LEFT JOIN cursos_estudiantes ce ON e.idEstudiante = ce.idEstudiante " 
            + "LEFT JOIN cursos c ON ce.idCurso = c.idCurso " 
            + "INNER JOIN estados_usuario eu ON u.idEstadoUsuario = eu.idEstadoUsuario " 
            + "WHERE u.correoInstitucionalUsuario = ? OR u.correoAlternoUsuario = ?";
    private final String OBTENER_ESTUDIANTE_POR_ID_USUARIO = "SELECT e.idEstudiante, u.nombreUsuario AS nombreEstudiante, "
            + "u.primerApellidoUsuario AS primerApellidoEstudiante, u.segundoApellidoUsuario AS segundoApellidoEstudiante, "
            + "e.matriculaEstudiante, u.correoInstitucionalUsuario AS correoInstitucionalEstudiante, "
            + "u.correoAlternoUsuario as correoAlternoEstudiante, u.contraseñaUsuario, u.idUsuario, e.idAnteproyecto, " 
            + "a.nombreTrabajoRecepcional, c.idCurso, c.nombreCurso, u.idEstadoUsuario, eu.nombreEstadoUsuario, " 
            + "u.esAdministrador " 
            + "FROM usuarios u " 
            + "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " 
            + "LEFT JOIN anteproyectos a ON e.idAnteproyecto = a.idAnteproyecto " 
            + "LEFT JOIN cursos_estudiantes ce ON e.idEstudiante = ce.idEstudiante " 
            + "LEFT JOIN cursos c ON ce.idCurso = c.idCurso " 
            + "INNER JOIN estados_usuario eu ON u.idEstadoUsuario = eu.idEstadoUsuario "
            + "WHERE u.idUsuario = ?";
    private final String GUARDAR_ESTUDIANTE = "INSERT INTO estudiantes (matriculaEstudiante, idUsuario) VALUES (?, ?);";
    private final String ACTUALIZAR_ESTUDIANTE = "UPDATE estudiantes SET matriculaEstudiante = ?, idUsuario = ?, idAnteproyecto = ? "
            + "WHERE idEstudiante = ?";
    private final String ELIMINAR_ESTUDIANTE = "DELETE FROM estudiantes WHERE idEstudiante = ?;";
    private final String DESASIGNAR_ESTUDIANTE = "Update estudiantes Set idAnteproyecto = ? where idEstudiante = ?";
    private final String OBTENER_ESTUDIANTES_POR_CURSO = "SELECT e.idEstudiante, u.nombreUsuario AS nombreEstudiante, "
            + "u.primerApellidoUsuario AS primerApellidoEstudiante, u.segundoApellidoUsuario AS segundoApellidoEstudiante, "
            + "e.matriculaEstudiante, u.correoInstitucionalUsuario AS correoInstitucionalEstudiante, " 
            + "u.correoAlternoUsuario as correoAlternoEstudiante, u.contraseñaUsuario, u.idUsuario, e.idAnteproyecto, " 
            + "a.nombreTrabajoRecepcional, c.idCurso, c.nombreCurso, u.idEstadoUsuario, eu.nombreEstadoUsuario, " 
            + "u.esAdministrador " 
            + "FROM usuarios u " 
            + "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " 
            + "LEFT JOIN anteproyectos a ON e.idAnteproyecto = a.idAnteproyecto " 
            + "LEFT JOIN cursos_estudiantes ce ON e.idEstudiante = ce.idEstudiante " 
            + "LEFT JOIN cursos c ON ce.idCurso = c.idCurso " 
            + "INNER JOIN estados_usuario eu ON u.idEstadoUsuario = eu.idEstadoUsuario " 
            + "WHERE c.idCurso = ?";
    private final String OBTENER_ESTUDIANTES_POR_ID_ANTEPROYECTO = "SELECT e.idEstudiante, u.nombreUsuario AS nombreEstudiante, "
            + "u.primerApellidoUsuario AS primerApellidoEstudiante, u.segundoApellidoUsuario AS segundoApellidoEstudiante, "
            + "e.matriculaEstudiante, u.correoInstitucionalUsuario AS correoInstitucionalEstudiante, " 
            + "u.correoAlternoUsuario as correoAlternoEstudiante, u.contraseñaUsuario, u.idUsuario, e.idAnteproyecto, " 
            + "a.nombreTrabajoRecepcional, c.idCurso, c.nombreCurso, u.idEstadoUsuario, eu.nombreEstadoUsuario, " 
            + "u.esAdministrador " 
            + "FROM usuarios u " 
            + "INNER JOIN estudiantes e ON u.idUsuario = e.idUsuario " 
            + "LEFT JOIN anteproyectos a ON e.idAnteproyecto = a.idAnteproyecto " 
            + "LEFT JOIN cursos_estudiantes ce ON e.idEstudiante = ce.idEstudiante " 
            + "LEFT JOIN cursos c ON ce.idCurso = c.idCurso " 
            + "INNER JOIN estados_usuario eu ON u.idEstadoUsuario = eu.idEstadoUsuario " 
            + "WHERE e.idAnteproyecto = ?";
    private final String VERIFICAR_SI_ANTEPROYECTO_ESTA_ASIGNADO = "SELECT EXISTS"
            + "(SELECT idAnteproyecto FROM estudiantes WHERE idAnteproyecto = ?) as estaAsignado;";
    private final String OBTENER_ESTUDIANTES_SIN_ANTEPROYECTO = OBTENER_ESTUDIANTES + " Where e.idAnteproyecto is null";
    private final String OBTENER_ESTUDIANTES_ASIGNADOS_POR_ACADEMICO = OBTENER_ESTUDIANTES
            + "INNER JOIN academicos acad "
            + "ON a.idAcademico = acad.idAcademico "
            + "WHERE acad.idAcademico = ? and a.idEstadoSeguimiento = 5";

    public ArrayList<Estudiante> obtenerEstudiantes() throws DAOException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ESTUDIANTES);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
                estudiantes.add(estudiante);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta",
                    Codigos.ERROR_CONSULTA);
        }
        return estudiantes;
    }

    public Estudiante obtenerEstudiante(int idEstudiante) throws DAOException {
        Estudiante estudiante = new Estudiante();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ESTUDIANTE);
            sentencia.setInt(1, idEstudiante);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiante;
    }

    public Estudiante obtenerEstudiantePorMatricula(String matricula) throws DAOException {
        Estudiante estudiante = new Estudiante();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ESTUDIANTE_POR_MATRICULA);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiante;
    }

    public Estudiante obtenerEstudiantePorCorreo(String correo) throws DAOException {
        Estudiante estudiante = new Estudiante();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ESTUDIANTE_POR_CORREO);
            sentencia.setString(1, correo);
            sentencia.setString(2, correo);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiante;
    }

    public Estudiante obtenerEstudiantePorIdUsuario(int idUsuario) throws DAOException {
        Estudiante estudiante = new Estudiante();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ESTUDIANTE_POR_ID_USUARIO);
            sentencia.setInt(1, idUsuario);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiante;
    }

    public ArrayList<Estudiante> obtenerEstudiantesPorIdCurso(int idCurso) throws DAOException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ESTUDIANTES_POR_CURSO);
            sentencia.setInt(1, idCurso);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
                estudiantes.add(estudiante);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiantes;
    }

    public int guardarEstudiante(Estudiante estudiante) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_ESTUDIANTE,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, estudiante.getMatriculaEstudiante());
            sentencia.setInt(2, estudiante.getIdUsuario());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            while (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int actualizarEstudiante(Estudiante estudiante) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_ESTUDIANTE);
            sentencia.setString(1, estudiante.getMatriculaEstudiante());
            sentencia.setInt(2, estudiante.getIdUsuario());
            if (estudiante.getIdAnteproyecto() > 0) {
                sentencia.setInt(3, estudiante.getIdAnteproyecto());
            } else {
                sentencia.setNull(3, Types.INTEGER);
            }
            sentencia.setInt(4, estudiante.getIdEstudiante());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? estudiante.getIdEstudiante() : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int desasignarEstudiante(Estudiante estudiante) throws DAOException {
        int respuestaExito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(DESASIGNAR_ESTUDIANTE);
            sentencia.setNull(1, Types.INTEGER);
            sentencia.setInt(2, estudiante.getIdEstudiante());
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas != 0) {
                respuestaExito = filasAfectadas;
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuestaExito;
    }

    public ArrayList<Estudiante> obtenerEstudiantesPorIdAnteproyecto(int idAnteproyecto) throws DAOException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ESTUDIANTES_POR_ID_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
                estudiantes.add(estudiante);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiantes;
    }

    public int eliminarEstudiante(int idEstudiante) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_ESTUDIANTE);
            sentencia.setInt(1, idEstudiante);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idEstudiante : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public boolean verificarSiAnteproyectoEstaAsignado(int idAnteproyecto) throws DAOException {
        boolean estaAsignado = false;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(VERIFICAR_SI_ANTEPROYECTO_ESTA_ASIGNADO);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                estaAsignado = resultado.getBoolean("estaAsignado");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estaAsignado;
    }

    public ArrayList<Estudiante> obtenerEstudiantesSinAnteproyecto() throws DAOException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ESTUDIANTES_SIN_ANTEPROYECTO);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
                estudiantes.add(estudiante);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiantes;
    }
    
     public ArrayList<Estudiante> obtenerEstudiantesPorAcademico(int idAcademico) throws DAOException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ESTUDIANTES_ASIGNADOS_POR_ACADEMICO);
            sentencia.setInt(1, idAcademico);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setNombre(resultado.getString("nombreEstudiante"));
                estudiante.setPrimerApellido(resultado.getString("primerApellidoEstudiante"));
                estudiante.setSegundoApellido(resultado.getString("segundoApellidoEstudiante"));
                estudiante.setMatriculaEstudiante(resultado.getString("matriculaEstudiante"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucionalEstudiante"));
                estudiante.setCorreoAlterno(resultado.getString("correoAlternoEstudiante"));
                estudiante.setContraseña(resultado.getString("contraseñaUsuario"));
                estudiante.setIdUsuario(resultado.getInt("idUsuario"));
                estudiante.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                estudiante.setAnteproyectoEstudiante(resultado.getString("nombreTrabajoRecepcional"));
                estudiante.setIdCurso(resultado.getInt("idCurso"));
                estudiante.setCursoEstudiante(resultado.getString("nombreCurso"));
                estudiante.setIdEstadoUsuario(resultado.getInt("idEstadoUsuario"));
                estudiante.setEstadoUsuario(resultado.getString("nombreEstadoUsuario"));
                estudiantes.add(estudiante);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return estudiantes;
    }

}