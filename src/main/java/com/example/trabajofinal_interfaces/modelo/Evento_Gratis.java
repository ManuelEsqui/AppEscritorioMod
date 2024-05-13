package com.example.trabajofinal_interfaces.modelo;

import java.util.Date;

public class Evento_Gratis extends Evento{
     private String descripcionAdicional;
     private String tipo;

    public Evento_Gratis(String nombre, String descripcion, String localidad, String ubicacion, Date fecha, String descripcionAdicional, String tipo) {
        super(nombre, descripcion, localidad, ubicacion, fecha);
        this.descripcionAdicional = descripcionAdicional;
        this.tipo = tipo;
    }

    public Evento_Gratis(int id, String nombre) {
        super(id, nombre);
    }

    public Evento_Gratis(int id, String nombre, String descripcion, String localidad, String ubicacion, Date fecha) {
        super(id, nombre, descripcion, localidad, ubicacion, fecha);
    }
    public String getDescripcionAdicional() {
        return descripcionAdicional;
    }
    public void setDescripcionAdicional(String descripcionAdicional) {
        this.descripcionAdicional = descripcionAdicional;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
