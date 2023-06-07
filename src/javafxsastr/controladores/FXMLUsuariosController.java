/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: La clase FXMLUsuariosController define los métodos 
 * para mostrar en la interfaz de usuario los usuarios registrados
 * y es el paso para gestionarlos. 
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.UsuarioDAO;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaUsuario;

public class FXMLUsuariosController implements Initializable {
    
    private ObservableList<Usuario> usuarios;
    @FXML
    private VBox contenedorTarjetasUsuarios;
    @FXML
    private TextField tfCampoBusqueda;
    @FXML
    private Label lbTituloVentana;
    @FXML
    private Pane pbnBotonCrearUsuario;
    private Usuario usuario;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obtenerUsuarios();
        SortedList<Usuario> sortedList = new SortedList<>(usuarios,
                    Comparator.comparing(Usuario::toString));
        cargarTarjetasUsuarios(sortedList);
    }   
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        obtenerUsuarios();
        SortedList<Usuario> sortedList = new SortedList<>(usuarios,
                    Comparator.comparing(Usuario::toString));
        cargarTarjetasUsuarios(sortedList);
    }
    
    private void obtenerUsuarios() {
        try {
            usuarios = FXCollections.observableArrayList(
                new UsuarioDAO().obtenerUsuarios()
            );
        } catch (DAOException ex) {
            
        }
    }
    
    private void cargarTarjetasUsuarios(ObservableList<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            TarjetaUsuario tarjeta = new TarjetaUsuario(usuario);
            tarjeta.getBotonVerDetalles().setOnAction((event) -> {
                irAVentanaVerUsuario(usuario);
            });
            contenedorTarjetasUsuarios.getChildren().add(tarjeta);
        }
    }
    
    public void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                ex.printStackTrace();
                System.out.println("Ocurrió un error de consulta.");
                break;
            case ERROR_CONEXION_BD:
                ex.printStackTrace();
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", Alert.AlertType.ERROR);
            default:
                throw new AssertionError();
        }
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        System.out.println(usuario.getIdUsuario());
        irAVentanaInicio(usuario);
    }

    @FXML
    private void clicBtnTodos(ActionEvent event) {
        contenedorTarjetasUsuarios.getChildren().clear();
        SortedList<Usuario> sortedList = new SortedList<>(usuarios,
                    Comparator.comparing(Usuario::toString));
        cargarTarjetasUsuarios(sortedList);
    }

    @FXML
    private void clicBtnEstudiantes(ActionEvent event) {
        contenedorTarjetasUsuarios.getChildren().clear();
        if (usuarios.size() > 0) {
            FilteredList<Usuario> filtroUsuarios = new FilteredList<>(usuarios, p -> true);
            filtroUsuarios.setPredicate(usuario -> {
                try {
                    if (new EstudianteDAO()
                            .obtenerEstudiantePorIdUsuario(usuario.getIdUsuario()).getIdEstudiante() > 0) {   
                        return true;
                    }
                } catch (DAOException ex) {
                    manejarDAOException(ex);
                }
                return false;
            });
            SortedList<Usuario> sortedList = new SortedList<>(filtroUsuarios,
                    Comparator.comparing(Usuario::toString));
            cargarTarjetasUsuarios(sortedList);
        }
    }

    @FXML
    private void clicCrearUsuario(MouseEvent event) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioUsuario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioUsuarioController controladorVistaUsuario = accesoControlador.getController();
            controladorVistaUsuario.setUsuario(usuario);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Formulario Usuario");
            escenario.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicBtnAcademicos(ActionEvent event) {
        contenedorTarjetasUsuarios.getChildren().clear();
        if (usuarios.size() > 0) {
            FilteredList<Usuario> filtroUsuarios = new FilteredList<>(usuarios, p -> true);
            filtroUsuarios.setPredicate(usuario -> {
                try {
                    if (new AcademicoDAO().obtenerAcademicoPorIdUsuario(usuario.getIdUsuario()).getIdAcademico() > 0) {   
                        return true;
                    }
                } catch (DAOException ex) {
                    manejarDAOException(ex);
                }
                return false;
            });
            SortedList<Usuario> sortedList = new SortedList<>(filtroUsuarios,
                    Comparator.comparing(Usuario::toString));
            cargarTarjetasUsuarios(sortedList);
        }
    }
    
    private void irAVentanaInicio(Usuario usuario) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicio.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInicioController controladorVistaInicio = accesoControlador.getController();
            controladorVistaInicio.setUsuario(usuario);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVentanaVerUsuario(Usuario usuarioVisualizacion) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLVerUsuario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLVerUsuarioController controladorVistaVerUsuario = accesoControlador.getController();
            controladorVistaVerUsuario.setUsuario(usuarioVisualizacion, usuario);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Ver usuario");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
