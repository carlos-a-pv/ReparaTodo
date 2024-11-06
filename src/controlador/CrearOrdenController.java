package controlador;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import modelo.Cliente;
import modelo.Orden;
import modelo.Pago;
import modelo.Producto;
import modelo.enums.Estado;

import java.time.LocalDate;

public class CrearOrdenController {

    @FXML
    private TextField tfId;
    @FXML
    private ComboBox<Producto> cbProducto;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private ComboBox<Cliente> cbClientes;
    @FXML
    private Button btnRegistrar, btnCancelar;

    ModelFactoryController modelFactoryController;

    @FXML
    void initialize(){
        modelFactoryController = ModelFactoryController.getInstance();
        cbClientes.setItems(FXCollections.observableList(modelFactoryController.getClientes()));
        cbProducto.setItems(FXCollections.observableList(modelFactoryController.getProductos()));
        tfId.setDisable(true);
        tfId.setText(String.valueOf((int) (Math.random() * 9000) + 1000));
    }


    public void onClickRegistrar(ActionEvent actionEvent) {
        Orden newOrden = new Orden(
        tfId.getText(),
        modelFactoryController.buscarCliente(cbClientes.getValue().getIdCliente()),
        new Pago("00",0,LocalDate.of(0000,01,01),""),
        modelFactoryController.buscarProducto(cbProducto.getValue().getIdProducto()),
        LocalDate.now(),
        Estado.RECEPCIONADA,
        tfDescripcion.getText()
        );
        boolean resultado = modelFactoryController.crearOrden(newOrden);
        if(resultado){
            mostrarMensaje("Crear orden","Crear orden","Se ha creado la orden correctamente", Alert.AlertType.INFORMATION);
        }
        else{
            mostrarMensaje("Crear orden", "Crear orden", "No se ha podido crear la orden", Alert.AlertType.ERROR);
        }

        limpiarCampos();
        btnRegistrar.getScene().getWindow().hide();


    }

    private void limpiarCampos() {
        tfId.clear();
        cbProducto.getSelectionModel().clearSelection();
        tfDescripcion.clear();
        cbClientes.getSelectionModel().clearSelection();


    }

    public void onClickCancelar(ActionEvent actionEvent) {
        btnCancelar.getScene().getWindow().hide();


    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert aler = new Alert(alertType);
        aler.setTitle(titulo);
        aler.setHeaderText(header);
        aler.setContentText(contenido);
        aler.showAndWait();
    }


}
