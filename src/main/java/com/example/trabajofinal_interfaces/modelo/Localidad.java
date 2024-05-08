package com.example.trabajofinal_interfaces.modelo;

import javafx.scene.image.Image;

public class Localidad {
    private String nombre,provincia;
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

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
    public Localidad(String nombre, String provincia, Image image) {
        this.nombre = nombre;
        this.provincia = provincia;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Localidad{" +
                "nombre='" + nombre + '\'' +
                ", provincia='" + provincia + '\'' +
                '}';
    }
}
