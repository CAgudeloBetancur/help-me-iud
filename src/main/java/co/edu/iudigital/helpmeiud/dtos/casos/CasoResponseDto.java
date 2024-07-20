package co.edu.iudigital.helpmeiud.dtos.casos;

import co.edu.iudigital.helpmeiud.models.Delito;
import co.edu.iudigital.helpmeiud.models.Usuario;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class CasoResponseDto {

    Long id;
    LocalDateTime fechaHora;
    Float latitud;
    Float longitud;
    Float altitud;
    String descripcion;
    Boolean visible;
    String urlMapa;
    String rmiUrl;
    String username;
    Long delitoId;

}
