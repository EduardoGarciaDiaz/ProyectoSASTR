/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 04/06/2023
 * Descripción: Clase para las tarjetas de la ventana asignarEstudianteCursor.
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.modelos.pojo.Estudiante;


public class TarjetaAgregarEstudianteCurso extends Pane {
    
    private Pane fondo ;
    private  Label nombreEstudiante;
    private  ImageView imvEliminar ;
    private Estudiante estudianteTarjeta;
    
    public TarjetaAgregarEstudianteCurso(Estudiante estudiante) {
        estudianteTarjeta = estudiante;
        inicializarElementos();
        establecerEstiloPane();
        establecerEstiloFondoImagen();
        establecerEstiloNombreEstudiante();
        nombreEstudiante.setText(estudiante.getNombre()
               +" "+estudiante.getPrimerApellido()+ " "+ estudiante.getSegundoApellido()+ "      " +estudiante.getMatriculaEstudiante());
               getChildren().addAll(fondo, nombreEstudiante);       
    }
    
    public Pane getImagen() {
        return this.fondo;
    }
    
    public Estudiante getEstudinate() {
        return estudianteTarjeta; 
    }
    
    private void inicializarElementos() {
        fondo = new Pane();       
        imvEliminar = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/delete.jpg")); 
        nombreEstudiante = new Label();
    }
    
    private void establecerEstiloPane() {
        setPrefSize(530, 30);
        setLayoutX(20);
        setLayoutY(20);         
        this.setStyle("-fx-background-color: #F4F4F4");
    }
    
    private void establecerEstiloFondoImagen() {
        fondo.setPrefSize(30, 25);
        fondo.setLayoutX(500);
        fondo.setLayoutY(0);
        fondo.setStyle("-fx-background-color: #F4F4F4");
        fondo.getChildren().add(imvEliminar);
        imvEliminar.setFitHeight(30);
        imvEliminar.setFitWidth(25);
        imvEliminar.setLayoutX(30);
        imvEliminar.setLayoutY(0);
    }
    
    private void establecerEstiloNombreEstudiante() {
        nombreEstudiante.setPrefSize(510, 20.0);
        nombreEstudiante.setLayoutX(10);
        nombreEstudiante.setLayoutY(0.0);
        nombreEstudiante.setTextAlignment(TextAlignment.CENTER);
        nombreEstudiante.setWrapText(true);
        nombreEstudiante.setFont(new Font(20));
        nombreEstudiante.setAlignment(Pos.CENTER_LEFT);
    }
    
}
