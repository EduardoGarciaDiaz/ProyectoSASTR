/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Clase para las tarjetas de la ventana asignarEstudianteCursor.
 */

package javafxsastr.utils.cards;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.JavaFXSASTR;
import javafxsastr.controladores.FXMLAsignarEstudianteCursoController;
import javax.print.DocFlavor;

public class TarjetaAgregarEstudianteCurso extends Pane {
    private Pane fondo ;
    private  Label nombreEstudiante;
    private  ImageView imvEliminar ;
    
    public TarjetaAgregarEstudianteCurso(String nombre, int IdEstudinate) {
        inicializarElementos();
        establecerEstiloPane();
        establecerEstiloFondoImagen();
        establecerEstiloNombreEstudiante();
        nombreEstudiante.setText(nombre);
        getChildren().addAll(fondo, nombreEstudiante);
        setId(String.valueOf(IdEstudinate));
         
        imvEliminar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {              
                try {
                    FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLAsignarEstudianteCurso.fxml"));
                    Parent vista = accesoControlador.load();
                    FXMLAsignarEstudianteCursoController accion = accesoControlador.getController();            
                    accion.quitarEstudianste(IdEstudinate);            
                } catch (IOException ex) {                    
                    Logger.getLogger(TarjetaAgregarAlumno.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        });
        
    }
    
    public ImageView getImagen() {
        return this.imvEliminar;
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
        fondo.setPrefSize(30, 30);
        fondo.setLayoutX(520);
        fondo.setLayoutY(-30);
        fondo.setStyle("-fx-background-color: #F4F4F4");
        fondo.getChildren().add(imvEliminar);
        imvEliminar.setFitHeight(30);
        imvEliminar.setFitWidth(30);
        imvEliminar.setLayoutX(30);
        imvEliminar.setLayoutY(30);
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
