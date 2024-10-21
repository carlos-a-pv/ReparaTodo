package modelo;

import SqlDataBase.SqlQuery;
import com.sun.javafx.tk.PrintPipeline;
import lombok.Data;
import modelo.enums.Estado;
import resources.Cola;
import resources.NodoCola;

import java.util.ArrayList;
import java.util.List;

import static controlador.TallerController.INSTACE;

@Data
public class Taller {

    public Cola<Orden> colaRepaciones;
    public List<Usuario>usuarios;
    public List<Producto> productos;
    public Agente agenteLogeado = new Agente("01","carlos","carlos@email.com","314","carlos","123");

    public Taller() {
        this.colaRepaciones = new Cola<>();
        this.usuarios = new ArrayList<>();
        this.productos = new ArrayList<>();
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
            SqlQuery.crearActividadTecnico(idOrden,agenteLogeado.getIdAgente(),registroActividades);
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

    public boolean registrarCliente(Cliente cliente) {

        Usuario usuario = usuarios.stream().filter(cliente1 -> cliente1.getUser().equals(cliente.getUser())).findFirst().orElse(null);

        if (usuario == null){
            usuario.setUser(cliente.getUser());
            usuario.setPassword(cliente.getPassword());
            Cliente cliente2 = new Cliente(cliente.getIdCliente(),cliente.getNombre(),cliente.getEmail(),cliente.getTelefono(),cliente.getDireccion(),usuario.getUser(),usuario.getPassword());
            usuarios.add(cliente2);
            SqlQuery.registrarCliente(cliente2);
            return true;
        }
        return false;
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

            if (!userExists) {

                clienteActualizado.setNombre(datosNuevos.getNombre());
                clienteActualizado.setEmail(datosNuevos.getEmail());
                clienteActualizado.setTelefono(datosNuevos.getTelefono());
                clienteActualizado.setDireccion(datosNuevos.getDireccion());

                SqlQuery.actualizarCliente(clienteActualizado, idCliente);
                return true;
            } else {
                System.out.println("El nuevo user ya existe.");
            }
        } else {
            System.out.println("El cliente no existe.");
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


}
