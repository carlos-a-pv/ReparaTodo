package controlador;

import modelo.*;
import modelo.enums.Estado;
import servicios.IModelFactoryService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static controlador.TallerController.INSTACE;

public class ModelFactoryController implements IModelFactoryService {

    public Usuario autenticar(String user, String password) {
        return INSTACE.getModel().autenticar(user,password);
    }

    public List<Cliente> getClientes() {
        return INSTACE.getModel().obtenerClientes();
    }

    public Cliente buscarCliente(String idCliente) {
        return INSTACE.getModel().obtenerCliente(idCliente);
    }

    public Producto buscarProducto(String idProducto) {
        return INSTACE.getModel().obtenerProducto(idProducto);
    }

    public List<Producto> getProductos() {
        return INSTACE.getModel().obtenerProductos();
    }

    public List<Orden> getOrdenes() {
        return INSTACE.getModel().obtenerOrdenes();
    }

    public void setOrdenSeleccionada(Orden ordeSelecionada) {
        INSTACE.getModel().setOrdenSeleccionada(ordeSelecionada);
    }

    public List<Orden> getOrdenesTecnico() throws SQLException {
    return INSTACE.getModel().getOrdenesTecnico();
    }

    public boolean crearTecnico(Tecnico tecnico) throws SQLException {
        return INSTACE.getModel().crearTecnico(tecnico);
    }

    public List<Tecnico> getTecnicos() {
        return INSTACE.getModel().obtenerTecnicos();
    }

    public List<Actividad> getActividades(String idOrden) throws SQLException {
        return INSTACE.getModel().getActividades(idOrden);
    }

    public List<Orden> getOrdenesCliente() throws SQLException {
        return INSTACE.getModel().getOrdenesCliente();
    }

    public boolean crearActividad(Actividad actividad) {
        return INSTACE.getModel().crearActividad(actividad);
    }

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
    public boolean registrarCliente(Cliente cliente) throws SQLException {
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

//    @Override
//    public Cliente buscarCliente(String idCliente) {
//        return INSTACE.getModel().buscarCliente(idCliente);
//    }
//
//    @Override
//    public ArrayList<Orden> getOrdenes() {
//        return INSTACE.getModel().getOrdenes();
//    }
//
//    @Override
//    public ArrayList<Cliente> getClientes() {
//        return INSTACE.getModel().getClientes();
//    }
//
//    @Override
//    public String generarId() {
//        return INSTACE.getModel().generarId();
//    }
//
    public Orden getOrdenSeleccionada() {
        return INSTACE.getModel().getOrdenSeleccionada();
    }
//
//    @Override
//    public void setOrdenSeleccionada(Orden ordeSelecionada) {
//        INSTACE.getModel().setOrdenSeleccionada(ordeSelecionada);
//    }
//
//    @Override
//    public boolean crearCliente(Cliente newCliente) {
//        return INSTACE.getModel().crearCliente(newCliente);
//    }


}
