package org.xideral.academy.springbatchmongomockito.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xideral.academy.springbatchmongomockito.model.Heroe;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests del HeroeProcessor SIN mocks.
 *
 * El processor es una funcion pura: recibe un Heroe y retorna
 * el mismo Heroe transformado (nombre en mayusculas + mejora del 10%).
 * No tiene dependencias externas, asi que no necesitamos Mockito.
 */
public class HeroeProcessorTest {
    private HeroeProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new HeroeProcessor();
    }

    @Test
    @DisplayName("process: convierte el nombre a mayusculas")
    void process_nombreMinusculas_retornaMayusculas() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("Juan Perez", "Ventas", 25000.0);

        // Act
        Heroe resultado = processor.process(heroe);

        // Assert
        assertEquals("JUAN PEREZ", resultado.getNombre_hp(),
                "El nombre debe estar en mayusculas");
    }

    @Test
    @DisplayName("process: calcula la mejora como 10% del poder")
    void process_poder25000_mejoraEs2500() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("Juan Perez", "Ventas", 25000.0);

        // Act
        Heroe resultado = processor.process(heroe);

        // Assert
        assertEquals(2500.0, resultado.getMejoraPoder_hp(),
                "La mejora debe ser el 10% del poder");
    }

    @Test
    @DisplayName("process: nombre que ya esta en mayusculas no cambia")
    void process_nombreYaEnMayusculas_permaneceIgual() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("MARIA LOPEZ", "TI", 35000.0);

        // Act
        Heroe resultado = processor.process(heroe);

        // Assert
        assertEquals("MARIA LOPEZ", resultado.getNombre_hp());
    }

    @Test
    @DisplayName("process: el equipo no se modifica")
    void process_equipo_noSeModifica() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("Juan Perez", "Ventas", 25000.0);

        // Act
        Heroe resultado = processor.process(heroe);

        // Assert
        assertEquals("Ventas", resultado.getEquipo_hp(),
                "El equipo no debe cambiar");
    }

    @Test
    @DisplayName("process: el poder original no se modifica")
    void process_poder_noSeModifica() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("Juan Perez", "Ventas", 25000.0);

        // Act
        Heroe resultado = processor.process(heroe);

        // Assert
        assertEquals(25000.0, resultado.getPoder_hp(),
                "El poder original no debe cambiar");
    }

    @Test
    @DisplayName("process: poder cero produce mejora cero")
    void process_poderCero_mejoraCero() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("Pedro", "RRHH", 0.0);

        // Act
        Heroe resultado = processor.process(heroe);

        // Assert
        assertEquals(0.0, resultado.getMejoraPoder_hp(), "Bono de salario 0 debe ser 0");
    }

    @Test
    @DisplayName("process: verifica todas las transformaciones a la vez")
    void process_heroeCompleto_todasLasTransformaciones() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("Carlos Garcia", "RRHH", 28000.0);

        // Act
        Heroe resultado = processor.process(heroe);

        // Assert
        assertAll("Transformaciones del processor",
                () -> assertEquals("CARLOS GARCIA", resultado.getNombre_hp()),
                () -> assertEquals("RRHH", resultado.getEquipo_hp()),
                () -> assertEquals(28000.0, resultado.getPoder_hp()),
                () -> assertEquals(2800.0, resultado.getMejoraPoder_hp())
        );
    }

    // =====================================================================
    //  Metodo auxiliar
    // =====================================================================

    private Heroe crearHeroe(String nombre, String equipo, double poder) {
        Heroe heroe = new Heroe();
        heroe.setNombre_hp(nombre);
        heroe.setEquipo_hp(equipo);
        heroe.setPoder_hp(poder);
        return heroe;
    }
}
