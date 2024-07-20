package co.edu.iudigital.helpmeiud.utils;

import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoResponseDto;
import co.edu.iudigital.helpmeiud.models.Delito;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DelitoMapper {

    public List<DelitoResponseDto> toDelitoResponseDtoList(List<Delito> delitos) {
        return delitos
            .stream()
            .map(delito -> toDelitoResponseDto(delito))
            .collect(Collectors.toList());
    }

    public DelitoResponseDto toDelitoResponseDto(Delito delito) {
        return DelitoResponseDto
            .builder()
            .id(delito.getId())
            .nombre(delito.getNombre())
            .descripcion(delito.getDescripcion())
            .build();
    }

}
