/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 09/06/2023
 * Descripción: Controlador de la ventana DetallesEntrega del estudiante
 */

package javafxsastr.controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.ArchivoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EntregaDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Archivo;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;

public class FXMLDetallesEntregaController implements Initializable {

    @FXML
    private Label lbEntrega;
    @FXML
    private Label lbNumeroEntrega;
    @FXML
    private TextArea taComentariosEstudiante;
    @FXML
    private Label lbFechaEnvio;
    @FXML
    private Label lbHoraEnvio;
    @FXML
    private Label lbEstadoActividad;
    @FXML
    private Label lbNumeroEntregas;
    @FXML
    private HBox hbxContenedorArchivosEstudiante;
    @FXML
    private Label lbEvaluacionDirector;
    @FXML
    private HBox hbxContenedorArchivosDirector;
    
    private Estudiante estudiante;
    private Entrega entrega;
    private Actividad actividad;
    private int numeroEntrega;
    private final DateTimeFormatter FORMATO_FECHA_COMPLETA = DateTimeFormatter.ofPattern("EEEE ',' dd 'de' MMMM 'de' yyyy",
        new Locale("es"));
    @FXML
    private TextArea taComentariosDirector;
    @FXML
    private ScrollPane paneArchivoDirector;
    @FXML
    private ImageView btnEditarEntrega;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
    
    public void setEntrega(Entrega entrega, int numeroEntrega) {
        hbxContenedorArchivosEstudiante.getChildren().clear();
        hbxContenedorArchivosDirector.getChildren().clear();
        this.entrega = entrega;
        this.numeroEntrega = numeroEntrega;
        mostrarDatosEntrega();
        mostrarDatosRevision();
    }
    
    private void mostrarDatosEntrega() {
        lbNumeroEntrega.setText("Entrega #" + numeroEntrega);
        LocalDate fechaEntrega = LocalDate.parse(entrega.getFechaEntrega());
        String fechaEntregaFormateada = fechaEntrega.format(FORMATO_FECHA_COMPLETA);
        lbFechaEnvio.setText(fechaEntregaFormateada);
        lbHoraEnvio.setText(entrega.getHoraEntrega());
        taComentariosEstudiante.setText(entrega.getComentarioAlumno());
        obtenerArchivos();
    }
    
    private void obtenerArchivos() {
        ArrayList<Archivo> archivos = null;
        try {
            archivos = new ArchivoDAO().consultarArchivosPorEntrega(entrega.getIdEntrega());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        if(archivos != null) {
            for (Archivo archivo : archivos) {
                System.out.println("archivo: " + archivo.getNombreArchivo());
                if (archivo.getEsEntrega()) {   //Obtiene los archivos enviados por el estudiante
                    mostrarArchivo(archivo, hbxContenedorArchivosEstudiante);
                } else {    //Obtiene los archivos enviador por el director
                    mostrarArchivo(archivo, hbxContenedorArchivosDirector);
                }
            }
        }
    }   
    
    private void mostrarArchivo(Archivo archivo, HBox hboxArchivo) {
        if (archivo != null) {
            configurarBotonArchivo(archivo, hboxArchivo);
        }
    }
    
    public void configurarBotonArchivo(Archivo archivo, HBox hboxArchivo) {
        System.out.println("archivo: " + archivo.getNombreArchivo());
        Pane contenedorArchivo = new Pane();
        contenedorArchivo.setPrefSize(285, 20);
        contenedorArchivo.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15");
        ImageView imgIconoArchivo = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/archivo.png"));
        ImageView imgIconoEliminarArchivo = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/descargas.png"));
        contenedorArchivo.getChildren().add(imgIconoArchivo);
        imgIconoArchivo.setFitHeight(40);
        imgIconoArchivo.setFitWidth(40);
        imgIconoArchivo.setLayoutX(12);
        imgIconoArchivo.setLayoutY(12);
        Label lbNombreArchivo = new Label(archivo.getNombreArchivo());
        contenedorArchivo.getChildren().add(lbNombreArchivo);
        lbNombreArchivo.setLayoutX(75);
        lbNombreArchivo.setLayoutY(6);
        Label lbPesoArchivo = new Label(String.valueOf(archivo.getArchivo().length / 1024) + "KB");
        contenedorArchivo.getChildren().add(lbPesoArchivo);
        lbPesoArchivo.setLayoutX(75);
        lbPesoArchivo.setLayoutY(40);
        contenedorArchivo.getChildren().add(imgIconoEliminarArchivo);
        imgIconoEliminarArchivo.setFitHeight(35);
        imgIconoEliminarArchivo.setFitWidth(35);
        imgIconoEliminarArchivo.setLayoutX(240);
        imgIconoEliminarArchivo.setLayoutY(12);
        imgIconoEliminarArchivo.setOnMouseClicked((event) -> {
            System.out.println("Clic en descargar...");
            descargarArchivo(archivo);
        });
        hboxArchivo.getChildren().add(contenedorArchivo);
    }
     
    private void descargarArchivo(Archivo archivo){
        DirectoryChooser directorioSeleccion = new DirectoryChooser();
        File directorioSeleccionado = directorioSeleccion.showDialog(this.taComentariosDirector.getScene().getWindow());
        if (directorioSeleccionado != null) {
            try {
                String rutaArchivo = directorioSeleccionado.getAbsolutePath() + "/" + archivo.getNombreArchivo();
                FileOutputStream fos = new FileOutputStream(rutaArchivo);
                fos.write(archivo.getArchivo());
                fos.close();
            } catch (IOException ex) {
                
            }
        }
    }
    
    private void mostrarDatosRevision() {
        if (entrega.getFechaRevision() != null) {
            lbEvaluacionDirector.setVisible(true);
            taComentariosDirector.setText(entrega.getComentarioDirector());
            btnEditarEntrega.setVisible(false);
        } else {
            lbEvaluacionDirector.setVisible(false);
            taComentariosDirector.setVisible(false);
            paneArchivoDirector.setVisible(false);
        }
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaDetallesActividad();
    }

    @FXML
    private void clicEditarEntrega(MouseEvent event) {
        irAVistaModificarEntregaActividad();
    }

    private void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                System.out.println("Ocurrió un error de consulta.");
                break;
            case ERROR_CONEXION_BD:
                Utilidades.mostrarDialogoSimple("Error de conexion",
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                        Alert.AlertType.ERROR);
            default:
                throw new AssertionError();
        }
    }
    
    private void irAVistaModificarEntregaActividad() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioEntregaActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioEntregaActividadController controladorVistaEntregaActividad = accesoControlador.getController();     
            controladorVistaEntregaActividad.setEstudiante(estudiante);
            controladorVistaEntregaActividad.setActividad(actividad);
            controladorVistaEntregaActividad.setNumeroEntrega(numeroEntrega);
            controladorVistaEntregaActividad.inicializarInformacionFormulario(true, entrega);
            Stage escenario = (Stage) lbEntrega.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Modificar Entrega");
            escenario.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaDetallesActividad() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesActividadController controladorVistaDetallesActividad = accesoControlador.getController();     
            controladorVistaDetallesActividad.setEstudiante(estudiante);
            controladorVistaDetallesActividad.setActividad(actividad);
            Stage escenario = (Stage) lbEntrega.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Detalles Actividad");
            escenario.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
