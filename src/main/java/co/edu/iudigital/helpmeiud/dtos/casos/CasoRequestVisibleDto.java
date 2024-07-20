package co.edu.iudigital.helpmeiud.dtos.casos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CasoRequestVisibleDto implements Serializable {

    static final long serialVersionUID = 1L;

    @NotNull(message = "visible requerido")
    @JsonProperty("visible")
    Boolean visible;
}
