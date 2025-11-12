package com.multipedidos.componentea.controller;

import com.multipedidos.componentea.dto.ClienteDTO;
import com.multipedidos.componentea.service.ClienteService;
import com.multipedidos.componentea.generated.api.ClientesApi;
import com.multipedidos.componentea.generated.model.Cliente;
import com.multipedidos.componentea.generated.model.ClienteInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClienteController implements ClientesApi {
    
    private final ClienteService clienteService;
    
    @Override
    public ResponseEntity<Cliente> crearCliente(ClienteInput clienteInput) {
        log.info("POST /clientes - Creando cliente: {}", clienteInput.getNombre());
        try {
            ClienteDTO clienteDTO = ClienteDTO.builder()
                    .nombre(clienteInput.getNombre())
                    .correo(clienteInput.getCorreo())
                    .build();
            
            ClienteDTO clienteCreado = clienteService.crearCliente(clienteDTO);
            Cliente response = convertirAOpenAPIModel(clienteCreado);
            
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Override
    public ResponseEntity<List<Cliente>> listarClientes() {
        log.info("GET /clientes - Listando todos los clientes");
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes()
                .stream()
                .map(this::convertirAOpenAPIModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }
    
    @Override
    public ResponseEntity<Cliente> obtenerCliente(Long id) {
        log.info("GET /clientes/{} - Obteniendo cliente", id);
        try {
            ClienteDTO clienteDTO = clienteService.obtenerClientePorId(id);
            Cliente response = convertirAOpenAPIModel(clienteDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Cliente no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    private Cliente convertirAOpenAPIModel(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId().longValue());
        cliente.setNombre(dto.getNombre());
        cliente.setCorreo(dto.getCorreo());
        return cliente;
    }
}