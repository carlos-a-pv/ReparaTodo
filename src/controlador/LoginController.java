package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.*;

import java.io.IOException;
import java.sql.SQLException;

import static controlador.TallerController.INSTACE;

public class LoginController {

    @FXML
    private Button btnLogin;

    @FXML
    private ImageView imgIcon;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUser;

    ModelFactoryController modelFactoryController;

    @FXML
    void initialize () throws SQLException {

        modelFactoryController = ModelFactoryController.getInstance();

    }

    @FXML
    void onClickLogin(ActionEvent event) throws IOException {
        String user = tfUser.getText();
        String password = tfPassword.getText();

        Usuario  usuario = modelFactoryController.autenticar(user,password);
        if (usuario!=null){
            if (usuario instanceof Agente){
            INSTACE.getModel().usuarioLogeado = usuario;
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.initOwner(btnLogin.getScene().getWindow());
            btnLogin.getScene().getWindow().hide();
            stage.show();
            }
            if (usuario instanceof Cliente){
                INSTACE.getModel().usuarioLogeado = usuario;
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/cliente-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
                Stage stage = new Stage();
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.initOwner(btnLogin.getScene().getWindow());
                btnLogin.getScene().getWindow().hide();
                stage.show();
            }
            if (usuario instanceof Tecnico){
                INSTACE.getModel().usuarioLogeado = usuario;
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/tecnico-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
                Stage stage = new Stage();
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.initOwner(btnLogin.getScene().getWindow());
                btnLogin.getScene().getWindow().hide();
                stage.show();
            }

        }

        else {
            limpiarCampos();
            mostrarMensaje("Login","Error","Credenciales incorrectas", Alert.AlertType.ERROR);
        }
    }
    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert aler = new Alert(alertType);
        aler.setTitle(titulo);
        aler.setHeaderText(header);
        aler.setContentText(contenido);
        aler.showAndWait();
    }
    public void limpiarCampos(){
        tfUser.setText("");
        tfPassword.setText("");
    }
}
