/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 05/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de los estudiantes que fueron desasignados de un anteproyecto
 */

package javafxsastr.utils.cards;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxsastr.modelos.pojo.Desasignacion;


public class TarjetaDesasignacion extends Pane {
    private int idEstudiante;
    private Label lbNombreEstudiante;
    private Button btnVerJustificacion;
    private Desasignacion desasignacion;
        
    public TarjetaDesasignacion() {
    }

    public TarjetaDesasignacion(Desasignacion desasignacion) {
        this.desasignacion = desasignacion;
        inicializarElementos();
        establecerEstilos();
        this.idEstudiante = desasignacion.getIdEstudiante();
        this.lbNombreEstudiante.setText(desasignacion.getNombreEstudiante());
        getChildren().addAll(lbNombreEstudiante, btnVerJustificacion);
    }
    
    public Button getBtnVerJustficacion() {
        return btnVerJustificacion;
    }
    
    public Desasignacion getDesasignacion() {
        return desasignacion;
    }
    
    private void inicializarElementos() {
        lbNombreEstudiante = new Label();
        btnVerJustificacion = new Button("Ver justificación");
    }
    
    private void establecerEstilos() {
        establecerEstiloPane();
        establecerEstiloNombreEstudiante();
        establecerEstiloBotonVerAvance();
    }
    
    private void establecerEstiloPane() {
        setPrefSize(507.0, 38.0);
        this.setStyle("-fx-background-color: white;"
                + "-fx-effect:  dropshadow( gaussian, rgba(0, 0, 0, 0.1), 25, 0.0, 0.0, 4);"
                + "-fx-background-radius: 15;");
    }
    
    private void establecerEstiloNombreEstudiante() {
        lbNombreEstudiante.setPrefSize(360.0, 27.0);
        lbNombreEstudiante.setLayoutX(6.0);
        lbNombreEstudiante.setLayoutY(7);
        lbNombreEstudiante.setTextAlignment(TextAlignment.LEFT);
        lbNombreEstudiante.setWrapText(true);
        lbNombreEstudiante.setFont(new Font(18.0));
        lbNombreEstudiante.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void establecerEstiloBotonVerAvance() {
        btnVerJustificacion.setPrefSize(116.0, 25.0);
        btnVerJustificacion.setLayoutX(369.0);
        btnVerJustificacion.setLayoutY(4.0);
        btnVerJustificacion.setFont(new Font(16.0));
        btnVerJustificacion.setStyle("-fx-border-color: transparent;"
                + "-fx-background-radius: 15;"
                + "-fx-background-color: #c9c9c9");       
    }
    
}
