/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 25/05/2023
 * Descripción: La clase FXMLFormularioAnteproyectoController actúa como controlador
 * de la vista FormularioAnteproyecto. Contiene los métodos necesarios 
 * para la creación, modificación y correción de un anteproyecto. 
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.CuerpoAcademicoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstadoSeguimientoDAO;
import javafxsastr.modelos.dao.LgacDAO;
import javafxsastr.modelos.dao.ModalidadDAO;
import javafxsastr.modelos.dao.RevisionAnteproyectoDAO;
import javafxsastr.modelos.dao.RubricaDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.CuerpoAcademico;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.modelos.pojo.Modalidad;
import javafxsastr.modelos.pojo.RevisionAnteproyecto;
import javafxsastr.modelos.pojo.Rubrica;
import javafxsastr.utils.CampoDeBusqueda;
import javafxsastr.utils.Utilidades;

public class FXMLFormularioAnteproyectoController implements Initializable {

    Academico academico = null;
    @FXML
    private Label lbMesCreacion;
    @FXML
    private Label lbAñoCreacion;
    @FXML
    private TextField tfCiudad;
    @FXML
    private Label lbFechaCreacion;
    @FXML
    private TextField tfCuerpoAcademico;
    @FXML
    private TextArea txaNombreProyectoInvestigacion;
    @FXML
    private ComboBox<Modalidad> cmbxModalidadTrabajoRecepcional;
    @FXML
    private TextField tfDuracionAproximada;
    @FXML
    private TextField tfLineaInvestigacion;
    @FXML
    private Label lbNombreDirector;
    @FXML
    private TextField tfCodirector;
    @FXML
    private ComboBox cmbxNumeroAlumnos;
    @FXML
    private TextArea txaDescripcionProyectoInvestigacion;
    @FXML
    private TextArea txaDescripcionTrabajoRecepcional;
    @FXML
    private TextArea txaResultados;
    @FXML
    private TextArea txaNotasExtra;
    @FXML
    private TextArea tfNombreTrabajoRecepcional;
    @FXML
    private TextArea tfRequisitos;
    @FXML
    private TextArea tfBibliografia;
    @FXML
    private Button btnEnviar;
    private ObservableList<CuerpoAcademico> cuerposAcademicos;
    private CuerpoAcademico cuerpoSeleccionado;
    @FXML
    private ListView<CuerpoAcademico> lvCuerposAcademicosBusqueda;
    private ObservableList<Academico> codirectores;
    private ObservableList<Modalidad> modalidades;
    @FXML
    private ListView<Academico> lvCodirectores;
    private ObservableList<Lgac> lgacs = FXCollections.observableArrayList();
    @FXML
    private VBox vbxContenedorLgac;
    @FXML
    private ComboBox<Lgac> cmbxLgacs;
    private ObservableList<Lgac> lgacsSeleccionadas = FXCollections.observableArrayList();
    private ObservableList<Academico> codirectoresSeleccionado = FXCollections.observableArrayList();
    @FXML
    private VBox vbxContenedorDirectores;
    private boolean esCorreccion;
    private Anteproyecto anteproyectoCorrecion;
    @FXML
    private Label lbAdvertenciaCuerpoAcademico;
    @FXML
    private Label lbAdvertenciaNombreTR;
    @FXML
    private Label lbAdvertenciaRequisitos;
    @FXML
    private Label lbAdvertenciaDuracionAproximada;
    @FXML
    private Label lbAdvertenciaDescripcionTR;
    @FXML
    private Label lbAdvertenciaResultados;
    @FXML
    private Label lbAdvertenciaBibliografia;
    @FXML
    private Label lbTituloVentana;
    @FXML
    private Pane contenedorNotasPorDefecto;
    @FXML
    private Pane contenedorNotasExtra;
    @FXML
    private Pane contenedorRubrica;
    @FXML
    private ToggleGroup LGACS;
    @FXML
    private ToggleGroup NombreTR;
    @FXML
    private ToggleGroup DescripcionTR;
    @FXML
    private ToggleGroup RequisitosAnteproyecto;
    @FXML
    private ToggleGroup ResultadosEsperados;
    @FXML
    private ToggleGroup BibliografiasRecomendadas;
    @FXML
    private ToggleGroup Redaccion;
    @FXML
    private Pane contenedorComentariosProfesor;
    @FXML
    private TextArea txaComentariosProfesor;
    private final String MESES[] = 
        {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
        "Diciembre"};
    @FXML
    private Label lbAdvertenciaCiudad;
    @FXML
    private Pane btnEliminar;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarListenersACamposObligatorios();
        cargarModalidades();
        obtenerCuerposAcademicos();
        obtenerCodirectores();
        configurarCampoDeBusquedaCuerposAcademicos();
        configurarCampoDeBusquedaCodirectores();
        cmbxNumeroAlumnos.setItems(FXCollections.observableArrayList(1, 2, 3));
    }
    
    public void inicializarInformacionFormulario(Anteproyecto anteproyectoCorrecion, boolean esEdicion) {
        this.esCorreccion = esEdicion;
        this.anteproyectoCorrecion = anteproyectoCorrecion;
        if (this.anteproyectoCorrecion == null) {
            btnEnviar.setDisable(true);
            lbTituloVentana.setText("Crear anteproyecto");
            mostrarFechaActual();
            contenedorNotasPorDefecto.setVisible(true);
            contenedorNotasExtra.setVisible(true);
        } else if ("Borrador".equals(this.anteproyectoCorrecion.getEstadoSeguimiento())) {
            this.anteproyectoCorrecion = anteproyectoCorrecion;
            lbTituloVentana.setText("Editar borrador de anteproyecto");
            setDatosAnteproyecto();
            contenedorNotasExtra.setVisible(true);
            contenedorNotasPorDefecto.setVisible(true);
            btnEliminar.setVisible(true);
        } else if ("Rechazado".equals(this.anteproyectoCorrecion.getEstadoSeguimiento())) {
            this.anteproyectoCorrecion = anteproyectoCorrecion;
            lbTituloVentana.setText("Corregir anteproyecto");
            setDatosRevision();
            contenedorRubrica.setVisible(true);
            contenedorComentariosProfesor.setVisible(true);
            setDatosAnteproyecto();
            btnEliminar.setVisible(true);
        }
    }
    
    /* 
    Asigna al academico su valor para obtener el id del 
    academico y saber de quien será el anteproyecto creado 
    */
    public void setAcademico(Academico academico) {
        this.academico = academico;
    }
    
    private void setDatosRevision() {
        try {
            RevisionAnteproyecto revisionAnteproyecto =
                    new RevisionAnteproyectoDAO().obtenerRevisionAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
            Rubrica rubricaRevision = new RubricaDAO().obtenerRubricaPorId(revisionAnteproyecto.getIdRubrica());
            LGACS.selectToggle(LGACS.getToggles().get(rubricaRevision.getValorLineasGeneracionAplicacionConocimiento()));
            NombreTR.selectToggle(NombreTR.getToggles().get(rubricaRevision.getValorNombreTrabajoRecepcional()));
            DescripcionTR.selectToggle(DescripcionTR.getToggles().get(rubricaRevision.getValorDescripcionTrabajoRecepcional()));
            RequisitosAnteproyecto.selectToggle(RequisitosAnteproyecto.getToggles().get(rubricaRevision.getValorRequisitosAnteproyecto()));
            ResultadosEsperados.selectToggle(ResultadosEsperados.getToggles().get(rubricaRevision.getValorResultadosEsperados()));
            BibliografiasRecomendadas.selectToggle(
                    BibliografiasRecomendadas.getToggles().get(rubricaRevision.getValorBibliografiasRecomendadas()));
            Redaccion.selectToggle(Redaccion.getToggles().get(rubricaRevision.getValorRedaccion()));
            txaComentariosProfesor.setText(revisionAnteproyecto.getComentarios());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void setDatosAnteproyecto() {
        LocalDate fechaCreacion = LocalDate.parse(anteproyectoCorrecion.getFechaCreacion());
        lbMesCreacion.setText(MESES[fechaCreacion.getMonthValue()-1]);
        lbAñoCreacion.setText(String.valueOf(fechaCreacion.getYear()));
        lbFechaCreacion.setText(fechaCreacion.getDayOfMonth() + " de " +
                MESES[fechaCreacion.getMonthValue()-1] + " de " + fechaCreacion.getYear());
        if (anteproyectoCorrecion.getCiudadCreacion() != null) {
            tfCiudad.setText(anteproyectoCorrecion.getCiudadCreacion());
        }
        if (anteproyectoCorrecion.getIdCuerpoAcademico() > 0) {
            cargarCuerpoAcademicoBorradorDeAnteproyecto(anteproyectoCorrecion.getIdCuerpoAcademico());
        }
        cargarLgacsBorradorDeAnteproyecto();
        if (anteproyectoCorrecion.getNombreProyectoInvestigacion()!= null) {
            txaNombreProyectoInvestigacion.setText(anteproyectoCorrecion.getNombreProyectoInvestigacion());
        }
        if (anteproyectoCorrecion.getNombreTrabajoRecepcional() != null) {
            tfNombreTrabajoRecepcional.setText(anteproyectoCorrecion.getNombreTrabajoRecepcional());
        }
        if (anteproyectoCorrecion.getLineaInvestigacion() != null) {
            tfLineaInvestigacion.setText(anteproyectoCorrecion.getLineaInvestigacion());
        }
        if (anteproyectoCorrecion.getRequisitos() != null) {
            tfRequisitos.setText(anteproyectoCorrecion.getRequisitos());
        }
        if (anteproyectoCorrecion.getDuracionAproximada() != null) {
            tfDuracionAproximada.setText(anteproyectoCorrecion.getDuracionAproximada());
        }
        if (anteproyectoCorrecion.getIdModalidad()>0) {
            cargarModalidadBorradorDeAnteproyecto(anteproyectoCorrecion.getIdModalidad());
        }
        if (anteproyectoCorrecion.getNumeroMaximoAlumnosParticipantes()> 0) {
            cmbxNumeroAlumnos.getSelectionModel().select(anteproyectoCorrecion.getNumeroMaximoAlumnosParticipantes());
        }
        if (anteproyectoCorrecion.getDescripcionProyectoInvestigacion() != null) {
            txaDescripcionProyectoInvestigacion.setText(anteproyectoCorrecion.getDescripcionProyectoInvestigacion());
        }
        if (anteproyectoCorrecion.getDescripcionTrabajoRecepcional() != null) {
            txaDescripcionTrabajoRecepcional.setText(anteproyectoCorrecion.getDescripcionTrabajoRecepcional());
        }
        if (anteproyectoCorrecion.getResultadosEsperadosAnteproyecto() != null) {
            txaResultados.setText(anteproyectoCorrecion.getResultadosEsperadosAnteproyecto());
        }
        if (anteproyectoCorrecion.getBibliografiaRecomendada() != null) {
            tfBibliografia.setText(anteproyectoCorrecion.getBibliografiaRecomendada());
        }
        if (anteproyectoCorrecion.getNotasExtras() != null) {
            txaNotasExtra.setText(anteproyectoCorrecion.getNotasExtras());
        }
    }
    
    private void cargarLgacsBorradorDeAnteproyecto() {
        try {
            if (cuerpoSeleccionado != null) {
                obtenerLgacs(cuerpoSeleccionado.getIdCuerpoAcademico());
            }
            ArrayList<Lgac> lgacs = 
                    new LgacDAO().obtenerInformacionLGACsPorAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
            lgacsSeleccionadas.addAll(lgacs);
            for (Lgac lgacsSeleccionada : lgacsSeleccionadas) {
                configurarComponenteLgacSeleccionada(lgacsSeleccionada);
            }
            FilteredList<Lgac> filtro = configurarFiltroLgac();
            System.out.println(filtro);
            cmbxLgacs.setItems(filtro);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarCodirectoresBorradorDeAnteproyecto() {
        try {
            ArrayList<Academico> codirectores = 
                    new AcademicoDAO().obtenerCodirectores(anteproyectoCorrecion.getIdAnteproyecto());
            codirectoresSeleccionado.addAll(codirectores);
            for (Academico academico : codirectoresSeleccionado) {
                configurarComponenteCodirectorSeleccionado(academico);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void cargarCuerpoAcademicoBorradorDeAnteproyecto(int idCuerpoAcademico) {
        for (CuerpoAcademico cuerpoAcademico : cuerposAcademicos) {
            if (cuerpoAcademico.getIdCuerpoAcademico() == anteproyectoCorrecion.getIdCuerpoAcademico()) {
                cuerpoSeleccionado = cuerpoAcademico;
                tfCuerpoAcademico.setText(cuerpoSeleccionado.toString());
                lvCuerposAcademicosBusqueda.getSelectionModel().select(cuerpoAcademico);
                lvCuerposAcademicosBusqueda.setVisible(false);
                break;
            }
        }
    }
    
    private void cargarModalidadBorradorDeAnteproyecto(int idModalidad) {
        for (Modalidad modalidad : modalidades) {
            if (modalidad.getIdModalidad() == anteproyectoCorrecion.getIdModalidad()) {
                cmbxModalidadTrabajoRecepcional.getSelectionModel().select(modalidad);
                break;
            }
        }
    }
    
    private void agregarListenersACamposObligatorios() {
        agregarListenerValidadorCampoVacio(tfCiudad, lbAdvertenciaCiudad);
        agregarListenerValidadorCampoVacio(tfNombreTrabajoRecepcional, lbAdvertenciaNombreTR);
        agregarListenerValidadorCampoVacio(tfDuracionAproximada, lbAdvertenciaDuracionAproximada);
        agregarListenerValidadorCampoVacio(tfRequisitos, lbAdvertenciaRequisitos);
        agregarListenerValidadorCampoVacio(txaDescripcionTrabajoRecepcional, lbAdvertenciaDescripcionTR);
        agregarListenerValidadorCampoVacio(txaResultados, lbAdvertenciaResultados);
        agregarListenerValidadorCampoVacio(tfBibliografia, lbAdvertenciaBibliografia);
    }

    private void cargarModalidades(){
        obtenerModalidades();
        cmbxModalidadTrabajoRecepcional.setItems(modalidades);
    }
    
    private void cargarLgacs(int idCuerpoAcademico){
        obtenerLgacs(idCuerpoAcademico);
        if (esCorreccion) {
            configurarFiltroLgac();
        } else {
            cmbxLgacs.setItems(lgacs);
            cmbxLgacs.setPromptText("Seleccionar LGAC");
        }
    }

    @FXML
    private void clicBtnGuardarBorrador(ActionEvent event) {
        Anteproyecto anteproyecto = prepararAnteproyecto("Borrador");
        if (esCorreccion) {
            anteproyecto.setIdAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
            actualizarAnteproyecto(anteproyecto);
        } else {
            guardarAnteproyecto(anteproyecto);
        }
        Utilidades.mostrarDialogoSimple("Borrador guardado",
                        "Borrador guardado correctamente.", Alert.AlertType.INFORMATION);
        cerrarVentana();
    }

    @FXML
    private void clicBtnEnviarAnteproyecto(ActionEvent event) {
        boolean camposLlenos = validarCamposObligatoriosLLenos();
        if (camposLlenos == true) {
            Anteproyecto anteproyecto = prepararAnteproyecto("Sin revisar");
            if (esCorreccion) {
                anteproyecto.setIdAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
                actualizarAnteproyecto(anteproyecto);
            } else {
                guardarAnteproyecto(anteproyecto);
                Utilidades.mostrarDialogoSimple("Anteproyecto enviado",
                        "Anteproyecto enviado para su aprobación correctamente.", Alert.AlertType.INFORMATION);
            }
            cerrarVentana();
        }
    }
    
    /* 
    Prepara un objeto de tipo anteproyecto a partir de los valores en los campos de texto
    para ser usado en el guardado, actualizacion o modificacion.
    */
    private Anteproyecto prepararAnteproyecto(String estadoSeguimiento) {
        String ciudad = tfCiudad.getText();
        LocalDate fecha = LocalDate.now();
        int idCuerpoAcademico = -1;
        if (cuerpoSeleccionado != null) {
            idCuerpoAcademico = cuerpoSeleccionado.getIdCuerpoAcademico();
        }
        String nombreProyectoInvestigacion = txaNombreProyectoInvestigacion.getText();
        String nombreTrabajoRecepcional = tfNombreTrabajoRecepcional.getText();
        String lineaInvestigacion = tfLineaInvestigacion.getText();
        String requisitos = tfRequisitos.getText();
        String duracionAproximada = tfDuracionAproximada.getText();
        int idModalidad = -1;
        if (cmbxModalidadTrabajoRecepcional.getSelectionModel().getSelectedItem() != null){
            idModalidad = cmbxModalidadTrabajoRecepcional.getSelectionModel().getSelectedItem().getIdModalidad();
        }
        int idDirector = academico.getIdAcademico();
        int numeroAlumnosParticipantes = cmbxNumeroAlumnos.getSelectionModel().getSelectedIndex();
        String descripcionProyectoInvestigacion = txaDescripcionProyectoInvestigacion.getText();
        String descripcionTrabajoRecepcional = txaDescripcionTrabajoRecepcional.getText();
        String resultados = txaResultados.getText();
        String bibliografiaRecomendada = tfBibliografia.getText();
        String notasExtra = txaNotasExtra.getText();
        int idEstadoSeguimiento = -1;
        try {
            idEstadoSeguimiento = new EstadoSeguimientoDAO().obtenerIdEstadoSeguimiento(estadoSeguimiento);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        
        Anteproyecto anteproyectoValidado = new Anteproyecto();
        anteproyectoValidado.setFechaCreacion(fecha.toString());
        anteproyectoValidado.setNombreProyectoInvestigacion(nombreProyectoInvestigacion);
        anteproyectoValidado.setCiudadCreacion(ciudad);
        anteproyectoValidado.setLineaInvestigacion(lineaInvestigacion);
        anteproyectoValidado.setDuracionAproximada(duracionAproximada);
        anteproyectoValidado.setNombreTrabajoRecepcional(nombreTrabajoRecepcional);
        anteproyectoValidado.setRequisitos(requisitos);
        anteproyectoValidado.setNumeroMaximoAlumnosParticipantes(numeroAlumnosParticipantes);
        anteproyectoValidado.setDescripcionProyectoInvestigacion(descripcionProyectoInvestigacion);
        anteproyectoValidado.setDescripcionTrabajoRecepcional(descripcionTrabajoRecepcional);
        anteproyectoValidado.setResultadosEsperadosAnteproyecto(resultados);
        anteproyectoValidado.setBibliografiaRecomendada(bibliografiaRecomendada);
        anteproyectoValidado.setNotasExtras(notasExtra);
        anteproyectoValidado.setIdAcademico(academico.getIdAcademico());
        anteproyectoValidado.setIdEstadoSeguimiento(idEstadoSeguimiento);
        anteproyectoValidado.setIdCuerpoAcademico(idCuerpoAcademico);
        anteproyectoValidado.setIdModalidad(idModalidad);
        return anteproyectoValidado;
    }
    
    private void guardarAnteproyecto(Anteproyecto anteproyecto) {
        AnteproyectoDAO anteproyectoDAO = new AnteproyectoDAO();
        try {
            int idAnteproyecto = anteproyectoDAO.guardarAnteproyecto(anteproyecto);
            guardarLgacsAnteproyecto(idAnteproyecto);
            guardarCodirectoresAnteproyecto(idAnteproyecto);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void actualizarAnteproyecto(Anteproyecto anteproyecto) {
        AnteproyectoDAO anteproyectoDAO = new AnteproyectoDAO();
        try {
            int idAnteproyecto = anteproyectoDAO.actualizarAnteproyecto(anteproyecto);
            anteproyectoDAO.eliminarLgacsAnteproyecto(idAnteproyecto);
            anteproyectoDAO.eliminarCodirectoresAnteproyecto(idAnteproyecto);
            guardarLgacsAnteproyecto(idAnteproyecto);
            guardarCodirectoresAnteproyecto(idAnteproyecto);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void eliminarAnteproyecto(int idAnteproyecto) {
        AnteproyectoDAO anteproyectoDAO = new AnteproyectoDAO();
        try {
            int respuesta = anteproyectoDAO.eliminarAnteproyecto(idAnteproyecto);
            anteproyectoDAO.eliminarLgacsAnteproyecto(idAnteproyecto);
            anteproyectoDAO.eliminarCodirectoresAnteproyecto(idAnteproyecto);
            if (respuesta != -1) {
                Utilidades.mostrarDialogoSimple("Borrador eliminado",
                        "El borrador del anteproyecto se ha eliminado correctamente", Alert.AlertType.INFORMATION);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void guardarLgacsAnteproyecto(int idAnteproyecto) {
        AnteproyectoDAO anteproyectoDAO = new AnteproyectoDAO();
        for (Lgac lgac : lgacsSeleccionadas) {
            try {
                anteproyectoDAO.guardarLgacAnteproyecto(idAnteproyecto, lgac.getIdLgac());
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
    }
    
    private void guardarCodirectoresAnteproyecto(int idAnteproyecto) {
        AnteproyectoDAO anteproyectoDAO = new AnteproyectoDAO();
        for (Academico codirector : codirectoresSeleccionado) {
            try {
                anteproyectoDAO.guardarCodirectorAnteproyecto(idAnteproyecto, codirector.getIdAcademico());
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
        }
    }
    
    private void mostrarFechaActual(){
        LocalDate fecha = LocalDate.now();
        String fechaActual = fecha.getDayOfMonth() + " de " + MESES[fecha.getMonthValue()-1] + " de " + fecha.getYear();
        lbFechaCreacion.setText(fechaActual);
        lbMesCreacion.setText(MESES[fecha.getMonthValue()-1]);
        lbAñoCreacion.setText(fecha.getYear() + "");
    }
    
    /*
    Define un listener que escucha cada vez que se entra o sale del campo especificado
    para mostrar un mensaje de error si se deja vacio.
    */
    private void agregarListenerValidadorCampoVacio(TextInputControl campoTexto, Label lbMensajeError){
        campoTexto.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, 
                Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                campoTexto.setStyle("-fx-border-color: gray");
                lbMensajeError.setVisible(false);
            }
            if (oldValue) {
                if (campoTexto.getText().trim().isEmpty()) {
                    campoTexto.setStyle("-fx-border-color: red");
                    lbMensajeError.setVisible(true);
                }
                if (validarCamposObligatoriosLLenos()) {
                    btnEnviar.setDisable(false);
                } else {
                    btnEnviar.setDisable(true);
                }
            }
        });
    }
    
    public void configurarCampoDeBusquedaCodirectores() {
        CampoDeBusqueda<Academico> campoDeBusqueda = new CampoDeBusqueda<Academico>(tfCodirector, lvCodirectores,
            codirectores, academico, new INotificacionSeleccionItem<Academico>() {
            @Override
            public void notificarSeleccionItem(Academico itemSeleccionado) {
                lbNombreDirector.requestFocus();
                Academico codirectorSeleccionado = itemSeleccionado;
                if (codirectorSeleccionado != null && !codirectoresSeleccionado.contains(codirectorSeleccionado)
                        && codirectorSeleccionado != academico) {
                    codirectoresSeleccionado.add(codirectorSeleccionado);
                    tfCodirector.setText("");
                    configurarComponenteCodirectorSeleccionado(codirectorSeleccionado);
                } else {
                    Utilidades.mostrarDialogoSimple("Acción no permitida",
                        "El academico ya ha sido seleccionado o es usted.", Alert.AlertType.INFORMATION);
                }
            }

            @Override
            public void notificarPerdidaDelFoco() {
                validarCamposObligatoriosLLenos();
            }
        });
    }
    
    public void configurarCampoDeBusquedaCuerposAcademicos(){
        CampoDeBusqueda<CuerpoAcademico> campoBusquedaAcademico = new CampoDeBusqueda<CuerpoAcademico>(
                tfCuerpoAcademico, lvCuerposAcademicosBusqueda, cuerposAcademicos, cuerpoSeleccionado,
                new INotificacionSeleccionItem<CuerpoAcademico>() {
            @Override
            public void notificarSeleccionItem(CuerpoAcademico itemSeleccionado) {
                lbAñoCreacion.requestFocus();
                vbxContenedorLgac.getChildren().clear();
                cuerpoSeleccionado = itemSeleccionado;
                cargarLgacs(itemSeleccionado.getIdCuerpoAcademico());
            }

            @Override
            public void notificarPerdidaDelFoco() {
                validarCampoBusqueda(tfCuerpoAcademico, lvCuerposAcademicosBusqueda);
            }
            }
        );       
    }
    
    private boolean validarCamposObligatoriosLLenos() {
        boolean respuesta = false;
        if ((!tfCiudad.getText().trim().isEmpty())
                && cuerpoSeleccionado != null
                && !lgacsSeleccionadas.isEmpty()
                && !codirectoresSeleccionado.isEmpty()
                && (!tfNombreTrabajoRecepcional.getText().trim().isEmpty())
                && (!tfDuracionAproximada.getText().trim().isEmpty())
                && (!tfRequisitos.getText().trim().isEmpty())
                && (cmbxModalidadTrabajoRecepcional.getSelectionModel().getSelectedItem() != null)
                && (!txaDescripcionTrabajoRecepcional.getText().trim().isEmpty())
                && (!txaResultados.getText().trim().isEmpty())
                && (!tfBibliografia.getText().trim().isEmpty())
                && (cmbxNumeroAlumnos.getSelectionModel().getSelectedItem() != null)) {
            respuesta = true;
        }
        return respuesta;
    }
    
    private void obtenerCuerposAcademicos() {
        try {
            cuerposAcademicos = FXCollections.observableArrayList(
                    new CuerpoAcademicoDAO().obtenerCuerposAcademicos()
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void obtenerCodirectores() {
        try {
            codirectores = FXCollections.observableArrayList(
                    new AcademicoDAO().obtenerAcademicos()
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void obtenerLgacs(int idCuerpoAcademico) {
        try {
            lgacs = FXCollections.observableArrayList(
                    new LgacDAO().obtenerInformacionLGACsPorCuerpoAcademico(idCuerpoAcademico)
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void obtenerModalidades() {
        try {
            modalidades = FXCollections.observableArrayList(
                    new ModalidadDAO().obtenerModalidades()
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void validarCampoBusqueda(TextField tfCampo, ListView listView) {
        if (tfCampo.getText().trim().isEmpty()) {
            tfCampo.setStyle("-fx-border-color: red");
            if (tfCampo == tfCuerpoAcademico && cmbxLgacs.getItems().size() > 0) {
                cmbxLgacs.setItems(FXCollections.observableArrayList());
                cmbxLgacs.setPromptText("Selecciona un cuerpo academico.");
                vbxContenedorLgac.getChildren().removeAll(vbxContenedorLgac.getChildren());
                cuerpoSeleccionado = null;
            }
            listView.getSelectionModel().clearSelection();
        }
        if (validarCamposObligatoriosLLenos()) {
            btnEnviar.setDisable(false);
        } else {
            btnEnviar.setDisable(true);
        }
    }  

    @FXML
    private void clicSeleccionLGAC(ActionEvent event) {
        Lgac lgacSeleccionada = cmbxLgacs.getSelectionModel().getSelectedItem();
        if (lgacSeleccionada != null) {
            lgacsSeleccionadas.add(lgacSeleccionada);
            configurarComponenteLgacSeleccionada(lgacSeleccionada);
            configurarFiltroLgac();   
            validarCamposObligatoriosLLenos();
        }
    }
    
    /*
    Configura un filtro que muestra o no las lgacs
    segun hayan sido seleccionadas.
    */
    private FilteredList<Lgac> configurarFiltroLgac(){
        FilteredList<Lgac> filtradoLgacs = new FilteredList(lgacs, p -> true );
        if (lgacs.size() > 0) {
            filtradoLgacs.setPredicate(lgac -> {
                return !lgacsSeleccionadas.contains(lgac);
            });
            SortedList<Lgac> sortedListLgacs = new SortedList<>(filtradoLgacs,
                Comparator.comparing(Lgac::getNombreLgac));
            Platform.runLater(() -> {
                cmbxLgacs.setItems(sortedListLgacs);
            });
        }
        return filtradoLgacs;
    }
    
    private void configurarComponenteLgacSeleccionada(Lgac lgacSeleccionada) {
        Label labelLgac = new Label(lgacSeleccionada.getNombreLgac());
        Button btnEliminar = new Button("X");
        clicEliminarLgacSeleccionado(btnEliminar);
        btnEliminar.setStyle("-fx-background-color: white;");
        btnEliminar.setLayoutX(300);
        btnEliminar.setLayoutY(10);
        labelLgac.setLayoutX(20);
        labelLgac.setLayoutY(15);
        Pane seleccion = new Pane();
        seleccion.getChildren().addAll(labelLgac, btnEliminar);
        seleccion.setPrefSize(370, 50);
        seleccion.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        seleccion.setId(String.valueOf(lgacSeleccionada.getIdLgac()));
        vbxContenedorLgac.getChildren().add(seleccion);
    }
    
    public void clicEliminarLgacSeleccionado(Button boton) {
        boton.setOnAction((event) -> {
            Pane padreBoton = (Pane) boton.getParent();
            Lgac seleccionada = null;
            for (Lgac lgac : lgacsSeleccionadas) {
                if (lgac.getIdLgac() == Integer.valueOf(padreBoton.getId())) {
                    seleccionada = lgac;
                }
            }
            if (seleccionada != null) {
                lgacsSeleccionadas.remove(seleccionada);
                vbxContenedorLgac.getChildren().remove(padreBoton);
                configurarFiltroLgac();
            }
        });
    }

    private void configurarComponenteCodirectorSeleccionado(Academico codirectorSeleccionado) {
        Label labelLgac = new Label(codirectorSeleccionado.toString());
        Button btnEliminar = new Button("X");
        clicEliminarCodirectorSeleccionado(btnEliminar);
        btnEliminar.setStyle("-fx-background-color: white;");
        btnEliminar.setLayoutX(440);
        btnEliminar.setLayoutY(10);
        labelLgac.setLayoutX(20);
        labelLgac.setLayoutY(15);
        Pane seleccion = new Pane();
        seleccion.getChildren().addAll(labelLgac, btnEliminar);
        seleccion.setId(String.valueOf(codirectorSeleccionado.getIdAcademico()));
        seleccion.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        seleccion.setPrefHeight(50);
        vbxContenedorDirectores.getChildren().add(seleccion);
    }
       
    public void clicEliminarCodirectorSeleccionado(Button boton) {
        boton.setOnAction((event) -> {
            Pane padreBoton = (Pane) boton.getParent();
            Academico seleccionado = null;
            for (Academico academico : codirectoresSeleccionado) {
                if (academico.getIdAcademico() == Integer.valueOf(padreBoton.getId())) {
                    seleccionado = academico;
                }
            }
            if (seleccionado != null) {
                codirectoresSeleccionado.remove(seleccionado);
                vbxContenedorDirectores.getChildren().remove(padreBoton);
                lbNombreDirector.requestFocus();
            }
        });
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicEliminarBorrador(MouseEvent event) {
        boolean respuesta = Utilidades.mostrarDialogoConfirmacion("Eliminar borrador de anteproyecto.",
                "¿Estás seguro de que deseas eliminar el borrador del anteproyecto?");
        if (respuesta == true) {
            eliminarAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
            cerrarVentana();
        }
    }
    
    private void cerrarVentana() {
        irAVistaAnteproyectos(academico);
    }
    
    private void irAVistaAnteproyectos(Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAnteproyectos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAnteproyectosController controladorVistaAnteproyectos = accesoControlador.getController();
            controladorVistaAnteproyectos.setAcademico(academico, false);
            Stage escenario = (Stage) lbAñoCreacion.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void activarBotonEnviar(KeyEvent event) {
        if (validarCamposObligatoriosLLenos()) {
            btnEnviar.setDisable(false);
        }
    }

    @FXML
    private void activaBotonEnviar(KeyEvent event) {
        if (validarCamposObligatoriosLLenos()) {
            btnEnviar.setDisable(false);
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
    
}
