package es.iespuertolacruz.almacen.modelo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import es.iespuertolacruz.almacen.exception.BbddException;
import es.iespuertolacruz.almacen.exception.FicheroException;

public class Bbdd {

    private static final String SE_HA_PRODUCIDO_UN_ERROR_REALIZANDO_LA_CONSULTA = "Se ha producido un error realizando la consulta";

    protected String nombreTabla;
    protected String clave;
    protected String driver;
    protected String url;
    protected String usuario;
    protected String password;

    /**
     * Constructor de la clase
     * 
     * @param nombreTabla a generar
     * @param clave       de la tabla
     * @param driver      de la bbdd
     * @param url         de la bbdd
     * @param usuario     de la bbdd
     * @param password    de la bbd
     * @throws BbddException    controlado
     * @throws FicheroException controlado
     */
    public Bbdd(String nombreTabla, String clave, String driver, String url, String usuario, String password)
            throws BbddException, FicheroException {
        this.nombreTabla = nombreTabla;
        this.clave = clave;
        this.driver = driver;
        this.url = url;
        this.usuario = usuario;
        this.password = password;
        inicializarTabla(nombreTabla);
    }

    /**
     * Metodo que carga las tablas e inserciones en la base de datos
     * 
     * @param driver de la base de datos
     * @throws BbddException    controlado
     * @throws FicheroException controlado
     */
    private void inicializarTabla(String nombreTabla) throws BbddException, FicheroException {
        DatabaseMetaData databaseMetaData;
        Connection connection = null;
        ResultSet resultSet = null;
        ArrayList<String> listaTablas = new ArrayList<>();
        try {
            connection = getConnection();
            databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});
            while (resultSet.next()) {
                listaTablas.add(resultSet.getString("TABLE_NAME"));
            }
            if (!listaTablas.contains(nombreTabla)) {
                String crearTabla = new Fichero().leer("resources/sql/sqlite/" + nombreTabla + ".crear.sql");
                actualizar(crearTabla);
                String insertElemento = new Fichero().leer("resources/sql/sqlite/" + nombreTabla + ".insertar.sql");
                insertarElementos(insertElemento);
            }

        } catch (Exception e) {
            throw new BbddException("Se ha producido un error en la inicializacion de la BBDD", e);
        } finally {
            closeConnection(connection, null, resultSet);
        }
    }

    /**
     * Metodo que realiza las inserciones en las tablas
     * 
     * @param cadena de texto que contiene las inserciones
     * @throws BbddException controlado
     */
    protected void insertarElementos(String cadena) throws BbddException {
        String[] cadenaSeparada = cadena.split(";");
        for (String sentencia : cadenaSeparada) {
            actualizar(sentencia);
        }
    }

    /**
     * Funcion encargada de realizar la conexion con la BBDD
     * 
     * @return la coneccion
     * @throws BbddException controlado
     */
    protected Connection getConnection() throws BbddException {
        Connection connection = null;

        try {
            Class.forName(driver);
            if (usuario == null || password == null) {
                connection = DriverManager.getConnection(url);
            } else {
                DriverManager.getConnection(url, usuario, password);
            }
        } catch (Exception exception) {
            throw new BbddException("No se ha podido establecer la coneccion con la BBDD", exception);
        }
        return connection;
    }

    /**
     * Metodo que cierra las conexiciones con la base de datos
     * 
     * @param connection
     * @param statement
     * @param resultSet
     * @throws BbddException
     */
    protected void closeConnection(Connection connection, Statement statement, ResultSet resultSet)
            throws BbddException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            throw new BbddException("Se ha producido un error cerrando la conexion", exception);
        }
    }

    /**
     * Metodo encargado de realizar la actualizacion de la BBDD
     * 
     * @param sql a ejecutar
     * @throws BbddException error controlado
     */
    protected void actualizar(String sql) throws BbddException {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception exception) {
            throw new BbddException(SE_HA_PRODUCIDO_UN_ERROR_REALIZANDO_LA_CONSULTA, exception);
        } finally {
            closeConnection(connection, statement, null);
        }

    }

    /**
     * Funcion que realiza una consulta sobre una sentencia sql dada
     * 
     * @param sql de la consulta
     * @return lista resultados (0..n) Usuasios
     * @throws PersistenciaException error controlado
     */
    protected ResultSet buscarElementos(String sql) throws BbddException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new BbddException("Se ha producido un error en la busqueda", exception);
        }
        return resultSet;
    }
}