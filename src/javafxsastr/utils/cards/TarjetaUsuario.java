/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 17/05/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de n usuario.
 */

package javafxsastr.utils.cards;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafxsastr.modelos.pojo.Usuario;

public class TarjetaUsuario extends Pane{
    
    private Usuario usuario;
    private Label lbNombreUsuario;
    private Label lbCorreoInstitucional;
    private Label lbTipoUsuario;
    private Pane fondoImagen;
    private ImageView imvUsuario = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/usuario.png"));
    private Button btnVerDetallesUsuario;
    private Font fuente = new Font(20);
    private Rectangle indicadorEstadoUsuario;
            
    public TarjetaUsuario(Usuario usuario) {
        this.usuario = usuario;
        inicializarElementos();
        establecerEstiloTarjeta();
        establecerEstiloLabels();
        establecerEstiloFondoImagen();
        establecerEstiloBotonVerDetalles();
        establecerEstiloIndicador();
        getChildren().addAll(lbNombreUsuario, lbCorreoInstitucional, lbTipoUsuario,
                fondoImagen, btnVerDetallesUsuario, indicadorEstadoUsuario);
    }
    
    public Button getBotonVerDetalles() {
        return btnVerDetallesUsuario;
    }
    
    private void inicializarElementos() {
        lbNombreUsuario = new Label(usuario.toString());
        lbCorreoInstitucional = new Label(usuario.getCorreoInstitucional());
        lbTipoUsuario = new Label();
        fondoImagen = new Pane();
        btnVerDetallesUsuario = new Button("Ver detalles");
        indicadorEstadoUsuario = new Rectangle();
    }
    
    private void establecerEstiloTarjeta() {
        this.setPrefSize(1555, 150);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15");
    }
    
    private void establecerEstiloIndicador() {
        indicadorEstadoUsuario.setHeight(30);
        indicadorEstadoUsuario.setWidth(30);
        indicadorEstadoUsuario.setStyle("-fx-arc-height: 500; -fx-arc-width: 500; -fx-stroke-width: 0;");
        if (usuario.getIdEstadoUsuario() == 1) {
            indicadorEstadoUsuario.setFill(Color.web("#89D759"));
        } else {
            indicadorEstadoUsuario.setFill(Color.web("#EB5555"));
        }
        indicadorEstadoUsuario.setLayoutX(1500);
        indicadorEstadoUsuario.setLayoutY(20);
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(95, 95);
        fondoImagen.setLayoutX(31);
        fondoImagen.setLayoutY(21);
        fondoImagen.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50");
        fondoImagen.getChildren().add(imvUsuario);
        imvUsuario.setFitWidth(53);
        imvUsuario.setFitHeight(53);
        imvUsuario.setLayoutX(21);
        imvUsuario.setLayoutY(21);
    }
    
    private void establecerEstiloLabels() {
        lbNombreUsuario.setFont(fuente);
        lbNombreUsuario.setLayoutX(151);
        lbNombreUsuario.setLayoutY(20);
        lbCorreoInstitucional.setFont(fuente);
        lbCorreoInstitucional.setLayoutX(151);
        lbCorreoInstitucional.setLayoutY(50);
        lbTipoUsuario.setFont(fuente);
        lbTipoUsuario.setLayoutX(151);
        lbTipoUsuario.setLayoutY(70);
    }
    
    private void establecerEstiloBotonVerDetalles() {
        btnVerDetallesUsuario.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnVerDetallesUsuario.setLayoutX(1350);
        btnVerDetallesUsuario.setLayoutY(90);
        btnVerDetallesUsuario.setPrefWidth(180);
        btnVerDetallesUsuario.setPrefHeight(40);
    }
    
}
