package org.xideral.academy.springbatchmongomockito.service;

import org.xideral.academy.springbatchmongomockito.model.Heroe;
import org.xideral.academy.springbatchmongomockito.model.HeroeReporte;
import org.xideral.academy.springbatchmongomockito.processor.HeroeProcessor;
import org.xideral.academy.springbatchmongomockito.processor.ReporteProcessor;
import org.xideral.academy.springbatchmongomockito.repository.HeroeReporteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que orquesta el procesamiento de heroes.
 *
 * Combina los dos processors del batch (HeroeProcessor y ReporteProcessor)
 * con el repositorio para procesar y persistir reportes.
 * Este servicio es el vehiculo principal para aprender Mockito.
 */
public class HeroeService {
    private final HeroeProcessor heroeProcessor;
    private final ReporteProcessor reporteProcessor;
    private final HeroeReporteRepository repository;

    public HeroeService(HeroeProcessor heroeProcessor, ReporteProcessor reporteProcessor, HeroeReporteRepository repository) {
        this.heroeProcessor = heroeProcessor;
        this.reporteProcessor = reporteProcessor;
        this.repository = repository;
    }

    // Procesa un heroe (mayusculas + mejoraPoder) y guarda el reporte.
    public HeroeReporte procesarYGuardar(Heroe heroe) throws Exception {
        // Paso 1: aplicar transformacion (mayusculas + mejoraPoder 10%)
        Heroe procesado = heroeProcessor.process(heroe);

        // Paso 2: convertir a reporte (calcula poderTotal)
        HeroeReporte reporte = reporteProcessor.process(procesado);

        // Paso 3: persistir en el repositorio
        return repository.save(reporte);
    }

    // Procesa una lista de heroes y guarda todos los reportes.
    public List<HeroeReporte> procesarLote(List<Heroe> heroes) throws Exception {
        List<HeroeReporte> reportes = new ArrayList<>();
        for (Heroe heroe : heroes) {
            reportes.add(procesarYGuardar(heroe));
        }
        return reportes;
    }

    // Busca un reporte por su ID.
    public Optional<HeroeReporte> obtenerReporte(String id) {
        return repository.findById(id);
    }

    // Retorna todos los reportes almacenados.
    public List<HeroeReporte> obtenerTodosLosReportes() {
        return repository.findAll();
    }

    // Elimina un reporte si existe.
    public boolean eliminarReporte(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}