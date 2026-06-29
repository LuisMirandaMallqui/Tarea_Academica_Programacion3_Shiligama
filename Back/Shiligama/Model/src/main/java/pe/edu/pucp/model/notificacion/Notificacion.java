package pe.edu.pucp.model.notificacion;

import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.model.enums.ReferenciaNotificacion;

import java.time.LocalDateTime;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Notificacion")
@XmlAccessorType(XmlAccessType.FIELD)
public class Notificacion {
    @XmlElement(name = "idNotificacion")
    @JsonbProperty("idNotificacion")
    private int idNotificacion;
    @XmlElement(name = "titulo")
    @JsonbProperty("titulo")
    private String titulo;
    @XmlElement(name = "mensaje")
    @JsonbProperty("mensaje")
    private String mensaje;
    @XmlElement(name = "tipo")
    @JsonbProperty("tipo")
    private TipoNotificacion tipo;
    @XmlElement(name = "leida")
    @JsonbProperty("leida")
    private boolean leida;
    @XmlElement(name = "fechaCreacion")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaCreacion")
    private LocalDateTime fechaCreacion;
    @XmlElement(name = "idDestinatario")
    @JsonbProperty("idDestinatario")
    private Integer idDestinatario; // null = broadcast a todos los admins/trabajadores

    // ── Referencia a la entidad de la que habla la notificacion ──────────
    // Permite que el front navegue exactamente al pedido/producto/devolucion/
    // promocion del que se trata, en vez de a un listado generico.
    @XmlElement(name = "referenciaTipo")
    @JsonbProperty("referenciaTipo")
    private ReferenciaNotificacion referenciaTipo; // null si no aplica
    @XmlElement(name = "referenciaId")
    @JsonbProperty("referenciaId")
    private Integer referenciaId; // PK de la entidad referenciada, null si no aplica

    public Notificacion() {
    }

    public Notificacion(int idNotificacion, String titulo, String mensaje,
                        TipoNotificacion tipo, boolean leida,
                        LocalDateTime fechaCreacion, Integer idDestinatario) {
        this.idNotificacion = idNotificacion;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.leida = leida;
        this.fechaCreacion = fechaCreacion;
        this.idDestinatario = idDestinatario;
    }

    public Notificacion(int idNotificacion, String titulo, String mensaje,
                        TipoNotificacion tipo, boolean leida,
                        LocalDateTime fechaCreacion, Integer idDestinatario,
                        ReferenciaNotificacion referenciaTipo, Integer referenciaId) {
        this(idNotificacion, titulo, mensaje, tipo, leida, fechaCreacion, idDestinatario);
        this.referenciaTipo = referenciaTipo;
        this.referenciaId = referenciaId;
    }

    public int getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(int idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public TipoNotificacion getTipo() { return tipo; }
    public void setTipo(TipoNotificacion tipo) { this.tipo = tipo; }

    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Integer getIdDestinatario() { return idDestinatario; }
    public void setIdDestinatario(Integer idDestinatario) { this.idDestinatario = idDestinatario; }

    public ReferenciaNotificacion getReferenciaTipo() { return referenciaTipo; }
    public void setReferenciaTipo(ReferenciaNotificacion referenciaTipo) { this.referenciaTipo = referenciaTipo; }

    public Integer getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Integer referenciaId) { this.referenciaId = referenciaId; }
}
