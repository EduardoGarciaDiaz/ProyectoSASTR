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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionRecargarDatos;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.*;
import javafxsastr.modelos.pojo.*;
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
    private ComboBox<Seccion> cmbSecciones;
    @FXML
    private ComboBox<Bloque> cmbBloques;
    @FXML
    private DatePicker dpFechaInicioClases;
    @FXML
    private DatePicker dpFechaFinClases;
    @FXML
    private TextField tfProfesor;
    @FXML
    private ListView<Academico> lvprofesores;
    @FXML
    private Label lbTituloFormulario;
    @FXML
    private Button btnAceptar;
    @FXML
    private Label lbMensajeDeError;
    private PeriodoEscolar periodoActual;
    @FXML
    private Label lbPeriodoActual;    
    
    private Usuario usuario;
    private ExperienciaEducativa experienciaEdicion;
    private Nrc nrcEdicion;
    private Seccion secccionEdicion;
    private Bloque bloqueEdicion;
    private ObservableList<ExperienciaEducativa> experienciasEducativas;
    private ObservableList<Nrc> nrcs;
    private Academico profesorSeleccionado;
    private ObservableList<Academico> profesores;
    private ObservableList<Seccion> secciones;
    private ObservableList<Bloque> bloques;
    private boolean esEdicion;
    private Curso cursoEdicion;
    private INotificacionRecargarDatos interfaz;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarExperienciasEducativas();
        obtenerProfesores();
        cargarPeriodoActual();
        cargarSecciones();
        cargarbloques();
        agregarListenerCampos();
        inicializarCamposDeBusqueda();
    }    
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        recuperarDatosCurso();
    }

    public void inicializarInformacionFormulario(boolean esEdicion, Curso cursoEdicion, INotificacionRecargarDatos interfazN) {
        this.interfaz = interfazN;
        this.esEdicion = esEdicion;
        this.cursoEdicion = cursoEdicion;
        if (esEdicion) {
            lbTituloFormulario.setText("Modificar curso");
            recuperarDatosCurso();
            lvprofesores.setItems(profesores);
            setInformacionCursoEdicion(cursoEdicion);
        } else {
            lbTituloFormulario.setText("Añadir curso");
            cargarPeriodoActual();
            lbPeriodoActual.setText(periodoActual.toString());
            btnAceptar.setDisable(true);
        }
    }
    
    private void recuperarDatosCurso() {
        cargarExperienciasEducativas();
        obtenerProfesores();
        cargarSecciones();
        cargarbloques();
        agregarListenerCampos();
        btnAceptar.setDisable(true);
        inicializarCamposDeBusqueda();
    }
    
    private void setInformacionCursoEdicion(Curso cursoEdicion) {
        tfNombreCurso.setText(cursoEdicion.getNombreCurso());
        for (ExperienciaEducativa experienciasEducativa : experienciasEducativas) {
            if (cursoEdicion.getIdExperienciaEducativa() == experienciasEducativa.getIdExperienciaEducativa()) {
                cmbExperienciasEducativas.getSelectionModel().select(experienciasEducativa);
                experienciaEdicion = experienciasEducativa;
            }
        }
        for (Nrc nrc : nrcs) {
            if (cursoEdicion.getIdNRC() == nrc.getIdNrc()) {
                cmbNrcs.getSelectionModel().select(nrc);
                nrcEdicion = nrc;
            }
        }
        try {
            periodoActual = new PeriodoEscolarDAO().obtenerPeriodoPorId(cursoEdicion.getIdPeriodoEscolar());
            lbPeriodoActual.setText(periodoActual.toString());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        for (Seccion seccion : secciones) {
            if (seccion.getIdSeccion() == cursoEdicion.getIdSeccion()) {
                cmbSecciones.getSelectionModel().select(seccion);
                secccionEdicion = seccion;
            }
        }
        for (Bloque bloque : bloques) {
            if (bloque.getIdBloque() == cursoEdicion.getIdBloque()) {
                cmbBloques.getSelectionModel().select(bloque);
                bloqueEdicion = bloque;
            }
        }
        for (Academico profesor : profesores) {
            if (profesor.getIdAcademico() == cursoEdicion.getIdAcademico()) {
                lvprofesores.getSelectionModel().select(profesor);
                profesorSeleccionado = profesor;
            }
        } 
        tfProfesor.setText(lvprofesores.getSelectionModel().getSelectedItem().toString());
        lvprofesores.setVisible(false);
        dpFechaInicioClases.setValue(LocalDate.parse(cursoEdicion.getFechaInicioCurso()));
        dpFechaFinClases.setValue(LocalDate.parse(cursoEdicion.getFechaFinCurso()));
    }
    
    private void obtenerProfesores() {
        try {
            profesores = FXCollections.observableArrayList(
                new AcademicoDAO().obtenerAcademicos()
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarExperienciasEducativas() {
        try {
            experienciasEducativas = FXCollections.observableArrayList(
                    new ExperienciaEducativaDAO().obtenerExperienciasEducativas()
            );
            cmbExperienciasEducativas.setItems(experienciasEducativas);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarNrcs(int idExperienciaEducativa) {
        try {
            nrcs = FXCollections.observableArrayList(
                new NrcDAO().obtenerNRCSDisponiblesPorEE(idExperienciaEducativa)
            );
            cmbNrcs.setItems(nrcs);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarPeriodoActual() {
        try {
            periodoActual = new PeriodoEscolarDAO().obtenerPeriodoActual();
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarSecciones() {
        try {
            secciones = FXCollections.observableArrayList(new SeccionDAO().obtenerSecciones());
            cmbSecciones.setItems(secciones);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarbloques() {
        try {
            bloques = FXCollections.observableArrayList(new BloqueDAO().obtenerBloques());
            cmbBloques.setItems(bloques);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        boolean respuesta = Utilidades.mostrarDialogoConfirmacion("Confirmación", 
                "¿Estás seguro de que deseas cancelar la creación del curso");
        if (respuesta) {
            cerrarVentana();
        }
    }

    @FXML
    private void clicBtnAceptar(ActionEvent event) {
        if (validarCamposObligatoriosLlenos()) {
            if (validarFechas()){
                if (tfNombreCurso.getText().trim().length() < 40) {
                    if (esEdicion) {
                        actualizarCurso(crearCursoValido());
                        Utilidades.mostrarDialogoSimple("Curso modificado.", 
                            "Cambios realizados correctamente.", Alert.AlertType.INFORMATION);
                    } else {
                        registrarCurso(crearCursoValido());
                        Utilidades.mostrarDialogoSimple("Curso registrado.", 
                            "El curso se creó correctamente en el sistema", Alert.AlertType.INFORMATION);
                    }
                    cerrarVentana();
                } else {
                    lbMensajeDeError.setText("El nombre del curso no puede tener más de 40 caracteres.");
                }
            } else {
                lbTituloFormulario.requestFocus();
                lbMensajeDeError.setText("No puedes seleccionar una fecha de fin de clases posterior a la de fin del periodo"
                        + " ni una previa a la de inicio del periodo");
                btnAceptar.setDisable(true);
            }
        }
    }
    
    private boolean validarFechas() {
        boolean fechasValidas = true;
        if (periodoActual != null) {
            LocalDate fechaFinPeriodoEscolar = LocalDate.parse(periodoActual.getFechaFinPeriodoEscolar());
            LocalDate fechaInicioPeriodoEscolar = LocalDate.parse(periodoActual.getFechaInicioPeriodoEscolar());
            LocalDate fechaInicioClases = dpFechaInicioClases.getValue();
            LocalDate fechaFinClases = dpFechaFinClases.getValue();
            if (fechaInicioClases.isBefore(fechaInicioPeriodoEscolar) || fechaInicioClases.isAfter(fechaFinClases)) {
                fechasValidas = false;
            }
            if (fechaFinClases.isBefore(fechaInicioClases) || fechaFinClases.isAfter(fechaFinPeriodoEscolar)) {
                fechasValidas = false;
            }
        }
        return fechasValidas;
    }
    
    private Curso crearCursoValido() {
        Curso curso = new Curso();
        curso.setNombreCurso(tfNombreCurso.getText().trim().replaceAll(" +", " "));
        curso.setIdPeriodoEscolar(periodoActual.getIdPeriodoEscolar());
        curso.setIdExperienciaEducativa(cmbExperienciasEducativas.getSelectionModel()
                .getSelectedItem()
                .getIdExperienciaEducativa());
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
        cursoModificado.setIdEstadoCurso(cursoEdicion.getIdEstadoCurso());
        try {
            cursoDAO.actualizarCurso(cursoModificado);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void inicializarCamposDeBusqueda() {
        cmbExperienciasEducativas.valueProperty().addListener(
            (ObservableValue<? extends ExperienciaEducativa> observable, 
                    ExperienciaEducativa oldValue, 
                    ExperienciaEducativa newValue) -> {
                if (newValue != null) {
                cargarNrcs(newValue.getIdExperienciaEducativa());
                }
            }
        );
        CampoDeBusqueda<Academico> campoBusquedaProfesor = new CampoDeBusqueda<> (tfProfesor, lvprofesores,
            profesores, profesorSeleccionado, 
            new INotificacionSeleccionItem<Academico>() {
               @Override
                public void notificarSeleccionItem(Academico itemSeleccionado) {
                    profesorSeleccionado = itemSeleccionado;
                }

                @Override
                public void notificarPerdidaDelFoco() {

                }
            }
        );
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
        comboBox.focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
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
            }
        );
    }
    
    private void agregarListenerDatePicker(DatePicker dp) {
        dp.focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
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
            }
        );
    }
    
    private void agregarListenerCampoVacio(TextInputControl campoTexto){
        campoTexto.focusedProperty().addListener(
            (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (oldValue) {
                    if (campoTexto.getText().isEmpty()) {
                        campoTexto.setStyle("-fx-border-color: red");
                    }
                } else {
                    campoTexto.setStyle("-fx-border-color: gray");
                    lbMensajeDeError.setText("");
                }
            }
        );
        campoTexto.textProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    if (validarCamposObligatoriosLlenos()) {
                     btnAceptar.setDisable(false);
                    } else {
                        btnAceptar.setDisable(true);
                    }
                } else {
                    btnAceptar.setDisable(true);
                }
            }
        );
    }
    
    private boolean validarCamposObligatoriosLlenos() {
        boolean camposLlenos = false;
        if ((!tfNombreCurso.getText().trim().isEmpty())
                && ((cmbExperienciasEducativas.getSelectionModel().getSelectedItem()!= null) || (experienciaEdicion != null))
                && ((cmbNrcs.getSelectionModel().getSelectedItem() != null) || (nrcEdicion != null))
                && ((cmbSecciones.getSelectionModel().getSelectedItem() != null) || (secccionEdicion != null))
                && ((cmbBloques.getSelectionModel().getSelectedItem() != null) || (bloqueEdicion != null))
                && (profesorSeleccionado != null)
                && (dpFechaFinClases.getValue() != null)
                && (dpFechaInicioClases.getValue() != null)) {
            camposLlenos = true;
        }
        return camposLlenos;
    }
    
    private void cerrarVentana() {
        if(esEdicion) {
            irAVistaDetallesCursos(); 
        }else {
            irAVistaCursos();
        } 
    }

    @FXML
    private void clicSeleccionCombo(ActionEvent event) {
        if (validarCamposObligatoriosLlenos()) {
            btnAceptar.setDisable(false);
        }
    }

    @FXML
    private void clicBtnVolver(MouseEvent event) {
        cerrarVentana();
    }
    
    private void irAVistaCursos() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLCursos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLCursosController controladorVistaCursos = accesoControlador.getController();
            controladorVistaCursos.setUsuario(usuario);
            Stage escenario = (Stage) lbTituloFormulario.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Cursos");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
     private void irAVistaDetallesCursos() {
       interfaz.notificacionRecargarDatosPorEdicion(true);
       Stage escenario = (Stage) lbTituloFormulario.getScene().getWindow();
       escenario.close();
    }
     
    private void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                System.out.println("Ocurrió un error de consulta.");
                break;
            case ERROR_CONEXION_BD:
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", Alert.AlertType.ERROR);
            default:
                throw new AssertionError();
        }
    }
    
}
