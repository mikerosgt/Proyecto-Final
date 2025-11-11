package com.multipedidos.componentea.service;

import com.multipedidos.componentea.dto.ClienteDTO;
import com.multipedidos.componentea.model.Cliente;
import com.multipedidos.componentea.repository.ClienteRepository;
import com.multipedidos.common.OperacionesNegocio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        log.info("Creando nuevo cliente: {}", clienteDTO.getNombre());
        
        if (!OperacionesNegocio.validarEmail(clienteDTO.getCorreo())) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }
        
        if (clienteRepository.existsByCorreo(clienteDTO.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un cliente con este correo");
        }
        
        Cliente cliente = Cliente.builder()
                .nombre(clienteDTO.getNombre())
                .correo(clienteDTO.getCorreo())
                .build();
        
        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getId());
        
        return convertirADTO(clienteGuardado);
    }
    
    public List<ClienteDTO> obtenerTodosLosClientes() {
        log.info("Obteniendo todos los clientes");
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public ClienteDTO obtenerClientePorId(Long id) {
        log.info("Obteniendo cliente con ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return convertirADTO(cliente);
    }
    
    private ClienteDTO convertirADTO(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .correo(cliente.getCorreo())
                .fechaCreacion(cliente.getFechaCreacion() != null ? 
                    cliente.getFechaCreacion().toString() : null)
                .build();
    }
}