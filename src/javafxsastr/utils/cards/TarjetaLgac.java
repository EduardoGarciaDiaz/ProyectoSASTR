/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 08/06/2023
 * Descripción: Clase para la tarjetas de Lgac.
 */

package javafxsastr.utils.cards;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafxsastr.modelos.pojo.Lgac;

public class TarjetaLgac extends Pane{
    
    private Lgac lgac;
    private Label lbNombreCuerpoLgac;
    private Label lbDescripcionLgac;
    private Pane fondoImagen;
    private ImageView imvLgac = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/LgacIcono.jpg"));
    private Button btnModificarLgac;
    private Font fuente = new Font(20);
    
    public TarjetaLgac(Lgac lgacN) {
       this.lgac = lgacN;
       inicializarElementos();
       establecerEstiloTarjeta();
       establecerEstiloFondoImagen();
       establecerEstiloBotonModificar();
       establecerEstiloLabels();
       this.getChildren().addAll(lbNombreCuerpoLgac, lbDescripcionLgac, fondoImagen, btnModificarLgac);
    }
     
    public Button getBotonModificarLgac() {
        return btnModificarLgac;
    }
   
     private void inicializarElementos() {
        lbNombreCuerpoLgac = new Label("Nombre: " + lgac.getNombreLgac());
        lbDescripcionLgac = new Label("Descripcion: " + lgac.getDescripcionLgac());        
        fondoImagen = new Pane();
        btnModificarLgac = new Button("Modificar Lgac");
    }
      
    public void establecerEstiloTarjeta() {
        this.setPrefSize(1555, 150);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(95, 95);
        fondoImagen.setLayoutX(31);
        fondoImagen.setLayoutY(21);
        fondoImagen.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50");
        fondoImagen.getChildren().add(imvLgac);
        imvLgac.setFitWidth(53);
        imvLgac.setFitHeight(53);
        imvLgac.setLayoutX(21);
        imvLgac.setLayoutY(21);
    }
    
      private void establecerEstiloLabels() {
        lbNombreCuerpoLgac.setFont(fuente);
        lbNombreCuerpoLgac.setLayoutX(151);
        lbNombreCuerpoLgac.setLayoutY(20);
        lbDescripcionLgac.setWrapText(true);
        lbDescripcionLgac.setPrefSize(1000,80);
        lbDescripcionLgac.setFont(fuente);
        lbDescripcionLgac.setLayoutX(150);
        lbDescripcionLgac.setLayoutY(50); 
        
    }
      
    private void establecerEstiloBotonModificar() {
        btnModificarLgac.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnModificarLgac.setLayoutX(1300);
        btnModificarLgac.setLayoutY(90);
        btnModificarLgac.setPrefWidth(230);
        btnModificarLgac.setPrefHeight(40);
    }   
    
}
