/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 08/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion las entregas de una actividad que se le muestran al estudiante
 */

package javafxsastr.utils.cards;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.modelos.pojo.Entrega;

public class TarjetaEntregas extends Pane{
    
    private Entrega entrega;
    private Button btnVerDetalles;
    private Label lbNumeroEntrega;
    private Label lbFechaEnvio;
    private Label lbHoraEnvio;
    private Label lbEstadoEntrega;
    private ImageView imgEntrega;
    private final DateTimeFormatter FORMATO_FECHA_COMPLETA = DateTimeFormatter.ofPattern("EEEE ',' dd 'de' MMMM 'de' yyyy",
            new Locale("es"));
    
   public TarjetaEntregas(Entrega entrega, int numeroEntrega) {
        this.entrega = entrega;
        inicializarElementos();
        establecerEstilos();
        lbNumeroEntrega.setText("Entrega #" + String.valueOf(numeroEntrega));
        getChildren().addAll(btnVerDetalles, lbNumeroEntrega, lbFechaEnvio, lbHoraEnvio, 
                lbEstadoEntrega, imgEntrega);
   }
   
   public Button getBotonVerDetalles() {
       return this.btnVerDetalles;
   }

    private void inicializarElementos() {
        LocalDate fechaEnvio = LocalDate.parse(entrega.getFechaEntrega());        
        imgEntrega = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/actividad-entrega.png"));
        lbNumeroEntrega = new Label();
        lbFechaEnvio = new Label("Fecha de envio: " + fechaEnvio.format(FORMATO_FECHA_COMPLETA));
        lbHoraEnvio = new Label("Hora de envio: " + entrega.getHoraEntrega());
        lbEstadoEntrega = new Label();
        btnVerDetalles = new Button("Ver detalles");
    }

    private void establecerEstilos() {
        establecerEstiloPane();
        establecerEstiloIcono();
        establecerEstiloNumeroEntrega();
        establecerEstiloFechaEnvio();
        establecerEstiloHoraEnvio();
        establecerEstiloEstadoEntrega();
        establecerEstiloBotonVerDetalles();
    }
    
    private void establecerEstiloPane() {
        setPrefSize(1536.0, 140.0);
        this.setStyle("-fx-background-color: white;"
                + "-fx-effect:  dropshadow( gaussian, rgba(0, 0, 0, 0.1), 25, 0.0, 0.0, 4);"
                + "-fx-background-radius: 15;");    
    }
    
    private void establecerEstiloIcono() {
        imgEntrega.setFitHeight(40.0);
        imgEntrega.setFitWidth(40.0);
        imgEntrega.setLayoutX(11.0);
        imgEntrega.setLayoutY(11.0);
    }
    
    private void establecerEstiloNumeroEntrega() {
        lbNumeroEntrega.setPrefSize(215.0, 32.0);
        lbNumeroEntrega.setLayoutX(67.0);
        lbNumeroEntrega.setLayoutY(10.0);
        lbNumeroEntrega.setTextAlignment(TextAlignment.LEFT);
        lbNumeroEntrega.setWrapText(true);
        lbNumeroEntrega.setFont(new Font(20.0));
        lbNumeroEntrega.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloFechaEnvio() {
        lbFechaEnvio.setPrefSize(647.0, 32.0);
        lbFechaEnvio.setLayoutX(15.0);
        lbFechaEnvio.setLayoutY(58.0);
        lbFechaEnvio.setTextAlignment(TextAlignment.LEFT);
        lbFechaEnvio.setWrapText(true);
        lbFechaEnvio.setFont(new Font(20.0));
        lbFechaEnvio.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloHoraEnvio() {
        lbHoraEnvio.setPrefSize(270.0, 32.0);
        lbHoraEnvio.setLayoutX(868.0);
        lbHoraEnvio.setLayoutY(58.0);
        lbHoraEnvio.setTextAlignment(TextAlignment.LEFT);
        lbHoraEnvio.setWrapText(true);
        lbHoraEnvio.setFont(new Font(20.0));
        lbHoraEnvio.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloEstadoEntrega() {
        lbEstadoEntrega.setPrefSize(588.0, 32.0);
        lbEstadoEntrega.setLayoutX(15.0);
        lbEstadoEntrega.setLayoutY(96.0);
        lbEstadoEntrega.setTextAlignment(TextAlignment.LEFT);
        lbEstadoEntrega.setWrapText(true);
        lbEstadoEntrega.setFont(new Font(20.0));
        lbEstadoEntrega.setAlignment(Pos.CENTER_LEFT);
        if (entrega.getFechaRevision() == null) {
            lbEstadoEntrega.setText("Estado: Sin revisar");
        } else {
            lbEstadoEntrega.setText("Estado: Revisado");
        }
    }
    
    private void establecerEstiloBotonVerDetalles() {
        btnVerDetalles.setPrefSize(197.0, 55.0);
        btnVerDetalles.setLayoutX(1327.0);
        btnVerDetalles.setLayoutY(77.0);
        btnVerDetalles.setFont(new Font(20.0));
        btnVerDetalles.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 13;"
                + "-fx-background-color: #c9c9c9");
    }
    
}