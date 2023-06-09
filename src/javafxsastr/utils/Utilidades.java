/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 12/05/2023
 * Descripción: Clase que agrupa funciones comunes que pueden ser usadas
 * desde distintos puntos del programa
 */

package javafxsastr.utils;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utilidades {
    
    public static void mostrarDialogoSimple(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alertaSimple = new Alert(tipo);
        alertaSimple.setTitle(titulo);
        alertaSimple.setContentText(mensaje);
        alertaSimple.setHeaderText(null);
        alertaSimple.showAndWait();
    }
    
    public static boolean mostrarDialogoConfirmacion(String titulo, String mensaje){
        ButtonType buttonTypeYes = new ButtonType("Sí");
        ButtonType buttonTypeNo = new ButtonType("No");
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setContentText(mensaje);
        alertaConfirmacion.setHeaderText(null);
        Optional<ButtonType> botonSeleccionado = alertaConfirmacion.showAndWait(); 
        return botonSeleccionado.isPresent() && botonSeleccionado.get() == buttonTypeYes;
    }
    
    public static Scene inicializarEscena(String ruta) {
        Scene escena = null;
        try {
            Parent vista = FXMLLoader.load(javafxsastr.JavaFXSASTR.class.getResource(ruta));
            escena = new Scene(vista);
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return escena;
    }   
        
}
