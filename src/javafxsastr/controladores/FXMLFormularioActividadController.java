/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 02/06/2023
 * Descripción: Controller de la ventana FormularioActividad
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.HistorialCambiosDAO;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.HistorialCambios;
import javafxsastr.utils.FiltrosTexto;
import javafxsastr.utils.Utilidades;


public class FXMLFormularioActividadController implements Initializable {

    @FXML
    private TextField txfNombreActividad;
    @FXML
    private TextArea txaDetallesActividad;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lbNombreLgac;
    @FXML
    private Label lbDescirpcionLgac;
    @FXML
    private DatePicker dtpInicio;
    @FXML
    private DatePicker dtpFin;
    @FXML
    private TextField txfHoraInicio;
    @FXML
    private TextField txfHoraFin;
    @FXML
    private Label lbTtituloVentana;

    private Estudiante estudiante;
    private Anteproyecto anteproyecto;
    private boolean isEdicion = false;
    private Actividad actividadEdicion;
    private final int ESTADO_PROXIMA = 1; 
    private final int LIMIT_CARAC_NOMBRE = 200;
    private final int LIMIT_CARAC_DETALLES = 1000;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
    } 
    
    public void iniciarFormularioNUevo(Estudiante estudianteN, boolean edicion, Actividad act ) {        
        isEdicion = edicion;
        this.estudiante = estudianteN;
        if (isEdicion) {
            lbTtituloVentana.setText("Modificar actividad");
            actividadEdicion = act;           
            cargarInformacion();
        }
        btnGuardar.setDisable(false);
        obtenerAnteproyecto();        
        inicializarListeners();  
        iniciarFiltros(); 
    }
    
    private void obtenerAnteproyecto() {
        try {
            this.anteproyecto = new AnteproyectoDAO().obtenerAnteproyectosPorEstudiante(estudiante.getIdEstudiante());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
     private void cargarInformacion() {     
        Actividad actividad = actividadEdicion;
        txfNombreActividad.setText(actividad.getNombreActividad());
        txaDetallesActividad.setText(actividad.getDetallesActividad());
        txfHoraFin.setText(actividad.getHoraFinActividad().substring(0,5));
        txfHoraInicio.setText(actividad.getHoraInicioActividad().substring(0,5));
        LocalDate fechaInicio = LocalDate.parse(actividad.getFechaInicioActividad());
        dtpInicio.setValue(fechaInicio);
        LocalDate fechaFin = LocalDate.parse(actividad.getFechaFinActividad());
        dtpFin.setValue(fechaFin);  
        btnGuardar.setDisable(false);
    }
    
    private void inicializarListeners() {
        txfNombreActividad.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (txfNombreActividad.getText().length() <= LIMIT_CARAC_NOMBRE) {
                    validarBtnGuardar();
                    lbNombreLgac.setText("");
                } else { 
                    mostraMensajelimiteSuperado(LIMIT_CARAC_NOMBRE,"Nombre Actividad",lbNombreLgac);
                }            
            }
        });        
        txaDetallesActividad.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (txaDetallesActividad.getText().length() <= LIMIT_CARAC_DETALLES) {
                    validarBtnGuardar();
                    lbDescirpcionLgac.setText("");
                } else { 
                    mostraMensajelimiteSuperado(LIMIT_CARAC_DETALLES,"Detalles Actividad",lbDescirpcionLgac);
                }  
            }
        });
        txfHoraInicio.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarHora(txfHoraInicio);
            }
        });
        txfHoraFin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarHora(txfHoraFin);
            }
        });
        dtpInicio.onMouseEnteredProperty().addListener(new ChangeListener<EventHandler<? super MouseEvent>>(){
            @Override
            public void changed(ObservableValue<? extends EventHandler<? super MouseEvent>> observable,
                    EventHandler<? super MouseEvent> oldValue, EventHandler<? super MouseEvent> newValue) {
                validarBtnGuardar();
            }
        });
        dtpFin.onMouseEnteredProperty().addListener(new ChangeListener<EventHandler<? super MouseEvent>>(){
            @Override
            public void changed(ObservableValue<? extends EventHandler<? super MouseEvent>> observable,
                    EventHandler<? super MouseEvent> oldValue, EventHandler<? super MouseEvent> newValue) {
                validarBtnGuardar();
            }
        });        
    }
    
    private void iniciarFiltros() {
        FiltrosTexto.filtroLetrasNumerosPuntosComasSignosComunes(txfNombreActividad);
        FiltrosTexto.filtroLetrasNumerosPuntosComasSignosComunes(txaDetallesActividad);
    }
    
    private void validarBtnGuardar() {
        if (txfNombreActividad.getText().trim().length() < 5 || txaDetallesActividad.getText().trim().length() < 10 || 
                txfNombreActividad.getText().trim().length() > LIMIT_CARAC_NOMBRE || 
                txaDetallesActividad.getText().trim().length() > LIMIT_CARAC_DETALLES
                || dtpInicio.getValue() == null || dtpFin.getValue() == null ||
                txfHoraInicio.getText().isEmpty() || txfHoraFin.getText().isEmpty()) {
            btnGuardar.setDisable(true);
        } else {
            btnGuardar.setDisable(false);
        }
    }
    
   public void validarFechas() { 
        LocalDate fechaInicioActividad = dtpInicio.getValue();
        LocalDate fechaFinActividad = dtpFin.getValue();           
        if (fechaInicioActividad.isAfter(fechaFinActividad)) {
               Utilidades.mostrarDialogoSimple("Error","Fechas Invalidas", Alert.AlertType.ERROR);
            } else {
               validarHora(fechaInicioActividad, fechaFinActividad);
            }        
    }
   
    private void validarHora(TextField textHora) {
        if (Pattern.matches("^([01]?\\d|2[0-3]):[0-5]\\d$", textHora.getText())){
            validarBtnGuardar();
            textHora.setStyle("-fx-border-width: 2;");
        } else {
            btnGuardar.setDisable(true);
            textHora.setStyle("-fx-border-color:RED; -fx-border-width: 2;");
        }
            
    }
    
    private void validarHora(LocalDate fechaInicioActividad, LocalDate fechaFinActividad ) {
        LocalTime horaInico = LocalTime.of((Integer.parseInt(txfHoraInicio.getText().substring(0,2))),
                                       Integer.parseInt(txfHoraInicio.getText().substring(3,5)));
        LocalTime horaFin = LocalTime.of((Integer.parseInt(txfHoraFin.getText().substring(0,2))),
                                       Integer.parseInt(txfHoraFin.getText().substring(3,5))); 
        if (fechaInicioActividad.isEqual(fechaFinActividad)){
            if (!horaFin.isBefore(horaInico)) {
                Utilidades.mostrarDialogoSimple("Error","Horas Invalidas", Alert.AlertType.ERROR);
                txfHoraInicio.setStyle("-fx-border-color:RED; -fx-border-width: 2;");
                txfHoraFin.setStyle("-fx-border-color:RED; -fx-border-width: 2;");
            } else {
                aceptarActividad();
            }          
        } else {
            aceptarActividad();
        }
    }
        
    private void aceptarActividad() {
       txfHoraFin.setStyle("-fx-border-width: 2;");
       txfHoraInicio.setStyle("-fx-border-width: 2;");
        if (isEdicion) {
            actualizarActividad();
        } else {
            registrarActividad(); 
        }
    }
    
    private void registrarActividad() {
        Actividad actividadNueva = new Actividad();
        actividadNueva.setNombreActividad(txfNombreActividad.getText());
        actividadNueva.setDetallesActividad(txaDetallesActividad.getText());
        actividadNueva.setFechaInicioActividad(dtpInicio.getValue().toString());
        actividadNueva.setFechaFinActividad(dtpFin.getValue().toString());
        actividadNueva.setHoraInicioActividad(txfHoraInicio.getText()+":00");
        actividadNueva.setHoraFinActividad(txfHoraFin.getText()+":00");
        actividadNueva.setFechaCreaciónActividad(LocalDate.now().toString());
        actividadNueva.setIdAnteproyecto(anteproyecto.getIdAnteproyecto());
        actividadNueva.setIdEstudiante(estudiante.getIdEstudiante());
        actividadNueva.setEstadoActividad("Proxima");
        actividadNueva.setIdEstadoActividad(ESTADO_PROXIMA);
        try {
            int exito = new ActividadDAO().guardarActividad(actividadNueva);
            Utilidades.mostrarDialogoSimple("Registro Exitoso","Actividad agregada con exito", 
                    Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (DAOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarDialogoSimple("Error", "Hubo un error al registrar la actividad", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void actualizarActividad() {
        Actividad actividadEditada = new Actividad();
        actividadEditada.setIdActividad(actividadEdicion.getIdActividad());
        actividadEditada.setNombreActividad(txfNombreActividad.getText());
        actividadEditada.setDetallesActividad(txaDetallesActividad.getText());
        actividadEditada.setFechaInicioActividad(dtpInicio.getValue().toString());
        actividadEditada.setFechaFinActividad(dtpFin.getValue().toString());
        actividadEditada.setHoraInicioActividad(txfHoraInicio.getText()+":00");
        actividadEditada.setHoraFinActividad(txfHoraFin.getText()+":00");  
        actividadEditada.setIdEstadoActividad(1);
        try {
            int exito = new ActividadDAO().actualizarActividad(actividadEditada);
            if (exito != -1) {
                HistorialCambiosDAO hisCamDao = new HistorialCambiosDAO();                
                if (!actividadEditada.getFechaInicioActividad().equals(actividadEdicion.getFechaInicioActividad())) {
                    HistorialCambios cambioFecha = new HistorialCambios();
                    cambioFecha.setIdActividad(actividadEdicion.getIdActividad());
                    String fechaAnterior = actividadEdicion.getFechaInicioActividad();
                    String fechaNueva = actividadEditada.getFechaInicioActividad();
                    String fechaModiciacion = LocalDate.now().toString();
                    String horaAnterior = actividadEdicion.getHoraInicioActividad();   
                    String horaNueva = actividadEditada.getHoraInicioActividad();
                    cambioFecha.setFechaAnterior(fechaAnterior);
                    cambioFecha.setFechaNueva(fechaNueva);
                    cambioFecha.setFechaDeModificacion(fechaModiciacion);
                    cambioFecha.setHoraAnterior(horaAnterior);
                    cambioFecha.setHoraNueva(horaNueva);
                    hisCamDao.guardarHistorialCambios(cambioFecha);  
                }
                if (!actividadEditada.getFechaFinActividad().equals(actividadEdicion.getFechaFinActividad())) {
                    HistorialCambios cambioFecha = new HistorialCambios();
                    cambioFecha.setIdActividad(actividadEdicion.getIdActividad());
                    String fechaAnterior = actividadEdicion.getFechaFinActividad();
                    String fechaNueva = actividadEditada.getFechaFinActividad();
                    String fechaModiciacion = LocalDate.now().toString();            
                    String horaAnterior = actividadEdicion.getHoraFinActividad();                   
                    String horaNueva = actividadEditada.getHoraFinActividad();           
                    cambioFecha.setFechaAnterior(fechaAnterior);
                    cambioFecha.setFechaNueva(fechaNueva);
                    cambioFecha.setFechaDeModificacion( fechaModiciacion);
                    cambioFecha.setHoraAnterior(horaAnterior);
                    cambioFecha.setHoraNueva(horaNueva);
                    hisCamDao.guardarHistorialCambios(cambioFecha);           
                }                    
            }
            Utilidades.mostrarDialogoSimple("Actualizacion Exitoso","Actividad actualizada con exito", 
                    Alert.AlertType.INFORMATION);
            irAVistaActividades(estudiante);        
        } catch (DAOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarDialogoSimple("Error", "Hubo un error al modificar la actividad", 
                    Alert.AlertType.ERROR);
        }      
    }
    
    private void limpiarCampos() {
        txfNombreActividad.setText("");
        txaDetallesActividad.setText("");
        txfHoraInicio.setText("");
        txfHoraFin.setText("");
        dtpInicio.setValue(null);
        dtpFin.setValue(null);
    }
    
     private void mostraMensajelimiteSuperado(int limiteCaracteres, String campo,  Label etiquetaError) { 
        etiquetaError.setText("Cuidado, Excediste el limite de caracteres("+limiteCaracteres+") de este campo " + campo);
        btnGuardar.setDisable(true);
    }    
     
    private void irAVistaActividades(Estudiante estudiante) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLActividades.fxml"));
            Parent vista = accesoControlador.load();
            FXMLActividadesController controladorVistaActividades = accesoControlador.getController();
            controladorVistaActividades.setEstudiante(estudiante);
            Stage escenario = (Stage) lbTtituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Actividades");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 
    
    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        if (Utilidades.mostrarDialogoConfirmacion("Cancelar Captura de Actividad ", 
                "¿Estás seguro de que deseas cancelar el registro de la actividad?")){
            irAVistaActividades(estudiante);
        }       
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        validarFechas();
    }

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        irAVistaActividades(estudiante); 
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
                break;
            default:
                throw new AssertionError();
        }
    }
    
}