package com.example.trabajofinal_interfaces.modelo;

import java.util.Date;

public class Evento {
    private int id;
    private String nombre,descripcion;
    private String localidad, ubicacion;
    private Date fecha;


    public Evento(String nombre, String descripcion, String localidad, String ubicacion, Date fecha) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.localidad=localidad;
        this.ubicacion=ubicacion;
    }
    public Evento(int id,String nombre, String descripcion, String localidad, String ubicacion, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.localidad=localidad;
        this.ubicacion=ubicacion;
    }

    public Evento(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ubicacion='" + localidad + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
