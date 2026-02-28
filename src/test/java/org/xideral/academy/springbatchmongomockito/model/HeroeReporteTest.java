package org.xideral.academy.springbatchmongomockito.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests basicos del modelo HeroeReporte.
 *
 * HeroeReporte extiende a Heroe con id y poderTotal.
 * Verificamos que todos los campos se almacenan correctamente.
 */
public class HeroeReporteTest {
    @Test
    @DisplayName("Constructor vacio crea reporte con valores por defecto")
    void constructorVacio_sinParametros_valoresPorDefecto() {
        // Arrange & Act
        HeroeReporte reporte = new HeroeReporte();

        // Assert
        assertAll("Estado inicial del reporte",
                () -> assertNull(reporte.getId(), "id debe ser null"),
                () -> assertNull(reporte.getNombre(), "nombre debe ser null"),
                () -> assertNull(reporte.getEquipo(), "equipo debe ser null"),
                () -> assertEquals(0.0, reporte.getPoder(), "poder debe ser 0.0"),
                () -> assertEquals(0.0, reporte.getMejoraPoder(), "mejoraPoder debe ser 0.0"),
                () -> assertEquals(0.0, reporte.getPoderTotal(), "poderTotal debe ser 0.0")
        );
    }

    @Test
    @DisplayName("setId y getId guardan y devuelven el ID")
    void setId_valorValido_retornaMismoValor() {
        // Arrange
        HeroeReporte reporte = new HeroeReporte();

        // Act
        reporte.setId("abc123");

        // Assert
        assertEquals("abc123", reporte.getId());
    }

    @Test
    @DisplayName("setPoderTotal y getPoderTotal guardan y devuelven el total")
    void setPoderTotal_valorValido_retornaMismoValor() {
        // Arrange
        HeroeReporte reporte = new HeroeReporte();

        // Act
        reporte.setPoderTotal(27500.0);

        // Assert
        assertEquals(27500.0, reporte.getPoderTotal());
    }

    @Test
    @DisplayName("Reporte completo tiene todos sus campos correctos")
    void reporteCompleto_todasLasPropiedades_verificaConAssertAll() {
        // Arrange
        HeroeReporte reporte = new HeroeReporte();

        // Act
        reporte.setId("rpt001");
        reporte.setNombre("JUAN PEREZ");
        reporte.setEquipo("Ventas");
        reporte.setPoder(25000.0);
        reporte.setMejoraPoder(2500.0);
        reporte.setPoderTotal(27500.0);

        // Assert
        assertAll("Propiedades del reporte",
                () -> assertEquals("rpt001", reporte.getId()),
                () -> assertEquals("JUAN PEREZ", reporte.getNombre()),
                () -> assertEquals("Ventas", reporte.getEquipo()),
                () -> assertEquals(25000.0, reporte.getPoder()),
                () -> assertEquals(2500.0, reporte.getMejoraPoder()),
                () -> assertEquals(27500.0, reporte.getPoderTotal())
        );
    }

    @Test
    @DisplayName("toString incluye nombre, equipo, poder, mejora y total")
    void toString_reporteCompleto_contieneTodasLasPropiedades() {
        // Arrange
        HeroeReporte reporte = new HeroeReporte();
        reporte.setNombre("MARIA LOPEZ");
        reporte.setEquipo("TI");
        reporte.setPoder(35000.0);
        reporte.setMejoraPoder(3500.0);
        reporte.setPoderTotal(38500.0);

        // Act
        String texto = reporte.toString();

        // Assert
        assertAll("toString contiene los datos",
                () -> assertTrue(texto.contains("MARIA LOPEZ"), "Debe contener el nombre"),
                () -> assertTrue(texto.contains("TI"), "Debe contener el equipo"),
                () -> assertTrue(texto.contains("35000"), "Debe contener el poder"),
                () -> assertTrue(texto.contains("3500"), "Debe contener el mejora"),
                () -> assertTrue(texto.contains("38500"), "Debe contener el total")
        );
    }

    @Test
    @DisplayName("PoderTotal puede ser la suma de poder + mejora")
    void poderTotal_sumaDePoderYMejora_esConsistente() {
        // Arrange
        HeroeReporte reporte = new HeroeReporte();
        double poder = 30000.0;
        double mejora = 3000.0;

        // Act
        reporte.setPoder(poder);
        reporte.setMejoraPoder(mejora);
        reporte.setPoderTotal(poder + mejora);

        // Assert
        assertEquals(poder + mejora, reporte.getPoderTotal(),
                "El total debe ser la suma del poder y la mejora");
    }
}
