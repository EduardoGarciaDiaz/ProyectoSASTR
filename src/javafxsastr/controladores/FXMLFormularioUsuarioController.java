/*
 * Autor: Eduardo García Díaz
 * Fecha de creación: 03/06/2023
 * Descripción: Controlador del formulario para registro y edición de usuarios
 */

package javafxsastr.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafxsastr.JavaFXSASTR;
import javafxsastr.interfaces.INotificacionRecargarDatos;
import javafxsastr.modelos.dao.AcademicoDAO;
import javafxsastr.modelos.dao.DAOException;
import javafxsastr.modelos.dao.EstudianteDAO;
import javafxsastr.modelos.dao.UsuarioDAO;
import javafxsastr.modelos.pojo.Academico;
import javafxsastr.modelos.pojo.Estudiante;
import javafxsastr.modelos.pojo.Usuario;
import javafxsastr.utils.FiltrosTexto;
import javafxsastr.utils.Utilidades;


public class FXMLFormularioUsuarioController implements Initializable {

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfPrimerApellido;
    @FXML
    private TextField tfSegundoApellido;
    @FXML
    private TextField tfContrasena;
    @FXML
    private TextField tfCorreoInstitucional;
    @FXML
    private TextField tfIdentificadorTipoUsuario;
    @FXML
    private ComboBox<String> cbTipoUsuario;
    @FXML
    private CheckBox chbxEsAdministrador;
    @FXML
    private Label lbIdentificadorTipoUsuario;
    @FXML
    private Label lbPrimerApellidoVacio;
    @FXML
    private Label lbSegundoApellidoVacio;
    @FXML
    private Label lbCorreoVacio;
    @FXML
    private Label lbContrasenaVacia;
    @FXML
    private Label lbTipoUsuarioVacio;
    @FXML
    private Label lbNombreVacio;   
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lbIdentificadorTipoUsuarioVacio;
    @FXML
    private Label lbTipoUsuario;
    
    private Usuario usuario;
    private ObservableList<String> tiposUsuarios;
    private Usuario usuarioEdicion;
    private boolean esEdicion;
    private boolean vieneDeDetallesCurso = false;
    private boolean vieneDeCuerpoAcademico = false;
    private Academico academicoEdicion;
    private Estudiante estudianteEdicion;
    private INotificacionRecargarDatos interfaz;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTiposUsuarios();
        lbUsuario.requestFocus();
        agregarListenersCamposVacios();
        btnGuardar.setDisable(true);
        cbTipoUsuario.valueProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null){
                    String tipoUsuarioSeleccionado = cbTipoUsuario.getSelectionModel().getSelectedItem();
                    mostrarCampoIdentificadorTipoUsuario(tipoUsuarioSeleccionado);
                }
            }
        });
    }    
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarTiposUsuarios();
        lbUsuario.requestFocus();
        agregarListenersCamposVacios();
        btnGuardar.setDisable(true);
        cbTipoUsuario.valueProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null){
                    String tipoUsuarioSeleccionado = cbTipoUsuario.getSelectionModel().getSelectedItem();
                    mostrarCampoIdentificadorTipoUsuario(tipoUsuarioSeleccionado);
                }
            }
        });
    }
    
    public void mostrarCampoIdentificadorTipoUsuario(String tipoUsuario) {
        if ("Estudiante".equals(tipoUsuario)) {
            tfIdentificadorTipoUsuario.setVisible(true);
            lbIdentificadorTipoUsuario.setText("Matricula");
        }
        if ("Académico".equals(tipoUsuario)) {
            tfIdentificadorTipoUsuario.setVisible(true);
            lbIdentificadorTipoUsuario.setText("Número de personal");
        }
    }
    
    public void cargarTiposUsuarios() {
        tiposUsuarios = FXCollections.observableArrayList();
        tiposUsuarios.add("Estudiante");
        tiposUsuarios.add("Académico");
        cbTipoUsuario.setItems(tiposUsuarios);
    }
    
    public void inicializarInformacionFormulario(boolean esEdicion, Usuario usuarioEdicion) {
        this.esEdicion = esEdicion;
        this.usuarioEdicion = usuarioEdicion;
        if (esEdicion) {
            lbUsuario.setText("Modificar usuario");
            btnGuardar.setText("Guardar cambios");
            cbTipoUsuario.setVisible(false);
            lbTipoUsuario.setVisible(false);
            cargarInformacionEdicion();
        }
    }
    
    public void vieneDeVentanaDetallesCurso(boolean esVentanaDetallesCurso, INotificacionRecargarDatos interfaN) {
        this.vieneDeDetallesCurso = esVentanaDetallesCurso;
        interfaz =  interfaN;
    }
    
    public void vieneDeVentanaCuerposAcademicos(boolean esVentanaCA, INotificacionRecargarDatos interfaN) {
        this.vieneDeCuerpoAcademico= esVentanaCA;
        interfaz =  interfaN;
    }
    
    private void cargarInformacionEdicion(){
        String tipoUsuario = "";
        tfNombre.setText(usuarioEdicion.getNombre());
        tfPrimerApellido.setText(usuarioEdicion.getPrimerApellido());
        tfSegundoApellido.setText(usuarioEdicion.getSegundoApellido());
        tfCorreoInstitucional.setText(usuarioEdicion.getCorreoInstitucional());
        tfContrasena.setText(usuarioEdicion.getContraseña());
        try { 
            estudianteEdicion = new EstudianteDAO().obtenerEstudiantePorIdUsuario(usuarioEdicion.getIdUsuario());
            if (estudianteEdicion.getIdUsuario() > 0) {
                tipoUsuario = "Estudiante";
                tfIdentificadorTipoUsuario.setText(estudianteEdicion.getMatriculaEstudiante());
            } else {
                academicoEdicion = new AcademicoDAO().obtenerAcademicoPorIdUsuario(usuarioEdicion.getIdUsuario());
                if (academicoEdicion.getIdUsuario() > 0) {
                    tipoUsuario = "Académico";
                    tfIdentificadorTipoUsuario.setText(String.valueOf(academicoEdicion.getNumeroPersonal()));
                }
            }
            cbTipoUsuario.getSelectionModel().select(tipoUsuario);
            mostrarCampoIdentificadorTipoUsuario(tipoUsuario);
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        chbxEsAdministrador.setSelected(usuarioEdicion.getEsAdministrador());
    }
    
    public void agregarListenersCamposVacios() {
        agregarListenerCampoVacio(tfNombre, lbNombreVacio);
        agregarListenerCampoVacio(tfPrimerApellido, lbPrimerApellidoVacio);
        agregarListenerCampoVacio(tfCorreoInstitucional, lbCorreoVacio);
        agregarListenerCampoVacio(tfContrasena, lbContrasenaVacia);
        agregarListenerCampoVacio(tfIdentificadorTipoUsuario, lbIdentificadorTipoUsuarioVacio);
        agregarListenerComboBoxVacio(cbTipoUsuario, lbTipoUsuarioVacio);
    }
    
    private void agregarListenerComboBoxVacio(ComboBox combo, Label etiqueta){
        combo.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                combo.setStyle("-fx-border-color: gray");
                etiqueta.setText("");
                tfIdentificadorTipoUsuario.setText("");
            }
            if (oldValue) {
                if (combo.getSelectionModel().getSelectedIndex()==-1) {
                    combo.setStyle("-fx-border-color: red");
                    etiqueta.setText("Campo obligatorio");
                }
                if (validarCamposObligatoriosLLenos()) {
                    btnGuardar.setDisable(false);
                } else {
                    btnGuardar.setDisable(true);
                    llenarMatriculaAutomatico();
                }
            }
        });
    }
    
    private void agregarListenerCampoVacio(TextInputControl campoTexto, Label etiqueta){
        campoTexto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.trim().isEmpty()) {
                    campoTexto.setStyle("-fx-border-color: gray");
                    etiqueta.setText("");
                    if (validarCamposObligatoriosLLenos()) {
                        btnGuardar.setDisable(false);
                    } else {
                        btnGuardar.setDisable(true);
                    }
                } else {
                    etiqueta.setText("Campo obligatorio");
                    campoTexto.setStyle("-fx-border-color: red");
                    btnGuardar.setDisable(true);
                }
            }
        });
    }
    
    private boolean validarCamposObligatoriosLLenos() {
        boolean respuesta = false;
        if ((!tfNombre.getText().trim().isEmpty())
                & (!tfPrimerApellido.getText().trim().isEmpty())
                & (!tfCorreoInstitucional.getText().trim().isEmpty())
                & (!tfContrasena.getText().trim().isEmpty())
                & (!tfIdentificadorTipoUsuario.getText().trim().isEmpty())
                & (cbTipoUsuario.getSelectionModel().getSelectedIndex() >= 0)){
            respuesta = true;
        }
        return respuesta;
    }
    
    private void llenarMatriculaAutomatico() {
        if ("Estudiante".equals(cbTipoUsuario.getSelectionModel().getSelectedItem())) {
            String correoInstitucional = tfCorreoInstitucional.getText().trim();
            if (!correoInstitucional.isEmpty()) {
                String matricula = correoInstitucional.substring(1, Math.min(correoInstitucional.length(), 10));
                tfIdentificadorTipoUsuario.setText(matricula);
            }
        }
    }
    
    public void validarCampos() {
        boolean datosValidos = true;
        String nombre = tfNombre.getText().trim();
        String primerApellido = tfPrimerApellido.getText().trim();
        String segundoApellido = tfSegundoApellido.getText().trim();
        String correo = tfCorreoInstitucional.getText().trim();
        String contrasena = tfContrasena.getText().trim();
        boolean esAdministrador = chbxEsAdministrador.isSelected();
        int idEstadoUsuario = 1;
        String tipoUsuarioSeleccionado = cbTipoUsuario.getSelectionModel().getSelectedItem();
        String identificadorTipoUsuario = tfIdentificadorTipoUsuario.getText().trim();
        Usuario usuarioAValidarLongitud = new Usuario(nombre, primerApellido, segundoApellido, correo,
                contrasena, esAdministrador, idEstadoUsuario);
        if (!validarLongitudCampos(usuarioAValidarLongitud)) {
            datosValidos = false;
        }
        if (!FiltrosTexto.correoValido(correo)) {
            datosValidos = false;
            lbCorreoVacio.setText("Correo no válido. Ejemplos: zs21013811@estudiantes.uv.mx o profesor@uv.mx");
        }
        if (!FiltrosTexto.contrasenaValida(contrasena)) {
            datosValidos = false;
            lbContrasenaVacia.setText("Contraseña no sválida. Debe tener de 7 a 16 caracteres," +
                    " debe contener al menos un simbolo y una mayúscula");
        }
        if ("Estudiante".equals(tipoUsuarioSeleccionado)) {
            if(!FiltrosTexto.matriculaValida(identificadorTipoUsuario)) {
                datosValidos = false;
                lbIdentificadorTipoUsuarioVacio.setText("Matricula no válida. Ejemplo S21013811");
            }
            if(!esEdicion) {
                if(validarUsuarioInexistente(correo, identificadorTipoUsuario, tipoUsuarioSeleccionado)) {
                    datosValidos = false;
                    Utilidades.mostrarDialogoSimple("Usuario existente", 
                            "El correo institucional y/o la matrícula ya está registrado en el sistema",
                            Alert.AlertType.WARNING);
                }
            }
        }
        if ("Académico".equals(tipoUsuarioSeleccionado)) {
            if (!FiltrosTexto.noPersonalValido(identificadorTipoUsuario)) {
                datosValidos = false;
                lbIdentificadorTipoUsuarioVacio.setText("Solo debe contener números");
            }
            if (!esEdicion) {
                if(validarUsuarioInexistente(correo, identificadorTipoUsuario, tipoUsuarioSeleccionado)) {
                    datosValidos = false;
                    Utilidades.mostrarDialogoSimple("Usuario existente", 
                            "El correo institucional y/o el número de personal ya está registrado en el sistema",
                            Alert.AlertType.WARNING);
                }
            }
        }
        if (esEdicion) {
            if (!validarExistenciaOtroAdministrador()) {
                datosValidos = false;
                Utilidades.mostrarDialogoSimple("Operación no posible", 
                            "No puedes dejar al sistema sin un administrador. "
                            + "Asegúrate de que exista otro administrador en el sistema.", Alert.AlertType.WARNING);
            }
        }

        if (datosValidos) {
            int respuesta;
            if ("Estudiante".equals(tipoUsuarioSeleccionado)) {
                Estudiante usuarioNuevo = new Estudiante();
                usuarioNuevo.setNombre(nombre);
                usuarioNuevo.setPrimerApellido(primerApellido);
                usuarioNuevo.setSegundoApellido(segundoApellido);
                usuarioNuevo.setCorreoInstitucional(correo);
                usuarioNuevo.setContraseña(contrasena);
                usuarioNuevo.setEsAdministrador(esAdministrador);
                usuarioNuevo.setIdEstadoUsuario(idEstadoUsuario);
                usuarioNuevo.setMatriculaEstudiante(identificadorTipoUsuario);
                if (!esEdicion) {
                    respuesta = guardarUsuario(usuarioNuevo);
                    if (respuesta > 0) {
                        usuarioNuevo.setIdUsuario(respuesta);
                    }
                } else {
                    usuarioNuevo.setIdUsuario(usuarioEdicion.getIdUsuario());
                    respuesta = guardarUsuario(usuarioNuevo);
                }
                if (esEdicion) {
                    usuarioNuevo.setIdEstudiante(estudianteEdicion.getIdEstudiante());
                }
                guardarEstudiante(usuarioNuevo);
            } else if("Académico".equals(tipoUsuarioSeleccionado)) {
                Academico usuarioNuevo = new Academico();
                usuarioNuevo.setNombre(nombre);
                usuarioNuevo.setPrimerApellido(primerApellido);
                usuarioNuevo.setSegundoApellido(segundoApellido);
                usuarioNuevo.setCorreoInstitucional(correo);
                usuarioNuevo.setContraseña(contrasena);
                usuarioNuevo.setEsAdministrador(esAdministrador);
                usuarioNuevo.setIdEstadoUsuario(idEstadoUsuario);
                usuarioNuevo.setNumeroPersonal(Integer.parseInt(identificadorTipoUsuario));
                if (!esEdicion) {
                    respuesta = guardarUsuario(usuarioNuevo);
                    if (respuesta > 0) {
                        usuarioNuevo.setIdUsuario(respuesta);
                    }
                } else {
                    usuarioNuevo.setIdUsuario(usuarioEdicion.getIdUsuario());
                    respuesta = guardarUsuario(usuarioNuevo);
                }
                if (esEdicion) {
                    usuarioNuevo.setIdAcademico(academicoEdicion.getIdAcademico());
                }
                guardarAcademico(usuarioNuevo);
            }
        } 
    }
    
    public boolean validarLongitudCampos(Usuario usuarioNuevo) {
        boolean esLongitudValida = true;
        String nombre = usuarioNuevo.getNombre();
        String primerApellido = usuarioNuevo.getPrimerApellido();
        String segundoApellido = usuarioNuevo.getSegundoApellido();
        String correo = usuarioNuevo.getCorreoInstitucional();
        if (nombre.length() > 50) {
            esLongitudValida = false;
            lbNombreVacio.setText("No puede tener más de 50 caracteres");
        }
        if (primerApellido.length() > 50) {
            esLongitudValida = false;
            lbPrimerApellidoVacio.setText("No puede tener más de 50 caracteres");
        }
        if (segundoApellido.length() > 50) {
            esLongitudValida = false;
            lbSegundoApellidoVacio.setText("No puede tener más de 50 caracteres");
        }
        if (correo.length() > 50) {
            esLongitudValida = false;
            lbCorreoVacio.setText("No puede tener más de 50 caracteres");
        }
        return esLongitudValida;
    }
    
    public int guardarUsuario(Usuario usuarioNuevo) {
        int respuestaUsuario = 0;
        try {
            if (esEdicion) {
                respuestaUsuario = new UsuarioDAO().actualizarUsuario(usuarioNuevo);
            } else {
                respuestaUsuario = new UsuarioDAO().guardarUsuario(usuarioNuevo);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return respuestaUsuario;
    }
    
    private boolean validarUsuarioInexistente(String correoInstitucional, String identificadorTipoUsuario, String tipoUsuario) {
        boolean esExistente = false;
        try {
            if ("Estudiante".equals(tipoUsuario)) {
                esExistente = new UsuarioDAO().verificarUsuarioEstudianteInexistente(correoInstitucional, identificadorTipoUsuario);
            } else if ("Académico".equals(tipoUsuario)) {
                esExistente = new UsuarioDAO().verificarUsuarioAcademicoInexistente(correoInstitucional, identificadorTipoUsuario);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return esExistente;
    }
    
    private boolean validarExistenciaOtroAdministrador() {
        boolean existeOtroAdministrador = false;
        int contadorAdministradores = 0;
        try {
            ArrayList<Usuario> usuarios = new UsuarioDAO().obtenerUsuarios();
            for (Usuario user : usuarios) {
                if (user.getEsAdministrador() && user.getIdEstadoUsuario() == 1) {
                    contadorAdministradores++;
                }
                if (contadorAdministradores > 1) {
                    existeOtroAdministrador = true;
                    return existeOtroAdministrador;
                }
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        return existeOtroAdministrador;
    }
    
    public void guardarEstudiante(Estudiante estudianteNuevo) {
        int respuestaEstudiante = 0;
        try {
            if (esEdicion) {
                respuestaEstudiante = new EstudianteDAO().actualizarEstudiante(estudianteNuevo);
            } else {
                respuestaEstudiante = new EstudianteDAO().guardarEstudiante(estudianteNuevo);

            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        if (respuestaEstudiante > 0) {
            if (esEdicion) {
                Utilidades.mostrarDialogoSimple("Modificación exitosa", "Usuario modificado con éxto",
                    Alert.AlertType.INFORMATION);
                if(vieneDeDetallesCurso) {
                    cerrarVentana();
                }else {
                    irAVistaVerUsuario(estudianteNuevo, usuario);
                }                
            } else {
                Utilidades.mostrarDialogoSimple("Estudiante registrado con éxito", "Usuario registrado correctamente en el sistema",
                    Alert.AlertType.INFORMATION);                
                cerrarVentana();
            }

        }
    }
    
    public void guardarAcademico(Academico academicoNuevo) {
        int respuestaAcademico = 0;
        try {
            if (esEdicion) {
                respuestaAcademico = new AcademicoDAO().actualizarAcademico(academicoNuevo);
            } else {
                respuestaAcademico = new AcademicoDAO().guardarAcademico(academicoNuevo);
            }
        } catch (DAOException ex) {
            manejarDAOException(ex);
        }
        if (respuestaAcademico > 0) {
            if (esEdicion) {
                Utilidades.mostrarDialogoSimple("Modificación exitosa", "Usuario modificado con éxito",
                    Alert.AlertType.INFORMATION);
                irAVistaVerUsuario(academicoNuevo, usuario);

            } else {
                Utilidades.mostrarDialogoSimple("Académico registrado con éxito", "Usuario registrado correctamente en el sistema",
                    Alert.AlertType.INFORMATION);
                cerrarVentana();
            }
        }
    }
    
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cancelarRegistro;
        if (esEdicion) {
            cancelarRegistro = Utilidades.mostrarDialogoConfirmacion("Cancelar modificación de usuario",
                "¿Estás seguro de que deseas cancelar la modificación del usuario?");
            if (cancelarRegistro) {
                cerrarVentana();
            }
        } else {
            cancelarRegistro = Utilidades.mostrarDialogoConfirmacion("Cancelar registro de usuario",
                "¿Estás seguro de que deseas cancelar el registro del usuario?");
            if (cancelarRegistro) {
                cerrarVentana();
             }
        }        
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
       cerrarVentana();
    }
    
    private void cerrarVentana() {
        if(vieneDeDetallesCurso){
            irAVistaVerDetallesCursos();
        }else {
             if(vieneDeCuerpoAcademico) {
            irAVistaCuerpoAcademico();
            }else {
                if (esEdicion) {
                irAVistaVerUsuario(usuarioEdicion, usuario);
                }else {
                irAVistaUsuarios(usuario);
                }   
            }
        }       
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        validarCampos();
    }
    
    private void irAVistaUsuarios(Usuario usuario) { //Cambiar nombre, no es a inicio
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLUsuarios.fxml"));
            Parent vista = accesoControlador.load();
            FXMLUsuariosController controladorVistaUsuarios = accesoControlador.getController();     
            controladorVistaUsuarios.setUsuario(usuario);
            Stage escenario = (Stage) lbContrasenaVacia.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Usuarios");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaVerUsuario(Usuario usuarioVisualizacion, Usuario usuario) {
        try {
            FXMLLoader accesoControlador = new FXMLLoader(JavaFXSASTR.class.getResource("vistas/FXMLVerUsuario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLVerUsuarioController controladorVistaVerUsuario = accesoControlador.getController();     
            controladorVistaVerUsuario.setUsuario(usuarioVisualizacion, usuario);
            Stage escenario = (Stage) lbContrasenaVacia.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Usuarios");
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irAVistaVerDetallesCursos() {
       interfaz.notitficacionRecargarDatos();
       Stage escenario = (Stage) lbContrasenaVacia.getScene().getWindow();
       escenario.close();
    }
    
    private void irAVistaCuerpoAcademico() {
       interfaz.notitficacionRecargarDatosPorEdicion(true);
       Stage escenario = (Stage) lbContrasenaVacia.getScene().getWindow();
       escenario.close();
    }
    
    private void manejarDAOException(DAOException ex) {
        switch (ex.getCodigo()) {
            case ERROR_CONSULTA:
                ex.printStackTrace();
                System.out.println("Ocurrió un error de consulta.");
                break;
            case ERROR_CONEXION_BD:
                ex.printStackTrace();
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "No se pudo conectar a la base de datos. Inténtelo de nuevo o hágalo más tarde.", Alert.AlertType.ERROR);
            default:
                throw new AssertionError();
        }
    }
    
}