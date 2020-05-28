package io.ads.covidtrackerdatapipeline.pipeline.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import io.ads.covidtrackerdatapipeline.model.CovidLocationDailyCountsInfo;
import io.ads.covidtrackerdatapipeline.pipeline.listener.JobCompletionNotificationListener;
import io.ads.covidtrackerdatapipeline.pipeline.processor.CovidDailyCountsProcessor;
import io.ads.covidtrackerdatapipeline.pipeline.reader.CovidDailyCountsReader;
import io.ads.covidtrackerdatapipeline.pipeline.writer.CovidDailyCountsWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;

@Configuration
@EnableBatchProcessing
public class CovidTrackerPipelineBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public ItemReader<CovidLocationDailyCountsInfo> reader() {
        return new CovidDailyCountsReader(
                LocalDate.of(2020, 03, 15),
                LocalDate.of(2020, 03, 31));
    }

    @Bean
    public ItemProcessor<CovidLocationDailyCountsInfo, CovidLocationDailyCountsInfo> processor() {
       return new CovidDailyCountsProcessor();
    }

    @Bean
    public ItemWriter<CovidLocationDailyCountsInfo> writer() {
        return new CovidDailyCountsWriter(amazonDynamoDB);
    }

    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(transactionManager);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    public Job importDataJob(JobCompletionNotificationListener listener, Step step1) throws Exception {
        return jobBuilderFactory.get("importDataJob")
                .repository(getJobRepository())
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<CovidLocationDailyCountsInfo, CovidLocationDailyCountsInfo> chunk(1000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }


}
