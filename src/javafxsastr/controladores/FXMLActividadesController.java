
package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaActividadGestion;

public class FXMLActividadesController implements Initializable {

    @FXML
    private VBox contenedorTarjetasActividades;
    @FXML
    private Label lbTituloVentana;
    private Estudiante estudiante;
    private ObservableList<Actividad> actividades;
    @FXML
    private Pane pnBotonCrearActividad;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        obtenerActividadesDelEstudiante();
        cargarTarjetasActividades();
    }
    
    public void obtenerActividadesDelEstudiante() {
        try {
            actividades = FXCollections.observableArrayList(
                new ActividadDAO().obtenerActividadesPorEstudiante(estudiante.getIdEstudiante()));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }

    private void cargarTarjetasActividades() {
        for (Actividad actividad : actividades) {
            TarjetaActividadGestion tarjeta = new TarjetaActividadGestion(actividad);
            tarjeta.getBotonVerDetalles().setOnAction((event) -> {
               
            });
            tarjeta.getBotonModificar().setOnAction((event) -> {
                irAModificarActividad(estudiante, actividad, true);
            });
            contenedorTarjetasActividades.getChildren().add(tarjeta);
        }
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaInicio(estudiante);
    }
    
    public void manejarDAOException(DAOException ex) {
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

    @FXML
    private void clicCrearActividad(MouseEvent event) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioActividadController controladorVistaInicio = accesoControlador.getController(); 
            controladorVistaInicio.iniciarFormularioNUevo(estudiante, false, null);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("ERROR","No se pido cargar la catividad", Alert.AlertType.ERROR);
        }
    }
    
    private void irAVistaInicio(Estudiante estudiante) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicio.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInicioController controladorVistaCrearAnteproyecto = accesoControlador.getController();
            System.out.println(this.estudiante.getEsAdministrador());
            controladorVistaCrearAnteproyecto.setUsuario(estudiante);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAModificarActividad(Estudiante estudiante, Actividad actividad, boolean esModificacion) {
        try {
                    FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioActividad.fxml"));
                    Parent vista = accesoControlador.load();
                    FXMLFormularioActividadController controladorVistaInicio = accesoControlador.getController(); 
                    controladorVistaInicio.iniciarFormularioNUevo(estudiante, esModificacion, actividad);
                    Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
                    escenario.setScene(new Scene(vista));
                    escenario.setTitle("Inicio");
                    escenario.show();
                } catch (IOException ex) {
                    Utilidades.mostrarDialogoSimple("ERROR","No se pido cargar la catividad", Alert.AlertType.ERROR);
                }
    }

}
