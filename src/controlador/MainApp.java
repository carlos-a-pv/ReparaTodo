package controlador;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import static controlador.TallerController.INSTACE;

public class MainApp extends Application {

    @FXML
    void initialize() throws SQLException {
        INSTACE.getModel().cargarDatosBD();
    }
    @Override
    public void start(Stage stage) throws Exception {
        initialize();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/vista/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

}
