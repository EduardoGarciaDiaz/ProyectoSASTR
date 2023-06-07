/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 25/05/2023
 * Descripción: La clase FXMLConsultarAvanceEstudiante actúa como controlador
 * de la vista ConsultarAvancesEstudiante. Contiene los métodos necesarios 
 * para la consulta del avance de un estudiante. 
 */

package javafxsastr.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaActividad;

public class FXMLConsultarAvanceEstudianteController implements Initializable {

    private ObservableList<Actividad> actividades;
    private Estudiante estudiante;
    private Anteproyecto anteproyecto;
    @FXML
    private ImageView clicBtnRegresar;
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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        obtenerActividades();
        cargarTarjetasActividades();
        obtenerAnteproyecto();
        setDatosEstudiante();
        setDatosAnteproyecto();
        setDatosAvanceGeneral();
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
        lbNombreEstudiante.setText(estudiante.getNombre());
        lbMatriculaEstudiante.setText(estudiante.getMatriculaEstudiante());
        lbCorreoInstitucionalEstudiante.setText(estudiante.getCorreoInstitucional());
        lbCursandoActualmente.setText(estudiante.getCursoEstudiante());
    }
    
    private void setDatosAnteproyecto() {
        lbNombreTrabajoRecepcional.setText(anteproyecto.getNombreTrabajoRecepcional());
        lbModalidadAnteproyecto.setText(anteproyecto.getNombreModalidad());
    }
    
    private void setDatosAvanceGeneral() {
        try {
            int totalActividades = new ActividadDAO().obtenerNumeroActividadesPorEstudiante(estudiante.getIdEstudiante());
            int actividadesCompletadas = new ActividadDAO().obtenerNumeroActividadesCompletadas(estudiante.getIdEstudiante());
            int actividadesNoRealizadas = new ActividadDAO().obtenerNumeroActividadesNoCompletadas(estudiante.getIdEstudiante());
            int actividadesRestantes = totalActividades - actividadesCompletadas - actividadesNoRealizadas;
            int porcentajeAvance = actividadesCompletadas * 100 / totalActividades;
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
    
    private void irAVentanaVerDetallesAnteproyecto(Anteproyecto anteproyecto) {
        
    }

    @FXML
    private void clicVerDetallesAnteproyecto(ActionEvent event) {
        irAVentanaVerDetallesAnteproyecto(anteproyecto);
    }
    
}
