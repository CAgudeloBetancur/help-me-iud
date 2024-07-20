package co.edu.iudigital.helpmeiud.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role implements Serializable {

    static final long serialVersionUID = 1L;

    public Role(Long id) {
        this.id = id;
    }

    public Role() { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true, length = 100)
    @Enumerated(EnumType.STRING)
    RoleEnum nombre;

    @Column
    String descripcion;

    // RELACIÓN BIDIRECCIONAL CON USUARIO
    /*@ManyToMany(mappedBy = "roles")
    @JsonBackReference
    List<Usuario> usuarios;*/
}
