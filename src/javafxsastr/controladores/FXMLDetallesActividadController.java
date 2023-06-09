/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 08/06/2023
 * Descripción: Controlador de la ventana DetallesActividad del estudiante
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EntregaDAO;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaEntregas;

public class FXMLDetallesActividadController implements Initializable {

    @FXML
    private Label lbEntrega;
    @FXML
    private Label lbNombreActividad;
    @FXML
    private TextArea taDetallesActividad;
    @FXML
    private ImageView ivArchivo;
    @FXML
    private VBox vbxEntregas;
    @FXML
    private Label lbFechaActividad;
    @FXML
    private Label lbHoraActividad;
    @FXML
    private Label lbEstadoActividad;
    @FXML
    private Pane paneEnviarEntrega;
    @FXML
    private Label lbNumeroEntregas;
    
    private Estudiante estudiante;
    private Actividad actividad;
    private ObservableList<Entrega> entregas;
    private Entrega entrega;
    private final DateTimeFormatter FORMATO_FECHA_COMPLETA = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
        new Locale("es"));
    private final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm a");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbxEntregas.getChildren().clear();
        lbNumeroEntregas.setText(0 + " Entregas enviadas");
    }    
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
        lbNumeroEntregas.setText(0 + " Entregas enviadas");
        vbxEntregas.getChildren().clear();
        mostrarInformacionActividad(actividad);
        obtenerListaEntregas();
    }
    
    private void mostrarInformacionActividad(Actividad actividad) {
        if (actividad != null) {
            LocalDate fechaInicio = LocalDate.parse(actividad.getFechaInicioActividad());
            LocalDate fechaFin = LocalDate.parse(actividad.getFechaFinActividad());
            String fechaInicioFormateada = fechaInicio.format(FORMATO_FECHA_COMPLETA);
            String fechaFinFormateada = fechaFin.format(FORMATO_FECHA_COMPLETA);
            String fechaActividad = fechaInicioFormateada + " - " + fechaFinFormateada;
            String horaActividad = actividad.getHoraInicioActividad() + " - " + actividad.getHoraFinActividad();
            String estadoActividad = actividad.getEstadoActividad();
            lbNombreActividad.setText(actividad.getNombreActividad());
            taDetallesActividad.setText(actividad.getDetallesActividad());
            lbFechaActividad.setText(fechaActividad);
            if (horaActividad.contains("null")) {
                lbHoraActividad.setText("Sin hora especificada");
            } else { 
                lbHoraActividad.setText(horaActividad);
            }
            lbEstadoActividad.setText(estadoActividad);
            if(estadoActividad.equals("No completada") || estadoActividad.equals("Completada")) {
                paneEnviarEntrega.setDisable(true);
                paneEnviarEntrega.setStyle("-fx-background-color: #e0e0e0; "
                        + "-fx-background-radius: 15px;");
            }
        } else {
            System.err.println("La actividad que se recibió viene NULA");
        }
    }
    
    public void obtenerListaEntregas() {
        try {
            if (actividad != null) {
                entregas  = FXCollections.observableArrayList(new EntregaDAO().consultarEntregasPorActividad(actividad.getIdActividad()));
                lbNumeroEntregas.setText(entregas.size() + " Entregas enviadas");
                mostrarEntregas(entregas, false);
            } else {
                 System.err.println("La actividad que se recibió viene NULA");
            }
        } catch(DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void mostrarEntregas(ObservableList<Entrega> entregas, boolean esOrdenInverso) {
        int numeroEntregaInverso = entregas.size();
        int numeroEntrega;
        for (int i = 0; i < entregas.size(); i++) {
            if (esOrdenInverso) {
                numeroEntrega = numeroEntregaInverso;
                numeroEntregaInverso --;
            } else {
                numeroEntrega = i + 1;
            }
            entrega = entregas.get(i);
            TarjetaEntregas tarjetaEntrega = new TarjetaEntregas(entrega, numeroEntrega);
            vbxEntregas.getChildren().add(tarjetaEntrega);
            Button btnVerDetalles = tarjetaEntrega.getBotonVerDetalles();
            btnVerDetalles.setOnAction((event) -> {
                    //TODO Establecer la escena a la que se dirigirá al darle clic
                irAVistaDetallesEntrega();
                System.out.println("Ver detalles clicked...");
            });
        }
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
            //TODO Redirigir a la pantalla que la activo
        Stage escenerioBase = (Stage) lbNombreActividad.getScene().getWindow();
        escenerioBase.close();
    }

    @FXML
    private void clicEnviarEntrega(MouseEvent event) {
        irAVistaEnviarEntrega();
    }
    
    private void irAVistaEnviarEntrega() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioEntregaActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioEntregaActividadController controladorVistaFormularioEntrega = accesoControlador.getController();    
            controladorVistaFormularioEntrega.setEstudiante(estudiante);
            controladorVistaFormularioEntrega.setActividad(actividad);
            Stage escenario = (Stage) lbEntrega.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Entregas de Actividad");
            escenario.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void irAVistaDetallesEntrega() {
        //TODO
        System.out.println("Ir a Vista Detalles de la entrega (ventana del estudiante)");
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