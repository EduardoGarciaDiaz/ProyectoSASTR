/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 03/06/2023
 * Descripción: Controller de la ventana de ver detalles de curso.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionRecargarDatos;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.PeriodoEscolarDAO;
import javafxsastr.modelos.dao.UsuarioDAO;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.PeriodoEscolar;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAgregarAlumno;
import javafxsastr.utils.cards.TarjetaEstudianteCurso;

public class FXMLDetallesCursoController implements Initializable, INotificacionRecargarDatos {

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
    @FXML
    private VBox hbxAlmacenEstdantes;
    @FXML
    private ImageView imvActivar;
    @FXML
    private ImageView btnEditarCurso;
     
    private final int INACTIVO = 2;
    private final int ACTIVO = 1;
    private Usuario usuario;
    private ObservableList<Estudiante> estudiantes;
    private Curso cursoActual;
    private PeriodoEscolar periodoEscolar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
    }
    
    public void setUsuarioYCurso(Usuario usuario, Curso curso) {
        this.usuario = usuario;
        this.cursoActual = curso;
        setLogoActivo();
        if (cursoActual != null) {
            obtenerPeriodoEscolar();
        } 
        cargarDetallesCurso();
        obtenerEstudiantesCurso();
        llenarEstudiantes();
    }
    
    private void editarUsuario(Estudiante estudiante) {         
        irVistaEditarEstudiante(estudiante);
    }
    
    private void agregarAlumno() {
       irVistaAgregarAlumno();
    }
    
    private void obtenerPeriodoEscolar() {
        try {
            periodoEscolar = new PeriodoEscolarDAO().obtenerPeriodoPorId(cursoActual.getIdPeriodoEscolar());
            if (periodoEscolar != null) {
                if (!periodoEscolar.esActual()) {
                    btnEditarCurso.setDisable(true);
                    btnEditarCurso.setVisible(false);
                }
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
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
        lbDocente.setText(cursoActual.getAcademicoCurso());
        lbPeriodoEsc.setText(periodoEscolar.toString());
        lbBloque.setText(cursoActual.getBloqueCurso());
        lbNrc.setText(cursoActual.getNrcCurso());
        lbSeccion.setText(cursoActual.getSeccionCurso());
    }    
    
    private void recargarCurso() {
        try {
            cursoActual = new CursoDAO().obtenerCurso(cursoActual.getIdCurso());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        cargarDetallesCurso();
    }    
    
    private void obtenerEstudiantesCurso() {        
        estudiantes = FXCollections.observableArrayList(); 
        if (cursoActual != null) {
            int idCurso = cursoActual.getIdCurso();
            try {
                estudiantes.addAll(new EstudianteDAO().obtenerEstudiantesPorIdCurso(idCurso));
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }        
    }
    
    private void llenarEstudiantes() {
        hbxAlmacenEstdantes.setSpacing(20);   
        int numeroEstudiantes = estudiantes.size();       
        int elementosPorFila = 4;  
        int numFilas = Math.round(numeroEstudiantes/elementosPorFila) + 1;    
        int contadorGeneral = 0;
        for (int i = 0; (i < numFilas && numeroEstudiantes != contadorGeneral) || i == 0; i++) {
            HBox fila = new HBox();           
            fila.setSpacing(60);
            if (i==0){
                TarjetaAgregarAlumno tarjetaAgregarAlumno = new TarjetaAgregarAlumno();
                fila.getChildren().add(tarjetaAgregarAlumno);
                tarjetaAgregarAlumno.setOnMouseClicked(
                    (MouseEvent event) -> {
                        if (cursoActual.getIdEstadoCurso() == 2) {
                            Utilidades.mostrarDialogoSimple("Accion invalida","No puedes asignar estudiantes a un curso inactivo",
                                    Alert.AlertType.WARNING);
                        } else {
                            irVistaAgregarAlumno();
                        }
                    }
                );
            }           
            for (int j = 0; j < elementosPorFila && numeroEstudiantes != contadorGeneral && estudiantes.size() != 0; j++) {
                TarjetaEstudianteCurso tarjeta = new TarjetaEstudianteCurso(estudiantes.get(contadorGeneral));
                tarjeta.getBotonDesactivar().setOnMouseClicked(
                    (MouseEvent event) -> {
                        if (tarjeta.getEstudiante().getIdEstadoUsuario() == 1) {
                            desactivarUsuario(tarjeta.getEstudiante());

                        } else {
                            activarUsuario(tarjeta.getEstudiante());
                        }
                    }
                );
                tarjeta.getBotonEditar().setOnMouseClicked((
                    MouseEvent event) -> {
                        editarUsuario(tarjeta.getEstudiante());
                    }
                );                 
                fila.getChildren().add(tarjeta);
                contadorGeneral++;                          
            }
            hbxAlmacenEstdantes.getChildren().add(fila);
        }
    }
    
    private void desactivarUsuario(Estudiante estudiante) {
        if (Utilidades.mostrarDialogoConfirmacion("Desactivar Estudiante de curso",
                "¿Estas seguro que deseas desactivar al estudiante del curso?")) {
            try {
                Usuario usuario = new UsuarioDAO().obtenerUsuarioPorId(estudiante.getIdUsuario());
                usuario.setIdEstadoUsuario(INACTIVO);
                new UsuarioDAO().actualizarUsuario(usuario);
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
        iniciarVentana();
    }
    
    private void activarUsuario(Estudiante estudiante) {
        if (Utilidades.mostrarDialogoConfirmacion("Activar Estudiante de curso",
                "¿Estas seguro que deseas activar al estudiante del curso?")) {
            try {
                Usuario usuario = new UsuarioDAO().obtenerUsuarioPorId(estudiante.getIdUsuario());
                usuario.setIdEstadoUsuario(ACTIVO);
                new UsuarioDAO().actualizarUsuario(usuario);
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
        iniciarVentana();
    }
    
    private void desactivarCurso(Curso curso) {        
        try {
            curso.setIdEstadoCurso(INACTIVO);
            int cursoEdicion = new CursoDAO().actualizarCurso(curso);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        recargarCurso();
    }
    
    private void activarCurso(Curso curso) {        
        try {
            curso.setIdEstadoCurso(ACTIVO);
            int cursoEdicion = new CursoDAO().actualizarCurso(curso);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        recargarCurso();
    }
     
    private void setLogoActivo() {
        if (cursoActual.getIdEstadoCurso()== 1) {
           imvActivar.setImage(new Image("file:src/javafxsastr/recursos/iconos/desactivarCurso.jpg")); 
           crlEstadoCurso.setFill(Color.web("#C3E0BE"));
        } else {
            imvActivar.setImage(new Image("file:src/javafxsastr/recursos/iconos/activarEstudiante.png")); 
            crlEstadoCurso.setFill(Color.web("#EBE555"));
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
            Utilidades.mostrarDialogoSimple("Error", "No se pudo cargar la Escena", Alert.AlertType.ERROR);
        }        
    }
    
    private void irVistaEditarEstudiante(Estudiante estudiante) {
         try {
            Usuario usuario = new UsuarioDAO().obtenerUsuarioPorId(estudiante.getIdUsuario());   
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioUsuario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioUsuarioController controladorVistaFormularioUsuario = accesoControlador.getController();   
            controladorVistaFormularioUsuario.vieneDeVentanaDetallesCurso(true, this);
            controladorVistaFormularioUsuario.inicializarInformacionFormulario(true, usuario);
            controladorVistaFormularioUsuario.setUsuario(this.usuario);
            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Modificar Usuario");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("Error", "No se pudo cargar la Escena", Alert.AlertType.ERROR);
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
        irAVistaEditarCurso(); 
    }
    
    @FXML
    private void clicDesactivarCurso(MouseEvent event) {
        if (cursoActual.getIdEstadoCurso() == 1) {
            if (Utilidades.mostrarDialogoConfirmacion("Desactivar curso",
                "¿Estas seguro que deseas desactivar el curso?")) {
                      desactivarCurso(cursoActual);
            }          
        } else {
            if (Utilidades.mostrarDialogoConfirmacion("Activar curso",
                "¿Estas seguro que deseas activar el curso?")) {
                      activarCurso(cursoActual);
            }         
        }
        setLogoActivo();
    }
    
    private void irAVistaEditarCurso() {
        try {              
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioCurso.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioCursoController controladorVistaFormularioCurso = accesoControlador.getController();  
            controladorVistaFormularioCurso.setUsuario(usuario);
            controladorVistaFormularioCurso.inicializarInformacionFormulario(true, cursoActual,this);
            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Modificar Curso");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("Error", "No se pudo cargar la Escena", Alert.AlertType.ERROR);
        }
        iniciarVentana();
    }
    
    private void irAVistaCursos() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCursos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCursosController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setUsuario(usuario);
            Stage escenario = (Stage) lbBloque.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Cursos");
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("Error", "No se pudo cargar la Escena", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificacionRecargarDatos() {
       iniciarVentana();
    }

    @Override
    public void notificacionRecargarDatosPorEdicion(boolean fueEditado) {
       if (fueEditado) {
           recargarCurso();
       }
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