package pe.edu.pucp.vetcitas.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class DBManager {
    //Atributos
    private Connection con;
    private static DBManager dbManager;
    private final String hostname;
    private final String esquema;
    private final String usuario;
    private final String password;
    private final String puerto;
    private final String url;

    //Constructor
    private DBManager(){
        ResourceBundle db = ResourceBundle.getBundle("db");
        this.hostname = db.getString("db.host");
        this.esquema = db.getString("db.esquema");
        this.usuario = db.getString("db.user");
        this.password = db.getString("db.password");
        this.puerto = db.getString("db.port");
        this.url = "jdbc:mysql://" + this.hostname+ ":"+ this.puerto+"/"+ this.esquema;
    }

    //Metodos
    public Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection(
                    this.url,
                    this.usuario,
                    this.password
            );
            System.out.println("Se ha realizado la conexión");
        }catch (Exception ex){
            System.out.println("Error al conectarse con la BD: "+ex.getMessage());
        }
        return this.con;
        //Cada vez que se retorne este objeto se tendra una conexión activa
        //con la base de datos
    }

    public static DBManager getInstance(){
        if(dbManager==null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }
}
