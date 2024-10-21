package modelo;

import SqlDataBase.SqlQuery;
import lombok.Data;
import modelo.enums.Estado;

import java.util.List;

@Data
public class Taller {

    public List<Orden>colaRepaciones;
    public List<Usuario>usuarios;
    public Agente agenteLogeado;


    public boolean crearOrden(Orden orden){
    Orden aux = colaRepaciones.stream().filter((ordenAux) -> ordenAux.getIdOrden().equals(orden.getIdOrden())).findFirst().orElse(null);

    if (aux == null){
        colaRepaciones.add(orden);
        SqlQuery.crearOrden(orden);
        return true;
    }
        return false;
    }

    public boolean cambiarEstadoOrden(int idOrden, Estado estadoNuevo) {
        return false;
    }

    public boolean tomarOrden(int idTecnico, int idOrden) {
        return false;
    }

    public boolean registrarActividad(int idOrden, String registroActividades) {
        return false;
    }


    public boolean registrarPago(int idOrden, Pago pago) {
        return false;
    }

    public boolean registrarCliente(Cliente cliente) {
        return false;
    }

    public boolean actualizarCliente(Cliente datosNuevos, int idCliente) {
        return false;
    }

    public boolean registrarProducto(Producto producto) {
        return false;
    }

    public boolean actualizaProducto(Producto datosNuevos, int idProducto) {
        return false;
    }

}
