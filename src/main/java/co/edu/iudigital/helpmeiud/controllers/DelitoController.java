package co.edu.iudigital.helpmeiud.controllers;

import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoRequestDto;
import co.edu.iudigital.helpmeiud.dtos.delitos.DelitoResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.services.interfaces.IDelitoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Delitos Controller", description = "Controlador para administrar delitos")
@RestController
@RequestMapping("/delitos")
public class DelitoController {

    @Autowired
    private IDelitoService delitoService;

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(summary = "Crea un Delito", description = "Crea un Delito")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<DelitoResponseDto> create(
        @Valid @RequestBody DelitoRequestDto delito,
        Authentication authentication
    ) throws RestException {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(delitoService.crearDelito(delito, authentication));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(summary = "Editar delito", description = "Edita un delito")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public ResponseEntity<DelitoResponseDto> update(
        @RequestBody DelitoRequestDto delito,
        @PathVariable(value = "id") Long id
    ) throws RestException {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(delitoService.editarDelito(id, delito));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(summary = "Eliminar un delito", description = "Elimina un delito por id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) throws RestException {
        delitoService.eliminarDelito(id);
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
    @Operation(summary = "Consulta un delito", description = "Consulta un delito por id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<DelitoResponseDto> getOne(@PathVariable(value = "id") Long id) throws RestException {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(delitoService.obtenerDelitoPorId(id));
    }

    @PreAuthorize("hasRole('USER')")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @Operation(
            summary = "Consulta todos los delitos",
            description = "Devuelve una lista con todos los delitos"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<DelitoResponseDto>> index() throws RestException {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(delitoService.obtenerDelitos());
    }
}
