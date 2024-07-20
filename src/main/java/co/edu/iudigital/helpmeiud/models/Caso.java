package co.edu.iudigital.helpmeiud.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "casos")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Caso implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "fecha_hora")
    LocalDateTime fechaHora;
    @Column
    Float latitud;
    @Column
    Float longitud;
    @Column
    Float altitud;
    @Column
    Boolean visible;
    @Column
    String descripcion;
    @Column(name = "url_mapa")
    String urlMapa;
    @Column(name = "rmi_url")
    String rmiUrl;
    // Relación con usuario
    @ManyToOne
    @JoinColumn(name="usuario_id")
    Usuario usuario;
    // Relación con delito
    @ManyToOne
    @JoinColumn(name="delito_id")
    Delito delito;
}
