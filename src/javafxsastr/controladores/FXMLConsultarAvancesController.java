/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Controlador de la vista de avances de los estudiantes
 */

package javafxsastr.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxsastr.modelos.dao.ActividadDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.utils.Utilidades;
import javafxsastr.utils.cards.TarjetaAvanceEstudiante;

public class FXMLConsultarAvancesController implements Initializable {

    @FXML
    private VBox vbxCards;
    @FXML
    private Label lbNombreCurso;
    @FXML
    private Label lbNRC;
    @FXML
    private Label lbPeriodoEscolar;
    @FXML
    private Label lbSeccion;
    @FXML
    private Label lbBloque;
    
    private Curso curso;
    private int idCurso;
    private final DateTimeFormatter FORMATO_FECHA_PERIODO = DateTimeFormatter.ofPattern("MMM ' 'yyyy", new Locale("es"));

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarInformacionCurso();
        obtenerInformacionEstudiantes();
    }
    
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
    
    public void mostrarInformacionCurso() {
        if (curso != null) {
            lbNombreCurso.setText(curso.getNombreCurso());
            lbNRC.setText(curso.getNrcCurso());
            LocalDate fechaInicio = LocalDate.parse(curso.getInicioPeriodoEscolar());
            LocalDate fechaFin = LocalDate.parse(curso.getFinPeriodoEscolar());
            String fechaInicioFormateda = fechaInicio.format(FORMATO_FECHA_PERIODO);
            String fechaFinFormateada = fechaFin.format(FORMATO_FECHA_PERIODO);
            String periodoEscolar = fechaInicioFormateda + " - " + fechaFinFormateada;
            lbPeriodoEscolar.setText(periodoEscolar);
            lbSeccion.setText(curso.getSeccionCurso());
            lbBloque.setText(curso.getBloqueCurso());
        } else {
            System.err.println("El curso que se recibió viene NULO");
        }
    }
    
    public void obtenerInformacionEstudiantes() {
        ArrayList<Estudiante> estudiantesCurso;
        try {
            estudiantesCurso = new EstudianteDAO().obtenerEstudiantesPorIdCurso(idCurso);
            for (Estudiante e : estudiantesCurso) {
                String nombreCompleto = e.getNombre() + " "+e.getPrimerApellido() + " " + e.getSegundoApellido();
                Map<String, Integer> actividades = calcularNumeroActividades(e.getIdEstudiante());
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
                vbxCards.getChildren().add(new TarjetaAvanceEstudiante(e.getIdEstudiante(), nombreCompleto,e.getMatriculaEstudiante(),
                e.getAnteproyectoEstudiante(), porcentaje, actividadesCompletadas, actividadesNoCompletadas, actividadesRestantes));
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
    }
    
    private Map<String, Integer> calcularNumeroActividades(int idEstudiante) {
        Map<String, Integer> actividades = new HashMap<>();
        try {
            ActividadDAO actividadDAO = new ActividadDAO();
            int totalActividades = actividadDAO.obtenerNumeroActividadesPorEstudiante(idEstudiante);
            int numeroActividadesCompletadas = actividadDAO.obtenerNumeroActividadesCompletadas(idEstudiante);
            int numeroActividadesNoCompletadas = actividadDAO.obtenerNumeroActividadesNoCompletadas(idEstudiante);
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
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        Stage escenerioBase = (Stage) lbNombreCurso.getScene().getWindow();
        escenerioBase.close();
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