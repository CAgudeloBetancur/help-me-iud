package co.edu.iudigital.helpmeiud.dtos.delitos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Builder
public class DelitoRequestDto {
    @NotNull(message = "Nombre no puede estar vacío")
    String nombre;
    @NotNull(message = "Descripción no puede estar vacía")
    String descripcion;
}
