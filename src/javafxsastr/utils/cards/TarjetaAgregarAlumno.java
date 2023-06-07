/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 24/05/2023
 * Descripción: Clae de la tarjeta añadir estudinate en DetallesCurso.
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.JavaFXSASTR;
import javafxsastr.controladores.FXMLDetallesCursoController;

public class TarjetaAgregarAlumno extends Pane{
    private Pane fondo;    
    private Label instruccion;
    private ImageView imvIconoAgregar;
    
    public TarjetaAgregarAlumno() {
        inicializarElementos();
        establecerEstiloPane();
        establecerEstiloFondoImagen();
        establecerEstiloInstruccion();
        getChildren().addAll(fondo,instruccion);
        imvIconoAgregar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {              
                try {
                    FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLDetallesCurso.fxml"));
                    Parent vista = accesoControlador.load();
                    FXMLDetallesCursoController formulario = accesoControlador.getController();            
                    formulario.agregarAlumno();                   
                } catch (IOException ex) {
                    Logger.getLogger(TarjetaAgregarAlumno.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        });
    }
    
    private void inicializarElementos() {
        fondo = new Pane();       
        imvIconoAgregar = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/agregarEstudiante.jpg")); 
        instruccion = new Label("Agregar Estudiante Al curso");
    }
    
    private void establecerEstiloPane() {
        setPrefSize(249, 249);
        setLayoutX(20);
        setLayoutY(20);
        this.setStyle("-fx-background-color: #F4F4F4");
    }
    
    private void establecerEstiloFondoImagen() {
        fondo.setPrefSize(104.0, 104.0);
        fondo.setLayoutX(80.5);
        fondo.setLayoutY(14.0);
        fondo.setStyle("-fx-background-color: #F4F4F4");
        fondo.getChildren().add(imvIconoAgregar);
        imvIconoAgregar.setFitHeight(100);
        imvIconoAgregar.setFitWidth(100);
        imvIconoAgregar.setLayoutX(2.0);
        imvIconoAgregar.setLayoutY(21.0);
    }
    
    private void establecerEstiloInstruccion() {
        instruccion.setPrefSize(200.0, 80.0);
        instruccion.setLayoutX(30);
        instruccion.setLayoutY(150);
        instruccion.setTextAlignment(TextAlignment.CENTER);
        instruccion.setWrapText(true);
        instruccion.setFont(new Font(20));
        instruccion.setAlignment(Pos.CENTER_LEFT);
    }
    
}
