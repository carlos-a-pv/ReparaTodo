package modelo;

import SqlDataBase.SqlQuery;
import com.sun.javafx.tk.PrintPipeline;
import lombok.Data;
import modelo.enums.Estado;
import resources.Cola;
import resources.NodoCola;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static controlador.TallerController.INSTACE;

@Data
public class Taller {

    public Cola<Orden> colaRepaciones;
    public List<Usuario>usuarios;
    public List<Producto> productos;
    public Usuario usuarioLogeado;
    public Orden ordenSeleccionada;
    public Cliente clienteSeleccionado;

    public Taller()  {

        this.colaRepaciones = new Cola<>();
        this.usuarios = new ArrayList<>();
        this.productos = new ArrayList<>();

    }
    public void cargarDatosBD() throws SQLException {

        SqlQuery.cargarOrdenes();
        SqlQuery.cargarProductos();
        SqlQuery.cargarClientes();
        SqlQuery.cargarTecnicos();
        SqlQuery.cargarAgentes();
    }

    public boolean crearOrden(Orden orden){

    NodoCola<Orden> aux = colaRepaciones.buscarNodoCola(orden);

    if (aux == null){
        colaRepaciones.encolar(orden);
        SqlQuery.crearOrden(orden);
        return true;
    }
        return false;
    }

    public boolean cambiarEstadoOrden(String idOrden, Estado estadoNuevo) {


        if (colaRepaciones.cambiarEstadoOrden(idOrden,estadoNuevo)){

            SqlQuery.cambiarEstadoOrden(idOrden,estadoNuevo);
            return true;
        }
        return false;
    }

    public boolean tomarOrden(String idTecnico, String idOrden) {

        Orden orden = colaRepaciones.buscarPorIdOrden(idOrden);
        if (orden != null){

            cambiarEstadoOrden(idOrden,Estado.EN_REPARACION);
            SqlQuery.crearActividadTecnico(idOrden,idTecnico,"Tomada por el tecnico:"+idTecnico);
            colaRepaciones.desencolar();
            return true;
        }
        return false;
    }


    public boolean registrarActividad(String idOrden, String registroActividades) {
        Orden orden = colaRepaciones.buscarPorIdOrden(idOrden);
        if (orden != null){

            cambiarEstadoOrden(idOrden,Estado.EN_REPARACION);
            Tecnico tecnico =(Tecnico) INSTACE.getModel().getUsuarioLogeado();
            SqlQuery.crearActividadTecnico(idOrden,tecnico.getIdTecnico(),registroActividades);
            return true;
        }
        return false;
    }

    public boolean registrarPago(String idOrden, Pago pago) {
        Orden orden = colaRepaciones.buscarPorIdOrden(idOrden);
        if (orden!=null){

            SqlQuery.crearOrdenPago(idOrden,pago);
            return true;
        }
        return false;
    }

    public boolean registrarCliente(Cliente cliente) throws SQLException {
        List<Cliente> clientes = obtenerClientes();
        for (Cliente cliente1 :clientes){
            if (cliente1.getIdCliente().equals(cliente.getIdCliente())){
                return false;
            }
        }
        usuarios.add(cliente);
        SqlQuery.crearCliente(cliente);

        return true;
    }

    public boolean actualizarCliente(Cliente datosNuevos, String idCliente) {
        // Buscar el cliente que se va a actualizar
        Cliente clienteActualizado = null;
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Cliente && ((Cliente) usuario).getIdCliente().equals(idCliente)) {
                clienteActualizado = (Cliente) usuario;
                break;
            }
        }

        // Validar si se encontró el cliente
        if (clienteActualizado != null) {

            boolean userExists = usuarios.stream()
                    .filter(usuario -> usuario instanceof Cliente)
                    .anyMatch(usuario -> !((Cliente) usuario).getIdCliente().equals(idCliente) && usuario.getUser().equals(datosNuevos.getUser()));

            if (userExists) {

                clienteActualizado.setNombre(datosNuevos.getNombre());
                clienteActualizado.setEmail(datosNuevos.getEmail());
                clienteActualizado.setTelefono(datosNuevos.getTelefono());
                clienteActualizado.setDireccion(datosNuevos.getDireccion());

                SqlQuery.actualizarCliente(clienteActualizado, idCliente);
                return true;
            }
        }
        return false;
    }

    public boolean actualizaProducto(Producto datosNuevos, String idProducto) {
        // Validar si el producto a actualizar existe en la lista (opcional)
        boolean productoEncontrado = false;
        for (Producto producto : productos) {
            if (producto.getIdProducto().equals(idProducto)) {
                productoEncontrado = true;
                break; // Salir del bucle si se encuentra el producto
            }
        }

        // Si el producto no se encuentra, retornar false
        if (!productoEncontrado) {
            System.out.println("El producto no existe.");
            return false;
        }

        // Llamar a la clase SQL para actualizar el producto en la base de datos
        boolean actualizado = SqlQuery.actualizarProducto(datosNuevos, idProducto);

        // Retornar el resultado de la actualización
        return actualizado;
    }

    public Usuario autenticar(String user, String password) {

        Usuario existe;

        for (Usuario usuario : usuarios){
            if (usuario.getUser().equals(user)&& usuario.getPassword().equals(password)){

                return usuario;
            }
        }
        return null;
    }

    public List<Cliente> obtenerClientes() {
        List <Cliente> encontrados = new ArrayList<>();
        for (Usuario cliente:usuarios){
            if (cliente instanceof Cliente){
                encontrados.add((Cliente) cliente);
            }
        }
        return encontrados;
    }
    public Cliente obtenerCliente(String idCliente){
        List<Cliente> clientes = obtenerClientes();
        for (Cliente cliente:clientes){
            if (cliente.getIdCliente().equals(idCliente)){
                return cliente;
            }
        }
        return null;
    }

    public Producto obtenerProducto(String idProducto) {
        for (Producto producto : productos){
            if (producto.getIdProducto().equals(idProducto)){
                return producto;
            }
        }
        return null;
    }

    public List<Producto> obtenerProductos() {
        return productos;
    }

    public List<Orden> obtenerOrdenes() {
       return colaRepaciones.obtenerLista();
    }

    public Usuario getUsuarioLogeado() {
        return usuarioLogeado;
    }

    public void setUsuarioLogeado(Usuario usuarioLogeado) {
        this.usuarioLogeado = usuarioLogeado;
    }

    public Orden getOrdenSeleccionada() {
        return ordenSeleccionada;
    }

    public void setOrdenSeleccionada(Orden ordenSeleccionada) {
        this.ordenSeleccionada = ordenSeleccionada;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public List<Orden> getOrdenesTecnico() throws SQLException {
        return SqlQuery.actividadTecnico(((Tecnico)usuarioLogeado).getIdTecnico());
    }

    public boolean crearTecnico(Tecnico tecnico) throws SQLException {
        List<Tecnico>tecnicos = obtenerTecnicos();

        for (Tecnico tecnicoAux:tecnicos){
                if (tecnicoAux.getIdTecnico().equals(tecnico.getIdTecnico())){
                 return false;
                }
        }
        usuarios.add(tecnico);
        SqlQuery.crearTecnico(tecnico);
        return true;
    }

    public List<Tecnico> obtenerTecnicos() {
        List <Tecnico> encontrados = new ArrayList<>();
        for (Usuario tecnico:usuarios){
            if (tecnico instanceof Tecnico){
                encontrados.add((Tecnico) tecnico);
            }
        }
        return encontrados;
    }

    public List<Actividad> getActividades(String idOrden) throws SQLException {
        return SqlQuery.actividadesOrden(idOrden);
    }

    public List<Orden> getOrdenesCliente() throws SQLException {
        return SqlQuery.ordenesCliente(((Cliente)usuarioLogeado).getIdCliente());
    }

    public boolean crearActividad(Actividad actividad) {
        SqlQuery.crearActividadTecnico(actividad.idOrden,actividad.idTecnico,actividad.getDescripcion());
        return true;
    }
}
