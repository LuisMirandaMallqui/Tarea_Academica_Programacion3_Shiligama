package pe.edu.pucp.model.usuario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDtoTest {

    @Test
    void getIdCliente() {
        ClienteDto clienteTest = new ClienteDto();
        clienteTest.setIdCliente(105);
        assertEquals(105, clienteTest.getIdCliente());
    }

    @Test
    void setIdCliente() {
    }

    @Test
    void getTelefonoWhatsapp() {
    }

    @Test
    void setTelefonoWhatsapp() {
    }

    @Test
    void getDireccionEntrega() {
    }

    @Test
    void setDireccionEntrega() {
    }

    @Test
    void getFechaRegistro() {
    }

    @Test
    void setFechaRegistro() {
    }

    @Test
    void obtenerRol() {
    }
}