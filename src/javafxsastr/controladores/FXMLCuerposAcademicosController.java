/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 06/06/2023
 * Descripción: La clase FXMLCuerposAcademicosController es el controlador
 * de la vista homónima, define los métodos funciones para mostrar
 * los cuerpos académicos y LGACS registradas en el sistema, así
 * como intermedia para realizar acciones con ellos. 
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.CuerpoAcademicoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.pojo.CuerpoAcademico;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaCuerpoAcademico;

public class FXMLCuerposAcademicosController implements Initializable {

    @FXML
    private Label lbTituloVentana;
    @FXML
    private VBox contenedorTarjetasCuerpoAcademico;
    @FXML
    private Pane pnCrearCuerpoAcademico;
    private Usuario usuario;
    private ObservableList<CuerpoAcademico> cuerposAcademicos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obtenerCuerposAcademicos();
        SortedList<CuerpoAcademico> sortedList = new SortedList<>(cuerposAcademicos,
                    Comparator.comparing(CuerpoAcademico::toString));
        cargarTarjetasCuerpoAcademico();
    }    

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    private void obtenerCuerposAcademicos() {
        try {
            cuerposAcademicos = FXCollections.observableArrayList(
                new CuerpoAcademicoDAO().obtenerCuerposAcademicos());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarTarjetasCuerpoAcademico() {
        for (CuerpoAcademico cuerposAcademico : cuerposAcademicos) {
            TarjetaCuerpoAcademico tarjeta = new TarjetaCuerpoAcademico(cuerposAcademico);
            tarjeta.getBotonModificarCuerpoAcademico().setOnAction((event) -> {
                FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAñadirCuerpoAcademico.fxml"));
                Parent vista;
                try {
                    vista = accesoControlador.load();
                    FXMLAñadirCuerpoAcademicoController controladorVistaInicio = accesoControlador.getController();
                    controladorVistaInicio.cargarDatos(cuerposAcademico, true);
                    Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
                    escenario.setScene(new Scene(vista));
                    escenario.setTitle("Modificar CA");
                    escenario.show();
                } catch (IOException ex) {                   
                    Logger.getLogger(FXMLCuerposAcademicosController.class.getName()).log(Level.SEVERE, null, ex);
                }               
            });
            contenedorTarjetasCuerpoAcademico.getChildren().add(tarjeta);
        }
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVentanaInicio(usuario);
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
    private void clicCrearCuerpoAcademico(MouseEvent event) {
         try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAñadirCuerpoAcademico.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAñadirCuerpoAcademicoController controladorVistaInicio = accesoControlador.getController();
            controladorVistaInicio.cargarDatos(null, false);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
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
    
}
