package co.edu.iudigital.helpmeiud.controllers;

import co.edu.iudigital.helpmeiud.dtos.casos.CasoRequestDto;
import co.edu.iudigital.helpmeiud.dtos.casos.CasoRequestUpdateDto;
import co.edu.iudigital.helpmeiud.dtos.casos.CasoRequestVisibleDto;
import co.edu.iudigital.helpmeiud.dtos.casos.CasoResponseDto;
import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.services.interfaces.ICasoService;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GenericResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Caso Controller", description = "Controlador para administrar casos")
@RestController
@RequestMapping("/casos")
@Slf4j
public class CasoController {

    @Autowired
    private ICasoService casoService;
    @Autowired
    private GenericResponseService responseBuilder;

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(
        summary = "Consultar todos los casos",
        description = "Endpoint para consultar todos los casos"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<CasoResponseDto>> index() throws RestException {
        log.info("Consultando todos los casos con el index en CasoController");
        return ResponseEntity.status(HttpStatus.OK).body(casoService.consultarCasos());
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(
        summary = "Consultar todos los casos visibles",
        description = "Endpoint para consultar todos los casos visibles"
    )
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/visibles")
    public ResponseEntity<List<CasoResponseDto>> indexVisible() throws RestException {
        log.info("Ejecutando indexVisible en CasoController");
        return ResponseEntity.status(HttpStatus.OK).body(casoService.consultarCasosVisibles());
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(
        summary = "Crear un caso",
        description = "Endpoint para crear un caso"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CasoResponseDto> create(
        @RequestBody CasoRequestDto caso, Authentication authentication
    ) throws RestException {
        log.info("Ejecutando create en CasoController");
        return ResponseEntity.status(HttpStatus.OK).body(casoService.crearCaso(caso, authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(
            summary = "Editar un caso",
            description = "Endpoint para editar un caso"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping
    public ResponseEntity<Boolean> updateVisible(
        @PathVariable Long id,
        @RequestBody CasoRequestVisibleDto request
    ) throws RestException {
        log.info("Ejecutando updateVisible en CasoController");
        final Boolean visible = request.getVisible();
        return ResponseEntity.status(HttpStatus.CREATED).body(casoService.visibilizarCaso(visible, id));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(
        summary = "Consultar un caso por username",
        description = "Endpoint para consultar un caso por username"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/user")
    public ResponseEntity<List<CasoResponseDto>> consultarCasosPorUsuario(
        Authentication authentication
    ) throws RestException {
        log.info("Ejecutando consultarCasosPorUsuario en CasoController");
        return ResponseEntity.status(HttpStatus.OK).body(casoService.consultarCasosPorUsuario(authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(summary = "Consulta un caso por id", description = "Consulta un caso por id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<CasoResponseDto> getOne(@PathVariable(value = "id") Long id) throws RestException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(casoService.obtenerCasoPorId(id));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(summary = "Consulta un caso por id", description = "Consulta un caso por id")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<CasoResponseDto> update(
        @RequestBody CasoRequestUpdateDto caso,
        @PathVariable(value = "id") Long id
    ) throws RestException {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(casoService.editarCaso(caso, id));
    }
}
