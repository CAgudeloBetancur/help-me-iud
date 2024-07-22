package co.edu.iudigital.helpmeiud.services.interfaces;

import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioRequestDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioRequestUpdateDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.models.Usuario;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<UsuarioResponseDto> consultarUsuarios() throws RestException;
    UsuarioResponseDto registrar(UsuarioRequestDto usuarioRequestDto) throws RestException;
    UsuarioResponseDto consultarPorId(Long id) throws RestException;
    UsuarioResponseDto findByUsername(String username) throws RestException;
    UsuarioResponseDto consultarPorUsername(Authentication authentication) throws RestException;
    UsuarioResponseDto editar(
        UsuarioRequestUpdateDto usuarioRequestUpdateDto,
        Authentication authentication
    ) throws RestException;
    UsuarioResponseDto subirImagen(
        MultipartFile imagen,
        Authentication authentication
    ) throws RestException;
    UrlResource obtenerImagen(String name) throws RestException;
    Boolean habilitarUsuario(Boolean habilitado, Long id) throws RestException;
}
