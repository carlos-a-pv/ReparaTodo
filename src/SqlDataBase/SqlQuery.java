package SqlDataBase;

import controlador.ModelFactoryController;
import modelo.*;
import modelo.enums.Estado;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static controlador.TallerController.INSTACE;

public class SqlQuery {

    ///////////////////CARGAR DATOS DESDE LA BASE DE DATOS///////////////////////

    public static void cargarOrdenes() throws SQLException {

        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement1 = null;
        ResultSet resultSet1 = null;
        PreparedStatement statement2 = null;
        ResultSet resultSet2 = null;
        PreparedStatement statement3 = null;
        ResultSet resultSet3 = null;
        PreparedStatement statement4 = null;
        ResultSet resultSet4 = null;
        PreparedStatement statement5 = null;
        ResultSet resultSet5 = null;

        try {
            // Primera consulta para obtener ordenes
            String sql1 = "SELECT * FROM bd_reparatodo.orden";
            statement1 = conn.prepareStatement(sql1);
            resultSet1 = statement1.executeQuery();

            // Procesar el ResultSet de la primera consulta
            while (resultSet1.next()) {
                String idOrden = resultSet1.getString("idOrden");
                String idCliente = resultSet1.getString("idCliente");
                String idAgente = resultSet1.getString("idAgente");
                String idProducto = resultSet1.getString("idProducto");
                String descripcionAveria = resultSet1.getString("descripcionAveria");
                LocalDate fechaCreacion = resultSet1.getDate("fechaCreacion").toLocalDate();
                Estado estado =  Estado.valueOf(resultSet1.getString("estado").toUpperCase());

                // Segunda consulta para obtener el cliente
                String sql2 = "SELECT * FROM bd_reparatodo.cliente " +
                        "WHERE idCliente = ?";
                statement2 = conn.prepareStatement(sql2);
                statement2.setString(1, resultSet1.getString("idCliente")); // Usar el idUsuario del usuario actual
                resultSet2 = statement2.executeQuery();

                String nombre = null;
                String direccion = null;
                String telefono = null;
                String email = null;
                if (resultSet2.next()) {
                    resultSet2.next();
                    nombre = resultSet2.getString("nombre");
                    direccion = resultSet2.getString("direccion");
                    telefono = resultSet2.getString("telefono");
                    email = resultSet2.getString("email");
                }
                Cliente cliente = new Cliente(idCliente,nombre,email,telefono,direccion);

                // Tercera consulta para obtener el producto
                String sql3 = "SELECT * FROM bd_reparatodo.producto " +
                        "WHERE idProducto = ?";
                statement3 = conn.prepareStatement(sql3);
                statement3.setString(1, resultSet1.getString("idProducto")); // Usar el idUsuario del usuario actual
                resultSet3 = statement3.executeQuery();

                String tipo = null;
                String marca = null;
                String modelo = null;
                String descripcion = null;

                if (resultSet3.next()) {
                    resultSet3.next();
                    tipo = resultSet3.getString("tipo");
                    marca = resultSet3.getString("marca");
                    modelo = resultSet3.getString("modelo");
                    descripcion = resultSet3.getString("descripcion");
                }
                Producto producto = new Producto(idProducto,tipo,marca,modelo,descripcion);

                // Cuarta consulta para obtener el Pago
                String sql4 = "SELECT * FROM bd_reparatodo.ordenpago o " +
                        "join bd_reparatodo.pago p ON o.idPago = p.idPago"+
                        "WHERE o.idOrden = ?";
                statement4 = conn.prepareStatement(sql4);
                statement4.setString(1, resultSet1.getString("idOrden")); // Usar el idUsuario del usuario actual
                resultSet4 = statement4.executeQuery();

                Float monto = null;
                LocalDate fechaPago = null;
                String metodoPago = null;
                String idPago = null;

                if (resultSet4.next()) {
                    idPago = resultSet4.getString("idPago");
                    monto = resultSet4.getFloat("monto");
                    fechaPago = resultSet4.getDate("tipo").toLocalDate();
                    metodoPago = resultSet4.getString("modelo");
                }
                Pago pago = new Pago(idPago,monto,fechaPago,metodoPago);


                // Crear el objeto Orden y agregarlo al modelo
                Orden orden = new Orden(idOrden,cliente,pago,producto,fechaCreacion,estado,descripcionAveria);
                ModelFactoryController.getInstance().crearOrden(orden);

                // Cerrar el ResultSet de las consultas anidadas
                resultSet2.close();
                statement2.close();
                resultSet3.close();
                statement3.close();
                resultSet4.close();
                statement4.close();
                resultSet5.close();
                statement5.close();
            }

            System.out.println("Consulta ordenes ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public static void crearOrden(Orden orden) {

        String queryOrden = "INSERT INTO orden (idOrden, idCliente,idAgente, idProducto,fechaCreacion,estado,descripcionAveria) VALUES (?, ?, ?, ?,?,?,?)";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmtOrden = conn.prepareStatement(queryOrden)) {
                stmtOrden.setString(1, orden.getIdOrden());
                stmtOrden.setString(2, orden.getCliente().getIdCliente());
                stmtOrden.setString(3, INSTACE.getModel().agenteLogeado.getIdAgente());
                stmtOrden.setString(4, orden.getProducto().getIdProducto());
                stmtOrden.setDate(5, Date.valueOf(orden.getFechaCreacion()));
                stmtOrden.setString(6, String.valueOf(orden.getEstado()));
                stmtOrden.setString(7,orden.getDescripcionAveria());
                stmtOrden.executeUpdate();
            }
            System.out.println("Orden guardada con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
