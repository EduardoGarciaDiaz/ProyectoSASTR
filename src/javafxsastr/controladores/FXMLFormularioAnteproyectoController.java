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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.*;
import javafxsastr.modelos.pojo.*;
import javafxsastr.utils.*;

public class FXMLFormularioAnteproyectoController implements Initializable {

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
    @FXML
    private ListView<CuerpoAcademico> lvCuerposAcademicosBusqueda;
    @FXML
    private ListView<Academico> lvCodirectores;
    @FXML
    private VBox vbxContenedorLgac;
    @FXML
    private ComboBox<Lgac> cmbxLgacs;
    @FXML
    private VBox vbxContenedorDirectores;
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
    @FXML
    private Label lbAdvertenciaCiudad;
    @FXML
    private Pane btnEliminar;
    @FXML
    private Label lbAdventenciaLineaInvestigacion;
    @FXML
    private Label lbAdvertenciaNombreProyecto;
    @FXML
    private Label lbAdvertenciaDescripcionProyecto;
    @FXML
    private Label lbAdvertenciaNotasExtra;
    
    private final String MESES[] = 
        {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
        "Diciembre"};
    private Academico academico = null; 
    private boolean esEdicion;
    private Anteproyecto anteproyectoCorrecion;
    private ObservableList<Lgac> lgacsSeleccionadas = FXCollections.observableArrayList();
    private ObservableList<Academico> codirectoresSeleccionado = FXCollections.observableArrayList();
    private ObservableList<Lgac> lgacs = FXCollections.observableArrayList();
    private ObservableList<Academico> codirectores;
    private ObservableList<Modalidad> modalidades;
    private ObservableList<CuerpoAcademico> cuerposAcademicos;
    private CuerpoAcademico cuerpoSeleccionado;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarListenersACampos();
        agregarFiltros();
        cargarModalidades();
        obtenerCuerposAcademicos();
        configurarCampoDeBusquedaCuerposAcademicos();
        cmbxNumeroAlumnos.setItems(
                FXCollections.observableArrayList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3))
        );
    }
    
    public void inicializarInformacionFormulario(Anteproyecto anteproyectoCorrecion, boolean esEdicion) {
        this.esEdicion = esEdicion;
        this.anteproyectoCorrecion = anteproyectoCorrecion;
        if (this.anteproyectoCorrecion == null) {
            btnEnviar.setDisable(true);
            lbTituloVentana.setText("Crear anteproyecto");
            mostrarFechaActual();
            contenedorNotasPorDefecto.setVisible(true);
            lbNombreDirector.setText(academico.toString());
        } else if ("Borrador".equals(this.anteproyectoCorrecion.getEstadoSeguimiento())) {
            this.anteproyectoCorrecion = anteproyectoCorrecion;
            lbTituloVentana.setText("Editar borrador de anteproyecto");
            setDatosAnteproyecto();
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
   
    public void setAcademico(Academico academico) {
        this.academico = academico;
        obtenerCodirectores();
        configurarCampoDeBusquedaCodirectores();
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
    
    private void obtenerLgacs(int idCuerpoAcademico) {
        try {
            lgacs = FXCollections.observableArrayList(
                new LgacDAO().obtenerInformacionLGACsPorCuerpoAcademico(idCuerpoAcademico)
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
    
    private void obtenerModalidades() {
        try {
            modalidades = FXCollections.observableArrayList(
                new ModalidadDAO().obtenerModalidades()
            );
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void setDatosRevision() {
        try {
            RevisionAnteproyecto revisionAnteproyecto 
                    = new RevisionAnteproyectoDAO().obtenerRevisionAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
            Rubrica rubricaRevision = new RubricaDAO().obtenerRubricaPorId(revisionAnteproyecto.getIdRubrica());
            LGACS.selectToggle(
                    LGACS.getToggles().get(rubricaRevision.getValorLineasGeneracionAplicacionConocimiento())
            );
            NombreTR.selectToggle(
                    NombreTR.getToggles().get(rubricaRevision.getValorNombreTrabajoRecepcional())
            );
            DescripcionTR.selectToggle(
                    DescripcionTR.getToggles().get(rubricaRevision.getValorDescripcionTrabajoRecepcional())
            );
            RequisitosAnteproyecto.selectToggle(
                    RequisitosAnteproyecto.getToggles().get(rubricaRevision.getValorRequisitosAnteproyecto())
            );
            ResultadosEsperados.selectToggle(
                    ResultadosEsperados.getToggles().get(rubricaRevision.getValorResultadosEsperados())
            );
            BibliografiasRecomendadas.selectToggle(
                    BibliografiasRecomendadas.getToggles().get(rubricaRevision.getValorBibliografiasRecomendadas())
            );
            Redaccion.selectToggle(Redaccion.getToggles().get(rubricaRevision.getValorRedaccion()));
            txaComentariosProfesor.setText(revisionAnteproyecto.getComentarios());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void setDatosAnteproyecto() {
        LocalDate fechaCreacion = LocalDate.parse(anteproyectoCorrecion.getFechaCreacion());
        lbMesCreacion.setText(MESES[fechaCreacion.getMonthValue() - 1]);
        lbAñoCreacion.setText(String.valueOf(fechaCreacion.getYear()));
        lbFechaCreacion.setText(fechaCreacion.getDayOfMonth() 
                + " de " + MESES[fechaCreacion.getMonthValue() - 1] 
                + " de " + fechaCreacion.getYear());
        if (anteproyectoCorrecion.getCiudadCreacion() != null) {
            tfCiudad.setText(anteproyectoCorrecion.getCiudadCreacion());
        }
        if (anteproyectoCorrecion.getIdCuerpoAcademico() > 0) {
            cargarCuerpoAcademicoBorradorDeAnteproyecto();
        }
        cargarLgacsBorradorDeAnteproyecto();
        cargarCodirectoresBorradorDeAnteproyecto();
        if (anteproyectoCorrecion.getNombreProyectoInvestigacion() != null) {
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
        if (anteproyectoCorrecion.getIdModalidad() > 0) {
            cargarModalidadBorradorDeAnteproyecto();
        }
        if (anteproyectoCorrecion.getNumeroMaximoAlumnosParticipantes() > 0) {
            cmbxNumeroAlumnos.getSelectionModel().select
                (Integer.valueOf(anteproyectoCorrecion.getNumeroMaximoAlumnosParticipantes())
            );
        }
        if (anteproyectoCorrecion.getNombreDirector() != null) {
            lbNombreDirector.setText(anteproyectoCorrecion.getNombreDirector());
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
        if (cuerpoSeleccionado != null) {
            obtenerLgacs(cuerpoSeleccionado.getIdCuerpoAcademico());
            llenarLgacsSeleccionadas();
            for (Lgac lgacsSeleccionada : lgacsSeleccionadas) {
                clicEliminarLgacSeleccionado(
                    UtilidadesFormularioAnteproyecto.configurarComponenteLgacSeleccionada(lgacsSeleccionada, vbxContenedorLgac)
                );
            }   
            configurarFiltroLgac();
        }
    }
    
    private void cargarCodirectoresBorradorDeAnteproyecto() {
        llenarCodirectoresSeleccionados();
        for (Academico academicoRecorrido : codirectoresSeleccionado) {
            clicEliminarCodirectorSeleccionado(
                UtilidadesFormularioAnteproyecto.configurarComponenteCodirectorSeleccionado(academicoRecorrido, vbxContenedorDirectores)
            );
        }
    }
    
    private void cargarCuerpoAcademicoBorradorDeAnteproyecto() {
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
    
    private void cargarModalidadBorradorDeAnteproyecto() {
        for (Modalidad modalidad : modalidades) {
            if (modalidad.getIdModalidad() == anteproyectoCorrecion.getIdModalidad()) {
                cmbxModalidadTrabajoRecepcional.getSelectionModel().select(modalidad);
                break;
            }
        }
    }

    private void cargarModalidades(){
        obtenerModalidades();
        cmbxModalidadTrabajoRecepcional.setItems(modalidades);
    }
    
    private void cargarLgacs(int idCuerpoAcademico){
        obtenerLgacs(idCuerpoAcademico);
        if (esEdicion) {
            configurarFiltroLgac();
        } else {
            cmbxLgacs.setItems(lgacs);
            cmbxLgacs.setPromptText("Seleccionar LGAC");
        }
    }

    private void llenarLgacsSeleccionadas() {
        try {
            ArrayList<Lgac> lgacsRecuperadas = new LgacDAO()
                    .obtenerInformacionLGACsPorAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
            for (Lgac lgac : lgacs) {
                for (Lgac lgacRecuperada : lgacsRecuperadas) {
                    if (lgac.getIdLgac() == lgacRecuperada.getIdLgac()) {
                        lgacsSeleccionadas.add(lgac);
                    }
                }
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void llenarCodirectoresSeleccionados() {
        try {
            ArrayList<Academico> codirectoresRecuperados = new AcademicoDAO()
                    .obtenerCodirectores(anteproyectoCorrecion.getIdAnteproyecto());
            for (Academico codirector : codirectores) {
                for (Academico codirectoresRecuperado : codirectoresRecuperados) {
                    if (codirector.getIdAcademico() == codirectoresRecuperado.getIdAcademico()) {
                        codirectoresSeleccionado.add(codirector);
                    }
                }
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    /* 
    Prepara un objeto de tipo anteproyecto a partir de los valores en los campos de texto
    para ser usado en el guardado, actualizacion o modificacion.
    */
    private Anteproyecto prepararAnteproyecto(String estadoSeguimiento) {
        String ciudad = tfCiudad.getText().trim().replaceAll(" +", " ");
        LocalDate fecha = LocalDate.now();
        int idCuerpoAcademico = -1;
        if (cuerpoSeleccionado != null) {
            idCuerpoAcademico = cuerpoSeleccionado.getIdCuerpoAcademico();
        }
        String nombreProyectoInvestigacion = txaNombreProyectoInvestigacion.getText().trim().replaceAll(" +", " ");
        String nombreTrabajoRecepcional = tfNombreTrabajoRecepcional.getText().trim().replaceAll(" +", " ");
        String lineaInvestigacion = tfLineaInvestigacion.getText().trim().replaceAll(" +", " ");
        String requisitos = tfRequisitos.getText().trim().replaceAll(" +", " ");
        String duracionAproximada = tfDuracionAproximada.getText().trim().replaceAll(" +", " ");
        int idModalidad = -1;
        if (cmbxModalidadTrabajoRecepcional.getSelectionModel().getSelectedItem() != null){
            idModalidad = cmbxModalidadTrabajoRecepcional.getSelectionModel().getSelectedItem().getIdModalidad();
        }
        int numeroAlumnosParticipantes = -1;
        if (cmbxNumeroAlumnos.getSelectionModel().getSelectedItem() != null) {
            numeroAlumnosParticipantes = (int) cmbxNumeroAlumnos.getSelectionModel().getSelectedItem();
        }
        String descripcionProyectoInvestigacion = txaDescripcionProyectoInvestigacion.getText().trim().replaceAll(" +", " ");
        String descripcionTrabajoRecepcional = txaDescripcionTrabajoRecepcional.getText().trim().replaceAll(" +", " ");
        String resultados = txaResultados.getText().trim().replaceAll(" +", " ");
        String bibliografiaRecomendada = tfBibliografia.getText().trim().replaceAll(" +", " ");
        String notasExtra = txaNotasExtra.getText().trim().replaceAll(" +", " ");
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
            anteproyectoDAO.eliminarLgacsAnteproyecto(idAnteproyecto);
            anteproyectoDAO.eliminarCodirectoresAnteproyecto(idAnteproyecto);
            int respuesta = anteproyectoDAO.eliminarAnteproyecto(idAnteproyecto);
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
        String fechaActual = fecha.getDayOfMonth() 
                + " de " + MESES[fecha.getMonthValue() - 1] 
                + " de " + fecha.getYear();
        lbFechaCreacion.setText(fechaActual);
        lbMesCreacion.setText(MESES[fecha.getMonthValue() - 1]);
        lbAñoCreacion.setText(String.valueOf(fecha.getYear()));
    }
    
    private void agregarListenersACampos() {
        agregarListenerValidadorCampoVacio(tfCiudad, lbAdvertenciaCiudad);
        agregarListenerValidadorCampoVacio(tfNombreTrabajoRecepcional, lbAdvertenciaNombreTR);
        agregarListenerValidadorCampoVacio(tfDuracionAproximada, lbAdvertenciaDuracionAproximada);
        agregarListenerValidadorCampoVacio(tfRequisitos, lbAdvertenciaRequisitos);
        agregarListenerValidadorCampoVacio(txaDescripcionTrabajoRecepcional, lbAdvertenciaDescripcionTR);
        agregarListenerValidadorCampoVacio(txaResultados, lbAdvertenciaResultados);
        agregarListenerValidadorCampoVacio(tfBibliografia, lbAdvertenciaBibliografia);
        agregarListenerACampoNoObligatorio(txaNombreProyectoInvestigacion, lbAdvertenciaNombreTR);
    }
    
    private void agregarFiltros() {
        FiltrosTexto.filtroLetrasNumeros(tfCiudad);
        FiltrosTexto.filtroLetrasNumerosPuntosComasSignosComunes(tfNombreTrabajoRecepcional);
        FiltrosTexto.filtroLetrasNumerosPuntosComasSignosComunes(tfLineaInvestigacion);
        FiltrosTexto.filtroLetrasNumerosPuntos(tfDuracionAproximada);
        FiltrosTexto.filtroLetrasNumerosPuntos(tfCodirector);
        FiltrosTexto.filtroLetrasNumerosPuntos(tfCuerpoAcademico);
    }
    
    private void agregarListenerValidadorCampoVacio(TextInputControl campoTexto, Label lbMensajeError){
        campoTexto.focusedProperty().addListener(
            (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (oldValue) {
                    if (campoTexto.getText().isEmpty()) {
                        campoTexto.setStyle("-fx-border-color: red");
                        lbMensajeError.setText("Campo requerido");
                    }
                } else {
                    campoTexto.setStyle("-fx-border-color: gray");
                    lbMensajeError.setText("");
                }
            }
        );
        campoTexto.textProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    validacionCamposLLenos();
                } else {
                btnEnviar.setDisable(true);
                }
            }
        );
    }
    
    private void agregarListenerACampoNoObligatorio(TextInputControl campoDeTexto, Label lbAdvertencia) {
        campoDeTexto.focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue) {
                    lbAdvertencia.setText("");
                }
            }
        );
    }   
    
    public void configurarCampoDeBusquedaCodirectores() {
        CampoDeBusqueda<Academico> campoDeBusqueda = new CampoDeBusqueda<>(tfCodirector, lvCodirectores, codirectores, academico, 
            new INotificacionSeleccionItem<Academico>() {
                @Override
                public void notificarSeleccionItem(Academico itemSeleccionado) {
                    lbNombreDirector.requestFocus();
                    Academico codirectorSeleccionado = itemSeleccionado;
                    if ((codirectorSeleccionado != null) 
                            && (!codirectoresSeleccionado.contains(codirectorSeleccionado))
                            && (codirectorSeleccionado.getIdAcademico() != academico.getIdAcademico())) {
                        codirectoresSeleccionado.add(codirectorSeleccionado);
                        tfCodirector.setText("");
                        clicEliminarCodirectorSeleccionado(
                            UtilidadesFormularioAnteproyecto
                                .configurarComponenteCodirectorSeleccionado(codirectorSeleccionado, vbxContenedorDirectores)
                        );
                        validacionCamposLLenos();
                    } else {
                        Utilidades.mostrarDialogoSimple("Acción no permitida",
                            "El academico ya ha sido seleccionado o es usted.", Alert.AlertType.INFORMATION);
                    }
                }

                @Override
                public void notificarPerdidaDelFoco() {
                    validacionCamposLLenos();
                }
            }
        );
    }
    
    public void configurarCampoDeBusquedaCuerposAcademicos(){
        CampoDeBusqueda<CuerpoAcademico> campoBusquedaAcademico = new CampoDeBusqueda<>(
            tfCuerpoAcademico, 
            lvCuerposAcademicosBusqueda, 
            cuerposAcademicos, 
            cuerpoSeleccionado,
            new INotificacionSeleccionItem<CuerpoAcademico>() {
                @Override
                public void notificarSeleccionItem(CuerpoAcademico itemSeleccionado) {
                    cmbxLgacs.requestFocus();
                    lbAdvertenciaCuerpoAcademico.setText("");
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
                && (cuerpoSeleccionado != null)
                && (!lgacsSeleccionadas.isEmpty())
                && (!codirectoresSeleccionado.isEmpty())
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
    
    private boolean validarLongitudesTexto(boolean esValidacionParaBorrador) {
        boolean longitudValida = true;
        String nombreTrabajoRecepcional = tfNombreTrabajoRecepcional.getText();
        String ciudad = tfCiudad.getText();
        String requisitos = tfRequisitos.getText();
        String duracionAproximada = tfDuracionAproximada.getText();
        if (esValidacionParaBorrador) {
            if (nombreTrabajoRecepcional.length() > 300) {
                longitudValida = false;
                lbAdvertenciaNombreTR.setText("El número de carácteres no puede ser mayor a 300.");
            }
            if (ciudad.length() > 30) {
                longitudValida = false;
                lbAdvertenciaCiudad.setText("El número de carácteres no puede ser mayor a 30.");
            }
            if (requisitos.length() > 1000) {
                longitudValida = false;
                lbAdvertenciaRequisitos.setText("Numero de caracteres no valido.");
            } 
            if (duracionAproximada.length() > 15) {
                longitudValida = false;
                lbAdvertenciaDuracionAproximada.setText("El numero de caracteres no puede ser mayor a 15");
            }
        } else {
            if (nombreTrabajoRecepcional.length() > 300 || nombreTrabajoRecepcional.length() < 30) {
                longitudValida = false;
                lbAdvertenciaNombreTR.setText("El número de carácteres no puede ser mayor a 300 ni menor a 30.");
            }
            if (ciudad.length() > 30 || ciudad.length() < 5) {
                longitudValida = false;
                lbAdvertenciaCiudad.setText("El número de carácteres no puede ser mayor a 30 ni menor a 3.");
            }   
            if (requisitos.length() > 1000 || requisitos.length() < 5) {
                longitudValida = false;
                lbAdvertenciaRequisitos.setText("Numero de caracteres no valido. "
                        + "Use <<Ninguno>> si no existen requisitos");
            } 
            if (duracionAproximada.length() > 15 || duracionAproximada.length() < 4) {
                longitudValida = false;
                lbAdvertenciaDuracionAproximada.setText("Largo de duracion no valido. Ej. 1 mes, 1 dia, 12 meses.");
            }
        }
        if (txaNombreProyectoInvestigacion.getText().length() > 200) {
            longitudValida = false;
            lbAdvertenciaNombreProyecto.setText("El número de carácteres no puede ser mayor a 200.");
        }
        if (tfLineaInvestigacion.getText().length() > 300) {
            longitudValida = false;
            lbAdventenciaLineaInvestigacion.setText("El número de carácteres no puede ser mayor a 300.");
        }
        if (txaDescripcionTrabajoRecepcional.getText().length() < 300) {
            longitudValida = false;
            lbAdvertenciaDescripcionTR.setText("La descripcion no puede ser menor a 300 caractéres. "
                    + "Proporcione una descripción amplia y entendible.");
        }
        if (txaDescripcionProyectoInvestigacion.getText().length() > 3000) {
            longitudValida = false;
            lbAdvertenciaDescripcionProyecto.setText("El número de carácteres no puede ser mayor a 3000.");
        }
        if (txaResultados.getText().length() > 3000 || txaResultados.getText().length() < 20) {
            longitudValida = false;
            lbAdvertenciaResultados.setText("El número de carácteres no puede ser mayor a 3000 ni menor a 20.");
        }
        if (tfBibliografia.getText().length() > 3000 || tfBibliografia.getText().length() < 5) {
            longitudValida = false;
            lbAdvertenciaBibliografia.setText("El número de carácteres no puede ser mayor a 3000 ni menor a 5."
                    + "Ingrese <<Ninguna>> en caso si no hay bibliografia recomendada.");
        }
        if (txaNotasExtra.getText().length() > 1000) {
            longitudValida = false;
            lbAdvertenciaNotasExtra.setText("El número de carácteres no puede ser mayor a 1000.");
        }
        return longitudValida;
    }
    
    private void validarCampoBusqueda(TextField tfCampo, ListView listView) {
        if (tfCampo.getText().trim().isEmpty()) {
            tfCampo.setStyle("-fx-border-color: red");
            if ((tfCampo == tfCuerpoAcademico) && (!cmbxLgacs.getItems().isEmpty())) {
                cmbxLgacs.setItems(FXCollections.observableArrayList());
                cmbxLgacs.setPromptText("Selecciona un cuerpo academico.");
                lbAdvertenciaCuerpoAcademico.setText("Selecciona un cuerpo academico.");
                vbxContenedorLgac.getChildren().removeAll(vbxContenedorLgac.getChildren());
                cuerpoSeleccionado = null;
            }
            listView.getSelectionModel().clearSelection();
        }
        validacionCamposLLenos();
    }  
    
    @FXML
    private void clicBtnGuardarBorrador(ActionEvent event) {
        if (validarLongitudesTexto(true)) {
            String nombreTrabajoRecepcion = tfNombreTrabajoRecepcional.getText().trim();
            if (nombreTrabajoRecepcion.isEmpty() 
                    || nombreTrabajoRecepcion.length() < 30 ) {
                Utilidades.mostrarDialogoSimple("Acción no permitidad.", 
                        "Para guardar un borrador debes asignar al menos un nombre al anteproyecto "
                                + "y este debe tener de 30 a 300 caracteres de largo.",
                        Alert.AlertType.INFORMATION);
                tfNombreTrabajoRecepcional.requestFocus();
            } else {
                Anteproyecto anteproyecto = prepararAnteproyecto("Borrador");
                if (esEdicion) {
                    anteproyecto.setIdAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
                    actualizarAnteproyecto(anteproyecto);
                } else {
                    guardarAnteproyecto(anteproyecto);
                }
                Utilidades.mostrarDialogoSimple("Borrador guardado", "Borrador guardado correctamente.", 
                    Alert.AlertType.INFORMATION);
                irAVistaAnteproyectos(academico);
            }
        }
    }

    @FXML
    private void clicBtnEnviarAnteproyecto(ActionEvent event) {
        if (validarLongitudesTexto(false)) {
            if (validarCamposObligatoriosLLenos()) {
                Anteproyecto anteproyecto = prepararAnteproyecto("Sin revisar");
                if (esEdicion) {
                    anteproyecto.setIdAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
                    actualizarAnteproyecto(anteproyecto);
                     Utilidades.mostrarDialogoSimple("Anteproyecto enviado",
                        "Anteproyecto enviado para su aprobación correctamente.", Alert.AlertType.INFORMATION);
                } else {
                    guardarAnteproyecto(anteproyecto);
                    Utilidades.mostrarDialogoSimple("Anteproyecto enviado",
                        "Anteproyecto enviado para su aprobación correctamente.", Alert.AlertType.INFORMATION);
                }
                irAVistaAnteproyectos(academico);
            }
        }
    }

    @FXML
    private void clicSeleccionLGAC(ActionEvent event) {
        Lgac lgacSeleccionada = cmbxLgacs.getSelectionModel().getSelectedItem();
        if (lgacSeleccionada != null) {
            lgacsSeleccionadas.add(lgacSeleccionada);
            clicEliminarLgacSeleccionado(
                UtilidadesFormularioAnteproyecto.configurarComponenteLgacSeleccionada(lgacSeleccionada, vbxContenedorLgac)
            );
            configurarFiltroLgac();   
            validacionCamposLLenos();
        }
    }
    
        @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaAnteproyectos(academico);
    }

    @FXML
    private void clicEliminarBorrador(MouseEvent event) {
        boolean respuesta = Utilidades.mostrarDialogoConfirmacion("Eliminar borrador de anteproyecto.",
                "¿Estás seguro de que deseas eliminar el borrador del anteproyecto?");
        if (respuesta == true) {
            eliminarAnteproyecto(anteproyectoCorrecion.getIdAnteproyecto());
            irAVistaAnteproyectos(academico);
        }
    }
    
    private FilteredList<Lgac> configurarFiltroLgac(){
        FilteredList<Lgac> filtradoLgacs = new FilteredList(lgacs, p -> true );
        if (!lgacs.isEmpty()) {
            filtradoLgacs.setPredicate(
                lgac -> {
                    return !lgacsSeleccionadas.contains(lgac);
                }
            );
            SortedList<Lgac> sortedListLgacs = new SortedList<>(filtradoLgacs, Comparator.comparing(Lgac::getNombreLgac));
            Platform.runLater(
                () -> {
                    cmbxLgacs.setItems(sortedListLgacs);
                }
            );
        }
        return filtradoLgacs;
    }
    
    public void clicEliminarLgacSeleccionado(Button boton) {
        boton.setOnAction(
            (event) -> {
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
                    validacionCamposLLenos();
                }
            }
        );
    }
       
    public void clicEliminarCodirectorSeleccionado(Button boton) {
        boton.setOnAction(
            (event) -> {
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
                    validacionCamposLLenos();
                }
            }
        );
    }
    
    private void irAVistaAnteproyectos(Academico academico) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAnteproyectos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAnteproyectosController controladorVistaAnteproyectos = accesoControlador.getController();
            controladorVistaAnteproyectos.setAcademico(academico, false, CodigosVentanas.INICIO);
            Stage escenario = (Stage) lbAñoCreacion.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Inicio");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void seleccionComboNumeroEstudiantes(ActionEvent event) {
        validacionCamposLLenos();
    }
    
    public void validacionCamposLLenos() {
        if (validarCamposObligatoriosLLenos()) {
            btnEnviar.setDisable(false);
        } else {
            btnEnviar.setDisable(true);
        }
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
