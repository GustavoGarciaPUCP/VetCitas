package pe.edu.pucp.vetcitas.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class DBManager {
    private static DBManager dbManager;
    private final String url;
    private final String usuario;
    private final String password;

    private DBManager() {
        ResourceBundle db = ResourceBundle.getBundle("db");
        String hostname = db.getString("db.host");
        String puerto = db.getString("db.port");
        String esquema = db.getString("db.esquema");
        this.usuario = db.getString("db.user");
        this.password = db.getString("db.password");

        // AQUÍ ESTÁ LA SOLUCIÓN AL "Communications link failure"
        this.url = "jdbc:mysql://" + hostname + ":" + puerto + "/" + esquema +
                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima&autoReconnect=true";
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(this.url, this.usuario, this.password);
            // Comentamos el println para que no te sature la consola con cada consulta
            // System.out.println("Se ha realizado la conexión");
        } catch (Exception ex) {
            throw new RuntimeException("Error al conectarse con la BD: " + ex.getMessage(), ex);
        }
        return con;
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }
}
