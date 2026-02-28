package org.xideral.academy.springbatchmongomockito.model;

public class Heroe {
    private String nombre_hp;
    private String equipo_hp;
    private double poder_hp;
    private double mejoraPoder_hp;

    public Heroe() {
    }

    public String getNombre_hp() {
        return nombre_hp;
    }

    public void setNombre_hp(String nombre_hp) {
        this.nombre_hp = nombre_hp;
    }

    public String getEquipo_hp() {
        return equipo_hp;
    }

    public void setEquipo_hp(String equipo_hp) {
        this.equipo_hp = equipo_hp;
    }

    public double getPoder_hp() {
        return poder_hp;
    }

    public void setPoder_hp(double poder_hp) {
        this.poder_hp = poder_hp;
    }

    public double getMejoraPoder_hp() {
        return mejoraPoder_hp;
    }

    public void setMejoraPoder_hp(double mejoraPoder_hp) {
        this.mejoraPoder_hp = mejoraPoder_hp;
    }

    @Override
    public String toString() {
        return "Heroe{" + "nombre: " + nombre_hp + ", equipo: " + equipo_hp + ", poder base: " + poder_hp + ", power up: " + mejoraPoder_hp + '}';
    }
}
