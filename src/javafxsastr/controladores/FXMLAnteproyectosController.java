/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 04/06/2023
 * Descripción: La clase FXMLAnteproyecto actúa como controlador
 * de la vista de Anteproyectos. Incluye los métodos necesarios
 * para visualizar los anteproyectos y a partir de ciertos 
 * componentes realizar acciones.
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionClicBotonAnteproyectos;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.CuerpoAcademicoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstadoSeguimientoDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.CuerpoAcademico;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAnteproyecto;

public class FXMLAnteproyectosController implements Initializable, INotificacionClicBotonAnteproyectos {

    @FXML
    private VBox contenedorTarjetasAnteproyectos;
    private Academico academico;
    private ObservableList<Anteproyecto> anteproyectos;
    private boolean esInvitado = false;
    private CodigosVentanas ventanaOrigen;
    @FXML
    private TextField tfCampoBusqueda;
    @FXML
    private Label lbTituloVentana;
    private boolean esRCA;
    @FXML
    private Pane pnBotonCrearAnteproyecto;
    @FXML
    private Pane contenedorBotonesAcademico;
    @FXML
    private Pane contenedorBotonesRCA;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setInvitado(CodigosVentanas codigoVentana) {
        this.ventanaOrigen = codigoVentana;
        esInvitado=true;
        recuperarAnteproyectosPublicados();
        cargarTarjetasAnteproyectos(anteproyectos);
        pnBotonCrearAnteproyecto.setVisible(false);
    }
    
    public void setAcademico(Academico academico, boolean esRCA,CodigosVentanas codigoVentana) {        
        this.esRCA = esRCA;
        this.academico = academico;
        this.ventanaOrigen = codigoVentana;
        if (this.esRCA) {
            CuerpoAcademico cuerpoAcademico = recuperarCuerpoAcademicoDelRCA(academico.getIdAcademico());
            recuperarAnteproyectosDelCA(cuerpoAcademico.getIdCuerpoAcademico());
            pnBotonCrearAnteproyecto.setVisible(false);
            contenedorBotonesRCA.setVisible(true);
        } else {
            recuperarAnteproyectosDelAcademico(this.academico.getIdAcademico());
            contenedorBotonesAcademico.setVisible(true);
            pnBotonCrearAnteproyecto.setVisible(true);
        }
        cargarTarjetasAnteproyectos(anteproyectos);
        configurarBusqueda(anteproyectos);
    }
    
    public void recuperarAnteproyectosDelAcademico(int idAcademico) {
        try {
            anteproyectos = FXCollections.observableArrayList(
                new AnteproyectoDAO().obtenerAnteproyectosPorAcademico(idAcademico));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    public CuerpoAcademico recuperarCuerpoAcademicoDelRCA(int idAcademico) {
        CuerpoAcademico cuerpoAcademico = null;
        try {
            cuerpoAcademico = 
                    new CuerpoAcademicoDAO().obtenerCuerpoAcademicoPorResponsable(idAcademico);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return cuerpoAcademico;
    }
    
    public void recuperarAnteproyectosDelCA(int idCuerpoAcademico) {
        try {
            anteproyectos = FXCollections.observableArrayList(
                new AnteproyectoDAO().obtenerAnteproyectosPorCuerpoAcademico(idCuerpoAcademico));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    public void recuperarAnteproyectosPublicados() {
        try {
             int idEstadoSeguimiento = new EstadoSeguimientoDAO().obtenerIdEstadoSeguimiento("Publicado");
            anteproyectos = FXCollections.observableArrayList(
                new AnteproyectoDAO().obtenerAnteproyectosPorEstadoSeguimiento(idEstadoSeguimiento));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    public void cargarTarjetasAnteproyectos(ObservableList<Anteproyecto> anteproyectosCarga) {
        for (Anteproyecto anteproyecto : anteproyectosCarga) {
            TarjetaAnteproyecto tarjeta = new TarjetaAnteproyecto(anteproyecto, this);            
            contenedorTarjetasAnteproyectos.getChildren().add(tarjeta);
        }
    }
    
    private void configurarBusqueda(ObservableList<Anteproyecto> elementos) {
        if (elementos.size() > 0) {
            FilteredList<Anteproyecto> filtroAnteproyectos = new FilteredList(elementos, p -> true );
            tfCampoBusqueda.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    contenedorTarjetasAnteproyectos.getChildren().clear();
                    filtroAnteproyectos.setPredicate(anteproyectoFiltro -> {
                       if (newValue == null || newValue.isEmpty()) {
                           return true;
                       }
                       String lowerNewValue = newValue.toLowerCase();
                       if (anteproyectoFiltro.getNombreTrabajoRecepcional().toLowerCase().contains(lowerNewValue)) {
                           return true;
                       } else if (anteproyectoFiltro.getNombreProyectoInvestigacion().toLowerCase().contains(lowerNewValue)) {
                           return true;
                       }
                       return false;
                    });
                    SortedList<Anteproyecto> sortedListAlumnos = new SortedList<>(filtroAnteproyectos,
                     Comparator.comparing(Anteproyecto::getNombreTrabajoRecepcional));
                    cargarTarjetasAnteproyectos(sortedListAlumnos);
                }
            });
        }
    }

    @FXML
    private void clicBtnAsignados(ActionEvent event) {
        contenedorTarjetasAnteproyectos.getChildren().clear();
        if (anteproyectos.size() > 0) {
            FilteredList<Anteproyecto> filtroAnteproyectosAsignados = new FilteredList<>(anteproyectos, p -> true);
            filtroAnteproyectosAsignados.setPredicate(anteproyecto -> {
                try {
                    if (new EstudianteDAO().verificarSiAnteproyectoEstaAsignado(anteproyecto.getIdAnteproyecto())) {   
                        return true;
                    }
                } catch (DAOException ex) {
                    manejarDAOException(ex);
                }
                return false;
            });
            SortedList<Anteproyecto> sortedList = new SortedList<>(filtroAnteproyectosAsignados,
                    Comparator.comparing(Anteproyecto::getNombreTrabajoRecepcional));
            cargarTarjetasAnteproyectos(sortedList);
            configurarBusqueda(sortedList);
        }
    }

    @FXML
    private void clicBtnPorCorregir(ActionEvent event) {
        configurarFiltroBusqueda("Rechazado");
    }

    @FXML
    private void clicBtnBorradores(ActionEvent event) {
        configurarFiltroBusqueda("Borrador");
    }

    @FXML
    private void clicBtnNoAsignados(ActionEvent event) {
        contenedorTarjetasAnteproyectos.getChildren().clear();
        if (anteproyectos.size() > 0) {
            FilteredList<Anteproyecto> filtroAnteproyectosNoAsignados = new FilteredList<>(anteproyectos, p -> true);
            filtroAnteproyectosNoAsignados.setPredicate(anteproyecto -> {
                try {
                    if (new EstudianteDAO().verificarSiAnteproyectoEstaAsignado(anteproyecto.getIdAnteproyecto())) {   
                        return false;
                    }
                } catch (DAOException ex) {
                    manejarDAOException(ex);
                }
                return true;
            });
            SortedList<Anteproyecto> sortedList = new SortedList<>(filtroAnteproyectosNoAsignados,
                    Comparator.comparing(Anteproyecto::getNombreTrabajoRecepcional));
            cargarTarjetasAnteproyectos(sortedList);
            configurarBusqueda(sortedList);
        }
    }

    @FXML
    private void clicBtnTodos(ActionEvent event) {
        contenedorTarjetasAnteproyectos.getChildren().clear();
        cargarTarjetasAnteproyectos(anteproyectos);
        configurarBusqueda(anteproyectos);
    }

    @Override
    public void notificarClicBotonVerDetallesAnteproyecto(Anteproyecto anteproyecto) {
        irAVistaDetallesAnteproyecto(anteproyecto);
    }

    @Override
    public void notificarClicBotonModificarBorradorAnteproyecto(Anteproyecto anteproyecto) {
        irAvistaFormularioAnteproyecto(anteproyecto, true);
    }

    @Override
    public void notificarClicBotonCorregirAnteproyectoe(Anteproyecto anteproyecto) {
        
    }

    @Override
    public void notificarClicValidarAnteproyecto(Anteproyecto anteproyecto) {
        irAVistaValidarAnteproyecto(anteproyecto);
    }

    @Override
    public void notificarClicPublicarAnteproyecto(Anteproyecto anteproyecto) {
    }

    @FXML
    private void filtrarAnteproyectos(KeyEvent event) {
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
        if(esInvitado) {
            irAVistaIniciarSesion();
        }else {
            irAVistaInicio(academico);
        }        
    }
    
    private void irAVistaInicio(Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicio.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInicioController controladorVistaCrearAnteproyecto = accesoControlador.getController();
            controladorVistaCrearAnteproyecto.setUsuario(academico);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaIniciarSesion() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLInicioSesion.fxml"));
            Parent vista = accesoControlador.load();            
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio Sesion");
            escenario.show();            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAvistaFormularioAnteproyecto(Anteproyecto anteproyectoModificacion, boolean esEdicion) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioAnteproyecto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioAnteproyectoController controladorVistaCrearAnteproyecto = accesoControlador.getController();
            controladorVistaCrearAnteproyecto.setAcademico(academico);
            controladorVistaCrearAnteproyecto.inicializarInformacionFormulario(anteproyectoModificacion,esEdicion);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Anteproyectos");
            escenario.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicBtnConcluidos(ActionEvent event) {
        configurarFiltroBusqueda("Concluido");
    }

    @FXML
    private void clicBtnSinPublicar(ActionEvent event) {
        configurarFiltroBusqueda("Sin publicar");
    }

    @FXML
    private void clicBtnSinRevisar(ActionEvent event) {
        configurarFiltroBusqueda("Sin revisar");
    }
    
    private void configurarFiltroBusqueda(String estadoSeguimiento) {
        contenedorTarjetasAnteproyectos.getChildren().clear();
        if (anteproyectos.size() > 0) {
            FilteredList<Anteproyecto> filtroAnteproyectosNoAsignados = new FilteredList<>(anteproyectos, p -> true);
            filtroAnteproyectosNoAsignados.setPredicate(anteproyecto -> {
                if (estadoSeguimiento.equals(anteproyecto.getEstadoSeguimiento())) {   
                    return true;
                }
                return false;
            });
            SortedList<Anteproyecto> sortedList = new SortedList<>(filtroAnteproyectosNoAsignados,
                    Comparator.comparing(Anteproyecto::getNombreTrabajoRecepcional));
            cargarTarjetasAnteproyectos(sortedList);
            configurarBusqueda(sortedList);
        }
    }

    @FXML
    private void clicCrearAnteproyecto(MouseEvent event) {
        irAvistaFormularioAnteproyecto(null, false);
    }
    
    
    private void irAVistaDetallesAnteproyecto(Anteproyecto anteproyecto) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesAnteproyecto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesAnteproyectoController controladorDetallesAnteproyecto = accesoControlador.getController();        
            switch (ventanaOrigen) {
                case INICIO_SESION:
                    controladorDetallesAnteproyecto.setInvitado(true, CodigosVentanas.ANTEPROYECTOS_INVITADO);
                    break;
                case INICIO:
                    if (esRCA) {
                        controladorDetallesAnteproyecto.setAcademico(academico, CodigosVentanas.VALIDAR_ANTEPROYECTOS);
                    }else{                
                       controladorDetallesAnteproyecto.setAcademico(academico, CodigosVentanas.MIS_ANTEPROYECTOS);   
                    } 
                    break;
                default:
                    throw new AssertionError();
            }    
            controladorDetallesAnteproyecto.setAnteproyecto(anteproyecto);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Detalles Anteproyecto");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
     private void irAVistaValidarAnteproyecto(Anteproyecto anteproyecto) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLValidarAnteproyecto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLValidarAnteproyectoController controladorDetallesAnteproyecto = accesoControlador.getController();
            controladorDetallesAnteproyecto.setAnteproyectoAcademico(anteproyecto, academico);
            Stage escenario = (Stage) lbTituloVentana.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Validar Anteproyecto");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
