/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Controller de la ventana validar Anteproyecto
 */
package javafxsastr.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstadoSeguimientoDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.RubricaDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Rubrica;
import javafxsastr.utils.Utilidades;

public class FXMLValidarAnteproyectoController implements Initializable {

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
    private Label lbMes;
    @FXML
    private Label lbAnio;
    @FXML
    private VBox vbxCards;
    @FXML
    private Label lbCuerpoAcademico;
    @FXML
    private Label lbNombreProyectoInvestigacion;
    @FXML
    private Label lbLgacAlimenta;
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
    private Label lbCodirector;
    @FXML
    private Label lbNumeroAlumnosParticipantes;
    @FXML
    private VBox vbxAlumnosParticipantes;
    @FXML
    private Label lbEstudianteParticipante;
    @FXML
    private Label lbEstudianteParticipante2;
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
    private RadioButton t0;
    @FXML
    private RadioButton t1;
    @FXML
    private RadioButton t2;
    @FXML
    private RadioButton t3;
    
    private Anteproyecto anteproyecto;
    private Academico academico;
    private ArrayList<Integer> valores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crearAnteproyectoTemporal();    //QUITAR
        mostrarDatosAnteproyecto();
        btnAprobar.setDisable(true);
        inicializarListeners();
    }  
    
    private void inicializarListeners() {  
        NombreTR.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {            
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {            
               validarBtnAprobar();
            }
        });
        LGACS.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {            
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
               validarBtnAprobar();
            }
        });
        Redaccion.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {            
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
               validarBtnAprobar();
            }
        });
        ResultadosEsperados.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {            
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
               validarBtnAprobar();
            }
        });
        DescripcionTR.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {            
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
               validarBtnAprobar();
            }
        });
        BibliografiasRecomendadas.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {            
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
               validarBtnAprobar();
            }
        });
        RequisitosAnteproyecto.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {            
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
               validarBtnAprobar();
            }
        });     
    }
    
    public void crearAnteproyectoTemporal() {   //QUITAR
        try {
            anteproyecto = new AnteproyectoDAO().obtenerAnteproyectoPorId(5);
        }catch(DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    public void setAnteproyecto(Anteproyecto anteproyecto) {
        this.anteproyecto = anteproyecto;
    }
    public void setAcademico(Academico academico) {
        this.academico = academico;
    }
    
    private void mostrarDatosAnteproyecto() {
        mostrarDatosFecha();
        mostrarDatosProyectoTitulacion();
        mostrarDatosResponsableTrabajoRecepcional();        
        mostrarDatosDescripciones();
        mostrarDatosFinales();
        
    }
    
    private void mostrarDatosFecha() {
        LocalDate fechaCreacion = obtenerFecha();
        String anio = String.valueOf(fechaCreacion.getYear());
        String mes = String.valueOf(fechaCreacion.getMonth());
        String ciudad = anteproyecto.getCiudadCreacion();
        lbFecha.setText(fechaCreacion.toString());
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
        ArrayList<Estudiante> estudiantesParticipantes = new ArrayList<>();
        String director = anteproyecto.getNombreDirector();
        String codirector = "Falta Obtenerlo";
        String numeroAlumnosParticipantes = String.valueOf(anteproyecto.getNumeroMaximoAlumnosParticipantes());
        try {
            estudiantesParticipantes = new EstudianteDAO().obtenerEstudiantesPorIdAnteproyecto(
                    anteproyecto.getIdAnteproyecto());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        for(Estudiante estudiante : estudiantesParticipantes) {
            System.out.println("estudiante " + estudiante.getNombre());
            lbEstudianteParticipante.setText(estudiante.getNombre());
        }
        lbDirector.setText(director);
        lbCodirector.setText(codirector);
        lbNumeroAlumnosParticipantes.setText(numeroAlumnosParticipantes);
        
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
    
    private void validarBtnAprobar() {
        if(BibliografiasRecomendadas.getSelectedToggle() != null && DescripcionTR.getSelectedToggle() != null
                && LGACS.getSelectedToggle() != null && NombreTR.getSelectedToggle() != null &&
                Redaccion.getSelectedToggle() != null && RequisitosAnteproyecto.getSelectedToggle() != null
                && ResultadosEsperados.getSelectedToggle() != null && validarValores()) {
            btnAprobar.setDisable(false);            
        }else {
            btnAprobar.setDisable(true);
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
            if(valores.get(i)==3) {
                return false;
            }
            if(valores.get(i) == 2) {
                contRegular++;
            }
            if(contRegular > 2) { 
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
            if(rubricaExistente != -1) {
                rubrica.setIdRubrica(rubricaExistente);
                exitoRubrica = new RubricaDAO().actualizarRubrica(rubrica);
            }else{
                 exitoRubrica = new RubricaDAO().guardarRubrica(rubrica);
                 int exitoRelacion = new RubricaDAO().guardarRelacionRubricaAnteproyecto(txaComentarios.getText(),
                        anteproyecto.getIdAnteproyecto(),exitoRubrica);
            }           
            if(exitoRubrica != -1) {                
                int idEstadoSeguimiento = new EstadoSeguimientoDAO().obtenerIdEstadoSeguimiento(estadoSeguimiento);
                int exitoActualizaicon = new AnteproyectoDAO().actualizarEstadoSeguimiento(anteproyecto.getIdAnteproyecto(),
                            idEstadoSeguimiento);
                if(exitoActualizaicon == 1) {
                    if( estadoSeguimiento.equals("Validado")) {
                     Utilidades.mostrarDialogoSimple("Registro Exitoso", 
                        "Se ha aprobado el anteproyecto correctamente", Alert.AlertType.INFORMATION);
                    }else {
                        Utilidades.mostrarDialogoSimple("Registro Exitoso", 
                            "Se ha rechazado el anteproyecto", Alert.AlertType.INFORMATION);
                    }               
                }
               cerrarVentana();
            }else {
                Utilidades.mostrarDialogoSimple("Error","Fallo al registrar la rubrica", 
                        Alert.AlertType.ERROR);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
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
     
     private void cerrarVentana() {
        Stage escenarioActual = (Stage) txaComentarios.getScene().getWindow();
        escenarioActual.close();
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
    
}