/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 * Descripción: Esta clase es un componente personalizado
 * creado a partir de Pane. Su fin es servir como contenedor dinamico
 * de informacion de un anteproyecto.
 */

package javafxsastr.utils.cards;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafxsastr.controladores.FXMLAnteproyectosController;
import javafxsastr.interfaces.INotificacionClicBotonAnteproyectos;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.pojo.Anteproyecto;
import javafxsastr.modelos.pojo.Estudiante;

public class TarjetaAnteproyecto extends Pane {
    private boolean esRCA;
    private Label lbNombreTrabajoRecepcional;
    private Label lbNombreDirector;
    private Label lbEstudiantes;
    private Button btnAccion;
    private ImageView imvAnteproyecto = new ImageView(new Image("file:src/javafxsastr/recursos/iconos/anteproyecto-negro.png"));
    private Anteproyecto anteproyecto;
    private Pane fondoImagen;
    private Font fuenteTexto = new Font(20);
    private Font fuenteTitulos = Font.font("System", FontWeight.BOLD, 20);
    private INotificacionClicBotonAnteproyectos interfaz;
    
    public TarjetaAnteproyecto(Anteproyecto anteproyecto, INotificacionClicBotonAnteproyectos interfaz, boolean esRca) {
        this.esRCA = esRca;
        this.anteproyecto = anteproyecto;
        this.interfaz = interfaz;
        inicializarElementos();
        establecerEstilos();
        getChildren().addAll(fondoImagen, lbNombreDirector, lbNombreTrabajoRecepcional, lbEstudiantes, btnAccion);
    }
    
    public void inicializarElementos() {
        lbNombreTrabajoRecepcional = new Label(anteproyecto.getNombreTrabajoRecepcional());
        lbNombreDirector = new Label(anteproyecto.getNombreDirector());
        lbEstudiantes = new Label();
        btnAccion = new Button();
        fondoImagen = new Pane();
    }
    
    public void establecerEstilos() {
        establecerEstiloTarjeta();
        establecerEstiloFondoImagen();
        establecerEstiloLabelsNombreTR();
        establecerEstiloLabelsNombreDirector();
        establecerEstiloLabelsEstudiantes();
        configurarBotonAccion();
    }
    
    private void establecerEstiloTarjeta() {
        this.setPrefSize(1563, 244);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15");
    }
    
    private void establecerEstiloFondoImagen() {
        fondoImagen.setPrefSize(95, 95);
        fondoImagen.setLayoutX(31);
        fondoImagen.setLayoutY(21);
        fondoImagen.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50");
        fondoImagen.getChildren().add(imvAnteproyecto);
        imvAnteproyecto.setFitWidth(45);
        imvAnteproyecto.setFitHeight(53);
        imvAnteproyecto.setLayoutX(25);
        imvAnteproyecto.setLayoutY(21);
    }
    
    private void establecerEstiloLabelsNombreTR() {
        Label tituloNombreTR = new Label("Nombre trabajo recepcional");
        tituloNombreTR.setFont(fuenteTitulos);
        tituloNombreTR.setLayoutX(151);
        tituloNombreTR.setLayoutY(25);
        lbNombreTrabajoRecepcional.setFont(fuenteTexto);
        lbNombreTrabajoRecepcional.setLayoutX(151);
        lbNombreTrabajoRecepcional.setLayoutY(58);
        getChildren().add(tituloNombreTR);
    }
    
    private void establecerEstiloLabelsNombreDirector() {
        Label tituloAcademico = new Label("Academico");
        tituloAcademico.setFont(fuenteTitulos);
        tituloAcademico.setLayoutX(151);
        tituloAcademico.setLayoutY(105);
        lbNombreDirector.setFont(fuenteTexto);
        lbNombreDirector.setLayoutX(151);
        lbNombreDirector.setLayoutY(133);
        getChildren().add(tituloAcademico);
    }
    
    private void establecerEstiloLabelsEstudiantes() {
        Label tituloNumeroEstudiantes = new Label("Estudiantes");
        tituloNumeroEstudiantes.setFont(fuenteTitulos);
        tituloNumeroEstudiantes.setLayoutX(151);
        tituloNumeroEstudiantes.setLayoutY(165);
        getChildren().add(tituloNumeroEstudiantes);
        lbEstudiantes.setFont(fuenteTexto);
        lbEstudiantes.setLayoutX(151);
        lbEstudiantes.setLayoutY(195);
        try {
            ArrayList<Estudiante> estudiantes = new EstudianteDAO().obtenerEstudiantesPorIdAnteproyecto(anteproyecto.getIdAnteproyecto());
            if (estudiantes.size() > 0) {
                String cadenaEstudiantes = "";
                for (Estudiante estudiante : estudiantes) {
                    cadenaEstudiantes += " " + estudiante.toString();
                }
                lbEstudiantes.setText(cadenaEstudiantes);
            } else {
                lbEstudiantes.setText("Ninguno");
            }
        } catch (DAOException ex) {

        }
        
    }
    
    private void configurarBotonAccion() {
        
        btnAccion.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnAccion.setLayoutX(1250);
        btnAccion.setLayoutY(179);
        btnAccion.setPrefWidth(250);
        btnAccion.setPrefHeight(40);
        if ("Borrador".equals(anteproyecto.getEstadoSeguimiento())) {
            btnAccion.setText("Editar anteproyecto");
            btnAccion.setOnAction((event) -> {
                interfaz.notificarClicBotonModificarBorradorAnteproyecto(anteproyecto);
            });
       } else if ("Sin revisar".equals(anteproyecto.getEstadoSeguimiento())) {
           btnAccion.setText("Revisar anteproyecto");
           btnAccion.setOnAction((event) -> {
               interfaz.notificarClicValidarAnteproyecto(anteproyecto);
           });
       } else if ("Rechazado".equals(anteproyecto.getEstadoSeguimiento())) {
           btnAccion.setText("Corregir anteproyecto");
           btnAccion.setOnAction( (event) -> {
               interfaz.notificarClicBotonCorregirAnteproyectoe(anteproyecto);
           });
       } else if ("Validado".equals(anteproyecto.getEstadoSeguimiento())) {
           btnAccion.setText("Ver detalles de anteproyecto");
            if(esRCA) {
                agregarBotonPublicar();
            }
            btnAccion.setOnAction((event) -> { 
                interfaz.notificarClicBotonVerDetallesAnteproyecto(anteproyecto);
            });        
        } else if ("Publicado".equals(anteproyecto.getEstadoSeguimiento())) {
           btnAccion.setText("Ver detalles de anteproyecto");          
           if(esRCA) {
                agregarBotonDespublicar();
           }
           btnAccion.setOnAction((event) -> {
               interfaz.notificarClicBotonVerDetallesAnteproyecto(anteproyecto);
           });
       } else {
            btnAccion.setText("Ver detalles de anteproyecto");
            btnAccion.setOnAction((event) -> {
            interfaz.notificarClicBotonVerDetallesAnteproyecto(anteproyecto);
           });
        }
    }
    
    private void agregarBotonPublicar() {
        Button btnPublicar = new Button("Publicar anteproyecto");
        btnPublicar.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnPublicar.setLayoutX(1250);
        btnPublicar.setLayoutY(100);
        btnPublicar.setPrefWidth(180);
        btnPublicar.setOnAction((event) -> {
            interfaz.notificarClicPublicarAnteproyecto(anteproyecto);
        });
        this.getChildren().add(btnPublicar);
    }
    
    private void agregarBotonDespublicar() {
        Button btnPublicar = new Button("Despublicar anteproyecto");
        btnPublicar.setStyle("-fx-background-color: #C4DAEF; -fx-background-radius: 15;");
        btnPublicar.setLayoutX(1250);
        btnPublicar.setLayoutY(100);
        btnPublicar.setPrefWidth(180);
        btnPublicar.setOnAction((event) -> {
            interfaz.notificarClicDespublicarAnteproyecto(anteproyecto);
        });
        this.getChildren().add(btnPublicar);
    }
}
