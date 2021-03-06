package es.iespuertolacruz.almacen.controlador;

import es.iespuertolacruz.almacen.api.Muelle;
import es.iespuertolacruz.almacen.exception.AlmacenException;
import es.iespuertolacruz.almacen.exception.BbddException;
import es.iespuertolacruz.almacen.exception.FicheroException;
import es.iespuertolacruz.almacen.modelo.MuelleModelo;

public class MuelleController {
    
    MuelleModelo muelleModelo;
    ZonaController zonaController;

    /**
     * Constructor de la clase
     * @throws BbddException controlado
     * @throws FicheroException controlado
     */
    public MuelleController() throws BbddException, FicheroException {
        zonaController = new ZonaController();
        muelleModelo = new MuelleModelo();
    }

    /**
     * Funcion que verifica si existe un muelle en la bbdd
     * @param muelle a verificar
     * @return true/false existe o no
     * @throws BbddException controlado
     */
    private boolean existe(Muelle muelle) throws BbddException {
        return buscar(muelle.getIdMuelle()) != null;
    }

    /**
     * Funcion que valida un muelle
     * @param muelle a validar
     * @throws AlmacenException controlado
     */
    public void validar(Muelle muelle) throws AlmacenException {
        String mensaje = "";
        if (muelle == null) {
            throw new AlmacenException("El muelle no puede ser nulo");
        }
        if (muelle.getIdMuelle() <= 0) {
            mensaje += "El id del muelle no puede ser menor o igual que 0\n";
        }
        if (!Validaciones.validarZona(muelle.getIdZona())) {
            mensaje += "El id de la zona debe estar entre la A y la Z";
        }
        if(!mensaje.isBlank()) {
            throw new AlmacenException(mensaje);
        }
    }

     //CRUD muelle

    /**
     * Metodo que inserta un muelle en la bbdd
     * @param muelle a insertar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void insertar(Muelle muelle) throws BbddException, AlmacenException {
        validar(muelle);
        if(!existe(muelle)) muelleModelo.insertar(muelle);
        else throw new AlmacenException("El muelle ya existe en la base de datos");
    }
    /**
     * Metodo que elimina un muelle de la bbdd
     * @param muelle a eliminar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void eliminar(Muelle muelle) throws BbddException, AlmacenException {
        validar(muelle);
        if(existe(muelle)) muelleModelo.eliminar(muelle);
        else throw new AlmacenException("El muelle no existe en la base de datos");
    }
    /**
     * Metodo que modifica un muelle de la bbdd
     * @param muelle a modificar
     * @throws BbddException controlado
     * @throws AlmacenException controlado
     */
    public void modificar(Muelle muelle) throws BbddException, AlmacenException {
        validar(muelle);
        if(existe(muelle)) muelleModelo.modificar(muelle);
        else throw new AlmacenException("El muelle no existe en la base de datos");
    }
    /**
     * Metodo que busca un muelle en la bbdd
     * @param idMuelle del muelle a buscar
     * @throws BbddException controlado
     */
    public Muelle buscar(int idMuelle) throws BbddException {
        return muelleModelo.buscar(String.valueOf(idMuelle));
    }
}
