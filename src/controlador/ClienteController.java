package controlador;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Actividad;
import modelo.Orden;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ClienteController {
    @FXML
    private TableColumn<Orden,String> colCliente;

    @FXML
    private TableColumn<Orden, String> colDescripcion;

    @FXML
    private TableColumn<Orden, String> colEstado;

    @FXML
    private TableColumn<Orden, LocalDate> colFecha;

    @FXML
    private TableColumn<Orden, String> colId;

    @FXML
    private TableColumn<Orden, Float> colPago;

    @FXML
    private TableColumn<Orden, String> colProducto;
    ///// tabla de actividades
    @FXML
    private Button btnVolver;
    @FXML
    private TableColumn<Actividad, LocalDate> colFechaActividad;

    @FXML
    private TableColumn<Actividad, String> colOrden;

    @FXML
    private TableColumn<Actividad, String> colTecnico;
    @FXML
    private TableColumn<Actividad, String> colDescripcionActividades;

    @FXML
    private TableView<Actividad> tbActividades;
    @FXML
    private TableView<Orden> tbOrdenes;
    ModelFactoryController modelFactoryController;
    Orden ordenSelecionada;
    @FXML
    void initialize() throws SQLException {

        modelFactoryController = ModelFactoryController.getInstance();

        //Tabla de ordenes
        this.colId.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
        this.colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        this.colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        this.colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        this.colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcionAveria"));
        this.colPago.setCellValueFactory(new PropertyValueFactory<>("pago"));
        ordenSelecionada = tbOrdenes.getSelectionModel().getSelectedItem();
        tbOrdenes.setItems(FXCollections.observableList(modelFactoryController.getOrdenesCliente()));

        tbOrdenes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                ordenSelecionada = newValue;
                modelFactoryController.setOrdenSeleccionada(ordenSelecionada);
                tbOrdenes.getSelectionModel().clearSelection();
                //Tabla de actividad
                if (ordenSelecionada != null) {
                    this.colTecnico.setCellValueFactory(new PropertyValueFactory<>("idTecnico"));
                    this.colOrden.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
                    this.colFechaActividad.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                    this.colDescripcionActividades.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
                    try {
                        tbActividades.setItems(FXCollections.observableList(modelFactoryController.getActividades(ordenSelecionada.getIdOrden())));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
    }

    @FXML
    void onClickPagar(ActionEvent event) {

    }
    @FXML
    void onClickVolver(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.initOwner(btnVolver.getScene().getWindow());
        btnVolver.getScene().getWindow().hide();
        stage.show();
    }
}
