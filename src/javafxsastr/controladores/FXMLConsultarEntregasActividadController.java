/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Controlador de la vista de entregas de una actividad
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EntregaDAO;
import javafxsastr.modelos.dao.HistorialCambiosDAO;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.modelos.pojo.ConsultarAvanceEstudianteSingleton;
import javafxsastr.modelos.pojo.Entrega;
import javafxsastr.modelos.pojo.HistorialCambios;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaCambioActividad;
import javafxsastr.utils.cards.TarjetaEntregasActividad;

public class FXMLConsultarEntregasActividadController implements Initializable {

    @FXML
    private VBox vbxCardsEntregas;
    @FXML
    private Label lbNombreActividad;
    @FXML
    private Label lbDetallesActividad;
    @FXML
    private Label lbFechaActividad;
    @FXML
    private Label lbHoraActividad;
    @FXML
    private Label lbEstadoActividad;
    @FXML
    private Label lbNumeroEntregas;
    @FXML
    private VBox vbxCardsCambios;
    @FXML
    private Button btnFecha;
    
    private ConsultarAvanceEstudianteSingleton consultarAvanceEstudiante
           = ConsultarAvanceEstudianteSingleton.obtenerConsultarAvanceEstudiante(null, null, null, null);
    private Actividad actividad;
    private ObservableList<Entrega> entregas;
    private boolean esBtnFechaPresionado = true;
    private final DateTimeFormatter FORMATO_FECHA_COMPLETA = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy",
            new Locale("es"));
    private final DateTimeFormatter FORMATO_FECHA_CON_DIAGONAL = DateTimeFormatter.ofPattern("dd'/'MMMM'/'yyyy",
            new Locale("es"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
        mostrarInformacionActividad();
        obtenerListaEntregas();
        obtenerListaCambios();
    }
    
    public void mostrarInformacionActividad() {
        if (actividad != null) {
            LocalDate fechaInicio = LocalDate.parse(actividad.getFechaInicioActividad());
            LocalDate fechaFin = LocalDate.parse(actividad.getFechaFinActividad());
            String fechaInicioFormateada = fechaInicio.format(FORMATO_FECHA_COMPLETA);
            String fechaFinFormateada = fechaFin.format(FORMATO_FECHA_COMPLETA);
            String fechaActividad = fechaInicioFormateada + " - " + fechaFinFormateada;
            String horaActividad = actividad.getHoraInicioActividad() + " - " + actividad.getHoraFinActividad();
            lbNombreActividad.setText(actividad.getNombreActividad());
            lbDetallesActividad.setText(actividad.getDetallesActividad());
            lbFechaActividad.setText(fechaActividad);
            if (horaActividad.contains("null")) {
                lbHoraActividad.setText("Sin hora especificada");
            } else { 
                lbHoraActividad.setText(horaActividad);
            }
            lbEstadoActividad.setText(actividad.getEstadoActividad());
        } else {
            System.err.println("La actividad que se recibió viene NULA");
        }
    }
    
    public void obtenerListaEntregas() {
        try {
            if (actividad != null) {
                entregas  = FXCollections.observableArrayList(new EntregaDAO().consultarEntregasPorActividad(actividad.getIdActividad()));
                lbNumeroEntregas.setText(entregas.size() + " Entregas enviadas");
                mostrarEntregas(entregas, false);
            } else {
                 System.err.println("La actividad que se recibió viene NULA");
            }
        } catch(DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private void mostrarEntregas(ObservableList<Entrega> entregas, boolean esOrdenInverso) {
        int numeroEntregaInverso = entregas.size();
        int numeroEntrega;
        for (int i = 0; i < entregas.size(); i++) {
            if (esOrdenInverso) {
                numeroEntrega = numeroEntregaInverso;
                numeroEntregaInverso --;
            } else {
                numeroEntrega = i + 1;
            }
            LocalDate fechaEntrega = LocalDate.parse(entregas.get(i).getFechaEntrega());
            String fechaEntregaFormateada = fechaEntrega.format(FORMATO_FECHA_CON_DIAGONAL);
            String horaEntrega = String.valueOf(entregas.get(i).getHoraEntrega());
            String fechaRevision = String.valueOf(entregas.get(i).getFechaRevision());
            if ("null".equals(fechaRevision)) {
                fechaRevision = "";
            }
            Entrega entrega = entregas.get(i);
            int numeroEntregaSeleccionado = numeroEntrega;
            TarjetaEntregasActividad tarjetaEntregaActividad = new TarjetaEntregasActividad(numeroEntrega,fechaEntregaFormateada,
                    horaEntrega, fechaRevision);
            tarjetaEntregaActividad.getBotonRevisar().setOnAction((event) -> {
                irAVistaRevisarEntrega(entrega, numeroEntregaSeleccionado);
            });
            vbxCardsEntregas.getChildren().add(tarjetaEntregaActividad);
        }
    }
    
    private void obtenerListaCambios() {
        try {
            if (actividad != null) {
                ArrayList<HistorialCambios> cambios = new HistorialCambiosDAO().obtenerInformacionHistorialCambios(actividad.getIdActividad());
                for (HistorialCambios cambio : cambios) {        
                    LocalDate fechaModificacion = LocalDate.parse(cambio.getFechaDeModificacion());
                    LocalDate fechaAnterior = LocalDate.parse(cambio.getFechaAnterior());
                    LocalDate fechaNueva = LocalDate.parse(cambio.getFechaNueva());
                    String fechaModificacionFormateada = fechaModificacion.format(FORMATO_FECHA_CON_DIAGONAL);
                    String fechaAnteriorFormateada = fechaAnterior.format(FORMATO_FECHA_CON_DIAGONAL);
                    String fechaNuevaFormateada = fechaNueva.format(FORMATO_FECHA_CON_DIAGONAL);
                    String cambioHecho = "Se cambió la fecha de la actividad de " + fechaAnteriorFormateada + " a " + fechaNuevaFormateada;
                    vbxCardsCambios.getChildren().add(new TarjetaCambioActividad(cambioHecho, fechaModificacionFormateada));
                } 
            } else {
                    System.err.println("La actividad que se recibió viene NULA");
            }

        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        irAVistaAvanceEstudiante();
    }

    @FXML
    private void clicFecha(ActionEvent event) {
        if (esBtnFechaPresionado){
            configurarFiltroBusqueda("asc");
            esBtnFechaPresionado = false;
        } else {
            vbxCardsEntregas.getChildren().clear();
            mostrarEntregas(entregas, false);
            esBtnFechaPresionado = true;
        }
    }

    private void configurarFiltroBusqueda(String orden) {
        vbxCardsEntregas.getChildren().clear();
        if (entregas.size() > 0) {
            FilteredList<Entrega> filtroEntregas = new FilteredList<>(entregas, p -> true);
            filtroEntregas.setPredicate(entrega -> {
                if (orden.equals("asc")) {   
                    return true;
                }
                return false;
            });
        Comparator<Entrega> fechaComparator = Comparator.comparing(Entrega::getFechaEntrega);
        List<Entrega> listaOrdenada = new ArrayList<>(filtroEntregas);
        listaOrdenada.sort(fechaComparator.reversed());
        mostrarEntregas(new SortedList<>(FXCollections.observableList(listaOrdenada), fechaComparator.reversed()), true);
        }
    }    
    
    private void irAVistaAvanceEstudiante() {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLConsultarAvanceEstudiante.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarAvanceEstudianteController controladorVistaAvanceEstudiante 
                    = accesoControlador.getController();
            controladorVistaAvanceEstudiante.setEstudianteAcademico(
                    consultarAvanceEstudiante.getEstudiante(),
                    consultarAvanceEstudiante.getAcademico(),
                    consultarAvanceEstudiante.getVentanaOrigen(),
                    consultarAvanceEstudiante.getCurso()
            );
            Stage escenario = (Stage) lbDetallesActividad.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Entregas de la actividad");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaRevisarEntrega(Entrega entrega, int numeroEntrega) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLRevisarEntrega.fxml"));
            Parent vista = accesoControlador.load();
            FXMLRevisarEntregaController controladorVistaRevisarEntrega = accesoControlador.getController();
            controladorVistaRevisarEntrega.setNumeroEntrega(numeroEntrega);
            controladorVistaRevisarEntrega.inicializarContenidoEntrega(
                    entrega, consultarAvanceEstudiante.getAcademico(), actividad);
            Stage escenario = (Stage) lbDetallesActividad.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Entregas de la actividad");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
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