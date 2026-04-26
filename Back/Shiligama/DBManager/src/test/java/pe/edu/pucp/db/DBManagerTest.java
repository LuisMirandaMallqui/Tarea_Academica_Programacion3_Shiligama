package pe.edu.pucp.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
class DBManagerTest {

    @Test
    void getInstance() { // pare ver que todo este bien con el patron singleton y la lectura del properties
        System.out.println("getInstance");
        DBManager dBManager = DBManager.getInstance();
        assertNotNull(dBManager);
    }

    @Test
    void getConnection() { // para ver que los properties si enganchen en la construccion de la conexion
        System.out.println("getConnection");
        DBManager dBManager = DBManager.getInstance();
        Connection conexion = dBManager.getConnection();
        assertNotNull(conexion);
        dBManager.closeConnection();
    }
}