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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Estudiante;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxsastr.modelos.dao.ArchivoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EntregaDAO;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Archivo;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;

public class FXMLFormularioEntregaActividadController implements Initializable {

    @FXML
    private Label lbNombreActividad;
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
    @FXML
    private Label lbErrorArchivos;
    @FXML
    private TextArea taDetallesActividad;
   
    private Actividad actividad;
    private Entrega entregaEdicion;
    private boolean esEdicion ; 
    private int numeroEntrega;
    private Archivo archivoEntregaSeleccion;
    private ArrayList<Archivo> archivosEntregaSeleccionados = new ArrayList<>();
    private Archivo archivoEntrega;
    private Usuario usuario;
    private Academico academico;
    private Estudiante estudiante;
    private Anteproyecto anteproyecto;
    private ArrayList<Archivo> archivosEdicion = new ArrayList<>();    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEnviar.setDisable(true);
        mostrarInformacionActividad(actividad);
        validarCamposVacios();
    }    
    
    public void setEstudiante(Estudiante estudiante) {   
       this.estudiante = estudiante;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
        btnEnviar.setDisable(true);
        mostrarInformacionActividad(actividad);
        validarCamposVacios();
    }
    
    public void setNumeroEntrega(int numeroEntrega) {
        this.numeroEntrega = numeroEntrega;
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

    public void configurarBotonArchivo(Archivo archivo) {
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
            eliminarArchivo(archivo);
            contenedorArchivo.setVisible(false);
            hbxContenedorArchivosAlumno.getChildren().remove(contenedorArchivo);
        });
        hbxContenedorArchivosAlumno.getChildren().add(contenedorArchivo);
    }
    
    private void visualizarArchivo(Archivo archivoEntrega) {
        if (archivoEntrega != null) {
            agregarArchivo(archivoEntrega);
        }
    }

    private void agregarArchivo(Archivo archivoEntrega) {
        archivosEntregaSeleccionados.add(archivoEntrega);
        configurarBotonArchivo(archivoEntrega);
    }

    private void eliminarArchivo(Archivo archivoEntrega) {
        archivosEntregaSeleccionados.remove(archivoEntrega);
    }

    private void mostrarInformacionActividad(Actividad actividad) {
        if (actividad != null) {
            lbNombreActividad.setText(actividad.getNombreActividad());
            taDetallesActividad.setText(actividad.getDetallesActividad());
        }
    }
    
    private void cargarInformacionEdicion() {
        taComentarios.setText(entregaEdicion.getComentarioAlumno());
        ArrayList<Archivo> archivos = obtenerArchivosDeEntrega();
        archivosEdicion.addAll(archivos);
        for (Archivo archivo : archivos) {
            visualizarArchivo(archivo);
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
        if (!taComentarios.getText().trim().isEmpty()) {
            btnEnviar.setDisable(false);
        }
        taComentarios.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.trim().isEmpty()) {
                    lbCampoComentariosError.setText("");
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
        obtenerAcademicoDelEstudiante();
        Entrega entregaNueva = new Entrega();
        String comentariosEstudiante = taComentarios.getText();
        LocalDate fechaEntrega = LocalDate.now();
        LocalTime horaEntrega = LocalTime.now();
        entregaNueva.setComentarioAlumno(comentariosEstudiante);
        entregaNueva.setFechaEntrega(String.valueOf(fechaEntrega));
        entregaNueva.setHoraEntrega(String.valueOf(horaEntrega));
        entregaNueva.setIdActividad(actividad.getIdActividad());
        entregaNueva.setIdAcademico(academico.getIdAcademico());
        try {
            if (esEdicion) {
                entregaNueva.setIdEntrega(entregaEdicion.getIdEntrega());
                respuesta = new EntregaDAO().actualizarEntrega(entregaNueva);
            } else {
                respuesta = new EntregaDAO().registrarEntrega(entregaNueva);
            }
            if (respuesta > 0) {
                enviarArchivosEntrega(respuesta);
            } else {
                Utilidades.mostrarDialogoSimple("Error al enviar la entrega",
                        "No se pudo cargar el archivo seleccionado. Inténtelo de nuevo o hágalo más tarde",
                        Alert.AlertType.ERROR);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void obtenerAcademicoDelEstudiante() {
        try { 
            int idAcademico = new AnteproyectoDAO().obtenerAnteproyectosPorEstudiante(estudiante.getIdEstudiante()).getIdAcademico();
            academico = new AcademicoDAO().obtenerAcademicoPorId(idAcademico);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private boolean validarLongitudCampos() {
        boolean esTamañoValido = true;
        lbErrorArchivos.setText("");
        double totalPesoArchivos = 0.0;
        String textoComentarios = taComentarios.getText();
        if (textoComentarios.length() > 1000) {
            esTamañoValido = false;
            lbCampoComentariosError.setText("No puede tener más de 1000 caracteres");
        }
        for (Archivo archivo : archivosEntregaSeleccionados) {
            totalPesoArchivos = totalPesoArchivos + (archivo.getArchivo().length / 1024);
        }
        if (totalPesoArchivos > 16000) {
            esTamañoValido = false;
            lbErrorArchivos.setText("El límite de peso de los archivos debe ser menor a 16 MB.");
        }
        if (actividad == null) {
            esTamañoValido = false;
            System.err.println("La actividad pasada es NULA, no se puede enviar una entrega");
        }
        return esTamañoValido;
    }

    private void enviarArchivosEntrega(int idEntrega) {
        int respuesta;
        archivoEntrega = new Archivo();
        boolean seEnviaronTodos = true;
        try {
            if (archivosEntregaSeleccionados.isEmpty()) {
                for (int i = 0; i < archivosEdicion.size(); i++) {
                    new ArchivoDAO().borrarArchivosRevision(archivosEdicion.get(i).getIdArchivo());
                }
            } else {
                int contador = 0;
                for (Archivo archivos : archivosEntregaSeleccionados) {
                    archivoEntrega.setEsEntrega(true);
                    archivoEntrega.setIdEntrega(idEntrega);
                    archivoEntrega.setNombreArchivo(archivos.getNombreArchivo());
                    if (esEdicion && archivosEdicion.size() > contador) {
                        archivoEntrega.setArchivo(archivosEdicion.get(contador).getArchivo());
                        archivoEntrega.setIdArchivo(archivosEdicion.get(contador).getIdArchivo());
                        respuesta = new ArchivoDAO().actualizarArchivo(archivoEntrega);
                    } else {
                        archivoEntrega.setArchivo(archivos.getArchivo());
                        archivosEdicion = new ArrayList<>();
                        respuesta = new ArchivoDAO().guardarArchivo(archivoEntrega);
                    }
                    contador++;
                    if (respuesta < 0) {
                        seEnviaronTodos = false;
                        System.out.println("No se pudo cargar el archivo: " + archivos.getNombreArchivo());
                    }
                }
                if (archivosEdicion.size() > contador) {
                    eliminaArchivosBDsinUsar(contador);
                }
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        if (seEnviaronTodos) {
            Utilidades.mostrarDialogoSimple("Entrega enviada", "Entrega enviada exitosamente",
                    Alert.AlertType.INFORMATION);
            if (esEdicion) {
                irAVistaDetallesEntrega();
            } else {
                irAVistaDetallesActividad();
            }
        } else {
            Utilidades.mostrarDialogoSimple("Error en la entrega",
                    "No se pudo cargar el archivo seleccionado. Inténtelo de nuevo o hágalo más tarde",
                    Alert.AlertType.ERROR);
        }
    }

    private void eliminaArchivosBDsinUsar(int contador) {
        for (int i = contador; i < archivosEdicion.size(); i++) {
            try {
                new ArchivoDAO().borrarArchivosRevision(archivosEdicion.get(i).getIdArchivo());
            } catch (DAOException ex) {
                Utilidades.mostrarDialogoSimple("Error en al actuaizar los archivos",
                        "No se pudo actualizar los archivos de la entrega", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        if (esEdicion) {
            irAVistaDetallesEntrega();
        } else {
            irAVistaDetallesActividad();
        }
    }

    @FXML
    private void clicAdjuntarArchivo(ActionEvent event) {
        File archivo;
        FileChooser ventanaSeleccionArchivo = new FileChooser();
        ventanaSeleccionArchivo.setTitle("Selecciona el archivo a enviar");
        FileChooser.ExtensionFilter filtroSeleccion = new FileChooser.ExtensionFilter(
                "Cualquier .PDF, .ZIP, .TXT, .XLSX, .DOCX, .PPTX",
                "*.PDF", "*.ZIP", "*.TXT", "*.XLSX", "*.DOCX", "*.PPTX", "*.PNG", "*.JPG");
        ventanaSeleccionArchivo.getExtensionFilters().add(filtroSeleccion);
        Stage escenarioBase = (Stage) lbEntrega.getScene().getWindow();
        archivo = ventanaSeleccionArchivo.showOpenDialog(escenarioBase);
        if (archivo != null) {
            try {
                archivoEntregaSeleccion = new Archivo();
                archivoEntregaSeleccion.setArchivo(Files.readAllBytes(archivo.toPath()));
                archivoEntregaSeleccion.setNombreArchivo(archivo.getName());
                visualizarArchivo(archivoEntregaSeleccion);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
  
    }
    
    @FXML
    private void clicEnviar(ActionEvent event) {
        if (validarLongitudCampos()) {
            enviarEntrega();
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cancelarRegistro = Utilidades.mostrarDialogoConfirmacion("Cancelar entrega de actividad",
                "¿Estás seguro de que deseas cancelar la entrega?");
        if (cancelarRegistro) {
            if (esEdicion) {
                irAVistaDetallesEntrega();
            } else {
                irAVistaDetallesActividad();
            }
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
            escenario.setTitle("Detalles de Actividad");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void irAVistaDetallesEntrega() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesEntrega.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesEntregaController controladorVistaDetallesEntrega = accesoControlador.getController();     
            controladorVistaDetallesEntrega.setEstudiante(estudiante);
            controladorVistaDetallesEntrega.setActividad(actividad);
            controladorVistaDetallesEntrega.setEntrega(entregaEdicion, numeroEntrega);
            Stage escenario = (Stage) lbEntrega.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Detalles de Entrega");
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
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.",
                        Alert.AlertType.ERROR);
                break;
            default:
                throw new AssertionError();
        }
    }

}