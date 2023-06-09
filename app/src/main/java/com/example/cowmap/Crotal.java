package com.example.cowmap;

import java.io.Serializable;

public class Crotal implements Serializable {

    public String nombre;
    public String crotal;

    public String latitud;

    public String longitud;

    Crotal(String nombre, String crotal, String latitud, String longitud) {
        this.nombre = nombre;
        this.crotal = crotal;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Crotal() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCrotal() {
        return crotal;
    }

    public void setCrotal(String crotal) {
        this.crotal = crotal;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return
                crotal;
    }
}
