package es.iespuertolacruz.almacen.controlador;

import java.util.ArrayList;

import es.iespuertolacruz.almacen.api.Cliente;
import es.iespuertolacruz.almacen.exception.AlmacenException;
import es.iespuertolacruz.almacen.exception.BbddException;
import es.iespuertolacruz.almacen.exception.FicheroException;
import es.iespuertolacruz.almacen.modelo.ClienteModelo;
public class ClienteController {
    
    ClienteModelo clienteModelo;
    EmpresaController empresaController;

    /**
     * Constructor de la clase
     * @throws BbddException controlado
     * @throws FicheroException controlado
     */
    public ClienteController() throws BbddException, FicheroException {
        empresaController = new EmpresaController();
        clienteModelo = new ClienteModelo();
    }

    /**
     * Funcion que verifica si existe un cliente en la bbdd
     * @param cliente a verificar
     * @return true/false existe o no
     * @throws BbddException controlado
     */
    private boolean existe(Cliente cliente) throws BbddException {
        return buscar(cliente.getCif()) != null;
    }
    
    /**
     * Funcion que valida un cliente
     * @param cliente a validar
     * @throws AlmacenException controlado
     */
    public void validar(Cliente cliente) throws AlmacenException {
        String mensaje = "";
        if (cliente == null) {
            throw new AlmacenException("El cliente no puede ser nulo");
        }
        if (cliente.getCif() == null || !Validaciones.validarCif(cliente.getCif())) {
            mensaje += "El cif del cliente no puede ser nulo o no valido\n";
        }
        if (cliente.getPorcentajeDesc() < 0) {
            mensaje += "El porcentaje de descuento del cliente no puede ser menor que 0\n";
        }
        if(!mensaje.isBlank()) {
            throw new AlmacenException(mensaje);
        }
    }

     /**
     * Metodo que inserta un cliente en la bbdd
     * @param cliente a insertar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void insertar(Cliente cliente) throws BbddException, AlmacenException {
        validar(cliente);
        if(!existe(cliente)) clienteModelo.insertar(cliente);
        else throw new AlmacenException("El cliente ya existe en la base de datos");
    }
    /**
     * Metodo que elimina un cliente de la bbdd
     * @param cliente a eliminar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void eliminar(Cliente cliente) throws BbddException, AlmacenException {
        validar(cliente);
        if(existe(cliente)) clienteModelo.eliminar(cliente);
        else throw new AlmacenException("El cliente no existe en la base de datos");
    }
    /**
     * Metodo que modifica un cliente de la bbdd
     * @param cliente a modificar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void modificar(Cliente cliente) throws BbddException, AlmacenException {
        validar(cliente);
        if(existe(cliente)) clienteModelo.modificar(cliente);
        else throw new AlmacenException("El cliente no existe en la base de datos");
    }
    /**
     * Metodo que busca un cliente en la bbdd
     * @param cif del cliente a buscar
     * @throws BbddException controlado
     */
    public Cliente buscar(String cif) throws BbddException {
        return clienteModelo.buscar(cif);
    }
    /**
     * Funcion que devuelve el listado de clientes
     * @return arraylist de clientes
     * @throws BbddException
     */
    public ArrayList<Cliente> buscarTodos() throws BbddException {
        return clienteModelo.buscarTodos();
    }
}
