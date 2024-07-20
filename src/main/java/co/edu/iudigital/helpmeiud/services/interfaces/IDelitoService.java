package co.edu.iudigital.helpmeiud.services.interfaces;

import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoRequestDto;
import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IDelitoService {
    DelitoResponseDto crearDelito(DelitoRequestDto delito, Authentication authentication) throws RestException;
    DelitoResponseDto editarDelito(Long id, DelitoRequestDto delito) throws RestException;
    void eliminarDelito(Long id) throws RestException;
    List<DelitoResponseDto> obtenerDelitos() throws RestException;
    DelitoResponseDto obtenerDelitoPorId(Long id) throws RestException;
}
