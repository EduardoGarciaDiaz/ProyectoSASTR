/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 17/05/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de un curso.
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
import javafxsastr.modelos.pojo.Curso;
import javafxsastr.utils.Utilidades;

public class TarjetaCursoCuadrada extends Pane {
    
    private Pane pnfondoImagen;
    private ImageView imvImagenCurso 
                = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/curso-2.png"));
    private Label lbNombreCurso;
    private Label lbNrc;
    private Label lbPeriodoEscolar;
    private Button btnVerDetallesCurso;
    private Curso curso;
    
    public TarjetaCursoCuadrada(Curso curso) {
        this.curso = curso;
        inicializarElementos();
        establecerEstilos();
        getChildren().addAll(pnfondoImagen, lbNombreCurso, lbNrc, lbPeriodoEscolar, btnVerDetallesCurso);
    }
    
    public Button getBotonVerDetalles() {
        return btnVerDetallesCurso;
    }
    
    private void establecerEstilos() {
        establecerEstiloTarjeta();
        establecerEstiloContenedorImagen();
        establecerEstilosPane();
        configurarBotonVerDetalles();
    }
    
    private void inicializarElementos() {
        pnfondoImagen = new Pane(imvImagenCurso);
        lbNombreCurso = new Label(curso.getNombreCurso());
        lbNrc = new Label(curso.getNrcCurso());
        String fechaInicio = Utilidades.formatearFechaPeriodo(curso.getInicioPeriodoEscolar());
        String fechaFin 
                = Utilidades.formatearFechaPeriodo(curso.getFinPeriodoEscolar());
        lbPeriodoEscolar = new Label(fechaInicio + " - " + fechaFin);
        btnVerDetallesCurso = new Button("Ver detalles");
    }
    
    public void establecerEstiloTarjeta() {
        this.setPrefSize(335, 379);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15");
    }
    
    public void establecerEstiloContenedorImagen() {
        pnfondoImagen.setPrefSize(100, 100);
        pnfondoImagen.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 100");
        pnfondoImagen.setLayoutX(112);
        pnfondoImagen.setLayoutY(26);
        imvImagenCurso.setFitHeight(52);
        imvImagenCurso.setFitWidth(50);
        imvImagenCurso.setLayoutX(25);
        imvImagenCurso.setLayoutY(24);
    }
    
    public void establecerEstilosPane() {
        lbNombreCurso.setLayoutX(28);
        lbNombreCurso.setLayoutY(147);
        lbNombreCurso.setFont(Font.font("System", FontWeight.BOLD, 23));
        lbNombreCurso.setPrefSize(269, 75);
        lbNombreCurso.setWrapText(true);
        lbNombreCurso.setAlignment(Pos.CENTER);
        lbNombreCurso.setTextAlignment(TextAlignment.CENTER);
        lbNrc.setLayoutX(130);
        lbNrc.setLayoutY(225);
        lbNrc.setFont(new Font(20));
        lbNrc.setAlignment(Pos.CENTER);
        lbNrc.setTextAlignment(TextAlignment.CENTER);
        lbPeriodoEscolar.setLayoutX(82);
        lbPeriodoEscolar.setLayoutY(268);
        lbPeriodoEscolar.setFont(new Font(20));
        lbPeriodoEscolar.setAlignment(Pos.CENTER);
        lbPeriodoEscolar.setTextAlignment(TextAlignment.CENTER);
    }
    
    public void configurarBotonVerDetalles() {
        btnVerDetallesCurso.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnVerDetallesCurso.setLayoutX(92.5);
        btnVerDetallesCurso.setLayoutY(315);
        btnVerDetallesCurso.setPrefWidth(150);
        btnVerDetallesCurso.setFont(new Font(20));
    }
}
