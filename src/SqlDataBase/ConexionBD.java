package SqlDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instancia;
    private String url = "jdbc:mysql://localhost:3306/bd_proyectofinalbbdd";
    private String usuario = "root";
    private String contraseña = "0223";

    private ConexionBD() {
        // Constructor privado para evitar instanciación
    }

    public static ConexionBD getInstance() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, contraseña);
    }
}