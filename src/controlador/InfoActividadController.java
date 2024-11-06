package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import modelo.Actividad;

import java.time.LocalDate;

public class InfoActividadController {
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnCrear;


    @FXML
    private TextArea tfDescripcion;

    @FXML
    private DatePicker tfFecha;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfOrden;
    ModelFactoryController modelFactoryController;

    @FXML
    void initialize(){
        modelFactoryController = ModelFactoryController.getInstance();

    }
    @FXML
    void onClickCancelar(ActionEvent event) {
        btnCancelar.getScene().getWindow().hide();
    }

    @FXML
    void onClickCrear(ActionEvent event) {
        String idTecnico = tfId.getText();
        String idOrden = tfOrden.getText();
        String descripcion = tfDescripcion.getText();
        Actividad actividad = new Actividad(idTecnico,idOrden,LocalDate.now(),descripcion);
        if (validarCampos(idTecnico, idOrden, descripcion)) {
            if (modelFactoryController.crearActividad(actividad)){
                mostrarMensaje("Actividad","Informacion","Se ha registrado la actividad correctamente", Alert.AlertType.INFORMATION);

            }
        }
        else {
            mostrarMensaje("Actividad","Error","No se ha realizado el registro", Alert.AlertType.ERROR);

        }
    }

    private boolean validarCampos(String idActividad, String idOrden, String descripcion) {
        String mensaje = "";
        boolean validado = true;
        if (idActividad.isEmpty()) {
            mensaje+="el id de la actividad es requerido";
            validado = false;
        }        if (idActividad.isEmpty()) {
            mensaje+="el id de la orden es requerido";
            validado = false;
        }       if (idActividad.isEmpty()) {
            mensaje += "la descripcion es requerido";
            validado = false;
        }
        if(!validado){
            mostrarMensaje("Actividad","Error",mensaje, Alert.AlertType.ERROR);
        }
        return validado;
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert aler = new Alert(alertType);
        aler.setTitle(titulo);
        aler.setHeaderText(header);
        aler.setContentText(contenido);
        aler.showAndWait();
    }



}
