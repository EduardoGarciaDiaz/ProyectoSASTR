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
    
    private ObservableList<Estudiante> estudinatesDisponibles = FXCollections.observableArrayList();
    private ObservableList<Estudiante> estudinatesActuales = FXCollections.observableArrayList();
    private ObservableList<Estudiante> estudinatesTabla = FXCollections.observableArrayList();
     private ObservableList<Estudiante> estudinatesTablaAuxiliar ;
    private Estudiante estudiante = new Estudiante();
    private int cursoActual ;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       BtnOtro.setDisable(true);
       btnGuardar.setDisable(true);
       recuperarEstudinates();
       iniciarListener();
    }    
    
    public void quitarEstudianste(int idEstudinate) {///CHECHAR ESTE ROLLO
         System.err.println("tabla usuarios al querer eliminar: "+estudinatesTablaAuxiliar.size());
        for (Estudiante estudianteEliminar : estudinatesTablaAuxiliar) {
            System.err.println(estudianteEliminar +" =="+idEstudinate);
            if(estudianteEliminar.getIdEstudiante() == idEstudinate) {
                System.err.println("Si es igual???????");
                estudinatesTabla.remove(estudianteEliminar);
            }
        }
        recargarVbox();        
    }
    
    public void iniciarEstudiantes(ObservableList<Estudiante> estudinates, int curso) {
        if(estudinates != null) {
            estudinatesActuales.addAll(estudinates);
        }
        cursoActual = curso;        
    }
    
    private void iniciarListener() {
        CampoDeBusqueda<Estudiante> campoDeBusqueda = new CampoDeBusqueda<>(txfAlumnoBusqueda, lsvAlumnosBuaqueda,
            estudinatesDisponibles, estudiante, new INotificacionSeleccionItem<Estudiante>() {            
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
            estudinatesDisponibles.addAll(new EstudianteDAO().obtenerEstudiantes());
        } catch (DAOException ex) {
            Logger.getLogger(FXMLAsignarEstudianteCursoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean validarEstudianteEnCurso() {
        for (Estudiante estudinates : estudinatesActuales) {
            if (estudiante.getIdEstudiante() == estudinates.getIdEstudiante()) {
                return false;
            }
        }
        for (Estudiante estudiante1 : estudinatesTabla) {
            if (estudiante.getIdEstudiante() == estudiante1.getIdEstudiante()) {
                return false;
            }
        }                
        return true;
    }
    
    private void AgregarATabla() {     
       estudinatesTabla.add(estudiante);
       System.err.println("tabla estudinates al agregar "+estudinatesTabla.size());
       vbxEstudiantesPorAgregar.setSpacing(40);      
       estudinatesTablaAuxiliar= FXCollections.observableArrayList();
       estudinatesTablaAuxiliar.addAll(estudinatesTabla);
       vbxEstudiantesPorAgregar.getChildren().add(new TarjetaAgregarEstudianteCurso(estudiante.getNombre()
               +" "+estudiante.getPrimerApellido()+ " "+ estudiante.getSegundoApellido()+ "      " +estudiante.getMatriculaEstudiante(),
               estudiante.getIdEstudiante()));     
       txfAlumnoBusqueda.setText("");
       txfAlumnoBusqueda.setDisable(true);
       BtnOtro.setDisable(false);
       btnGuardar.setDisable(false);
    }
    
    public void recargarVbox() {
        vbxEstudiantesPorAgregar.getChildren().clear();
        for (Estudiante estudianteAgregar : estudinatesTabla) {
            vbxEstudiantesPorAgregar.getChildren().add(new TarjetaAgregarEstudianteCurso(estudianteAgregar.getNombre()+
                    " "+estudianteAgregar.getPrimerApellido()+" "+estudianteAgregar.getSegundoApellido()+"    "
                    +estudianteAgregar.getMatriculaEstudiante(),
                    estudianteAgregar.getIdEstudiante()));     
        }
    }
    
    private void guardarEstudiantes() {
        for (Estudiante estudinateNuevo : estudinatesTabla) {
            try {
                System.err.println(cursoActual);
                int exito = new CursoDAO().guardarRelacionCursoEstudiante(cursoActual,
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
