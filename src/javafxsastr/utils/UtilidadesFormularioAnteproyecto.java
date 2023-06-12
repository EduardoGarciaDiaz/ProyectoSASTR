/*
 * Autor: Daniel Garcia Arcos
 * Fecha de creación: 13/05/2023
 * Descripción: Funciona como utilidad para el controlador 
 * FormularioAnteproyectoController.
 */

package javafxsastr.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Lgac;

public class UtilidadesFormularioAnteproyecto {
    
    public static Button configurarComponenteLgacSeleccionada(Lgac lgacSeleccionada, VBox vbxContenedorLgac) {
        Label labelLgac = new Label(lgacSeleccionada.getNombreLgac());
        Button btnEliminar = new Button("X");
        btnEliminar.setStyle("-fx-background-color: white; -fx-border-radius: 15;");
        btnEliminar.setLayoutX(300);
        btnEliminar.setLayoutY(10);
        labelLgac.setLayoutX(20);
        labelLgac.setLayoutY(15);
        Pane seleccion = new Pane();
        seleccion.getChildren().addAll(labelLgac, btnEliminar);
        seleccion.setPrefSize(370, 50);
        seleccion.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        seleccion.setId(String.valueOf(lgacSeleccionada.getIdLgac()));
        vbxContenedorLgac.getChildren().add(seleccion);
        return btnEliminar;
    }
    
    public static Button configurarComponenteCodirectorSeleccionado(Academico codirectorSeleccionado, VBox vbxContenedorDirectores) {
        Label labelLgac = new Label(codirectorSeleccionado.toString());
        Button btnEliminar = new Button("X");
        btnEliminar.setStyle("-fx-background-color: white; -fx-border-radius: 15;");
        btnEliminar.setLayoutX(440);
        btnEliminar.setLayoutY(10);
        labelLgac.setLayoutX(20);
        labelLgac.setLayoutY(15);
        Pane seleccion = new Pane();
        seleccion.getChildren().addAll(labelLgac, btnEliminar);
        seleccion.setId(String.valueOf(codirectorSeleccionado.getIdAcademico()));
        seleccion.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        seleccion.setPrefHeight(50);
        vbxContenedorDirectores.getChildren().add(seleccion);
        return btnEliminar;
    }
    
}
