package github.alfonsojaen.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "habito", schema = "carbono")
public class Habito {
    @EmbeddedId
    private HabitoId id;

    @MapsId("idUsuario")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private github.alfonsojaen.entities.Usuario idUsuario;

    @MapsId("idActividad")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad idActividad;

    @Column(name = "frecuencia")
    private Integer frecuencia;

    @Lob
    @Column(name = "tipo")
    private String tipo;

    @Column(name = "ultima_fecha")
    private LocalDate ultimaFecha;

    public HabitoId getId() {
        return id;
    }

    public void setId(HabitoId id) {
        this.id = id;
    }

    public github.alfonsojaen.entities.Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(github.alfonsojaen.entities.Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Actividad getIdActividad() {
        return idActividad;
    }

    public Integer getIdUsuarioInt() {
        return idUsuario != null ? idUsuario.getId() : null;
    }
    public void setIdActividad(Actividad idActividad) {
        this.idActividad = idActividad;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Integer frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getUltimaFecha() {
        return ultimaFecha;
    }

    public void setUltimaFecha(LocalDate ultimaFecha) {
        this.ultimaFecha = ultimaFecha;
    }

    @Override
    public String toString() {
        return "Habito{" +
                "Actividad= " + idActividad + "" +
                ", Frecuencia= " + frecuencia + "" +
                ", Tipo= " + tipo + "" +
                ", UltimaFecha= " + ultimaFecha + "" ;
    }
}