/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Controller de la ventana AñadirCuerpoAcademico
 */
package javafxsastr.controladores;

import com.sun.corba.se.impl.util.Utility;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.DesasignacionDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Desasignacion;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;

public class FXMLDesaginarEstudianteAnteproyectoController implements Initializable {

    @FXML
    private TextArea txaJustificacion;
    @FXML
    private Button btnGuardar;
    @FXML
    private ComboBox<String> cmbMotivos;
    
    private ObservableList<String> motivos;
    private Estudiante estudianteDesasignar;
    private INotificacionSeleccionItem interfazNotificaiconDesasignacion;
    private int idAnteproyecto;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       iniciarListeners();
       btnGuardar.setDisable(true);
       llenarComboJustificaion();
        try {
            this.estudianteDesasignar = (new EstudianteDAO().obtenerEstudiante(1));
        } catch (DAOException ex) {
            Logger.getLogger(FXMLDesaginarEstudianteAnteproyectoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.idAnteproyecto = 5;
    }  
    
    private void iniciarDesasignacion(Estudiante estudiante,int idAnteproyecto,INotificacionSeleccionItem interfazNotificacion) {
        this.interfazNotificaiconDesasignacion = interfazNotificacion;
        this.estudianteDesasignar = estudiante;
        this.idAnteproyecto = 5;//idAnteproyecto;
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
               validarCampos();
            }
        });
    }
    
    private void validarCampos() {
        if(cmbMotivos.getSelectionModel().getSelectedItem() != null && txaJustificacion.getText().length() > 10) {
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
                desasignacion.setIdAnteproyecto(idAnteproyecto);
                int insertDesasignacion = new DesasignacionDAO().guardarDesasignacion(desasignacion);
                if(insertDesasignacion != -1) { 
                    
                     //interfazNotificaiconDesasignacion.notificarPerdidaDelFoco();
                      Utilidades.mostrarDialogoSimple("Desasignacion exitosa",
                        "Se desasigno exitosamente al estudiante : "+estudianteDesasignar.getNombre()+" "
                                +estudianteDesasignar.getPrimerApellido(), Alert.AlertType.INFORMATION);
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
