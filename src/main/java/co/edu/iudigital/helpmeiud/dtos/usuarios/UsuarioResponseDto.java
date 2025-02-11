package co.edu.iudigital.helpmeiud.dtos.usuarios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class UsuarioResponseDto {

    Long id;

    String username;

    String nombre;

    String apellido;

    @JsonProperty("fecha_nacimiento")
    LocalDate fechaNacimiento;

    Boolean enabled;

    @JsonProperty("red_social")
    Boolean redSocial;

    String image;

    List<String> roles;
}
