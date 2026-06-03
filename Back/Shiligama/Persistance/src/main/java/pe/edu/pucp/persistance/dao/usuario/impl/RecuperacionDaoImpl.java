package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.seguridad.TokenRecuperacion;
import pe.edu.pucp.model.seguridad.UsuarioBasicoDto;
import pe.edu.pucp.persistance.dao.usuario.dao.RecuperacionDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class RecuperacionDaoImpl implements RecuperacionDao {

    // SP: BUSCAR_USUARIO_X_CORREO(IN _correo)
    @Override
    public UsuarioBasicoDto buscarUsuarioPorCorreo(String correo) {
        UsuarioBasicoDto usuario = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, correo);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_USUARIO_X_CORREO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    usuario = new UsuarioBasicoDto();
                    usuario.setIdUsuario(rs.getInt("USUARIO_ID"));
                    usuario.setNombres(rs.getString("NOMBRES"));
                    usuario.setApellidos(rs.getString("APELLIDOS"));
                    usuario.setCorreo(rs.getString("CORREO"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar usuario por correo: " + ex.getMessage());
        }
        return usuario;
    }

    // SP: ACTUALIZAR_CONTRASENA(IN _usuario_id, IN _contrasena)
    @Override
    public int actualizarContrasena(int idUsuario, String nuevaContrasena) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idUsuario);
        parametrosEntrada.put(2, nuevaContrasena);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ACTUALIZAR_CONTRASENA", parametrosEntrada, null);
    }

    // SP: INSERTAR_TOKEN_RECUPERACION(OUT _token_id, IN _usuario_id, _token, _expiracion)
    @Override
    public int crearToken(TokenRecuperacion token) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, token.getIdUsuario());
        parametrosEntrada.put(3, token.getToken());
        parametrosEntrada.put(4, token.getExpiracion()); // LocalDateTime -> setObject (preserva hora)

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_TOKEN_RECUPERACION", parametrosEntrada, parametrosSalida);
        token.setIdToken((int) parametrosSalida.get(1));
        return token.getIdToken();
    }

    // SP: BUSCAR_TOKEN_RECUPERACION(IN _token)
    @Override
    public TokenRecuperacion buscarToken(String tokenValor) {
        TokenRecuperacion token = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, tokenValor);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_TOKEN_RECUPERACION", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    token = new TokenRecuperacion();
                    token.setIdToken(rs.getInt("TOKEN_ID"));
                    token.setIdUsuario(rs.getInt("USUARIO_ID"));
                    token.setToken(rs.getString("TOKEN"));
                    Timestamp exp = rs.getTimestamp("EXPIRACION");
                    if (exp != null) {
                        token.setExpiracion(exp.toLocalDateTime());
                    }
                    token.setUsado(rs.getBoolean("USADO"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar token de recuperacion: " + ex.getMessage());
        }
        return token;
    }

    // SP: MARCAR_TOKEN_USADO(IN _token_id)
    @Override
    public int marcarTokenUsado(int idToken) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idToken);
        return DBManager.getInstance().ejecutarProcedimiento(
                "MARCAR_TOKEN_USADO", parametrosEntrada, null);
    }
}
