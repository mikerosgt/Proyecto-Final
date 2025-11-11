package com.multipedidos.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperacionesNegocio {
    
    public static double calcularTotalConIVA(double subtotal) {
        log.info("Calculando total con IVA para subtotal: {}", subtotal);
        return subtotal * 1.12;
    }
    
    public static double aplicarDescuento(double total, double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        log.info("Aplicando descuento del {}% a total: {}", porcentaje, total);
        return total - (total * (porcentaje / 100));
    }
    
    public static double calcularDescuentoAutomatico(double subtotal) {
        double porcentajeDescuento;
        if (subtotal <= 200) {
            porcentajeDescuento = 5.0;
        } else if (subtotal <= 600) {
            porcentajeDescuento = 8.0;
        } else {
            porcentajeDescuento = 10.0;
        }
        log.info("Descuento automático calculado: {}% para subtotal: {}", porcentajeDescuento, subtotal);
        return porcentajeDescuento;
    }
    
    public static boolean validarCodigo(String codigo) {
        boolean valido = codigo != null && codigo.matches("[A-Z]{3}-\\d{4}");
        log.info("Validación código {}: {}", codigo, valido);
        return valido;
    }
    
    public static boolean validarEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}