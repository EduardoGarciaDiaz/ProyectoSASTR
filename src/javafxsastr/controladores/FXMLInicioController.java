/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 13/05/2023
 * Descripción: Controlador de FXMLInicioController.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionClicBotonInicio;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.CuerpoAcademicoDAO;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.ConstructorInicio;
import javafxsastr.utils.Utilidades;

public class FXMLInicioController implements Initializable, INotificacionClicBotonInicio {

    @FXML
    private ImageView btnMenu;
    @FXML
    private AnchorPane menuLateral;
    @FXML
    private AnchorPane menuContraido;
    @FXML
    private VBox vbxMenuContraido;
    @FXML
    private VBox vbxMenuDesplegado;
    @FXML
    private VBox vbxCards;
    private Usuario usuario;
    private Academico academico = null;
    private Estudiante estudiante = null;
    @FXML
    private Pane pbnBotonCrearAnteproyecto;
    @FXML
    private Label lbTituloVentana;
    @FXML
    private Pane paneCerrarSesion;
    @FXML
    private ImageView imvDesplegarCerrarSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            prepararAnimacionMenu();
            prepararAnimacionBtnCerrarSesion();
    } 
    
    public void setUsuario(Usuario usuario) {
        try {
            this.usuario = usuario;
            Academico academico = new AcademicoDAO().obtenerAcademicoPorIdUsuario(this.usuario.getIdUsuario());
            if (academico.getIdAcademico() > 0) {
                this.academico = academico;
                prepararVistaParaAcademico(this.academico);
            }
            Estudiante estudiante = new EstudianteDAO().obtenerEstudiantePorIdUsuario(this.usuario.getIdUsuario());
            if (estudiante.getIdEstudiante() > 0) {
                this.estudiante = estudiante;
                if (estudiante.getIdAnteproyecto() > 0) {
                    crearVistaEstudiante();
                }
            }
            if (this.usuario.getEsAdministrador()) {
                if (this.academico != null) {
                    this.academico.setEsAdministrador(true);
                }
                if (this.estudiante!= null) {
                    this.estudiante.setEsAdministrador(true);
                }
                ConstructorInicio.crearPantallaInicio(this)
                        .cargarBotonIconoGestionUsuarios(vbxMenuContraido)
                        .cargarBotonIconoGestionCA(vbxMenuContraido)
                        .cargarBotonIconoGestionCursos(vbxMenuContraido)
                        .cargarBotonTextoGestionUsuarios(vbxMenuDesplegado)
                        .cargarBotonTextoGestionCA(vbxMenuDesplegado)
                        .cargarBotonTextoGestionCursos(vbxMenuDesplegado);
            }
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void crearVistaEstudiante() {
        ConstructorInicio.crearPantallaInicio(this)
                .cargarBotonIconoCronograma(vbxMenuContraido)
                .cargarBotonTextoCronograma(vbxMenuDesplegado)
                .cargarBotonIconoMiAnteproyecto(vbxMenuContraido)
                .cargarBotonTextoMiAnteproyecto(vbxMenuDesplegado);
    }
      
    public void prepararVistaParaAcademico(Academico academico) {
        try {
            ConstructorInicio constructorInicio = new ConstructorInicio().crearPantallaInicio(this);
            boolean esProfesor = new CursoDAO().verificarSiAcademicoImparteCurso(academico.getIdAcademico());
            System.out.println(esProfesor);
            if (esProfesor) {
                constructorInicio.cargarBotonIconoCurso(vbxMenuContraido)
                        .cargarBotonTextoCursos(vbxMenuDesplegado);
            }
            
            boolean esResponsableDeCA = new CuerpoAcademicoDAO().verificarSiAcademicoEsResponsableDeCA(academico.getIdAcademico());
            if (esResponsableDeCA) {
                constructorInicio.cargarBotonIconoAnteproyectosRCA(vbxMenuContraido)
                        .cargarBotonTextoAnteproyectosRCA(vbxMenuDesplegado);
            }
            
            boolean esDirector = new AnteproyectoDAO().verificarSiAcademicoEsDirector(academico.getIdAcademico());
            if (esDirector) {
                constructorInicio
                        .cargarBotonIconoEstudiantes(vbxMenuContraido)
                        .cargarBotonTextoEstudiantes(vbxMenuDesplegado)
                        .cargarBotonIconoAnteproyecto(vbxMenuContraido)
                        .cargarBotonTextoAnteproyectos(vbxMenuDesplegado);
            }
            pbnBotonCrearAnteproyecto.setVisible(true);
        } catch (DAOException ex) {
            Logger.getLogger(FXMLInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void prepararAnimacionMenu() {
        menuContraido.toFront();
        TranslateTransition menuDesplegado = new TranslateTransition(new Duration(350.0), menuLateral);
        menuDesplegado.setToX(0);
        TranslateTransition menuCerrado = new TranslateTransition(new Duration(350.0), menuLateral);
        btnMenu.setOnMouseClicked((MouseEvent evt)->{
            if (menuLateral.getTranslateX() != 0) {
                menuDesplegado.play();
            }else{
                menuCerrado.setToX(- (menuLateral.getWidth()));
                menuCerrado.play();
            }
        });
    }
    
    @FXML
    private void clicCrearAnteproyecto(MouseEvent event) {
        irAVistaCrearAnteproyecto(academico);
    }
    
    private void irAVistaCrearAnteproyecto(Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioAnteproyecto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioAnteproyectoController controladorVistaCrearAnteproyecto = accesoControlador.getController();
            controladorVistaCrearAnteproyecto.setAcademico(academico);
            controladorVistaCrearAnteproyecto.inicializarInformacionFormulario(null,false);
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Anteproyectos");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaAnteproyectos(Academico academico, boolean esRCA) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAnteproyectos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAnteproyectosController controladorVistaAnteproyectos = accesoControlador.getController();
            controladorVistaAnteproyectos.setAcademico(academico, esRCA);
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Anteproyectos");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaUsuarios(Usuario usuario) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLUsuarios.fxml"));
            Parent vista = accesoControlador.load();
            FXMLUsuariosController controladorVistaUsuarios = accesoControlador.getController();
            controladorVistaUsuarios.setUsuario(usuario);
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Usuarios");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaCuerposAcademicos(Usuario usuario) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCuerposAcademicos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCuerposAcademicosController controladorVistaCA = accesoControlador.getController();
            controladorVistaCA.setUsuario(usuario);
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Cuerpos academicos");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaActividades(Estudiante estudiante) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLActividades.fxml"));
            Parent vista = accesoControlador.load();
            FXMLActividadesController controladorVistaActividades = accesoControlador.getController();
            controladorVistaActividades.setEstudiante(this.estudiante);
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Actividades");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void irAVistaCursos() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCursosProfesor.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCursosProfesorController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setProfesor(academico);
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Cursos del profesor");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaGestionCursos() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCursos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCursosController controladorVistaCuesos = accesoControlador.getController();
            controladorVistaCuesos.setUsuario(academico);
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Cursos");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaEstudiantesAsignados() {
        try {
            FXMLLoader accesoControlador 
                    = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLEstudiantesAsignados.fxml"));
            Parent vista = accesoControlador.load();
            FXMLEstudiantesAsignadosController controladorVistaEstudiantes = accesoControlador.getController();
            controladorVistaEstudiantes.setDirector(academico);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Estudiantes");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaDetallesAnteproyecto(Anteproyecto anteproyecto) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesAnteproyecto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesAnteproyectoController controladorDetallesAnteproyecto = accesoControlador.getController();
            controladorDetallesAnteproyecto.setAnteproyecto(anteproyecto);
            controladorDetallesAnteproyecto.setEstudiante(estudiante, CodigosVentanas.INICIO);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Detalles Anteproyecto");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notificarClicBotonUsuarios() {
        irAVistaUsuarios(usuario);
    }

    @Override
    public void notificarClicBotonAnteproyectos() {
        irAVistaAnteproyectos(academico, false);
    }

    @Override
    public void notificarClicBotonAprobarAnteproyectos() {
        irAVistaAnteproyectos(academico, true);
    }

    @Override
    public void notificarClicBotonGestionCA() {
        irAVistaCuerposAcademicos(usuario);
    }

    @Override
    public void notificarClicBotonActividades() {
        irAVistaActividades(estudiante);
    }

    @Override
    public void notificarClicBotonCursos() {
        irAVistaCursos();
    }
    
    @Override
    public void notificarClicBotonGestionCursos() {
        irAVistaGestionCursos();
    }
    
    @Override
    public void notificarClicBotonEstudiantes() {
        irAVistaEstudiantesAsignados();
    }
    
    @Override
    public void notificarClicBotonMiAnteproyecto() {
        try {
            Anteproyecto anteproyecto
                    = new AnteproyectoDAO().obtenerAnteproyectosPorEstudiante(estudiante.getIdEstudiante());
            irAVistaDetallesAnteproyecto(anteproyecto);
        } catch (DAOException ex) {
            manejarDAOException(ex);
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
    
    private void prepararAnimacionBtnCerrarSesion() {
        TranslateTransition menuDesplegado = new TranslateTransition(new Duration(350.0), paneCerrarSesion);
        menuDesplegado.setToX(0);
        TranslateTransition menuCerrado = new TranslateTransition(new Duration(350.0), paneCerrarSesion);
        imvDesplegarCerrarSesion.setOnMouseClicked((MouseEvent evt)->{
            if (paneCerrarSesion.getTranslateX() != 0) {
                menuDesplegado.play();
            }else{
                menuCerrado.setToX(345);
                menuCerrado.play();
            }
        });
    }

    @FXML
    private void clicCerrarSesion(MouseEvent event) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicioSesion.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInicioSesionController controladorVistaInicioSesion = accesoControlador.getController();
            Stage escenario = (Stage) menuContraido.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Iniciar sesion");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicDesplegarCerrarSesion(MouseEvent event) {
    }
        
}
