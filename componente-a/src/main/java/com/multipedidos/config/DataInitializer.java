package com.multipedidos.componentea.config;

import com.multipedidos.componentea.model.Producto;
import com.multipedidos.componentea.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ProductoRepository productoRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando datos de prueba...");
        
        if (productoRepository.count() == 0) {
            Producto p1 = Producto.builder()
                    .nombre("Laptop Gaming")
                    .precio(1200.00)
                    .descripcion("Laptop para gaming de alta gama")
                    .build();
            
            Producto p2 = Producto.builder()
                    .nombre("Mouse Inalámbrico")
                    .precio(25.50)
                    .descripcion("Mouse ergonómico inalámbrico")
                    .build();
            
            Producto p3 = Producto.builder()
                    .nombre("Teclado Mecánico")
                    .precio(89.99)
                    .descripcion("Teclado mecánico RGB")
                    .build();
            
            Producto p4 = Producto.builder()
                    .nombre("Monitor 24\"")
                    .precio(199.99)
                    .descripcion("Monitor Full HD 24 pulgadas")
                    .build();
            
            productoRepository.saveAll(Arrays.asList(p1, p2, p3, p4));
            log.info("Productos de prueba creados exitosamente");
        }
        
        log.info("Inicialización de datos completada");
    }
}