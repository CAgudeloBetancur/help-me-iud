package co.edu.iudigital.helpmeiud.services.implementations;

import co.edu.iudigital.helpmeiud.dtos.casos.CasoRequestDto;
import co.edu.iudigital.helpmeiud.dtos.casos.CasoRequestUpdateDto;
import co.edu.iudigital.helpmeiud.dtos.casos.CasoResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.ErrorDto;
import co.edu.iudigital.helpmeiud.exceptions.InternalServerErrorException;
import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.models.Caso;
import co.edu.iudigital.helpmeiud.models.Delito;
import co.edu.iudigital.helpmeiud.models.Usuario;
import co.edu.iudigital.helpmeiud.repositories.ICasoRepository;
import co.edu.iudigital.helpmeiud.repositories.IDelitoRepository;
import co.edu.iudigital.helpmeiud.repositories.IUsuarioRepository;
import co.edu.iudigital.helpmeiud.services.interfaces.ICasoService;
import co.edu.iudigital.helpmeiud.utils.CasoMapper;
import co.edu.iudigital.helpmeiud.utils.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CasoServiceImplementation implements ICasoService {

    @Autowired
    private ICasoRepository casoRepository;

    @Autowired
    private CasoMapper casoMapper;

    @Autowired
    private IDelitoRepository delitoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public List<CasoResponseDto> consultarCasos() throws RestException {
        log.info("consultarCasos CasoServiceImplementation");
        try {
            final List<Caso> casos = casoRepository.findAll();
            final List<CasoResponseDto> casoResponseDtoList = casoMapper.toCasoResponseDtoList(casos);
            return casoResponseDtoList;
        }catch (Exception e) {
            log.error("Error consultado casos: {}", e.getMessage());
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error("Error general")
                    .status(500)
                    .message(e.getMessage())
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public List<CasoResponseDto> consultarCasosVisibles(Authentication authentication, boolean visible) throws RestException {
        log.info("consultarCasosVisibles CasoServiceImplementation");
        try {
            String username = authentication.getName();
            final List<Caso> casos = casoRepository.findAllByUsuarioUsernameAndVisible(username,visible);
            final List<CasoResponseDto> casoResponseDtoList = casoMapper.toCasoResponseDtoList(casos);
            return casoResponseDtoList;
        }catch (Exception e) {
            log.error("Error consultado casos visibles: {}", e.getMessage());
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error("Error general")
                    .message(e.getMessage())
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public CasoResponseDto crearCaso(
        CasoRequestDto caso,
        Authentication authentication
    ) throws RestException {
        log.info("crearCaso CasoServiceImplementation");
        String username = authentication.getName();
        final Delito delitoExistente = delitoRepository.findById(caso.getDelitoId())
            .orElseThrow(() -> {
                log.error("Error al consultar delito: {}", caso.getDelitoId());
                return new NotFoundException(
                    ErrorDto
                        .builder()
                        .message("Delito no encontrado")
                        .error(Messages.NO_ENCONTRADO)
                        .status(404)
                        .date(LocalDateTime.now())
                        .build()

                );
            });
        Usuario usuarioExistente = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.error("Error al consultar usuario: {}", username);
                return new NotFoundException(
                    ErrorDto
                        .builder()
                        .error("Usuario no encontrado")
                        .message("Usuario no existe")
                        .status(404)
                        .date(LocalDateTime.now())
                        .build()
                );
            });
        try {
            Caso casoEntity = Caso
                .builder()
                .fechaHora(caso.getFechaHora())
                .latitud(caso.getLatitud())
                .longitud(caso.getLongitud())
                .altitud(caso.getAltitud())
                .visible(true)
                .descripcion(caso.getDescripcion())
                .urlMapa(caso.getUrlMapa())
                .rmiUrl(caso.getRmiUrl())
                .usuario(usuarioExistente)
                .delito(delitoExistente)
                .build();

            casoEntity = casoRepository.save(casoEntity);
            return casoMapper.toCasoResponseDto(casoEntity);
        } catch (Exception e) {
            log.error("Error al crear caso: {}", e.getMessage());
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error("Error General")
                    .status(500)
                    .message("No se ha creado el caso")
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public Boolean visibilizarCaso(Boolean visible, Long id) throws RestException {
        log.info("visibilizarCaso CasoServiceImplementation");
        Caso casoExistente = casoRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Error al consultar caso: {}", id);
                return new NotFoundException(
                    ErrorDto
                        .builder()
                        .error(Messages.NO_ENCONTRADO)
                        .message("Caso no existe")
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()
                );
            });
        try {
            casoExistente.setVisible(visible);
            casoExistente = casoRepository.saveAndFlush(casoExistente);
            return casoExistente != null;
        } catch(Exception e) {
            log.error("Error al consultar caso: {}", e.getMessage());
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error("Error General")
                    .message("Error al intentar actualizar caso")
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public CasoResponseDto obtenerCasoPorId(Long id) throws RestException {
        log.info("obtenerCasoPorId desde CasoServiceImplementation");
        Caso casoEntity = casoRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException(
                    ErrorDto
                        .builder()
                        .error("No encontrado")
                        .message("Caso no existe")
                        .status(404)
                        .date(LocalDateTime.now())
                        .build()
                )
            );
        return casoMapper.toCasoResponseDto(casoEntity);
    }

    @Override
    public List<CasoResponseDto> consultarCasosPorUsuario(Authentication authentication) throws RestException {
        log.info("obtenerCasoPorId desde CasoServiceImplementation");
        String username = authentication.getName();
        try {
            List<Caso> casos = casoRepository.findAllByUsuarioUsername(username);
            return casoMapper.toCasoResponseDtoList(casos);
        } catch (Exception e) {
            log.error("Error consultar casos por username: {}", e.getMessage());
            throw  new NotFoundException(
                ErrorDto
                    .builder()
                    .error("No encontrado")
                    .message("Delito no existe")
                    .status(400)
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public CasoResponseDto editarCaso(CasoRequestUpdateDto caso, Long id) throws RestException {
        log.info("Editando caso desde CasoServiceImplementation");
        Caso casoExistente = casoRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException(
                    ErrorDto
                        .builder()
                        .error("No encontrado")
                        .message("Caso no existe")
                        .status(400)
                        .date(LocalDateTime.now())
                        .build()
                )
            );
        casoExistente.setFechaHora(caso.getFechaHora());
        casoExistente.setLatitud(caso.getLatitud());
        casoExistente.setLongitud(caso.getLongitud());
        casoExistente.setAltitud(caso.getAltitud());
        casoExistente.setDescripcion(caso.getDescripcion());
        Delito delitoExistente = delitoRepository
            .findById(caso.getDelitoId())
            .orElseThrow(
                () -> new NotFoundException(
                    ErrorDto
                        .builder()
                        .error("No encontrado")
                        .message("Delito no existe")
                        .status(400)
                        .date(LocalDateTime.now())
                        .build()
                )
            );
        casoExistente.setDelito(delitoExistente);
        return casoMapper.toCasoResponseDto(casoRepository.save(casoExistente));
    }
}
