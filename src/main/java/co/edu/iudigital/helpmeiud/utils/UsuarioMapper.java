package co.edu.iudigital.helpmeiud.utils;

import co.edu.iudigital.helpmeiud.dtos.casos.CasoResponseDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioRequestDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioResponseDto;
import co.edu.iudigital.helpmeiud.models.Caso;
import co.edu.iudigital.helpmeiud.models.Role;
import co.edu.iudigital.helpmeiud.models.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public List<UsuarioResponseDto> toUsuarioResponseDtoList(List<Usuario> usuarios) {
        return usuarios
            .stream()
            .map(usuario -> toUsuarioResponseDto(usuario))
            .collect(Collectors.toList());
    }

    public Usuario toUsuario(UsuarioRequestDto usuarioRequestDto) {
        return Usuario
            .builder()
            .username(usuarioRequestDto.getUsername())
            .nombre(usuarioRequestDto.getNombre())
            .apellido(usuarioRequestDto.getApellido())
            .password(usuarioRequestDto.getPassword())
            .fechaNacimiento(usuarioRequestDto.getFechaNacimiento())
            .enabled(usuarioRequestDto.getEnabled())
            .redSocial(usuarioRequestDto.getRedSocial())
            .image(usuarioRequestDto.getImage())
            .build();
    }

    public UsuarioResponseDto toUsuarioResponseDto(Usuario usuario) {
        List<String> roles = new ArrayList<>();
        for(Role role : usuario.getRoles()) roles.add(role.getNombre().name());
        return UsuarioResponseDto
            .builder()
            .id(usuario.getId())
            .username(usuario.getUsername())
            .nombre(usuario.getNombre())
            .apellido(usuario.getApellido())
            .fechaNacimiento(usuario.getFechaNacimiento())
            .enabled(usuario.getEnabled())
            .redSocial(usuario.getRedSocial())
            .image(usuario.getImage())
            .roles(roles)
            .build();
    }
}
