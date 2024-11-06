package controlador;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Cliente;
import modelo.Orden;
import modelo.Pago;
import modelo.Tecnico;
import modelo.enums.Estado;

import java.io.IOException;
import java.time.LocalDate;

public class HomeController {

    @FXML
    private TableView<Orden> tbOrdenes;
    @FXML
    private TableColumn<Orden, String> colId;
    @FXML
    private TableColumn<Orden, String> colProducto;
    @FXML
    private TableColumn<Orden, String> colCliente;
    @FXML
    private TableColumn<Orden, LocalDate> colFecha;
    @FXML
    private TableColumn<Orden, Estado> colEstado;
    @FXML
    private TableColumn<Orden, String> colDescripcion;
    @FXML
    private TableColumn<Orden, Pago> colPago;
    @FXML
    private TableColumn<Tecnico,String> colIdTecnico;
    @FXML
    private TableColumn<Tecnico,String> colNombreTecnico;
    @FXML
    private TableColumn<Tecnico,String> colEspecialidad;
    @FXML
    private TableView<Tecnico> tbTecnico;
    @FXML
    private TableView<Cliente> tbClientes;
    @FXML
    private TableColumn<Cliente, String> colCedula;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colDireccion;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    @FXML
    private TableColumn<Cliente, String> colEmail;

    @FXML
    private Button btnCrearOrden;
    @FXML
    private Button btnCrearCliente;
    @FXML
    private Button btnCrearTecnico;


    ModelFactoryController modelFactoryController;
    Orden ordeSelecionada;
    Cliente clienteSeleccionado;
    Tecnico tecnicoSelecionado;

    @FXML
    void initialize(){
        modelFactoryController = ModelFactoryController.getInstance();

        //Tabla de ordenes
        this.colId.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
        this.colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        this.colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        this.colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        this.colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcionAveria"));
        this.colPago.setCellValueFactory(new PropertyValueFactory<>("pago"));


        ordeSelecionada = tbOrdenes.getSelectionModel().getSelectedItem();

        tbOrdenes.setItems(FXCollections.observableList(modelFactoryController.getOrdenes()));

        tbOrdenes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                ordeSelecionada = newValue;
                try {
                    modelFactoryController.setOrdenSeleccionada(ordeSelecionada);
                    mostrarInfoOrden();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                tbOrdenes.getSelectionModel().clearSelection();
            }

        });

        //tabla Clientes
        this.colCedula.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        this.colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        clienteSeleccionado = tbClientes.getSelectionModel().getSelectedItem();

        tbClientes.setItems(FXCollections.observableList(modelFactoryController.getClientes()));

        tbClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                clienteSeleccionado = newValue;
//                try {
                    modelFactoryController.setOrdenSeleccionada(ordeSelecionada);
//                    mostrarInfoOrden();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                tbOrdenes.getSelectionModel().clearSelection();
            }

        });
        //tabla tecnicos
        this.colIdTecnico.setCellValueFactory(new PropertyValueFactory<>("idTecnico"));
        this.colNombreTecnico.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        tecnicoSelecionado = tbTecnico.getSelectionModel().getSelectedItem();

        tbTecnico.setItems(FXCollections.observableList(modelFactoryController.getTecnicos()));

//        tbClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//                    if (newValue != null) {
//                        tecnicoSelecionado = newValue;
////                try {
//                        modelFactoryController.(tecnicoSelecionado);
////                    mostrarInfoOrden();
////                } catch (IOException e) {
////                    throw new RuntimeException(e);
////                }
//                        tbOrdenes.getSelectionModel().clearSelection();
//                    }
//                }

        btnCrearOrden.setCursor(Cursor.HAND);
        btnCrearCliente.setCursor(Cursor.HAND);
        btnCrearTecnico.setCursor(Cursor.HAND);

        btnCrearOrden.setOnMouseEntered((event) -> {
            btnCrearOrden.setScaleY(1.1);
            btnCrearOrden.setScaleX(1.1);
        });
        btnCrearOrden.setOnMouseExited((event) -> {
            btnCrearOrden.setScaleY(1.0);
            btnCrearOrden.setScaleX(1.0);
        });
        btnCrearCliente.setOnMouseEntered((event) -> {
            btnCrearCliente.setScaleY(1.1);
            btnCrearCliente.setScaleX(1.1);
        });
        btnCrearCliente.setOnMouseExited((event) -> {
            btnCrearCliente.setScaleY(1.0);
            btnCrearCliente.setScaleX(1.0);
        });

        btnCrearTecnico.setOnMouseEntered((event) -> {
            btnCrearTecnico.setScaleX(1.1);
            btnCrearTecnico.setScaleY(1.1);
        });
        btnCrearTecnico.setOnMouseExited((event) -> {
            btnCrearTecnico.setScaleY(1.0);
            btnCrearTecnico.setScaleX(1.0);
        });
        }

    private void mostrarInfoOrden() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/info-orden.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        Stage stage = new Stage();
        stage.setTitle("Info orden");
        stage.setScene(scene);
        stage.initOwner(btnCrearOrden.getScene().getWindow());
        stage.show();
        stage.setOnHiding(event -> {
            tbOrdenes.getItems().clear(); // limpia los datos actuales
            tbOrdenes.setItems(FXCollections.observableList(modelFactoryController.getOrdenes()));
            tbOrdenes.refresh();
        });

    }


    public void onClickCrearOrden(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/crear-orden.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 300);
        Stage stage = new Stage();
        stage.setTitle("Crear orden");
        stage.setScene(scene);
        stage.initOwner(btnCrearOrden.getScene().getWindow());
        stage.show();
        stage.setOnHiding(event -> {
            tbOrdenes.refresh();
        });

    }

    public void onClickBuscar(ActionEvent actionEvent) {
    }

    public void onClickCrearCliente(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/crear-cliente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 300);
        Stage stage = new Stage();
        stage.setTitle("Crear Cliente");
        stage.setScene(scene);
        stage.initOwner(btnCrearCliente.getScene().getWindow());
        stage.show();
        stage.setOnHiding(event -> {
            initialize();
        });
    }

    private void cargarTablaOrdenes() {
        tbOrdenes.getItems().clear();
        tbOrdenes.setItems(FXCollections.observableList(modelFactoryController.getOrdenes()));
        tbOrdenes.refresh();
    }

    public void onClickCrearTecnico(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/crear-tecnico.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Crear Tecnico");
        stage.setScene(scene);
        stage.initOwner(btnCrearTecnico.getScene().getWindow());
        stage.show();
        stage.setOnHiding(event -> initialize());
    }
}
