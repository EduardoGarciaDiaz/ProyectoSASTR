/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Controller de la ventana AñadirCuerpoAcademico y modificar.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAgregarAlumno;
import javafxsastr.utils.cards.TarjetaEstudianteCurso;

/**
 * FXML Controller class
 *
 * @author tristan
 */
public class FXMLDetallesCursoController implements Initializable {

    @FXML
    private AnchorPane menuContraido;
    @FXML
    private Label lbTituloventana;
    @FXML
    private Circle crlEstadoCurso;
    @FXML
    private Label lbNombreCurso;
    @FXML
    private Label lbExpEdu;
    @FXML
    private Label lbPeriodoEsc;
    @FXML
    private Label lbDocente;
    @FXML
    private Label lbBloque;
    @FXML
    private Label lbNrc;
    @FXML
    private Label lbSeccion;
    private FlowPane fwpAlmacenAlumnos;
    @FXML
    private ScrollPane slpAlamcenEstudinates;
    @FXML
    private VBox hbxAlmacenEstdantes;
    
    private Academico academico;
    private ObservableList<Estudiante> estudiantes;
    private Curso cursoActual;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obtenerDatosCurso();
        obtenerEstudiantesCurso();
        llenarEstudiantes();
    }
    
    public void setUsuario(Academico academico) {
        this.academico = academico;
    }
    
    public void desactivarUsuario(int idUsuario) {
          Utilidades.mostrarDialogoConfirmacion("Desactivar Estudiante de curso",
                "¿Estas seguro que deseas desactivar al estudinate del curso?");
    }
    
    public void editarUsuario(int idUsuario) {
          Utilidades.mostrarDialogoConfirmacion("Editar Estudiante de curso",
                "¡Empieza edicion!");
        /////INICIA EDITAR Usuario
       /* Stage escenarioNuevo = new Stage();
        Scene escenaNueva = Utilidades.inicializarEscena("vistas/FXML.fxml");
        escenarioNuevo.setScene(escenaNueva);
        escenarioNuevo.setTitle("Modificar Uusuario");
        escenarioNuevo.initModality(Modality.APPLICATION_MODAL);
        escenarioNuevo.show();*/
        
    }
    public void agregarAlumno() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAsignarEstudianteCurso.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAsignarEstudianteCursoController accion = accesoControlador.getController();
            accion.iniciarEstudiantes(estudiantes, cursoActual.getIdCurso());
            Stage escenarioNuevo = new Stage();
            Scene escenaNueva = Utilidades.inicializarEscena("vistas/FXMLAsignarEstudianteCurso.fxml");
            escenarioNuevo.setScene(escenaNueva);
            escenarioNuevo.setTitle("Modificar Uusuario");
            escenarioNuevo.initModality(Modality.APPLICATION_MODAL);
            escenarioNuevo.show();
        } catch (IOException ex) {
           Utilidades.mostrarDialogoSimple("Error","No sepudo caragar la Escena", Alert.AlertType.ERROR);
        }
        
    }
    
    private void cargarDetallesCurso() {
        lbNombreCurso.setText(cursoActual.getNombreCurso());
        lbExpEdu.setText(cursoActual.getExperienciaEducativaCurso());
        lbPeriodoEsc.setText(cursoActual.getFechaInicioCurso() + "-" + cursoActual.getFinPeriodoEscolar());
        lbDocente.setText(cursoActual.getAcademicoCurso());
        lbBloque.setText(cursoActual.getBloqueCurso());
        lbNrc.setText(cursoActual.getNrcCurso());
        lbSeccion.setText(cursoActual.getSeccionCurso());
    }
    
    private void obtenerDatosCurso(/*Curso curso*/) {
        try {
            cursoActual = new CursoDAO().obtenerCurso(1);  //OBTEENR CURSO POR ID,RECIBIRLOD EUNA PANTALLA ANTERIOR          
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        cargarDetallesCurso();
    }
    
    private void obtenerEstudiantesCurso() {
        estudiantes = FXCollections.observableArrayList();
        try {
            estudiantes.addAll(new EstudianteDAO().obtenerEstudiantesPorIdCurso(cursoActual.getIdCurso()));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void llenarEstudiantes() {
        hbxAlmacenEstdantes.setSpacing(20);   
        hbxAlmacenEstdantes.setPadding(new Insets(5));
        int numFilas = 3;   //Math.round(estudiantes.size()/3);
        int elementosPorFila = 5;       
        for (int i = 0; i < numFilas; i++) {
            HBox fila = new HBox();           
            fila.setSpacing(60);
            for (int j = 0; j < elementosPorFila; j++) {
                if(j==0 && i == 0) {
                    fila.getChildren().add(new TarjetaAgregarAlumno());
                }else {    
                    fila.getChildren().add(new TarjetaEstudianteCurso("Juan Peres De Girjalva", "juan@homtail.com", i+j));
                }             
            }

            hbxAlmacenEstdantes.getChildren().add(fila);
        }
    }
    
    
    private void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                System.out.println("Ocurrió un error de consulta.");
                ex.printStackTrace();
                break;
            case ERROR_CONEXION_BD:
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", 
                        Alert.AlertType.ERROR);
            default:
                throw new AssertionError();
        }
    }
    
    public void cerrarVentana() {
        Stage escenarioActual = (Stage) lbBloque.getScene().getWindow();
        escenarioActual.close();
    }

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        irAVistaCursos();
    }

    @FXML
    private void clicEditarCurso(MouseEvent event) {
          Utilidades.mostrarDialogoConfirmacion("Editar  curso",
                "¡Empieza edicion!");
        /////INICIA EDITAR CURSO
       /* Stage escenarioNuevo = new Stage();
        Scene escenaNueva = Utilidades.inicializarEscena("vistas/FXML.fxml");
        escenarioNuevo.setScene(escenaNueva);
        escenarioNuevo.setTitle("Añadir Usuario");
        escenarioNuevo.initModality(Modality.APPLICATION_MODAL);
        escenarioNuevo.show();*/
    }

    @FXML
    private void clicDesactivarCurso(MouseEvent event) {
        Utilidades.mostrarDialogoConfirmacion("Desactivar Estudiante de curso",
                "¡Estas seguro que deseas desactivar el curso!");
    }
    
    private void irAVistaCursos() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCursos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCursosController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setUsuario(academico);
            Stage escenario = (Stage) lbBloque.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Usuarios");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
