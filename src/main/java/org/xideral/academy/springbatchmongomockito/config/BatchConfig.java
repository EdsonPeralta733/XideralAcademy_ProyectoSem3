package org.xideral.academy.springbatchmongomockito.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.xideral.academy.springbatchmongomockito.model.Heroe;
import org.xideral.academy.springbatchmongomockito.model.HeroeReporte;
import org.xideral.academy.springbatchmongomockito.processor.HeroeProcessor;
import org.xideral.academy.springbatchmongomockito.processor.ReporteProcessor;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {
    // =====================================================================
    //  STEP 1: Lee CSV → procesa (bono + mayusculas) → escribe en MySQL
    // =====================================================================
    @Bean
    public FlatFileItemReader<Heroe> leerCSV() {
        return new FlatFileItemReaderBuilder<Heroe>()
                .name("heroeReader")
                .resource(new ClassPathResource("heroes.csv"))
                .delimited()
                .names("nombre_hp", "equipo_hp", "poder_hp")
                .targetType(Heroe.class)
                .linesToSkip(1)
                .build();
    }

    // ---------- PROCESSOR: transforma cada registro ----------
    @Bean
    public HeroeProcessor procesarHeroe() {
        return new HeroeProcessor();
    }

    // ---------- WRITER: escribe en la tabla MySQL ----------
    @Bean
    public JdbcBatchItemWriter<Heroe> escribirEnBD(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Heroe>()
                .sql("INSERT INTO heroes_procesados (nombre_hp, equipo_hp, poder_hp, mejoraPoder_hp)" +
                        "VALUES (:nombre_hp, :equipo_hp, :poder_hp, :mejoraPoder_hp)" )
                .dataSource(dataSource)
                .beanMapped() // usa los getters del POJO para mapear.
                .build();
    }

    // ---------- STEP: un paso = reader + processor + writer ----------
    @Bean
    public Step paso1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      FlatFileItemReader<Heroe> leerCSV,
                      HeroeProcessor procesarHeroe,
                      JdbcBatchItemWriter<Heroe> escribirEnBD) {
        return new StepBuilder("paso1", jobRepository)
                .<Heroe, Heroe>chunk(3, transactionManager) // procesa de 3 en 3
                .reader(leerCSV)
                .processor(procesarHeroe)
                .writer(escribirEnBD)
                .build();
    }

    // =====================================================================
    //  STEP 2: Lee MySQL → calcula poder total → escribe en MongoDB
    // =====================================================================
    @Bean
    public JdbcCursorItemReader<Heroe> leerDeBD(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Heroe>()
                .name("heroeDBReader")
                .dataSource(dataSource)
                .sql("SELECT nombre_hp, equipo_hp, poder_hp, mejoraPoder_hp FROM heroes_procesados")
                .rowMapper((rs, rowNum) -> {
                    Heroe heroe = new Heroe();
                    heroe.setNombre_hp(rs.getString("nombre_hp"));
                    heroe.setEquipo_hp(rs.getString("equipo_hp"));
                    heroe.setPoder_hp(rs.getDouble("poder_hp"));
                    heroe.setMejoraPoder_hp(rs.getDouble("mejoraPoder_hp"));
                    return heroe;
                })
                .build();
    }

    @Bean
    public ReporteProcessor procesarReporte() {
        return new ReporteProcessor();
    }

    @Bean
    public MongoItemWriter<HeroeReporte> escribirEnMongo(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<HeroeReporte>()
                .template(mongoTemplate).collection("reportes").build();
    }

    @Bean
    public Step paso2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcCursorItemReader<Heroe> leerDeBD,
                      ReporteProcessor procesarReporte,
                      MongoItemWriter<HeroeReporte> escribirEnMongo) {
        return new StepBuilder("paso2", jobRepository)
                .<Heroe, HeroeReporte>chunk(3, transactionManager)
                .reader(leerDeBD)
                .processor(procesarReporte)
                .writer(escribirEnMongo)
                .build();
    }

    // =====================================================================
    //  JOB: ejecuta paso1 y luego paso2
    // =====================================================================
    @Bean
    public Job procesarHeroesJob(JobRepository jobRepository, Step paso1, Step paso2) {
        return new JobBuilder("procesarHeroesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(paso1)
                .next(paso2)
                .build();
    }
}