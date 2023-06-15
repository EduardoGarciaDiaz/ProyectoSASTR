/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 03/06/2023
 * Descripción: Controller de la ventana para validar un Anteproyecto
 */
package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstadoSeguimientoDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.LgacDAO;
import javafxsastr.modelos.dao.RevisionAnteproyectoDAO;
import javafxsastr.modelos.dao.RubricaDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.modelos.pojo.RevisionAnteproyecto;
import javafxsastr.modelos.pojo.Rubrica;
import javafxsastr.utils.CodigosVentanas;
import javafxsastr.utils.Utilidades;

public class FXMLValidarAnteproyectoController implements Initializable {

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
    private Label lbMes;
    @FXML
    private Label lbAnio;
    @FXML
    private Label lbCuerpoAcademico;
    @FXML
    private Label lbNombreProyectoInvestigacion;
    @FXML
    private Label lbLineaInvestigacion;
    @FXML
    private Label lbDuracion;
    @FXML
    private Label lbModalidad;
    @FXML
    private Label lbNombreTrabajoRecepcional;
    @FXML
    private Label lbRequisitos;
    @FXML
    private Label lbDirector;
    @FXML
    private Label lbDescripcionProyectoInvestigacion;
    @FXML
    private Label lbDescripcionTrabajoRecepcional;
    @FXML
    private Label lbResultadosEsperados;
    @FXML
    private Label lbBibliografiaRecomendada;
    @FXML
    private Label lbNotas;
    @FXML
    private Label lbLugar;
    @FXML
    private Label lbFecha;
    @FXML
    private Button btnAprobar;
    @FXML
    private TextArea txaComentarios;
    @FXML
    private VBox vbxLgacs;
    @FXML
    private VBox vbxCodirectores;
    @FXML
    private Label lbComentarios;
    @FXML
    private Button btnRechazar;
    
    private final int LIM_CARACT_COMENTARIOS = 2000;
    private Anteproyecto anteproyecto;
    private Academico academico;
    private ArrayList<Estudiante> estudiantesParticipantes = new ArrayList<>();
    private ObservableList<Estudiante> estudiantesRegistrados;
    private ArrayList<Integer> valores;
    private ArrayList<Academico> codirectores = new ArrayList<>();
    private ArrayList<Lgac> lgacs = new ArrayList<>();
    private final int ANTEPROYECTO_APROBADO = 3;
    private final int NUMERO_MAXIMO_ESTUDIANTES = 3;
    private final int NUMERO_MINIMO_ESTUDIANTES = 1;
    private final DateTimeFormatter FORMATO_FECHA_MES = DateTimeFormatter.ofPattern("MMMM",
            new Locale("es"));
    private final DateTimeFormatter FORMATO_FECHA_COMPLETA = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
            new Locale("es"));
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    
    public void setAnteproyectoAcademico(Anteproyecto anteproyecto, Academico academico) {
        this.anteproyecto = anteproyecto;
        this.academico = academico;
        mostrarDatosAnteproyecto();
        btnAprobar.setDisable(true);
        btnRechazar.setDisable(true);
        inicializarListeners();
    }
    
     public void setAnteproyecto(Anteproyecto anteproyecto) {
        this.anteproyecto = anteproyecto;
    }
    
    public void setAcademico(Academico academico) {
        this.academico = academico;
    }
    
    private void inicializarListeners() {  
        NombreTR.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                validarBotones();
            }
        );
        LGACS.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                validarBotones();
            }
        );
        Redaccion.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                validarBotones();
            }
        );
        ResultadosEsperados.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                validarBotones();
            }
        );
        DescripcionTR.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                validarBotones();
            }
        );
        BibliografiasRecomendadas.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                validarBotones();
            }
        );
        RequisitosAnteproyecto.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                validarBotones();
            }
        );
        txaComentarios.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (txaComentarios.getText().length() > LIM_CARACT_COMENTARIOS) {
                    mostraMensajelimiteSuperado(LIM_CARACT_COMENTARIOS,"Comentarios",lbComentarios);
                    btnAprobar.setDisable(true);
                } else {
                    lbComentarios.setText("");
                }
                validarBotones();
            }
        ); 
    }
    
    private void obtenerEstudiantes() {
        try {
            estudiantesRegistrados = FXCollections.observableArrayList(new EstudianteDAO().obtenerEstudiantes());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void mostrarDatosAnteproyecto() {
        if (anteproyecto != null && !anteproyecto.getFechaCreacion().isEmpty()) {
            mostrarDatosLugarFecha();
            mostrarDatosProyectoTitulacion();
            mostrarDatosResponsableTrabajoRecepcional();        
            mostrarDatosDescripciones();
            mostrarDatosFinales();  
            mostrarDatosLGACs();
        } 
    }
    
    private void mostrarDatosLugarFecha() {
        LocalDate fechaCreacion = obtenerFecha();
        String anio = String.valueOf(fechaCreacion.getYear());
        String mes = fechaCreacion.format(FORMATO_FECHA_MES);
        String ciudad = anteproyecto.getCiudadCreacion();
        lbFecha.setText(fechaCreacion.format(FORMATO_FECHA_COMPLETA));
        lbMes.setText(mes);
        lbAnio.setText(anio);
        lbLugar.setText(ciudad + ", Veracruz");
    }
    
    private LocalDate obtenerFecha() {
        String fechaCreacion = anteproyecto.getFechaCreacion();
        DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fecha = LocalDate.parse(fechaCreacion, formateadorFecha);
        return fecha;
    }
    
    private void mostrarDatosProyectoTitulacion(){
        String cuerpoAcademico = anteproyecto.getNombreCuerpoAcademico();
        String nombreProyectoInvestigacion = anteproyecto.getNombreProyectoInvestigacion();        
        String lineaInvestigacion = anteproyecto.getLineaInvestigacion();
        String duracion = anteproyecto.getDuracionAproximada();
        String modalidad = anteproyecto.getNombreModalidad();
        String nombreTrabajoRecepcional = anteproyecto.getNombreTrabajoRecepcional();
        String requisitos = anteproyecto.getRequisitos();
        lbCuerpoAcademico.setText(cuerpoAcademico);
        lbNombreProyectoInvestigacion.setText(nombreProyectoInvestigacion);        
        lbLineaInvestigacion.setText(lineaInvestigacion);
        lbDuracion.setText(duracion);
        lbModalidad.setText(modalidad);
        lbNombreTrabajoRecepcional.setText(nombreTrabajoRecepcional);
        lbRequisitos.setText(requisitos);        
    }      
    
    private void mostrarDatosResponsableTrabajoRecepcional() {
        String director = anteproyecto.getNombreDirector();
        try {
            codirectores = new AcademicoDAO().obtenerCodirectores(anteproyecto.getIdAnteproyecto());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        lbDirector.setText(director);
        mostrarCodirectores(codirectores);        
    }
    
    private void mostrarDatosDescripciones() {
        String descripcionProyectoInvestigacion = anteproyecto.getDescripcionProyectoInvestigacion();
        String descripcionTrabajoRecepcional = anteproyecto.getDescripcionTrabajoRecepcional();
        lbDescripcionProyectoInvestigacion.setText(descripcionProyectoInvestigacion);
        lbDescripcionTrabajoRecepcional.setText(descripcionTrabajoRecepcional);
    }
    
    private void mostrarDatosFinales() {
        String resultadosEsperados = anteproyecto.getResultadosEsperadosAnteproyecto();
        String bibliografiaRecomendada = anteproyecto.getBibliografiaRecomendada();
        String notas = anteproyecto.getNotasExtras();
        lbResultadosEsperados.setText(resultadosEsperados);
        lbBibliografiaRecomendada.setText(bibliografiaRecomendada);
        lbNotas.setText(notas);
    }
    
    private void mostrarCodirectores(ArrayList<Academico> codirectores) {
        vbxCodirectores.getChildren().clear();
        for (Academico codirector : codirectores) {
            configurarCodirectores(codirector);
        }
    }
    
    private void mostrarDatosLGACs() {
        try {
            lgacs = new LgacDAO().obtenerInformacionLGACsPorAnteproyecto(anteproyecto.getIdAnteproyecto());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        for (Lgac lgac : lgacs) {
            configurarLgacs(lgac);
        }
    }
    
     public void configurarCodirectores(Academico academico) {
        String nombreCodirector = academico.getNombre() + " " +academico.getPrimerApellido() + " " +academico.getSegundoApellido();
        Label lbNombreCodirector = new Label(nombreCodirector);
        lbNombreCodirector.setPrefSize(703, 30);
        lbNombreCodirector.setFont(new Font(20.0));
        vbxCodirectores.getChildren().add(lbNombreCodirector);
    }
     
     public void configurarLgacs(Lgac lgac) {
        String nombreLgac = lgac.getNombreLgac();
        Label lbNombreLgac = new Label(nombreLgac);
        lbNombreLgac.setPrefSize(655, Region.USE_COMPUTED_SIZE);
        lbNombreLgac.setFont(new Font(20.0));
        lbNombreLgac.setLayoutX(75);
        lbNombreLgac.setLayoutY(6);
        vbxLgacs.getChildren().add(lbNombreLgac);
    }
    
    private void validarBotones() {
        if (BibliografiasRecomendadas.getSelectedToggle() != null 
                && DescripcionTR.getSelectedToggle() != null
                && LGACS.getSelectedToggle() != null 
                && NombreTR.getSelectedToggle() != null 
                && Redaccion.getSelectedToggle() != null 
                && RequisitosAnteproyecto.getSelectedToggle() != null
                && ResultadosEsperados.getSelectedToggle() != null ) {
            btnRechazar.setDisable(false);
            if (validarValores()) {
                btnAprobar.setDisable(false);  
            }                      
        } else {
            btnAprobar.setDisable(true);
            btnRechazar.setDisable(true);
        }
    }
 
    private void obtenerValoresRubrica() {        
        ObservableList<ToggleGroup> metricasRevision = FXCollections.observableArrayList();
        metricasRevision.add(LGACS);
        metricasRevision.add(NombreTR);        
        metricasRevision.add(DescripcionTR);
        metricasRevision.add(RequisitosAnteproyecto);
        metricasRevision.add(ResultadosEsperados);
        metricasRevision.add(BibliografiasRecomendadas);
        metricasRevision.add(Redaccion);
        int contador = 0;
        valores = new ArrayList();
        for (ToggleGroup toggleGroup : metricasRevision) {            
            RadioButton eleccion = (RadioButton) toggleGroup.getSelectedToggle();           
            switch (eleccion.getText()) {
                case "0":
                    valores.add(0);
                    break;
                case "1":
                    valores.add(1);
                    break;
                case "2":
                    valores.add(2);
                    break;
                case "3":
                    valores.add(3);
                    btnAprobar.setDisable(true);
                    break;
                  
                default:
                    throw new AssertionError();
            }
        }                
    }
    
    private boolean validarValores() {
        obtenerValoresRubrica();        
        int contRegular=0;
        for (int i = 0; i < valores.size(); i++) {
            if (valores.get(i)==3) {
                return false;
            }
            if (valores.get(i) == 2) {
                contRegular++;
            }
            if (contRegular > 2) { 
                return false;
            }
        }
        return true;
    }
    
    private void guardarRevision(String estadoSeguimiento) {
        Rubrica rubrica = new Rubrica();
        rubrica.setValorLineasGeneracionAplicacionConocimiento(valores.get(0));
        rubrica.setValorNombreTrabajoRecepcional(valores.get(1));
        rubrica.setValorDescripcionTrabajoRecepcional(valores.get(2));
        rubrica.setValorRequisitosAnteproyecto(valores.get(3));
        rubrica.setValorResultadosEsperados(valores.get(4));
        rubrica.setValorBibliografiasRecomendadas(valores.get(5));
        rubrica.setValorRedaccion(valores.get(6));     
        try {
            int rubricaExistente = new RubricaDAO().obtenerRubricaAnteproyecto(anteproyecto.getIdAnteproyecto());
            int exitoRubrica= -1 ;
            if (rubricaExistente != -1) {
                rubrica.setIdRubrica(rubricaExistente);
                exitoRubrica = new RubricaDAO().actualizarRubrica(rubrica);
                actualizarRevisonRubrica();
            } else {
                 exitoRubrica = new RubricaDAO().guardarRubrica(rubrica);
                 int exitoRelacion = new RevisionAnteproyectoDAO().guardarRelacionRubricaAnteproyecto(txaComentarios.getText(),
                        anteproyecto.getIdAnteproyecto(),exitoRubrica);            }           
            if (exitoRubrica != -1) {                
                int idEstadoSeguimiento = new EstadoSeguimientoDAO().obtenerIdEstadoSeguimiento(estadoSeguimiento);
                int exitoActualizaicon = new AnteproyectoDAO().actualizarEstadoSeguimiento(anteproyecto.getIdAnteproyecto(),
                            idEstadoSeguimiento);
                if (exitoActualizaicon == 1) {
                        if ( estadoSeguimiento.equals("Rechazado")) {
                            Utilidades.mostrarDialogoSimple("Rechazado", "Se ha rechazado el anteproyecto correctamente",
                                                                                                Alert.AlertType.INFORMATION);
                        } else {
                            Utilidades.mostrarDialogoSimple("Aprobado","Se ha aprobado el anteproyecto",
                                                                                    Alert.AlertType.INFORMATION);
                    }               
                } else {
                Utilidades.mostrarDialogoSimple("Error","Fallo actualizar el estado de seguimiento", 
                        Alert.AlertType.ERROR);
                }
               cerrarVentana();
            } else {
                Utilidades.mostrarDialogoSimple("Error","Fallo al registrar la rubrica", 
                        Alert.AlertType.ERROR);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void actualizarRevisonRubrica() throws DAOException {        
        RevisionAnteproyecto revisionAnteproyecto = new RevisionAnteproyectoDAO().obtenerRevisionAnteproyecto(anteproyecto.getIdAnteproyecto());
        new RevisionAnteproyectoDAO().actualizarRelacionRubricaAnteproyecto(revisionAnteproyecto.getIdRevisionAnteproyecto(), 
                                                                                                      txaComentarios.getText().trim()); 
    }
    
    private void mostraMensajelimiteSuperado(int limiteCaracteres, String campo,  Label etiquetaError) { 
        etiquetaError.setText("Cuidado, Excediste el limite de caracteres("+limiteCaracteres+") de este campo " + campo);
        btnAprobar.setDisable(true);
    }    
     
    private void cerrarVentana() {
          try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAnteproyectos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAnteproyectosController controladorAnteproyectos = accesoControlador.getController();
            controladorAnteproyectos.setAcademico(academico, true, CodigosVentanas.INICIO);
            Stage escenario = (Stage) lbDuracion.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Anteproyectos");
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarDialogoSimple("Error","Hubo un error al mostrar la vista",
                                                                   Alert.AlertType.INFORMATION);
        }
    }  

    @FXML
    private void clicRegresar(MouseEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicRechazar(ActionEvent event) {
        guardarRevision("Rechazado");
    }
    
    @FXML
    private void clicAprobar(ActionEvent event) {
        guardarRevision("Validado");
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
