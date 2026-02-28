package org.xideral.academy.springbatchmongomockito.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.xideral.academy.springbatchmongomockito.model.Heroe;

public class HeroeProcessor implements ItemProcessor<Heroe, Heroe> {
    private static final Logger log = LoggerFactory.getLogger(HeroeProcessor.class);

    @Override
    public Heroe process(Heroe heroe) {
        // Regla de negocio: nombre en mayusculas y bono del 10%
        heroe.setNombre_hp(heroe.getNombre_hp().toUpperCase());
        heroe.setMejoraPoder_hp(heroe.getPoder_hp() * 0.10);

        log.info("Step 1 - Procesando heroe: " + heroe);
        return heroe;
    }
}
