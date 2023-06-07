/*
 * Autor: Daniel García Arcos
 * Fecha de creación: 05/06/2023
 *
 * Descripción: Clase genérica que permite la creación de un campo de búsqueda
 * a partir de componentes definidos de forma externa. Recibe un TextField para
 * funcionar como barra de búsqueda, una ListView en donde se muestran los resultados, 
 * la lista en donde se va a buscar y un objeto al cual asignarle el objeto seleccionado de 
 * la listview. Recibe una interfaz que notificará a las clases que la implementen
 * sobre la selección de un elemento o la perdida del coco. 
 * IMPORTANTE: Para mostrar los elementos de forma ordenada se hace uso del método toString 
 * del objeto E, por lo que es necesario que este lo tenga definido. 
 */

package javafxsastr.utils;

import java.util.Comparator;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafxsastr.interfaces.INotificacionSeleccionItem;

public class CampoDeBusqueda <E> {
    
    private TextField campoBusqueda;
    private ListView<E> listaElementos;
    private ObservableList<E> coleccionElementos;
    private E elementoSeleccionado;
    private INotificacionSeleccionItem<E> interfazNotificacion;

    public CampoDeBusqueda(TextField campoBusqueda, ListView<E> listaElementos, ObservableList<E> coleccionElementos,
            E seleccion, INotificacionSeleccionItem<E> interfaz) {
        this.campoBusqueda = campoBusqueda;
        this.listaElementos = listaElementos;
        this.coleccionElementos = coleccionElementos;
        this.elementoSeleccionado = seleccion;
        this.interfazNotificacion = interfaz;
        configurarBusquedaCuerpoAcademico();
        iniciarListenerListView();
        iniciarListenersCampoTexto();
        agregarListenerFocuseProperty();
    }
    
    private void configurarBusquedaCuerpoAcademico() {
        if (coleccionElementos.size() > 0) {
            FilteredList<E> elementosFiltrados = new FilteredList(coleccionElementos, p -> true );
            campoBusqueda.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null) {                             
                        listaElementos.setVisible(true);   
                    }
                    elementosFiltrados.setPredicate(new Predicate<E>() {
                        @Override
                        public boolean test(E elem) {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            String lowerNewValue = newValue.toLowerCase();
                            return elem.toString().toLowerCase().contains(lowerNewValue);
                        }
                    });
                    if (elementosFiltrados.isEmpty() || newValue.isEmpty()) {
                        listaElementos.setVisible(false);               
                    } else {
                        SortedList<E> sortedListAlumnos = new SortedList<>(elementosFiltrados, 
                            Comparator.comparing(E::toString));
                        listaElementos.setItems(sortedListAlumnos);
                    }
                }
            });
        }
    }
    
    private void iniciarListenerListView() {
        listaElementos.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                setElementoSeleccionado();
            }
        });
        listaElementos.setOnMouseClicked((event) -> {
            setElementoSeleccionado();
        });
    }
    
    private void iniciarListenersCampoTexto() {
        campoBusqueda.setOnKeyPressed((event) -> {
            if(event.getCode() == KeyCode.DOWN) {
                listaElementos.requestFocus();
                listaElementos.getSelectionModel().selectFirst();
            } else if (event.getCode() == KeyCode.ENTER) {
                listaElementos.getSelectionModel().selectFirst();
                E elementoSeleccionado = listaElementos.getSelectionModel().getSelectedItem();
                if (campoBusqueda.getText().toLowerCase().contains(
                        elementoSeleccionado.toString().toLowerCase())) {
                    campoBusqueda.setText(elementoSeleccionado.toString());
                    setElementoSeleccionado();
                } else {
                    listaElementos.requestFocus();
                }
            }
        });
    }
    
    private void setElementoSeleccionado() {
        E itemSeleccionado = listaElementos.getSelectionModel().getSelectedItem();
            if (itemSeleccionado != null) {
                this.elementoSeleccionado = itemSeleccionado;
                campoBusqueda.setText(itemSeleccionado.toString());
                listaElementos.setVisible(false);
                interfazNotificacion.notificarSeleccionItem(itemSeleccionado);
        }
    }
    
    private void agregarListenerFocuseProperty() {
        campoBusqueda.focusedProperty().addListener((
                ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (oldValue && !(campoBusqueda.getScene().getFocusOwner() instanceof ListView)) {
                listaElementos.setVisible(false);
                interfazNotificacion.notificarPerdidaDelFoco();
            } else {
                campoBusqueda.setStyle("-fx-border-color: gray");
            }
        });
        listaElementos.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue && !campoBusqueda.isFocused()) {
                    listaElementos.setVisible(false);
                    interfazNotificacion.notificarPerdidaDelFoco();
                } else {
                    campoBusqueda.setStyle("-fx-border-color: gray");
                }
            }
        });
    }
}
