package com.multipedidos.componentea.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoInputDTO {
    private Long productoId;
    private Integer cantidad;
}
