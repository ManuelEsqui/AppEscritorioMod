package com.example.trabajofinal_interfaces.modelo;

import java.util.Date;

public class Evento_Pago extends Evento{
    private float precio;
    private String puntoDeVenta;

    public Evento_Pago(Evento e, float precio, String puntoDeVenta) {
        super(e.getNombre(), e.getDescripcion(), e.getLocalidad(), e.getUbicacion(), e.getFecha());
        this.precio = precio;
        this.puntoDeVenta = puntoDeVenta;
    }


    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getPuntoDeVenta() {
        return puntoDeVenta;
    }

    public void setPuntoDeVenta(String puntoDeVenta) {
        this.puntoDeVenta = puntoDeVenta;
    }

    @Override
    public String toString() {
        return "Evento_Pago{" +
                "precio=" + precio +
                ", puntoDeVenta='" + puntoDeVenta + '\'' +
                "} " + super.toString();
    }
}