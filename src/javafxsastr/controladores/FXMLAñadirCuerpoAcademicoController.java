/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Controller de la ventana Añadir Cuerpo Academico y modificar.
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionRecargarLgac;
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
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.CampoDeBusqueda;
import javafxsastr.utils.Utilidades;

public class FXMLAñadirCuerpoAcademicoController implements Initializable, INotificacionRecargarLgac{

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
    private TextField bsdUsuario;
    @FXML
    private Button btnEliminar;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbDiciplina;
    @FXML
    private Label lbDescripcion;
    
    Academico academico = null;
    Lgac lgacSelected = null;
    private final int LIM_CARACT_NOMBRE = 100;
    private final int LIM_CARACT_DICIPLINA = 200;
    private final int LIM_CARACT_DESCRIPCION = 600;    
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
    private int responsableOriginalEdicion = -1;
    private Usuario usuario;
    
   
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
    public void recargarDatos() {
       
    }
    
    public void cargarDatos(CuerpoAcademico cu, boolean edicion, Usuario usuarioActual) {
        esEdicion = edicion; 
        this.usuario = usuarioActual;
        if(esEdicion) {
            cuerpoAcademicoEdicion = cu;
            responsableOriginalEdicion = cu.getIdAcademico();       
            idAcademicoSeleccionado = cu.getIdAcademico();
           ObservableList<Lgac> lgacsEdicion = FXCollections.observableArrayList();
            try {
            Academico academicoResponsable = new AcademicoDAO().obtenerAcademicoPorId(
                                    cuerpoAcademicoEdicion.getIdAcademico());
            int idCA = cuerpoAcademicoEdicion.getIdCuerpoAcademico();   
            LgacDAO lgacD = new LgacDAO();
            lgacsEdicion.addAll(lgacD.obtenerInformacionLGACPorCuerpoAcademico(idCA));
            txfNombreCA.setText(cuerpoAcademicoEdicion.getNombreCuerpoAcademico());
            txaDescripcionCA.setText(cuerpoAcademicoEdicion.getDescripcion());
            txfDiciplinaCA.setText(cuerpoAcademicoEdicion.getDisciplinaCuerpoAcademico());
            int areaPosicion = obtenerPosicionComboArea(cuerpoAcademicoEdicion.getIdArea());
            cmbAreas.getSelectionModel().select(areaPosicion);            
            lbNombreResponsable.setText(cuerpoAcademicoEdicion.getNombreResponsableCA());
            lbCorreoResponsable.setText(academicoResponsable.getCorreoInstitucional()); 
            tblvLgacs.setItems(lgacsEdicion);
            lgacsEntabla.addAll(lgacsEdicion);
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
                if(txfNombreCA.getText().length() <= LIM_CARACT_NOMBRE) {
                    validarBtnGuardar();
                    lbNombre.setText("");
                }else { 
                    mostraMensajelimiteSuperado(LIM_CARACT_NOMBRE,"Nombre CA",lbNombre);
                }                
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
                 if(txfDiciplinaCA.getText().length() <= LIM_CARACT_DICIPLINA) {
                    validarBtnGuardar();
                    lbDiciplina.setText("");
                }else { 
                    mostraMensajelimiteSuperado(LIM_CARACT_DICIPLINA,"Diciplina", lbDiciplina);
                }                 
            }
        });
         txaDescripcionCA.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 if(txaDescripcionCA.getText().length() <= LIM_CARACT_DESCRIPCION) {
                    validarBtnGuardar();
                    lbDescripcion.setText("");
                }else { 
                    mostraMensajelimiteSuperado(LIM_CARACT_DESCRIPCION,"Descripcion CA", lbDescripcion);
                } 
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
        ObservableList<Lgac> lgacsEdicion = FXCollections.observableArrayList();
        lgacsEdicion = tblvLgacs.getItems();
        try {
            cuerpoAcademicoDao.actualizarCuerpoAcademico(cuerpoEdicion);
            eliminarRelacionesLgacCA(cuerpoEdicion.getIdCuerpoAcademico());
            for (int i = 0; i < lgacsEdicion.size(); i++) {
                    if(cuerpoAcademicoDao.verificarRelacionCaLgac(lgacsEdicion.get(i).getIdLgac(),
                            cuerpoEdicion.getIdCuerpoAcademico()) == 0) {
                            cuerpoAcademicoDao.agregarRelacionCUconLgac(cuerpoEdicion.getIdCuerpoAcademico(),
                                                                      lgacsEdicion.get(i).getIdLgac());
                    }                    
            }            
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
        ObservableList<Lgac> lgacsEdicion = FXCollections.observableArrayList();
        lgacsEdicion = tblvLgacs.getItems();
        try {
            relacionCaLgac.addAll(cuerpoAcademicoDao.obtenerRelacionesLgacCa(idCa));
            boolean hayRelacion = false;
            for (int i = 0; i < relacionCaLgac.size(); i++) {
                for (int j = 0; j < lgacsEdicion.size(); j++) {
                    if(relacionCaLgac.get(i).getLgac() == lgacsEdicion.get(j).getIdLgac()) {
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
    
    private void validarBtnGuardar() {       
        if(!cmbAreas.getSelectionModel().isEmpty()) {
            nombreCa = txfNombreCA.getText() ;
            area = cmbAreas.getSelectionModel().getSelectedItem().getIdArea();
            diciplina = txfDiciplinaCA.getText();
            descripcion = txaDescripcionCA.getText();
            nombreRca = lbNombreResponsable.getText(); 
            if(nombreCa.trim().length() > 5 && diciplina.trim().length() > 0 && descripcion.trim().length() > 10 && nombreCa.length() < 100 &&
                diciplina.length() < 200 && descripcion.length() < 600 &&
                nombreRca.length() > 5  && tblvLgacs.getItems().size() > 0) {
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
                
    private boolean validarExistenciaCuerpoAcademico() {   
        try {       
            CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();             
            if(cuerpoAcademicoDao.verificarSiCuerpoAcademicoExiste(nombreCa)) {
                Utilidades.mostrarDialogoSimple("Error","Este Cuerpo Academico ya existe", 
                                                                        Alert.AlertType.WARNING);
                return false;
            }    
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return true;
    }
    
    private boolean validarExistenciaResponsable()  {
        try {
            CuerpoAcademicoDAO cuerpoAcademicoDao = new CuerpoAcademicoDAO();
            if(responsableOriginalEdicion == idAcademicoSeleccionado) {
                return true ;                
            }else {
                 if(cuerpoAcademicoDao.verificarSiAcademicoEsResponsableDeCA(idAcademicoSeleccionado)) {
                Utilidades.mostrarDialogoSimple("Error",
                        "Este Academico ya es Responsable de un Cuerpo Academico actualemen, seleccione otro.",
                        Alert.AlertType.WARNING);
                        return false;
                 }                 
            }            
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
       return true;
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
            controladorVistaInicio.setUsuario(usuario);
            Stage escenario = (Stage) lbCorreoResponsable.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("Fallo al cargar la venta","No se pudo cargar la ventana de Cuerpos Academicos", Alert.AlertType.ERROR);
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
     
    private void mostraMensajelimiteSuperado(int limiteCaracteres, String campo,  Label etiquetaError) {        
        etiquetaError.setText("Cuidado, Exediste el limite de caracteres("+limiteCaracteres+") de este campo " + campo);
        btnGuardar.setDisable(esEdicion);
    }    

    @FXML
    private void clicBtnRegresar(MouseEvent event) {
       cerrarVentana();
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if(esEdicion) {            
            if(validarExistenciaResponsable()) {
                 actualizarCuerpoAcademico();
            }           
        }else {
            if(validarExistenciaCuerpoAcademico()) {
                if(validarExistenciaResponsable()) {
                 registrarCuerpoAcademico();
                }    
            }              
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
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAñadirLgac.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAñadirLgacController controladorVistaInicio = accesoControlador.getController();
            controladorVistaInicio.registroLgacPorCA(true);
            controladorVistaInicio.instancearInterfaz(this);
            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Registro Lgac");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("Fallo al cargar la venta","No se pudo cargar la ventana Añadir Lgac", Alert.AlertType.ERROR);
        }
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
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLFormularioUsuario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioUsuarioController controladorVistaInicio = accesoControlador.getController();
            controladorVistaInicio.setUsuario(usuario);
            Stage escenario = (Stage) lbCorreoResponsable.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("Fallo al cargar la venta","No se pudo cargar la ventana Añadir Usuario", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notitficacionRecargarLgac() {
        recuperarDatos(); 
    }
    
}
