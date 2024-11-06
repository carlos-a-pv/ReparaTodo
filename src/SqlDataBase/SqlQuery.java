package SqlDataBase;

import controlador.ModelFactoryController;
import modelo.*;
import modelo.enums.Estado;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static controlador.TallerController.INSTACE;

public class SqlQuery {

    ///////////////////CARGAR DATOS DESDE LA BASE DE DATOS///////////////////////

    public static void cargarOrdenes() throws SQLException {

        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement1 = null;
        ResultSet resultSet1 = null;

        try {
            // Primera consulta para obtener ordenes
            String sql1 = "SELECT * FROM bd_reparatodo.orden ORDER BY fechaCreacion DESC ";
            statement1 = conn.prepareStatement(sql1);
            resultSet1 = statement1.executeQuery();

            // Procesar el ResultSet de la primera consulta
            while (resultSet1.next()) {
                String idOrden = resultSet1.getString("idOrden");
                String idCliente = resultSet1.getString("idCliente");
                String idProducto = resultSet1.getString("idProducto");
                String descripcionAveria = resultSet1.getString("descripcionAveria");
                LocalDate fechaCreacion = resultSet1.getDate("fechaCreacion").toLocalDate();
                Estado estado = Estado.valueOf(resultSet1.getString("estado").toUpperCase());

                Cliente cliente = null;
                try {
                    // Segunda consulta para obtener el cliente
                    String sql2 = "SELECT * FROM bd_reparatodo.cliente WHERE idCliente = ?";
                    PreparedStatement statement2 = conn.prepareStatement(sql2);
                    statement2.setString(1, idCliente);
                    ResultSet resultSet2 = statement2.executeQuery();

                    if (resultSet2.next()) {
                        String nombre = resultSet2.getString("nombre");
                        String direccion = resultSet2.getString("direccion");
                        String telefono = resultSet2.getString("telefono");
                        String email = resultSet2.getString("email");
                        String user = resultSet2.getString("user");
                        String password = resultSet2.getString("password");
                        cliente = new Cliente(idCliente, nombre, email, telefono, direccion, user, password);
                    }
                    resultSet2.close();
                    statement2.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Cliente para la orden " + idOrden + ": " + e.getMessage());
                }

                Producto producto = null;
                try {
                    // Tercera consulta para obtener el producto
                    String sql3 = "SELECT * FROM bd_reparatodo.producto WHERE idProducto = ?";
                    PreparedStatement statement3 = conn.prepareStatement(sql3);
                    statement3.setString(1, idProducto);
                    ResultSet resultSet3 = statement3.executeQuery();

                    if (resultSet3.next()) {
                        String tipo = resultSet3.getString("tipo");
                        String marca = resultSet3.getString("marca");
                        String modelo = resultSet3.getString("modelo");
                        String descripcion = resultSet3.getString("descripcion");
                        producto = new Producto(idProducto, tipo, marca, modelo, descripcion);
                    }
                    resultSet3.close();
                    statement3.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Producto para la orden " + idOrden + ": " + e.getMessage());
                }

                Pago pago = null;
                try {
                    // Cuarta consulta para obtener el Pago
                    String sql4 = "SELECT p.* FROM bd_reparatodo.pago p " +
                            "JOIN bd_reparatodo.ordenpago o ON p.idPago = o.idPago " +
                            "WHERE o.idOrden = ?";
                    PreparedStatement statement4 = conn.prepareStatement(sql4);
                    statement4.setString(1, idOrden);
                    ResultSet resultSet4 = statement4.executeQuery();

                    if (resultSet4.next()) {
                        String idPago = resultSet4.getString("idPago");
                        LocalDate fechaPago = resultSet4.getDate("fechaPago").toLocalDate();
                        String metodoPago = resultSet4.getString("metodoPago");
                        Float monto = resultSet4.getFloat("montoTotal");
                        pago = new Pago(idPago, monto, fechaPago, metodoPago);
                    }
                    resultSet4.close();
                    statement4.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Pago para la orden " + idOrden + ": " + e.getMessage());
                }

                // Crear el objeto Orden y agregarlo al modelo
                Orden orden = new Orden(idOrden, cliente, pago, producto, fechaCreacion, estado, descripcionAveria);
                INSTACE.getModel().getColaRepaciones().encolar(orden);
            }

            System.out.println("Consulta de órdenes ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta de órdenes: " + e.getMessage());
        } finally {
            if (resultSet1 != null) resultSet1.close();
            if (statement1 != null) statement1.close();
        }
    }


    public static void crearOrden(Orden orden) {

        String queryOrden = "INSERT INTO bd_reparatodo.orden (idOrden, idCliente, idProducto,fechaCreacion,estado,descripcionAveria) VALUES (?, ?, ?,?,?,?)";

        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmtOrden = conn.prepareStatement(queryOrden)) {
                stmtOrden.setString(1, orden.getIdOrden());
                stmtOrden.setString(2, orden.getCliente().getIdCliente());
                stmtOrden.setString(3, orden.getProducto().getIdProducto());
                stmtOrden.setDate(4, Date.valueOf(orden.getFechaCreacion()));
                stmtOrden.setString(5, String.valueOf(orden.getEstado()));
                stmtOrden.setString(6, orden.getDescripcionAveria());
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
        String query = "INSERT INTO bd_reparatodo.actividadtecnico ( idOrden, idTecnico, fecha, descripcion,idActividad) VALUES (?,?, ?, ?, ?)";
        String idActividad = String.valueOf((int) (Math.random() * 9000) + 1000);
        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, idOrden);
                stmt.setString(2, idTecnico);
                stmt.setDate(3, Date.valueOf(LocalDate.now()));
                stmt.setString(4, descripcion);
                stmt.setString(5,idActividad);


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

    public static boolean actualizarCliente(Cliente datosNuevos, String idCliente) {

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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void cargarTecnicos() throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Consulta para obtener los técnicos
            String sql = "SELECT * FROM bd_reparatodo.tecnico";
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            // Procesar el ResultSet y crear los objetos Tecnico
            while (resultSet.next()) {
                String idTecnico = resultSet.getString("idTecnico");
                String nombre = resultSet.getString("nombre");
                String especialidad = resultSet.getString("especialidad");
                String user = resultSet.getString("user");
                String password = resultSet.getString("password");

                // Crear el objeto Tecnico
                Tecnico tecnico = new Tecnico(idTecnico, nombre, especialidad, user, password);

                // Agregar el objeto técnico al modelo o a una lista
                INSTACE.getModel().usuarios.add(tecnico);
            }

            System.out.println("Consulta técnicos ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    public static void cargarClientes() throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Consulta para obtener los clientes
            String sql = "SELECT * FROM bd_reparatodo.cliente";
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            // Procesar el ResultSet y crear los objetos Cliente
            while (resultSet.next()) {
                String idCliente = resultSet.getString("idCliente");
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                String user = resultSet.getString("user");
                String password = resultSet.getString("password");

                // Crear el objeto Cliente
                Cliente cliente = new Cliente(idCliente, nombre, direccion, telefono, email, user, password);

                // Agregar el cliente al modelo o lista
                INSTACE.getModel().usuarios.add(cliente);
            }

            System.out.println("Consulta clientes ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    public static void cargarAgentes() throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Consulta para obtener los agentes
            String sql = "SELECT * FROM bd_reparatodo.agente";
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();


            // Procesar el ResultSet y crear los objetos Agente
            while (resultSet.next()) {
                String idAgente = resultSet.getString("idAgente");
                String nombre = resultSet.getString("nombre");
                String email = resultSet.getString("email");
                String telefono = resultSet.getString("telefono");
                String user = resultSet.getString("user");
                String password = resultSet.getString("password");

                // Crear el objeto Agente
                Agente agente = new Agente(idAgente, nombre, email, telefono, user, password);

                // Agregar el agente al modelo o lista
                INSTACE.getModel().usuarios.add(agente);
            }

            System.out.println("Consulta agentes ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    public static void cargarProductos() throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Consulta para obtener los producto
            String sql = "SELECT * FROM bd_reparatodo.producto";
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            Producto producto = null;
            // Procesar el ResultSet y crear los objetos producto
            while (resultSet.next()) {
                String idProducto = resultSet.getString("idProducto");
                String tipo = resultSet.getString("tipo");
                String marca = resultSet.getString("marca");
                String modelo = resultSet.getString("modelo");
                String descripcion = resultSet.getString("descripcion");
                producto = new Producto(idProducto, tipo, marca, modelo, descripcion);

                // Agregar el producto al modelo o lista
                INSTACE.getModel().productos.add(producto);
            }

            System.out.println("Consulta productos ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    public static List<Orden> actividadTecnico(String idTecnico) throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement1 = null;
        ResultSet resultSet1 = null;
        List<Orden>ordenes = new ArrayList<>();
        try {
            // Primera consulta para obtener ordenes
            String sql1 = "SELECT o.* FROM bd_reparatodo.orden o JOIN bd_reparatodo.actividadtecnico a " +
                    "ON o.idOrden = a.idOrden WHERE a.idTecnico = ?";
            statement1 = conn.prepareStatement(sql1);
            statement1.setString(1,idTecnico);
            resultSet1 = statement1.executeQuery();

            // Procesar el ResultSet de la primera consulta
            while (resultSet1.next()) {
                String idOrden = resultSet1.getString("idOrden");
                String idCliente = resultSet1.getString("idCliente");
                String idProducto = resultSet1.getString("idProducto");
                String descripcionAveria = resultSet1.getString("descripcionAveria");
                LocalDate fechaCreacion = resultSet1.getDate("fechaCreacion").toLocalDate();
                Estado estado = Estado.valueOf(resultSet1.getString("estado").toUpperCase());

                Cliente cliente = null;
                try {
                    // Segunda consulta para obtener el cliente
                    String sql2 = "SELECT * FROM bd_reparatodo.cliente WHERE idCliente = ?";
                    PreparedStatement statement2 = conn.prepareStatement(sql2);
                    statement2.setString(1, idCliente);
                    ResultSet resultSet2 = statement2.executeQuery();

                    if (resultSet2.next()) {
                        String nombre = resultSet2.getString("nombre");
                        String direccion = resultSet2.getString("direccion");
                        String telefono = resultSet2.getString("telefono");
                        String email = resultSet2.getString("email");
                        String user = resultSet2.getString("user");
                        String password = resultSet2.getString("password");
                        cliente = new Cliente(idCliente, nombre, email, telefono, direccion, user, password);
                    }
                    resultSet2.close();
                    statement2.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Cliente para la orden " + idOrden + ": " + e.getMessage());
                }

                Producto producto = null;
                try {
                    // Tercera consulta para obtener el producto
                    String sql3 = "SELECT * FROM bd_reparatodo.producto WHERE idProducto = ?";
                    PreparedStatement statement3 = conn.prepareStatement(sql3);
                    statement3.setString(1, idProducto);
                    ResultSet resultSet3 = statement3.executeQuery();

                    if (resultSet3.next()) {
                        String tipo = resultSet3.getString("tipo");
                        String marca = resultSet3.getString("marca");
                        String modelo = resultSet3.getString("modelo");
                        String descripcion = resultSet3.getString("descripcion");
                        producto = new Producto(idProducto, tipo, marca, modelo, descripcion);
                    }
                    resultSet3.close();
                    statement3.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Producto para la orden " + idOrden + ": " + e.getMessage());
                }

                Pago pago = null;
                try {
                    // Cuarta consulta para obtener el Pago
                    String sql4 = "SELECT p.* FROM bd_reparatodo.pago p " +
                            "JOIN bd_reparatodo.ordenpago o ON p.idPago = o.idPago " +
                            "WHERE o.idOrden = ?";
                    PreparedStatement statement4 = conn.prepareStatement(sql4);
                    statement4.setString(1, idOrden);
                    ResultSet resultSet4 = statement4.executeQuery();

                    if (resultSet4.next()) {
                        String idPago = resultSet4.getString("idPago");
                        LocalDate fechaPago = resultSet4.getDate("fechaPago").toLocalDate();
                        String metodoPago = resultSet4.getString("metodoPago");
                        Float monto = resultSet4.getFloat("montoTotal");
                        pago = new Pago(idPago, monto, fechaPago, metodoPago);
                    }
                    resultSet4.close();
                    statement4.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Pago para la orden " + idOrden + ": " + e.getMessage());
                }

                // Crear el objeto Orden y agregarlo al modelo
                Orden orden = new Orden(idOrden, cliente, pago, producto, fechaCreacion, estado, descripcionAveria);
                ordenes.add(orden);
            }

            System.out.println("Consulta de órdenes ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta de órdenes: " + e.getMessage());
        } finally {
            if (resultSet1 != null) resultSet1.close();
            if (statement1 != null) statement1.close();
        }
        return ordenes;
    }

    public static void crearTecnico(Tecnico tecnico) throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement = null;

        try {
            // Consulta para insertar un técnico
            String sql = "INSERT INTO bd_reparatodo.tecnico (idTecnico, nombre, especialidad, user, password) " +
                    "VALUES (?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, tecnico.getIdTecnico());
            statement.setString(2, tecnico.getNombre());
            statement.setString(3, tecnico.getEspecialidad());
            statement.setString(4, tecnico.getUser());
            statement.setString(5, tecnico.getPassword());

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Técnico insertado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el técnico: " + e.getMessage());
        } finally {
            if (statement != null) statement.close();
        }
    }

    public static void crearCliente(Cliente cliente) throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement = null;

        try {
            // Consulta para insertar un cliente
            String sql = "INSERT INTO bd_reparatodo.cliente (idCliente, nombre, direccion, telefono, email, user, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, cliente.getIdCliente());
            statement.setString(2, cliente.getNombre());
            statement.setString(3, cliente.getDireccion());
            statement.setString(4, cliente.getTelefono());
            statement.setString(5, cliente.getEmail());
            statement.setString(6, cliente.getUser());
            statement.setString(7, cliente.getPassword());

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Cliente insertado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente: " + e.getMessage());
        } finally {
            if (statement != null) statement.close();
        }
    }

    public static List<Actividad> actividadesOrden(String idOrden) throws SQLException {

        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Actividad> actividades = new ArrayList<>();

        try {
            // Consulta para obtener las actividades asociadas al idOrden
            String sql = "SELECT * FROM bd_reparatodo.actividadtecnico WHERE idOrden = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, idOrden);
            resultSet = statement.executeQuery();

            // Procesar el ResultSet y crear los objetos Actividad
            while (resultSet.next()) {
                String idTecnico = resultSet.getString("idTecnico");
                LocalDate fecha = resultSet.getDate("fecha").toLocalDate();
                String descripcion = resultSet.getString("descripcion");

                // Crear el objeto Actividad
                Actividad actividad = new Actividad(idTecnico, idOrden,fecha,descripcion);

                // Agregar la actividad a la lista
                actividades.add(actividad);
            }

            System.out.println("Consulta de actividades por idOrden ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }

        return actividades;
    }

    public static List<Orden> ordenesCliente(String idCliente) throws SQLException {
        Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement statement1 = null;
        ResultSet resultSet1 = null;
        List<Orden>ordenes = new ArrayList<>();
        try {
            // Primera consulta para obtener ordenes
            String sql1 = "SELECT * FROM bd_reparatodo.orden WHERE idCliente = ? ";
            statement1 = conn.prepareStatement(sql1);
            statement1.setString(1,idCliente);
            resultSet1 = statement1.executeQuery();

            // Procesar el ResultSet de la primera consulta
            while (resultSet1.next()) {
                String idOrden = resultSet1.getString("idOrden");
                String idCliente1 = resultSet1.getString("idCliente");
                String idProducto = resultSet1.getString("idProducto");
                String descripcionAveria = resultSet1.getString("descripcionAveria");
                LocalDate fechaCreacion = resultSet1.getDate("fechaCreacion").toLocalDate();
                Estado estado = Estado.valueOf(resultSet1.getString("estado").toUpperCase());

                Cliente cliente = null;
                try {
                    // Segunda consulta para obtener el cliente
                    String sql2 = "SELECT * FROM bd_reparatodo.cliente WHERE idCliente = ?";
                    PreparedStatement statement2 = conn.prepareStatement(sql2);
                    statement2.setString(1, idCliente);
                    ResultSet resultSet2 = statement2.executeQuery();

                    if (resultSet2.next()) {
                        String nombre = resultSet2.getString("nombre");
                        String direccion = resultSet2.getString("direccion");
                        String telefono = resultSet2.getString("telefono");
                        String email = resultSet2.getString("email");
                        String user = resultSet2.getString("user");
                        String password = resultSet2.getString("password");
                        cliente = new Cliente(idCliente, nombre, email, telefono, direccion, user, password);
                    }
                    resultSet2.close();
                    statement2.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Cliente para la orden " + idOrden + ": " + e.getMessage());
                }

                Producto producto = null;
                try {
                    // Tercera consulta para obtener el producto
                    String sql3 = "SELECT * FROM bd_reparatodo.producto WHERE idProducto = ?";
                    PreparedStatement statement3 = conn.prepareStatement(sql3);
                    statement3.setString(1, idProducto);
                    ResultSet resultSet3 = statement3.executeQuery();

                    if (resultSet3.next()) {
                        String tipo = resultSet3.getString("tipo");
                        String marca = resultSet3.getString("marca");
                        String modelo = resultSet3.getString("modelo");
                        String descripcion = resultSet3.getString("descripcion");
                        producto = new Producto(idProducto, tipo, marca, modelo, descripcion);
                    }
                    resultSet3.close();
                    statement3.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Producto para la orden " + idOrden + ": " + e.getMessage());
                }

                Pago pago = null;
                try {
                    // Cuarta consulta para obtener el Pago
                    String sql4 = "SELECT p.* FROM bd_reparatodo.pago p " +
                            "JOIN bd_reparatodo.ordenpago o ON p.idPago = o.idPago " +
                            "WHERE o.idOrden = ?";
                    PreparedStatement statement4 = conn.prepareStatement(sql4);
                    statement4.setString(1, idOrden);
                    ResultSet resultSet4 = statement4.executeQuery();

                    if (resultSet4.next()) {
                        String idPago = resultSet4.getString("idPago");
                        LocalDate fechaPago = resultSet4.getDate("fechaPago").toLocalDate();
                        String metodoPago = resultSet4.getString("metodoPago");
                        Float monto = resultSet4.getFloat("montoTotal");
                        pago = new Pago(idPago, monto, fechaPago, metodoPago);
                    }
                    resultSet4.close();
                    statement4.close();
                } catch (SQLException e) {
                    System.out.println("Error al obtener el Pago para la orden " + idOrden + ": " + e.getMessage());
                }

                // Crear el objeto Orden y agregarlo al modelo
                Orden orden = new Orden(idOrden, cliente, pago, producto, fechaCreacion, estado, descripcionAveria);
                ordenes.add(orden);
            }

            System.out.println("Consulta de órdenes ejecutada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta de órdenes: " + e.getMessage());
        } finally {
            if (resultSet1 != null) resultSet1.close();
            if (statement1 != null) statement1.close();
        }
        return ordenes;
    }
}
