package controlador;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Orden;
import modelo.enums.Estado;

public class InfoOrdenController {
    @FXML
    private TextField tfId, getTfProducto;
    @FXML
    private TextField tfProducto;
    @FXML
    private DatePicker tfFecha;
    @FXML
    private ComboBox<Estado> cbEstado;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private Label lbPago;
    @FXML
    private ImageView imgProducto;
    @FXML
    private Button btnCancelar, btnEditar;

    ModelFactoryController modelFactoryController;
    Orden ordenSelecionada;

    @FXML
    void initialize() {
        modelFactoryController = ModelFactoryController.getInstance();
        ordenSelecionada = modelFactoryController.getOrdenSeleccionada();

        cbEstado.setItems(FXCollections.observableArrayList(Estado.values()));
        imgProducto.setImage(new Image("resources/producto.png"));

        tfId.setText(ordenSelecionada.getIdOrden());
        tfId.setDisable(true);
        tfProducto.setText(ordenSelecionada.getProducto().modelo);
        tfFecha.setValue(ordenSelecionada.getFechaCreacion());
        cbEstado.setValue(ordenSelecionada.getEstado());
        tfDescripcion.setText(ordenSelecionada.getDescripcionAveria());
        if(ordenSelecionada.getPago() != null) {
            lbPago.setText(String.valueOf(ordenSelecionada.getPago()));
        }else{
            lbPago.setText("No se ha registrado el pago");
        }
   }

    public void onClickCancelar(ActionEvent actionEvent) {
        btnCancelar.getScene().getWindow().hide();
    }

    public void onClickEditar(ActionEvent actionEvent) {

        Estado newEstado = cbEstado.getValue();
        modelFactoryController.cambiarEstadoOrden(ordenSelecionada.getIdOrden(), newEstado);

        mostrarMensaje("Editar orden", "Acción existosa","Se ha cambiado el estado de la orden correctamente", Alert.AlertType.INFORMATION);
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert aler = new Alert(alertType);
        aler.setTitle(titulo);
        aler.setHeaderText(header);
        aler.setContentText(contenido);
        aler.showAndWait();
    }

}
