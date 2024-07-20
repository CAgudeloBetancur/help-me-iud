package co.edu.iudigital.helpmeiud.controllers;

import co.edu.iudigital.helpmeiud.dtos.casos.CasoResponseDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioRequestDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioRequestUpdateDto;
import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.services.interfaces.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URLConnection;
import java.util.List;

@Tag(name = "Usuario Controller", description = "Controlador para gestión de Usuarios")
@RestController
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @Operation(
            summary = "Consultar todos los usuarios",
            description = "Endpoint para consultar todos los usuarios"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> index() throws RestException {
        log.info("Consultando todos los usuarios con el index en UsuarioController");
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.consultarUsuarios());
    }

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(
        summary = "SignUp en HelpMeIUD",
        description = "Endpoint para registrarse en HelpMeIUD"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<UsuarioResponseDto> register(
        @Valid @RequestBody UsuarioRequestDto request
    ) throws RestException {
        log.info("Ejecutando register UsuarioController");
        final String passwordEncoded = passwordEncoder.encode(request.getPassword());
        request.setPassword(passwordEncoded);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuarioService.registrar(request));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @Operation(
        summary = "Consultar mi perfil",
        description = "Endpoint para consultar usuario de un usuario autenticado"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/profile")
    public ResponseEntity<UsuarioResponseDto> profile(
            Authentication authentication
    ) throws RestException {
        log.info("Consultando el profile UsuarioController");
        return ResponseEntity
            .ok(usuarioService.consultarPorUsername(authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @Operation(
        summary = "Actualizar informacion",
        description = "Endpoint para actualizar información de usuario autenticado"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/profile")
    public ResponseEntity<UsuarioResponseDto> updateProfile(
        @Valid @RequestBody UsuarioRequestUpdateDto request,
        Authentication authentication
    ) throws RestException {
        log.info("Actualizando el profile UsuarioController");
        if(request.getPassword() != null) {
            final String passwordEncoded = passwordEncoder.encode(request.getPassword());
            request.setPassword(passwordEncoded);
        }
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuarioService.editar(request, authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @Operation(
        summary = "Subir foto de perfil",
        description = "Endpoint para subir la foto de perfil de un usuario autenticado"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload")
    public ResponseEntity<UsuarioResponseDto> uploadImage(
        @RequestParam("image") MultipartFile image,
        Authentication authentication
    ) throws RestException {
        log.info("upload image con Usuariocontroller");
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuarioService.subirImagen(image, authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @Operation(
        summary = "Obtener imagen de perfil",
        description = "Endpoint para obtener la foto de perfil de un usuario autenticado"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/upload/img/{name:.+}")
    public ResponseEntity<UrlResource> getImage(
        @PathVariable String name
    ) throws RestException {
        log.info("Obtener imagen con getImage en UsuarioController");
        UrlResource resource = usuarioService.obtenerImagen(name);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());
        String mimeType = URLConnection.guessContentTypeFromName(resource.getFilename());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        headers.add(HttpHeaders.CONTENT_TYPE, mimeType);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(summary = "Consulta un usuario por id", description = "Consulta un usuario por id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getOne(@PathVariable(value = "id") Long id) throws RestException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.consultarPorId(id));
    }

}
