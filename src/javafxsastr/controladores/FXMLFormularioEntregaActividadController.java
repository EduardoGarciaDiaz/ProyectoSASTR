/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Controlador de la vista de entrega de actividad
 */

package javafxsastr.controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxsastr.modelos.dao.ArchivoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EntregaDAO;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Archivo;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.utils.Utilidades;

public class FXMLFormularioEntregaActividadController implements Initializable {

    @FXML
    private Label lbNombreActividad;
    @FXML
    private Label lbDetallesActividad;
    @FXML
    private TextArea taComentarios;
    @FXML
    private Label lbEntrega;
    @FXML
    private Label lbCampoComentariosError;
    @FXML
    private Button btnEnviar;
    @FXML
    private HBox hbxContenedorArchivosAlumno;
    @FXML
    private ImageView ivArchivo;
    
    private Actividad actividad;
    private Entrega entregaEdicion;
    private boolean esEdicion;
    private File archivoEntregaSeleccion;
    private ArrayList<File> archivosEntregaSeleccionados = new ArrayList<>();
    private Archivo archivoEntrega;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEnviar.setDisable(true);
        mostrarInformacionActividad(actividad);
        inicializarInformacionFormulario(esEdicion, entregaEdicion);
        validarCamposVacios();
    }
        
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        Stage escenerioBase = (Stage) lbNombreActividad.getScene().getWindow();
        escenerioBase.close();
    }

    @FXML
    private void clicAdjuntarArchivo(ActionEvent event) {
        FileChooser ventanaSeleccionArchivo = new FileChooser();
        ventanaSeleccionArchivo.setTitle("Selecciona el archivo a enviar");
        FileChooser.ExtensionFilter filtroSeleccion = new FileChooser.ExtensionFilter("Cualquier .PDF, .ZIP, .TXT, .XLSX, .DOCX, .PPTX",
                "*.PDF","*.ZIP", "*.TXT", "*.XLSX", "*.DOCX", "*.PPTX", "*.PNG", "*.JPG");
        ventanaSeleccionArchivo.getExtensionFilters().add(filtroSeleccion);
        Stage escenarioBase = (Stage) lbEntrega.getScene().getWindow();
        archivoEntregaSeleccion = ventanaSeleccionArchivo.showOpenDialog(escenarioBase);
        visualizarArchivo(archivoEntregaSeleccion);
    }
    
    private void visualizarArchivo(File archivoEntrega) {
        if (archivoEntrega != null) {
            agregarArchivo(archivoEntrega);     
        }
    }
    
    private void agregarArchivo(File archivoEntrega) {
        archivosEntregaSeleccionados.add(archivoEntrega);
        configurarBotonArchivo(archivoEntrega);
    }
    
    private void eliminarArchivo(File archivoEntrega) {     //FALTA EL BOTON DE ESTO
        archivosEntregaSeleccionados.remove(archivoEntrega);
    }
    
    public void configurarBotonArchivo(File archivo) {
        Pane contenedorArchivo = new Pane();
        contenedorArchivo.setPrefSize(285, 20);
        contenedorArchivo.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15");
        ImageView imgIconoArchivo = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/archivo.png"));
        ImageView imgIconoEliminarArchivo = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/eliminar-archivo-adjunto.png"));
        contenedorArchivo.getChildren().add(imgIconoArchivo);
        imgIconoArchivo.setFitHeight(40);
        imgIconoArchivo.setFitWidth(40);
        imgIconoArchivo.setLayoutX(12);
        imgIconoArchivo.setLayoutY(12);
        Label lbNombreArchivo = new Label(archivo.getName());
        contenedorArchivo.getChildren().add(lbNombreArchivo);
        lbNombreArchivo.setLayoutX(75);
        lbNombreArchivo.setLayoutY(6);
        Label lbPesoArchivo = new Label(String.valueOf(archivo.length() / 1024) + "KB");
        contenedorArchivo.getChildren().add(lbPesoArchivo);
        lbPesoArchivo.setLayoutX(75);
        lbPesoArchivo.setLayoutY(40);
        contenedorArchivo.getChildren().add(imgIconoEliminarArchivo);
        imgIconoEliminarArchivo.setFitHeight(35);
        imgIconoEliminarArchivo.setFitWidth(35);
        imgIconoEliminarArchivo.setLayoutX(240);
        imgIconoEliminarArchivo.setLayoutY(12);
        contenedorArchivo.setOnMouseClicked((event) -> {
            eliminarArchivo(archivo);
            contenedorArchivo.setVisible(false);
            hbxContenedorArchivosAlumno.getChildren().remove(contenedorArchivo);
        });
        hbxContenedorArchivosAlumno.getChildren().add(contenedorArchivo);
    }

    @FXML
    private void clicEnviar(ActionEvent event) {
        enviarEntrega();
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cancelarRegistro = Utilidades.mostrarDialogoConfirmacion("Cancelar entrega de actividad",
                "¿Estás seguro de que deseas cancelar la entrega?");
        if (cancelarRegistro) {
            Stage escenerioBase = (Stage) lbNombreActividad.getScene().getWindow();
            escenerioBase.close();
        }
    }

    private void mostrarInformacionActividad(Actividad actividad) {
        if (actividad != null) {
            lbNombreActividad.setText(actividad.getNombreActividad());
            lbDetallesActividad.setText(actividad.getDetallesActividad());
        }
    }
    
    public void inicializarInformacionFormulario(boolean esEdicion, Entrega entregaEdicion) {
        this.esEdicion = esEdicion;
        this.entregaEdicion = entregaEdicion;
        if (esEdicion) {
            lbEntrega.setText("Modificar entrega");
            btnEnviar.setText("Guardar cambios");
            cargarInformacionEdicion();
        }
    }
    
    private void cargarInformacionEdicion() {
        taComentarios.setText(entregaEdicion.getComentarioAlumno());
        ArrayList<Archivo> archivos = obtenerArchivosDeEntrega();
        for (Archivo archivo : archivos) {
            archivo.getNombreArchivo();
            String rutaArchivo = archivo.getNombreArchivo();
            File archivoFile = new File(rutaArchivo);
            configurarBotonArchivo(archivoFile);
        }
    }
    
    private ArrayList<Archivo> obtenerArchivosDeEntrega() {
        ArrayList<Archivo> archivos = null;
        try {
            archivos = new ArchivoDAO().consultarArchivosPorEntrega(entregaEdicion.getIdEntrega());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return archivos;
    }
    
    private void validarCamposVacios() {
        if (!taComentarios.getText().isEmpty()) {
            btnEnviar.setDisable(false);
        }
        taComentarios.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    btnEnviar.setDisable(false);
                } else {
                    btnEnviar.setDisable(true);
                    lbCampoComentariosError.setText("Campo obligatorio");
                }
            }
        });
    }
    
    private void enviarEntrega() {
        int respuesta;
        Entrega entregaNueva = new Entrega();
        String comentariosEstudiante = taComentarios.getText();
        LocalDate fechaEntrega = LocalDate.now();
        LocalTime horaEntrega = LocalTime.now();
        entregaNueva.setComentarioAlumno(comentariosEstudiante);
        entregaNueva.setFechaEntrega(String.valueOf(fechaEntrega));
        entregaNueva.setHoraEntrega(String.valueOf(horaEntrega));
        entregaNueva.setIdActividad(actividad.getIdActividad());
        entregaNueva.setIdAcademico(1);         //CAMBIAR
        try {
            if (esEdicion) {
                respuesta = new EntregaDAO().actualizarEntrega(entregaNueva);
            } else {
                respuesta = new EntregaDAO().registrarEntrega(entregaNueva);
            }
            if (respuesta > 0) {
                enviarArchivosEntrega(respuesta);
            } else {
                Utilidades.mostrarDialogoSimple("Error al enviar la entrega", 
                        "No se pudo cargar el archivo seleccionado. Inténtelo de nuevo o hágalo más tarde", Alert.AlertType.ERROR);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void enviarArchivosEntrega(int idEntrega) {
        int respuesta;
        archivoEntrega = new Archivo();
        boolean seEnviaronTodos = true;
        try {
            for (File archivos : archivosEntregaSeleccionados) {
                archivoEntrega.setArchivo(Files.readAllBytes(archivos.toPath()));
                archivoEntrega.setEsEntrega(true);
                archivoEntrega.setIdEntrega(idEntrega);
                archivoEntrega.setNombreArchivo(archivos.getName());
                if (esEdicion) {
                    respuesta = new ArchivoDAO().actualizarArchivo(archivoEntrega);
                } else {
                    respuesta = new ArchivoDAO().guardarArchivo(archivoEntrega);
                }
                if (respuesta < 0) {
                    seEnviaronTodos = false;
                    System.out.println("No se pudo cargar el archivo: " + archivos.getName());
                }
            }
            
        } catch (DAOException ex) {
            manejarDAOException(ex);
        } catch (IOException ex) {
            System.out.println("Ocurrió un error al cargar los archivos.");
        }
        if (seEnviaronTodos) {
            Utilidades.mostrarDialogoSimple("Entrega enviada", "Entrega enviada exitosamente", Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarDialogoSimple("Error en la entrega", 
                    "No se pudo cargar el archivo seleccionado. Inténtelo de nuevo o hágalo más tarde", Alert.AlertType.ERROR);
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
