package com.multipedidos.componentea.service;

import com.multipedidos.componentea.dto.*;
import com.multipedidos.componentea.model.*;
import com.multipedidos.componentea.repository.*;
import com.multipedidos.common.OperacionesNegocio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    
    @Transactional
    public PedidoDTO crearPedido(PedidoInputDTO pedidoInput) {
        log.info("Creando nuevo pedido para cliente ID: {}", pedidoInput.getClienteId());
        
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(pedidoInput.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + pedidoInput.getClienteId()));
        
        // Crear items del pedido y calcular subtotal
        List<ItemPedido> items = new ArrayList<>();
        double subtotal = 0.0;
        
        for (ItemPedidoInputDTO itemInput : pedidoInput.getProductos()) {
            Producto producto = productoRepository.findById(itemInput.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemInput.getProductoId()));
            
            double subtotalItem = producto.getPrecio() * itemInput.getCantidad();
            subtotal += subtotalItem;
            
            ItemPedido item = ItemPedido.builder()
                    .producto(producto)
                    .cantidad(itemInput.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotalItem)
                    .build();
            
            items.add(item);
        }
        
        // Calcular descuento automático
        double porcentajeDescuento = OperacionesNegocio.calcularDescuentoAutomatico(subtotal);
        double descuento = subtotal * (porcentajeDescuento / 100);
        double totalSinIVA = subtotal - descuento;
        double totalConIVA = OperacionesNegocio.calcularTotalConIVA(totalSinIVA);
        
        log.info("Pedido - Subtotal: {}, Descuento: {}%, Total con IVA: {}", 
                subtotal, porcentajeDescuento, totalConIVA);
        
        // Crear y guardar el pedido
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .items(items)
                .subtotal(subtotal)
                .descuento(descuento)
                .total(totalConIVA)
                .estado("PENDIENTE")
                .build();
        
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        log.info("Pedido creado exitosamente con ID: {}", pedidoGuardado.getId());
        
        return convertirADTO(pedidoGuardado);
    }
    
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        log.info("Obteniendo todos los pedidos");
        return pedidoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public PedidoDTO obtenerPedidoPorId(Long id) {
        log.info("Obteniendo pedido con ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        return convertirADTO(pedido);
    }
    
    public List<PedidoDTO> obtenerPedidosPorCliente(Long clienteId) {
        log.info("Obteniendo pedidos para cliente ID: {}", clienteId);
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<PedidoDTO> obtenerPedidosPendientes() {
        log.info("Obteniendo pedidos pendientes");
        return pedidoRepository.findByEstado("PENDIENTE").stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    private PedidoDTO convertirADTO(Pedido pedido) {
        List<ItemPedidoDTO> itemsDTO = pedido.getItems().stream()
                .map(item -> ItemPedidoDTO.builder()
                        .productoId(item.getProducto().getId())
                        .productoNombre(item.getProducto().getNombre())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());
        
        return PedidoDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getCliente().getId())
                .clienteNombre(pedido.getCliente().getNombre())
                .productos(itemsDTO)
                .subtotal(pedido.getSubtotal())
                .descuento(pedido.getDescuento())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .fechaCreacion(pedido.getFechaCreacion() != null ? 
                    pedido.getFechaCreacion().toString() : null)
                .build();
    }
}