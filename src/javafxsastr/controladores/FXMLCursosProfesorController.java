//toDO
package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaCursoCuadrada;

public class FXMLCursosProfesorController implements Initializable {

    @FXML
    private VBox vbxContenedorHbxTarjetas;
    private Academico profesor;
    private ObservableList<Curso> cursosActuales;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    public void setProfesor(Academico academico) {
        this.profesor = academico;
        obtenerCursosActualesDelProfesor();
        cargarTarjetasCursos();
    }
    
    private void obtenerCursosActualesDelProfesor() {
        try {
            cursosActuales = FXCollections.observableArrayList(
                   new CursoDAO().obtenerCursosDelProfesor(profesor.getIdAcademico())
            );
            Collections.sort(cursosActuales, Comparator.comparing(Curso::getNombreCurso));
        } catch (DAOException ex) {
            
        }
    }
    
    private void cargarTarjetasCursos() {
        int filas = (int)Math.ceil((float) cursosActuales.size()/4);
        Iterator<Curso> cursoIterator = cursosActuales.iterator();
        if (cursoIterator.hasNext()) {
            for (int i = 0; i < filas; i++) {
                HBox contenedorTarjetas = new HBox(30);
                for (int j = 0; j < 4; j++) {
                    if (cursoIterator.hasNext()) {
                        Curso curso = cursoIterator.next();
                        TarjetaCursoCuadrada tarjeta = new TarjetaCursoCuadrada(curso);
                        tarjeta.getBotonVerDetalles().setOnAction((event) -> {
                            irAVistaConsultarAvancesCurso(curso);
                        });
                        contenedorTarjetas.getChildren().add(tarjeta);
                    } else {
                        break;
                    }
                }
                vbxContenedorHbxTarjetas.getChildren().add(contenedorTarjetas);
            }  
        }
    }

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        irAVistaInicio(profesor);
    }
    
    private void irAVistaConsultarAvancesCurso(Curso curso) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarAvances.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarAvancesController controladorVistaAvances = accesoControlador.getController();
            controladorVistaAvances.setCursoAcademico(curso, profesor);
            controladorVistaAvances.setIdCurso(curso.getIdCurso());
            Stage escenario = (Stage) vbxContenedorHbxTarjetas.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Avances de estudiantes");
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
            Stage escenario = (Stage) vbxContenedorHbxTarjetas.getScene().getWindow();
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
