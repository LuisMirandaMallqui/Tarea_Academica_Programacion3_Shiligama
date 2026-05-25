package pe.edu.pucp.model.usuario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void getIdUsuario() {
        Cliente clienteTest = new Cliente();
        clienteTest.setIdUsuario(105);
        assertEquals(105, clienteTest.getIdUsuario());
    }

    @Test
    void setIdUsuario() {
    }

    @Test
    void getDireccionEntrega() {
    }

    @Test
    void setDireccionEntrega() {
    }

    @Test
    void obtenerRol() {
    }
}
