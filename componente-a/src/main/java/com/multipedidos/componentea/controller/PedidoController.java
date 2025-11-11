package com.multipedidos.componentea.controller;

import com.multipedidos.componentea.dto.PedidoDTO;
import com.multipedidos.componentea.dto.PedidoInputDTO;
import com.multipedidos.componentea.service.PedidoService;
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
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para gestión de pedidos")
public class PedidoController {
    
    private final PedidoService pedidoService;
    
    @PostMapping
    @Operation(summary = "Crear un pedido")
    public ResponseEntity<PedidoDTO> crearPedido(@RequestBody PedidoInputDTO pedidoInput) {
        log.info("POST /pedidos - Creando pedido para cliente ID: {}", pedidoInput.getClienteId());
        try {
            PedidoDTO pedidoCreado = pedidoService.crearPedido(pedidoInput);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado);
        } catch (RuntimeException e) {
            log.error("Error al crear pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        log.info("GET /pedidos - Listando todos los pedidos");
        List<PedidoDTO> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por ID")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        log.info("GET /pedidos/{} - Obteniendo pedido", id);
        try {
            PedidoDTO pedido = pedidoService.obtenerPedidoPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            log.error("Pedido no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener pedidos por cliente")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPorCliente(@PathVariable Long clienteId) {
        log.info("GET /pedidos/cliente/{} - Obteniendo pedidos del cliente", clienteId);
        List<PedidoDTO> pedidos = pedidoService.obtenerPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/pendientes")
    @Operation(summary = "Obtener pedidos pendientes")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPendientes() {
        log.info("GET /pedidos/pendientes - Obteniendo pedidos pendientes");
        List<PedidoDTO> pedidos = pedidoService.obtenerPedidosPendientes();
        return ResponseEntity.ok(pedidos);
    }
}