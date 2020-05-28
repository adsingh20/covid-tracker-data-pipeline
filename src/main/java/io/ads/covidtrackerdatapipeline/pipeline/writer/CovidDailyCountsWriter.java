package io.ads.covidtrackerdatapipeline.pipeline.writer;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.ads.covidtrackerdatapipeline.model.CovidLocationDailyCountsInfo;
import io.ads.covidtrackerdatapipeline.pipeline.listener.JobCompletionNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CovidDailyCountsWriter implements ItemWriter<CovidLocationDailyCountsInfo> {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private DynamoDBMapper dynamoDBMapper;


    public CovidDailyCountsWriter(AmazonDynamoDB amazonDynamoDB) {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    }

    @Override
    public void write(List<? extends CovidLocationDailyCountsInfo> items) throws Exception {
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchSave(items);
        if (failedBatches.size() > 0) {
            throw new JobExecutionException("Write to DynamoDB Failed", failedBatches.get(0).getException());
        }
    }
}
