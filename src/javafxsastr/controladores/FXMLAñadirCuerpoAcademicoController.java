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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.CuerpoAcademicoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.LgacDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Area;
import javafxsastr.modelos.pojo.CuerpoAcademico;
import javafxsastr.modelos.pojo.CuerpoAcademicoLgac;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.utils.CampoDeBusqueda;
import javafxsastr.utils.Utilidades;

public class FXMLAñadirCuerpoAcademicoController implements Initializable {

    @FXML
    private AnchorPane menuContraido;
    @FXML
    private TextField txfNombreCA;
    @FXML
    private TextArea txaDescripcionCA;
    @FXML
    private TextField txfDiciplinaCA;
    @FXML
    private Label lbNombreResponsable;
    @FXML
    private Label lbCorreoResponsable;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField bsdLgac;
    @FXML
    private TableView<Lgac> tblvLgacs;
    @FXML
    private TextField bsdAcademico;
    @FXML
    private ListView<Academico> lvAcademicos;
    @FXML
    private TableColumn colLgacNombre;
    @FXML
    private TableColumn colDescipcionLgac;
    @FXML
    private TableColumn colNoLgac;
    @FXML
    private ListView<Lgac> lvLgac; 
    @FXML
    private ComboBox<Area> cmbAreas;
    @FXML
    private Label lbTituloventana;
    @FXML
    private ImageView btnMenu;
    @FXML
    private TextField bsdUsuario;
    @FXML
    private AnchorPane menuLateral;
    @FXML
    private Button btnEliminar;
    
    Academico academico = null;
    Lgac lgacSelected = null;
    private ObservableList<Lgac> lgacs;
    private ObservableList<Area> areas;
    private ObservableList<Academico> academicosBusqueda;
    private ObservableList<Lgac> lgacsEntabla; 
    private String nombreCa;
    private int area;
    private String diciplina;
    private String descripcion;
    private String nombreRca;
    private Lgac lgac;
    private int idAcademicoSeleccionado; 
    private CuerpoAcademico cuerpoAcademicoEdicion;
    private boolean esEdicion;
    private INotificacionSeleccionItem interfazNotificaicon;
    private ObservableList<Lgac> lgacsEdicion;
    
   
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConfigurarTabla();
        btnGuardar.setDisable(true);
        lgacsEntabla=  FXCollections.observableArrayList();        
        recuperarDatos();
        recuperarAreas();
        inicializarLisneters();  
        if(esEdicion) {
            lbTituloventana.setText("Modificar Cuerpo Academico");
        }
    }  
    
    public void cargarDatos(CuerpoAcademico cu, boolean edicion) {
        esEdicion = edicion; 
        if(esEdicion) {
            cuerpoAcademicoEdicion = cu;
            try {
            Academico academicoResponsable = new AcademicoDAO().obtenerAcademicoPorId(cuerpoAcademicoEdicion.getIdAcademico());
            int idCA = cuerpoAcademicoEdicion.getIdCuerpoAcademico();
            System.err.println(idCA);
            lgacsEdicion = FXCollections.observableArrayList();
            LgacDAO lgacD = new LgacDAO();
            lgacsEdicion.addAll(lgacD.obtenerInformacionLGACPorCuerpoAcademico(idCA));
            txfNombreCA.setText(cuerpoAcademicoEdicion.getNombreCuerpoAcademico());
            txaDescripcionCA.setText(cuerpoAcademicoEdicion.getDescripcion());
            txfDiciplinaCA.setText(cuerpoAcademicoEdicion.getDisciplinaCuerpoAcademico());
            int areaPosicion = obtenerPosicionComboArea(cuerpoAcademicoEdicion.getIdArea());
            cmbAreas.getSelectionModel().select(areaPosicion);            
            lbNombreResponsable.setText(cuerpoAcademicoEdicion.getNombreResponsableCA());
            lbCorreoResponsable.setText(academicoResponsable.getCorreoInstitucional()); 
            System.err.println(lgacsEdicion.isEmpty());
            tblvLgacs.setItems(lgacsEdicion);            
        } catch (DAOException ex) {
            ex.printStackTrace();
             manejarDAOException(ex);
        }
        btnGuardar.setDisable(true);
        }      
    }
    
    private void inicializarLisneters() {        
        
        txfNombreCA.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarBtnGuardar();
            }
        });
         cmbAreas.valueProperty().addListener((ObservableValue<? extends Area> observable, Area oldValue, Area newValue) -> {
              if (newValue != null) {
                  validarBtnGuardar();
              }
        });
                 
        txfDiciplinaCA.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarBtnGuardar();
            }
        });
         txaDescripcionCA.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarBtnGuardar();
            }
        });       
        lbNombreResponsable.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validarBtnGuardar();
            }
        });        
        
        CampoDeBusqueda<Academico> campoDeBusqueda = new CampoDeBusqueda<>(bsdAcademico, lvAcademicos,
            academicosBusqueda, academico, new INotificacionSeleccionItem<Academico>() {
            @Override
            public void notificarSeleccionItem(Academico itemSeleccionado) {
                academico = itemSeleccionado; 
                lbCorreoResponsable.requestFocus();
            }

            @Override
            public void notificarPerdidaDelFoco() {
                if(academico != null) {
                    lbNombreResponsable.setText(academico.getNombre()+" "+academico.getPrimerApellido()+" "+academico.getSegundoApellido());
                    lbCorreoResponsable.setText(academico.getCorreoInstitucional());
                    bsdAcademico.setText("");
                    idAcademicoSeleccionado = academico.getIdAcademico();
                    validarBtnGuardar();
                }                
            }
            }
        );
        CampoDeBusqueda<Lgac> campoDeBusquedaLgca = new CampoDeBusqueda<>(bsdLgac, lvLgac,
            lgacs, lgacSelected, new INotificacionSeleccionItem<Lgac>() {            
            @Override
            public void notificarSeleccionItem(Lgac itemSeleccionado) {
                lgacSelected = itemSeleccionado; 
                lbCorreoResponsable.requestFocus();
            }
            @Override
            public void notificarPerdidaDelFoco() {
               if(lgacSelected != null) {
                    if(verificarTabla()) {
                        lgacsEntabla.add(lgacSelected);
                        mostrarLgacEnTabla();                         
                    }else{
                        bsdLgac.setText("");
                    }
               } 
               validarBtnGuardar();
            }
            }
        );    
    }
    
    private void recuperarDatos() {
        try {
           academicosBusqueda = FXCollections.observableArrayList(new AcademicoDAO().obtenerAcademicos());
           lgacs = FXCollections.observableArrayList(new LgacDAO().obtenerInformacionLGCAS());
        } catch (DAOException ex) {
            ex.printStackTrace();
        }        
    }
    
     private void validarBtnGuardar() {        
        
        if(!cmbAreas.getSelectionModel().isEmpty()) {
            nombreCa = txfNombreCA.getText() ;
            area = cmbAreas.getSelectionModel().getSelectedItem().getIdArea();
            diciplina = txfDiciplinaCA.getText();
            descripcion = txaDescripcionCA.getText();
            nombreRca = lbNombreResponsable.getText(); 
            if(nombreCa.length() > 5 && diciplina.length() > 0 && descripcion.length() > 10 && 
                nombreRca.length() > 5 && tblvLgacs.getItems().size() > 0) {
                 habilitarBtnGuardar();
            }else {
                 btnGuardar.setDisable(true);
            }
        }
     }
     
    private void habilitarBtnGuardar() {
        btnGuardar.setDisable(false);
    }
    
    private void ConfigurarTabla() {
       colLgacNombre.setCellValueFactory(new PropertyValueFactory("nombreLgac"));
       colDescipcionLgac.setCellValueFactory(new PropertyValueFactory("descripcionLgac"));    
       colNoLgac.setCellValueFactory(new PropertyValueFactory("idLgac"));
    }
    
    private void mostrarLgacEnTabla() {
        tblvLgacs.setItems(lgacsEntabla);        
        bsdLgac.setText("");
    }
    
    private boolean verificarTabla() {
        lgacsEntabla = tblvLgacs.getItems();
        for (int i = 0; i < lgacsEntabla.size(); i++) {
            if(lgacsEntabla.get(i).getIdLgac() == lgacSelected.getIdLgac()) return false;           
        }       
        return true;        
    }
            
    
    private void registrarCuerpoAcademico() {
        try {
            CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();
            CuerpoAcademico cuerpoNuevo = new CuerpoAcademico();
            cuerpoNuevo.setNombreCuerpoAcademico(nombreCa);
            cuerpoNuevo.setDisciplinaCuerpoAcademico(diciplina);
            cuerpoNuevo.setIdArea(area);
            cuerpoNuevo.setIdAcademico(idAcademicoSeleccionado);
            cuerpoNuevo.setDescripcion(descripcion);
            int exito = cuerpoAcademicoDao.agregarCuerpoAcademico(cuerpoNuevo);
            if(exito != -1) {
                Utilidades.mostrarDialogoSimple("Registro exitoso", "Se registro el cuerpo academico exitosamente",
                        Alert.AlertType.CONFIRMATION);               
                for (int i = 0; i < lgacsEntabla.size(); i++) {
                    cuerpoAcademicoDao.agregarRelacionCUconLgac(exito, lgacsEntabla.get(i).getIdLgac());
                }
            }
        } catch (DAOException ex) {
             manejarDAOException(ex);
        }
        limpiarCampos();
    }
    
    private void actualizarCuerpoAcademico() {
        CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();
        CuerpoAcademico cuerpoEdicion = new CuerpoAcademico();
        cuerpoEdicion.setIdCuerpoAcademico(cuerpoAcademicoEdicion.getIdCuerpoAcademico());
        cuerpoEdicion.setNombreCuerpoAcademico(nombreCa);
        cuerpoEdicion.setDisciplinaCuerpoAcademico(diciplina);
        cuerpoEdicion.setIdArea(area);
        cuerpoEdicion.setIdAcademico(idAcademicoSeleccionado);
        cuerpoEdicion.setDescripcion(descripcion);
        try {
            cuerpoAcademicoDao.actualizarCuerpoAcademico(cuerpoEdicion);
            for (int i = 0; i < lgacsEntabla.size(); i++) {
                    if(cuerpoAcademicoDao.verificarRelacionCaLgac(lgacsEntabla.get(i).getIdLgac(), 
                            cuerpoEdicion.getIdCuerpoAcademico()) == 0)
                    cuerpoAcademicoDao.agregarRelacionCUconLgac(cuerpoEdicion.getIdCuerpoAcademico(),
                    lgacsEntabla.get(i).getIdLgac());
                }
            eliminarRelacionesLgacCA(cuerpoEdicion.getIdCuerpoAcademico());
            Utilidades.mostrarDialogoSimple("Actualizacion exitosa", "Se actualizo el cuerpo academico exitosamente",
                        Alert.AlertType.CONFIRMATION);  
            cerrarVentana();
        } catch (DAOException ex) {
             manejarDAOException(ex);
        }
    }
    
    private void eliminarRelacionesLgacCA(int idCa) {
        CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();
        ObservableList<CuerpoAcademicoLgac> relacionCaLgac = FXCollections.observableArrayList();
        try {
            relacionCaLgac.addAll(cuerpoAcademicoDao.obtenerRelacionesLgacCa(idCa));
            boolean hayRelacion = false;
            for (int i = 0; i < relacionCaLgac.size(); i++) {
                for (int j = 0; j < lgacsEntabla.size(); j++) {
                    if(relacionCaLgac.get(i).getLgac() == lgacsEntabla.get(j).getIdLgac()) {
                        hayRelacion = true;                        
                    }
                }
                if(!hayRelacion) {
                    cuerpoAcademicoDao.eliminarRelacionCuerpoLgac(relacionCaLgac.get(i).getIdCuerpoAcademicoLgac());
                }
                hayRelacion = false;
            }
        } catch (DAOException ex) {
             manejarDAOException(ex);
        }
    }
    
    
    
    private void eliminarLgacTabla(Lgac lgacEliminar) {
        for (int i = 0; i < lgacsEntabla.size(); i++) {
            if(lgacsEntabla.get(i).getIdLgac() == lgacEliminar.getIdLgac()) 
                lgacsEntabla.remove(i);           
        }  
        validarBtnGuardar();
    }
    
    private void limpiarCampos() {
        txfNombreCA.clear();       
        cmbAreas.setPromptText("<Area>");
        txfDiciplinaCA.clear();
        txaDescripcionCA.clear();
        lbNombreResponsable.setText("");
        lbCorreoResponsable.setText("");
        lgacsEntabla.clear();
        tblvLgacs.setItems(lgacsEntabla);
    }
    
    private void llenarComboAreas() {
        cmbAreas.setItems(areas);
    } 
                
    private void validarExistenciaCuerpoAcademico() {   
        try {       
            CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();
            if(cuerpoAcademicoDao.verificarSiCuerpoAcademicoExiste(nombreCa)) {
                Utilidades.mostrarDialogoSimple("Error","Este Cuerpo Academico ya existe", Alert.AlertType.WARNING);
            }else {
                validarExistenciaResponsable();
            }       
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void validarExistenciaResponsable()  {
        try {
            CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();
            if(cuerpoAcademicoDao.verificarSiAcademicoEsResponsableDeCA(idAcademicoSeleccionado)) {
                Utilidades.mostrarDialogoSimple("Error",
                        "Este Academico ya es Responsable de un Cuerpo Academico actualemen, seleccione otro.",
                        Alert.AlertType.WARNING);
            }else {
                registrarCuerpoAcademico();
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void recuperarAreas() {
        try {
            CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();
            areas = FXCollections.observableArrayList();
            areas.addAll(cuerpoAcademicoDao.obtenerAreas());
            llenarComboAreas();
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
     private int obtenerPosicionComboArea(int idArea) {
        for (int i = 0; i < areas.size(); i++) {
            if(areas.get(i).getIdArea()== idArea) {
                return i;
            }
        }
        return 0;   
    }
    
    private void cerrarVentana() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCuerposAcademicos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCuerposAcademicosController controladorVistaInicio = accesoControlador.getController();            
            Stage escenario = (Stage) lbNombreResponsable.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAñadirCuerpoAcademicoController.class.getName()).log(Level.SEVERE, null, ex);
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
    

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
       cerrarVentana();
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if(esEdicion) {
            actualizarCuerpoAcademico();
        }else {
            validarExistenciaCuerpoAcademico();
        }        
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        if( Utilidades.mostrarDialogoConfirmacion("Cancelar Captura de Cuerpo Academico",
                "Estas seguro que deseas cancelar el registro del cuerpo academico, se perderan todos los cambios")) {
            cerrarVentana();
        }        
    }

    @FXML
    private void clicBtnAñadirLgac(ActionEvent event) {
        Stage escenarioNuevo = new Stage();
        Scene escenaNueva = Utilidades.inicializarEscena("vistas/FXMLAñadirLgac.fxml");
        escenarioNuevo.setScene(escenaNueva);
        escenarioNuevo.setTitle("Añadir Lgac");
        escenarioNuevo.initModality(Modality.APPLICATION_MODAL);
        escenarioNuevo.show();
    }    

    @FXML
    private void clicEliminarLgac(ActionEvent event) {
        Lgac lgacEliminar = tblvLgacs.getSelectionModel().getSelectedItem();   
        if(lgacEliminar!= null) {
            eliminarLgacTabla(lgacEliminar);
            mostrarLgacEnTabla();
        }        
    }

    @FXML
    private void clicEliminarRepsonsable(MouseEvent event) {
        lbCorreoResponsable.setText("");
        lbNombreResponsable.setText("");
        academico=null;
        validarBtnGuardar();
    }

    @FXML
    private void clicBtnAñadirUsuario(ActionEvent event) {       
        Stage escenarioNuevo = new Stage();
        Scene escenaNueva = Utilidades.inicializarEscena("vistas/FXMLFormularioUsuario.fxml");
        escenarioNuevo.setScene(escenaNueva);
        escenarioNuevo.setTitle("Añadir Usuario");
        escenarioNuevo.initModality(Modality.APPLICATION_MODAL);
        escenarioNuevo.show();
    }

    
    
    
}
