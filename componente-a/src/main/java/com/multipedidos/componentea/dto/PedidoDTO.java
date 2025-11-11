package com.multipedidos.componentea.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private List<ItemPedidoDTO> productos;
    private Double subtotal;
    private Double descuento;
    private Double total;
    private String estado;
    private String fechaCreacion;
}