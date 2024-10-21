package controlador;

import modelo.Cliente;
import modelo.Orden;
import modelo.Pago;
import modelo.Producto;
import modelo.enums.Estado;
import servicios.IModelFactoryService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static controlador.TallerController.INSTACE;

public class ModelFactoryController implements IModelFactoryService {

    private static class SingletonHolder {
        private final static ModelFactoryController eINSTANCE = new ModelFactoryController();
    }

    public static ModelFactoryController getInstance() {
        return SingletonHolder.eINSTANCE;
    }


    @Override
    public boolean crearOrden(Orden orden) {
        INSTACE.getModel().crearOrden(orden);
        return false;
    }

    @Override
    public boolean cambiarEstadoOrden(String idOrden, Estado estadoNuevo) {
        return false;
    }

    @Override
    public boolean tomarOrden(String idTecnico, String idOrden) {
        return false;
    }

    @Override
    public boolean registrarActividad(String idOrden, String registroActividades) {
        return false;
    }

    @Override
    public boolean registrarPago(String idOrden, Pago pago) {
        return false;
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        return false;
    }

    @Override
    public boolean actualizarCliente(Cliente datosNuevos, String idCliente) {
        return false;
    }

    @Override
    public boolean registrarProducto(Producto producto) {
        return false;
    }

    @Override
    public boolean actualizaProducto(Producto datosNuevos, String idProducto) {
        return false;
    }



}
