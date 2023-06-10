/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de una actividad.
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafxsastr.modelos.pojo.Actividad;

public class TarjetaActividadGestion extends Pane {
    
    private Label lbNombreActividad;
    private Label fechas;
    private Actividad actividad;
    private ImageView imvActividad = 
            new ImageView(new Image("file:src/javafxsastr/recursos/iconos/actividad-entrega.png"));
    private ImageView imvModificar
            = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/edicion.png"));
    private Pane fondoImagen;
    private Pane fondoEstado;
    private Pane fondoModificar;
    private Button btnVerDetalles;
    private CheckBox chbxCompletar;
    private Font fuenteTexto = new Font(20);
    private Font fuenteTitulos = Font.font("System", FontWeight.BOLD, 23);
    
    public TarjetaActividadGestion(Actividad actividad) {
        this.actividad = actividad;
        inicializarElementos();
        establecerEstilos();
        getChildren().addAll(lbNombreActividad, fechas, fondoEstado,
                fondoImagen, btnVerDetalles, chbxCompletar, fondoModificar);
    }
   
    public void inicializarElementos() {
        lbNombreActividad = new Label(actividad.getNombreActividad());
        fechas = new Label();
        btnVerDetalles = new Button("Ver detalles");
        chbxCompletar = new CheckBox();
        fondoImagen = new Pane();
        fondoEstado = new Pane();
        fondoModificar = new Pane();
    }
    
    public Button getBotonVerDetalles() {
        return btnVerDetalles;
    }
    
    public ImageView getBotonModificar() {
        return imvModificar;
    }
    
    public CheckBox getCheckBox() {
        return chbxCompletar;
    }
    
    private void establecerEstilos() {
        establecerEstiloFondoEstado();
        establecerEstiloFondoImagen();
        establecerEstiloFechas();
        establecerEstiloLabelTituloActividad();
        establecerEstiloTarjeta();
        configurarBotonVerDetalles();
        configurarBotonModificarActividad();
        configurarCheckBox();
    }
    
    private void establecerEstiloTarjeta() {
        this.setPrefSize(1150, 179);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15");
    }
    
    private void establecerEstiloFondoEstado() {
        fondoEstado.setPrefSize(150, 38);
        fondoEstado.setLayoutX(130);
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
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(80, 80);
        fondoImagen.setLayoutX(20);
        fondoImagen.setLayoutY(30);
        fondoImagen.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50");
        fondoImagen.getChildren().add(imvActividad);
        imvActividad.setFitWidth(35);
        imvActividad.setFitHeight(43);
        imvActividad.setLayoutX(20);
        imvActividad.setLayoutY(21);
    }
    
    private void establecerEstiloLabelTituloActividad() {
        lbNombreActividad.setFont(fuenteTitulos);
        lbNombreActividad.setLayoutX(130);
        lbNombreActividad.setLayoutY(72);
    }
    
    private void establecerEstiloFechas() {
        fechas.setText(actividad.getFechaInicioActividad() + " - " + actividad.getFechaFinActividad());
        fechas.setLayoutX(130);
        fechas.setLayoutY(118);
        fechas.setFont(fuenteTexto);
    }
    
    private void configurarBotonVerDetalles() {
        btnVerDetalles.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnVerDetalles.setLayoutX(970);
        btnVerDetalles.setLayoutY(120);
        btnVerDetalles.setPrefWidth(140);
        btnVerDetalles.setPrefHeight(30);
        btnVerDetalles.setFont(fuenteTexto);
    }
    
    private void configurarBotonModificarActividad() {
        fondoModificar.setPrefSize(36, 36);
        fondoModificar.setLayoutX(900);
        fondoModificar.setLayoutY(118);
        imvModificar.setFitHeight(36);
        imvModificar.setFitWidth(36);
        imvModificar.setLayoutX(0);
        imvModificar.setLayoutY(0);
        fondoModificar.getChildren().add(imvModificar);
    }
    
    private void configurarCheckBox() {
        chbxCompletar.setLayoutX(1087);
        chbxCompletar.setLayoutY(45);
        if (actividad.getEstadoActividad().equals("No completada")) {
            chbxCompletar.setDisable(true);
        }
        if (actividad.getEstadoActividad().equals("Completada")) {
            chbxCompletar.selectedProperty().set(true);
            chbxCompletar.setDisable(true);
        }
    }
}
