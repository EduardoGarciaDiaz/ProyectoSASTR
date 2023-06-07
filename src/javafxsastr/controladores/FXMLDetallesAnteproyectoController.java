/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 04/06/2023
 * Descripción: Controlador de la vista de detalles de anteproyecto
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionSeleccionItem;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.AnteproyectoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.DesasignacionDAO;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.LgacDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Desasignacion;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Lgac;
import javafxsastr.utils.CampoDeBusqueda;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaDesasignacion;

public class FXMLDetallesAnteproyectoController implements Initializable {
    
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
    private Label lbDirector;
    @FXML
    private Button btnAsignarOtroEstudiante;
    @FXML
    private Label lbNumeroAlumnosParticipantes;
    @FXML
    private VBox vbxAlumnosParticipantes;
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
    private Label lbFecha;
    @FXML
    private VBox vbxEstudiantesDesasignados;
    @FXML
    private Label lbLugar;
    @FXML
    private Label lbRequisitos;
    @FXML
    private Label lbPorcentaje;
    @FXML
    private Label lbActividadesNoCompletadas;
    @FXML
    private Label lbActividadesCompletadas;
    @FXML
    private Label lbTotalActividades;
    @FXML
    private Label lbActividadesRestantes;
    @FXML
    private Label lbNingunDesasignado;
    @FXML
    private VBox vbxCodirectores;
    @FXML
    private VBox vbxSeccionesAnteproyecto;
    @FXML
    private VBox vbxLgacs;
    @FXML
    private TextField tfEstudiante;
    @FXML
    private ListView<Estudiante> lvEstudiantes;
    private Estudiante estudiante;
    @FXML
    private Pane paneBusqueda;
    @FXML
    private Button btnClicAsignacionEstudiante;
    @FXML
    private Button btnCancelarAsignacionEstudiante;
    
    private Anteproyecto anteproyecto;
    private Academico academico;
    private ArrayList<Estudiante> estudiantesParticipantes = new ArrayList<>();
    private ObservableList<Estudiante> estudiantesRegistrados;
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
        obtenerEstudiantes();                
        mostrarDatosAnteproyecto();
        obtenerInformacionAvance();
        mostrarDesasignaciones();
    }
        
    public void setAnteproyecto(Anteproyecto anteproyecto) {
        this.anteproyecto = anteproyecto;
    }
    
    public void setAcademico(Academico academico) {
        this.academico = academico;
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
            mostrarDatosResponsableTrabajoRecepcional();
            validarEsDirector();
            mostrarDatosLugarFecha();
            mostrarDatosProyectoTitulacion();

            mostrarDatosDescripciones();
            mostrarDatosFinales();
        } else {
            System.err.println("El anteproyector recibido viene NULO");
        }
    }
    
    public void validarEsDirector() {
        if (academico == null) {
            btnAsignarOtroEstudiante.setVisible(false);
        } else {
            if(academico.getIdAcademico() == anteproyecto.getIdAcademico()
                    && anteproyecto.getIdEstadoSeguimiento() == ANTEPROYECTO_APROBADO) {
                validarAsignarPrimerEstudiante();
                validarAsignarOtroEstudiante();
            }
        }
    }
    
    private void validarAsignarPrimerEstudiante() {
        if (estudiantesParticipantes.size() < NUMERO_MINIMO_ESTUDIANTES) {
            configurarAgregarPrimerEstudiante();
        }
    }
    
    private void validarAsignarOtroEstudiante() {
        if (estudiantesParticipantes.size() < NUMERO_MAXIMO_ESTUDIANTES
                && estudiantesParticipantes.size() >= 1) {
            btnAsignarOtroEstudiante.setVisible(true);
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
        mostrarDatosLGACs();
        lbLineaInvestigacion.setText(lineaInvestigacion);
        lbDuracion.setText(duracion);
        lbModalidad.setText(modalidad);
        lbNombreTrabajoRecepcional.setText(nombreTrabajoRecepcional);
        lbRequisitos.setText(requisitos);
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
    
    private void mostrarDatosResponsableTrabajoRecepcional() {
        String director = anteproyecto.getNombreDirector();
        try {
            estudiantesParticipantes = new EstudianteDAO().obtenerEstudiantesPorIdAnteproyecto(anteproyecto.getIdAnteproyecto());
            codirectores = new AcademicoDAO().obtenerCodirectores(anteproyecto.getIdAnteproyecto());
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        lbNumeroAlumnosParticipantes.setText(String.valueOf(estudiantesParticipantes.size()));
        lbDirector.setText(director);
        mostrarCodirectores(codirectores);
        mostrarEstudiantes(estudiantesParticipantes);
    }
    
    private void mostrarEstudiantes(ArrayList<Estudiante> estudiantesParticipantes) {
        for(Estudiante e : estudiantesParticipantes) {
            configurarEstudianteParticipante(e, false);
        }
    }
    
    private void mostrarCodirectores(ArrayList<Academico> codirectores) {
        for(Academico codirector : codirectores) {
            configurarCodirectores(codirector);
        }
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
    
    private void obtenerInformacionAvance() {
        if (anteproyecto != null) {
        Map<String, Integer> actividades = calcularNumeroActividades(anteproyecto.getIdAnteproyecto());
        int totalActividades = actividades.get("total");
        int numeroActividadesCompletadas = actividades.get("completadas");
        int numeroActividadesNoCompletadas = actividades.get("noCompletadas");
        int numeroActividadesRestantes = actividades.get("restantes");
        double porcentaje = 0.0;
        if (totalActividades > 0) {
            porcentaje = (numeroActividadesCompletadas * 100) / totalActividades;
        }
        String actividadesCompletadas = String.valueOf(numeroActividadesCompletadas)+" / "+String.valueOf(totalActividades);
        String actividadesNoCompletadas = String.valueOf(numeroActividadesNoCompletadas)+" / "+String.valueOf(totalActividades);
        String actividadesRestantes = String.valueOf(numeroActividadesRestantes)+" / "+String.valueOf(totalActividades);
        lbTotalActividades.setText(String.valueOf(totalActividades));
        lbActividadesCompletadas.setText(actividadesCompletadas);
        lbActividadesNoCompletadas.setText(actividadesNoCompletadas);
        lbActividadesRestantes.setText(actividadesRestantes);
        lbPorcentaje.setText(String.valueOf(porcentaje) + " %");
        } else {
            System.err.println("El anteproyector recibido viene NULO");
        }
    }
    
    private Map<String, Integer> calcularNumeroActividades(int idAnteproyecto) {
        Map<String, Integer> actividades = new HashMap<>();
        try {
            ActividadDAO actividadDAO = new ActividadDAO();
            int totalActividades = actividadDAO.obtenerNumeroActividadesPorAnteproyecto(idAnteproyecto);
            int numeroActividadesCompletadas = actividadDAO.obtenerNumeroActividadesCompletadasPorAnteproyecto(idAnteproyecto);
            int numeroActividadesNoCompletadas = actividadDAO.obtenerNumeroActividadesNoCompletadasPorAnteproyecto(idAnteproyecto);
            int numeroActividadesRestantes = totalActividades - (numeroActividadesCompletadas + numeroActividadesNoCompletadas);
            actividades.put("total", totalActividades);
            actividades.put("completadas", numeroActividadesCompletadas);
            actividades.put("noCompletadas", numeroActividadesNoCompletadas);
            actividades.put("restantes", numeroActividadesRestantes);
            return actividades;
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
            return actividades;
    }
    
    private void mostrarDesasignaciones() {
        if (anteproyecto != null) {
            ArrayList<Desasignacion> desasignaciones = new ArrayList<>();
            try {
                desasignaciones = new DesasignacionDAO().obtenerDesasignacionesPorIdAnteproyecto(anteproyecto.getIdAnteproyecto());
            } catch (DAOException ex) {
                manejarDAOException(ex);
            }
            if (!desasignaciones.isEmpty()) {
                lbNingunDesasignado.setText("");
                for(Desasignacion desasignacion : desasignaciones) {
                    vbxEstudiantesDesasignados.getChildren().add(new TarjetaDesasignacion(desasignacion.getIdEstudiante(),
                            desasignacion.getNombreEstudiante()));
                }
            }
        } else {
            System.err.println("El anteproyector recibido viene NULO");
        }
    }
    
    private void mostrarBusquedaEstudiantes() {
        configurarCampoDeBusqueda();
        CampoDeBusqueda<Estudiante> campoDeBusqueda = new CampoDeBusqueda<>(tfEstudiante, lvEstudiantes,
            estudiantesRegistrados, estudiante, new INotificacionSeleccionItem<Estudiante>() {
                @Override
                public void notificarPerdidaDelFoco() {
                }
                @Override
                public void notificarSeleccionItem(Estudiante itemSeleccionado) {
                    lbActividadesCompletadas.requestFocus();
                    Estudiante estudianteSeleccionado = itemSeleccionado;
                    if (estudianteSeleccionado != null && !estudiantesParticipantes.contains(estudianteSeleccionado)) {
                        if (estudiantesParticipantes.size() < NUMERO_MAXIMO_ESTUDIANTES) {                            
                            tfEstudiante.setText("");
                            estudiantesParticipantes.add(estudianteSeleccionado);
                            configurarEstudianteParticipante(estudianteSeleccionado, true);         
                        } else {
                            Utilidades.mostrarDialogoSimple("Limite de estudiantes",
                                    "Número máximo de estudiantes permitidos alcanzado.", Alert.AlertType.WARNING);
                            tfEstudiante.setText("");
                        }
        
                    } else {
                        Utilidades.mostrarDialogoSimple("Error", "Ya seleccionado", Alert.AlertType.WARNING);
                        tfEstudiante.setText("");
                    }                    
                }
            }
        );
    }
    
    public void configurarCampoDeBusqueda() {
        btnAsignarOtroEstudiante.setVisible(false);
        vbxAlumnosParticipantes.getChildren().add(paneBusqueda);
    }
    
    public void desasignarEstudiante(Estudiante estudiante) {
        //TODO 
    }
    
    public void configurarAgregarPrimerEstudiante() {
        Pane contenedorSinEstudiante = new Pane();
        contenedorSinEstudiante.setPrefSize(1068, Region.USE_COMPUTED_SIZE);
        contenedorSinEstudiante.setStyle("-fx-background-color: white; -fx-background-radius: 15");
        ImageView imgIconoAdvertenia = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/advertencia-rojo.png"));
        contenedorSinEstudiante.getChildren().add(imgIconoAdvertenia);
        imgIconoAdvertenia.setFitHeight(30);
        imgIconoAdvertenia.setFitWidth(30);
        imgIconoAdvertenia.setLayoutX(14);
        imgIconoAdvertenia.setLayoutY(10);
        Label lbSinEstudiante = new Label("El trabajo recepcional no tiene estudiante asignado.");
        contenedorSinEstudiante.getChildren().add(lbSinEstudiante);
        lbSinEstudiante.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        lbSinEstudiante.setFont(new Font(16.0));
        lbSinEstudiante.setStyle("-fx-text-fill: red;");
        lbSinEstudiante.setLayoutX(52);
        lbSinEstudiante.setLayoutY(10);
        Button btnAsignarEstudiante = new Button("Asignar estudiante");
        contenedorSinEstudiante.getChildren().add(btnAsignarEstudiante);
        btnAsignarEstudiante.setLayoutX(867);
        btnAsignarEstudiante.setLayoutY(4);
        btnAsignarEstudiante.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 15;"
                + "-fx-background-color: #c9c9c9");
        btnAsignarEstudiante.setPrefSize(173, 0);
        btnAsignarEstudiante.setOnMouseClicked((event) -> {
            mostrarBusquedaEstudiantes();
            vbxSeccionesAnteproyecto.getChildren().remove(contenedorSinEstudiante);
        });
        vbxSeccionesAnteproyecto.getChildren().add(1, contenedorSinEstudiante);
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
    
    public void configurarEstudianteParticipante(Estudiante estudiante, boolean esRecienAsignado) {
        String nombreEstudiante = estudiante.getNombre() + " " +estudiante.getPrimerApellido() + " " +estudiante.getSegundoApellido();
        HBox contenedorEstudiante = new HBox();
        contenedorEstudiante.setPrefSize(200, Region.USE_COMPUTED_SIZE);
        contenedorEstudiante.setStyle("-fx-background-color: white; -fx-background-radius: 15");
        Pane contenedorVacio = new Pane();
        contenedorEstudiante.getChildren().add(contenedorVacio);
        contenedorVacio.setPrefSize(422, Region.USE_COMPUTED_SIZE);
        Label lbNombreEstudiante = new Label(nombreEstudiante);
        lbNombreEstudiante.setPrefSize(460, 30);
        contenedorEstudiante.getChildren().add(lbNombreEstudiante);
        lbNombreEstudiante.setLayoutX(75);
        lbNombreEstudiante.setLayoutY(6);
        lbNombreEstudiante.setFont(new Font(20.0));
        Button btnDesasignarEstudiante = new Button("Desasignar estudiante");
        if (academico != null) {
            btnDesasignarEstudiante.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 15;"
                + "-fx-background-color: #c9c9c9");
            btnDesasignarEstudiante.setPrefSize(173, 0);
            contenedorEstudiante.getChildren().add(btnDesasignarEstudiante);
            if (esRecienAsignado) {
                btnDesasignarEstudiante.setVisible(false);
            }
        }
        btnDesasignarEstudiante.setOnMouseClicked((event) -> {
            desasignarEstudiante(estudiante);                                       //TO-DO VALIDAR DESASIGNACION
            vbxAlumnosParticipantes.getChildren().remove(contenedorEstudiante);   
        });
        vbxAlumnosParticipantes.getChildren().add(contenedorEstudiante);
    }
    
    public void configurarCodirectores(Academico academico) {
        String nombreCodirector = academico.getNombre() + " " +academico.getPrimerApellido() + " " +academico.getSegundoApellido();
        Label lbNombreCodirector = new Label(nombreCodirector);
        lbNombreCodirector.setPrefSize(703, 30);
        lbNombreCodirector.setFont(new Font(20.0));
        vbxCodirectores.getChildren().add(lbNombreCodirector);
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaAnteproyectos();
    }
    
    private void irAVistaAnteproyectos() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAnteproyectos.fxml"));
            Parent vista = accesoControlador.load();
            FXMLAnteproyectosController controladorVistaAnteproyecto = accesoControlador.getController();
            controladorVistaAnteproyecto.setAcademico(academico, false);
            Stage escenario = (Stage) lbMes.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Anteproyectos");
            escenario.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicAsignarOtroEstudiante(ActionEvent event) {
        validarAsignarOtroEstudiante();
        mostrarBusquedaEstudiantes();    
    }

    @FXML
    private void clicGuardarAsignarEstudiante(ActionEvent event) {
        vbxAlumnosParticipantes.getChildren().remove(paneBusqueda);
        guardarAsignacionDeEstudiantes();
        vbxAlumnosParticipantes.getChildren().clear();
        vbxCodirectores.getChildren().clear();
        mostrarDatosResponsableTrabajoRecepcional();
        validarAsignarPrimerEstudiante();
        validarAsignarOtroEstudiante();
    }
    
    private void guardarAsignacionDeEstudiantes() {
        try {
            for(Estudiante e : estudiantesParticipantes) {
                e.setIdAnteproyecto(anteproyecto.getIdAnteproyecto());
                int respuesta = new EstudianteDAO().actualizarEstudiante(e);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }

    @FXML
    private void clicCancelarAsignarEstudiante(ActionEvent event) {
        boolean cancelarAsignacion = Utilidades.mostrarDialogoConfirmacion("Cancelar asignación",
                "¿Estás seguro de que deseas cancelar la asignación del estudiante?");
        if (cancelarAsignacion) {
            vbxAlumnosParticipantes.getChildren().remove(paneBusqueda);
            estudiantesParticipantes.clear();
            vbxAlumnosParticipantes.getChildren().clear();
            vbxCodirectores.getChildren().clear();
            mostrarDatosResponsableTrabajoRecepcional();
            validarAsignarPrimerEstudiante();
            validarAsignarOtroEstudiante();
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
