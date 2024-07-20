package co.edu.iudigital.helpmeiud.dtos.delitos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
public class DelitoResponseDto {
    Long id;
    String nombre;
    String descripcion;
}
