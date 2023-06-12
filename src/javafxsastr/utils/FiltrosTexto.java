/*
 * Autor: Tristan Eduardo Suarez Santiago
 * Fecha de creación: 8/06/2023
 * Descripción: Clase que agurpa distintos patrones pára validar entradas de texto
 */
package javafxsastr.utils;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

public class FiltrosTexto {

    public FiltrosTexto() {
    }
    
    private static final Pattern PATRON_CORREO_VALIDO = Pattern.compile("^[A-Za-z0-9]+@(?:[A-Za-z0-9]+\\.)?uv\\.mx$");
    private static final Pattern PATRON_NUMERO_PERSONAL_VALIDO = Pattern.compile("^[0-9]+$");
    private static final Pattern PATRON_MATRICULA_VALIDA = Pattern.compile("^[sS]\\d{8}$");
    private static final Pattern PATRON_CONTRASENA_VALIDA = Pattern.compile("^(?=.*[A-Z])(?=.*[\\W_]).{7,16}$");
    private static final Pattern PATRON_SOLO_LETRA_NUMEROS = Pattern.compile("^[\\p{L}0-9\\s]*$");
    private static final Pattern PATRON_SOLO_LETRA_NUMEROS_PUNTOS = Pattern.compile("^[\\p{L}0-9.\\s]*$");
    private static final Pattern PATRON_LET_NUM_SIG_ORT = Pattern.compile("^[\\p{L}\\p{M}0-9\\s.,!?'-]+$");
    private static final Pattern PATRON_HORA_MINUTOS = Pattern.compile("^([01]?\\d|2[0-3]):[0-5]\\d$");
    private static final Pattern PATRON_CONTIENE_LETRAS = Pattern.compile("[a-zA-Z]+");
    
    public static  void filtroLetrasNumeros(TextInputControl campoTexto) {              
            UnaryOperator<TextFormatter.Change> filtroTextoIngresado = 
                change -> {
                    String textoIngresado = change.getControlNewText();
                    if (PATRON_SOLO_LETRA_NUMEROS.matcher(textoIngresado).matches() || textoIngresado.equals("")) {
                        return change;
                    } else {
                        return null;
                    }
                };
            TextFormatter<String> textFormatter = new TextFormatter<>(filtroTextoIngresado);
            campoTexto.setTextFormatter(textFormatter);
    };
    
    public static  void filtroHoraMinutos(TextInputControl campoTexto) {              
            UnaryOperator<TextFormatter.Change> filtroTextoIngresado =
                change -> {
                    String textoIngresado = change.getControlNewText();
                    if (PATRON_HORA_MINUTOS.matcher(textoIngresado).matches() || textoIngresado.equals("")) {
                        return change;
                    } else {
                        return null;
                    }
                };
            TextFormatter<String> textFormatter = new TextFormatter<>(filtroTextoIngresado);
            campoTexto.setTextFormatter(textFormatter);
    };
    
     public static  void filtroLetrasNumerosPuntos(TextInputControl campoTexto) {              
            UnaryOperator<TextFormatter.Change> filtroTextoIngresado =
                change -> {
                    String textoIngresado = change.getControlNewText();
                    if (PATRON_SOLO_LETRA_NUMEROS_PUNTOS.matcher(textoIngresado).matches() || textoIngresado.equals("")) {
                        return change;
                    } else {
                        return null;
                    }
            };
            TextFormatter<String> textFormatter = new TextFormatter<>(filtroTextoIngresado);
            campoTexto.setTextFormatter(textFormatter);
    };
    
    public static  void filtroLetrasNumerosPuntosComasSignosComunes(TextInputControl campoTexto) {              
            UnaryOperator<TextFormatter.Change> filtroTextoIngresado = 
                change -> {
                    String textoIngresado = change.getControlNewText();
                    if (PATRON_LET_NUM_SIG_ORT.matcher(textoIngresado).matches() || textoIngresado.equals("")) {
                        return change;
                    } else {
                        return null;
                    }
                };
            TextFormatter<String> textFormatter = new TextFormatter<>(filtroTextoIngresado);
            campoTexto.setTextFormatter(textFormatter);
    }; 
     
    public static boolean correoValido(String correo) {
        if(correo != null && !correo.isEmpty()) {
            Matcher matchPatron = PATRON_CORREO_VALIDO.matcher(correo); 
            return matchPatron.find();
        }else{
            return false;
        }
    }
    
    public static boolean noPersonalValido(String noPersonal) {
        if(noPersonal != null && !noPersonal.isEmpty()) {
            Matcher matchPatron = PATRON_NUMERO_PERSONAL_VALIDO.matcher(noPersonal); 
            return matchPatron.find();
        }else{
            return false;
        }
    }
    
    public static boolean matriculaValida(String matricula) {
        if(matricula != null && !matricula.isEmpty() ) {
            Matcher matchPatron = PATRON_MATRICULA_VALIDA.matcher(matricula); 
            return matchPatron.find();
        }else{
            return false;
        }
    }
    
    public static boolean contrasenaValida(String contrasena) {
        if(contrasena != null && !contrasena.isEmpty()) {
            Matcher matchPatron = PATRON_CONTRASENA_VALIDA.matcher(contrasena); 
            return matchPatron.find();
        }else{
            return false;
        }
    }
    
    public static boolean contieneAlMenosUnaLetra(String cadena) {
        if ((cadena != null) && (!cadena.isEmpty())) {
            Matcher matchPatron = PATRON_CONTIENE_LETRAS.matcher(cadena);
            return matchPatron.find();
        } else {
            return false;
        }
    }
    
}

 
