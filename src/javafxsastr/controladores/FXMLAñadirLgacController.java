/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 23/05/2023
 * Descripción: Controlador de la ventana Añadir y modificar Lgac.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.LgacDAO;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.FiltrosTexto;
import javafxsastr.utils.Utilidades;
import javafxsastr.interfaces.INotificacionRecargarDatos;

public class FXMLAñadirLgacController implements Initializable {

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
    private Label lbTituloVentana;

    private final int LIMT_CARACT_NOMBRE = 200;
    private final int LIMT_CARACT_DESCRIPCION = 500;
    private final int MIN_CARACT_NOMBRE = 5;
    private final int MIN_CARACT_DESCRIPCION = 10;
    private Usuario ususarioActual;
    private ObservableList<Lgac> lgacs;
    private boolean isEdicion = false;
    private Lgac lgacIdEdicion;
    private boolean esDeVentanaCA = false;
    private INotificacionRecargarDatos interfaz;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        btnGuardar.setDisable(true);
        inicializarListeners();
        inicializarFiltros();
    }  
    
    public void setUsuario(Usuario usuario) {
        this.ususarioActual = usuario;
    }
    
    public void instancearInterfaz(INotificacionRecargarDatos interfazN) {
       interfaz = interfazN;
    }

    public void registroLgacPorCA(boolean origen) {
        esDeVentanaCA = origen;
    }
    
    public void cargarCampos(Lgac lgacEditar, boolean esEdicio, Usuario usuario) {
        isEdicion = true;
        lgacIdEdicion = lgacEditar;
        ususarioActual = usuario;               
        txfNombreLgac.setText(lgacEditar.getNombreLgac());
        txaDescripcionLgac.setText(lgacEditar.getDescripcionLgac()); 
        if (isEdicion){
            lbTituloVentana.setText("Modificar Lgac");
        }
    }
    
    private void inicializarListeners() {
        txfNombreLgac.textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (txfNombreLgac.getText().length() < LIMT_CARACT_NOMBRE) {
                        validarBtnGuardar();
                        lbNombreLgac.setText("");
                    } else {
                        mostraMensajelimiteSuperado(LIMT_CARACT_NOMBRE, "Nombre LGAC", lbNombreLgac);
                    }                
                }
            }
        );       
        txaDescripcionLgac.textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                     if (txfNombreLgac.getText().length() < LIMT_CARACT_DESCRIPCION) {
                        validarBtnGuardar();
                        lbDescirpcionLgac.setText("");
                    } else {
                        mostraMensajelimiteSuperado(LIMT_CARACT_DESCRIPCION, "Descripcion LGAC", lbDescirpcionLgac);
                    }     
                }
            }
        ); 
    }
    
    private void inicializarFiltros() {
        FiltrosTexto.filtroLetrasNumerosPuntosComasSignosComunes(txfNombreLgac);
        FiltrosTexto.filtroLetrasNumerosPuntosComasSignosComunes(txaDescripcionLgac);
    }
    
    private void validarBtnGuardar() {
       String nombreLgac = txfNombreLgac.getText();
       String descripcionLgac = txaDescripcionLgac.getText();
        if (nombreLgac.trim().length() > MIN_CARACT_NOMBRE && descripcionLgac.trim().length() > MIN_CARACT_DESCRIPCION && 
            nombreLgac.trim().length() < LIMT_CARACT_NOMBRE && descripcionLgac.trim().length() < LIMT_CARACT_DESCRIPCION) {
            habilitarBtnGuardar();
        } else {
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
            String nueva = txfNombreLgac.getText().toLowerCase().trim();
            if (original == null ? nueva == null : original.equals(nueva)) {                
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
            if (lgacs.isEmpty()) {      
                registrarLgac();      
            } else if (estaRepetida()) {
                Utilidades.mostrarDialogoSimple("LGAC repetida", "La LGAC " + txfNombreLgac.getText() + " ya existe", 
                    Alert.AlertType.INFORMATION);
                } else {                
                    registrarLgac();
                }            
        } catch (DAOException ex) {
             manejarDAOException(ex);
        }
    }
    
    private void registrarLgac() {
        LgacDAO lgacDao = new LgacDAO();
        Lgac lgacNuevo = new Lgac();
        lgacNuevo.setNombreLgac(txfNombreLgac.getText().trim());
        lgacNuevo.setDescripcionLgac(txaDescripcionLgac.getText().trim());
        try {
            if (isEdicion) {
                lgacNuevo.setIdLgac(lgacIdEdicion.getIdLgac());
                int exito = lgacDao.actualizarLgac(lgacNuevo);
                if (exito != -1) {
                    Utilidades.mostrarDialogoSimple("Actualizacion exitoso","La LGAC " + txfNombreLgac.getText()
                            + " se actualizo correctamente",Alert.AlertType.INFORMATION);   
                    cerrarVentana();
                } else {               
                    Utilidades.mostrarDialogoSimple("Registro Fallido","Ocurrio un error al registrar la LGAC, intenetelo mas tarde",
                        Alert.AlertType.ERROR);
                }
            } else {
                int exito = lgacDao.guardarLgac(lgacNuevo);
                if (exito != -1) {
                    Utilidades.mostrarDialogoSimple("Registro exitoso","La LGAC " + txfNombreLgac.getText()+" se registro correctamente", 
                        Alert.AlertType.INFORMATION);
                    btnGuardar.setDisable(true);
                    limpiarCampos();
                } else {           
                    Utilidades.mostrarDialogoSimple("Registro Fallido", "Ocurrio un error al registrar la LGAC, intentelo mas tarde",
                        Alert.AlertType.ERROR);
                }
            }            
        } catch (DAOException ex) {           
            manejarDAOException(ex);
        }
    }
    
    private void limpiarCampos() {
        txfNombreLgac.setText("");
        txaDescripcionLgac.setText("");
    }
    
    private void mostraMensajelimiteSuperado(int limiteCaracteres, String campo, Label etiquetaError) {        
        etiquetaError.setText("Cuidado, Exediste el limite de caracteres("+limiteCaracteres+") de este campo " + campo);
        btnGuardar.setDisable(true);
    }    
    
    private void cerrarVentana() {
        try {               
            Stage escenarioActual = (Stage) txaDescripcionLgac.getScene().getWindow();
            if (esDeVentanaCA) {
               FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAñadirCuerpoAcademico.fxml"));
               interfaz.notificacionRecargarDatos();
           } else {
                FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCuerposAcademicos.fxml"));
                Parent vista = accesoControlador.load();
                FXMLCuerposAcademicosController controladorVistauerposAcademicos = accesoControlador.getController();  
                controladorVistauerposAcademicos.setUsuario(ususarioActual);
                controladorVistauerposAcademicos.setterVistaLgacs();
                escenarioActual.setScene(new Scene(vista));
                escenarioActual.setTitle("Cuerpos Academicos"); 
                escenarioActual.show();
            }          
        } catch (IOException ex) {
              Utilidades.mostrarDialogoSimple("Error","No se pudo regresar a Registrar Cuerpos Academicos", Alert.AlertType.NONE);
        }
    } 
    
    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        boolean confirmacion = Utilidades.mostrarDialogoConfirmacion("Cancelar captura de LGAC", 
                "Estas seguro que deseas cancelar la adicion de esta LGAC, se perderan todos datos");
        if (confirmacion) {
            cerrarVentana();
        }
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if (isEdicion) {
            registrarLgac();
        } else {
             validarLgac();
        }       
    }

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        cerrarVentana();
    }
    
    private void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error de Consulta", 
                        "Hubo un error al realizar la consulta. Intentelo de nuevo o hagalo mas tarde", 
                        Alert.AlertType.ERROR);
                break;
            case ERROR_CONEXION_BD:
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", 
                        Alert.AlertType.ERROR);
            default:
                throw new AssertionError();
        }
    }
    
}
