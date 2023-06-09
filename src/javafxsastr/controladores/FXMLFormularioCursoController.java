/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 03/06/2023
 * Descripción: La clase FXMLFormularioCurso actúa como controlador
 * de la vista FormularioCurso. Contiene los métodos necesarios 
 * para la creación y modificación de cursos. 
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionRecargarDatos;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.BloqueDAO;
import javafxsastr.modelos.dao.CursoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.ExperienciaEducativaDAO;
import javafxsastr.modelos.dao.NrcDAO;
import javafxsastr.modelos.dao.PeriodoEscolarDAO;
import javafxsastr.modelos.dao.SeccionDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Bloque;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.ExperienciaEducativa;
import javafxsastr.modelos.pojo.Nrc;
import javafxsastr.modelos.pojo.PeriodoEscolar;
import javafxsastr.modelos.pojo.Seccion;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.CampoDeBusqueda;
import javafxsastr.utils.Utilidades;

public class FXMLFormularioCursoController implements Initializable {

    @FXML
    private TextField tfNombreCurso;
    @FXML
    private ComboBox<ExperienciaEducativa> cmbExperienciasEducativas;
    @FXML
    private ComboBox<Nrc> cmbNrcs;
    @FXML
    private ComboBox<PeriodoEscolar> cmbPeriodosEscolares;
    @FXML
    private ComboBox<Seccion> cmbSecciones;
    @FXML
    private ComboBox<Bloque> cmbBloques;
    private TextField cmbProfesor;
    @FXML
    private DatePicker dpFechaInicioClases;
    @FXML
    private DatePicker dpFechaFinClases;
    private ObservableList<ExperienciaEducativa> experienciasEducativas;
    private ObservableList<Nrc> nrcs;
    private Academico profesorSeleccionado;
    @FXML
    private TextField tfProfesor;
    @FXML
    private ListView<Academico> lvprofesores;
    private ObservableList<Academico> profesores;
    private ObservableList<PeriodoEscolar> periodos;
    private ObservableList<Seccion> secciones;
    private ObservableList<Bloque> bloques;
    private boolean esEdicion;
    private boolean vieneDeDetalles = false;
    private Curso cursoEdicion;
    private INotificacionRecargarDatos interfaz;
    @FXML
    private Label lbTituloFormulario;
    @FXML
    private Button btnAceptar;
    private Label lbMensajeError;
    private Label lbFechaError;
    @FXML
    private Label lbMensajeDeError;
    private Academico academico;    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarExperienciasEducativas();
        cargarPeriodosEscolares();
        obtenerProfesores();
        cargarSecciones();
        cargarbloques();
        agregarListenerCampos();
        btnAceptar.setDisable(true);
        inicializarCamposDeBusqueda();
    }    
    
    public void setUsuario(Academico academico) {
        this.academico = academico;
        cargarExperienciasEducativas();
        cargarPeriodosEscolares();
        obtenerProfesores();
        cargarSecciones();
        cargarbloques();
        agregarListenerCampos();
        btnAceptar.setDisable(true);
        inicializarCamposDeBusqueda();
    }
    
    public void esEdiconPorVentanaDetalles(INotificacionRecargarDatos interfazN) {
        this.vieneDeDetalles = true;
        this.interfaz = interfazN;
    }

    public void inicializarInformacionFormulario(boolean esEdicion, Curso cursoEdicion) {
        this.esEdicion = esEdicion;
        this.cursoEdicion = cursoEdicion;
        if (esEdicion) {
            lbTituloFormulario.setText("Modificar curso");
            setInformacionCursoEdicion(cursoEdicion);
        } else {
            lbTituloFormulario.setText("Añadir curso");
            btnAceptar.setDisable(true);
        }
    }
    
    private void setInformacionCursoEdicion(Curso cursoEdicion) {
        tfNombreCurso.setText(cursoEdicion.getNombreCurso());
        int posicionExperienciaEducativa = obtenerPosicionComboExperienciaEducativa(cursoEdicion.getIdExperienciaEducativa());
        cmbExperienciasEducativas.getSelectionModel().select(posicionExperienciaEducativa);
        int posicionNrc = obtenerPosicionComboNrc(cursoEdicion.getIdNRC());
        cmbNrcs.getSelectionModel().select(posicionNrc);
        int posicionPeriodoEscolar = obtenerPosicionComboPeriodoEscolar(cursoEdicion.getIdPeriodoEscolar());
        cmbPeriodosEscolares.getSelectionModel().select(posicionPeriodoEscolar);
        int posicionSeccion = obtenerPosicionComboSecciones(cursoEdicion.getIdSeccion());
        cmbSecciones.getSelectionModel().select(posicionSeccion);
        int posicionBloque = obtenerPosicionComboBloques(cursoEdicion.getIdBloque());
        cmbBloques.getSelectionModel().select(posicionBloque);
        int posicionProfesor = obtenerPosicionProfesor(cursoEdicion.getIdAcademico());
        lvprofesores.getSelectionModel().select(posicionProfesor);
        tfProfesor.setText(lvprofesores.getSelectionModel().getSelectedItem().toString());
        dpFechaInicioClases.setValue(LocalDate.parse(cursoEdicion.getFechaInicioCurso()));
        dpFechaFinClases.setValue(LocalDate.parse(cursoEdicion.getFechaFinCurso()));
        
    }
    
    private int obtenerPosicionComboExperienciaEducativa(int idExperienciaEducativa) {
        for (int i = 0; i < experienciasEducativas.size(); i++) {
            if (experienciasEducativas.get(i).getIdExperienciaEducativa() == idExperienciaEducativa) {
                return i;
            }
        }
        return 0;
    }
    
    private int obtenerPosicionComboNrc(int idNrc) {
        for (int i = 0; i < nrcs.size(); i++) {
            if (nrcs.get(i).getIdNrc() == idNrc) {
                return i;
            }
        }
        return 0;
    }
    
    private int obtenerPosicionComboPeriodoEscolar(int idPeriodoEscolar) {
        for (int i = 0; i < periodos.size(); i++) {
            if (periodos.get(i).getIdPeriodoEscolar() == idPeriodoEscolar) {
                return i;
            }
        }
        return 0;
    }
    
    private int obtenerPosicionComboSecciones(int idSeccion) {
        for (int i = 0; i < secciones.size(); i++) {
            if (secciones.get(i).getIdSeccion()== idSeccion) {
                return i;
            }
        }
        return 0;
    }
    
    private int obtenerPosicionComboBloques(int idBloque) {
        for (int i = 0; i < bloques.size(); i++) {
            if (bloques.get(i).getIdBloque()== idBloque) {
                return i;
            }
        }
        return 0;
    }
    
    private int obtenerPosicionProfesor(int idProfesor) {
        for (int i = 0; i < profesores.size(); i++) {
            if (profesores.get(i).getIdAcademico() == idProfesor) {
                return i;
            }
        }
        return 0;
    }
    
    private void obtenerProfesores() {
        try {
            profesores = FXCollections.observableArrayList(
                new AcademicoDAO().obtenerAcademicos());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarExperienciasEducativas() {
        try {
            experienciasEducativas = FXCollections.observableArrayList(
                    new ExperienciaEducativaDAO().obtenerExperienciasEducativas());
            cmbExperienciasEducativas.setItems(experienciasEducativas);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarNrcs(int idExperienciaEducativa) {
        try {
            nrcs = FXCollections.observableArrayList(
                new NrcDAO().obtenerNRCSPorExperienciaEducativa(idExperienciaEducativa));
            cmbNrcs.setItems(nrcs);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarPeriodosEscolares() {
        try {
            periodos = FXCollections.observableArrayList(
                new PeriodoEscolarDAO().obtenerPeriodosEscolares());
            cmbPeriodosEscolares.setItems(periodos);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarSecciones() {
        try {
            secciones = FXCollections.observableArrayList(
                new SeccionDAO().obtenerSecciones());
            cmbSecciones.setItems(secciones);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarbloques() {
        try {
            bloques = FXCollections.observableArrayList(
                new BloqueDAO().obtenerBloques());
            cmbBloques.setItems(bloques);
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

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        boolean respuesta = Utilidades.mostrarDialogoConfirmacion("Confirmacion", "¿Estás seguro de que deseas cancelar la creación del curso");
        if (respuesta) {
            irAVistaCursos();
        }
    }

    @FXML
    private void clicBtnAceptar(ActionEvent event) {
        if (validarCamposObligatoriosLlenos()) {
            System.out.println("entre aqui");
            if (validarFechas()){
                System.out.println("valide fechas");
                if (esEdicion) {
                actualizarCurso(crearCursoValido());
                Utilidades.mostrarDialogoSimple("Curso modificado.", 
                            "Cambios realizados correctamente.", Alert.AlertType.INFORMATION);
                } else {
                    registrarCurso(crearCursoValido());
                    Utilidades.mostrarDialogoSimple("Curso registrado.", 
                            "El curso se creó correctamente en el sistema", Alert.AlertType.INFORMATION);
                }
                irAVistaCursos();
            } else {
                lbTituloFormulario.requestFocus();
                lbMensajeDeError.setText("No puedes seleccionar una fecha de fin de clases posterior a la de fin del periodo");
                btnAceptar.setDisable(true);
            }
        }
    }
    
    private boolean validarFechas() {
        boolean fechasValidas = false;
        if (cmbPeriodosEscolares.getSelectionModel().getSelectedItem() != null) {
            LocalDate fechaFinPeriodoEscolar = LocalDate.parse(cmbPeriodosEscolares.getSelectionModel().getSelectedItem().getFechaFinPeriodoEscolar());
            if (!dpFechaFinClases.getValue().isAfter(fechaFinPeriodoEscolar)) {
                fechasValidas = true;
            }
        }
        return fechasValidas;
    }
    
    private Curso crearCursoValido() {
        Curso curso = new Curso();
        curso.setNombreCurso(tfNombreCurso.getText());
        curso.setIdPeriodoEscolar(cmbPeriodosEscolares.getSelectionModel().getSelectedItem().getIdPeriodoEscolar());
        curso.setIdExperienciaEducativa(cmbExperienciasEducativas.getSelectionModel().getSelectedItem().getIdExperienciaEducativa());
        curso.setIdNRC(cmbNrcs.getSelectionModel().getSelectedItem().getIdNrc());
        curso.setIdSeccion(cmbSecciones.getSelectionModel().getSelectedItem().getIdSeccion());
        curso.setIdBloque(cmbBloques.getSelectionModel().getSelectedItem().getIdBloque());
        curso.setIdAcademico(profesorSeleccionado.getIdAcademico());
        curso.setFechaInicioCurso(dpFechaInicioClases.getValue().toString());
        curso.setFechaFinCurso(dpFechaFinClases.getValue().toString());
        curso.setIdEstadoCurso(1);
        return curso;
    }
    
    private void registrarCurso(Curso cursoRegistro) {
        CursoDAO cursoDAO = new CursoDAO();
        try {
            cursoDAO.guardarCurso(cursoRegistro);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void actualizarCurso(Curso cursoModificado) {
        CursoDAO cursoDAO = new CursoDAO();
        cursoModificado.setIdCurso(cursoEdicion.getIdCurso());
        try {
            cursoDAO.actualizarCurso(cursoModificado);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void inicializarCamposDeBusqueda() {
        cmbExperienciasEducativas.valueProperty().addListener(new ChangeListener<ExperienciaEducativa>(){
            @Override
            public void changed(ObservableValue<? extends ExperienciaEducativa> observable, 
                    ExperienciaEducativa oldValue, ExperienciaEducativa newValue) {
                if (newValue != null) {
                    cargarNrcs(newValue.getIdExperienciaEducativa());
                }
            }
        });
        CampoDeBusqueda<Academico> campoBusquedaProfesor = new CampoDeBusqueda<Academico> (tfProfesor, lvprofesores,
                profesores, profesorSeleccionado, new INotificacionSeleccionItem<Academico>() {
            @Override
            public void notificarSeleccionItem(Academico itemSeleccionado) {
                profesorSeleccionado = itemSeleccionado;
            }

            @Override
            public void notificarPerdidaDelFoco() {

            }
        });
    }
    
    private void agregarListenerCampos(){
        agregarListenerCampoVacio(tfNombreCurso);
        agregarListenerCampoVacio(tfProfesor);
        agregarListenerDatePicker(dpFechaFinClases);
        agregarListenerDatePicker(dpFechaInicioClases);
        agregarListenerComboBox(cmbBloques);
        agregarListenerComboBox(cmbExperienciasEducativas);
        agregarListenerComboBox(cmbNrcs);
        agregarListenerComboBox(cmbSecciones);
        agregarListenerComboBox(cmbBloques);
    }
    
    private void agregarListenerComboBox(ComboBox comboBox) {
        comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                comboBox.setStyle("-fx-border-color: gray");
                lbMensajeDeError.setText("");
            }
            if (oldValue) {
                if (comboBox.getSelectionModel().getSelectedItem() == null) {
                    comboBox.setStyle("-fx-border-color: red");
                    lbMensajeDeError.setText("Campos obligatorios vacios.");
                }
                if (validarCamposObligatoriosLlenos()) {
                    btnAceptar.setDisable(false);
                } else {
                    btnAceptar.setDisable(true);
                }
            } 
        });
    }
    
    private void agregarListenerDatePicker(DatePicker dp) {
        dp.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                dp.setStyle("-fx-border-color: gray");
                lbMensajeDeError.setText("");
            }
            if (oldValue) {
                if (dp.getValue() == null) {
                    dp.setStyle("-fx-border-color: red");
                    lbMensajeDeError.setText("Campos obligatorios vacios.");
                }
                if (validarCamposObligatoriosLlenos()) {
                    btnAceptar.setDisable(false);
                } else {
                    btnAceptar.setDisable(true);
                }
            }
        });
    }
    
    private void agregarListenerCampoVacio(TextInputControl campoTexto){
        campoTexto.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, 
                Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                campoTexto.setStyle("-fx-border-color: gray");
                lbMensajeDeError.setText("");
            }
            if (oldValue) {
                if (campoTexto.getText().trim().isEmpty()) {
                    campoTexto.setStyle("-fx-border-color: red");
                    lbMensajeDeError.setText("Campos obligatorios vacios.");
                }
                if (validarCamposObligatoriosLlenos()) {
                    btnAceptar.setDisable(false);
                } else {
                    btnAceptar.setDisable(true);
                }
            }
        });
    }
    
    private boolean validarCamposObligatoriosLlenos() {
        boolean camposLlenos = false;
        if ((!tfNombreCurso.getText().trim().isEmpty())
                && cmbExperienciasEducativas.getSelectionModel().getSelectedItem()!= null
                && cmbNrcs.getSelectionModel().getSelectedItem() != null
                && cmbPeriodosEscolares.getSelectionModel().getSelectedItem() != null
                && cmbSecciones.getSelectionModel().getSelectedItem() != null
                && cmbBloques.getSelectionModel().getSelectedItem() != null
                && profesorSeleccionado != null
                && dpFechaFinClases.getValue() != null
                && dpFechaInicioClases.getValue() != null) {
            camposLlenos = true;
        }
        return camposLlenos;
    }

    @FXML
    private void clicSeleccionCombo(ActionEvent event) {
        if (validarCamposObligatoriosLlenos()) {
            btnAceptar.setDisable(false);
        }
    }

    @FXML
    private void activarBotonAceptar(KeyEvent event) {
    }

    @FXML
    private void clicBtnVolver(MouseEvent event) {
        if(vieneDeDetalles) {
            irAVistaDetallesCursos();
        }else {
            irAVistaCursos();
        }        
    }
    
    private void irAVistaCursos() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCursos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCursosController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setUsuario(academico);
            Stage escenario = (Stage) lbTituloFormulario.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Usuarios");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
     private void irAVistaDetallesCursos() {
       interfaz.notitficacionRecargarDatos();
       Stage escenario = (Stage) lbTituloFormulario.getScene().getWindow();
       escenario.close();
    }
    
}
