package com.multipedidos.componentea.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoInputDTO {
    private Long clienteId;
    private List<ItemPedidoInputDTO> productos;
}
