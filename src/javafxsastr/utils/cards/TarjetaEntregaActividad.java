/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion las entregas de una actividad.
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxsastr.utils.Utilidades;

public class TarjetaEntregaActividad extends Pane{
    
    private Pane fondoImagen;
    private Button btnRevisar;
    private Label lbNumeroEntrega;
    private Label lbFechaEntrega;
    private Label lbHoraEntrega;
    private Label lbFechaRevision;
    
    private boolean esRevisada = false;
    
    public TarjetaEntregaActividad(int numeroEntrega, String fechaEntrega, String horaEntrega, String fechaRevision) {
        if (!fechaRevision.isEmpty()) {
            esRevisada = true;
        }
        inicializarElementos();
        establecerEstilos();
        this.lbNumeroEntrega.setText(String.valueOf(numeroEntrega));
        this.lbFechaEntrega.setText(fechaEntrega);
        this.lbHoraEntrega.setText("Recibido a las " + horaEntrega);
        this.lbFechaRevision.setText(fechaRevision);
        getChildren().addAll(fondoImagen, btnRevisar, lbNumeroEntrega, lbFechaEntrega,
                lbHoraEntrega, lbFechaRevision);
    }

    private void inicializarElementos() {
        fondoImagen = new Pane();
        lbNumeroEntrega = new Label();
        lbFechaEntrega = new Label();
        lbHoraEntrega = new Label();
        lbFechaRevision = new Label();
        if (esRevisada) {
            btnRevisar = new Button("Mod. Revisión");
        } else {
            btnRevisar = new Button("Revisar");
        }
    }

    private void establecerEstilos() {
        establecerEstiloPane();
        establecerEstiloNumeroEntrega();
        establecerEstiloFechaEntrega();
        establecerEstiloHoraEntrega();
        establecerEstiloFechaRevision();
        establecerEstiloBotonVerAvance();
    }
    
    private void establecerEstiloPane() {
        setPrefSize(970.0, 75.0);
        this.setStyle("-fx-background-color: white;"
                + "-fx-effect:  dropshadow( gaussian, rgba(0, 0, 0, 0.1), 25, 0.0, 0.0, 4);"
                + "-fx-background-radius: 15;");
    }
    
    private void establecerEstiloNumeroEntrega() {
        lbNumeroEntrega.setPrefSize(64.0, 32.0);
        lbNumeroEntrega.setLayoutX(20.0);
        lbNumeroEntrega.setLayoutY(22.0);
        lbNumeroEntrega.setTextAlignment(TextAlignment.CENTER);
        lbNumeroEntrega.setWrapText(true);
        lbNumeroEntrega.setFont(new Font(20.0));
        lbNumeroEntrega.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloFechaEntrega() {
        lbFechaEntrega.setPrefSize(372.0, 32.0);
        lbFechaEntrega.setLayoutX(131.0);
        lbFechaEntrega.setLayoutY(22.0);
        lbFechaEntrega.setTextAlignment(TextAlignment.CENTER);
        lbFechaEntrega.setWrapText(true);
        lbFechaEntrega.setFont(new Font(20.0));
        lbFechaEntrega.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloHoraEntrega() {
        lbHoraEntrega.setPrefSize(227.0, 30.0);
        lbHoraEntrega.setLayoutX(506.0);
        lbHoraEntrega.setLayoutY(22.0);
        lbHoraEntrega.setTextAlignment(TextAlignment.CENTER);
        lbHoraEntrega.setWrapText(true);
        lbHoraEntrega.setFont(new Font(20.0));
        lbHoraEntrega.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloFechaRevision() {
        lbFechaRevision.setPrefSize(204.0, 20.0);
        lbFechaRevision.setLayoutX(780.0);
        lbFechaRevision.setLayoutY(51.0);
        lbFechaRevision.setTextAlignment(TextAlignment.CENTER);
        lbFechaRevision.setWrapText(true);
        lbFechaRevision.setFont(new Font(14.0));
        lbFechaRevision.setAlignment(Pos.CENTER);
    }

    private void establecerEstiloBotonVerAvance() {
        btnRevisar.setPrefSize(121.0, 31.0);
        btnRevisar.setLayoutX(820.0);
        btnRevisar.setLayoutY(15.0);
        btnRevisar.setFont(new Font(15.0));
        btnRevisar.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 15;"
                + "-fx-background-color: #C4DAEF");
        btnRevisar.setOnAction((event) -> {
            Stage  escenarioBase = new Stage();
            escenarioBase.setScene(Utilidades.inicializarEscena("vistas/FXMLFormularioUsuario.fxml")); //CAMBIAR A LA RUTA 
            escenarioBase.setTitle("Entrega del estudiante");
            escenarioBase.initModality(Modality.APPLICATION_MODAL);
            escenarioBase.showAndWait();
            
        });
    }
    
}
