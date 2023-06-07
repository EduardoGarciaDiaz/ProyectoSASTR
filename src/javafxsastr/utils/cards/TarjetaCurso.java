/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de un curso
 */

package javafxsastr.utils.cards;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.modelos.pojo.Curso;

public class TarjetaCurso extends Pane{
    
    private Curso curso;
    private Pane fondoImagen;
    private ImageView imvIconoCurso;
    private Label lbNombreCurso;
    private Label lbExperienciaEducativa;
    private Label lbPeriodoEscolar;
    private Label lbNombreDocente;
    private Label lbNrc;
    private Label lbSeccion;
    private Label lbBloque;
    private Circle circuloEstado;
    private Button btnVerDetalles;
    private Label lbTituloExperienciaEducativa;
    private Label lbTituloPeriodoEscolar;
    private Label lbTituloDocente;
    private Label lbTituloNrc;
    private Label lbTituloSeccion;
    private Label lbTituloBloque;
    private final DateTimeFormatter FORMATO_FECHA_PERIODO_ESCOLAR = DateTimeFormatter.ofPattern("MMM' 'yyyy",
            new Locale("es"));

    public TarjetaCurso() {
    }
    
    public TarjetaCurso(Curso curso) {
        this.curso = curso;
        inicializarElementos();
        establecerEstilos();
        getChildren().addAll(fondoImagen, lbNombreCurso, lbExperienciaEducativa, lbPeriodoEscolar,
                lbNombreDocente, lbNrc, lbSeccion, lbBloque, circuloEstado, btnVerDetalles,
                lbTituloExperienciaEducativa, lbTituloPeriodoEscolar, lbTituloDocente, lbTituloNrc,
                lbTituloSeccion,lbTituloBloque);
    }
    
    public Button getBotonVerDetalles() {
        return btnVerDetalles;
    }

    private void inicializarElementos() {
        fondoImagen = new Pane();
        imvIconoCurso = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/curso-2.png"));
        lbNombreCurso = new Label(curso.getNombreCurso());
        lbExperienciaEducativa = new Label(curso.getExperienciaEducativaCurso());
        lbPeriodoEscolar = new Label();
        lbNombreDocente = new Label(curso.getAcademicoCurso());
        lbNrc = new Label(curso.getNrcCurso());
        lbSeccion = new Label(curso.getSeccionCurso());
        lbBloque = new Label(curso.getBloqueCurso());
        circuloEstado = new Circle(20);
        btnVerDetalles = new Button("Ver detalles");
        lbTituloExperienciaEducativa = new Label("Experiencia Educativa:");
        lbTituloPeriodoEscolar = new Label("PeriodoEscolar:");
        lbTituloDocente = new Label("Docente:");
        lbTituloNrc = new Label("NRC:");
        lbTituloSeccion = new Label("Sección:");
        lbTituloBloque = new Label("Bloque:");
    }
    
    private void establecerEstilos() {
        establecerEstiloPane();
        establecerEstiloFondoImagen();
        establecerEstiloNobreCurso();
        establecerEstiloExperienciaEducativa();
        establecerEstiloPeriodoEscolar();
        establecerEstiloDocente();
        establecerEstiloNrc();
        establecerEstiloSeccion();
        establecerEstiloBloque();
        establecerEstiloTituloExperienciaEducativa();
        establecerEstiloTituloPeriodoEscolar();
        establecerEstiloTituloDocente();
        establecerEstiloTituloNrc();
        establecerEstiloTituloSeccion();
        establecerEstiloTituloBloque();
        establecerEstiloCirculoEstado();
        establecerEstiloBotonVerDetalles();
    }

    private void establecerEstiloPane() {
        setPrefSize(1568.0, 172.0);
        this.setStyle("-fx-background-color: white;"
                + "-fx-effect:  dropshadow( gaussian, rgba(0, 0, 0, 0.1), 25, 0.0, 0.0, 4);"
                + "-fx-background-radius: 15;");
    }

    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(95, 95);
        fondoImagen.setLayoutX(31);
        fondoImagen.setLayoutY(21);
        fondoImagen.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50");
        fondoImagen.getChildren().add(imvIconoCurso);
        imvIconoCurso.setFitWidth(45);
        imvIconoCurso.setFitHeight(53);
        imvIconoCurso.setLayoutX(25);
        imvIconoCurso.setLayoutY(21);
    }
    
    private void establecerEstiloNobreCurso() {
        lbNombreCurso.setPrefSize(727.0, 30.0);
        lbNombreCurso.setLayoutX(220.0);
        lbNombreCurso.setLayoutY(12.0);
        lbNombreCurso.setTextAlignment(TextAlignment.LEFT);
        lbNombreCurso.setWrapText(true);
        lbNombreCurso.setFont(new Font(20.0));
        lbNombreCurso.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloExperienciaEducativa() {
        lbExperienciaEducativa.setPrefSize(280.0, 30.0);
        lbExperienciaEducativa.setLayoutX(430.0);
        lbExperienciaEducativa.setLayoutY(48.0);
        lbExperienciaEducativa.setTextAlignment(TextAlignment.LEFT);
        lbExperienciaEducativa.setWrapText(true);
        lbExperienciaEducativa.setFont(new Font(20.0));
        lbExperienciaEducativa.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloPeriodoEscolar() {
        lbPeriodoEscolar.setPrefSize(280.0, 30.0);
        lbPeriodoEscolar.setLayoutX(370.0);
        lbPeriodoEscolar.setLayoutY(86.0);
        lbPeriodoEscolar.setTextAlignment(TextAlignment.LEFT);
        lbPeriodoEscolar.setWrapText(true);
        lbPeriodoEscolar.setFont(new Font(20.0));
        lbPeriodoEscolar.setAlignment(Pos.CENTER_LEFT);
        establecerPeriodoEscolar();
    }
    
    private void establecerPeriodoEscolar() {
        LocalDate fechaInicio = LocalDate.parse(curso.getInicioPeriodoEscolar());
        LocalDate fechaFin = LocalDate.parse(curso.getFinPeriodoEscolar());
        String fechaInicioFormateada = fechaInicio.format(FORMATO_FECHA_PERIODO_ESCOLAR);
        String fechaFinFormateada = fechaFin.format(FORMATO_FECHA_PERIODO_ESCOLAR);
        String periodoEscolar = fechaInicioFormateada + " - " + fechaFinFormateada;
        lbPeriodoEscolar.setText(periodoEscolar);
    }
    
    private void establecerEstiloDocente() {
        lbNombreDocente.setPrefSize(700.0, 30.0);
        lbNombreDocente.setLayoutX(309.0);
        lbNombreDocente.setLayoutY(124.0);
        lbNombreDocente.setTextAlignment(TextAlignment.LEFT);
        lbNombreDocente.setWrapText(true);
        lbNombreDocente.setFont(new Font(20.0));
        lbNombreDocente.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloNrc() {
        lbNrc.setPrefSize(200.0, 30.0);
        lbNrc.setLayoutX(867.0);
        lbNrc.setLayoutY(47.0);
        lbNrc.setTextAlignment(TextAlignment.LEFT);
        lbNrc.setWrapText(true);
        lbNrc.setFont(new Font(20.0));
        lbNrc.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloSeccion() {
        lbSeccion.setPrefSize(200.0, 30.0);
        lbSeccion.setLayoutX(898.0);
        lbSeccion.setLayoutY(91.0);
        lbSeccion.setTextAlignment(TextAlignment.LEFT);
        lbSeccion.setWrapText(true);
        lbSeccion.setFont(new Font(20.0));
        lbSeccion.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloBloque() {
        lbBloque.setPrefSize(100.0, 30.0);
        lbBloque.setLayoutX(1187.0);
        lbBloque.setLayoutY(91.0);
        lbBloque.setTextAlignment(TextAlignment.LEFT);
        lbBloque.setWrapText(true);
        lbBloque.setFont(new Font(20.0));
        lbBloque.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloTituloExperienciaEducativa() {
        lbTituloExperienciaEducativa.setPrefSize(210.0, 30.0);
        lbTituloExperienciaEducativa.setLayoutX(220.0);
        lbTituloExperienciaEducativa.setLayoutY(47.0);
        lbTituloExperienciaEducativa.setTextAlignment(TextAlignment.LEFT);
        lbTituloExperienciaEducativa.setWrapText(true);
        lbTituloExperienciaEducativa.setFont(new Font(20.0));
        lbTituloExperienciaEducativa.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloTituloPeriodoEscolar() {
        lbTituloPeriodoEscolar.setPrefSize(152.0, 30.0);
        lbTituloPeriodoEscolar.setLayoutX(220.0);
        lbTituloPeriodoEscolar.setLayoutY(86.0);
        lbTituloPeriodoEscolar.setTextAlignment(TextAlignment.LEFT);
        lbTituloPeriodoEscolar.setWrapText(true);
        lbTituloPeriodoEscolar.setFont(new Font(20.0));
        lbTituloPeriodoEscolar.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloTituloDocente() {
        lbTituloDocente.setPrefSize(152.0, 30.0);
        lbTituloDocente.setLayoutX(220.0);
        lbTituloDocente.setLayoutY(124.0);
        lbTituloDocente.setTextAlignment(TextAlignment.LEFT);
        lbTituloDocente.setWrapText(true);
        lbTituloDocente.setFont(new Font(20.0));
        lbTituloDocente.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloTituloNrc() {
        lbTituloNrc.setPrefSize(58.0, 30.0);
        lbTituloNrc.setLayoutX(816.0);
        lbTituloNrc.setLayoutY(47.0);
        lbTituloNrc.setTextAlignment(TextAlignment.LEFT);
        lbTituloNrc.setWrapText(true);
        lbTituloNrc.setFont(new Font(20.0));
        lbTituloNrc.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloTituloSeccion() {
        lbTituloSeccion.setPrefSize(82.0, 30.0);
        lbTituloSeccion.setLayoutX(816.0);
        lbTituloSeccion.setLayoutY(91.0);
        lbTituloSeccion.setTextAlignment(TextAlignment.LEFT);
        lbTituloSeccion.setWrapText(true);
        lbTituloSeccion.setFont(new Font(20.0));
        lbTituloSeccion.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloTituloBloque() {
        lbTituloBloque.setPrefSize(82.0, 30.0);
        lbTituloBloque.setLayoutX(1109.0);
        lbTituloBloque.setLayoutY(91.0);
        lbTituloBloque.setTextAlignment(TextAlignment.LEFT);
        lbTituloBloque.setWrapText(true);
        lbTituloBloque.setFont(new Font(20.0));
        lbTituloBloque.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloCirculoEstado() {
        circuloEstado.setLayoutX(1422.0);
        circuloEstado.setLayoutY(40.0);
        circuloEstado.setFill(Color.WHITE);
    }
    
    private void establecerEstiloBotonVerDetalles() {
        btnVerDetalles.setPrefSize(174.0, 46.0);
        btnVerDetalles.setLayoutX(1304.0);
        btnVerDetalles.setLayoutY(110.0);
        btnVerDetalles.setFont(new Font(20.0));
        btnVerDetalles.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 13;"
                + "-fx-background-color:  #716E6E;"
                + "-fx-text-fill: white;");
    }
    
}