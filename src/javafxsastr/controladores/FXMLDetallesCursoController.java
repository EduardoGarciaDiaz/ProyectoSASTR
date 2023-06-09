/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Controller de la ventana de ver detalles de curso.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionRecargarDatos;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.UsuarioDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAgregarAlumno;
import javafxsastr.utils.cards.TarjetaEstudianteCurso;

public class FXMLDetallesCursoController implements Initializable, INotificacionRecargarDatos {

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
     
    private final int DESACTIVAR_USUSARIO = 2;
    private final int ACTIVAR_USUSARIO = 1;
    private Academico academico;
    private ObservableList<Estudiante> estudiantes;
    private Curso cursoActual;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
    }
    
    public void setUsuarioYCurso(Academico academicoN, Curso curso) {
        this.academico = academicoN;
        this.cursoActual = curso;
        cargarDetallesCurso();
        obtenerEstudiantesCurso();
        llenarEstudiantes();
    }
    
    public void desactivarUsuario(Estudiante estudiante) {
        if(Utilidades.mostrarDialogoConfirmacion("Desactivar Estudiante de curso",
                "¿Estas seguro que deseas desactivar al estudiante del curso?")) {
            try {
                Usuario usuario = new UsuarioDAO().obtenerUsuarioPorId(estudiante.getIdUsuario());
                usuario.setIdEstadoUsuario(DESACTIVAR_USUSARIO);
                new UsuarioDAO().actualizarUsuario(usuario);
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
        iniciarVentana();
    }
    
     public void activarUsuario(Estudiante estudiante) {
        if(Utilidades.mostrarDialogoConfirmacion("Activar Estudiante de curso",
                "¿Estas seguro que deseas activar al estudiante del curso?")) {
            try {
                Usuario usuario = new UsuarioDAO().obtenerUsuarioPorId(estudiante.getIdUsuario());
                usuario.setIdEstadoUsuario(ACTIVAR_USUSARIO);
                new UsuarioDAO().actualizarUsuario(usuario);
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
        iniciarVentana();
    }
    
    public void editarUsuario(Estudiante estudiante) {         
        irVistaEditarEstudiante(estudiante);
    }
    
    public void agregarAlumno() {
       irVistaAgregarAlumno();
    }
    
    private void iniciarVentana(){     
        hbxAlmacenEstdantes.getChildren().clear();
        estudiantes.clear();
        obtenerEstudiantesCurso();
        llenarEstudiantes();     
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
    
    
    private void obtenerEstudiantesCurso() {        
        estudiantes = FXCollections.observableArrayList(); 
        if(cursoActual != null) {
            int idCurso = cursoActual.getIdCurso();
        try {
            estudiantes.addAll(new EstudianteDAO().obtenerEstudiantesPorIdCurso(idCurso));
        } catch (DAOException ex) {
            ex.printStackTrace();
            manejarDAOException(ex);
        }
        }        
    }
    
    private void llenarEstudiantes() {
        hbxAlmacenEstdantes.setSpacing(20);   
        hbxAlmacenEstdantes.setPadding(new Insets(5));
        int numeroEstudiantes = estudiantes.size();       
        int elementosPorFila = 5;  
        int numFilas = Math.round(numeroEstudiantes/elementosPorFila) + 1;    
        int contadorGeneral = 0;
        for (int i = 0; (i < numFilas && numeroEstudiantes != contadorGeneral) || i == 0; i++) {
            HBox fila = new HBox();           
            fila.setSpacing(60);
            if(i==0){
                TarjetaAgregarAlumno tarjetaAgregarAlumno = new TarjetaAgregarAlumno();
                fila.getChildren().add(tarjetaAgregarAlumno);
                tarjetaAgregarAlumno.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {  
                        System.out.println("Clic agregar");
                        irVistaAgregarAlumno();
                    }
                });
            }           
            for (int j = 0; j < elementosPorFila && numeroEstudiantes != contadorGeneral && estudiantes.size() != 0; j++) {
                TarjetaEstudianteCurso tarjeta = new TarjetaEstudianteCurso(estudiantes.get(contadorGeneral));
                tarjeta.getBotonDesactivar().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                            if(tarjeta.getEstudiante().getIdEstadoUsuario() == 1) {                                    
                                desactivarUsuario(tarjeta.getEstudiante());

                            }else {                            
                                 activarUsuario(tarjeta.getEstudiante());
                            }                                
                        }
                    });
                tarjeta.getBotonEditar().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                            editarUsuario(tarjeta.getEstudiante());
                        }
                    });                 
                fila.getChildren().add(tarjeta);
                contadorGeneral++;                          
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
    
    private void cerrarVentana() {
        Stage escenarioActual = (Stage) lbBloque.getScene().getWindow();
        escenarioActual.close();
    }
    
    private void irVistaAgregarAlumno() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAsignarEstudianteCurso.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAsignarEstudianteCursoController controladorVistaAsignarEstudianteCurso = accesoControlador.getController();
            controladorVistaAsignarEstudianteCurso.iniciarEstudiantes(estudiantes, cursoActual, this);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene(vista));
            escenarioFormulario.setTitle("Añadir estudiantes");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarDialogoSimple("Error","No se pudo cargar la Escena", Alert.AlertType.ERROR);
        }        
    }
    
    private void irVistaEditarEstudiante(Estudiante estudiante) {
         try {
            Usuario usuario = new UsuarioDAO().obtenerUsuarioPorId(estudiante.getIdUsuario());   
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioUsuario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioUsuarioController controladorVistaVerUsuario = accesoControlador.getController();     
            controladorVistaVerUsuario.inicializarInformacionFormulario(true, usuario);
            controladorVistaVerUsuario.vieneDeVentanaDetallesCurso(true, this);
            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Modificar Usuarios");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        iniciarVentana();
    }
    

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
        irAVistaCursos();
    }

    @FXML
    private void clicEditarCurso(MouseEvent event) {
        try {              
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioCurso.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioCursoController controladorVista = accesoControlador.getController();     
            controladorVista.esEdiconPorVentanaDetalles(this);
            controladorVista.inicializarInformacionFormulario(true, cursoActual);
            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Modificar Curso");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        iniciarVentana();
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

    @Override
    public void notitficacionRecargarDatos() {
       iniciarVentana();
    }
    
}
