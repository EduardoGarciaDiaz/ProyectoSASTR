//TODO

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaEstudiante;

public class FXMLEstudiantesAsignadosController implements Initializable {

    @FXML
    private VBox contenedorTarjetasEstudiantes;
    private Academico academico;
    private ObservableList<Estudiante> estudiantes;
    @FXML
    private Label lbAdvertencia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setDirector(Academico academico) {
        this.academico = academico;
        obtenerEstudiantesAsignadosDelDirector();
        if (estudiantes.isEmpty()) {
            lbAdvertencia.setVisible(true);
        } else {
            cargarTarjetasCursos();
        }
    }
    
    private void obtenerEstudiantesAsignadosDelDirector() {
        try {
            estudiantes 
                = FXCollections.observableArrayList(
                        new EstudianteDAO().obtenerEstudiantesPorAcademico(academico.getIdAcademico())
            );
            Collections.sort(estudiantes, Comparator.comparing(Estudiante::getNombre));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarTarjetasCursos() {
        int filas = (int) Math.ceil((float) estudiantes.size()/4);
        Iterator<Estudiante> cursoIterator = estudiantes.iterator();
        if (cursoIterator.hasNext()) {
            for (int i = 0; i < filas; i++) {
                HBox contenedorTarjetas = new HBox(30);
                for (int j = 0; j < 4; j++) {
                    if (cursoIterator.hasNext()) {
                        Estudiante estudiante = cursoIterator.next();
                        TarjetaEstudiante tarjeta = new TarjetaEstudiante(estudiante);
                        tarjeta.getBotonVerAvance().setOnAction((event) -> {
                            irAVistaAvance(estudiante);
                        });
                        contenedorTarjetas.getChildren().add(tarjeta);
                    } else {
                        break;
                    }
                }
                contenedorTarjetasEstudiantes.getChildren().add(contenedorTarjetas);
            }  
        }
    }

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        irAVistaInicio(academico);
    }
    
    private void irAVistaAvance(Estudiante estudiante) {
        try {
            FXMLLoader accesoControlador 
                    = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarAvanceEstudiante.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarAvanceEstudianteController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setEstudianteAcademico(
                    estudiante, academico, CodigosVentanas.ESTUDIANTES_ASIGNADOS, null);
            Stage escenario = (Stage) lbAdvertencia.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Avance del estudiante");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaInicio(Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicio.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInicioController controladorVistaCrearAnteproyecto = accesoControlador.getController();
            controladorVistaCrearAnteproyecto.setUsuario(academico);
            Stage escenario = (Stage) lbAdvertencia.getScene().getWindow();
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
}
