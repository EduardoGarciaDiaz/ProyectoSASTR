/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 06/06/2023
 * Descripción: Clase controlador para la vista de un usuario específico. 
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.UsuarioDAO;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;

public class FXMLVerUsuarioController implements Initializable {

    @FXML
    private Label lbTituloVentana;
    @FXML
    private Label lbCorreoInstitucional;
    @FXML
    private Label lbPrimerApellido;
    @FXML
    private Label lbSegundoApellido;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbTipoUsuario;
    @FXML
    private Button clicBtnDesativar;

    private Usuario usuarioVisualizacion;
    private Usuario usuario;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setUsuario(Usuario usuarioVisualizacion, Usuario usuarioActual) {
        this.usuarioVisualizacion = usuarioVisualizacion;
        usuario = usuarioActual;
        setDatos();
    }
    
    private void setDatos() {
        if (usuarioVisualizacion.getIdEstadoUsuario() == 1) {
            clicBtnDesativar.setText("Desactivar");
        } else {
            clicBtnDesativar.setText("Activar");
        }
        if (usuarioVisualizacion.getNombre() != null) {
            lbNombre.setText(usuarioVisualizacion.getNombre());
        }
        if (usuarioVisualizacion.getPrimerApellido() != null) {
            lbPrimerApellido.setText(usuarioVisualizacion.getPrimerApellido());
        }
        if (usuarioVisualizacion.getSegundoApellido()!= null) {
            lbSegundoApellido.setText(usuarioVisualizacion.getSegundoApellido());
        }
        if (usuarioVisualizacion.getCorreoInstitucional() != null){
            lbCorreoInstitucional.setText(usuarioVisualizacion.getCorreoInstitucional());
        }
        try {
            if (new EstudianteDAO().obtenerEstudiantePorIdUsuario(usuarioVisualizacion.getIdUsuario()).getIdEstudiante()>0) {
                lbTipoUsuario.setText("Estudiante");
            } else {
                lbTipoUsuario.setText("Academico");
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVentanaUsuarios(usuario);
    }

    @FXML
    private void clicBtnDesactivar(ActionEvent event) {
        if (usuarioVisualizacion.getIdEstadoUsuario() == 1) {
            boolean respuesta = Utilidades.mostrarDialogoConfirmacion
                ("Desactivar usuario", "¿Estás seguro de que deseas desactivar al usuario?");
            if (respuesta) {
                if ((usuarioVisualizacion.getEsAdministrador()) && (!validarExistenciaOtroAdministrador())) {
                    Utilidades.mostrarDialogoSimple("Operación no posible", 
                                    "No puedes dejar al sistema sin un administrador. "
                                    + "Asegúrate de que exista otro administrador en el sistema.", Alert.AlertType.WARNING);
                } else {
                    usuarioVisualizacion.setIdEstadoUsuario(2);
                    actualizarUsuario(usuarioVisualizacion);
                    Utilidades.mostrarDialogoSimple("Usuario desactivado", 
                            "El usuario se ha desactivado correctamente", Alert.AlertType.INFORMATION);
                    clicBtnDesativar.setText("Activar");
                }
            }
        } else {
            boolean respuesta = Utilidades.mostrarDialogoConfirmacion
                ("Activar usuario", "¿Estás seguro de que deseas activar al usuario?");
            if (respuesta) {
                usuarioVisualizacion.setIdEstadoUsuario(1);
                actualizarUsuario(usuarioVisualizacion);
                Utilidades.mostrarDialogoSimple("Usuario activado", 
                        "El usuario se ha activado correctamente", Alert.AlertType.INFORMATION);
                clicBtnDesativar.setText("Desactivar");
            }
        }
    }
    
    private boolean validarExistenciaOtroAdministrador() {
        try {
            int numeroAdministradores = new UsuarioDAO().consultarNumeroAdministradores();
            return numeroAdministradores > 1;
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return false;
    }
    
    private void actualizarUsuario(Usuario usuario) {
        try {
            new UsuarioDAO().actualizarUsuario(usuario);
        } catch(DAOException ex) {
            manejarDAOException(ex);
        }
    }

    @FXML
    private void clicBtnModificar(ActionEvent event) {
        irAVentanaModificar(usuarioVisualizacion);
    }
    
    private void irAVentanaModificar(Usuario usuarioEdicion) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioUsuario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioUsuarioController controladorVistaFormularioUsuario = accesoControlador.getController();
            controladorVistaFormularioUsuario.inicializarInformacionFormulario(true, usuarioEdicion);
            controladorVistaFormularioUsuario.setUsuario(usuario);
            Stage escenarioBase = (Stage) lbTituloVentana.getScene().getWindow();
            escenarioBase.setScene(new Scene(vista));
            escenarioBase.setTitle("Formulario Usuario");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVentanaUsuarios(Usuario usuario) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLUsuarios.fxml"));
            Parent vista = accesoControlador.load();
            FXMLUsuariosController controladorVistaUsuarios = accesoControlador.getController();
            controladorVistaUsuarios.setUsuario(usuario);
            Stage escenario = (Stage) lbCorreoInstitucional.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Usuarios");
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
                throw new AssertionError();
        }
    }
        
}