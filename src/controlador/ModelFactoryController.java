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
        return INSTACE.getModel().crearOrden(orden);
    }

    @Override
    public boolean cambiarEstadoOrden(String idOrden, Estado estadoNuevo) {
        return INSTACE.getModel().cambiarEstadoOrden(idOrden,estadoNuevo);
    }

    @Override
    public boolean tomarOrden(String idTecnico, String idOrden) {
        return INSTACE.getModel().tomarOrden(idTecnico,idOrden);
    }

    @Override
    public boolean registrarActividad(String idOrden,String registroActividades) {
        return INSTACE.getModel().registrarActividad(idOrden,registroActividades);
    }

    @Override
    public boolean registrarPago(String idOrden, Pago pago) {
        return INSTACE.getModel().registrarPago(idOrden,pago);
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        return INSTACE.getModel().registrarCliente(cliente);
    }

    @Override
    public boolean actualizarCliente(Cliente datosNuevos, String idCliente) {
        return INSTACE.getModel().actualizarCliente(datosNuevos,idCliente);
    }

    @Override
    public boolean actualizaProducto(Producto datosNuevos, String idProducto) {
        return INSTACE.getModel().actualizaProducto(datosNuevos,idProducto);
    }



}
