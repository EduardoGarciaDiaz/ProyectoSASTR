/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 12/05/2023
 * Descripción: Clase que agrupa funciones comunes que pueden ser usadas
 * desde distintos puntos del programa
 */
package javafxsastr.utils;

 

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class FiltrosTexto {

    public FiltrosTexto() {
    }
    
    private static final Pattern PATRON_SOLO_LETRA_NUMEROS = Pattern.compile("^[\\p{L}0-9\\s]+$");
     private static final Pattern PATRON_SOLO_LETRA_NUMEROS_PUNTOS = Pattern.compile("^[\\p{L}0-9.\\s]+$");
    
    public static  void filtroLetrasNumeros(TextField campoTexto) {              
            UnaryOperator<TextFormatter.Change> filtroTetxtoIngresado = change -> {
                String textoIngresado = change.getControlNewText();
                if (PATRON_SOLO_LETRA_NUMEROS.matcher(textoIngresado).matches()) {
                    return change;
                } else {
                    return null;
                }
            };
            TextFormatter<String> textFormatter = new TextFormatter<>(filtroTetxtoIngresado);
            campoTexto.setTextFormatter(textFormatter);
    };
    
     public static  void filtroLetrasNumerosPuntos(TextArea campoTexto) {              
            UnaryOperator<TextFormatter.Change> filtroTetxtoIngresado = change -> {
                String textoIngresado = change.getControlNewText();
                if (PATRON_SOLO_LETRA_NUMEROS_PUNTOS.matcher(textoIngresado).matches()) {
                    return change;
                } else {
                    return null;
                }
            };
            TextFormatter<String> textFormatter = new TextFormatter<>(filtroTetxtoIngresado);
            campoTexto.setTextFormatter(textFormatter);
    };
    
    
    
}

 
