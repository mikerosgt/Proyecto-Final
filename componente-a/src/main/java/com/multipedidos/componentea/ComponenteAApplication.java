package com.multipedidos.componentea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "API Componente A - Clientes y Pedidos",
        version = "1.0.0",
        description = "API para gestión de clientes y pedidos con MariaDB"
    )
)
public class ComponenteAApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ComponenteAApplication.class, args);
    }
}