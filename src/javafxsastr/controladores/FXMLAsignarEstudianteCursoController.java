/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 04/06/2023
 * Descripción: Controller de la ventana Asignar estudinate Curso.
 */

package javafxsastr.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.interfaces.INotificacionRecargarDatos;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.CampoDeBusqueda;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAgregarEstudianteCurso;

public class FXMLAsignarEstudianteCursoController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txfEstudianteBusqueda;
    @FXML
    private ListView<Estudiante> lsvEstudiantesBusqueda;    
    @FXML
    private VBox vbxEstudiantesPorAgregar;
    @FXML
    private Button btnOtro;
    
    private final int ESTADO_DESACTIVADO = 2;
    private ObservableList<Estudiante> estudiantesDisponibles;
    private ObservableList<Estudiante> estudiantesActuales;
    private ObservableList<Estudiante> estudiantesTabla;
    private Estudiante estudianteSeleccionado;
    private Curso cursoActual ;
    private INotificacionRecargarDatos interfazNotificacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void iniciarEstudiantes(ObservableList<Estudiante> estudiantes, Curso curso, INotificacionRecargarDatos interfazNotificacionRecargarDatos) {
        interfazNotificacion = interfazNotificacionRecargarDatos;
        if (estudiantes != null) {
            estudiantesActuales = FXCollections.observableArrayList((estudiantes));                    
        } 
        recuperarEstudiantes();
        cursoActual = curso;    
        btnOtro.setDisable(true);
        btnGuardar.setDisable(true);
        estudiantesTabla = FXCollections.observableArrayList();
        iniciarListener();
    }
    
    private void iniciarListener() {
        CampoDeBusqueda<Estudiante> campoDeBusqueda = new CampoDeBusqueda<>(txfEstudianteBusqueda, lsvEstudiantesBusqueda,
            estudiantesDisponibles, estudianteSeleccionado, 
                new INotificacionSeleccionItem<Estudiante>() {            
                @Override
                public void notificarSeleccionItem(Estudiante itemSeleccionado) {
                    if(itemSeleccionado != null) {
                        estudianteSeleccionado = itemSeleccionado;
                         vbxEstudiantesPorAgregar.requestFocus();     
                    }                          
                }
                @Override
                public void notificarPerdidaDelFoco() {
                    if (estudianteSeleccionado != null) {
                        if (validarEstudianteEnCurso()) {                    
                        agregarATabla();
                        } else {
                            Utilidades.mostrarDialogoSimple("Accion invalida", "Este Estudiante ya pertenece a un curso",
                                Alert.AlertType.INFORMATION);                   
                        }
                        txfEstudianteBusqueda.setText("");
                    }                
                }         
            }
        );        
    }
    
    private void recuperarEstudiantes() {
        try {
            estudiantesDisponibles = FXCollections.observableArrayList((new EstudianteDAO().obtenerEstudiantes()));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private boolean validarEstudianteEnCurso() {
        CursoDAO  cursoDao= new CursoDAO();
        if (estudianteSeleccionado != null) {            
            for (Estudiante estudiantes : estudiantesActuales) {
                if (estudiantes.getIdEstudiante() == estudianteSeleccionado.getIdEstudiante()) {
                    return false;
                }
            }
            try {
                if (cursoDao.verificarSiEstudiantePerteneceACursoActivo(estudianteSeleccionado.getIdEstudiante())) {
                    return false;
                }
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
            if (estudianteSeleccionado.getIdEstadoUsuario() == ESTADO_DESACTIVADO) {
                    return false;
            }
            if (estudiantesTabla != null) {
                for (Estudiante estudiante1 : estudiantesTabla) {
                    if (estudiante1.getIdEstudiante() == estudianteSeleccionado.getIdEstudiante()) {
                        return false;
                    }
                }  
            } 
            return true;
        }
        return false;        
    }
    
    private void agregarATabla() {     
       estudiantesTabla.add(estudianteSeleccionado);
       vbxEstudiantesPorAgregar.setSpacing(40);      
       agregarEstudianteATabla(estudianteSeleccionado);           
       txfEstudianteBusqueda.setText("");
       txfEstudianteBusqueda.setDisable(true);
       btnOtro.setDisable(false);
       btnGuardar.setDisable(false);
    }
    
    private void agregarEstudianteATabla(Estudiante estudianteAgregar) {
        TarjetaAgregarEstudianteCurso tarjeta = new TarjetaAgregarEstudianteCurso(estudianteAgregar);
        tarjeta.getImagen().setOnMouseClicked(
            (MouseEvent event) -> {
                quitarEstudiante(tarjeta.getEstudiante());
            }
        );
        vbxEstudiantesPorAgregar.getChildren().add(tarjeta); 
    }
    
    private void quitarEstudiante(Estudiante estudianteParaQuitar) {
        for (Estudiante estudiante : estudiantesTabla) {
            if (estudiante.getIdEstudiante() == estudianteParaQuitar.getIdEstudiante()) {                
                estudiantesTabla.remove(estudiante);
                break;
            }
        }
        recargarVbox();        
    }
    
    private void recargarVbox() {
        vbxEstudiantesPorAgregar.getChildren().clear();
        if (estudiantesTabla.size() > 0) {
            for (Estudiante estudianteAgregar : estudiantesTabla) {
                agregarEstudianteATabla(estudianteAgregar);                   
            }
        } else {
            txfEstudianteBusqueda.setDisable(false);
            btnOtro.setDisable(true);
            btnGuardar.setDisable(true);
        }    
    }
    
    private void guardarEstudiantes() {
        for (Estudiante estudianteNuevo : estudiantesTabla) {
            try {                
                new CursoDAO().guardarRelacionCursoEstudiante(cursoActual.getIdCurso(),
                        estudianteNuevo.getIdEstudiante());
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
        Utilidades.mostrarDialogoSimple("Registro Exitoso","Se guardaron los estudiantes", 
                Alert.AlertType.INFORMATION);       
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage escenarioActual = (Stage) txfEstudianteBusqueda.getScene().getWindow();  
        interfazNotificacion.notificacionRecargarDatos();
        escenarioActual.close();
    }    
    
    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        if  (Utilidades.mostrarDialogoConfirmacion("¡Cuidado!",
                "¿Estás seguro de que deseas cancelar la adición de un estudiante a un curso?" )) {
            cerrarVentana(); 
        }               
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        guardarEstudiantes();
    }

    @FXML
    private void clicBtnOtro(ActionEvent event) {
       txfEstudianteBusqueda.setDisable(false);
       btnOtro.setDisable(true);
       btnGuardar.setDisable(true);
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
