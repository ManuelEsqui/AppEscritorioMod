package com.example.trabajofinal_interfaces.modelo;

public class Localidad {
    private String nombre,provincia;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Localidad(String nombre, String provincia) {
        this.nombre = nombre;
        this.provincia = provincia;

    }

    @Override
    public String toString() {
        return "Localidad{" +
                "nombre='" + nombre + '\'' +
                ", provincia='" + provincia + '\'' +
                '}';
    }
}
