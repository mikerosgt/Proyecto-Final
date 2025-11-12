package com.multipedidos.componentea.controller;

import com.multipedidos.componentea.dto.PedidoDTO;
import com.multipedidos.componentea.dto.PedidoInputDTO;
import com.multipedidos.componentea.dto.ItemPedidoInputDTO;
import com.multipedidos.componentea.service.PedidoService;
import com.multipedidos.componentea.generated.api.PedidosApi;
import com.multipedidos.componentea.generated.model.Pedido;
import com.multipedidos.componentea.generated.model.PedidoInput;
import com.multipedidos.componentea.generated.model.ItemPedidoInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PedidoController implements PedidosApi {
    
    private final PedidoService pedidoService;
    
    @Override
    public ResponseEntity<Pedido> crearPedido(PedidoInput pedidoInput) {
        log.info("POST /pedidos - Creando pedido para cliente ID: {}", pedidoInput.getClienteId());
        try {
            PedidoInputDTO pedidoInputDTO = convertirDesdeOpenAPI(pedidoInput);
            PedidoDTO pedidoCreado = pedidoService.crearPedido(pedidoInputDTO);
            Pedido response = convertirAOpenAPIModel(pedidoCreado);
            
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            log.error("Error al crear pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Override
    public ResponseEntity<List<Pedido>> listarPedidos() {
        log.info("GET /pedidos - Listando todos los pedidos");
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos()
                .stream()
                .map(this::convertirAOpenAPIModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }
    
    @Override
    public ResponseEntity<Pedido> obtenerPedido(Long id) {
        log.info("GET /pedidos/{} - Obteniendo pedido", id);
        try {
            PedidoDTO pedidoDTO = pedidoService.obtenerPedidoPorId(id);
            Pedido response = convertirAOpenAPIModel(pedidoDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Pedido no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    private PedidoInputDTO convertirDesdeOpenAPI(PedidoInput pedidoInput) {
        List<ItemPedidoInputDTO> items = pedidoInput.getProductos().stream()
                .map(item -> ItemPedidoInputDTO.builder()
                        .productoId(item.getProductoId())
                        .cantidad(item.getCantidad())
                        .build())
                .collect(Collectors.toList());
        
        return PedidoInputDTO.builder()
                .clienteId(pedidoInput.getClienteId())
                .productos(items)
                .build();
    }
    
    private Pedido convertirAOpenAPIModel(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setId(dto.getId().longValue());
        pedido.setClienteId(dto.getClienteId().longValue());
        pedido.setClienteNombre(dto.getClienteNombre());
        pedido.setSubtotal(dto.getSubtotal());
        pedido.setDescuento(dto.getDescuento());
        pedido.setTotal(dto.getTotal());
        pedido.setEstado(dto.getEstado());
        
        // Convertir items del pedido
        if (dto.getProductos() != null) {
            List<com.multipedidos.componentea.generated.model.ItemPedido> items = dto.getProductos().stream()
                    .map(item -> {
                        com.multipedidos.componentea.generated.model.ItemPedido itemModel = 
                            new com.multipedidos.componentea.generated.model.ItemPedido();
                        itemModel.setProductoId(item.getProductoId().longValue());
                        itemModel.setCantidad(item.getCantidad());
                        itemModel.setPrecioUnitario(item.getPrecioUnitario());
                        itemModel.setSubtotal(item.getSubtotal());
                        return itemModel;
                    })
                    .collect(Collectors.toList());
            pedido.setProductos(items);
        }
        
        return pedido;
    }
}