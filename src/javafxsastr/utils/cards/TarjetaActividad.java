/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 17/05/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de una actividad.
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafxsastr.modelos.pojo.Actividad;
import javafxsastr.utils.Utilidades;

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
    private Pane fondoEstado;
    
    public TarjetaActividad(Actividad actividad) {
        this.actividad = actividad;
        inicializarElementos();
        establecerEstilos();
        getChildren().addAll(fondoImagen, nombreActividad, fechaInicio, fechaFin, btnVerDetalles, fondoEstado);
    }
    
    public Button getBotonVerDetalles(){
        return this.btnVerDetalles;
    }
    
    private void establecerEstilos() {
        establecerEstiloTarjeta();
        establecerEstiloFondoImagen();
        establecerEstiloTituloActividad();
        establecerEstiloFechas();
        establecerEstiloBotonVerDetalles();
        establecerEstiloFondoEstado();
    }
    
    private void inicializarElementos() {
        fondoImagen = new Pane();
        idActividad = actividad.getIdActividad();
        iconoActividad = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/actividad-entrega.png"));
        nombreActividad = new Label(actividad.getNombreActividad());
        fechaInicio 
                = new Label(Utilidades.formatearFechaNormal(actividad.getFechaInicioActividad()));
        fechaFin 
                = new Label(Utilidades.formatearFechaNormal(actividad.getFechaFinActividad()));
        fondoEstado = new Pane();
        btnVerDetalles = new Button("Ver detalles");
    }
    
    private void establecerEstiloTarjeta() {
        setPrefSize(985, 170);
        setStyle("-fx-background-color: white; -fx-background-radius: 15;");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(67, 67);
        fondoImagen.setLayoutX(24);
        fondoImagen.setLayoutY(40);
        fondoImagen.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 50;");
        fondoImagen.getChildren().add(iconoActividad);
        iconoActividad.setFitWidth(29);
        iconoActividad.setFitHeight(34);
        iconoActividad.setLayoutX(19);
        iconoActividad.setLayoutY(17);
    }
    
    private void establecerEstiloTituloActividad() {
        nombreActividad.setLayoutX(113);
        nombreActividad.setLayoutY(65);
        nombreActividad.setFont(new Font(25));
    }
    
    private void establecerEstiloFechas() {
        Label lbFechaInicio = new Label("Fecha de inicio: ");
        lbFechaInicio.setFont(fuenteLabels);
        lbFechaInicio.setLayoutX(114);
        lbFechaInicio.setLayoutY(115);
        fechaInicio.setFont(fuenteLabels);
        fechaInicio.setLayoutX(260);
        fechaInicio.setLayoutY(115);
        Label lbFechaFin = new Label("Fecha de fin:");
        lbFechaFin.setFont(fuenteLabels);
        lbFechaFin.setLayoutX(441);
        lbFechaFin.setLayoutY(115);
        fechaFin.setFont(fuenteLabels);
        fechaFin.setLayoutX(560);
        fechaFin.setLayoutY(115);
        getChildren().addAll(lbFechaFin, lbFechaInicio);
    }
    
    private void establecerEstiloFondoEstado() {
        fondoEstado.setPrefSize(150, 30);
        fondoEstado.setLayoutX(114);
        fondoEstado.setLayoutY(19);
        Label lbEstado = new Label(actividad.getEstadoActividad());
        fondoEstado.getChildren().add(lbEstado);
        lbEstado.setLayoutX(9);
        lbEstado.setLayoutY(5);
        lbEstado.setPrefWidth(130);
        lbEstado.setStyle("-fx-text-fill: white;");
        lbEstado.setAlignment(Pos.CENTER);
        switch (actividad.getEstadoActividad()) {
            case "No completada":
                fondoEstado.setStyle("-fx-background-color: #D04545; -fx-background-radius: 15");
                break;
            case "Completada":
                fondoEstado.setStyle("-fx-background-color: #7EA0CD; -fx-background-radius: 15");
                break;
            case "Proxima":
                fondoEstado.setStyle("-fx-background-color: #C5BE19; -fx-background-radius: 15");
                break;
            default:
               System.out.println("La actividad viene sin estado");
        }
    }
    
    
    private void establecerEstiloBotonVerDetalles() {
        btnVerDetalles.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnVerDetalles.setLayoutX(807);
        btnVerDetalles.setLayoutY(115);
        btnVerDetalles.setPrefWidth(180);
    }
    
}
