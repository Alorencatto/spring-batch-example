package com.example.batchprocessing;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    public Tasklet helloWorldTasklet() {
        return (contribution, chunkContext) -> {
            log.info("Execution of step with parameter message = Teste Augusto");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet secondTasklet(){
        return (contribution, chunkContext) -> {
            log.info("Second tasklet");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step helloWorldStep() {
        return stepBuilderFactory.get("helloWorldStep")
                .tasklet((helloWorldTasklet()))
                .tasklet(secondTasklet())
                .build();
    }

  /*  @Bean
    public Job helloWorldJob() {
        return jobBuilderFactory.get("helloWorldJob")
                .start(helloWorldStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }*/

    @Bean
    public Job jobDeletedFields(Step jobDeletedFieldsStep){
        return jobBuilderFactory
                .get("jobDeletedFieldsStep")
                .start(jobDeletedFieldsStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }


}
