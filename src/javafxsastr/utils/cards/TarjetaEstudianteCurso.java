/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 04/06/2023
 * Descripción: Clase para la tarjetas de DetalleCursor.
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.modelos.pojo.Estudiante;

public class TarjetaEstudianteCurso extends Pane{
    private Pane fondo;
    private Pane fondoEitar;
    private Pane fondoDesactivar;
    private Label lbNombreEstudiante;
    private Label lbcorreoEstudinate;
    private ImageView imvIconoEstudiate;
    private ImageView imvBtnDesactivar;
    private ImageView imvBtnEditar;
     private Circle crlEstadoUsuario;
    private boolean esActivo = false;
    private Estudiante estudiante;
    
    public TarjetaEstudianteCurso(Estudiante estudianteN) {
        this.estudiante = estudianteN;
        inicializarElementos();
        establecerEstiloPane();
        establecerEstiloFondoImagen();
        establecerEstiloNombreEstudiante();
        establecerColorEstadoEstudinate(estudiante);
        establecerEstiloCorreo();
        lbNombreEstudiante.setText(estudiante.getNombre()+" "+estudiante.getPrimerApellido()+" "+estudiante.getSegundoApellido());
        lbNombreEstudiante.setWrapText(true);
        lbcorreoEstudinate.setText(estudiante.getCorreoInstitucional());
        lbcorreoEstudinate.setWrapText(true);
        getChildren().addAll(fondo,fondoEitar,crlEstadoUsuario,fondoDesactivar,lbNombreEstudiante,lbcorreoEstudinate);     
    }
    
    public Estudiante getEstudiante() {
        return this.estudiante;
    }
    
    public Pane getBotonDesactivar() {
        return fondoDesactivar;
    }
    
     public Pane getBotonEditar() {
        return fondoEitar;
    }
    
    private void inicializarElementos() {
        fondo = new Pane();
        fondoEitar = new Pane();
        fondoDesactivar= new Pane();
        lbNombreEstudiante = new Label();
        lbcorreoEstudinate = new Label();
        imvIconoEstudiate = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/iconoEstudianteCurso.jpg")); 
        imvBtnEditar = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/editarCurso.jpg"));   
        if(estudiante.getIdEstadoUsuario() == 1) {
            imvBtnDesactivar = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/desactivarCurso.jpg")); 
        }else {
            imvBtnDesactivar = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/activarEstudiante.png")); 
        }        
        crlEstadoUsuario =new Circle(14.46);
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
        fondo.getChildren().add(imvIconoEstudiate);
        imvIconoEstudiate.setFitHeight(100);
        imvIconoEstudiate.setFitWidth(100);
        imvIconoEstudiate.setLayoutX(2.0);
        imvIconoEstudiate.setLayoutY(21.0);
        
        fondoEitar.setPrefSize(30, 30);
        fondoEitar.setLayoutX(210);
        fondoEitar.setLayoutY(205);
        fondoEitar.setStyle("-fx-background-color: #bcc4d7");
        fondoEitar.getChildren().add(imvBtnEditar);
        imvBtnEditar.setFitHeight(40);
        imvBtnEditar.setFitWidth(40);
        imvBtnEditar.setLayoutX(0);
        imvBtnEditar.setLayoutY(0);
        
        fondoDesactivar.setPrefSize(30, 30);
        fondoDesactivar.setLayoutX(160);
        fondoDesactivar.setLayoutY(205);
        fondoDesactivar.setStyle("-fx-background-color: #bcc4d7");     
        fondoDesactivar.getChildren().add(imvBtnDesactivar);
        imvBtnDesactivar.setFitHeight(40);
        imvBtnDesactivar.setFitWidth(40);
        imvBtnDesactivar.setLayoutX(0);
        imvBtnDesactivar.setLayoutY(0);     
    }
     
    private void establecerColorEstadoEstudinate(Estudiante estudiante) {        
        if(estudiante.getIdEstadoUsuario() == 1) {
             crlEstadoUsuario.setFill(Color.web("#C3E0BE"));
        }else {
            crlEstadoUsuario.setFill(Color.web("#EBE555"));
        }  
        crlEstadoUsuario.setLayoutX(230);
        crlEstadoUsuario.setLayoutY(20);
    }
     
    private void establecerEstiloNombreEstudiante() {
        lbNombreEstudiante.setPrefSize(240.0, 72.0);
        lbNombreEstudiante.setLayoutX(10);
        lbNombreEstudiante.setLayoutY(114.0);
        lbNombreEstudiante.setTextAlignment(TextAlignment.CENTER);
        lbNombreEstudiante.setWrapText(true);
        lbNombreEstudiante.setFont(new Font(20));
        lbNombreEstudiante.setAlignment(Pos.CENTER);
    }
    
    private void establecerEstiloCorreo() {
        lbcorreoEstudinate.setPrefSize(200.0, 62.0);
        lbcorreoEstudinate.setLayoutX(30);
        lbcorreoEstudinate.setLayoutY(155.0);
        lbcorreoEstudinate.setTextAlignment(TextAlignment.CENTER);
        lbcorreoEstudinate.setWrapText(true);
        lbcorreoEstudinate.setFont(new Font(20.0));
        lbcorreoEstudinate.setAlignment(Pos.CENTER);
    }      
     
}
