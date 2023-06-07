/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion del avance de cada estudiante.
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxsastr.utils.Utilidades;

public class TarjetaAvanceEstudiante extends Pane {
    
    private int idEstudiante;
    private Pane fondoImagen;
    private Label lbNombreEstudiante;
    private Label lbMatriculaEstudiante;
    private Label lbNombreTrabajoRecepcional;
    private Label lbAvance;
    private Label lbAvancePorcentaje;
    private Label lbActividadCompletada;
    private Label lbNumeroActividadesCompletadas;
    private Label lbActividadNoCompletada;
    private Label lbNumeroActividadesNoCompletadas;
    private Label lbActividadRestante;
    private Label lbNumeroActividadesRestantes;
    private Button btnVerAvance;
    private ImageView ivIconoEstudiante;

    public TarjetaAvanceEstudiante() {
    }

    public TarjetaAvanceEstudiante(int idEstudiante, String nombreEstudiante, String matriculaEstudiante, String nombreTrabajoRecepcional,
            Double avancePorcentaje, String actividadesCompletadas, String actividadesNoCompletadas, String actividadesRestantes) {
        inicializarElementos();
        establecerEstilos();
        this.idEstudiante = idEstudiante;
        this.lbNombreEstudiante.setText(nombreEstudiante);
        this.lbMatriculaEstudiante.setText(matriculaEstudiante);
        this.lbNombreTrabajoRecepcional.setText(nombreTrabajoRecepcional);
        lbAvance.setText("Avance");
        this.lbAvancePorcentaje.setText(String.valueOf(avancePorcentaje)+ "%");
        lbActividadCompletada.setText("actividades completadas.");
        this.lbNumeroActividadesCompletadas.setText(actividadesCompletadas);
        lbActividadNoCompletada.setText("actividades no completadas");
        this.lbNumeroActividadesNoCompletadas.setText(actividadesNoCompletadas);
        lbActividadRestante.setText("actividades restantes");
        this.lbNumeroActividadesRestantes.setText(actividadesRestantes);
        getChildren().addAll(fondoImagen, lbNombreEstudiante, lbMatriculaEstudiante, lbNombreTrabajoRecepcional, lbAvancePorcentaje,
                lbNumeroActividadesCompletadas, lbNumeroActividadesNoCompletadas, lbNumeroActividadesRestantes,
                lbAvance, lbActividadCompletada, lbActividadNoCompletada, lbActividadRestante, btnVerAvance);
    }
    
    private void inicializarElementos() {
        fondoImagen = new Pane();
        ivIconoEstudiante = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/estudiante.png"));
        lbNombreEstudiante = new Label();
        lbMatriculaEstudiante = new Label();
        lbNombreTrabajoRecepcional = new Label();
        lbAvancePorcentaje = new Label();
        lbNumeroActividadesCompletadas = new Label();
        lbNumeroActividadesNoCompletadas = new Label();
        lbNumeroActividadesRestantes = new Label();
        lbAvance = new Label();
        lbActividadCompletada = new Label();
        lbActividadNoCompletada = new Label();
        lbActividadRestante = new Label();
        btnVerAvance = new Button("Ver su avance");
    }
    
    private void establecerEstilos() {
        establecerEstiloPane();
        establecerEstiloFondoImagen();
        establecerEstiloNombreEstudiante();
        establecerEstiloBotonVerAvance();
        establecerEstiloActividadRestante();
        establecerEstiloActividadNoCompletada();
        establecerEstiloActividadCompletada();
        establecerEstiloNumeroActividadesRestantes();
        establecerEstiloNumeroActividadesNoCompletadas();
        establecerEstiloNumeroActividadesCompletadas();
        establecerEstiloAvancePorcentaje();
        establecerEstiloAvance();
        establecerEstiloNombreTrabajoRecepcional();
        establecerEstiloMatricula();
    }
    
    private void establecerEstiloPane() {
        setPrefSize(1572.0, 150.0);
        this.setStyle("-fx-background-color: white;"
                + "-fx-effect:  dropshadow( gaussian, rgba(0, 0, 0, 0.1), 25, 0.0, 0.0, 4);"
                + "-fx-background-radius: 15;");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(80.0, 80.0);
        fondoImagen.setLayoutX(15.0);
        fondoImagen.setLayoutY(15.0);
        fondoImagen.setStyle("-fx-background-color: #bcc4d7;"
                + "-fx-background-radius: 100;");
        fondoImagen.getChildren().add(ivIconoEstudiante);
        ivIconoEstudiante.setFitHeight(54.0);
        ivIconoEstudiante.setFitWidth(53.0);
        ivIconoEstudiante.setLayoutX(12.0);
        ivIconoEstudiante.setLayoutY(15.0);
    }
    
    private void establecerEstiloNombreEstudiante() {
        lbNombreEstudiante.setPrefSize(651.0, 30.0);
        lbNombreEstudiante.setLayoutX(120.0);
        lbNombreEstudiante.setLayoutY(11.0);
        lbNombreEstudiante.setTextAlignment(TextAlignment.LEFT);
        lbNombreEstudiante.setWrapText(true);
        lbNombreEstudiante.setFont(new Font(20.0));
        lbNombreEstudiante.setAlignment(Pos.CENTER_LEFT);
    }
        
    private void establecerEstiloMatricula() {
        lbMatriculaEstudiante.setPrefSize(261.0, 30.0);
        lbMatriculaEstudiante.setLayoutX(120.0);
        lbMatriculaEstudiante.setLayoutY(42.0);
        lbMatriculaEstudiante.setTextAlignment(TextAlignment.CENTER);
        lbMatriculaEstudiante.setWrapText(true);
        lbMatriculaEstudiante.setFont(new Font(20.0));
        lbMatriculaEstudiante.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloNombreTrabajoRecepcional() {
        lbNombreTrabajoRecepcional.setPrefSize(632.0, 53.0);
        lbNombreTrabajoRecepcional.setLayoutX(120.0);
        lbNombreTrabajoRecepcional.setLayoutY(80.0);
        lbNombreTrabajoRecepcional.setTextAlignment(TextAlignment.CENTER);
        lbNombreTrabajoRecepcional.setWrapText(true);
        lbNombreTrabajoRecepcional.setFont(new Font(20.0));
        lbNombreTrabajoRecepcional.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloAvance() {
        lbAvance.setPrefSize(66.0, 30.0);
        lbAvance.setLayoutX(786.0);
        lbAvance.setLayoutY(11.0);
        lbAvance.setTextAlignment(TextAlignment.CENTER);
        lbAvance.setWrapText(true);
        lbAvance.setFont(new Font(20.0));
        lbAvance.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloAvancePorcentaje() {
        lbAvancePorcentaje.setPrefSize(182.0, 89.0);
        lbAvancePorcentaje.setLayoutX(728.0);
        lbAvancePorcentaje.setLayoutY(42.0);
        lbAvancePorcentaje.setTextAlignment(TextAlignment.CENTER);
        lbAvancePorcentaje.setWrapText(true);
        lbAvancePorcentaje.setFont(Font.font("System", FontWeight.BOLD, 48.0));
        lbAvancePorcentaje.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloNumeroActividadesCompletadas() {
        lbNumeroActividadesCompletadas.setPrefSize(140.0, 30.0);
        lbNumeroActividadesCompletadas.setLayoutX(924.0);
        lbNumeroActividadesCompletadas.setLayoutY(24.0);
        lbNumeroActividadesCompletadas.setTextAlignment(TextAlignment.CENTER);
        lbNumeroActividadesCompletadas.setWrapText(true);
        lbNumeroActividadesCompletadas.setFont(new Font(20.0));
        lbNumeroActividadesCompletadas.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloNumeroActividadesNoCompletadas() {
        lbNumeroActividadesNoCompletadas.setPrefSize(140.0, 30.0);
        lbNumeroActividadesNoCompletadas.setLayoutX(924.0);
        lbNumeroActividadesNoCompletadas.setLayoutY(62.0);
        lbNumeroActividadesNoCompletadas.setTextAlignment(TextAlignment.CENTER);
        lbNumeroActividadesNoCompletadas.setWrapText(true);
        lbNumeroActividadesNoCompletadas.setFont(new Font(20.0));
        lbNumeroActividadesNoCompletadas.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloNumeroActividadesRestantes() {
        lbNumeroActividadesRestantes.setPrefSize(140.0, 30.0);
        lbNumeroActividadesRestantes.setLayoutX(924.0);
        lbNumeroActividadesRestantes.setLayoutY(99.0);
        lbNumeroActividadesRestantes.setTextAlignment(TextAlignment.CENTER);
        lbNumeroActividadesRestantes.setWrapText(true);
        lbNumeroActividadesRestantes.setFont(new Font(20.0));
        lbNumeroActividadesRestantes.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloActividadCompletada() {
        lbActividadCompletada.setPrefSize(312.0, 30.0);
        lbActividadCompletada.setLayoutX(1072.0);
        lbActividadCompletada.setLayoutY(22.0);
        lbActividadCompletada.setTextAlignment(TextAlignment.CENTER);
        lbActividadCompletada.setWrapText(true);
        lbActividadCompletada.setFont(new Font(20.0));
        lbActividadCompletada.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloActividadNoCompletada() {
        lbActividadNoCompletada.setPrefSize(312.0, 30.0);
        lbActividadNoCompletada.setLayoutX(1072.0);
        lbActividadNoCompletada.setLayoutY(62.0);
        lbActividadNoCompletada.setTextAlignment(TextAlignment.CENTER);
        lbActividadNoCompletada.setWrapText(true);
        lbActividadNoCompletada.setFont(new Font(20.0));
        lbActividadNoCompletada.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloActividadRestante() {
        lbActividadRestante.setPrefSize(312.0, 30.0);
        lbActividadRestante.setLayoutX(1072.0);
        lbActividadRestante.setLayoutY(99.0);
        lbActividadRestante.setTextAlignment(TextAlignment.CENTER);
        lbActividadRestante.setWrapText(true);
        lbActividadRestante.setFont(new Font(20.0));
        lbActividadRestante.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloBotonVerAvance() {
        btnVerAvance.setPrefSize(185.0, 33.0);
        btnVerAvance.setLayoutX(1370.0);
        btnVerAvance.setLayoutY(95.0);
        btnVerAvance.setFont(new Font(20.0));
        btnVerAvance.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 15;"
                + "-fx-background-color: #c9c9c9");
        btnVerAvance.setOnAction((event) -> {
            Stage  escenarioBase = new Stage();
            escenarioBase.setScene(Utilidades.inicializarEscena("vistas/FXMLAvanceEstudiante.fxml"));    //CAMBIAR A LA RUTA 
            escenarioBase.setTitle("Avance del estudiante");
            escenarioBase.initModality(Modality.APPLICATION_MODAL);
            escenarioBase.showAndWait();
            
        });
    }
    
}
