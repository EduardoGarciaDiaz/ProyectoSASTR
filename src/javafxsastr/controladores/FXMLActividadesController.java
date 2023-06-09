/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 04/06/2023
 * Descripción: Recupera y muestra las actividades del estudiante.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaActividadGestion;

public class FXMLActividadesController implements Initializable {

    @FXML
    private VBox contenedorTarjetasActividades;
    @FXML
    private Label lbTituloVentana;
    @FXML
    private Pane panelLateral;
    @FXML
    private Label lbActRevisadas;
    @FXML
    private Label lbActSinPendientes;
    @FXML
    private Label lbActPorVencer;
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
    private ImageView imgBtnHide;
    @FXML
    private ImageView imgBtnShow;
   
    TranslateTransition panelDesplegado;
    TranslateTransition panelCerrado;
    private Curso curso;
    private Anteproyecto anteproyecto;
    private Academico academico;
    private Estudiante estudiante;
    private ObservableList<Actividad> actividades = FXCollections.observableArrayList();
    private ObservableList<Academico> codirectores;
    private int actividadesPorVencer;
    private int actividadesRealizadas;
    private int actividadesNoRealizadas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panelLateral.setTranslateX(0);
        prepararAnimacionBtnCerrarSesion();
    }    
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        obtenerActividadesDelEstudiante();
        cargarTarjetasActividades();
        obtenerDatosRelacionadoAlEstudiante();
        setInformacion();
    }
    
    public void obtenerActividadesDelEstudiante() {
        try {
            ObservableList<Actividad> actividadesRecuperadas = FXCollections.observableArrayList(
                new ActividadDAO().obtenerActividadesPorEstudiante(estudiante.getIdEstudiante(), estudiante.getIdAnteproyecto())
            );
            for (Actividad actividadRecuperada : actividadesRecuperadas) {
                verificarSiEsNoCompletada(actividadRecuperada);
                actividades.add(actividadRecuperada);
            }
            FXCollections.sort(actividades, Comparator.comparing(Actividad::getEstadoActividad));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }

    private void cargarTarjetasActividades() {
        for (Actividad actividad : actividades) {
            TarjetaActividadGestion tarjeta = new TarjetaActividadGestion(actividad);
            tarjeta.getBotonVerDetalles().setOnAction(
                (event) -> {
                    irAVerDetallesActividad(estudiante, actividad);
                }
            );
            tarjeta.getBotonModificar().setOnMouseClicked(
                (event) -> {
                    irAModificarActividad(estudiante, actividad, true);
                }
            );
            tarjeta.getCheckBox().setOnAction(
                (event) -> {
                    if (actividad.getEstadoActividad().equals("Proxima")) {
                        boolean marcarComoCompletada = Utilidades.mostrarDialogoConfirmacion("Marcar actividad como completada",
                                    "¿Estás seguro de que deseas marcar la actividad como completada?");
                        if (marcarComoCompletada) {
                            try {
                                actividad.setIdEstadoActividad(2);
                                new ActividadDAO().actualizarActividad(actividad);
                                obtenerDatosRelacionadoAlEstudiante();
                            } catch (DAOException ex) {
                                manejarDAOException(ex);
                            }
                            contenedorTarjetasActividades.getChildren().clear();
                            actividades.clear();
                            obtenerActividadesDelEstudiante();
                            cargarTarjetasActividades();
                        } else {
                            tarjeta.getCheckBox().selectedProperty().setValue(false);
                        }
                    }
                }
            );
            contenedorTarjetasActividades.getChildren().add(tarjeta);
        }
    }
    
    private void verificarSiEsNoCompletada(Actividad actividad) {
        LocalDate fechaFin = LocalDate.parse(actividad.getFechaFinActividad());
        if ((actividad.getEstadoActividad().equals("Proxima")) && (fechaFin.isBefore(LocalDate.now()))) {
            actividad.setEstadoActividad("No completada");
            actividad.setIdEstadoActividad(3);
            try {
                new ActividadDAO().actualizarActividad(actividad);
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
    }
    
    private void obtenerDatosRelacionadoAlEstudiante() {
        AnteproyectoDAO anteproyectoDAO = new AnteproyectoDAO();
        CursoDAO cursoDao = new CursoDAO();        
        AcademicoDAO academicoDao = new AcademicoDAO(); 
        ActividadDAO actividadesDAO = new ActividadDAO();
        try {
            curso = cursoDao.obtenerCursosPorEstudiante(estudiante.getIdEstudiante());
            if (curso != null) {
                academico = academicoDao.obtenerAcademicoPorId(curso.getIdAcademico());
            }
            anteproyecto = anteproyectoDAO.obtenerAnteproyectosPorEstudiante(estudiante.getIdEstudiante());
            int totalActividades = actividadesDAO
                    .obtenerNumeroActividadesPorEstudiante(estudiante.getIdEstudiante(), estudiante.getIdAnteproyecto());
            actividadesRealizadas = actividadesDAO
                    .obtenerNumeroActividadesCompletadas(estudiante.getIdEstudiante(),estudiante.getIdAnteproyecto());
            actividadesNoRealizadas = actividadesDAO
                    .obtenerNumeroActividadesNoCompletadas(estudiante.getIdEstudiante(),estudiante.getIdAnteproyecto());
            actividadesPorVencer = totalActividades - actividadesNoRealizadas - actividadesRealizadas;
        } catch (DAOException ex) {
            manejarDAOException(ex);
        } 
        setInformacion();
    }
    
    private void setInformacion() {
        if (curso != null) {
            lbCurso.setText(curso.getNombreCurso());
            lbNrc.setText(curso.getNrcCurso());
            lbPeriodo.setText(
                    Utilidades.formatearFechaPeriodo(curso.getFechaInicioCurso())
                    + " - "
                    + Utilidades.formatearFechaPeriodo(curso.getFinPeriodoEscolar())
            );
        }
        if (academico != null) {
            lbDocente.setText(academico.getNombre() + " " + academico.getPrimerApellido());
        }
        if (anteproyecto != null) {
            lbAnteproyecto.setText(anteproyecto.getNombreTrabajoRecepcional());
            lbDirector.setText(anteproyecto.getNombreDirector());
        }
        lbActSinPendientes.setText(actividadesPorVencer + " Actividades pendientes");
        lbActRevisadas.setText(actividadesRealizadas + " Actividades realizadas");
        lbActPorVencer.setText(actividadesNoRealizadas + " Actividades sin completar");
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaInicio(estudiante);
    }

    @FXML
    private void clicCrearActividad(MouseEvent event) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioActividadController controladorVistaFormularioActividad = accesoControlador.getController();           
            controladorVistaFormularioActividad.iniciarFormularioNUevo(estudiante, false, null);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Crear Actividad");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void clicAnimarPanel(MouseEvent event) {
        if (panelLateral.getTranslateX() != 0) {
            panelDesplegado.play();
            imgBtnHide.setVisible(true);
            imgBtnShow.setVisible(false);
        } else {
            panelCerrado.setToX(433);
            panelCerrado.play();
            imgBtnHide.setVisible(false);
            imgBtnShow.setVisible(true);
        }
    }
    
    private void irAVistaInicio(Estudiante estudiante) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicio.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInicioController controladorVistaInicio = accesoControlador.getController();
            controladorVistaInicio.setUsuario(estudiante);
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
            FXMLFormularioActividadController controladorVistaFormularioActividad = accesoControlador.getController(); 
            controladorVistaFormularioActividad.iniciarFormularioNUevo(estudiante, esModificacion, actividad);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Modificar Actividad");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVerDetallesActividad(Estudiante estudiante, Actividad actividad) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesActividad.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesActividadController controladorVistaDetallesActividad = accesoControlador.getController(); 
            controladorVistaDetallesActividad.setEstudiante(estudiante);
            controladorVistaDetallesActividad.setActividad(actividad);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Detalles de actividad");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void prepararAnimacionBtnCerrarSesion() {
        panelDesplegado = new TranslateTransition(new Duration(350.0), panelLateral);
        panelDesplegado.setToX(0);
        panelCerrado = new TranslateTransition(new Duration(350.0), panelLateral);
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