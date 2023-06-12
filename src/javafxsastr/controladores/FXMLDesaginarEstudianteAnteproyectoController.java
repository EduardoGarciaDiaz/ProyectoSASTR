/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 04/06/2023
 * Descripción: Controller de la ventana AñadirCuerpoAcademico
 */

package javafxsastr.controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafxsastr.interfaces.INotificacionRecargarDatos;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.DesasignacionDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Desasignacion;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;

public class FXMLDesaginarEstudianteAnteproyectoController implements Initializable {

    @FXML
    private TextArea txaJustificacion;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<String> cmbMotivos;
    @FXML
    private Label lbJustificacion;
    
    private final int LIMIT_CARAC_JUSTIFICACION = 200;
    private ObservableList<String> motivos;
    private Estudiante estudianteDesasignar;
    private INotificacionRecargarDatos interfazNotificaiconDesasignacion;
    private Anteproyecto anteproyectoModificacion;
    private boolean esSoloVisto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {      
    } 
    
    public void iniciarDesasignacion(Estudiante estudiante,Anteproyecto anteproyecto,INotificacionRecargarDatos interfazNotificacion) {
        this.interfazNotificaiconDesasignacion = interfazNotificacion;
        this.estudianteDesasignar = estudiante;
        this.anteproyectoModificacion = anteproyecto;
        iniciarListeners();
        btnGuardar.setDisable(true);
        llenarComboJustificaion();        
    }
    
    public void verDesasignaicones(boolean esVista, Desasignacion desasignacion) {
        this.esSoloVisto = esVista;
        cmbMotivos.setValue(desasignacion.getMotivo());
        txaJustificacion.setText(desasignacion.getComentarios());
        btnGuardar.setStyle("-fx-background-color: #D9D9D9");  
        btnGuardar.setText("Cerrar");
        cmbMotivos.setEditable(false);
        txaJustificacion.setEditable(false);
        btnCancelar.setVisible(false);
        btnGuardar.setOnAction((event) -> {
            cerrarVentana();
        });
    }
    
    private void iniciarListeners() {
        cmbMotivos.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
              if (newValue != null) {              
                 validarCampos();            
               }
        });
        txaJustificacion.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txaJustificacion.getText().length() > LIMIT_CARAC_JUSTIFICACION) {
                    mostraMensajelimiteSuperado(LIMIT_CARAC_JUSTIFICACION,"Justificacion",lbJustificacion); 
                    btnGuardar.setDisable(true);
                }else {
                   lbJustificacion.setText(""); 
                   validarCampos();                   
                }                
            }
        });
    }
    
    private void validarCampos() {
        if(cmbMotivos.getSelectionModel().getSelectedItem() != null && txaJustificacion.getText().trim().length() > 5 
                && txaJustificacion.getText().trim().length() <= 200) {
            btnGuardar.setDisable(false);
        }else {
            btnGuardar.setDisable(true);
        }
    }
    
    private void desasignarEstudiante() {
        EstudianteDAO estudianteDao = new EstudianteDAO();
        try {
            int respuesta = estudianteDao.desasignarEstudiante(estudianteDesasignar);
            if(respuesta != -1){               
                Desasignacion desasignacion = new Desasignacion();
                desasignacion.setMotivo(cmbMotivos.getSelectionModel().getSelectedItem());
                desasignacion.setComentarios(txaJustificacion.getText());
                desasignacion.setIdEstudiante(estudianteDesasignar.getIdEstudiante());
                desasignacion.setIdAnteproyecto(anteproyectoModificacion.getIdAnteproyecto());
                int insertDesasignacion = new DesasignacionDAO().guardarDesasignacion(desasignacion);
                if(insertDesasignacion != -1) {              
                      Utilidades.mostrarDialogoSimple("Desasignacion exitosa",
                        "Se desasigno exitosamente al estudiante : "+estudianteDesasignar.getNombre() + " "
                                + estudianteDesasignar.getPrimerApellido(), Alert.AlertType.INFORMATION);
                      interfazNotificaiconDesasignacion.notitficacionRecargarDatosPorEdicion(true);
                      cerrarVentana();
                }                
            }
        } catch (DAOException ex) {  
            ex.printStackTrace();
            Utilidades.mostrarDialogoSimple("Error",
                        "No se pudo desasignar al estudiante", Alert.AlertType.ERROR);
        }        
    }
    
    private void llenarComboJustificaion() {
        motivos = FXCollections.observableArrayList();
       ArrayList<String> justificaciones = new ArrayList();
       justificaciones.add("Desacato");
       justificaciones.add("Retraso en el trabajo");
       justificaciones.add("Abandono");
       justificaciones.add("Incumplimiento");
       justificaciones.add("Insurbordinacion");
       justificaciones.add("Otro");
       motivos.addAll(justificaciones);
       llenarCombo();
    }
    
    private void mostraMensajelimiteSuperado(int limiteCaracteres, String campo,  Label etiquetaError) { 
        etiquetaError.setText("Cuidado, Exediste el limite de caracteres("+limiteCaracteres+") de este campo " + campo);
        btnGuardar.setDisable(true);
    }    
    
    private void llenarCombo() {
        cmbMotivos.setItems(motivos);
    }
    
    private void cerrarVentana() {        
        Stage escenarioActual = (Stage) cmbMotivos.getScene().getWindow();
        escenarioActual.close();
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        desasignarEstudiante();
    }
    
}