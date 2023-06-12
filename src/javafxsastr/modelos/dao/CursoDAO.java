/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 19/05/2023
 * Descripción: DAO de los cursos
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.utils.Codigos;

public class CursoDAO {

    private final String OBTENER_CURSOS = "SELECT c.idCurso, c.nombreCurso, c.fechaInicioCurso, c.fechaFinCurso, "
            + "c.idExperienciaEducativa, e.nombreExperienciaEducativa, c.idNrc, n.nombreNrc, c.idPeriodoEscolar, "
            + "pe.fechaInicioPeriodoEscolar, pe.fechaFinPeriodoEscolar, c.idSeccion, s.nombreSeccion, c.idBloque, "
            + "b.nombreBloque, c.idAcademico, "
            + "CONCAT(u.nombreUsuario,' ', u.primerApellidoUsuario,' ', u.segundoApellidoUsuario) AS nombreCompletoAcademico, "
            + "c.idEstadoCurso, ec.nombreEstadoCurso " 
            + "FROM cursos c "
            + "INNER JOIN experiencias_educativas e ON c.idExperienciaEducativa = e.idExperienciaEducativa "
            + "INNER JOIN nrcs n ON c.idNrc = n.idNrc "
            + "INNER JOIN periodos_escolares pe ON c.idPeriodoEscolar = pe.idPeriodoEscolar "
            + "INNER JOIN secciones s ON c.idSeccion = s.idSeccion "
            + "INNER JOIN bloques b ON c.idBloque = b.idBloque "
            + "INNER JOIN academicos a ON c.idAcademico = a.idAcademico "
            + "INNER JOIN usuarios u ON u.idUsuario = a.idUsuario "
            + "INNER JOIN estados_curso ec ON c.idEstadoCurso = ec.idEstadoCurso ";
    private final String OBTENER_CURSOS_POR_EXPERIENCIA_EDUCATIVA = OBTENER_CURSOS
            + "WHERE nombreExperienciaEducativa = ?";
    private final String OBTENER_CURSOS_POR_NOMBRE = OBTENER_CURSOS + "WHERE nombreCurso = ?";
    private final String ORDENAR_CURSOS_POR_PERIODO_ESCOLAR = OBTENER_CURSOS + "ORDER BY fechaInicioPeriodoEscolar DESC;";
    private final String OBTENER_CURSO = OBTENER_CURSOS + " WHERE idCurso = ?";
    private final String GUARDAR_CURSO = "INSERT INTO cursos (nombreCurso, fechaInicioCurso, fechaFinCurso, idSeccion, idBloque, "
            + "idExperienciaEducativa, idNrc, idPeriodoEscolar, idEstadoCurso, idAcademico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String ACTUALIZAR_CURSO = "UPDATE cursos SET nombreCurso = ?, fechaInicioCurso = ?, fechaFinCurso = ?, idSeccion = ?, "
            + "idBloque = ?, idExperienciaEducativa = ?, idNrc = ?, idPeriodoEscolar = ?, idEstadoCurso = ?, idAcademico = ? "
            + "WHERE idCurso = ?";
    private final String ELIMINAR_CURSO = "DELETE FROM cursos WHERE idCurso = ?";
    private final String VERIFICAR_SI_ACADEMICO_IMPARTE_CURSO = "SELECT EXISTS"
            + "(SELECT idAcademico FROM cursos WHERE idAcademico = ?) as esProfesor;";
    private final String OBTENER_CURSOS_POR_ESTUDIANTE = OBTENER_CURSOS 
            + "INNER JOIN cursos_estudiantes cu on c.idCurso = cu.idCurso " 
            + "INNER JOIN estudiantes est ON cu.idEstudiante = est.idEstudiante " 
            + "WHERE est.idEstudiante = ? and ec.idEstadoCurso = ?";
    private final String GUARDAR_RELACIONESTUDIANTE_CURSO = "INSERT INTO sastr.cursos_estudiantes "
            + "( idCurso, idEstudiante) VALUES (?,?);";
    private final String OBTENER_CURSOS_ACTUALES_DEL_PROFESOR = OBTENER_CURSOS
            + "where a.idAcademico = ? and pe.esActual = 1;";

    public ArrayList<Curso> obtenerCursos() throws DAOException {
        ArrayList<Curso> cursos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CURSOS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(resultado.getInt("idCurso"));
                curso.setNombreCurso(resultado.getString("nombreCurso"));
                curso.setFechaInicioCurso(resultado.getString("fechaInicioCurso"));
                curso.setFechaFinCurso(resultado.getString("fechaFinCurso"));
                curso.setIdSeccion(resultado.getInt("idSeccion"));
                curso.setIdBloque(resultado.getInt("idBloque"));
                curso.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
                curso.setIdNRC(resultado.getInt("idNrc"));
                curso.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                curso.setIdEstadoCurso(resultado.getInt("idEstadoCurso"));
                curso.setIdAcademico(resultado.getInt("idAcademico"));
                curso.setSeccionCurso(resultado.getString("nombreSeccion"));
                curso.setBloqueCurso(resultado.getString("nombreBloque"));
                curso.setExperienciaEducativaCurso(resultado.getString("nombreExperienciaEducativa"));
                curso.setNrcCurso(resultado.getString("nombreNrc"));
                curso.setInicioPeriodoEscolar(resultado.getString("fechaInicioPeriodoEscolar"));
                curso.setFinPeriodoEscolar(resultado.getString("fechaFinPeriodoEscolar"));
                curso.setEstadoCurso(resultado.getString("nombreEstadoCurso"));
                curso.setAcademicoCurso(resultado.getString("nombreCompletoAcademico"));
                cursos.add(curso);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return cursos;
    }

    public ArrayList<Curso> obtenerCursosPorExperienciaEducativa(int idExperienciaEducativa) throws DAOException {
        ArrayList<Curso> cursos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_CURSOS_POR_EXPERIENCIA_EDUCATIVA);
            sentencia.setInt(1, idExperienciaEducativa);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(resultado.getInt("idCurso"));
                curso.setNombreCurso(resultado.getString("nombreCurso"));
                curso.setFechaInicioCurso(resultado.getString("fechaInicioCurso"));
                curso.setFechaFinCurso(resultado.getString("fechaFinCurso"));
                curso.setIdSeccion(resultado.getInt("idSeccion"));
                curso.setIdBloque(resultado.getInt("idBloque"));
                curso.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
                curso.setIdNRC(resultado.getInt("idNrc"));
                curso.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                curso.setIdEstadoCurso(resultado.getInt("idEstadoCurso"));
                curso.setIdAcademico(resultado.getInt("idAcademico"));
                curso.setSeccionCurso(resultado.getString("nombreSeccion"));
                curso.setBloqueCurso(resultado.getString("nombreBloque"));
                curso.setExperienciaEducativaCurso(resultado.getString("nombreExperienciaEducativa"));
                curso.setNrcCurso(resultado.getString("nombreNrc"));
                curso.setInicioPeriodoEscolar(resultado.getString("fechaInicioPeriodoEscolar"));
                curso.setFinPeriodoEscolar(resultado.getString("fechaFinPeriodoEscolar"));
                curso.setEstadoCurso(resultado.getString("nombreEstadoCurso"));
                curso.setAcademicoCurso(resultado.getString("nombreCompletoAcademico"));
                cursos.add(curso);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return cursos;
    }

    public Curso obtenerCursoPorNombre(String nombreCurso) throws DAOException {
        Curso curso = new Curso();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CURSOS_POR_NOMBRE);
            sentencia.setString(1, nombreCurso);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                curso.setIdCurso(resultado.getInt("idCurso"));
                curso.setNombreCurso(resultado.getString("nombreCurso"));
                curso.setFechaInicioCurso(resultado.getString("fechaInicioCurso"));
                curso.setFechaFinCurso(resultado.getString("fechaFinCurso"));
                curso.setIdSeccion(resultado.getInt("idSeccion"));
                curso.setIdBloque(resultado.getInt("idBloque"));
                curso.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
                curso.setIdNRC(resultado.getInt("idNrc"));
                curso.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                curso.setIdEstadoCurso(resultado.getInt("idEstadoCurso"));
                curso.setIdAcademico(resultado.getInt("idAcademico"));
                curso.setSeccionCurso(resultado.getString("nombreSeccion"));
                curso.setBloqueCurso(resultado.getString("nombreBloque"));
                curso.setExperienciaEducativaCurso(resultado.getString("nombreExperienciaEducativa"));
                curso.setNrcCurso(resultado.getString("nombreNrc"));
                curso.setInicioPeriodoEscolar(resultado.getString("fechaInicioPeriodoEscolar"));
                curso.setFinPeriodoEscolar(resultado.getString("fechaFinPeriodoEscolar"));
                curso.setEstadoCurso(resultado.getString("nombreEstadoCurso"));
                curso.setAcademicoCurso(resultado.getString("nombreCompletoAcademico"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return curso;
    }

    public ArrayList<Curso> ordenarCursosPorPeriodoEscolar() throws DAOException {
        ArrayList<Curso> cursos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(ORDENAR_CURSOS_POR_PERIODO_ESCOLAR);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(resultado.getInt("idCurso"));
                curso.setNombreCurso(resultado.getString("nombreCurso"));
                curso.setFechaInicioCurso(resultado.getString("fechaInicioCurso"));
                curso.setFechaFinCurso(resultado.getString("fechaFinCurso"));
                curso.setIdSeccion(resultado.getInt("idSeccion"));
                curso.setIdBloque(resultado.getInt("idBloque"));
                curso.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
                curso.setIdNRC(resultado.getInt("idNrc"));
                curso.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                curso.setIdEstadoCurso(resultado.getInt("idEstadoCurso"));
                curso.setIdAcademico(resultado.getInt("idAcademico"));
                curso.setSeccionCurso(resultado.getString("nombreSeccion"));
                curso.setBloqueCurso(resultado.getString("nombreBloque"));
                curso.setExperienciaEducativaCurso(resultado.getString("nombreExperienciaEducativa"));
                curso.setNrcCurso(resultado.getString("nombreNrc"));
                curso.setInicioPeriodoEscolar(resultado.getString("fechaInicioPeriodoEscolar"));
                curso.setFinPeriodoEscolar(resultado.getString("fechaFinPeriodoEscolar"));
                curso.setEstadoCurso(resultado.getString("nombreEstadoCurso"));
                curso.setAcademicoCurso(resultado.getString("nombreCompletoAcademico"));
                cursos.add(curso);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return cursos;
    }

    public Curso obtenerCurso(int idCurso) throws DAOException {
        Curso curso = new Curso();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CURSO);
            sentencia.setInt(1, idCurso);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                curso.setIdCurso(resultado.getInt("idCurso"));
                curso.setNombreCurso(resultado.getString("nombreCurso"));
                curso.setFechaInicioCurso(resultado.getString("fechaInicioCurso"));
                curso.setFechaFinCurso(resultado.getString("fechaFinCurso"));
                curso.setIdSeccion(resultado.getInt("idSeccion"));
                curso.setIdBloque(resultado.getInt("idBloque"));
                curso.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
                curso.setIdNRC(resultado.getInt("idNrc"));
                curso.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                curso.setIdEstadoCurso(resultado.getInt("idEstadoCurso"));
                curso.setIdAcademico(resultado.getInt("idAcademico"));
                curso.setSeccionCurso(resultado.getString("nombreSeccion"));
                curso.setBloqueCurso(resultado.getString("nombreBloque"));
                curso.setExperienciaEducativaCurso(resultado.getString("nombreExperienciaEducativa"));
                curso.setNrcCurso(resultado.getString("nombreNrc"));
                curso.setInicioPeriodoEscolar(resultado.getString("fechaInicioPeriodoEscolar"));
                curso.setFinPeriodoEscolar(resultado.getString("fechaFinPeriodoEscolar"));
                curso.setEstadoCurso(resultado.getString("nombreEstadoCurso"));
                curso.setAcademicoCurso(resultado.getString("nombreCompletoAcademico"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return curso;
    }

    public int guardarCurso(Curso curso) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_CURSO,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, curso.getNombreCurso());
            sentencia.setString(2, curso.getFechaInicioCurso());
            sentencia.setString(3, curso.getFechaFinCurso());
            sentencia.setInt(4, curso.getIdSeccion());
            sentencia.setInt(5, curso.getIdBloque());
            sentencia.setInt(6, curso.getIdExperienciaEducativa());
            sentencia.setInt(7, curso.getIdNRC());
            sentencia.setInt(8, curso.getIdPeriodoEscolar());
            sentencia.setInt(9, curso.getIdEstadoCurso());
            sentencia.setInt(10, curso.getIdAcademico());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int actualizarCurso(Curso curso) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_CURSO);
            sentencia.setString(1, curso.getNombreCurso());
            sentencia.setString(2, curso.getFechaInicioCurso());
            sentencia.setString(3, curso.getFechaFinCurso());
            sentencia.setInt(4, curso.getIdSeccion());
            sentencia.setInt(5, curso.getIdBloque());
            sentencia.setInt(6, curso.getIdExperienciaEducativa());
            sentencia.setInt(7, curso.getIdNRC());
            sentencia.setInt(8, curso.getIdPeriodoEscolar());
            sentencia.setInt(9, curso.getIdEstadoCurso());
            sentencia.setInt(10, curso.getIdAcademico());
            sentencia.setInt(11,curso.getIdCurso());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? curso.getIdCurso() : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int eliminarCurso(int idCurso) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_CURSO);
            sentencia.setInt(1, idCurso);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idCurso : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public boolean verificarSiAcademicoImparteCurso(int idAcademico) throws DAOException {
        boolean esProfesor = false;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(VERIFICAR_SI_ACADEMICO_IMPARTE_CURSO);
            sentencia.setInt(1, idAcademico);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                esProfesor = resultado.getBoolean("esProfesor");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return esProfesor;
    }

    public Curso obtenerCursosPorEstudiante(int idEstudiante) throws DAOException {
        Curso curso = null;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_CURSOS_POR_ESTUDIANTE);
            sentencia.setInt(1, idEstudiante);
            sentencia.setInt(2, 1);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                curso = new Curso();
                curso.setIdCurso(resultado.getInt("idCurso"));
                curso.setNombreCurso(resultado.getString("nombreCurso"));
                curso.setFechaInicioCurso(resultado.getString("fechaInicioCurso"));
                curso.setFechaFinCurso(resultado.getString("fechaFinCurso"));
                curso.setIdSeccion(resultado.getInt("idSeccion"));
                curso.setIdBloque(resultado.getInt("idBloque"));
                curso.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
                curso.setIdNRC(resultado.getInt("idNrc"));
                curso.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                curso.setIdEstadoCurso(resultado.getInt("idEstadoCurso"));
                curso.setIdAcademico(resultado.getInt("idAcademico"));
                curso.setSeccionCurso(resultado.getString("nombreSeccion"));
                curso.setBloqueCurso(resultado.getString("nombreBloque"));
                curso.setExperienciaEducativaCurso(resultado.getString("nombreExperienciaEducativa"));
                curso.setNrcCurso(resultado.getString("nombreNrc"));
                curso.setInicioPeriodoEscolar(resultado.getDate("fechaInicioPeriodoEscolar").toString());
                curso.setFinPeriodoEscolar(resultado.getDate("fechaFinPeriodoEscolar").toString());
                curso.setEstadoCurso(resultado.getString("nombreEstadoCurso"));
                curso.setAcademicoCurso(resultado.getString("nombreCompletoAcademico"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return curso;
    }

    public int guardarRelacionCursoEstudiante(int idCurso, int IdEstudiante) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(
                    GUARDAR_RELACIONESTUDIANTE_CURSO,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, idCurso);
            sentencia.setInt(2, IdEstudiante);
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Hubo un error al realizar la consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }
    
    public ArrayList<Curso> obtenerCursosDelProfesor(int idProfesor) throws DAOException {
        ArrayList<Curso> cursos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_CURSOS_ACTUALES_DEL_PROFESOR);
            sentencia.setInt(1, idProfesor);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(resultado.getInt("idCurso"));
                curso.setNombreCurso(resultado.getString("nombreCurso"));
                curso.setFechaInicioCurso(resultado.getString("fechaInicioCurso"));
                curso.setFechaFinCurso(resultado.getString("fechaFinCurso"));
                curso.setIdSeccion(resultado.getInt("idSeccion"));
                curso.setIdBloque(resultado.getInt("idBloque"));
                curso.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
                curso.setIdNRC(resultado.getInt("idNrc"));
                curso.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                curso.setIdEstadoCurso(resultado.getInt("idEstadoCurso"));
                curso.setIdAcademico(resultado.getInt("idAcademico"));
                curso.setSeccionCurso(resultado.getString("nombreSeccion"));
                curso.setBloqueCurso(resultado.getString("nombreBloque"));
                curso.setExperienciaEducativaCurso(resultado.getString("nombreExperienciaEducativa"));
                curso.setNrcCurso(resultado.getString("nombreNrc"));
                curso.setInicioPeriodoEscolar(resultado.getString("fechaInicioPeriodoEscolar"));
                curso.setFinPeriodoEscolar(resultado.getString("fechaFinPeriodoEscolar"));
                curso.setEstadoCurso(resultado.getString("nombreEstadoCurso"));
                curso.setAcademicoCurso(resultado.getString("nombreCompletoAcademico"));
                cursos.add(curso);
            }
            System.out.println(cursos);
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta al obtener los cursos actuales del profesor", Codigos.ERROR_CONSULTA);
        }
        return cursos;
    }

}