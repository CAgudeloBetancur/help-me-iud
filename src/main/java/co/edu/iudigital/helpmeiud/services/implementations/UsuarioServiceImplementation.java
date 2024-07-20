package co.edu.iudigital.helpmeiud.services.implementations;

import co.edu.iudigital.helpmeiud.dtos.casos.CasoResponseDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioRequestDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioRequestUpdateDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.BadRequestException;
import co.edu.iudigital.helpmeiud.exceptions.ErrorDto;
import co.edu.iudigital.helpmeiud.exceptions.InternalServerErrorException;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.models.Caso;
import co.edu.iudigital.helpmeiud.models.Role;
import co.edu.iudigital.helpmeiud.models.RoleEnum;
import co.edu.iudigital.helpmeiud.models.Usuario;
import co.edu.iudigital.helpmeiud.repositories.IRoleRepository;
import co.edu.iudigital.helpmeiud.repositories.IUsuarioRepository;
import co.edu.iudigital.helpmeiud.services.interfaces.IEmailService;
import co.edu.iudigital.helpmeiud.services.interfaces.IUsuarioService;
import co.edu.iudigital.helpmeiud.utils.UsuarioMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UsuarioServiceImplementation implements IUsuarioService, UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public List<UsuarioResponseDto> consultarUsuarios() throws RestException {
        log.info("consultarUsuarios UsuarioServiceImplementation");
        try {
            final List<Usuario> usuarios = usuarioRepository.findAll();
            final List<UsuarioResponseDto> usuarioResponseDtoList = usuarioMapper.toUsuarioResponseDtoList(usuarios);
            return usuarioResponseDtoList;
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
    public UsuarioResponseDto registrar(UsuarioRequestDto usuarioRequestDto) throws RestException {
        boolean usuarioExiste = usuarioRepository.existsByUsername(usuarioRequestDto.getUsername());
        if(usuarioExiste) {
            throw new BadRequestException(
                ErrorDto
                    .builder()
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message("Usuario existente")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .date(LocalDateTime.now())
                    .build()
            );
        }
        try {
            Usuario usuario = usuarioMapper.toUsuario(usuarioRequestDto);
            Role roleUser = roleRepository.findByNombre(RoleEnum.USER);
            usuario.setRoles(Collections.singleton(roleUser));
            usuario.setEnabled(true);
            usuario = usuarioRepository.save(usuario);
            if(usuario != null) {
                if(emailService.sendMail(
                    "Hola " +
                    usuario.getNombre() +
                    " Te has registrado en HelpMeIUD con el usuario " +
                    usuario.getUsername(),
                    usuario.getUsername(),
                    "Registro exitoso"
                )) {
                    log.info("Mensaje enviado");
                } else {
                    log.info("Mensaje no enviado | algo fallo");
                }
            }
            return usuarioMapper.toUsuarioResponseDto(usuario);
        } catch(Exception e) {
            throw new InternalServerErrorException(
                ErrorDto
                    .builder()
                    .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error general")
                    .date(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public UsuarioResponseDto consultarPorId(Long id) throws RestException {
        return null;
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepository
            .findByUsername(username)
            .orElseThrow(
                () -> {
                    log.error("Usuario no encontrado: " + username);
                    return new UsernameNotFoundException("El usuario " + username + " no existe");
                }
            );
    }

    @Override
    public UsuarioResponseDto consultarPorUsername(Authentication authentication) throws RestException {
        if(!authentication.isAuthenticated()){
            // Lanzar error para autenticación
        }
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(
                () -> {
                    log.error("Usuario no encontrado: " + username);
                    return new UsernameNotFoundException("El usuario " + username + " no existe");
                }
            );
        return usuarioMapper.toUsuarioResponseDto(usuario);
    }

    @Override
    public UsuarioResponseDto editar(
        UsuarioRequestUpdateDto usuarioRequestUpdateDto,
        Authentication authentication) throws RestException {

        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(
            () -> new BadRequestException(
                ErrorDto
                    .builder()
                    .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Usuario no existe")
                    .status(HttpStatus.NOT_FOUND.value())
                    .date(LocalDateTime.now())
                    .build()
            )
        );
        usuario.setNombre(
            usuarioRequestUpdateDto.getNombre() != null
                ? usuarioRequestUpdateDto.getNombre()
                : usuario.getNombre()
        );
        usuario.setApellido(
            usuarioRequestUpdateDto.getApellido() != null
                ? usuarioRequestUpdateDto.getApellido()
                : usuario.getApellido()
        );
        usuario.setPassword(
            usuarioRequestUpdateDto.getPassword() != null
                ? usuarioRequestUpdateDto.getPassword()
                : usuario.getPassword()
        );
        usuario.setFechaNacimiento(
            usuarioRequestUpdateDto.getFechaNacimiento() != null
                ? usuarioRequestUpdateDto.getFechaNacimiento()
                : usuario.getFechaNacimiento()
        );
        usuario = usuarioRepository.save(usuario); // Try Catch
        return usuarioMapper.toUsuarioResponseDto(usuario);
    }

    @Override
    public UsuarioResponseDto subirImagen(
            MultipartFile imagen,
            Authentication authentication
    ) throws RestException {
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName())
            .orElseThrow(
                () -> new BadRequestException(
                    ErrorDto
                        .builder()
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message("Usuario no existe")
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()
                )
            );
        if(!imagen.isEmpty()) {
            String nombreImage = UUID
                .randomUUID()
                .toString()
                .concat("_")
                .concat(imagen.getOriginalFilename())
                .replace(" ", "");
            Path path = Paths.get("uploads").resolve(nombreImage).toAbsolutePath();
            try {
                Files.copy(imagen.getInputStream(), path);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new BadRequestException(
                    ErrorDto
                        .builder()
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Error subiendo imagen")
                        .date(LocalDateTime.now())
                        .build()
                );
            }
            String imagenAnterior = usuario.getImage();
            if(
                imagenAnterior != null &&
                imagenAnterior.length() > 0 &&
                !imagenAnterior.startsWith(nombreImage)
            ) {
                Path pathAnterior = Paths
                    .get("uploads")
                    .resolve(imagenAnterior)
                    .toAbsolutePath();
                File fileAnterior = pathAnterior.toFile();
                if(fileAnterior.exists() && fileAnterior.canRead()) {
                    fileAnterior.delete();
                }
            }
            usuario.setImage(nombreImage);
            usuarioRepository.save(usuario);
        }
        return usuarioMapper.toUsuarioResponseDto(usuario);
    }

    @Override
    public UrlResource obtenerImagen(String name) throws RestException {
        Path path = Paths.get("uploads").resolve(name).toAbsolutePath();
        UrlResource resource = null;
        try {
            resource = new UrlResource(path.toUri());
            if(!resource.exists()) {
                path = Paths.get("uploads").resolve("default.jpg").toAbsolutePath();
                resource = new UrlResource(path.toUri());
            }
        } catch(MalformedURLException e) {
            log.error(e.getMessage());
        }
        return resource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository
            .findByUsername(username)
            .orElseThrow(
                () -> {
                    log.error("Usuario no encontrado: " + username);
                    return new UsernameNotFoundException("El usuario " + username + " no existe");
                }
            );
        List<GrantedAuthority> authorities = new ArrayList<>();
        usuario
            .getRoles()
            .forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getNombre().name())));
            });
        return new User(
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.getEnabled(),
            true,
            true,
            true,
            authorities
        );
    }
}
