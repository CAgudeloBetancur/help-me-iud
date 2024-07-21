package co.edu.iudigital.helpmeiud.services.interfaces;

import co.edu.iudigital.helpmeiud.dtos.casos.CasoRequestDto;
import co.edu.iudigital.helpmeiud.dtos.casos.CasoRequestUpdateDto;
import co.edu.iudigital.helpmeiud.dtos.casos.CasoResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ICasoService {
    List<CasoResponseDto> consultarCasos() throws RestException;
    List<CasoResponseDto> consultarCasosVisibles(Authentication authentication, boolean visible) throws RestException;
    CasoResponseDto crearCaso(CasoRequestDto caso, Authentication authentication) throws RestException;
    Boolean visibilizarCaso(Boolean visible, Long id) throws RestException;
    CasoResponseDto obtenerCasoPorId(Long id) throws RestException;
    List<CasoResponseDto> consultarCasosPorUsuario(Authentication authentication) throws RestException;
    CasoResponseDto editarCaso(CasoRequestUpdateDto caso, Long id) throws RestException;
}
