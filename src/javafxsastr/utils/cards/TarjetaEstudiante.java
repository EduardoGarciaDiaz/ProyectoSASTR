/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 17/05/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion del estudiante.
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.modelos.pojo.Estudiante;

public class TarjetaEstudiante extends Pane {
   
    private Pane fondoImagen;
    private ImageView imvIconoEstudiante;
    private Label lbNombreEstudiante;
    private Label lbMatriculaEstudiante;
    private Label lbNombreTrabajoRecepcional;
    private Button btnVerAvance;
    private Estudiante estudiante;

    public TarjetaEstudiante() {
    }

    public TarjetaEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        inicializarElementos();
        establecerEstiloPane();
        establecerEstiloFondoImagen();
        establecerEstiloNombreEstudiante();
        establecerEstiloMatricula();
        establecerEstiloNombreTrabajoRecepcional();
        establecerEstiloBotonVerAvance();
        getChildren().addAll(fondoImagen, lbNombreEstudiante, lbMatriculaEstudiante, 
                lbNombreTrabajoRecepcional, btnVerAvance);
    }
    
    public Button getBotonVerAvance() {
        return btnVerAvance;
    }
    
    private void inicializarElementos() {
        fondoImagen = new Pane();
        imvIconoEstudiante = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/estudiante.png"));
        lbNombreEstudiante = new Label(obtenerNombreEstudiante());
        lbMatriculaEstudiante = new Label(estudiante.getMatriculaEstudiante());
        lbNombreTrabajoRecepcional = new Label(estudiante.getAnteproyectoEstudiante());
        btnVerAvance = new Button("Ver su avance");
    }
    
    private String obtenerNombreEstudiante() {
        String nombre = "";
        String primerApellido = "";
        String segundoApellido = "";
        if (estudiante.getNombre() != null) {
            nombre = estudiante.getNombre();
        }
        if (estudiante.getPrimerApellido() != null) {
            primerApellido = estudiante.getPrimerApellido();
        } 
        if (estudiante.getSegundoApellido() != null) {
            segundoApellido = estudiante.getSegundoApellido();
        }
        return nombre + " " + primerApellido + " " + segundoApellido;
    }
    
    private void establecerEstiloPane() {
        setPrefSize(400.0, 400.0);
        this.setStyle("-fx-background-color: white;"
                + "-fx-effect:  dropshadow( gaussian, rgba(0, 0, 0, 0.1), 25, 0.0, 0.0, 4);"
                + "-fx-background-radius: 15;");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(104.0, 104.0);
        fondoImagen.setLayoutX(148.0);
        fondoImagen.setLayoutY(14.0);
        fondoImagen.setStyle("-fx-background-color: #bcc4d7;"
                + "-fx-background-radius: 100;");
        fondoImagen.getChildren().add(imvIconoEstudiante);
        imvIconoEstudiante.setFitHeight(62.0);
        imvIconoEstudiante.setFitWidth(70.0);
        imvIconoEstudiante.setLayoutX(16.0);
        imvIconoEstudiante.setLayoutY(21.0);
    }
    
    private void establecerEstiloNombreEstudiante() {
        lbNombreEstudiante.setPrefSize(360.0, 62.0);
        lbNombreEstudiante.setLayoutX(20.0);
        lbNombreEstudiante.setLayoutY(124.0);
        lbNombreEstudiante.setTextAlignment(TextAlignment.CENTER);
        lbNombreEstudiante.setWrapText(true);
        lbNombreEstudiante.setFont(new Font(20.0));
        lbNombreEstudiante.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloMatricula() {
        lbMatriculaEstudiante.setPrefSize(104.0, 35.0);
        lbMatriculaEstudiante.setLayoutX(149.0);
        lbMatriculaEstudiante.setLayoutY(183.0);
        lbMatriculaEstudiante.setTextAlignment(TextAlignment.CENTER);
        lbMatriculaEstudiante.setWrapText(true);
        lbMatriculaEstudiante.setFont(new Font(20.0));
        lbMatriculaEstudiante.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloNombreTrabajoRecepcional() {
        lbNombreTrabajoRecepcional.setPrefSize(360.0, 100.0);
        lbNombreTrabajoRecepcional.setLayoutX(20.0);
        lbNombreTrabajoRecepcional.setLayoutY(223.0);
        lbNombreTrabajoRecepcional.setTextAlignment(TextAlignment.CENTER);
        lbNombreTrabajoRecepcional.setWrapText(true);
        lbNombreTrabajoRecepcional.setFont(new Font(20.0));
        lbNombreTrabajoRecepcional.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloBotonVerAvance() {
        btnVerAvance.setPrefSize(185.0, 33.0);
        btnVerAvance.setLayoutX(108.0);
        btnVerAvance.setLayoutY(334.0);
        btnVerAvance.setFont(new Font(20.0));
        btnVerAvance.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 13;"
                + "-fx-background-color: #C4DAEF;");
    }
    
}
