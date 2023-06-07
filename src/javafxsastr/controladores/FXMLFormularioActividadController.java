/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 02/06/2023
 * Descripción: Controller de la ventana AñadirCuerpoAcademico
 */
package javafxsastr.controladores;

import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.HistorialCambiosDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.HistorialCambios;
import javafxsastr.utils.Utilidades;


public class FXMLFormularioActividadController implements Initializable {

    @FXML
    private AnchorPane menuContraido;
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
    private Pane paneLateralIzquierdo;
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
    private Pane panelLateral;
    @FXML
    private Line lineaVeritcal;
    @FXML
    private Line lineaHorizontal;
    @FXML
    private ImageView imgBtnDesplegar;
    @FXML
    private Label lbActRevisadas;
    @FXML
    private Label lbActSinPendientes;
    @FXML
    private Label lbActPorVencer;
    @FXML
    private Label lbTtituloVentana;

    private INotificacionSeleccionItem interfazNotificaicon;
    private Estudiante estudiante;
    private Anteproyecto anteproyecto;
    private Curso curso;
    private Academico academico;
    private ObservableList<Academico> codirectores;
    private boolean menuDatos;
    private int porVnecer;
    private int realizadas;
    private int revisadas;
    private boolean isEdicion = false;
    private Actividad actividadEdicion;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnGuardar.setDisable(true);
        menuDatos = false;
        inicializarListeners();   
        obtenerDatosRelacionadoAlEstudiante();
        if(isEdicion) {
            lbTtituloVentana.setText("Actualizar Actividad");
        }        
    }
    
    private void cargarInformacion() {     
        Actividad actividad = actividadEdicion;
        txfNombreActividad.setText(actividad.getNombreActividad());
        txaDetallesActividad.setText(actividad.getDetallesActividad());
        txfHoraFin.setText(actividad.getHoraFinActividad());
        txfHoraInicio.setText(actividad.getHoraInicioActividad());
        LocalDate fechaInicio = LocalDate.parse(actividad.getFechaInicioActividad());
        dtpInicio.setValue(fechaInicio);
        LocalDate fechaFin = LocalDate.parse(actividad.getFechaFinActividad());
        dtpFin.setValue(fechaFin);  
        btnGuardar.setDisable(false);
    }
    
    public void iniciarFormularioNUevo(Estudiante estudianteN, boolean edicion, Actividad act ) {        
        isEdicion = edicion;
        this.estudiante = estudianteN;
        if(isEdicion) {
            actividadEdicion = act;           
            cargarInformacion();
        }        
    }
    
    private void inicializarListeners() {
        txfNombreActividad.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                
                validarBtnGuardar();
                
            }
        });        
        txaDetallesActividad.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                
                validarBtnGuardar();
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
            public void changed(ObservableValue<? extends EventHandler<? super MouseEvent>> observable, EventHandler<? super MouseEvent> oldValue, EventHandler<? super MouseEvent> newValue) {
                
                validarBtnGuardar();
            }
        });
        dtpFin.onMouseEnteredProperty().addListener(new ChangeListener<EventHandler<? super MouseEvent>>(){
            @Override
            public void changed(ObservableValue<? extends EventHandler<? super MouseEvent>> observable, EventHandler<? super MouseEvent> oldValue, EventHandler<? super MouseEvent> newValue) {
               
                validarBtnGuardar();
            }
        });        
    }
    
    private void validarBtnGuardar() {
        if( txfNombreActividad.getText().isEmpty() || txaDetallesActividad.getText().length()== 0
                || dtpInicio.getValue() == null || dtpFin.getValue() == null ||
                txfHoraInicio.getText().isEmpty() || txfHoraFin.getText().isEmpty()) {
            btnGuardar.setDisable(true);
        }else {
            btnGuardar.setDisable(false);
        }
    }
    
    public void validarFechas() {   
        try {
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
            Date primeraFecha = fecha.parse(dtpInicio.getValue().toString());
            Date segundaFecha = fecha.parse(dtpFin.getValue().toString());
            Date periodoEscolar = fecha.parse(curso.getFechaFinCurso());
            Date periodoEscolarInicio = fecha.parse(curso.getFechaInicioCurso());
            long diferenciaMilisecond = segundaFecha.getTime()-primeraFecha.getTime();    
            long diferenciaTiempoActual = primeraFecha.getTime()-periodoEscolarInicio.getTime(); 
            long diferenciaFinCurso = periodoEscolar.getTime()-segundaFecha.getTime();            
            LocalDate fechaInicio = dtpInicio.getValue();
            LocalDate fechaFin = dtpFin.getValue();
                if(diferenciaMilisecond <= 0 || diferenciaFinCurso <= 0 || diferenciaTiempoActual < 0) {
                   Utilidades.mostrarDialogoSimple("Error","Fechas Invalidas", Alert.AlertType.ERROR);
                }else{
                    if(isEdicion) {
                        actualizarActividad();
                    }else {
                        registrarActiivad(); 
                    }                   
                }
        } catch (ParseException ex) {
            Utilidades.mostrarDialogoSimple("Error","Intentelo mas tarde", Alert.AlertType.ERROR);
        }
    }
    
    private void validarHora(TextField textHora) {
        if(Pattern.matches("^([01]?\\d|2[0-3]):[0-5]\\d$", textHora.getText())){
            validarBtnGuardar();
            textHora.setStyle("-fx-border-width: 2;");
        }else {
            btnGuardar.setDisable(true);
            textHora.setStyle("-fx-border-color:RED; -fx-border-width: 2;");
        }
            
    }
    
    private void registrarActiivad() {
        Actividad actividadNueva = new Actividad();
        actividadNueva.setNombreActividad(txfNombreActividad.getText());
        actividadNueva.setDetallesActividad(txaDetallesActividad.getText());
        actividadNueva.setFechaInicioActividad(dtpInicio.getValue().toString());
        actividadNueva.setFechaFinActividad(dtpFin.getValue().toString());
        actividadNueva.setHoraInicioActividad(txfHoraInicio.getText()+":00");
        actividadNueva.setHoraFinActividad(txfHoraFin.getText()+":00");
        actividadNueva.setFechaCreaciónActividad(LocalDate.now().toString());
        actividadNueva.setIdAnteproyecto(anteproyecto.getIdAnteproyecto());
        actividadNueva.setIdEstadoActividad(1);
        actividadNueva.setIdEstudiante(estudiante.getIdEstudiante());
        actividadNueva.setEstadoActividad("Creada");
        try {
            int exito = new ActividadDAO().guardarActividad(actividadNueva);
            Utilidades.mostrarDialogoSimple("Registro Exitoso","Actividad agregada con exito", 
                    Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (DAOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarDialogoSimple("Error", "HUbo un error al registrar la actividad", 
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
        try {
            int exito = new ActividadDAO().actualizarActividad(actividadEditada);
            if(exito != -1) {
                SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
                HistorialCambiosDAO hisCamDao = new HistorialCambiosDAO();                
                if(!actividadEditada.getFechaInicioActividad().equals(actividadEdicion.getFechaInicioActividad())) {
                    HistorialCambios cambioFecha = new HistorialCambios();
                    Date fechaAnterior = fecha.parse(actividadEdicion.getFechaInicioActividad());
                    Date fechaNueva = fecha.parse(actividadEditada.getFechaInicioActividad());
                    Date fechaModiciacion = fecha.parse(LocalDate.now().toString());
                    Time horaAnterior = (Time) hora.parse(actividadEdicion.getHoraInicioActividad());
                    Time horaNueva = (Time) hora.parse(actividadEditada.getHoraInicioActividad());
                    cambioFecha.setFechaAnterior((java.sql.Date) fechaAnterior);
                    cambioFecha.setFechaNueva((java.sql.Date) fechaNueva);
                    cambioFecha.setFechaDeModificacion((java.sql.Date) fechaModiciacion);
                    cambioFecha.setHoraAnterior(horaAnterior);
                    cambioFecha.setHoraNueva(horaNueva);
                    hisCamDao.guardarHistorialCambios(cambioFecha);  
                }
                if(!actividadEditada.getFechaFinActividad().equals(actividadEdicion.getFechaFinActividad())) {
                    HistorialCambios cambioFecha = new HistorialCambios();
                    Date fechaAnterior = fecha.parse(actividadEdicion.getFechaFinActividad());
                    Date fechaNueva = fecha.parse(actividadEditada.getFechaFinActividad());
                    Date fechaModiciacion = fecha.parse(LocalDate.now().toString());  
                    Time horaAnterior = (Time) hora.parse(actividadEdicion.getHoraFinActividad());
                    Time horaNueva = (Time) hora.parse(actividadEditada.getHoraFinActividad());
                    cambioFecha.setFechaAnterior((java.sql.Date) fechaAnterior);
                    cambioFecha.setFechaNueva((java.sql.Date) fechaNueva);
                    cambioFecha.setFechaDeModificacion((java.sql.Date) fechaModiciacion);
                    cambioFecha.setHoraAnterior(horaAnterior);
                    cambioFecha.setHoraNueva(horaNueva);
                    hisCamDao.guardarHistorialCambios(cambioFecha);           
                }                    
            }
            Utilidades.mostrarDialogoSimple("Actualizacion Exitoso","Actividad actualizada con exito", 
                    Alert.AlertType.INFORMATION);
            //interfazNotificaicon.notificarPerdidaDelFoco();
            cerrarVentana();
        }catch (ParseException ex) {
                        Utilidades.mostrarDialogoSimple("Error", "Hubo un error al castear la fechas", 
                    Alert.AlertType.ERROR);
                    }  
        catch (DAOException ex) {
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
    
    private void obtenerDatosRelacionadoAlEstudiante() {
        AnteproyectoDAO anteproyectoDao = new AnteproyectoDAO();
        CursoDAO cursoDao = new CursoDAO();        
        AcademicoDAO academicoDao = new AcademicoDAO(); 
        ActividadDAO acatividadesDao = new ActividadDAO();
        try {
           curso = cursoDao.ordenarCursosPorEstudiante(estudiante.getIdEstudiante());
           anteproyecto = anteproyectoDao.obtenerAnteproyectosPorEstudiante(estudiante.getIdEstudiante());
           academico = academicoDao.obtenerAcademicoPorId(curso.getIdAcademico());
           codirectores =  FXCollections.observableArrayList(new AcademicoDAO().obtenerCodirectoresProAnteproyecto(anteproyecto.getIdAnteproyecto()));
           porVnecer = acatividadesDao.totalActividades(1,estudiante.getIdEstudiante());
           realizadas = acatividadesDao.totalActividades(4,estudiante.getIdEstudiante());
           revisadas = acatividadesDao.totalActividades(3,estudiante.getIdEstudiante());
        } catch (DAOException ex) {
            Logger.getLogger(FXMLFormularioActividadController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        setInformacion();
    }
    
     private void setInformacion() {
         if(curso != null) {
             lbCurso.setText(curso.getNombreCurso());
             lbNrc.setText(curso.getNrcCurso());
             lbPeriodo.setText(curso.getFechaInicioCurso()+"-"+curso.getFinPeriodoEscolar());
         }
         if(academico != null) {
             lbDocente.setText(academico.getNombre()+" "+academico.getPrimerApellido());
         }
         if(anteproyecto != null) {
             lbAnteproyecto.setText(anteproyecto.getNombreTrabajoRecepcional());
         lbDirector.setText(anteproyecto.getNombreDirector());
         }
         String codirectoresNombre= "";
         if(!codirectores.isEmpty()) {             
             for (int i = 0; i < codirectores.size(); i++) {
                 codirectoresNombre = codirectoresNombre+codirectores.get(i).getNombre()+" "+codirectores.get(i).getPrimerApellido()+"\n";
             }
         }        
         lbCodirector.setText(codirectoresNombre);
         lbActSinPendientes.setText(porVnecer+" Actividades sin realizar");
         lbActRevisadas.setText(realizadas+" Actividades realizadas");
         lbActPorVencer.setText(revisadas+" Actiivdades revisadas");
     }
     
      private void actualizaEstadoMenu(int posicion, boolean abierto, String icono){
        animacionMenu(posicion);
        menuDatos= abierto;
        imgBtnDesplegar.setImage(new Image(JavaFXSASTR.class.getResource(icono).toString()));
    }
      
    private void animacionMenu(int posicion){
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(panelLateral);
        translate.setDuration(Duration.millis(300));
        translate.setByX(posicion);        
        translate.setAutoReverse(true);
        translate.play();
    }
     
     
     private void cerrarVentana() {
        Stage escenarioActual = (Stage) lbAnteproyecto.getScene().getWindow();
        escenarioActual.close();
    }   

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        if(Utilidades.mostrarDialogoConfirmacion("Cancelar Captura de Actividad ", 
                "¿Estás seguro de que deseas cancelar el registro de la actividad?")){
         cerrarVentana();
        }       
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        validarFechas();
    }

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        cerrarVentana();
    }


    @FXML
    private void clicEscoderPanleIzquierdo(MouseEvent event) {
        if(menuDatos) {
            actualizaEstadoMenu(-433, false, "recursos/hide.jpg");
            lineaHorizontal.setVisible(true);
            lineaVeritcal.setVisible(true);
        }
        else{
            actualizaEstadoMenu(433, true, "recursos/show.jpg");
            lineaHorizontal.setVisible(false);
            lineaVeritcal.setVisible(false);
        }
            
    }
    
}
