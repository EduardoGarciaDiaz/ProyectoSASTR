/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 25/05/2023
 * Descripción: La clase FXMLConsultarAvanceEstudiante actúa como controlador
 * de la vista ConsultarAvancesEstudiante. Contiene los métodos necesarios 
 * para la consulta del avance de un estudiante, tal como actividades completadas, anteproyecto, etc.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.ConsultarAvanceEstudianteSingleton;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaActividad;

public class FXMLConsultarAvanceEstudianteController implements Initializable {
    
    @FXML
    private Label lbNombreEstudiante;
    @FXML
    private Label lbMatriculaEstudiante;
    @FXML
    private Label lbCorreoInstitucionalEstudiante;
    @FXML
    private Label lbCursandoActualmente;
    @FXML
    private Label lbNombreTrabajoRecepcional;
    @FXML
    private Label lbModalidadAnteproyecto;
    @FXML
    private Label lbActividadesCompletadas;
    @FXML
    private Label lbActividadesNoRealizadas;
    @FXML
    private Label lbActividadesRestantes;
    @FXML
    private Label lbPorcentaje;
    @FXML
    private VBox vbxContenedorTarjetasActividades;
    @FXML
    private Button btnVerDetallesAnteproyecto;
    
    private ObservableList<Actividad> actividades;
    private Estudiante estudiante;
    private Anteproyecto anteproyecto;
    private ConsultarAvanceEstudianteSingleton consultarAvanceEstudiante;
    private CodigosVentanas ventanaOrigen;
    private Curso cursoAuxiliar;
    private Academico academicoAuxiliar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setEstudianteAcademico(Estudiante estudiante, Academico academico, CodigosVentanas origen, Curso curso) {
        this.academicoAuxiliar = academico;
        this.estudiante = estudiante;
        this.ventanaOrigen = origen;
        ConsultarAvanceEstudianteSingleton.setConsultarAvanceEstudiante(null);
        consultarAvanceEstudiante = ConsultarAvanceEstudianteSingleton
                .obtenerConsultarAvanceEstudiante(academico, estudiante, ventanaOrigen, curso);
        obtenerActividades();
        cargarTarjetasActividades();
        obtenerAnteproyecto();
        setDatosEstudiante();
        setDatosAnteproyecto();
        setDatosAvanceGeneral();
    }
    
    public void setCurso(Curso curso) {
        this.cursoAuxiliar = curso;
    }
    
    private void obtenerActividades() {
        try {
            actividades = FXCollections.observableArrayList(
                new ActividadDAO().obtenerActividadesPorEstudiante(estudiante.getIdEstudiante(), estudiante.getIdAnteproyecto())
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void obtenerAnteproyecto() {
        try {
            anteproyecto = new AnteproyectoDAO().obtenerAnteproyectoPorId(estudiante.getIdAnteproyecto());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarTarjetasActividades() {
        for (Actividad actividad : actividades) {
            TarjetaActividad tarjeta = new TarjetaActividad(actividad);
            tarjeta.getBotonVerDetalles().setOnAction(
                (event) -> {
                    irAVistaConsultarEntregasActividad(actividad);
                }
            );
            vbxContenedorTarjetasActividades.getChildren().add(tarjeta);
        }
    }
    
    private void setDatosEstudiante() {
        lbNombreEstudiante.setText(obtenerNombreEstudiante());
        lbMatriculaEstudiante.setText(estudiante.getMatriculaEstudiante());
        lbCorreoInstitucionalEstudiante.setText(estudiante.getCorreoInstitucional());
        lbCursandoActualmente.setText(estudiante.getCursoEstudiante());
    }
    
    private String obtenerNombreEstudiante() {
        String nombreCompletoEstudiante = estudiante.getNombre() 
                + " " + estudiante.getPrimerApellido() 
                + " " + estudiante.getSegundoApellido();
        if(nombreCompletoEstudiante.contains("null")) {
            nombreCompletoEstudiante = nombreCompletoEstudiante.replaceAll("null", " ");
        }
        return nombreCompletoEstudiante;
    }
    
    private void setDatosAnteproyecto() {
        if (anteproyecto.getIdAnteproyecto() > 0) {
            lbNombreTrabajoRecepcional.setText(anteproyecto.getNombreTrabajoRecepcional());
            lbModalidadAnteproyecto.setText(anteproyecto.getNombreModalidad());
        } else {
            lbNombreTrabajoRecepcional.setText("SIN ANTEPROYECTO ASIGNADO");
            btnVerDetallesAnteproyecto.setDisable(true);
        }
    }
    
    private void setDatosAvanceGeneral() {
        try {
            int totalActividades = new ActividadDAO()
                    .obtenerNumeroActividadesPorEstudiante(estudiante.getIdEstudiante(), estudiante.getIdAnteproyecto());
            int actividadesCompletadas = new ActividadDAO()
                    .obtenerNumeroActividadesCompletadas(estudiante.getIdEstudiante(), estudiante.getIdAnteproyecto());
            int actividadesNoRealizadas = new ActividadDAO()
                    .obtenerNumeroActividadesNoCompletadas(estudiante.getIdEstudiante(), estudiante.getIdAnteproyecto());
            int actividadesRestantes = totalActividades - actividadesCompletadas - actividadesNoRealizadas;
            double porcentajeAvance = 0.0;
            if (totalActividades != 0) {
                porcentajeAvance = actividadesCompletadas * 100 / totalActividades;
            }
            lbActividadesCompletadas.setText(actividadesCompletadas + "/" + totalActividades + " actividades completadas.");
            lbActividadesNoRealizadas.setText(actividadesNoRealizadas + "/" + totalActividades + " actividades no realizadas.");
            lbActividadesRestantes.setText(actividadesRestantes + "/" + totalActividades + " actividades restantes.");
            lbPorcentaje.setText(porcentajeAvance + "%");
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }

    @FXML
    private void clicVerDetallesAnteproyecto(ActionEvent event) {
        irAVistaDetallesAnteproyecto(anteproyecto);
    }
    
    @FXML
    private void cllicBtnRegresar(MouseEvent event) {
        switch (consultarAvanceEstudiante.getVentanaOrigen()) {
            case ESTUDIANTES_ASIGNADOS:
                irAVistaEstudiantesAsignados(academicoAuxiliar);
                break;
            case CONSULTAR_AVANCES_ESTUDIANTES:
                irAVistaConsultarAvancesCurso(cursoAuxiliar, academicoAuxiliar);
            default:
                System.out.println("Error. No se pudo encontrar la ventana de origen.");
        }
    }
    
    private void irAVistaDetallesAnteproyecto(Anteproyecto anteproyecto) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesAnteproyecto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesAnteproyectoController controladorDetallesAnteproyecto = accesoControlador.getController();
            controladorDetallesAnteproyecto.setAnteproyecto(anteproyecto);
            controladorDetallesAnteproyecto.setAcademico(academicoAuxiliar, CodigosVentanas.CONSULTAR_AVANCE_DE_ESTUDIANTE);
            controladorDetallesAnteproyecto.setDatosVerAvanceEstudiante(estudiante, ventanaOrigen, cursoAuxiliar);
            Stage escenario = (Stage) lbActividadesCompletadas.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Detalles Anteproyecto");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaEstudiantesAsignados(Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLEstudiantesAsignados.fxml"));
            Parent vista = accesoControlador.load();
            FXMLEstudiantesAsignadosController controladorVistaEstudiantes = accesoControlador.getController();
            controladorVistaEstudiantes.setDirector(academico);
            Stage escenario = (Stage) lbActividadesCompletadas.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Estudiantes");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaConsultarAvancesCurso(Curso curso, Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarAvances.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarAvancesController controladorVistaAvances = accesoControlador.getController();
            controladorVistaAvances.setCursoAcademico(consultarAvanceEstudiante.getCurso(), academico);
            controladorVistaAvances.setIdCurso(consultarAvanceEstudiante.getCurso().getIdCurso());
            Stage escenario = (Stage) lbActividadesCompletadas.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Avances de estudiantes");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaConsultarEntregasActividad(Actividad actividad) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarEntregasActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarEntregasActividadController controladorVistaEntregasActividades = accesoControlador.getController();
            controladorVistaEntregasActividades.setActividad(actividad);
            Stage escenario = (Stage) lbActividadesCompletadas.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Entregas de la actividad");
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
            default:
                throw new AssertionError();
        }
    }
    
}
