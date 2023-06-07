/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Controlador de la vista de entrega de actividad
 */

package javafxsastr.controladores;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.ArchivoDAO;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EntregaDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Archivo;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.modelos.pojo.Estudiante;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxsastr.modelos.dao.ActividadDAO;
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
    @FXML
    private Label lbCurso;
    @FXML
    private Label lbNrc;
    @FXML
    private Label lbDocente;
    @FXML
    private Label lbPeriodo;
    @FXML
    private Label lbAnteproyecto;
    @FXML
    private Label lbDirector;
    @FXML
    private Label lbCodirector;
    @FXML
    private Label lbActRevisadas;
    @FXML
    private Label lbActSinPendientes;
    @FXML
    private Label lbActPorVencer;
    @FXML
    private ImageView imgBtnDesplegar;
    @FXML
    private Pane paneLateralDer;
   

    private Actividad actividad;
    private Entrega entregaEdicion;
    private boolean esEdicion = true; // Este dato se setteará al llamar a esta controlador en
                                      // inicializarInformacionFormulario
    private File archivoEntregaSeleccion;
    private ArrayList<File> archivosEntregaSeleccionados = new ArrayList<>();
    private Archivo archivoEntrega;
    private boolean menuDatos;
    private int porVencer;
    private int realizadas;
    private int revisadas;
    private Curso curso;
    private Academico academico;
    private ObservableList<Academico> codirectores;
    private Estudiante estudiante;
    private Anteproyecto anteproyecto;
    private ArrayList<Archivo> archivosEdicion = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EstudianteDAO est = new EstudianteDAO();// METODO TEMPORALPAR ARECUPERAR UN ESTUDIANTE, LA VENTANA ANTERIOR ME
                                                // LO DEBE MANDAR
        Estudiante estudidi;
        try {
            entregaEdicion = new EntregaDAO().consultarEntregaUnicaEdicion(9);
            estudidi = est.obtenerEstudiante(1);
            this.estudiante = estudidi;
        } catch (DAOException ex) {
            Logger.getLogger(FXMLFormularioActividadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Instancia para probar si funciona al cargarInformacionEdicion, se hece lo
        // mismo que con "esEdicion"

        // entregaEdicion = new Entrega(2, "Hola maestro, esta es mi entrega.",
        // "2023-06-03", "07:59:00", null, null, null, 1, 1);
        btnEnviar.setDisable(true);
        obtenerDatosRelacionadoAlEstudiante();
        obtenerActividad(); // QUITAR ESTE MÉTODO
        mostrarInformacionActividad(actividad);
        inicializarInformacionFormulario(esEdicion, entregaEdicion);
        validarCamposVacios();
    }    

    private void obtenerActividad() { // QUITAR MÉTODO, ES PARA PROBAR LA FUNCIONALIDAD
        try { // PERO LA ACTIVIDAD DEBE SER PASADA
            Actividad ac = new ActividadDAO().obtenerActividad(7);
            actividad = ac;
            lbNombreActividad.setText(ac.getNombreActividad());
            lbDetallesActividad.setText(ac.getDetallesActividad());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }

    public void setIdActividad(Actividad actividad) {
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
        FileChooser.ExtensionFilter filtroSeleccion = new FileChooser.ExtensionFilter(
                "Cualquier .PDF, .ZIP, .TXT, .XLSX, .DOCX, .PPTX",
                "*.PDF", "*.ZIP", "*.TXT", "*.XLSX", "*.DOCX", "*.PPTX", "*.PNG", "*.JPG");
        ventanaSeleccionArchivo.getExtensionFilters().add(filtroSeleccion);

        Stage escenarioBase = (Stage) lbEntrega.getScene().getWindow();
        archivoEntregaSeleccion = ventanaSeleccionArchivo.showOpenDialog(escenarioBase);

        visualizarArchivo(archivoEntregaSeleccion);
    }

    private void visualizarArchivo(File archivoEntrega) {
        if (archivoEntrega != null) {
            agregarArchivo(archivoEntrega);
            archivoEntrega.getName(); // SETTEARLO A LA TARJETA
            System.out.println(archivoEntrega.getName());
            // hbxEvidencias TarhetaEvidencias
        }
    }

    private void agregarArchivo(File archivoEntrega) {
        archivosEntregaSeleccionados.add(archivoEntrega);
        configurarBotonArchivo(archivoEntrega);
    }

    private void eliminarArchivo(File archivoEntrega) { // FALTA EL BOTON DE ESTO
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
        System.out.println("Archivos");
        for (File a : archivosEntregaSeleccionados) {
            System.out.println("ARCHIVOS:" + a.getName());
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cancelarRegistro = Utilidades.mostrarDialogoConfirmacion("Cancelar entrega de actividad",
                "¿Estás seguro de que deseas cancelar la entrega?");
        if (cancelarRegistro) {
            cerrarVenatan();
        }
    }

    private void mostrarInformacionActividad(Actividad actividad) {
        lbNombreActividad.setText(actividad.getNombreActividad());
        lbDetallesActividad.setText(actividad.getDetallesActividad());
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
        archivosEdicion.addAll(archivos);
        for (Archivo archivo : archivos) {
            archivo.getNombreArchivo();
            String rutaArchivo = archivo.getNombreArchivo();
            File archivoFile = new File(rutaArchivo);
            visualizarArchivo(archivoFile);
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
        taComentarios.textProperty().addListener(new ChangeListener<String>() {
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
        entregaNueva.setIdAcademico(1); // CAMBIAR
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
                for (File archivos : archivosEntregaSeleccionados) {
                    archivoEntrega.setEsEntrega(true);
                    archivoEntrega.setIdEntrega(idEntrega);
                    archivoEntrega.setNombreArchivo(archivos.getName());
                    if (esEdicion && archivosEdicion.size() > contador) {
                        archivoEntrega.setArchivo(archivosEdicion.get(contador).getArchivo());
                        archivoEntrega.setIdArchivo(archivosEdicion.get(contador).getIdArchivo());
                        respuesta = new ArchivoDAO().actualizarArchivo(archivoEntrega);
                    } else {
                        archivoEntrega.setArchivo(Files.readAllBytes(archivos.toPath()));
                        archivosEdicion = new ArrayList<>();
                        respuesta = new ArchivoDAO().guardarArchivo(archivoEntrega);
                    }
                    contador++;
                    if (respuesta < 0) {
                        seEnviaronTodos = false;
                        System.out.println("No se pudo cargar el archivo: " + archivos.getName());
                    }
                }
                if (archivosEdicion.size() > contador) {
                    eliminaArchivosBDsinUsar(contador);
                }
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Ocurrió un error al cargar los archivos.");
        }
        if (seEnviaronTodos) {
            Utilidades.mostrarDialogoSimple("Entrega enviada", "Entrega enviada exitosamente",
                    Alert.AlertType.INFORMATION);
            cerrarVenatan();
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

    private void actualizaEstadoMenu(int posicion, boolean abierto, String icono) {
        animacionMenu(posicion);
        menuDatos = abierto;
        imgBtnDesplegar.setImage(new Image(JavaFXSASTR.class.getResource(icono).toString()));
    }

    private void obtenerDatosRelacionadoAlEstudiante() {
        AnteproyectoDAO anteproyectoDao = new AnteproyectoDAO();
        CursoDAO cursoDao = new CursoDAO();
        AcademicoDAO academicoDao = new AcademicoDAO();
        ActividadDAO actividadesDao = new ActividadDAO();
        try {
            curso = cursoDao.ordenarCursosPorEstudiante(estudiante.getIdEstudiante());
            anteproyecto = anteproyectoDao.obtenerAnteproyectosPorEstudiante(estudiante.getIdEstudiante());
            academico = academicoDao.obtenerAcademicoPorId(curso.getIdAcademico());
            codirectores = FXCollections.observableArrayList(
                    new AcademicoDAO().obtenerCodirectoresProAnteproyecto(anteproyecto.getIdAnteproyecto()));
            int totalActividades = actividadesDao.obtenerNumeroActividadesPorEstudiante(estudiante.getIdEstudiante());
            porVencer = actividadesDao.totalActividades(1, estudiante.getIdEstudiante());
            realizadas = actividadesDao.totalActividades(4, estudiante.getIdEstudiante());
            revisadas = actividadesDao.totalActividades(3, estudiante.getIdEstudiante());
        } catch (DAOException ex) {
            ex.printStackTrace();
            Logger.getLogger(FXMLFormularioActividadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setInformacion();
    }

    private void setInformacion() {
        if (curso != null) {
            lbCurso.setText(curso.getNombreCurso());
            lbNrc.setText(curso.getNrcCurso());
            lbPeriodo.setText(curso.getFechaInicioCurso() + "-" + curso.getFinPeriodoEscolar());
        }
        if (academico != null) {
            lbDocente.setText(academico.getNombre() + " " + academico.getPrimerApellido());
        }
        if (anteproyecto != null) {
            lbAnteproyecto.setText(anteproyecto.getNombreTrabajoRecepcional());
            lbDirector.setText(anteproyecto.getNombreDirector());
        }
        String codirectoresNombre = "";
        if (!codirectores.isEmpty()) {
            for (int i = 0; i < codirectores.size(); i++) {
                codirectoresNombre = codirectoresNombre + codirectores.get(i).getNombre() + " "
                        + codirectores.get(i).getPrimerApellido() + "\n";
            }
        }
        lbCodirector.setText(codirectoresNombre);
        lbActSinPendientes.setText(porVencer + " Actividades sin realizar");
        lbActRevisadas.setText(realizadas + " Actividades realizadas");
        lbActPorVencer.setText(revisadas + " Activdades revisadas");
    }

    private void animacionMenu(int posicion) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(paneLateralDer);
        translate.setDuration(Duration.millis(300));
        translate.setByX(posicion);
        translate.setAutoReverse(true);
        translate.play();
    }

    private void cerrarVenatan() {
        Stage escenerioBase = (Stage) lbNombreActividad.getScene().getWindow();
        escenerioBase.close();
    }

    @FXML
    private void clicEscoderPanleIzquierdo(MouseEvent event) {
         if(menuDatos) {
            actualizaEstadoMenu(-433, false, "recursos/hide.jpg");            
        }
        else{
            actualizaEstadoMenu(433, true, "recursos/show.jpg");           
        }
    }

}
