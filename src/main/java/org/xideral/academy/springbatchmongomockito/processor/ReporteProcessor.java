package org.xideral.academy.springbatchmongomockito.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.xideral.academy.springbatchmongomockito.model.Heroe;
import org.xideral.academy.springbatchmongomockito.model.HeroeReporte;

public class ReporteProcessor implements ItemProcessor<Heroe, HeroeReporte> {
    private static final Logger log = LoggerFactory.getLogger(ReporteProcessor.class);

    @Override
    public HeroeReporte process(Heroe heroe) {
        HeroeReporte reporte = new HeroeReporte();
        reporte.setNombre(heroe.getNombre_hp());
        reporte.setEquipo(heroe.getEquipo_hp());
        reporte.setPoder(heroe.getPoder_hp());
        reporte.setMejoraPoder(heroe.getMejoraPoder_hp());
        reporte.setPoderTotal(heroe.getPoder_hp() + heroe.getMejoraPoder_hp());

        log.info("Step 2 - Procesando reporte: " + reporte);
        return reporte;
    }
}