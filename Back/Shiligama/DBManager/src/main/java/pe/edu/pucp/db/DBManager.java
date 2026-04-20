package pe.edu.pucp.db;

import java.sql.Connection;
import java.util.ResourceBundle;

public abstract class DBManager {
    private static Connection con;
    private static DBManager dbManager;
    private final String hostname;
    private final String esquema;
    private final String puerto;
    private final String usuario;
    private final String password;
    private final String url;

    protected DBManager() {
        //constructor protegido para evitar que se creen instancias.
        //Solo se podrá crear una instancia y esta debe hacerse usando el
        //método getInstance()
    }


    private DBManager(){
        ResourceBundle db = ResourceBundle.getBundle("datos");
        this.hostname = db.getString("db.host");
        this.esquema = db.getString("db.esquema");
        this.puerto = db.getString("db.puerto");
        this.usuario = db.getString("db.usuario");
        this.password = db.getString("db.password");
        this.url = "jdbc:mysql://" + this.hostname + ":" + this.puerto + "/" + this.esquema;
    }
}
