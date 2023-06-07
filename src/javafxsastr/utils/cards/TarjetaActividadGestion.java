/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
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
import javafx.scene.text.FontWeight;
import javafxsastr.modelos.pojo.Actividad;

public class TarjetaActividadGestion extends Pane {
    
    private Label lbNombreActividad;
    private Label fechaInicio;
    private Label fechaFin;
    private Actividad actividad;
    private ImageView imvActividad = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/actividad-entrega.png"));
    private Pane fondoImagen;
    private Button btnVerDetalles;
    private Button btnModificarActividad;
    private Font fuenteTexto = new Font(20);
    private Font fuenteTitulos = Font.font("System", FontWeight.BOLD, 20);
    
    public TarjetaActividadGestion(Actividad actividad) {
        this.actividad = actividad;
        inicializarElementos();
        establecerEstilos();
        getChildren().addAll(lbNombreActividad, fechaInicio, fechaFin,
                fondoImagen, btnVerDetalles, btnModificarActividad);
    }
   
    public void inicializarElementos() {
        lbNombreActividad = new Label(actividad.getNombreActividad());
        fechaInicio = new Label(actividad.getFechaInicioActividad());
        fechaFin = new Label(actividad.getFechaFinActividad());
        btnVerDetalles = new Button("Ver detalles");
        btnModificarActividad = new Button("Modificar actividad");
        fondoImagen = new Pane();
    }
    
    public Button getBotonVerDetalles() {
        return btnVerDetalles;
    }
    
    public Button getBotonModificar() {
        return btnModificarActividad;
    }
    
    private void establecerEstilos() {
        establecerEstiloFondoImagen();
        establecerEstiloLabeTituloFechaFin();
        establecerEstiloLabelTituloFechaInicio();
        establecerEstiloLabelTituloActividad();
        establecerEstiloTarjeta();
        configurarBotonVerDetalles();
        configurarBotonModificarActividad();
    }
    
    private void establecerEstiloTarjeta() {
        this.setPrefSize(1563, 244);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(95, 95);
        fondoImagen.setLayoutX(31);
        fondoImagen.setLayoutY(21);
        fondoImagen.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50");
        fondoImagen.getChildren().add(imvActividad);
        imvActividad.setFitWidth(45);
        imvActividad.setFitHeight(53);
        imvActividad.setLayoutX(25);
        imvActividad.setLayoutY(21);
    }
    
    private void establecerEstiloLabelTituloActividad() {
        Label tituloActividad = new Label("Nombre actividad");
        tituloActividad.setFont(fuenteTitulos);
        tituloActividad.setLayoutX(151);
        tituloActividad.setLayoutY(25);
        lbNombreActividad.setFont(fuenteTexto);
        lbNombreActividad.setLayoutX(151);
        lbNombreActividad.setLayoutY(58);
        getChildren().add(tituloActividad);
    }
    
    private void establecerEstiloLabelTituloFechaInicio() {
        Label tituloFechaInicio = new Label("Fecha inicio: ");
        tituloFechaInicio.setFont(fuenteTitulos);
        tituloFechaInicio.setLayoutX(151);
        tituloFechaInicio.setLayoutY(105);
        fechaInicio.setFont(fuenteTexto);
        fechaInicio.setLayoutX(151);
        fechaInicio.setLayoutY(133);
        getChildren().add(tituloFechaInicio);
    }
    
    private void establecerEstiloLabeTituloFechaFin() {
        Label tituloFechaFin = new Label("Fecha fin: ");
        tituloFechaFin.setFont(fuenteTitulos);
        tituloFechaFin.setLayoutX(151);
        tituloFechaFin.setLayoutY(165);
        fechaFin.setFont(fuenteTexto);
        fechaFin.setLayoutX(151);
        fechaFin.setLayoutY(195);
        getChildren().add(tituloFechaFin);
    }
    
    private void configurarBotonVerDetalles() {
        btnVerDetalles.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnVerDetalles.setLayoutX(1350);
        btnVerDetalles.setLayoutY(179);
        btnVerDetalles.setPrefWidth(180);
        btnVerDetalles.setPrefHeight(40);
    }
    
    private void configurarBotonModificarActividad() {
        btnModificarActividad.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnModificarActividad.setLayoutX(1150);
        btnModificarActividad.setLayoutY(179);
        btnModificarActividad.setPrefWidth(180);
        btnModificarActividad.setPrefHeight(40);
    }
}
