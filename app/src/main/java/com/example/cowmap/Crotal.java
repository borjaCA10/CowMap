package com.example.cowmap;

import java.io.Serializable;

public class Crotal implements Serializable {

    public String nombre;
    public String crotal;

    Crotal(String nombre, String crotal) {
        this.nombre = nombre;
        this.crotal = crotal;
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

    @Override
    public String toString() {
        return
                crotal;
    }
}
