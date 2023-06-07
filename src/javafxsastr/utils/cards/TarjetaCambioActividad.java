/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de los cambios en las actividades por el estudiante
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TarjetaCambioActividad extends Pane{
    
    private Pane fondoImagen;
    private Label lbCambio;
    private Label lbFechaDeCambio;

    public TarjetaCambioActividad(String cambio, String fechaCambio) {
        inicializarElementos();
        establecerEstilos();
        this.lbCambio.setText(cambio);
        this.lbFechaDeCambio.setText(fechaCambio);
        getChildren().addAll(fondoImagen, lbFechaDeCambio, lbCambio);
    }

    private void inicializarElementos() {
        fondoImagen = new Pane();
        lbCambio = new Label();
        lbFechaDeCambio = new Label();
    }

    private void establecerEstilos() {
        establecerEstiloPane();
        establecerEstiloCambio();
        establecerEstiloFecha();
    }
    
    private void establecerEstiloPane() {
        setPrefSize(649.0, 94.0);
        this.setStyle("-fx-background-color: white;"
                + "-fx-effect:  dropshadow( gaussian, rgba(0, 0, 0, 0.1), 25, 0.0, 0.0, 4);"
                + "-fx-background-radius: 15;");
    }
    
    private void establecerEstiloCambio() {
        lbCambio.setPrefSize(438.0, 100.0);
        lbCambio.setLayoutX(210.0);
        lbCambio.setLayoutY(2.0);
        lbCambio.setTextAlignment(TextAlignment.LEFT);
        lbCambio.setWrapText(true);
        lbCambio.setFont(new Font(16.0));
        lbCambio.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloFecha() {
        lbFechaDeCambio.setPrefSize(195.0, 31.0);
        lbFechaDeCambio.setLayoutX(12.0);
        lbFechaDeCambio.setLayoutY(32.0);
        lbFechaDeCambio.setTextAlignment(TextAlignment.CENTER);
        lbFechaDeCambio.setWrapText(true);
        lbFechaDeCambio.setFont(new Font(18.0));
        lbFechaDeCambio.setAlignment(Pos.CENTER_LEFT);
    }
    
}
