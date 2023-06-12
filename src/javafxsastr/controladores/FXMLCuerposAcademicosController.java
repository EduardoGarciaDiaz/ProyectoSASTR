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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.CuerpoAcademicoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.LgacDAO;
import javafxsastr.modelos.pojo.CuerpoAcademico;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaCuerpoAcademico;
import javafxsastr.utils.cards.TarjetaLgac;

public class FXMLCuerposAcademicosController implements Initializable {

    @FXML
    private Label lbTituloVentana;
    @FXML
    private VBox contenedorTarjetasCuerpoAcademico;
    @FXML
    private Label lbCaLgac;
    
    private Usuario usuario;
    private ObservableList<CuerpoAcademico> cuerposAcademicos;
    private ObservableList<Lgac> lgacs;
    private boolean consultandoCA = true;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verCuerposAcademicos();
    }    

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public void setterVistaLgacs() {
        cargarLgacs();
    }
    
    private void obtenerCuerposAcademicos() {
        try {
            cuerposAcademicos = FXCollections.observableArrayList(
                new CuerpoAcademicoDAO().obtenerCuerposAcademicos()
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void obtenerLgacs() {
       try {
            lgacs = FXCollections.observableArrayList(new LgacDAO().obtenerInformacionLGCAS());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    } 
    
    private void cargarTarjetasCuerpoAcademico() {
        for (CuerpoAcademico cuerposAcademico : cuerposAcademicos) {
            TarjetaCuerpoAcademico tarjeta = new TarjetaCuerpoAcademico(cuerposAcademico);
            tarjeta.getBotonModificarCuerpoAcademico().setOnAction(
                (event) -> {
                    irAvistaFormularioCuerpoAcademico(cuerposAcademico);                
                }
            );
            contenedorTarjetasCuerpoAcademico.getChildren().add(tarjeta);
        }
    }
    
    private void cargarTarjetasLgacs() {
        for (Lgac lgac : lgacs) {
            TarjetaLgac tarjeta = new TarjetaLgac(lgac);
            tarjeta.getBotonModificarLgac().setOnAction(
                (event) -> {
                    irVistaFormularioLgac(lgac);                
                }
            );
            contenedorTarjetasCuerpoAcademico.getChildren().add(tarjeta);
        }
    } 
    
    private void verCuerposAcademicos() {
        obtenerCuerposAcademicos();
        FXCollections.sort(cuerposAcademicos, Comparator.comparing(CuerpoAcademico::getNombreCuerpoAcademico));
        cargarTarjetasCuerpoAcademico();
    }
   
    private void cargarLgacs() {
        contenedorTarjetasCuerpoAcademico.getChildren().clear();
        obtenerLgacs();
        FXCollections.sort(lgacs, Comparator.comparing(Lgac::getNombreLgac));
        cargarTarjetasLgacs();
        lbCaLgac.setText("Añadir LGAC");
        lbTituloVentana.setText("LGAC");
        consultandoCA = false;
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVentanaInicio(usuario);
    }

    @FXML
    private void clicCrearCuerpoAcademico(MouseEvent event) {        
        if(consultandoCA) {
            irAvistaFormularioCuerpoAcademico(null);
        }else {
            irVistaFormularioLgac(null);
        }   
    }
    
    @FXML
    private void clicVerCuerposAcademicos(ActionEvent event) {
        contenedorTarjetasCuerpoAcademico.getChildren().clear();
        verCuerposAcademicos();
        lbCaLgac.setText("Añadir CA");
        lbTituloVentana.setText("Cuerpos Academicos");
        consultandoCA = true;
    }

    @FXML
    private void clicVerLgacs(ActionEvent event) {
        cargarLgacs();
    }
    
    private void irAvistaFormularioCuerpoAcademico(CuerpoAcademico cuerpoAcademicoEdicion ) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAñadirCuerpoAcademico.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAñadirCuerpoAcademicoController controladorVistaInicio = accesoControlador.getController();
            if (cuerpoAcademicoEdicion != null) {
                controladorVistaInicio.cargarDatos(cuerpoAcademicoEdicion, true, usuario, false);
            } else {
                controladorVistaInicio.cargarDatos(null, false, usuario,false);
            }
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            if (cuerpoAcademicoEdicion == null) {
                escenario.setTitle("Añadir Cuerpo Académico");
            } else {
                escenario.setTitle("Modificar Cuerpo Académico");
            }
            escenario.show();
        } catch (IOException ex) {                   
            ex.printStackTrace();
        }               
    }
    
    private void irVistaFormularioLgac(Lgac lgacEdicion) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAñadirLgac.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAñadirLgacController controladorFormularioLgac = accesoControlador.getController();
            if (lgacEdicion == null) {
                controladorFormularioLgac.setUsuario(usuario);
            } else {
                controladorFormularioLgac.cargarCampos(lgacEdicion, true, usuario);
            }
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            if (lgacEdicion == null) {
                escenario.setTitle("Añadir LGAC");
            } else {
                escenario.setTitle("Modificar LGAC");
            }
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
