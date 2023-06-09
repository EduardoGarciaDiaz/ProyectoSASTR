/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Controller de la ventana Asignar estudinate Curso.
 */

package javafxsastr.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.CampoDeBusqueda;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAgregarAlumno;
import javafxsastr.utils.cards.TarjetaAgregarEstudianteCurso;

public class FXMLAsignarEstudianteCursoController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txfAlumnoBusqueda;
    @FXML
    private ListView<Estudiante> lsvAlumnosBuaqueda;    
    @FXML
    private Button BtnOtro;
    @FXML
    private VBox vbxEstudiantesPorAgregar;
    
    private ObservableList<Estudiante> estudiantesDisponibles;// = FXCollections.observableArrayList();
    private ObservableList<Estudiante> estudiantesActuales;// = FXCollections.observableArrayList();
    private ObservableList<Estudiante> estudiantesTabla;// = FXCollections.observableArrayList();
    private ObservableList<Estudiante> estudiantesTablaAuxiliar ;
    private Estudiante estudiante = new Estudiante();
    private Curso cursoActual ;
    private INotificacionRecargarDatos interfaz;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //BtnOtro.setDisable(true);
       //btnGuardar.setDisable(true);
       //recuperarEstudinates();
       //iniciarListener();
    }    
    
    public void quitarEstudiante(int idEstudiante) {
         System.err.println("tabla usuarios al querer eliminar: " + estudiantesTablaAuxiliar.size());
        for (Estudiante estudianteEliminar : estudiantesTablaAuxiliar) {
            System.err.println(estudianteEliminar +" =="+idEstudiante);
            if (estudianteEliminar.getIdEstudiante() == idEstudiante) {
                System.err.println("Si es igual???????");
                estudiantesTabla.remove(estudianteEliminar);
            }
        }
        recargarVbox();        
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
        iniciarListener();
    }
    
    private void iniciarListener() {
        CampoDeBusqueda<Estudiante> campoDeBusqueda = new CampoDeBusqueda<>(txfAlumnoBusqueda, lsvAlumnosBuaqueda,
            estudiantesDisponibles, estudiante, new INotificacionSeleccionItem<Estudiante>() {            
              @Override
            public void notificarSeleccionItem(Estudiante itemSeleccionado) {
                estudiante = itemSeleccionado;
                btnGuardar.requestFocus();                
            }
            @Override
            public void notificarPerdidaDelFoco() {                
                if(validarEstudianteEnCurso()) {                    
                    AgregarATabla();
                }else {
                    Utilidades.mostrarDialogoSimple("NO posible", "EsteEstudiante ya existe en el curso",
                            Alert.AlertType.INFORMATION);
                }
            }         
        });
        
    }
    
    private void recuperarEstudinates() {
        try {
            estudiantesDisponibles = FXCollections.observableArrayList((new EstudianteDAO().obtenerEstudiantes()));
        } catch (DAOException ex) {
            Logger.getLogger(FXMLAsignarEstudianteCursoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean validarEstudianteEnCurso() {
        for (Estudiante estudinates : estudiantesActuales) {
            if (estudiante.getIdEstudiante() == estudinates.getIdEstudiante()) {
                return false;
            }
        }
        for (Estudiante estudiante1 : estudiantesTabla) {
            if (estudiante.getIdEstudiante() == estudiante1.getIdEstudiante()) {
                return false;
            }
        }                
        return true;
    }
    
    private void AgregarATabla() {     
       estudiantesTabla.add(estudiante);
       vbxEstudiantesPorAgregar.setSpacing(40);      
       estudiantesTablaAuxiliar= FXCollections.observableArrayList();
       estudiantesTablaAuxiliar.addAll(estudiantesTabla);
       TarjetaAgregarEstudianteCurso tarjeta = new TarjetaAgregarEstudianteCurso(estudiante);
       tarjeta.getImagen().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {              
                      quitarEstudiante(tarjeta.getEstudinate().getIdEstudiante());
                }
            });
       vbxEstudiantesPorAgregar.getChildren().add(tarjeta);     
       txfAlumnoBusqueda.setText("");
       txfAlumnoBusqueda.setDisable(true);
       BtnOtro.setDisable(false);
       btnGuardar.setDisable(false);
    }
    
    public void recargarVbox() {
        vbxEstudiantesPorAgregar.getChildren().clear();
        for (Estudiante estudianteAgregar : estudiantesTabla) {
             TarjetaAgregarEstudianteCurso tarjeta = new TarjetaAgregarEstudianteCurso(estudiante);
            tarjeta.getImagen().setOnMouseClicked(new EventHandler<MouseEvent>() {
                 public void handle(MouseEvent event) {              
                           quitarEstudiante(tarjeta.getEstudinate().getIdEstudiante());
                     }
                 });
            vbxEstudiantesPorAgregar.getChildren().add(tarjeta);     
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
        Utilidades.mostrarDialogoSimple("Registor Exitoso","Se guardaron los estudinates", 
                Alert.AlertType.INFORMATION);
        cerrarVentana();
    }
    
    public void cerrarVentana() {
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
