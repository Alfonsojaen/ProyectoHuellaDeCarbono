package github.alfonsojaen.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "actividad", schema = "carbono")
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private github.alfonsojaen.entities.Categoria idCategoria;

    @OneToMany(mappedBy = "idActividad")
    private Set<github.alfonsojaen.entities.Habito> habitos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idActividad")
    private Set<github.alfonsojaen.entities.Huella> huellas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public github.alfonsojaen.entities.Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(github.alfonsojaen.entities.Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Set<github.alfonsojaen.entities.Habito> getHabitos() {
        return habitos;
    }

    public void setHabitos(Set<github.alfonsojaen.entities.Habito> habitos) {
        this.habitos = habitos;
    }

    public Set<github.alfonsojaen.entities.Huella> getHuellas() {
        return huellas;
    }

    public void setHuellas(Set<github.alfonsojaen.entities.Huella> huellas) {
        this.huellas = huellas;
    }

}