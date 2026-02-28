package org.xideral.academy.springbatchmongomockito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Modelo usado en el Step 2: agrega el campo poderTotal para el reporte
// En esta version se persiste como documento en la coleccion "reportes" de MongoDB
@Document(collection = "reportes")
public class HeroeReporte {
    @Id
    private String id;
    private String nombre;
    private String equipo;
    private double poder;
    private double mejoraPoder;
    private double poderTotal;

    public HeroeReporte() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public double getPoder() {
        return poder;
    }

    public void setPoder(double poder) {
        this.poder = poder;
    }

    public double getMejoraPoder() {
        return mejoraPoder;
    }

    public void setMejoraPoder(double mejorPoder) {
        this.mejoraPoder = mejorPoder;
    }

    public double getPoderTotal() {
        return poderTotal;
    }

    public void setPoderTotal(double poderTotal) {
        this.poderTotal = poderTotal;
    }

    @Override
    public String toString() {
        return "HeroeReporte{" + "Id: " + id + ", Nombre: " + nombre + ", Equipo: " + equipo + ", Poder: " + poder + ", Power Up: " + mejoraPoder + ", Poder Total: " + poderTotal + '}';
    }
}