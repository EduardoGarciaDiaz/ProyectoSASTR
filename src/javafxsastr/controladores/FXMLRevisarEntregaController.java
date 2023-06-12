/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 04/06/2023
 * Descripción: La clase FXMLRevisarEntrega actúa como controlador
 * de la vista RevisarEntrega. Contiene los métodos necesarios 
 * para la revision de entregas por parte del director de trabajo recepcional.
 */

package javafxsastr.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.ArchivoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EntregaDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Archivo;
import javafxsastr.modelos.pojo.ConsultarAvanceEstudianteSingleton;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;

public class FXMLRevisarEntregaController implements Initializable {

    @FXML
    private Label lbNombreActividad;
    @FXML
    private Label lbFechaRecibido;
    @FXML
    private Label lbHoraRecibido;
    @FXML
    private Label lbNumeroEntrega;
    @FXML
    private TextArea txaComentariosEstudiante;
    @FXML
    private HBox hbxContenedorArchivosAlumno;
    @FXML
    private TextArea txaComentariosDirector;
    @FXML
    private HBox hbxContenedorArchivosRevision;
    @FXML
    private Button btnEnviarRevision;
    @FXML
    private Button btnAdjuntarArchivo;
    
    private ObservableList<Archivo> archivosRevision = FXCollections.observableArrayList();
    private ConsultarAvanceEstudianteSingleton consultarAvanceEstudiante = ConsultarAvanceEstudianteSingleton
            .obtenerConsultarAvanceEstudiante(null, null, null, null);
    private Entrega entrega;
    private ObservableList<Archivo> archivosEntrega;
    private Academico academico;
    private Actividad actividad;
    int numeroEntrega;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    public void inicializarContenidoEntrega(Entrega entrega, Academico academico, Actividad actividad) {
        agregarListenerCampoComentarios();
        this.entrega = entrega;
        this.academico = academico;
        this.actividad = actividad;
        if (entrega != null) {
            setDatosEntrega(actividad);
            obtenerArchivos();
            cargarArchivos();
            btnEnviarRevision.setDisable(true);
        }
        if (!validarSiEsDirector()) {
            txaComentariosDirector.setEditable(false);
            btnAdjuntarArchivo.setDisable(true);
        }
    }
    
    public void setNumeroEntrega(int numeroEntrega) {
        this.numeroEntrega = numeroEntrega;
    }
        
    private void setDatosEntrega(Actividad actividad){
        lbFechaRecibido.setText(entrega.getFechaEntrega());
        lbHoraRecibido.setText(entrega.getHoraEntrega());
        lbNumeroEntrega.setText(String.valueOf(numeroEntrega));
        txaComentariosEstudiante.setText(entrega.getComentarioAlumno());
        lbNombreActividad.setText(actividad.getNombreActividad());
        if (entrega.getComentarioDirector() != null) {
            txaComentariosDirector.setText(entrega.getComentarioDirector());
        }
    }
    
    private void obtenerArchivos(){
        try {
            archivosEntrega = FXCollections.observableArrayList(
                new ArchivoDAO().consultarArchivosPorEntrega(entrega.getIdEntrega()));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarArchivos() {
        hbxContenedorArchivosRevision.getChildren().clear();
        for (Archivo archivo : archivosEntrega) {
            if (!archivo.getEsEntrega()) {
                archivosRevision.add(archivo);
            }
            configurarBotonArchivo(archivo);
        }
    }
    
    private void configurarBotonArchivo(Archivo archivo) {
        Pane contenedorArchivo = new Pane();
        contenedorArchivo.setPrefSize(200, 20);
        contenedorArchivo.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15");
        ImageView imgIconoArchivo = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/archivo.png"));
        contenedorArchivo.getChildren().add(imgIconoArchivo);
        imgIconoArchivo.setFitHeight(37);
        imgIconoArchivo.setFitWidth(37);
        imgIconoArchivo.setLayoutX(5);
        imgIconoArchivo.setLayoutY(7);
        Label lbNombreArchivo = new Label(archivo.getNombreArchivo());
        lbNombreArchivo.setPrefWidth(100);
        contenedorArchivo.getChildren().add(lbNombreArchivo);
        lbNombreArchivo.setLayoutX(50);
        lbNombreArchivo.setLayoutY(16);
        if ((archivo.getEsEntrega()) || (!validarSiEsDirector())) {
            ImageView imgIconoDescarga = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/descargas.png"));
            contenedorArchivo.getChildren().add(imgIconoDescarga);
            imgIconoDescarga.setFitHeight(38);
            imgIconoDescarga.setFitWidth(38);
            imgIconoDescarga.setLayoutX(150);
            imgIconoDescarga.setLayoutY(5);
            contenedorArchivo.setId(String.valueOf(archivo.getIdArchivo()));
            contenedorArchivo.setOnMouseClicked(
                (event) -> {
                    descargarArchivo(archivo);
                }
            );
            if (!archivo.getEsEntrega()) {
                hbxContenedorArchivosRevision.getChildren().add(contenedorArchivo);
            } else {
                hbxContenedorArchivosAlumno.getChildren().add(contenedorArchivo);
            }
        } else {
            ImageView imgIconoEliminar = new ImageView(
                    new Image("file:src/javafxsastr/recursos/iconos/eliminar-archivo-adjunto.png")
            );
            contenedorArchivo.getChildren().add(imgIconoEliminar);
            imgIconoEliminar.setFitHeight(30);
            imgIconoEliminar.setFitWidth(30);
            imgIconoEliminar.setLayoutX(150);
            imgIconoEliminar.setLayoutY(5);
            contenedorArchivo.setId(String.valueOf(archivo.getIdArchivo()));
            imgIconoEliminar.setOnMouseClicked(
                (event) -> {
                    contenedorArchivo.setVisible(false);
                    eliminarArchivo(contenedorArchivo, archivo);
                }
            );
            imgIconoArchivo.setOnMouseClicked(
                (event) -> {
                    descargarArchivo(archivo);
                }
            );            
            hbxContenedorArchivosRevision.getChildren().add(contenedorArchivo);
        }

    }
    
    private void eliminarArchivo(Pane contenedorArchivo, Archivo archivo){
        hbxContenedorArchivosRevision.getChildren().remove(contenedorArchivo);
        archivosRevision.remove(archivo);
    }
    
    private void descargarArchivo(Archivo archivo){
        DirectoryChooser directorioSeleccion = new DirectoryChooser();
        File directorioSeleccionado = directorioSeleccion.showDialog(this.txaComentariosDirector.getScene().getWindow());
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
    
    private void agregarListenerCampoComentarios() {
        txaComentariosDirector.focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue) {
                    if (validarSiEsDirector()) {
                        btnEnviarRevision.setDisable(false);
                    }
                }
                if (oldValue) {
                    if (txaComentariosDirector.getText().isEmpty()) {
                        btnEnviarRevision.setDisable(true);
                    }
                }
            }
        );
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaConsultarEntregasActividad();
    }

    @FXML
    private void clicBtnAdjuntarArchivo(ActionEvent event) {
        File archivo;
        FileChooser dialogoSeleccionImg = new FileChooser();
        dialogoSeleccionImg.setTitle("Selecciona un archivo:");
        FileChooser.ExtensionFilter filtroDialogo = new FileChooser.ExtensionFilter(
                "Cualquier .PDF, .ZIP, .TXT, .XLSX, .DOCX, .PPTX", 
                "*.PDF", "*.ZIP", "*.TXT", "*.XLSX", "*.DOCX", "*.PPTX", "*.PNG", "*.JPG");
        dialogoSeleccionImg.getExtensionFilters().add(filtroDialogo);
        Stage escenarioPrincipal = (Stage) lbFechaRecibido.getScene().getWindow();
        archivo = dialogoSeleccionImg.showOpenDialog(escenarioPrincipal);
        Archivo archivoRevision = new Archivo();
        if (archivo != null) {
            try {
                archivoRevision.setArchivo(Files.readAllBytes(archivo.toPath()));
            } catch (IOException ex) {
                System.out.println("Error");
            }
            archivoRevision.setEsEntrega(false);
            archivoRevision.setNombreArchivo(archivo.getName());
            archivoRevision.setIdEntrega(entrega.getIdEntrega());
            archivosRevision.add(archivoRevision);
            configurarBotonArchivo(archivoRevision);
        }
    }

    @FXML
    private void clicBtnEnviarRevision(ActionEvent event) {
        Entrega entregaValida = prepararEntregaValida();
        actualizarEntrega(entregaValida);
        guardarArchivosRevision();
        Utilidades.mostrarDialogoSimple("Revisión enviada", "Revisión enviada correctamente", Alert.AlertType.INFORMATION);
        irAVistaConsultarEntregasActividad();
    }
    
    @FXML
    private void activarBtnEnviarRevision(KeyEvent event) {
        if (validarSiEsDirector()) {
            btnEnviarRevision.setDisable(false);
        }
    }
    
    private Entrega prepararEntregaValida() {
        Entrega entrega = new Entrega(this.entrega.getIdEntrega(), 
                this.entrega.getComentarioAlumno(), 
                this.entrega.getFechaEntrega(), 
                this.entrega.getHoraEntrega(), 
                txaComentariosDirector.getText(), 
                LocalDate.now().toString(), 
                LocalTime.now().toString(), 
                this.entrega.getIdActividad(), 
                this.academico.getIdAcademico()
        );
        return entrega;
    }
    
    private void actualizarEntrega(Entrega entrega) {
        try {
            new EntregaDAO().actualizarEntrega(entrega);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void guardarArchivosRevision() {
        try {
            new ArchivoDAO().borrarArchivosRevision(entrega.getIdEntrega());
            for (Archivo archivo : archivosRevision) {
                new ArchivoDAO().guardarArchivo(archivo);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void irAVistaConsultarEntregasActividad() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarEntregasActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarEntregasActividadController controladorVistaEntregasActividades = accesoControlador.getController();
            controladorVistaEntregasActividades.setActividad(actividad);
            Stage escenario = (Stage) lbNombreActividad.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Entregas de la actividad");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private boolean validarSiEsDirector() {
        return consultarAvanceEstudiante.getVentanaOrigen() != CodigosVentanas.CONSULTAR_AVANCES_ESTUDIANTES;
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