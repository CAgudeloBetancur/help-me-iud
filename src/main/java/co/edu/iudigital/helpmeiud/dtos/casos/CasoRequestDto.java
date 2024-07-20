package co.edu.iudigital.helpmeiud.dtos.casos;

import co.edu.iudigital.helpmeiud.models.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class CasoRequestDto {

    @JsonProperty("fecha_hora")
    LocalDateTime fechaHora;
    Float latitud;
    Float longitud;
    Float altitud;
    String descripcion;
    @JsonProperty("url_mapa")
    String urlMapa;
    @JsonProperty("rmi_url")
    String rmiUrl;
    @JsonProperty("delito_id")
    @NotNull(message = "No puede estar vacío")
    Long delitoId;

}
