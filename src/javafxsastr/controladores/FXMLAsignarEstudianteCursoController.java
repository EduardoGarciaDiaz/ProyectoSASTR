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
import javafx.event.EventHandler;
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
    private TextField txfAlumnoBusqueda;
    @FXML
    private ListView<Estudiante> lsvEstudiantesBusqueda;    
    @FXML
    private Button BtnOtro;
    @FXML
    private VBox vbxEstudiantesPorAgregar;
    
    private ObservableList<Estudiante> estudiantesDisponibles;
    private ObservableList<Estudiante> estudiantesActuales;
    private ObservableList<Estudiante> estudiantesTabla;
    private Estudiante estudianteSeleccionado;
    private Curso cursoActual ;
    private INotificacionRecargarDatos interfaz;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void iniciarEstudiantes(ObservableList<Estudiante> estudiantes, Curso curso, INotificacionRecargarDatos interfazN) {
        interfaz = interfazN;
        if(estudiantes != null) {
            estudiantesActuales = FXCollections.observableArrayList((estudiantes));
                    recuperarEstudinates();

        } else {
            System.out.println("El Observable de estudiantes viene nulo");
        }
        cursoActual = curso;    
        BtnOtro.setDisable(true);
        btnGuardar.setDisable(true);
        estudiantesTabla = FXCollections.observableArrayList();
        iniciarListener();
    }
    
    private void iniciarListener() {
        CampoDeBusqueda<Estudiante> campoDeBusqueda = new CampoDeBusqueda<>(txfAlumnoBusqueda, lsvEstudiantesBusqueda,
            estudiantesDisponibles, estudianteSeleccionado, new INotificacionSeleccionItem<Estudiante>() {            
              @Override
            public void notificarSeleccionItem(Estudiante itemSeleccionado) {
                if(itemSeleccionado != null) {
                    estudianteSeleccionado = itemSeleccionado;
                     vbxEstudiantesPorAgregar.requestFocus();     
                }                          
            }
            @Override
            public void notificarPerdidaDelFoco() {                
                if(validarEstudianteEnCurso()) {                    
                    AgregarATabla();
                }else {
                    Utilidades.mostrarDialogoSimple("Accion invalida", "Este Estudiante ya pertenece a un curso",
                            Alert.AlertType.INFORMATION);                   
                }
                txfAlumnoBusqueda.setText("");
            }         
        });        
    }
    
    private void recuperarEstudinates() {
        try {
            estudiantesDisponibles = FXCollections.observableArrayList((new EstudianteDAO().obtenerEstudiantes()));
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private boolean validarEstudianteEnCurso() {
        if(estudianteSeleccionado != null) {
            for (Estudiante estudiantes : estudiantesActuales) {
                if (estudiantes.getIdEstudiante() == estudianteSeleccionado.getIdEstudiante()) {
                    return false;
                }
            }
            if(estudianteSeleccionado.getIdCurso() != 0){
                return false;
            }
            if(estudiantesTabla != null) {
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
    
    private void AgregarATabla() {     
       estudiantesTabla.add(estudianteSeleccionado);
       vbxEstudiantesPorAgregar.setSpacing(40);      
       TarjetaAgregarEstudianteCurso tarjeta = new TarjetaAgregarEstudianteCurso(estudianteSeleccionado);
       tarjeta.getImagen().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {              
                      quitarEstudiante(tarjeta.getEstudinate());                      
                }
            });
       vbxEstudiantesPorAgregar.getChildren().add(tarjeta);     
       txfAlumnoBusqueda.setText("");
       txfAlumnoBusqueda.setDisable(true);
       BtnOtro.setDisable(false);
       btnGuardar.setDisable(false);
    }
    
    private void quitarEstudiante(Estudiante estudianteQuitar) {
        for (Estudiante estudianteEliminar : estudiantesTabla) {
            if (estudianteEliminar.getIdEstudiante() == estudianteQuitar.getIdEstudiante()) {
                estudiantesTabla.remove(estudianteEliminar);
                break;
            }
        }
        recargarVbox();        
    }
    
    private void recargarVbox() {
        vbxEstudiantesPorAgregar.getChildren().clear();
        if(estudiantesTabla.size() > 0) {
            for (Estudiante estudianteAgregar : estudiantesTabla) {
                TarjetaAgregarEstudianteCurso tarjeta = new TarjetaAgregarEstudianteCurso(estudianteSeleccionado);
                tarjeta.getImagen().setOnMouseClicked(new EventHandler<MouseEvent>() {
                     public void handle(MouseEvent event) {              
                               quitarEstudiante(tarjeta.getEstudinate());                               
                         }
                     });
                vbxEstudiantesPorAgregar.getChildren().add(tarjeta);     
             }
        }else {
            txfAlumnoBusqueda.setDisable(false);
            BtnOtro.setDisable(true);
            btnGuardar.setDisable(true);
        }    
    }
    
    private void guardarEstudiantes() {
        for (Estudiante estudinateNuevo : estudiantesTabla) {
            try {
                System.err.println(cursoActual);
                int exito = new CursoDAO().guardarRelacionCursoEstudiante(cursoActual.getIdCurso(),
                        estudinateNuevo.getIdEstudiante());
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
        Utilidades.mostrarDialogoSimple("Registro Exitoso","Se guardaron los estudiantes", 
                Alert.AlertType.INFORMATION);       
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage escenarioActual = (Stage) txfAlumnoBusqueda.getScene().getWindow();  
        interfaz.notitficacionRecargarDatos();
        escenarioActual.close();
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
    
    @FXML
    private void clicBtnCancelar(ActionEvent event) {
       if( Utilidades.mostrarDialogoConfirmacion("Cuidado!!",
                "¿Estás seguro de que deseas cancelar la adición de un estudiante a un curso?" ))
           cerrarVentana();                
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        guardarEstudiantes();
    }

    @FXML
    private void clicBtnOtro(ActionEvent event) {
       txfAlumnoBusqueda.setDisable(false);
       BtnOtro.setDisable(true);
       btnGuardar.setDisable(true);
    }
    
}
