package es.iespuertolacruz.almacen.controlador;

import es.iespuertolacruz.almacen.api.Zona;
import es.iespuertolacruz.almacen.exception.AlmacenException;
import es.iespuertolacruz.almacen.exception.BbddException;
import es.iespuertolacruz.almacen.exception.FicheroException;
import es.iespuertolacruz.almacen.modelo.ZonaModelo;

public class ZonaController {
    
    ZonaModelo zonaModelo;

    /**
     * Constructor de la clase
     * @throws BbddException controlado
     * @throws FicheroException controlado
     */
    public ZonaController() throws BbddException, FicheroException {
        zonaModelo = new ZonaModelo();
    }

    /**
     * Funcion que verifica si existe una zona en la bbdd
     * @param zona a verificar
     * @return true/false existe o no
     * @throws BbddException controlado
     */
    private boolean existe(Zona zona) throws BbddException {
        return buscar(zona.getIdZona()) != null;
    }

    /**
     * Funcion que valida una zona
     * @param zona a validar
     * @throws AlmacenException controlado
     */
    public void validar(Zona zona) throws AlmacenException {
        String mensaje = "";
        if (zona == null) {
            throw new AlmacenException("La zona no puede ser nula");
        }
        if (!Validaciones.validarZona(zona.getIdZona())) {
            mensaje += "El id de la zona debe estar entre la A y la Z\n";
        }
        if (zona.getTipo() == null || !Validaciones.validarTipoProducto(zona.getTipo())) {
            mensaje += "El tipo de la zona no puede ser nulo o no valido\n";
        }
        if(!mensaje.isBlank()) {
            throw new AlmacenException(mensaje);
        }
    }

    /**
     * Metodo que inserta una zona en la bbdd
     * @param zona a insertar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void insertar(Zona zona) throws BbddException, AlmacenException {
        validar(zona);
        if(!existe(zona)) zonaModelo.insertar(zona);
        else throw new AlmacenException("La zona ya existe en la base de datos");
    }
    /**
     * Metodo que elimina una zona de la bbdd
     * @param zona a eliminar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void eliminar(Zona zona) throws BbddException, AlmacenException {
        validar(zona);
        if(existe(zona)) zonaModelo.eliminar(zona);
        else throw new AlmacenException("La zona no existe en la base de datos");
    }
    /**
     * Metodo que modifica una zona de la bbdd
     * @param zona a modificar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void modificar(Zona zona) throws BbddException, AlmacenException {
        validar(zona);
        if(existe(zona)) zonaModelo.modificar(zona);
        else throw new AlmacenException("La zona no existe en la base de datos");
    }
    /**
     * Metodo que busca una zona en la bbdd
     * @param idZona de la zona a buscar
     * @throws BbddException controlado
     */
    public Zona buscar(char idZona) throws BbddException {
        return zonaModelo.buscar(String.valueOf(idZona));
    }

}
