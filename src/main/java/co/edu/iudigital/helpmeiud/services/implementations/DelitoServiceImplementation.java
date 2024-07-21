package co.edu.iudigital.helpmeiud.services.implementations;

import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoRequestDto;
import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.*;
import co.edu.iudigital.helpmeiud.models.Delito;
import co.edu.iudigital.helpmeiud.models.Usuario;
import co.edu.iudigital.helpmeiud.repositories.IDelitoRepository;
import co.edu.iudigital.helpmeiud.repositories.IUsuarioRepository;
import co.edu.iudigital.helpmeiud.services.interfaces.IDelitoService;
import co.edu.iudigital.helpmeiud.services.interfaces.IUsuarioService;
import co.edu.iudigital.helpmeiud.utils.DelitoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DelitoServiceImplementation implements IDelitoService {

    @Autowired
    private IDelitoRepository delitoRepository;

    @Autowired
    private DelitoMapper delitoMapper;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public DelitoResponseDto crearDelito(
        DelitoRequestDto delito,
        Authentication authentication
    ) throws RestException {
        log.info("Creando delito con el DelitoService");
        String username = authentication.getName();
        Usuario usuario = usuarioRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new NotFoundException(
                    ErrorDto
                        .builder()
                        .error("No encontrado")
                        .message("Usuario no existe")
                        .status(400)
                        .date(LocalDateTime.now())
                        .build()
                )
            );
        try {
            Delito delitoEntity = Delito
                .builder()
                .nombre(delito.getNombre())
                .descripcion(delito.getDescripcion())
                .usuario(usuario)
                .build();
            delitoEntity = delitoRepository.save(delitoEntity);
            return delitoMapper.toDelitoResponseDto(delitoEntity);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error("Conflicto")
                    .status(409)
                    .message("Nombre del delito ya existe")
                    .date(LocalDateTime.now())
                    .build()
            );
        } catch (Exception e) {
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error("Error General")
                    .status(409)
                    .message(e.getMessage())
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public DelitoResponseDto editarDelito(Long id, DelitoRequestDto delito) throws RestException {
        log.info("Editando delito con el DelitoService");
        Delito delitoExistente = delitoRepository
            .findById(id)
            .orElseThrow( () -> {
                return new NotFoundException(
                    ErrorDto
                        .builder()
                        .error("No encontrado")
                        .message("Delito no existe")
                        .status(400)
                        .date(LocalDateTime.now())
                        .build()
                );
            });
        if(!delito.getNombre().equals(delitoExistente.getNombre())) {
            delitoExistente.setNombre(delito.getNombre());
        }
        delitoExistente.setDescripcion(delito.getDescripcion());
        try {
            return delitoMapper.toDelitoResponseDto( delitoRepository.save(delitoExistente) );
        } catch (Exception e) {
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error("Error General")
                    .status(500)
                    .message(e.getMessage())
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public void eliminarDelito(Long id) throws RestException {
        log.info("eliminarDelito - DelitoService");
        try {
            delitoRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(
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
    public List<DelitoResponseDto> obtenerDelitos() throws RestException {
        log.info("obtenerDelitos - DelitoService");
        try {
            return delitoMapper.toDelitoResponseDtoList( delitoRepository.findAll() );
        } catch (Exception e) {
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
    public DelitoResponseDto obtenerDelitoPorId(Long id) throws RestException {
        log.info("obtenerDelitoPorId - DelitoService");
        Delito delitoEntity = delitoRepository
            .findById(id)
            .orElseThrow( () -> {
                return new NotFoundException(
                    ErrorDto
                        .builder()
                        .error("No encontrado")
                        .message("Delito no existe")
                        .status(400)
                        .date(LocalDateTime.now())
                        .build()
                );
            });
        return delitoMapper.toDelitoResponseDto(delitoEntity);
    }
}
