package pe.edu.pucp.usuario.impl;

import pe.edu.pucp.model.usuario.Usuario;
import pe.edu.pucp.persistance.dao.usuario.dao.AuthDao;
import pe.edu.pucp.persistance.dao.usuario.impl.AuthDaoImpl;
import pe.edu.pucp.usuario.bo.AuthBo;

public class AuthBoImpl implements AuthBo {
    private final AuthDao authDao;

    public AuthBoImpl() {
        this.authDao = new AuthDaoImpl();
    }

    @Override
    public Usuario autenticar(String correo, String contrasena) throws Exception {
        validarTextoObligatorio(correo, "El correo es obligatorio.");
        validarTextoObligatorio(contrasena, "La contraseña es obligatoria.");
        return authDao.autenticar(correo.trim(), contrasena);
    }

    private void validarTextoObligatorio(String texto, String mensaje) throws Exception {
        if (texto == null || texto.trim().isEmpty()) {
            throw new Exception(mensaje);
        }
    }
}
