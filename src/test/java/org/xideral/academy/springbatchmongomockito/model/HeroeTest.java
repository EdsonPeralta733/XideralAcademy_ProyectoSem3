package org.xideral.academy.springbatchmongomockito.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests basicos del modelo Heroe.
 *
 * Estos tests verifican que los getters, setters y toString
 * funcionan correctamente. No se necesita mock para modelos simples.
 */
public class HeroeTest {
    @Test
    @DisplayName("Constructor vacio crea empleado con valores por defecto")
    void constructorVacio_sinParametros_valoresPorDefecto() {
        // Arrange & Act
        Heroe heroe = new Heroe();

        // Assert
        assertAll("Estado inicial del heroe",
                () -> assertNull(heroe.getNombre_hp(), "nombre debe ser null"),
                () -> assertNull(heroe.getEquipo_hp(), "equipo debe ser null"),
                () -> assertEquals(0.0, heroe.getPoder_hp(), "poder debe ser 0.0"),
                () -> assertEquals(0.0, heroe.getMejoraPoder_hp(), "mejoraPoder debe ser 0.0")
        );
    }

    @Test
    @DisplayName("setNombre_hp y getNombre_hp guardan y devuelven el nombre")
    void setNombre_valorValido_retornaMismoValor() {
        // Arrange
        Heroe heroe = new Heroe();

        // Act
        heroe.setNombre_hp("Juan Perez");

        // Assert
        assertEquals("Juan Perez", heroe.getNombre_hp());
    }

    @Test
    @DisplayName("setEquipo_hp y getEquipo_hp guardan y devuelven el equipo")
    void setDepartamento_valorValido_retornaMismoValor() {
        // Arrange
        Heroe heroe = new Heroe();

        // Act
        heroe.setEquipo_hp("Ventas");

        // Assert
        assertEquals("Ventas", heroe.getEquipo_hp());
    }

    @Test
    @DisplayName("setPoder_hp y getPoder_hp guardan y devuelven el poder")
    void setSalario_valorValido_retornaMismoValor() {
        // Arrange
        Heroe heroe = new Heroe();

        // Act
        heroe.setPoder_hp(25000.0);

        // Assert
        assertEquals(25000.0, heroe.getPoder_hp());
    }

    @Test
    @DisplayName("setMejoraPoder_hp y getMejoraPoder_hp guardan y devuelven la mejora")
    void setBono_valorValido_retornaMismoValor() {
        // Arrange
        Heroe heroe = new Heroe();

        // Act
        heroe.setMejoraPoder_hp(2500.0);

        // Assert
        assertEquals(2500.0, heroe.getMejoraPoder_hp());
    }

    @Test
    @DisplayName("toString incluye nombre, equipo, poder y mejoraPoder")
    void toString_empleadoCompleto_contieneTodasLasPropiedades() {
        // Arrange
        Heroe heroe = new Heroe();
        heroe.setNombre_hp("Juan Perez");
        heroe.setEquipo_hp("Ventas");
        heroe.setPoder_hp(25000.0);
        heroe.setMejoraPoder_hp(2500.0);

        // Act
        String texto = heroe.toString();

        // Assert
        assertAll("toString contiene los datos",
                () -> assertTrue(texto.contains("Juan Perez"), "Debe contener el nombre"),
                () -> assertTrue(texto.contains("Ventas"), "Debe contener el equipo"),
                () -> assertTrue(texto.contains("25000"), "Debe contener el poder"),
                () -> assertTrue(texto.contains("2500"), "Debe contener la mejoraPoder")
        );
    }

    @Test
    @DisplayName("Empleado completo tiene todos sus campos correctos")
    void empleadoCompleto_todasLasPropiedades_verificaConAssertAll() {
        // Arrange
        Heroe heroe = new Heroe();

        // Act
        heroe.setNombre_hp("Maria Lopez");
        heroe.setEquipo_hp("TI");
        heroe.setPoder_hp(35000.0);
        heroe.setMejoraPoder_hp(3500.0);

        // Assert
        assertAll("Propiedades del empleado",
                () -> assertEquals("Maria Lopez", heroe.getNombre_hp()),
                () -> assertEquals("TI", heroe.getEquipo_hp()),
                () -> assertEquals(35000.0, heroe.getPoder_hp()),
                () -> assertEquals(3500.0, heroe.getMejoraPoder_hp())
        );
    }
}
