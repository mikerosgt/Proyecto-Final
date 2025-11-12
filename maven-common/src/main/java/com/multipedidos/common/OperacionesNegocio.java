package com.multipedidos.common;

/**
 * Clase de utilidades para operaciones de negocio comunes
 * Sin dependencias externas para mayor portabilidad
 */
public class OperacionesNegocio {
    
    public static double calcularTotalConIVA(double subtotal) {
        System.out.println("Calculando total con IVA para subtotal: " + subtotal);
        return subtotal * 1.12;
    }
    
    public static double aplicarDescuento(double total, double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        System.out.println("Aplicando descuento del " + porcentaje + "% a total: " + total);
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
        System.out.println("Descuento automático calculado: " + porcentajeDescuento + "% para subtotal: " + subtotal);
        return porcentajeDescuento;
    }
    
    public static boolean validarCodigo(String codigo) {
        boolean valido = codigo != null && codigo.matches("[A-Z]{3}-\\d{4}");
        System.out.println("Validación código " + codigo + ": " + valido);
        return valido;
    }
    
    public static boolean validarEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
    
    public static String formatearMoneda(double cantidad) {
        return String.format("Q. %.2f", cantidad);
    }
}