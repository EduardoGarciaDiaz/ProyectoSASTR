/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 17/05/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion del cuerpo académico.
 */
package javafxsastr.utils.cards;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafxsastr.modelos.pojo.CuerpoAcademico;

public class TarjetaCuerpoAcademico extends Pane {
    
    private CuerpoAcademico cuerpoAcademico;
    private Label lbNombreCuerpoAcademico;
    private Label lbNombreDisciplina;
    private Label lbNombreResponsableCA;
    private Pane fondoImagen;
    private ImageView imvCuerpoAcademico = 
            new ImageView(new Image("file:src/javafxsastr/recursos/iconos/cuerpo-academico-negro.png"));
    private Button btnModificarCuerpoAcademico;
    private Font fuente = new Font(20);
    
    public TarjetaCuerpoAcademico(CuerpoAcademico cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
        inicializarElementos();
        establecerEstiloTarjeta();
        establecerEstiloLabels();
        establecerEstiloFondoImagen();
        establecerEstiloBotonVerDetalles();
        this.getChildren().addAll(lbNombreCuerpoAcademico, lbNombreDisciplina, lbNombreResponsableCA, fondoImagen,
                btnModificarCuerpoAcademico);
        
    }
    
    public Button getBotonModificarCuerpoAcademico() {
        return btnModificarCuerpoAcademico;
    }
    
    private void inicializarElementos() {
        lbNombreCuerpoAcademico = new Label("Nombre: " + cuerpoAcademico.getNombreCuerpoAcademico());
        lbNombreDisciplina = new Label("Disciplina: " + cuerpoAcademico.getDisciplinaCuerpoAcademico());
        lbNombreResponsableCA = new Label("Responsable: "+cuerpoAcademico.getNombreResponsableCA());
        fondoImagen = new Pane();
        btnModificarCuerpoAcademico = new Button("Ver Detalles CA");
    }
    
    private void establecerEstiloTarjeta() {
        this.setPrefSize(1555, 150);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(95, 95);
        fondoImagen.setLayoutX(31);
        fondoImagen.setLayoutY(21);
        fondoImagen.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50");
        fondoImagen.getChildren().add(imvCuerpoAcademico);
        imvCuerpoAcademico.setFitWidth(53);
        imvCuerpoAcademico.setFitHeight(53);
        imvCuerpoAcademico.setLayoutX(21);
        imvCuerpoAcademico.setLayoutY(21);
    }
    
    private void establecerEstiloLabels() {
        lbNombreCuerpoAcademico.setFont(fuente);
        lbNombreCuerpoAcademico.setLayoutX(151);
        lbNombreCuerpoAcademico.setLayoutY(20);
        lbNombreDisciplina.setFont(fuente);
        lbNombreDisciplina.setLayoutX(151);
        lbNombreDisciplina.setLayoutY(50);
        lbNombreResponsableCA.setFont(fuente);
        lbNombreResponsableCA.setLayoutX(151);
        lbNombreResponsableCA.setLayoutY(85);
    }
    
    private void establecerEstiloBotonVerDetalles() {
        btnModificarCuerpoAcademico.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnModificarCuerpoAcademico.setLayoutX(1300);
        btnModificarCuerpoAcademico.setLayoutY(90);
        btnModificarCuerpoAcademico.setPrefWidth(230);
        btnModificarCuerpoAcademico.setPrefHeight(40);
    }
    
}

