/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 23/05/2023
 * Descripción: Controlador de FXMLAñadirLgac.
 */
package javafxsastr.controladores;

import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.LgacDAO;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.utils.Utilidades;

/**
 * FXML Controller class
 *
 * @author tristan
 */
public class FXMLAñadirLgacController implements Initializable {

    @FXML
    private AnchorPane menuContraido;
    @FXML
    private TextField txfNombreLgac;
    @FXML
    private TextArea txaDescripcionLgac;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lbNombreLgac;
    @FXML
    private Label lbDescirpcionLgac;
    @FXML
    private ImageView btnMenu;
    @FXML
    private AnchorPane menuLateral;

    private String nombreLgac;
    private  String descripcionLgac;
    private ObservableList<Lgac> lgacs;
    private boolean isEdicion;
    private int lgacIdEdicion;
    private INotificacionSeleccionItem interfazNotificaiconDesasignacion;
    
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        btnGuardar.setDisable(true);
        txfNombreLgac.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarBtnGuardar();
            }
        });       
        txaDescripcionLgac.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarBtnGuardar();
            }
        });  
    }      
    
    private void cargarCampos(Lgac lgacEditar, boolean esEdicion, INotificacionSeleccionItem interfaz) {
        interfazNotificaiconDesasignacion = interfaz;
        lgacIdEdicion = lgacEditar.getIdLgac();
        ///borrar despues
        try {
            lgacEditar= new LgacDAO().obtenerInformacionLGAC(1);
        } catch (DAOException ex) {
            Logger.getLogger(FXMLAñadirLgacController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lbNombreLgac.setText(lgacEditar.getNombreLgac());
        lbDescirpcionLgac.setText(lgacEditar.getDescripcionLgac());
        isEdicion = true;//esEdicion;
    }
    
    private void validarBtnGuardar() {
       nombreLgac = txfNombreLgac.getText();
       descripcionLgac = txaDescripcionLgac.getText();
        if(nombreLgac.length() > 10 && descripcionLgac.length() > 10) {
            habilitarBtnGuardar();
        }else {
            btnGuardar.setDisable(true);
        }
    }
    
    private void habilitarBtnGuardar() {
        btnGuardar.setDisable(false);
    }
    
    private boolean estaRepetida() {
        boolean esRepetida = false;
        for (int i = 0; i < lgacs.size(); i++) {            
            String original = lgacs.get(i).getNombreLgac().toLowerCase().trim();
            String nueva = nombreLgac.toLowerCase().trim();
            if(original == null ? nueva == null : original.equals(nueva)) {                
                esRepetida = true;              
            }
        }       
        return esRepetida;
    }
    
    private void validarLgac() {
        LgacDAO lgacDao = new LgacDAO();
        try {
            lgacs = FXCollections.observableArrayList();           
            lgacs.addAll(lgacDao.obtenerInformacionLGCAS());
                       
            if(lgacs.isEmpty()) {      
                registrarLgac();      
            }else if(estaRepetida()) {
                Utilidades.mostrarDialogoSimple("LGAC repetida", "La LGAC "+nombreLgac+" ya existe", Alert.AlertType.INFORMATION);
                }else {                
                    registrarLgac();
                }            
        } catch (DAOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarDialogoSimple("Error", "Ocurrio un error al consultar las LGAC's", Alert.AlertType.ERROR);
        }
    }
    
    private void registrarLgac() {
        LgacDAO lgacDao = new LgacDAO();
        Lgac lgacNuevo = new Lgac();
        lgacNuevo.setNombreLgac(nombreLgac);
        lgacNuevo.setDescripcionLgac(descripcionLgac);
        try {
            if(isEdicion) {
                lgacNuevo.setIdLgac(lgacIdEdicion);
                int exito = lgacDao.actualizarLgac(lgacNuevo);
                if(exito != -1) {
                    Utilidades.mostrarDialogoSimple("Actualizacion exitoso","La LGAC "+nombreLgac+" se actualizo correctamente", 
                            Alert.AlertType.CONFIRMATION);           
                }else {
                    System.err.println(exito);                
                    Utilidades.mostrarDialogoSimple("Registro Fallido","Ocurrio un error al registrar la LGAC, intenetelo mas tarde",
                            Alert.AlertType.ERROR);
                }
            }else {
                int exito = lgacDao.guardarLgac(lgacNuevo);
                if(exito != -1) {
                    Utilidades.mostrarDialogoSimple("Registro exitoso","La LGAC "+nombreLgac+" se registro correctamente", 
                            Alert.AlertType.CONFIRMATION);
                    txfNombreLgac.clear();
                    txaDescripcionLgac.clear();
                    btnGuardar.setDisable(true);
                }else {
                    System.err.println(exito);                
                    Utilidades.mostrarDialogoSimple("Registro Fallido","Ocurrio un error al registrar la LGAC, intenetelo mas tarde",
                            Alert.AlertType.ERROR);
                }
            }
        }catch (DAOException ex) {           
                Utilidades.mostrarDialogoSimple("Registro Fallido","Ocurrio un error al registrar la LGAC",
                            Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
       // interfazNotificaiconDesasignacion.notificarPerdidaDelFoco();
        Stage escenarioActual = (Stage) txaDescripcionLgac.getScene().getWindow();
        escenarioActual.close();
    }   
    
    @FXML
    private void clicBtnCancelar(ActionEvent event) {
       boolean confirmacion = Utilidades.mostrarDialogoConfirmacion("Cancelar captura de LGAC", 
               "Estas seguro que deseas cancelar la adicion de esta LGAC, se perderan todos datos sin guardados");
       if(confirmacion) {
           cerrarVentana();
       }
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if(isEdicion) {
            registrarLgac();
        }else{
             validarLgac();
        }
       
    }

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        cerrarVentana();
    }

 
    
    
    
}
