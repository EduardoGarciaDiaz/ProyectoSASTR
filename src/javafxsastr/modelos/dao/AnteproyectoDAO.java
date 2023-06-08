/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 13/05/2023
 * Descripción: DAO para realizar las consultas a la base de datos
 * correspondientes a un anteproyecto.
 */

package javafxsastr.modelos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxsastr.modelos.ConexionBD;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.utils.Codigos;

public class AnteproyectoDAO {

    private final String OBTENER_ANTEPROYECTOS = "SELECT  " +
            "A.idAnteproyecto, A.fechaCreacionAnteproyecto, A.nombreProyectoInvestigacion, A.ciudadCreacionAnteproyecto, " +
            "A.lineaInvestigacion, A.duracionAproximadaAnteproyecto, A.nombreTrabajoRecepcional, A.requisitosAnteproyecto, " +
            "A.alumnosParticipantesAnteproyecto, A.descripcionProyectoInvestigacion, A.descripcionTrabajoRecepcional, " +
            "A.resultadosEsperadosAnteproyecto, A.bibliografiasRecomendadasAnteproyecto, A.notasExtraAnteproyecto, A.idAcademico, " +
            "CONCAT(US.nombreUsuario, ' ', US.primerApellidoUsuario,' ', US.segundoApellidoUsuario) AS 'nombreDirector', " +
            "A.idEstadoSeguimiento, A.idCuerpoAcademico, A.idModalidad, nombreEstadoSeguimiento, nombreModalidad, nombreCuerpoAcademico " +
            "from sastr.anteproyectos A " +
            "inner join sastr.estados_seguimiento ES " +
            "on A.idEstadoSeguimiento = ES.idEstadoSeguimiento " +
            "inner join sastr.academicos ACS " +
            "on A.idAcademico = ACS.idAcademico " +
            "inner join usuarios US " +
            "on ACS.idUsuario = US.idUsuario " +
            "inner join sastr.modalidades MS " +
            "on MS.idModalidad = A.idModalidad " +
            "inner join sastr.cuerpos_academicos CA " +
            "on A.idCuerpoAcademico = CA.idCuerpoAcademico ";
    private final String OBTENER_ANTEPROYECTOS_POR_CUERPO_ACADEMICO = "SELECT  " +
            "idAnteproyecto, fechaCreacionAnteproyecto, nombreProyectoInvestigacion, ciudadCreacionAnteproyecto, " +
            "lineaInvestigacion, duracionAproximadaAnteproyecto, nombreTrabajoRecepcional, requisitosAnteproyecto, " +
            "alumnosParticipantesAnteproyecto, descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, " +
            "resultadosEsperadosAnteproyecto, bibliografiasRecomendadasAnteproyecto, notasExtraAnteproyecto, A.idAcademico, "
            +
            "CONCAT(US.nombreUsuario, ' ', US.primerApellidoUsuario,' ', US.segundoApellidoUsuario) AS 'nombreDirector', "
            +
            "A.idEstadoSeguimiento, A.idCuerpoAcademico, A.idModalidad, nombreEstadoSeguimiento, nombreModalidad, nombreCuerpoAcademico "
            +
            "from sastr.anteproyectos A " +
            "inner join sastr.estados_seguimiento ES " +
            "on A.idEstadoSeguimiento = ES.idEstadoSeguimiento " +
            "inner join sastr.academicos ACS " +
            "on A.idAcademico = ACS.idAcademico " +
            "inner join usuarios US " +
            "on ACS.idUsuario = US.idUsuario " +
            "left join sastr.modalidades MS " +
            "on MS.idModalidad = A.idModalidad " +
            "left join sastr.cuerpos_academicos CA " +
            "on A.idCuerpoAcademico = CA.idCuerpoAcademico "
            + "where A.idCuerpoAcademico = ? AND A.idEstadoSeguimiento != 1";
    private final String OBTENER_ANTEPROYECTOS_POR_ACADEMICO = "SELECT  " +
            "idAnteproyecto, fechaCreacionAnteproyecto, nombreProyectoInvestigacion, ciudadCreacionAnteproyecto, " +
            "lineaInvestigacion, duracionAproximadaAnteproyecto, nombreTrabajoRecepcional, requisitosAnteproyecto, " +
            "alumnosParticipantesAnteproyecto, descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, " +
            "resultadosEsperadosAnteproyecto, bibliografiasRecomendadasAnteproyecto, notasExtraAnteproyecto, A.idAcademico, "
            +
            "CONCAT(US.nombreUsuario, ' ', US.primerApellidoUsuario,' ', US.segundoApellidoUsuario) AS 'nombreDirector', "
            +
            "A.idEstadoSeguimiento, A.idCuerpoAcademico, A.idModalidad, nombreEstadoSeguimiento, nombreModalidad, nombreCuerpoAcademico "
            +
            "from sastr.anteproyectos A " +
            "inner join sastr.estados_seguimiento ES " +
            "on A.idEstadoSeguimiento = ES.idEstadoSeguimiento " +
            "inner join sastr.academicos ACS " +
            "on A.idAcademico = ACS.idAcademico " +
            "inner join usuarios US " +
            "on ACS.idUsuario = US.idUsuario " +
            "left join sastr.modalidades MS " +
            "on MS.idModalidad = A.idModalidad " +
            "left join sastr.cuerpos_academicos CA " +
            "on A.idCuerpoAcademico = CA.idCuerpoAcademico "
            + "where A.idAcademico = ? AND A.idEstadoSeguimiento != 2";
    private final String OBTENER_ANTEPROYECTOS_POR_ESTADO_SEGUIMIENTO = OBTENER_ANTEPROYECTOS
            + " WHERE A.idEstadoSeguimiento = ?;";
    private final String OBTENER_ANTEPROYECTOS_POR_LGAC = "SELECT "
            + "A.idAnteproyecto, fechaCreacionAnteproyecto, nombreProyectoInvestigacion, ciudadCreacionAnteproyecto, "
            + "lineaInvestigacion, duracionAproximadaAnteproyecto, nombreTrabajoRecepcional, requisitosAnteproyecto, "
            + "alumnosParticipantesAnteproyecto, descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, "
            + "resultadosEsperadosAnteproyecto, bibliografiasRecomendadasAnteproyecto, notasExtraAnteproyecto, A.idAcademico, "
            + "CONCAT(US.nombreUsuario, ' ', US.primerApellidoUsuario,' ', US.segundoApellidoUsuario) AS 'nombreDirector', "
            +
            "A.idEstadoSeguimiento, idCuerpoAcademico, A.idModalidad, nombreEstadoSeguimiento, nombreModalidad " +
            "from sastr.anteproyectos A " +
            "inner join sastr.estados_seguimiento ES " +
            "on A.idEstadoSeguimiento = ES.idEstadoSeguimiento " +
            "inner join sastr.academicos ACS " +
            "on A.idAcademico = ACS.idAcademico " +
            "inner join usuarios US " +
            "on ACS.idUsuario = US.idUsuario " +
            "inner join sastr.modalidades MS " +
            "on MS.idModalidad = A.idModalidad " +
            "inner join sastr.lgacs_anteproyectos LA " +
            "ON LA.idAnteproyecto = A.idAnteproyecto " +
            "inner join sastr.lgacs LG " +
            "ON LA.idLgac = LG.idLgac;";
    private final String OBTENER_ANTEPROYECTO_POR_ID = OBTENER_ANTEPROYECTOS + " WHERE A.idAnteproyecto = ?";
    private final String ELIMINAR_ANTEPROYECTO = "DELETE FROM sastr.anteproyectos WHERE idAnteproyecto = ?";
    private final String ACTUALIZAR_ANTEPROYECTO = "UPDATE anteproyectos SET"
            + "fechaCreacionAnteproyecto = ?, nombreProyectoInvestigacion = ?, ciudadCreacionAnteproyecto = ?, "
            + "lineaInvestigacion = ?, duracionAproximadaAnteproyecto = ?, nombreTrabajoRecepcional = ?, "
            + "requisitosAnteproyecto = ?, alumnosParticipantesAnteproyecto = ?, descripcionProyectoInvestigacion = ?,"
            + "descripcionTrabajoRecepcional = ?, resultadosEsperadosAnteproyecto = ?, bibliografiasRecomendadasAnteproyecto = ?,"
            + "notasExtraAnteproyecto = ?, idAcademico = ?, idEstadoSeguimiento = ?, idCuerpoAcademico = ?, idModalidad = ?"
            + "WHERE idAnteproyecto = ?";
    private final String GUARDAR_ANTEPROYECTO = "insert into anteproyectos (fechaCreacionAnteproyecto, nombreProyectoInvestigacion, "
            + "ciudadCreacionAnteproyecto, lineaInvestigacion, duracionAproximadaAnteproyecto, nombreTrabajoRecepcional, "
            + "requisitosAnteproyecto, alumnosParticipantesAnteproyecto, descripcionProyectoInvestigacion, "
            + "descripcionTrabajoRecepcional, resultadosEsperadosAnteproyecto, bibliografiasRecomendadasAnteproyecto, "
            + "notasExtraAnteproyecto, idAcademico, idEstadoSeguimiento, idCuerpoAcademico, idModalidad) values "
            + "(?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String VERIFICAR_SI_ACADEMICO_ES_DIRECTOR = "SELECT EXISTS"
            + "(SELECT idAcademico FROM anteproyectos WHERE idAcademico = ?) as esDirector;";
    private final String GUARDAR_CODIRECTOR_ANTEPROYECTO = "INSERT INTO `sastr`.`codirectores_anteproyectos` "
            + "(`idAnteproyecto`, `idAcademico`) VALUES (?, ?);";
    private final String GUARDAR_LGAC_ANTEPROYECTO = "INSERT INTO `sastr`.`lgacs_anteproyectos` "
            + "(`idLgac`, `idAnteproyecto`) VALUES (?, ?);";
    private final String ELIMINAR_LGACS_DE_ANTEPROYECTO = "DELETE FROM `sastr`.`lgacs_anteproyectos` WHERE (`idAnteproyecto` = '?');";
    private final String ELIMINAR_CODIRECTORES_DE_ANTEPROYECTO = "DELETE FROM `sastr`.`codirectores_anteproyectos` WHERE (`idAnteproyecto` = '?');";
    private final String OBTENER_ANTEPROYECTOS_POR_ESTUDIANTE = OBTENER_ANTEPROYECTOS
            + " inner join sastr.estudiantes ASE\n" +
            "on A.idAnteproyecto = ASE.idAnteproyecto\n" +
            "WHERE ASE.idEstudiante = ?;";
    private final String ACTUALIZAR_ESTADO_SEGUMIMIENTO = "UPDATE anteproyectos set idEstadoSeguimiento = ? "
            + "where idAnteproyecto = ?";

    public Anteproyecto obtenerAnteproyectoPorId(int idAnteproyecto) throws DAOException {
        Anteproyecto anteproyecto = new Anteproyecto();
        anteproyecto.setIdAnteproyecto(-1);
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ANTEPROYECTO_POR_ID);
            sentencia.setInt(1, idAnteproyecto);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                anteproyecto.setIdAnteproyecto(resultado.getInt("idAnteproyecto"));
                anteproyecto.setIdCuerpoAcademico(resultado.getInt("idCuerpoAcademico"));
                anteproyecto.setIdModalidad(resultado.getInt("idModalidad"));
                anteproyecto.setFechaCreacion(resultado.getString("fechaCreacionAnteproyecto"));
                anteproyecto.setCiudadCreacion(resultado.getString("ciudadCreacionAnteproyecto"));
                anteproyecto.setNombreProyectoInvestigacion(resultado.getString("nombreProyectoInvestigacion"));
                anteproyecto.setLineaInvestigacion(resultado.getString("lineaInvestigacion"));
                anteproyecto.setNombreTrabajoRecepcional(resultado.getString("nombreTrabajoRecepcional"));
                anteproyecto.setRequisitos(resultado.getString("requisitosAnteproyecto"));
                anteproyecto
                        .setDescripcionProyectoInvestigacion(resultado.getString("descripcionProyectoInvestigacion"));
                anteproyecto.setDescripcionTrabajoRecepcional(resultado.getString("descripcionTrabajoRecepcional"));
                anteproyecto.setResultadosEsperadosAnteproyecto(resultado.getString("resultadosEsperadosAnteproyecto"));
                anteproyecto.setBibliografiaRecomendada(resultado.getString("bibliografiasRecomendadasAnteproyecto"));
                anteproyecto.setDuracionAproximada(resultado.getString("duracionAproximadaAnteproyecto"));
                anteproyecto.setNumeroMaximoAlumnosParticipantes(resultado.getInt("alumnosParticipantesAnteproyecto"));
                anteproyecto.setNotasExtras(resultado.getString("notasExtraAnteproyecto"));
                anteproyecto.setNombreDirector(resultado.getString("nombreDirector"));
                anteproyecto.setIdAcademico(resultado.getInt("idAcademico"));
                anteproyecto.setEstadoSeguimiento(resultado.getString("nombreEstadoSeguimiento"));
                anteproyecto.setIdEstadoSeguimiento(resultado.getInt("idEstadoSeguimiento"));
                anteproyecto.setNombreModalidad(resultado.getString("nombreModalidad"));
                anteproyecto.setNombreCuerpoAcademico(resultado.getString("nombreCuerpoAcademico"));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al obtener la informaicon de los anteproyectos",
                    Codigos.ERROR_CONSULTA);
        }
        return anteproyecto;
    }

    public ArrayList<Anteproyecto> obtenerAnteproyectos() throws DAOException {
        ArrayList<Anteproyecto> anteproyectos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(OBTENER_ANTEPROYECTOS);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                anteproyectos.add(new Anteproyecto(
                        resultado.getInt("idAnteproyecto"),
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getInt("idModalidad"),
                        resultado.getString("fechaCreacionAnteproyecto"),
                        resultado.getString("ciudadCreacionAnteproyecto"),
                        resultado.getString("nombreProyectoInvestigacion"),
                        resultado.getString("lineaInvestigacion"),
                        resultado.getString("nombreTrabajoRecepcional"),
                        resultado.getString("requisitosAnteproyecto"),
                        resultado.getString("descripcionProyectoInvestigacion"),
                        resultado.getString("descripcionTrabajoRecepcional"),
                        resultado.getString("resultadosEsperadosAnteproyecto"),
                        resultado.getString("bibliografiasRecomendadasAnteproyecto"),
                        resultado.getString("duracionAproximadaAnteproyecto"),
                        resultado.getInt("alumnosParticipantesAnteproyecto"),
                        resultado.getString("notasExtraAnteproyecto"),
                        resultado.getString("nombreDirector"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreEstadoSeguimiento"),
                        resultado.getInt("idEstadoSeguimiento"),
                        resultado.getString("nombreModalidad"),
                        resultado.getString("nombreCuerpoAcademico")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al obtener la informaicon de los anteproyectos",
                    Codigos.ERROR_CONSULTA);
        }
        return anteproyectos;
    }

    public ArrayList<Anteproyecto> obtenerAnteproyectosPorCuerpoAcademico(int idCuerpoAcademico) throws DAOException {
        ArrayList<Anteproyecto> anteproyectos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ANTEPROYECTOS_POR_CUERPO_ACADEMICO);
            sentencia.setInt(1, idCuerpoAcademico);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                anteproyectos.add(new Anteproyecto(
                        resultado.getInt("idAnteproyecto"),
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getInt("idModalidad"),
                        resultado.getString("fechaCreacionAnteproyecto"),
                        resultado.getString("ciudadCreacionAnteproyecto"),
                        resultado.getString("nombreProyectoInvestigacion"),
                        resultado.getString("lineaInvestigacion"),
                        resultado.getString("nombreTrabajoRecepcional"),
                        resultado.getString("requisitosAnteproyecto"),
                        resultado.getString("descripcionProyectoInvestigacion"),
                        resultado.getString("descripcionTrabajoRecepcional"),
                        resultado.getString("resultadosEsperadosAnteproyecto"),
                        resultado.getString("bibliografiasRecomendadasAnteproyecto"),
                        resultado.getString("duracionAproximadaAnteproyecto"),
                        resultado.getInt("alumnosParticipantesAnteproyecto"),
                        resultado.getString("notasExtraAnteproyecto"),
                        resultado.getString("nombreDirector"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreEstadoSeguimiento"),
                        resultado.getInt("idEstadoSeguimiento"),
                        resultado.getString("nombreModalidad"),
                        resultado.getString("nombreCuerpoAcademico")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al obtener la informacion de los anteproyectos",
                    Codigos.ERROR_CONSULTA);
        }
        return anteproyectos;
    }

    public ArrayList<Anteproyecto> obtenerAnteproyectosPorAcademico(int idAcademico) throws DAOException {
        ArrayList<Anteproyecto> anteproyectos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ANTEPROYECTOS_POR_ACADEMICO);
            sentencia.setInt(1, idAcademico);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                anteproyectos.add(new Anteproyecto(
                        resultado.getInt("idAnteproyecto"),
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getInt("idModalidad"),
                        resultado.getString("fechaCreacionAnteproyecto"),
                        resultado.getString("ciudadCreacionAnteproyecto"),
                        resultado.getString("nombreProyectoInvestigacion"),
                        resultado.getString("lineaInvestigacion"),
                        resultado.getString("nombreTrabajoRecepcional"),
                        resultado.getString("requisitosAnteproyecto"),
                        resultado.getString("descripcionProyectoInvestigacion"),
                        resultado.getString("descripcionTrabajoRecepcional"),
                        resultado.getString("resultadosEsperadosAnteproyecto"),
                        resultado.getString("bibliografiasRecomendadasAnteproyecto"),
                        resultado.getString("duracionAproximadaAnteproyecto"),
                        resultado.getInt("alumnosParticipantesAnteproyecto"),
                        resultado.getString("notasExtraAnteproyecto"),
                        resultado.getString("nombreDirector"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreEstadoSeguimiento"),
                        resultado.getInt("idEstadoSeguimiento"),
                        resultado.getString("nombreModalidad"),
                        resultado.getString("nombreCuerpoAcademico")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al obtener la informacion de los anteproyectos",
                    Codigos.ERROR_CONSULTA);
        }
        return anteproyectos;
    }

    public ArrayList<Anteproyecto> obtenerAnteproyectosPorEstadoSeguimiento(int idEstadoSeguimiento)
            throws DAOException {
        ArrayList<Anteproyecto> anteproyectos = new ArrayList<>();
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ANTEPROYECTOS_POR_ESTADO_SEGUIMIENTO);
            sentencia.setInt(1, idEstadoSeguimiento);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                anteproyectos.add(new Anteproyecto(
                        resultado.getInt("idAnteproyecto"),
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getInt("idModalidad"),
                        resultado.getString("fechaCreacionAnteproyecto"),
                        resultado.getString("ciudadCreacionAnteproyecto"),
                        resultado.getString("nombreProyectoInvestigacion"),
                        resultado.getString("lineaInvestigacion"),
                        resultado.getString("nombreTrabajoRecepcional"),
                        resultado.getString("requisitosAnteproyecto"),
                        resultado.getString("descripcionProyectoInvestigacion"),
                        resultado.getString("descripcionTrabajoRecepcional"),
                        resultado.getString("resultadosEsperadosAnteproyecto"),
                        resultado.getString("bibliografiasRecomendadasAnteproyecto"),
                        resultado.getString("duracionAproximadaAnteproyecto"),
                        resultado.getInt("alumnosParticipantesAnteproyecto"),
                        resultado.getString("notasExtraAnteproyecto"),
                        resultado.getString("nombreDirector"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreEstadoSeguimiento"),
                        resultado.getInt("idEstadoSeguimiento"),
                        resultado.getString("nombreModalidad"),
                        resultado.getString("nombreCuerpoAcademico")));
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Lo sentimos, hubo un problema al obtener la informacion de los anteproyectos",
                    Codigos.ERROR_CONSULTA);
        }
        return anteproyectos;
    }

    public int guardarAnteproyecto(Anteproyecto anteproyecto) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_ANTEPROYECTO,
                    Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, anteproyecto.getFechaCreacion());
            sentencia.setString(2, anteproyecto.getNombreProyectoInvestigacion());
            sentencia.setString(3, anteproyecto.getCiudadCreacion());
            sentencia.setString(4, anteproyecto.getLineaInvestigacion());
            sentencia.setString(5, anteproyecto.getDuracionAproximada());
            sentencia.setString(6, anteproyecto.getNombreTrabajoRecepcional());
            sentencia.setString(7, anteproyecto.getRequisitos());
            sentencia.setInt(8, anteproyecto.getNumeroMaximoAlumnosParticipantes());
            sentencia.setString(9, anteproyecto.getDescripcionProyectoInvestigacion());
            sentencia.setString(10, anteproyecto.getDescripcionTrabajoRecepcional());
            sentencia.setString(11, anteproyecto.getResultadosEsperadosAnteproyecto());
            sentencia.setString(12, anteproyecto.getBibliografiaRecomendada());
            sentencia.setString(13, anteproyecto.getNotasExtras());
            sentencia.setInt(14, anteproyecto.getIdAcademico());
            System.out.println(anteproyecto.getIdEstadoSeguimiento());
            sentencia.setInt(15, anteproyecto.getIdEstadoSeguimiento());
            if (anteproyecto.getIdCuerpoAcademico() > 0) {
                sentencia.setInt(16, anteproyecto.getIdCuerpoAcademico());
            } else {
                sentencia.setNull(16, Types.INTEGER);
            }
            if (anteproyecto.getIdModalidad() > 0) {
                sentencia.setInt(17, anteproyecto.getIdModalidad());
            } else {
                sentencia.setNull(17, Types.INTEGER);
            }
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            while (resultado.next()) {
                respuesta = resultado.getInt(1);
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Lo sentimos, hubo un problema al registrar el anteproyecto.",
                    Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int actualizarAnteproyecto(Anteproyecto anteproyecto) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ACTUALIZAR_ANTEPROYECTO);
            sentencia.setString(1, anteproyecto.getFechaCreacion());
            sentencia.setString(2, anteproyecto.getNombreProyectoInvestigacion());
            sentencia.setString(3, anteproyecto.getCiudadCreacion());
            sentencia.setString(4, anteproyecto.getLineaInvestigacion());
            sentencia.setString(5, anteproyecto.getDuracionAproximada());
            sentencia.setString(6, anteproyecto.getNombreTrabajoRecepcional());
            sentencia.setString(7, anteproyecto.getRequisitos());
            sentencia.setInt(8, anteproyecto.getNumeroMaximoAlumnosParticipantes());
            sentencia.setString(9, anteproyecto.getDescripcionProyectoInvestigacion());
            sentencia.setString(10, anteproyecto.getDescripcionTrabajoRecepcional());
            sentencia.setString(11, anteproyecto.getResultadosEsperadosAnteproyecto());
            sentencia.setString(12, anteproyecto.getBibliografiaRecomendada());
            sentencia.setString(13, anteproyecto.getNotasExtras());
            sentencia.setInt(14, anteproyecto.getIdAcademico());
            sentencia.setInt(15, anteproyecto.getIdEstadoSeguimiento());
            sentencia.setInt(16, anteproyecto.getIdCuerpoAcademico());
            sentencia.setInt(17, anteproyecto.getIdModalidad());
            sentencia.setInt(18, anteproyecto.getIdAnteproyecto());
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? anteproyecto.getIdAnteproyecto() : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int eliminarAnteproyecto(int idAnteproyecto) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(ELIMINAR_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? idAnteproyecto : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {

        }
        return respuesta;
    }

    public boolean verificarSiAcademicoEsDirector(int idAcademico) throws DAOException {
        boolean esDirector = false;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(VERIFICAR_SI_ACADEMICO_ES_DIRECTOR);
            sentencia.setInt(1, idAcademico);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                esDirector = resultado.getBoolean("esDirector");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return esDirector;
    }

    public int guardarCodirectorAnteproyecto(int idAnteproyecto, int idCodirector) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(GUARDAR_CODIRECTOR_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            sentencia.setInt(2, idCodirector);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? 1 : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int guardarLgacAnteproyecto(int idAnteproyecto, int idLgac) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD().prepareStatement(GUARDAR_LGAC_ANTEPROYECTO);
            sentencia.setInt(1, idLgac);
            sentencia.setInt(2, idAnteproyecto);
            int filasAfectadas = sentencia.executeUpdate();
            respuesta = (filasAfectadas == 1) ? 1 : -1;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int eliminarLgacsAnteproyecto(int idAnteproyecto) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(ELIMINAR_LGACS_DE_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            sentencia.executeUpdate();
            respuesta = idAnteproyecto;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public int eliminarCodirectoresAnteproyecto(int idAnteproyecto) throws DAOException {
        int respuesta = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(ELIMINAR_CODIRECTORES_DE_ANTEPROYECTO);
            sentencia.setInt(1, idAnteproyecto);
            sentencia.executeUpdate();
            respuesta = idAnteproyecto;
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return respuesta;
    }

    public Anteproyecto obtenerAnteproyectosPorEstudiante(int idEstudiante) throws DAOException {
        Anteproyecto anteproyecto = null;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(OBTENER_ANTEPROYECTOS_POR_ESTUDIANTE);
            sentencia.setInt(1, idEstudiante);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                anteproyecto = new Anteproyecto(
                        resultado.getInt("idAnteproyecto"),
                        resultado.getInt("idCuerpoAcademico"),
                        resultado.getInt("idModalidad"),
                        resultado.getString("fechaCreacionAnteproyecto"),
                        resultado.getString("ciudadCreacionAnteproyecto"),
                        resultado.getString("nombreProyectoInvestigacion"),
                        resultado.getString("lineaInvestigacion"),
                        resultado.getString("nombreTrabajoRecepcional"),
                        resultado.getString("requisitosAnteproyecto"),
                        resultado.getString("descripcionProyectoInvestigacion"),
                        resultado.getString("descripcionTrabajoRecepcional"),
                        resultado.getString("resultadosEsperadosAnteproyecto"),
                        resultado.getString("bibliografiasRecomendadasAnteproyecto"),
                        resultado.getString("duracionAproximadaAnteproyecto"),
                        resultado.getInt("alumnosParticipantesAnteproyecto"),
                        resultado.getString("notasExtraAnteproyecto"),
                        resultado.getString("nombreDirector"),
                        resultado.getInt("idAcademico"),
                        resultado.getString("nombreEstadoSeguimiento"),
                        resultado.getInt("idEstadoSeguimiento"),
                        resultado.getString("nombreModalidad"),
                        resultado.getString("nombreCuerpoAcademico"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return anteproyecto;
    }

    public int actualizarEstadoSeguimiento(int idAnteproyecto, int idEstadoSegumientoNuevo) throws DAOException {
        int exito = -1;
        try {
            PreparedStatement sentencia = ConexionBD.obtenerConexionBD()
                    .prepareStatement(ACTUALIZAR_ESTADO_SEGUMIMIENTO);
            sentencia.setInt(1, idEstadoSegumientoNuevo);
            sentencia.setInt(2, idAnteproyecto);
            exito = sentencia.executeUpdate();
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            throw new DAOException("Error de consulta", Codigos.ERROR_CONSULTA);
        }
        return exito;
    }

}
