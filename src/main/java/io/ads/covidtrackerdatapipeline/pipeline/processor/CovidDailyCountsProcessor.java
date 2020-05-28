package io.ads.covidtrackerdatapipeline.pipeline.processor;

import io.ads.covidtrackerdatapipeline.model.CovidLocationDailyCountsInfo;
import org.springframework.batch.item.ItemProcessor;

import java.util.Optional;
import java.util.UUID;

public class CovidDailyCountsProcessor implements ItemProcessor<CovidLocationDailyCountsInfo, CovidLocationDailyCountsInfo> {
    @Override
    public CovidLocationDailyCountsInfo process(CovidLocationDailyCountsInfo item) {
        CovidLocationDailyCountsInfo transformedItem = item;

        transformedItem.setId(
                UUID.nameUUIDFromBytes(item.getCountCaptureDate()
                        .concat(Optional.ofNullable(item.getCombinedStateCountryKey()).orElse(""))
                        .getBytes()).toString());

        return transformedItem;
    }
}
