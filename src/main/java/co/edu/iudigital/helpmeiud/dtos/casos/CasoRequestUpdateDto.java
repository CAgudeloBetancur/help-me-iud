package co.edu.iudigital.helpmeiud.dtos.casos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Builder
public class CasoRequestUpdateDto {
    @JsonProperty("fecha_hora")
    LocalDateTime fechaHora;
    Float latitud;
    Float longitud;
    Float altitud;
    String descripcion;
    @JsonProperty("delito_id")
    @NotNull(message = "Delito id no puede estar vacio")
    Long delitoId;
}
