package controlador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import modelo.Tecnico;

import java.sql.SQLException;

public class CrearTecnicoController {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnCrear;

    @FXML
    private TextField tfEspecialidad;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfUser;

    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();
    @FXML
    void onClickCancelar(ActionEvent event) {

    }

    @FXML
    void onClickCrear(ActionEvent event) throws SQLException {
        String id= tfId.getText();
        String nombre= tfNombre.getText();
        String especialidad= tfEspecialidad.getText();
        String user = tfUser.getText();
        String password= tfPassword.getText();

        if (validarDatos(id,nombre,especialidad,user,password)){
            Tecnico tecnico = new Tecnico(id,nombre,especialidad,user,password);
            if (modelFactoryController.crearTecnico(tecnico)){
                mostrarMensaje("Tecnico","Información","El tecnico se ha creado exitosamente", Alert.AlertType.INFORMATION);
                btnCrear.getScene().getWindow().hide();
            }
        }
    }

    private boolean validarDatos (String id,String nombre,String especialidad,String user,String password){
        String mensaje = "";
        boolean valido = true;
        if (id.isEmpty()){
            mensaje+= "Es necesario la Cedula";
            valido = false;
        }
        if (nombre.isEmpty()){
            mensaje+= "Es necesario el nombre";
            valido = false;
        }if (especialidad.isEmpty()){
            mensaje+= "Es necesario el especialidad";
            valido = false;
        }if (user.isEmpty()){
            mensaje+= "Es necesario el usuario";
            valido = false;
        }if (password.isEmpty()){
            mensaje+= "Es necesario el contraseña";
            valido = false;
        }
        if (!valido){
            mostrarMensaje("Tecnico","Error",mensaje, Alert.AlertType.ERROR);
        }

        return valido;
    }
    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert aler = new Alert(alertType);
        aler.setTitle(titulo);
        aler.setHeaderText(header);
        aler.setContentText(contenido);
        aler.showAndWait();
    }

}
