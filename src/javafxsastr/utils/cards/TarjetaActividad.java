/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 17/05/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de una actividad.
 */

package javafxsastr.utils.cards;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafxsastr.modelos.pojo.Actividad;

public class TarjetaActividad  extends Pane {
   
    private int idActividad;
    private Pane fondoImagen;
    private ImageView iconoActividad;
    private Label nombreActividad;
    private Label fechaInicio;
    private Label fechaFin;
    private Button btnVerDetalles;
    private Actividad actividad;
    Font fuenteLabels = new Font(20);
    
    public TarjetaActividad(Actividad actividad) {
        this.actividad = actividad;
        inicializarElementos();
    }
    
    public Button getBotonVerDetalles(){
        return this.btnVerDetalles;
    }
    
    private void inicializarElementos() {
        fondoImagen = new Pane();
        idActividad = actividad.getIdActividad();
        iconoActividad = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/actividad-entrega.png"));
        nombreActividad = new Label(actividad.getNombreActividad());
        fechaInicio = new Label(actividad.getFechaInicioActividad());
        fechaFin = new Label(actividad.getFechaFinActividad());
        btnVerDetalles = new Button("Ver detalles");
        establecerEstiloTarjeta();
        establecerEstiloFondoImagen();
        establecerEstiloTituloActividad();
        establecerEstiloFechas();
        establecerEstiloBotonVerDetalles();
        getChildren().addAll(fondoImagen, nombreActividad, fechaInicio, fechaFin, btnVerDetalles);
    }
    
    private void establecerEstiloTarjeta() {
        setPrefSize(985, 138);
        setStyle("-fx-background-color: white; -fx-background-radius: 15;");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(67, 67);
        fondoImagen.setLayoutX(24);
        fondoImagen.setLayoutY(26);
        fondoImagen.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 50;");
        fondoImagen.getChildren().add(iconoActividad);
        iconoActividad.setFitWidth(29);
        iconoActividad.setFitHeight(34);
        iconoActividad.setLayoutX(19);
        iconoActividad.setLayoutY(17);
    }
    
    private void establecerEstiloTituloActividad() {
        nombreActividad.setLayoutX(113);
        nombreActividad.setLayoutY(23);
        nombreActividad.setFont(new Font(25));
    }
    
    private void establecerEstiloFechas() {
        Label lbFechaInicio = new Label("Fecha de inicio: ");
        lbFechaInicio.setFont(fuenteLabels);
        lbFechaInicio.setLayoutX(114);
        lbFechaInicio.setLayoutY(65);
        fechaInicio.setFont(fuenteLabels);
        fechaInicio.setLayoutX(260);
        fechaInicio.setLayoutY(65);
        Label lbFechaFin = new Label("Fecha de fin:");
        lbFechaFin.setFont(fuenteLabels);
        lbFechaFin.setLayoutX(441);
        lbFechaFin.setLayoutY(65);
        fechaFin.setFont(fuenteLabels);
        fechaFin.setLayoutX(560);
        fechaFin.setLayoutY(65);
        getChildren().addAll(lbFechaFin, lbFechaInicio);
    }
    
    private void establecerEstiloBotonVerDetalles() {
        btnVerDetalles.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnVerDetalles.setLayoutX(807);
        btnVerDetalles.setLayoutY(86);
        btnVerDetalles.setPrefWidth(180);
    }
    
}
