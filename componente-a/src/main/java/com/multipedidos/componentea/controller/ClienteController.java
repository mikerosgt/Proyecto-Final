package com.multipedidos.componentea.controller;

import com.multipedidos.componentea.dto.ClienteDTO;
import com.multipedidos.componentea.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @PostMapping
    @Operation(summary = "Crear un cliente")
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        log.info("POST /clientes - Creando cliente: {}", clienteDTO.getNombre());
        try {
            ClienteDTO clienteCreado = clienteService.crearCliente(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        log.info("GET /clientes - Listando todos los clientes");
        List<ClienteDTO> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        log.info("GET /clientes/{} - Obteniendo cliente", id);
        try {
            ClienteDTO cliente = clienteService.obtenerClientePorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            log.error("Cliente no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}