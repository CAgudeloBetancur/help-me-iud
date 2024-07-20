package co.edu.iudigital.helpmeiud.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Usuario implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, length = 120, updatable = false)
    String username;
    @Column(length = 120, nullable = false)
    String nombre;
    @Column(length = 120)
    String apellido;
    @Column
    String password;
    @Column
    Boolean enabled;
    @Column(name = "fecha_nacimiento")
    LocalDate fechaNacimiento;
    @Column(name = "red_social")
    Boolean redSocial;
    @Column
    String image;
    @ManyToMany(fetch = FetchType.EAGER,targetEntity = Role.class, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "roles_usuarios",
        joinColumns = {@JoinColumn(name="usuario_id")},
        inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    Set<Role> roles;
}
