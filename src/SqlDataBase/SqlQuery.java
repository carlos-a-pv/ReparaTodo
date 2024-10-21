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
                String user = null;
                String password = null;
                if (resultSet2.next()) {
                    resultSet2.next();
                    nombre = resultSet2.getString("nombre");
                    direccion = resultSet2.getString("direccion");
                    telefono = resultSet2.getString("telefono");
                    email = resultSet2.getString("email");
                    user = resultSet2.getString("user");
                    password = resultSet2.getString("password");
                }
                Cliente cliente = new Cliente(idCliente,nombre,email,telefono,direccion,user,password);

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

        String queryOrden = "INSERT INTO bd_reparatodo.orden (idOrden, idCliente,idAgente, idProducto,fechaCreacion,estado,descripcionAveria) VALUES (?, ?, ?, ?,?,?,?)";

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

    public static void cambiarEstadoOrden(String idOrden, Estado estadoNuevo) {
        String query = "UPDATE bd_reparatodo.orden SET estado = ? WHERE idOrden = ?";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                // Usar el valor personalizado del enum Estado
                stmt.setString(1, String.valueOf(estadoNuevo));
                stmt.setString(2, idOrden);

                // Ejecutar la actualización
                int filasActualizadas = stmt.executeUpdate();
                if (filasActualizadas > 0) {
                    System.out.println("Estado de la orden actualizado correctamente.");
                } else {
                    System.out.println("No se encontró una orden con el ID especificado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void crearActividadTecnico(String idOrden, String idTecnico, String descripcion) {
        String query = "INSERT INTO bd_reparatodo.actividad_tecnico (idOrden, idTecnico, fechaActividad, descripcion) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, idOrden);
                stmt.setString(2, idTecnico);
                stmt.setDate(3, Date.valueOf(LocalDate.now()));
                stmt.setString(4, descripcion);


                int filasInsertadas = stmt.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Actividad técnica creada con éxito.");
                } else {
                    System.out.println("No se pudo crear la actividad técnica.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void crearOrdenPago(String idOrden, Pago pago) {

        String query = "INSERT INTO bd_reparatodo.actividadtecnico (idOrden,idPago,monto) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, idOrden);
                stmt.setString(2, pago.getIdPago());
                stmt.setFloat(4, pago.getMonto());

                int filasInsertadas = stmt.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Actividad técnica creada con éxito.");
                } else {
                    System.out.println("No se pudo crear la actividad técnica.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void registrarCliente(Cliente cliente) {
        String query = "INSERT INTO bd_reparatodo.cliente (idCliente,nombre, telefono, direccion, user, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, cliente.getIdCliente());
                stmt.setString(2, cliente.getNombre());
                stmt.setString(3, cliente.getTelefono());
                stmt.setString(4, cliente.getDireccion());
                stmt.setString(5, cliente.getUser());
                stmt.setString(6, cliente.getPassword());

                int filasInsertadas = stmt.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Cliente registrado con éxito.");
                } else {
                    System.out.println("No se pudo registrar el cliente.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean actualizarCliente(Cliente datosNuevos,String idCliente) {

        String query = "UPDATE bd_reparatodo.cliente SET nombre = ?, email = ?, telefono = ?, direccion = ? , user =? , password =? WHERE idCliente = ?";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, datosNuevos.getNombre());
                stmt.setString(2, datosNuevos.getEmail());
                stmt.setString(3, datosNuevos.getTelefono());
                stmt.setString(4, datosNuevos.getDireccion());
                stmt.setString(5, datosNuevos.getUser());
                stmt.setString(6, datosNuevos.getPassword());
                stmt.setString(7, idCliente);

                int filasActualizadas = stmt.executeUpdate();
                if (filasActualizadas > 0) {
                    System.out.println("Cliente actualizado con éxito.");
                    return true;
                } else {
                    System.out.println("No se pudo actualizar el cliente. Verifique si el ID es correcto.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean actualizarProducto(Producto producto, String idProducto) {
        String query = "UPDATE bd_reparatodo.producto SET tipo = ?, marca = ?, modelo = ?, descripcion = ? WHERE idProducto = ?";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, producto.getTipo());
                stmt.setString(2, producto.getMarca());
                stmt.setString(3, producto.getModelo());
                stmt.setString(4, producto.getDescripcion());
                stmt.setString(5, idProducto);

                // Ejecutar la actualización y verificar el resultado
                int filasActualizadas = stmt.executeUpdate();
                return filasActualizadas > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
