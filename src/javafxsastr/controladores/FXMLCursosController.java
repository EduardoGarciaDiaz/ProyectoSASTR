/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 05/06/2023
 * Descripción: Controla la vista en donde se visualizan los cursos
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaCurso;

public class FXMLCursosController implements Initializable {

    @FXML
    private TextField tfCampoBusqueda;
    @FXML
    private Label lbTituloVentana;
    @FXML
    private VBox vbxCursos;
    @FXML
    private Button btnTodos;
    @FXML
    private Button btnProyectoGuiado;
    @FXML
    private Button btnExperienciaRecepcional;
    
    private ObservableList<Curso> cursos ;
    private Usuario usuario;
    private Curso cursoConsutla;
    private final String ESTILO_SELECCIONADO = "-fx-background-color: ACACAC;"
                                                + "-fx-background-radius: 15;";
    private final String ESTILO_NORMAL = "-fx-background-color: C9C9C9;"
                                                + "-fx-background-radius: 15;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        obtenerCursos();
    }
    
    public void obtenerCursos() {
        try {
            cursos = FXCollections.observableArrayList(new CursoDAO().obtenerCursos());
            configurarBusqueda(cursos);
            mostrarCursos(cursos);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void mostrarCursos(ObservableList<Curso> cursos) {
        for (Curso curso : cursos) {
            TarjetaCurso tarjeta = new TarjetaCurso(curso);
            tarjeta.getBotonVerDetalles().setOnAction(
                (event) -> {
                    cursoConsutla = curso;
                    irAVistaDetallesCurso();
                }
            );
            vbxCursos.getChildren().add(tarjeta);
        }
    }
    
    private void configurarFiltroBusqueda(String nombreExperiencia) {
        vbxCursos.getChildren().clear();
        if (cursos.size() > 0) {
            FilteredList<Curso> filtroCursos = new FilteredList<>(cursos, p -> true);
            filtroCursos.setPredicate(curso -> {
                if (nombreExperiencia.equals(curso.getExperienciaEducativaCurso())) {   
                    return true;
                }
                return false;
            });
            SortedList<Curso> sortedList = new SortedList<>(filtroCursos,
                    Comparator.comparing(Curso::getNombreCurso));
            mostrarCursos(sortedList);
            configurarBusqueda(sortedList);
        }
    }
    
    private void configurarBusqueda(ObservableList<Curso> cursos) {
        if (cursos.size() > 0) {
            FilteredList<Curso> filtroCursos = new FilteredList(cursos, p -> true );
            tfCampoBusqueda.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    vbxCursos.getChildren().clear();
                    filtroCursos.setPredicate(cursoFiltro -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerNewValue = newValue.toLowerCase();
                        if (cursoFiltro.getNombreCurso().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        } else if (cursoFiltro.getAcademicoCurso().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        } else if (cursoFiltro.getNrcCurso().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        return false;
                    });
                    SortedList<Curso> sortedListCursos = new SortedList<>(filtroCursos,
                        Comparator.comparing(Curso::getNombreCurso));
                    mostrarCursos(sortedListCursos);
                }
            });
        }
    }

    @FXML
    private void filtrarCursos(KeyEvent event) {
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaInicio(usuario);
    }

    @FXML
    private void clicTodos(ActionEvent event) {
        vbxCursos.getChildren().clear();
        btnTodos.setStyle(ESTILO_SELECCIONADO);
        btnProyectoGuiado.setStyle(ESTILO_NORMAL);
        btnExperienciaRecepcional.setStyle(ESTILO_NORMAL);
        FilteredList<Curso> filtroCursos = new FilteredList<>(cursos, p -> true);
        filtroCursos.setPredicate(curso -> {
            return true;
        });
        SortedList<Curso> sortedList = new SortedList<>(filtroCursos,
                Comparator.comparing(Curso::getNombreCurso));
        mostrarCursos(sortedList);
        configurarBusqueda(sortedList);
    }

    @FXML
    private void clicProyectoGuiado(ActionEvent event) {
        btnTodos.setStyle(ESTILO_NORMAL);
        btnProyectoGuiado.setStyle(ESTILO_SELECCIONADO);
        btnExperienciaRecepcional.setStyle(ESTILO_NORMAL);
        configurarFiltroBusqueda("Proyecto Guiado");
    }

    @FXML
    private void clicExperienciaRecepcional(ActionEvent event) {
        btnTodos.setStyle(ESTILO_NORMAL);
        btnProyectoGuiado.setStyle(ESTILO_NORMAL);
        btnExperienciaRecepcional.setStyle(ESTILO_SELECCIONADO);
        configurarFiltroBusqueda("Experiencia Recepcional");
    }

    @FXML
    private void clicAnadirCurso(ActionEvent event) {
        irAVistaFormularioController();
    }
    
    private void irAVistaFormularioController() {
        try {          
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioCurso.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioCursoController controladorVistaFormularioCurso = accesoControlador.getController();
            controladorVistaFormularioCurso.setUsuario(usuario);
            controladorVistaFormularioCurso.inicializarInformacionFormulario(false, null, null);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Formulario Cursos");
            escenario.show();            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaInicio(Usuario usuario) {
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
    
    private void irAVistaDetallesCurso() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesCurso.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesCursoController controladorDetallesCurso = accesoControlador.getController();
            controladorDetallesCurso.setUsuarioYCurso(usuario, cursoConsutla);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));            
            escenario.setTitle("Detalles curso");
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
                break;
            default:
                throw new AssertionError();
        }
    }
    
}