package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.model.usuario.UsuarioDto;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDAO;
import pe.edu.pucp.persistance.daoImpl.DAOImplBase;
import pe.edu.pucp.persistance.daoImpl.util.Columna;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioImpl extends DAOImplBase implements UsuarioDAO {
    private UsuarioDto usuario;
    public UsuarioImpl(){
        super("usuarios");
        this.usuario = null;
        this.retornarLlavePrimaria = true;
    }
    @Override
    protected void configurarListaDeColumnas() {
        this.listaColumnas.add(new Columna("PERSONA_ID", true, true));
        this.listaColumnas.add(new Columna("ESTADO_PERSONA_ID_ESTADOPERSONA", false, false));
        this.listaColumnas.add(new Columna("NOMBRES", false, false));
        this.listaColumnas.add(new Columna("PRIMER_APELLIDO", false, false));
        this.listaColumnas.add(new Columna("SEGUNDO_APELLIDO", false, false));
        this.listaColumnas.add(new Columna("CODIGO", false, false));
        this.listaColumnas.add(new Columna("CORREO", false, false));
        this.listaColumnas.add(new Columna("CONTRASENA", false, false));
        this.listaColumnas.add(new Columna("ULTIMA_ACTIVIDAD", false, false));
        this.listaColumnas.add(new Columna("USUARIO_CREACION", false, false));
        this.listaColumnas.add(new Columna("USUARIO_MODIFICACION", false, false));
    }

    @Override
    protected void incluirValorDeParametrosParaInsercion() throws SQLException {
        int i = 1;
        this.prepared_statement.setInt(i++, this.persona.getEstadoPersona().getEstadoPersonaId());
        this.prepared_statement.setString(i++, this.persona.getNombres());
        this.prepared_statement.setString(i++, this.persona.getPrimerApellido());
        this.prepared_statement.setString(i++, this.persona.getSegundoApellido());
        this.prepared_statement.setString(i++, this.persona.getCodigo());
        this.prepared_statement.setString(i++, this.persona.getCorreo());
        this.prepared_statement.setString(i++, this.persona.getContrasena());
        this.prepared_statement.setString(i++, this.persona.getUltimaActividad());
        this.prepared_statement.setString(i++, this.persona.getusuarioCreacion());
        this.prepared_statement.setString(i++, this.persona.getusuarioModificacion());
    }

    @Override
    protected void incluirValorDeParametrosParaModificacion() throws SQLException {
        int i = 1;
        this.prepared_statement.setInt(i++, this.persona.getEstadoPersona().getEstadoPersonaId());
        this.prepared_statement.setString(i++, this.persona.getNombres());
        this.prepared_statement.setString(i++, this.persona.getPrimerApellido());
        this.prepared_statement.setString(i++, this.persona.getSegundoApellido());
        this.prepared_statement.setString(i++, this.persona.getCodigo());
        this.prepared_statement.setString(i++, this.persona.getCorreo());
        this.prepared_statement.setString(i++, this.persona.getContrasena());
        this.prepared_statement.setString(i++, this.persona.getUltimaActividad());
        this.prepared_statement.setString(i++, this.persona.getusuarioCreacion());
        this.prepared_statement.setString(i++, this.persona.getusuarioModificacion());
        this.prepared_statement.setInt(i++, this.persona.getPersonaId());
    }

    @Override
    protected void incluirValorDeParametrosParaInsercion() throws SQLException {
        // Usamos nombres de columnas en lugar de índices i++
        this.asignarParametro("email", this.usuario.getEmail());
        this.asignarParametro("contrasena", this.usuario.getContrasena());
        this.asignarParametro("nombres", this.usuario.getNombres());
        this.asignarParametro("apellidos", this.usuario.getApellidos());
        this.asignarParametro("dni", this.usuario.getDni());
        this.asignarParametro("telefono", this.usuario.getTelefono());
    }

    @Override
    protected void incluirValorDeParametrosParaModificacion() throws SQLException {
        // 1. Columnas a actualizar (SET)
        this.asignarParametro("email", this.usuario.getEmail());
        this.asignarParametro("contrasena", this.usuario.getContrasena());
        this.asignarParametro("nombres", this.usuario.getNombres());
        this.asignarParametro("apellidos", this.usuario.getApellidos());
        this.asignarParametro("dni", this.usuario.getDni());
        this.asignarParametro("telefono", this.usuario.getTelefono());

        // 2. Condición del WHERE (Llave primaria)
        this.asignarParametro("idUsuario", this.usuario.getIdUsuario());
    }




    @Override
    protected void incluirValorDeParametrosParaEliminacion() throws SQLException {
        this.prepared_statement.setInt(1, this.persona.getPersonaId());
    }

    @Override
    protected void incluirValorDeParametrosParaObtenerPorId() throws SQLException {
        this.prepared_statement.setInt(1, this.persona.getPersonaId());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.persona = new PersonaDto();

        EstadoPersonaDto ep = new EstadoPersonaDto();
        ep.setEstadoPersonaId(this.resultSet.getInt("ESTADO_PERSONA_ID_ESTADOPERSONA"));
        this.persona.setEstadoPersona(ep);

        this.persona.setPersonaId(this.resultSet.getInt("PERSONA_ID"));
        this.persona.setNombres(this.resultSet.getString("NOMBRES"));
        this.persona.setPrimerApellido(this.resultSet.getString("PRIMER_APELLIDO"));
        this.persona.setSegundoApellido(this.resultSet.getString("SEGUNDO_APELLIDO"));
        this.persona.setCodigo(this.resultSet.getString("CODIGO"));
        this.persona.setCorreo(this.resultSet.getString("CORREO"));
        this.persona.setContrasena(this.resultSet.getString("CONTRASENA"));
        this.persona.setUltimaActividad(this.resultSet.getString("ULTIMA_ACTIVIDAD"));
        this.persona.setusuarioCreacion(this.resultSet.getString("USUARIO_CREACION"));
        this.persona.setusuarioModificacion(this.resultSet.getString("USUARIO_MODIFICACION"));

        // Sub-consulta de roles
        RolPersonaDao rolDao = new RolPersonaDaoImpl();
        ArrayList<RolPersonaDto> roles = rolDao.listarPorPersona(this.persona.getPersonaId());
        this.persona.setRolPersona(roles);
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.persona = null;
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        this.instanciarObjetoDelResultSet();
        lista.add(this.persona);
    }

    @Override
    public Integer insertar(PersonaDto persona) {
        this.persona = persona;
        return super.insertar();
    }

    @Override
    public PersonaDto obtenerPorId(Integer id) {
        this.persona = new PersonaDto();
        this.persona.setPersonaId(id);
        super.obtenerPorId();
        return this.persona;
    }

    @Override
    public ArrayList<PersonaDto> listarTodos() {
        return (ArrayList<PersonaDto>) super.listarTodos();
    }

    @Override
    public Integer modificar(PersonaDto persona) {
        this.persona = persona;
        return super.modificar();
    }

    @Override
    public Integer eliminar(PersonaDto persona) {
        this.persona = persona;
        return super.eliminar();
    }

    @Override
    public PersonaDto obtenerPorDNI(String dni) {
        String sql = this.generarSQLParaListarTodos() + " WHERE DNI=?";
        Consumer<PreparedStatement> incluir = ps -> {
            try {
                this.statement.setString(1, dni);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        ArrayList lista = (ArrayList) this.listarTodos(sql, incluir, null);
        return (lista == null || lista.isEmpty()) ? null : (PersonaDto) lista.get(0);
    }

    @Override
    public PersonaDto buscarPorCorreo(String correo) {
        String sql = this.generarSQLParaListarTodos() + " WHERE CORREO=?";
        Consumer<PreparedStatement> incluir = ps -> {
            try {
                ps.setString(1, correo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        ArrayList lista = (ArrayList) this.listarTodos(sql, incluir, null);
        return (lista == null || lista.isEmpty()) ? null : (PersonaDto) lista.get(0);
    }

    public void ejecutarReporteCalificaciones(String nombreSP, boolean conTransaccion) {
        String sql = "{call REPORTE_CALIFICACIONES()}";
        this.ejecutarProcedimientoAlmacenado(sql, conTransaccion);
    }

    @Override
    public Boolean existeUsuarioEnBD(PersonaDto per){
        String sql = this.generarSQLParaListarTodos() + " WHERE CODIGO=?";
        Consumer<PreparedStatement> incluir = ps -> {
            try {
                ps.setString(1, per.getCodigo());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        ArrayList lista = (ArrayList) this.listarTodos(sql, incluir, null);
        return (lista != null && !lista.isEmpty());
    }
}
