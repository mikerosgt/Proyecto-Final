package com.multipedidos.componentea.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedido> items;
    
    @Column(nullable = false)
    private Double subtotal;
    
    @Column(nullable = false)
    private Double descuento;
    
    @Column(nullable = false)
    private Double total;
    
    @Column(name = "fecha_creacion")
    private java.time.LocalDateTime fechaCreacion;
    
    @Column(name = "estado")
    private String estado;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = java.time.LocalDateTime.now();
        if (estado == null) {
            estado = "PENDIENTE";
        }
    }
}