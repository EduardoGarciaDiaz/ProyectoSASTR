/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 25/05/2023
 * Descripción: La clase FXMLConsultarAvanceEstudiante actúa como controlador
 * de la vista ConsultarAvancesEstudiante. Contiene los métodos necesarios 
 * para la consulta del avance de un estudiante. 
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
import javafx.scene.image.ImageView;
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
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaActividad;

public class FXMLConsultarAvanceEstudianteController implements Initializable {

    private ObservableList<Actividad> actividades;
    private Estudiante estudiante;
    private Anteproyecto anteproyecto;
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
    private Label lbAvancesEnviados;
    @FXML
    private Label lbPorcentaje;
    @FXML
    private VBox vbxContenedorTarjetasActividades;
    private CodigosVentanas ventanaOrigen;
    private Curso cursoAuxiliar;
    private Academico academicoAuxiliar;
    @FXML
    private ImageView clicBtnRegresar;
    @FXML
    private Button btnVerDetallesAnteproyecto;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setEstudiante(Estudiante estudiante, CodigosVentanas origen) {
        this.estudiante = estudiante;
        this.ventanaOrigen = origen;
        obtenerActividades();
        cargarTarjetasActividades();
        obtenerAnteproyecto();
        setDatosEstudiante();
        setDatosAnteproyecto();
        setDatosAvanceGeneral();
    }
    
    public void setCursoAcademico(Curso curso, Academico academico) {
        this.cursoAuxiliar = curso;
        this.academicoAuxiliar = academico;
    }
    
    private void obtenerActividades() {
        try {
            actividades = FXCollections.observableArrayList(
                    new ActividadDAO().obtenerActividadesPorEstudiante(estudiante.getIdEstudiante()));
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
            System.out.println(actividad.getNombreActividad());
            TarjetaActividad tarjeta = new TarjetaActividad(actividad);
            tarjeta.getBotonVerDetalles().setOnAction((event) -> {
                iraVentanaEntregasActividad(actividad.getIdActividad());
            });
            vbxContenedorTarjetasActividades.getChildren().add(tarjeta);
        }
    }
    
    private void setDatosEstudiante() {
        lbNombreEstudiante.setText(estudiante.toString());
        lbMatriculaEstudiante.setText(estudiante.getMatriculaEstudiante());
        lbCorreoInstitucionalEstudiante.setText(estudiante.getCorreoInstitucional());
        lbCursandoActualmente.setText(estudiante.getCursoEstudiante());
    }
    
    private void setDatosAnteproyecto() {
        if (anteproyecto != null) {
            lbNombreTrabajoRecepcional.setText(anteproyecto.getNombreTrabajoRecepcional());
            lbModalidadAnteproyecto.setText(anteproyecto.getNombreModalidad());
        } else {
            lbNombreTrabajoRecepcional.setText("SIN ANTEPROYECTO ASIGNADO");
            btnVerDetallesAnteproyecto.setDisable(true);
        }
    }
    
    private void setDatosAvanceGeneral() {
        try {
            int totalActividades = new ActividadDAO().obtenerNumeroActividadesPorEstudiante(estudiante.getIdEstudiante());
            int actividadesCompletadas = new ActividadDAO().obtenerNumeroActividadesCompletadas(estudiante.getIdEstudiante());
            int actividadesNoRealizadas = new ActividadDAO().obtenerNumeroActividadesNoCompletadas(estudiante.getIdEstudiante());
            int actividadesRestantes = totalActividades - actividadesCompletadas - actividadesNoRealizadas;
            double porcentajeAvance = 0.0;
            if (totalActividades != 0) {
                porcentajeAvance = (double) actividadesCompletadas * 100 / totalActividades;
            }
            lbActividadesCompletadas.setText(actividadesCompletadas+"/"+totalActividades+" actividades completadas.");
            lbActividadesNoRealizadas.setText(actividadesNoRealizadas+"/"+totalActividades+" actividades no realizadas.");
            lbActividadesRestantes.setText(actividadesRestantes+"/"+totalActividades+ " actividades restantes.");
            lbPorcentaje.setText(porcentajeAvance+"%");
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                ex.printStackTrace();
                System.out.println("Ocurrió un error de consulta.");
                break;
            case ERROR_CONEXION_BD:
                ex.printStackTrace();
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", Alert.AlertType.ERROR);
            default:
                throw new AssertionError();
        }
    }
    
    private void iraVentanaEntregasActividad(int idActividad) {
        
    }

    @FXML
    private void clicVerDetallesAnteproyecto(ActionEvent event) {
        irAVistaDetallesAnteproyecto(anteproyecto);
    }
    
    private void irAVistaDetallesAnteproyecto(Anteproyecto anteproyecto) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesAnteproyecto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesAnteproyectoController controladorDetallesAnteproyecto = accesoControlador.getController();
            controladorDetallesAnteproyecto.setAnteproyectoAcademico(anteproyecto, null);
            Stage escenario = (Stage) lbActividadesCompletadas.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Detalles Anteproyecto");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaEstudiantesAsignados() {
        
    }
    
    private void irAVistaConsultarAvancesCurso(Curso curso, Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarAvances.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarAvancesController controladorVistaAvances = accesoControlador.getController();
            controladorVistaAvances.setCursoAcademico(curso, academico);
            controladorVistaAvances.setIdCurso(curso.getIdCurso());
            Stage escenario = (Stage) lbActividadesCompletadas.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Avances de estudiantes");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void cllicBtnRegresar(MouseEvent event) {
        switch (ventanaOrigen) {
            case ESTUDIANTES_ASIGNADOS:
                //TODO
                break;
            case CONSULTAR_AVANCES_ESTUDIANTES:
                irAVistaConsultarAvancesCurso(cursoAuxiliar, academicoAuxiliar);
            default:
                
        }
    }
    
}
