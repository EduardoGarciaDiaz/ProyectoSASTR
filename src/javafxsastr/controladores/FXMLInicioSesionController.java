/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 21/05/2023
 * Descripción: Clase controlador para la vista de Inicio de sesión:
 * define e implementa los métodos para la autenticación del usuario a fin de 
 * de determinar si permitir el acceso o no. 
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.UsuarioDAO;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfCorreoUsuario;
    @FXML
    private PasswordField tfContraseñaUsuario;
    @FXML
    private Label lbCampoCorreoRequerido;
    @FXML
    private Label lbCampoContraseñaRequerido;
    @FXML
    private Label lbErrorCredenciales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarListenersACampos();
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        try {
            if (tfCorreoUsuario.getText().isEmpty()) {
                lbCampoCorreoRequerido.setText("Campo requerido");
            } 
            if (tfContraseñaUsuario.getText().isEmpty()) {
                lbCampoContraseñaRequerido.setText("Campo requerido");
            }
            if (!tfCorreoUsuario.getText().isEmpty() && !tfContraseñaUsuario.getText().isEmpty()) {
                if (tfCorreoUsuario.getText()
                        .toLowerCase()
                        .equals(tfCorreoUsuario.getText())) {
                    autenticarUsuario(tfCorreoUsuario.getText(), tfContraseñaUsuario.getText());
                } else {
                    lbErrorCredenciales.setText("Ingresa el correo en minúsculas, por favor.");
                }
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void autenticarUsuario(String correoUsuario, String contraseña) throws DAOException {
        Usuario usuario = new UsuarioDAO().obtenerUsuario(correoUsuario, contraseña);
        if (usuario.getIdUsuario() > 0) {
            if (usuario.getIdEstadoUsuario() == 1) {
                irAVistaInicio(usuario);
            } else {
                Utilidades.mostrarDialogoSimple("Acceso denegado",
                        "Lo sentimos, en este momento no puedes acceder al sistema. Comunicate con el administrador.",
                        Alert.AlertType.INFORMATION);
            }
        } else {
            lbErrorCredenciales.setText("Usuario no encontrado: correo o contraseña incorrectos.");
        }
    }
    
    private void irAVistaInicio(Usuario usuario) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicio.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInicioController controladorVistaInicio = accesoControlador.getController();
            controladorVistaInicio.setUsuario(usuario);
            Stage escenario = (Stage) tfContraseñaUsuario.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void agregarListenersACampos() {
        tfCorreoUsuario.focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue) {
                    lbCampoCorreoRequerido.setText("");
                    lbErrorCredenciales.setText("");
                } else {
                    if (tfCorreoUsuario.getText().isEmpty()) {
                        lbCampoCorreoRequerido.setText("Campo requerido");
                    }
                }
            }
        );
        tfContraseñaUsuario.focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue) {
                    lbCampoContraseñaRequerido.setText("");
                    lbErrorCredenciales.setText("");
                } else {
                    if (tfContraseñaUsuario.getText().isEmpty()) {
                        lbCampoContraseñaRequerido.setText("Campo requerido");
                    }
                }
            }
        );
    }

    @FXML
    private void irVentanaAnteproyectosInvitado(MouseEvent event) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAnteproyectos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAnteproyectosController controladorVistaInvitado = accesoControlador.getController();
            controladorVistaInvitado.setInvitado(CodigosVentanas.INICIO_SESION);
            Stage escenario = (Stage) tfContraseñaUsuario.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
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
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", Alert.AlertType.ERROR);
            default:
                System.out.println("Error desconocido");
                
        }
    }
    
}
