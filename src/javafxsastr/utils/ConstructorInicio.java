/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 21/05/2023
 * Descripción: La clase ConstructorInicio define los métodos particulares
 * para mostrar objetos en la pantalla inicio. Recibe la Interfaz de notificación de inicio,
 * así cuando suceda algo en alguno de los componentes visuales instanciados en esta clase
 * se notificará al controlador del Inicio para que realice la acción correspondiente. 
 * 
 */

package javafxsastr.utils;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafxsastr.interfaces.INotificacionClicBotonInicio;

public class ConstructorInicio {

    private static ConstructorInicio constructor;
    private static INotificacionClicBotonInicio interfazNotificacion;
    public static ConstructorInicio crearPantallaInicio(INotificacionClicBotonInicio interfazNotificacion) {
        constructor = new ConstructorInicio();
        constructor.setInterfazNotificacion(interfazNotificacion);
        return constructor;
    }
    
    private void setInterfazNotificacion(INotificacionClicBotonInicio interfaz){
        interfazNotificacion = interfaz;
    }
    
    public ConstructorInicio cargarBotonIconoCronograma(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/cronograma.png");
        ImageView imvCronograma = new ImageView(img);
        imvCronograma.setFitHeight(38);
        imvCronograma.setFitWidth(38);
        imvCronograma.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvCronograma);
        imvCronograma.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonActividades();
        });
        return constructor;     
    }
    
    public ConstructorInicio cargarBotonIconoAvance(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/avance.png");
        ImageView imvAvance = new ImageView(img);
        imvAvance.setFitHeight(38);
        imvAvance.setFitWidth(38);
        imvAvance.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvAvance);
        imvAvance.setOnMouseClicked((event) -> {
            //TODO
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoCurso(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/curso.png");
        ImageView imvCurso = new ImageView(img);
        imvCurso.setFitHeight(38);
        imvCurso.setFitWidth(38);
        imvCurso.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvCurso);
        imvCurso.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonCursos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoGestionCursos(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/gestion-cursos.png");
        ImageView imvCurso = new ImageView(img);
        imvCurso.setFitHeight(38);
        imvCurso.setFitWidth(38);
        imvCurso.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvCurso);
        imvCurso.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonGestionCursos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoAnteproyecto(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/anteproyecto.png");
        ImageView imvAnteproyecto = new ImageView(img);
        imvAnteproyecto.setFitHeight(38);
        imvAnteproyecto.setFitWidth(38);
        imvAnteproyecto.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvAnteproyecto);
        imvAnteproyecto.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonAnteproyectos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoGestionUsuarios(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/gestion_usuarios.png");
        ImageView imvGestionUsuarios = new ImageView(img);
        imvGestionUsuarios.setFitHeight(38);
        imvGestionUsuarios.setFitWidth(38);
        imvGestionUsuarios.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvGestionUsuarios);
        imvGestionUsuarios.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonUsuarios();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoGestionCA(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/cuerpos-academicos.png");
        ImageView imvGestionCA = new ImageView(img);
        imvGestionCA.setFitHeight(38);
        imvGestionCA.setFitWidth(38);
        imvGestionCA.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvGestionCA);
        imvGestionCA.setOnMouseClicked((event) -> {
           interfazNotificacion.notificarClicBotonGestionCA();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoAnteproyectosRCA(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/aprobar-anteproyectos.png");
        ImageView imvAprobarAnteproyecto = new ImageView(img);
        imvAprobarAnteproyecto.setFitHeight(38);
        imvAprobarAnteproyecto.setFitWidth(38);
        imvAprobarAnteproyecto.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvAprobarAnteproyecto);
        imvAprobarAnteproyecto.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonAprobarAnteproyectos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoEstudiantes(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/estudiante-blanco.png");
        ImageView imvEstudiantes = new ImageView(img);
        imvEstudiantes.setFitHeight(38);
        imvEstudiantes.setFitWidth(38);
        imvEstudiantes.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvEstudiantes);
        imvEstudiantes.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonEstudiantes();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonIconoMiAnteproyecto(VBox vbxMenuContraido) {
        Image img = new Image("file:src/javafxsastr/recursos/iconos/anteproyecto.png");
        ImageView imvMiAnteproyecto = new ImageView(img);
        imvMiAnteproyecto.setFitHeight(38);
        imvMiAnteproyecto.setFitWidth(38);
        imvMiAnteproyecto.setLayoutX(30);
        vbxMenuContraido.getChildren().add(imvMiAnteproyecto);
        imvMiAnteproyecto.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonMiAnteproyecto();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoMiAnteproyecto(VBox vbxMenuDesplegado) {
        Pane btnAnteproyectos = new Pane();
        btnAnteproyectos.setPrefSize(300, 38);
        Label lbAnteproyectos = new Label("Mi anteproyecto");
        lbAnteproyectos.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbAnteproyectos.setLayoutX(75);
        lbAnteproyectos.setLayoutY(7);
        btnAnteproyectos.getChildren().add(lbAnteproyectos);
        vbxMenuDesplegado.getChildren().add(btnAnteproyectos);
        btnAnteproyectos.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonMiAnteproyecto();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoAnteproyectosRCA(VBox vbxMenuDesplegado) {
        Pane botonValidarAnteproyectos = new Pane();
        botonValidarAnteproyectos.setPrefSize(300, 38);
        Label textoValidarAnteproyecto = new Label("Validar anteproyectos");
        textoValidarAnteproyecto.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        textoValidarAnteproyecto.setLayoutX(75);
        textoValidarAnteproyecto.setLayoutY(7);
        botonValidarAnteproyectos.getChildren().add(textoValidarAnteproyecto);
        vbxMenuDesplegado.getChildren().add(botonValidarAnteproyectos);
        botonValidarAnteproyectos.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonAprobarAnteproyectos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoCronograma(VBox vbxMenuDesplegado) {
        Pane botonCronograma = new Pane();
        botonCronograma.setPrefSize(300, 38);
        Label textoCronograma = new Label("Cronograma");
        textoCronograma.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        textoCronograma.setLayoutX(75);
        textoCronograma.setLayoutY(7);
        botonCronograma.getChildren().add(textoCronograma);
        vbxMenuDesplegado.getChildren().add(botonCronograma);
        botonCronograma.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonActividades();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoAvance(VBox vbxMenuDesplegado) {
        Pane btnAvance = new Pane();
        btnAvance.setPrefSize(300, 38);
        Label lbAvance = new Label("Avance");
        lbAvance.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbAvance.setLayoutX(75);
        lbAvance.setLayoutY(7);
        btnAvance.getChildren().add(lbAvance);
        vbxMenuDesplegado.getChildren().add(btnAvance);
        btnAvance.setOnMouseClicked((event) -> {
            //TODO
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoCursos(VBox vbxMenuDesplegado) {
        Pane btnCursos = new Pane();
        btnCursos.setPrefSize(300, 38);
        Label lbCursos = new Label("Mis Cursos");
        lbCursos.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbCursos.setLayoutX(75);
        lbCursos.setLayoutY(7);
        btnCursos.getChildren().add(lbCursos);
        vbxMenuDesplegado.getChildren().add(btnCursos);
        btnCursos.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonCursos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoGestionCursos(VBox vbxMenuDesplegado) {
        Pane btnCursos = new Pane();
        btnCursos.setPrefSize(300, 38);
        Label lbCursos = new Label("Gestión de Cursos");
        lbCursos.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbCursos.setLayoutX(75);
        lbCursos.setLayoutY(7);
        btnCursos.getChildren().add(lbCursos);
        vbxMenuDesplegado.getChildren().add(btnCursos);
        btnCursos.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonGestionCursos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoAnteproyectos(VBox vbxMenuDesplegado) {
        Pane btnAnteproyectos = new Pane();
        btnAnteproyectos.setPrefSize(300, 38);
        Label lbAnteproyectos = new Label("Anteproyectos");
        lbAnteproyectos.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbAnteproyectos.setLayoutX(75);
        lbAnteproyectos.setLayoutY(7);
        btnAnteproyectos.getChildren().add(lbAnteproyectos);
        vbxMenuDesplegado.getChildren().add(btnAnteproyectos);
        btnAnteproyectos.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonAnteproyectos();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoGestionUsuarios(VBox vbxMenuDesplegado) {
        Pane btnGestionUsuarios = new Pane();
        btnGestionUsuarios.setPrefSize(300, 38);
        Label lbGestionUsuarios = new Label("Gestión de usuarios");
        lbGestionUsuarios.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbGestionUsuarios.setLayoutX(75);
        lbGestionUsuarios.setLayoutY(7);
        btnGestionUsuarios.getChildren().add(lbGestionUsuarios);
        vbxMenuDesplegado.getChildren().add(btnGestionUsuarios);
        btnGestionUsuarios.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonUsuarios();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoGestionCA(VBox vbxMenuDesplegado) {
        Pane btnGestionCA = new Pane();
        btnGestionCA.setPrefSize(300, 38);
        Label lbGestionCA = new Label("Gestión de CA");
        lbGestionCA.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbGestionCA.setLayoutX(75);
        lbGestionCA.setLayoutY(7);
        btnGestionCA.getChildren().add(lbGestionCA);
        vbxMenuDesplegado.getChildren().add(btnGestionCA);
        btnGestionCA.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonGestionCA();
        });
        return constructor;
    }
    
    public ConstructorInicio cargarBotonTextoEstudiantes(VBox vbxMenuDesplegado) {
        Pane btnEstudiantes = new Pane();
        btnEstudiantes.setPrefSize(300, 38);
        Label lbEstudiantes = new Label("Estudiantes");
        lbEstudiantes.setStyle("-fx-text-fill: white; -fx-font-size: 24");
        lbEstudiantes.setLayoutX(75);
        lbEstudiantes.setLayoutY(7);
        btnEstudiantes.getChildren().add(lbEstudiantes);
        vbxMenuDesplegado.getChildren().add(btnEstudiantes);
        btnEstudiantes.setOnMouseClicked((event) -> {
            interfazNotificacion.notificarClicBotonEstudiantes();
        });
        return constructor;
    }
    
}
