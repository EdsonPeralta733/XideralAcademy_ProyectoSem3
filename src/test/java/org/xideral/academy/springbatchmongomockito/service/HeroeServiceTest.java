package org.xideral.academy.springbatchmongomockito.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.xideral.academy.springbatchmongomockito.model.Heroe;
import org.xideral.academy.springbatchmongomockito.model.HeroeReporte;
import org.xideral.academy.springbatchmongomockito.processor.HeroeProcessor;
import org.xideral.academy.springbatchmongomockito.processor.ReporteProcessor;
import org.xideral.academy.springbatchmongomockito.repository.HeroeReporteRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests del HeroeService usando Mockito.
 *
 * Conceptos de Mockito que se practican aqui:
 *
 * 1. @Mock          - crea un objeto falso que no hace nada por defecto
 * 2. @InjectMocks   - crea el objeto real e inyecta los mocks automaticamente
 * 3. @ExtendWith    - activa la extension de Mockito para JUnit 5
 * 4. when/thenReturn - programa el comportamiento de un mock
 * 5. verify         - comprueba que se llamo un metodo del mock
 * 6. ArgumentCaptor - captura el argumento con el que se llamo un metodo
 * 7. InOrder        - verifica el ORDEN en que se llamaron los metodos
 * 8. never()        - verifica que un metodo NUNCA se llamo
 * 9. times(n)       - verifica cuantas veces se llamo un metodo
 */
@ExtendWith(MockitoExtension.class)
public class HeroeServiceTest {
    // Mocks: objetos falsos que simulan las dependencias del servicio
    @Mock
    private HeroeProcessor heroeProcessor;

    @Mock
    private ReporteProcessor reporteProcessor;

    @Mock
    private HeroeReporteRepository repository;

    // Objeto real bajo prueba: Mockito inyecta los 3 mocks automaticamente
    @InjectMocks
    private HeroeService service;

    // =====================================================================
    //  procesarYGuardar() - when/thenReturn + verify
    // =====================================================================

    @Test
    @DisplayName("procesarYGuardar: procesa heroe y guarda el reporte")
    void procesarYGuardar_heroeValido_guardaReporte() throws Exception {
        // Arrange - preparamos los datos y programamos los mocks
        Heroe original = crearHeroe("Juan Perez", "Ventas", 25000.0);
        Heroe procesado = crearHeroe("JUAN PEREZ", "Ventas", 25000.0);
        procesado.setMejoraPoder_hp(2500.0);

        HeroeReporte reporte = crearReporte("rpt1", "JUAN PEREZ", "Ventas",
                25000.0, 2500.0, 27500.0);

        // Programamos el comportamiento de cada mock:
        // "cuando llamen a process(original), retorna procesado"
        when(heroeProcessor.process(original)).thenReturn(procesado);
        when(reporteProcessor.process(procesado)).thenReturn(reporte);
        when(repository.save(reporte)).thenReturn(reporte);

        // Act - ejecutamos el metodo real del servicio
        HeroeReporte resultado = service.procesarYGuardar(original);

        // Assert - verificamos el resultado
        assertNotNull(resultado);
        assertEquals("JUAN PEREZ", resultado.getNombre());
        assertEquals(27500.0, resultado.getPoderTotal());

        // Verify - comprobamos que se llamo cada mock exactamente 1 vez
        verify(heroeProcessor).process(original);
        verify(reporteProcessor).process(procesado);
        verify(repository).save(reporte);
    }

    // =====================================================================
    //  procesarYGuardar() - InOrder (verificar orden de ejecucion)
    // =====================================================================

    @Test
    @DisplayName("procesarYGuardar: ejecuta los pasos en el orden correcto")
    void procesarYGuardar_heroeValido_ejecutaEnOrden() throws Exception {
        // Arrange
        Heroe original = crearHeroe("Maria Lopez", "TI", 35000.0);
        Heroe procesado = crearHeroe("MARIA LOPEZ", "TI", 35000.0);
        procesado.setMejoraPoder_hp(3500.0);

        HeroeReporte reporte = crearReporte("rpt2", "MARIA LOPEZ", "TI",
                35000.0, 3500.0, 38500.0);

        when(heroeProcessor.process(original)).thenReturn(procesado);
        when(reporteProcessor.process(procesado)).thenReturn(reporte);
        when(repository.save(reporte)).thenReturn(reporte);

        // Act
        service.procesarYGuardar(original);

        // Assert - InOrder verifica que los metodos se llamaron EN ESTE ORDEN:
        // primero heroeProcessor, luego reporteProcessor, finalmente repository
        InOrder orden = inOrder(heroeProcessor, reporteProcessor, repository);
        orden.verify(heroeProcessor).process(original);       // 1ro
        orden.verify(reporteProcessor).process(procesado);       // 2do
        orden.verify(repository).save(reporte);                  // 3ro
    }

    // =====================================================================
    //  procesarYGuardar() - ArgumentCaptor (capturar argumentos)
    // =====================================================================

    @Test
    @DisplayName("procesarYGuardar: captura el reporte que se intenta guardar")
    void procesarYGuardar_heroeValido_capturaReporteGuardado() throws Exception {
        // Arrange
        Heroe original = crearHeroe("Carlos Garcia", "RRHH", 28000.0);
        Heroe procesado = crearHeroe("CARLOS GARCIA", "RRHH", 28000.0);
        procesado.setMejoraPoder_hp(2800.0);

        HeroeReporte reporte = crearReporte(null, "CARLOS GARCIA", "RRHH",
                28000.0, 2800.0, 30800.0);

        when(heroeProcessor.process(original)).thenReturn(procesado);
        when(reporteProcessor.process(procesado)).thenReturn(reporte);
        when(repository.save(any(HeroeReporte.class))).thenReturn(reporte);

        // Act
        service.procesarYGuardar(original);

        // Assert - ArgumentCaptor "captura" el argumento real que recibio repository.save()
        ArgumentCaptor<HeroeReporte> captor = ArgumentCaptor.forClass(HeroeReporte.class);
        verify(repository).save(captor.capture());

        HeroeReporte reporteCapturado = captor.getValue();
        assertAll("Reporte capturado",
                () -> assertEquals("CARLOS GARCIA", reporteCapturado.getNombre()),
                () -> assertEquals("RRHH", reporteCapturado.getEquipo()),
                () -> assertEquals(30800.0, reporteCapturado.getPoderTotal())
        );
    }

    // =====================================================================
    //  procesarLote() - times() (verificar numero de invocaciones)
    // =====================================================================

    @Test
    @DisplayName("procesarLote: procesa cada heroe de la lista")
    void procesarLote_listaConDosHeroes_procesaCadaUno() throws Exception {
        // Arrange
        Heroe emp1 = crearHeroe("Juan", "Ventas", 25000.0);
        Heroe emp2 = crearHeroe("Maria", "TI", 35000.0);

        Heroe proc1 = crearHeroe("JUAN", "Ventas", 25000.0);
        proc1.setMejoraPoder_hp(2500.0);
        Heroe proc2 = crearHeroe("MARIA", "TI", 35000.0);
        proc2.setMejoraPoder_hp(3500.0);

        HeroeReporte rpt1 = crearReporte("r1", "JUAN", "Ventas", 25000.0, 2500.0, 27500.0);
        HeroeReporte rpt2 = crearReporte("r2", "MARIA", "TI", 35000.0, 3500.0, 38500.0);

        when(heroeProcessor.process(emp1)).thenReturn(proc1);
        when(heroeProcessor.process(emp2)).thenReturn(proc2);
        when(reporteProcessor.process(proc1)).thenReturn(rpt1);
        when(reporteProcessor.process(proc2)).thenReturn(rpt2);
        when(repository.save(rpt1)).thenReturn(rpt1);
        when(repository.save(rpt2)).thenReturn(rpt2);

        // Act
        List<HeroeReporte> resultados = service.procesarLote(Arrays.asList(emp1, emp2));

        // Assert
        assertEquals(2, resultados.size(), "Debe procesar 2 heroes");

        // times(2) verifica que cada processor se llamo exactamente 2 veces
        verify(heroeProcessor, times(2)).process(any(Heroe.class));
        verify(reporteProcessor, times(2)).process(any(Heroe.class));
        verify(repository, times(2)).save(any(HeroeReporte.class));
    }

    @Test
    @DisplayName("procesarLote: lista vacia no invoca ningun mock")
    void procesarLote_listaVacia_noInvocaMocks() throws Exception {
        // Arrange
        List<Heroe> listaVacia = List.of();

        // Act
        List<HeroeReporte> resultados = service.procesarLote(listaVacia);

        // Assert
        assertTrue(resultados.isEmpty(), "Lista vacia no produce reportes");

        // never() verifica que NUNCA se llamo al metodo
        verify(heroeProcessor, never()).process(any());
        verify(reporteProcessor, never()).process(any());
        verify(repository, never()).save(any());
    }

    // =====================================================================
    //  obtenerReporte() - when/thenReturn con Optional
    // =====================================================================

    @Test
    @DisplayName("obtenerReporte: retorna el reporte cuando existe")
    void obtenerReporte_idExistente_retornaReporte() {
        // Arrange
        HeroeReporte reporte = crearReporte("rpt1", "JUAN", "Ventas",
                25000.0, 2500.0, 27500.0);
        when(repository.findById("rpt1")).thenReturn(Optional.of(reporte));

        // Act
        Optional<HeroeReporte> resultado = service.obtenerReporte("rpt1");

        // Assert
        assertTrue(resultado.isPresent(), "Debe encontrar el reporte");
        assertEquals("JUAN", resultado.get().getNombre());
        verify(repository).findById("rpt1");
    }

    @Test
    @DisplayName("obtenerReporte: retorna vacio cuando no existe")
    void obtenerReporte_idInexistente_retornaVacio() {
        // Arrange
        when(repository.findById("noExiste")).thenReturn(Optional.empty());

        // Act
        Optional<HeroeReporte> resultado = service.obtenerReporte("noExiste");

        // Assert
        assertTrue(resultado.isEmpty(), "No debe encontrar el reporte");
        verify(repository).findById("noExiste");
    }

    // =====================================================================
    //  obtenerTodosLosReportes()
    // =====================================================================

    @Test
    @DisplayName("obtenerTodosLosReportes: retorna la lista del repositorio")
    void obtenerTodosLosReportes_conReportes_retornaTodos() {
        // Arrange
        HeroeReporte rpt1 = crearReporte("r1", "JUAN", "Ventas", 25000.0, 2500.0, 27500.0);
        HeroeReporte rpt2 = crearReporte("r2", "MARIA", "TI", 35000.0, 3500.0, 38500.0);
        when(repository.findAll()).thenReturn(Arrays.asList(rpt1, rpt2));

        // Act
        List<HeroeReporte> resultado = service.obtenerTodosLosReportes();

        // Assert
        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    // =====================================================================
    //  eliminarReporte() - verify + never()
    // =====================================================================

    @Test
    @DisplayName("eliminarReporte: elimina cuando el reporte existe")
    void eliminarReporte_idExistente_eliminaYRetornaTrue() {
        // Arrange
        when(repository.existsById("rpt1")).thenReturn(true);

        // Act
        boolean resultado = service.eliminarReporte("rpt1");

        // Assert
        assertTrue(resultado, "Debe retornar true al eliminar");

        // Verificamos que se llamo existsById Y LUEGO deleteById (InOrder)
        InOrder orden = inOrder(repository);
        orden.verify(repository).existsById("rpt1");
        orden.verify(repository).deleteById("rpt1");
    }

    @Test
    @DisplayName("eliminarReporte: no elimina cuando el reporte no existe")
    void eliminarReporte_idInexistente_retornaFalseSinEliminar() {
        // Arrange
        when(repository.existsById("noExiste")).thenReturn(false);

        // Act
        boolean resultado = service.eliminarReporte("noExiste");

        // Assert
        assertFalse(resultado, "Debe retornar false si no existe");

        // never() verifica que deleteById NUNCA se llamo
        verify(repository).existsById("noExiste");
        verify(repository, never()).deleteById(anyString());
    }

    // =====================================================================
    //  Metodos auxiliares para crear datos de prueba
    // =====================================================================

    private Heroe crearHeroe(String nombre, String equipo, double poder) {
        Heroe heroe = new Heroe();
        heroe.setNombre_hp(nombre);
        heroe.setEquipo_hp(equipo);
        heroe.setPoder_hp(poder);
        return heroe;
    }

    private HeroeReporte crearReporte(String id, String nombre, String equipo,
                                         double poder, double mejoraPoder, double poderTotal) {
        HeroeReporte reporte = new HeroeReporte();
        reporte.setId(id);
        reporte.setNombre(nombre);
        reporte.setEquipo(equipo);
        reporte.setPoder(poder);
        reporte.setMejoraPoder(mejoraPoder);
        reporte.setPoderTotal(poderTotal);
        return reporte;
    }
}
