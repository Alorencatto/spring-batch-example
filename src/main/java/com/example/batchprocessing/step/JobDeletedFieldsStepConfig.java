package com.example.batchprocessing.step;

import com.example.batchprocessing.model.TablesWithDeleteColumn;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class JobDeletedFieldsStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step jobDeletedFieldsStep(
            ItemReader<TablesWithDeleteColumn> databaseColumnReader,
            ItemProcessor<TablesWithDeleteColumn,TablesWithDeleteColumn> deleteFieldsProcessor,
            ItemWriter<TablesWithDeleteColumn> writer

    ){
        return stepBuilderFactory
                .get("jobDeletedFieldsStep")
                .<TablesWithDeleteColumn,TablesWithDeleteColumn>chunk(10)
                .reader(databaseColumnReader)
                .processor(deleteFieldsProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemWriter<TablesWithDeleteColumn> writer(DataSource dataSource){
        return System.out::println;
    }
}
