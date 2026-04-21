package pe.edu.pucp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBManager {
    private static DBManager instance; // Única instancia del singleton
    private Connection connection; // Conexión activa a la BD
    private final String hostname;
    private final String schema;
    private final String username;
    private final String password;
    private final String port;
    private final String url;


    private DBManager() {
        ResourceBundle bundle = ResourceBundle.getBundle("datos");
        this.hostname = bundle.getString("db.host");
        this.schema = bundle.getString("db.schema");
        this.username = bundle.getString("db.user");
        this.password = bundle.getString("db.password");
        this.port = bundle.getString("db.port");
        this.url = "jdbc:mysql://" + hostname + ":" + port + "/" + schema;
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Conexión establecida correctamente.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar con la BD: " + e.getMessage());
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
