/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Controla la vista de avances de los estudiantes
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAvanceEstudiante;

public class FXMLConsultarAvancesController implements Initializable {

    @FXML
    private VBox vbxCards;
    @FXML
    private Label lbNombreCurso;
    @FXML
    private Label lbNRC;
    @FXML
    private Label lbPeriodoEscolar;
    @FXML
    private Label lbSeccion;
    @FXML
    private Label lbBloque;
    
    private Curso curso;
    private int idCurso;
    private final DateTimeFormatter FORMATO_FECHA_PERIODO = DateTimeFormatter.ofPattern("MMM ' 'yyyy", new Locale("es"));
    private Academico academico;

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    }
    
    public void setCursoAcademico(Curso curso, Academico academico) {
        this.curso = curso;
        this.academico = academico;
        mostrarInformacionCurso();
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
        obtenerInformacionEstudiantes();
    }
    
    public void mostrarInformacionCurso() {
        if (curso != null) {
            lbNombreCurso.setText(curso.getNombreCurso());
            lbNRC.setText(curso.getNrcCurso());
            LocalDate fechaInicio = LocalDate.parse(curso.getInicioPeriodoEscolar());
            LocalDate fechaFin = LocalDate.parse(curso.getFinPeriodoEscolar());
            String fechaInicioFormateda = fechaInicio.format(FORMATO_FECHA_PERIODO);
            String fechaFinFormateada = fechaFin.format(FORMATO_FECHA_PERIODO);
            String periodoEscolar = fechaInicioFormateda + " - " + fechaFinFormateada;
            lbPeriodoEscolar.setText(periodoEscolar);
            lbSeccion.setText(curso.getSeccionCurso());
            lbBloque.setText(curso.getBloqueCurso());
        } else {
            System.err.println("El curso que se recibió viene NULO");
        }
    }
    
    public void obtenerInformacionEstudiantes() {
        ArrayList<Estudiante> estudiantesCurso;
        try {
            estudiantesCurso = new EstudianteDAO().obtenerEstudiantesPorIdCurso(idCurso);
            for (Estudiante e : estudiantesCurso) {
                String nombreCompleto = obtenerNombreEstudiante(e);
                Map<String, Integer> actividades = calcularNumeroActividades(e.getIdEstudiante(), e.getIdAnteproyecto());
                int totalActividades = actividades.get("total");
                int numeroActividadesCompletadas = actividades.get("completadas");
                int numeroActividadesNoCompletadas = actividades.get("noCompletadas");
                int numeroActividadesRestantes = actividades.get("restantes");
                double porcentaje = 0.0;
                if (totalActividades > 0) {
                    porcentaje = (numeroActividadesCompletadas * 100) / totalActividades;
                }
                String actividadesCompletadas = String.valueOf(numeroActividadesCompletadas) + " / " + String.valueOf(totalActividades);
                String actividadesNoCompletadas = String.valueOf(numeroActividadesNoCompletadas) 
                        + " / " + String.valueOf(totalActividades);
                String actividadesRestantes = String.valueOf(numeroActividadesRestantes) + " / " + String.valueOf(totalActividades);
                TarjetaAvanceEstudiante tarjeta = new TarjetaAvanceEstudiante(e.getIdEstudiante(), nombreCompleto,
                        e.getMatriculaEstudiante(), e.getAnteproyectoEstudiante(), porcentaje,
                        actividadesCompletadas, actividadesNoCompletadas, actividadesRestantes);
                tarjeta.getBotonVerAvance().setOnAction(
                    (event) -> {
                        irAVistaAvance(e);
                    }
                );
                vbxCards.getChildren().add(tarjeta);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private String obtenerNombreEstudiante(Estudiante estudiante) {
        String nombre = "";
        String primerApellido = "";
        String segundoApellido = "";
        if (estudiante.getNombre() != null) {
            nombre = estudiante.getNombre();
        }
        if (estudiante.getPrimerApellido() != null) {
            primerApellido = estudiante.getPrimerApellido();
        } 
        if (estudiante.getSegundoApellido() != null) {
            segundoApellido = estudiante.getSegundoApellido();
        }
        return nombre + " " + primerApellido + " " + segundoApellido;
    }
    
    private Map<String, Integer> calcularNumeroActividades(int idEstudiante, int idAnteproyecto) {
        Map<String, Integer> actividades = new HashMap<>();
        try {
            ActividadDAO actividadDAO = new ActividadDAO();
            int totalActividades = actividadDAO
                    .obtenerNumeroActividadesPorEstudiante(idEstudiante, idAnteproyecto);
            int numeroActividadesCompletadas = actividadDAO.obtenerNumeroActividadesCompletadas(idEstudiante, idAnteproyecto);
            int numeroActividadesNoCompletadas = actividadDAO.obtenerNumeroActividadesNoCompletadas(idEstudiante, idAnteproyecto);
            int numeroActividadesRestantes = totalActividades - (numeroActividadesCompletadas + numeroActividadesNoCompletadas);
            actividades.put("total", totalActividades);
            actividades.put("completadas", numeroActividadesCompletadas);
            actividades.put("noCompletadas", numeroActividadesNoCompletadas);
            actividades.put("restantes", numeroActividadesRestantes);
            return actividades;
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return actividades;
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaCursos(academico);
    }
    
    private void irAVistaCursos(Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCursosProfesor.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCursosProfesorController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setProfesor(academico);
            Stage escenario = (Stage) lbBloque.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Cursos del profesor");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaAvance(Estudiante estudiante) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarAvanceEstudiante.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarAvanceEstudianteController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setEstudianteAcademico(estudiante, academico, CodigosVentanas.CONSULTAR_AVANCES_ESTUDIANTES, curso);
            Stage escenario = (Stage) lbBloque.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Avance del estudiante");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                System.out.println("Ocurrió un error de consulta.");
                break;
            case ERROR_CONEXION_BD:
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", Alert.AlertType.ERROR);
                break;
            default:
                throw new AssertionError();
        }
    }
    
}