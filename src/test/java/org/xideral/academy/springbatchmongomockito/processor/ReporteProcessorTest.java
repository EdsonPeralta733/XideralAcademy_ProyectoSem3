package org.xideral.academy.springbatchmongomockito.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xideral.academy.springbatchmongomockito.model.Heroe;
import org.xideral.academy.springbatchmongomockito.model.HeroeReporte;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests del ReporteProcessor SIN mocks.
 *
 * El processor transforma un Heroe en un HeroeReporte,
 * copiando los campos y calculando el poderTotal (poder + mejora).
 * Es una funcion pura sin dependencias externas.
 */
public class ReporteProcessorTest {
    private ReporteProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ReporteProcessor();
    }

    @Test
    @DisplayName("process: copia el nombre del heroe al reporte")
    void process_nombre_seCopiAlReporte() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("JUAN PEREZ", "Ventas", 25000.0, 2500.0);

        // Act
        HeroeReporte reporte = processor.process(heroe);

        // Assert
        assertEquals("JUAN PEREZ", reporte.getNombre());
    }

    @Test
    @DisplayName("process: copia el equipo del heroe al reporte")
    void process_equipo_seCopiAlReporte() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("JUAN PEREZ", "Ventas", 25000.0, 2500.0);

        // Act
        HeroeReporte reporte = processor.process(heroe);

        // Assert
        assertEquals("Ventas", reporte.getEquipo());
    }

    @Test
    @DisplayName("process: calcula poderTotal como poder + mejoraPoder")
    void process_poderYMejoraPoder_totalEsLaSuma() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("JUAN PEREZ", "Ventas", 25000.0, 2500.0);

        // Act
        HeroeReporte reporte = processor.process(heroe);

        // Assert
        assertEquals(27500.0, reporte.getPoderTotal(),
                "poderTotal debe ser poder + mejoraPoder");
    }

    @Test
    @DisplayName("process: copia poder y mejoraPoder al reporte")
    void process_poderYMejoraPoder_seCopiAnAlReporte() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("MARIA LOPEZ", "TI", 35000.0, 3500.0);

        // Act
        HeroeReporte reporte = processor.process(heroe);

        // Assert
        assertAll("poder y mejoraPoder copiados",
                () -> assertEquals(35000.0, reporte.getPoder()),
                () -> assertEquals(3500.0, reporte.getMejoraPoder())
        );
    }

    @Test
    @DisplayName("process: mejoraPoder cero produce total igual al poder")
    void process_mejoraPoderCero_totalIgualpoder() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("PEDRO", "RRHH", 30000.0, 0.0);

        // Act
        HeroeReporte reporte = processor.process(heroe);

        // Assert
        assertEquals(30000.0, reporte.getPoderTotal(),
                "Sin mejoraPoder, el total debe ser igual al poder");
    }

    @Test
    @DisplayName("process: verifica todas las propiedades del reporte")
    void process_heroeCompleto_reporteCompletoYCorrecto() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("CARLOS GARCIA", "RRHH", 28000.0, 2800.0);

        // Act
        HeroeReporte reporte = processor.process(heroe);

        // Assert
        assertAll("Reporte generado correctamente",
                () -> assertEquals("CARLOS GARCIA", reporte.getNombre()),
                () -> assertEquals("RRHH", reporte.getEquipo()),
                () -> assertEquals(28000.0, reporte.getPoder()),
                () -> assertEquals(2800.0, reporte.getMejoraPoder()),
                () -> assertEquals(30800.0, reporte.getPoderTotal())
        );
    }

    @Test
    @DisplayName("process: el reporte es una instancia nueva (no el mismo objeto)")
    void process_heroe_retornaObjetoNuevo() throws Exception {
        // Arrange
        Heroe heroe = crearHeroe("ANA", "Ventas", 27000.0, 2700.0);

        // Act
        HeroeReporte reporte = processor.process(heroe);

        // Assert - el tipo de retorno es heroeReporte, diferente de heroe
        assertNotNull(reporte, "El reporte no debe ser null");
        assertNull(reporte.getId(), "El ID del reporte nuevo debe ser null");
    }

    // =====================================================================
    //  Metodo auxiliar
    // =====================================================================

    private Heroe crearHeroe(String nombre, String equipo, double poder, double mejora) {
        Heroe heroe = new Heroe();
        heroe.setNombre_hp(nombre);
        heroe.setEquipo_hp(equipo);
        heroe.setPoder_hp(poder);
        heroe.setMejoraPoder_hp(mejora);
        return heroe;
    }
}
