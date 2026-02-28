package org.xideral.academy.springbatchmongomockito.repository;

import org.xideral.academy.springbatchmongomockito.model.HeroeReporte;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de reportes de heroes.
 *
 * Definimos una interfaz simple (no extiende MongoRepository) para que
 * sea facil de entender y de mockear en los tests con Mockito.
 */
public interface HeroeReporteRepository {
    HeroeReporte save(HeroeReporte reporte);
    Optional<HeroeReporte> findById(String id);
    List<HeroeReporte> findAll();
    void deleteById(String id);
    boolean existsById(String id);
}
