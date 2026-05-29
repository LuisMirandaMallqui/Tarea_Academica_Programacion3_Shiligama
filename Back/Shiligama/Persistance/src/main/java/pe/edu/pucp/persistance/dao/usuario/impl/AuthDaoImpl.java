package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.usuario.Administrador;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.model.usuario.Usuario;
import pe.edu.pucp.persistance.dao.usuario.dao.AuthDao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación del DAO de autenticación.
 * Invoca el SP AUTENTICAR_USUARIO, lee el ROL devuelto y instancia el
 * subtipo concreto (Cliente / Trabajador / Administrador) con todos sus
 * campos. La contraseña queda en null por seguridad (el SP no la devuelve).
 */
public class AuthDaoImpl implements AuthDao {

    // SP: AUTENTICAR_USUARIO(IN _correo, IN _contrasena)
    @Override
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, correo);
        parametrosEntrada.put(2, contrasena);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("AUTENTICAR_USUARIO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    String rol = rs.getString("ROL");
                    if (rol != null) {
                        usuario = mapearSegunRol(rs, rol);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en autenticar (AuthDaoImpl): " + ex.getMessage());
        }
        return usuario;
    }

    /**
     * Instancia el subtipo concreto según el ROL y popula los campos base
     * + los específicos de cada hijo.
     */
    private Usuario mapearSegunRol(ResultSet rs, String rol) throws SQLException {
        Usuario usuario;
        switch (rol) {
            case "CLIENTE": {
                Cliente c = new Cliente();
                c.setDireccionEntrega(rs.getString("DIRECCION_ENTREGA"));
                usuario = c;
                break;
            }
            case "TRABAJADOR": {
                Trabajador t = new Trabajador();
                t.setCargo(rs.getString("CARGO"));
                Date fechaIngreso = rs.getDate("FECHA_INGRESO");
                if (fechaIngreso != null) {
                    t.setFechaIngreso(fechaIngreso.toLocalDate());
                }
                usuario = t;
                break;
            }
            case "ADMINISTRADOR":
                usuario = new Administrador();
                break;
            default:
                return null;
        }
        usuario.setIdUsuario(rs.getInt("USUARIO_ID"));
        usuario.setNombres(rs.getString("NOMBRES"));
        usuario.setApellidos(rs.getString("APELLIDOS"));
        usuario.setDni(rs.getString("DNI"));
        usuario.setTelefono(rs.getString("TELEFONO"));
        usuario.setCorreo(rs.getString("CORREO"));
        // Contraseña no se devuelve al cliente.
        usuario.setContrasena(null);
        return usuario;
    }
}
