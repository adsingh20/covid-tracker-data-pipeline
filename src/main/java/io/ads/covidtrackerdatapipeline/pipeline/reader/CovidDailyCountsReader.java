package io.ads.covidtrackerdatapipeline.pipeline.reader;

import io.ads.covidtrackerdatapipeline.model.CovidLocationDailyCountsInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CovidDailyCountsReader implements ItemReader<CovidLocationDailyCountsInfo> {

    private final HttpClient httpClient;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    private int nextIndex;

    @Value("${covid.data.url}")
    private String covidDataUrl;

    private List<CovidLocationDailyCountsInfo> covidLocationDailyCountsInfoList;


    public CovidDailyCountsReader(LocalDate fromDate, LocalDate toDate) {
        this.httpClient = HttpClient.newHttpClient();
        this.fromDate = fromDate;
        this.toDate = toDate;
    }


    @Override
    public CovidLocationDailyCountsInfo read() throws Exception {
        if (dataIsNotInitialized()) {
            covidLocationDailyCountsInfoList = fetchDailyLocationCounts();
        }

        CovidLocationDailyCountsInfo info = null;

        if (nextIndex < covidLocationDailyCountsInfoList.size()) {
            info = covidLocationDailyCountsInfoList.get(nextIndex);
            nextIndex++;
        }

        return info;
    }

    private boolean dataIsNotInitialized() {
        return this.covidLocationDailyCountsInfoList == null;
    }

    private List<CovidLocationDailyCountsInfo> fetchDailyLocationCounts() throws JobExecutionException, FileNotFoundException {

        final List<LocalDate> dateRange = fromDate.datesUntil(toDate.plusDays(1)).collect(Collectors.toUnmodifiableList());

        List<CovidLocationDailyCountsInfo> dailyLocationCounts = new ArrayList<>();

        for (LocalDate currentFetchDate : dateRange) {
            URI uri = URI.create(covidDataUrl.concat(currentFetchDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))).concat(".csv"));
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> httpResponse;
            try {
                httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new JobExecutionException("Unexpected error during fetch call to data endpoint", e.getCause());
            }

            if (httpResponse.statusCode() == 404) {
                throw new FileNotFoundException("File not found on URL" + uri.toASCIIString());
            }

            StringReader stringReader = new StringReader(httpResponse.body());

            Iterable<CSVRecord> records;
            try {
                records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
            } catch (IOException e) {
                throw new JobExecutionException("Unable to parse the http response", e.getCause());
            }

            for (CSVRecord record: records) {
                CovidLocationDailyCountsInfo covidLocationDailyCountsInfo =
                        new CovidLocationDailyCountsInfo.CovidLocationDailyCountsInfoBuilder()
                                .setCountCaptureDate(currentFetchDate.toString())
                                .setState(record.get("Province_State"))
                                .setCountry(record.get("Country_Region"))
                                .setLatitude(record.get("Lat"))
                                .setLongitude(record.get("Long_"))
                                .setConfirmedCount(Long.parseLong(record.get("Confirmed")))
                                .setDeathCount(Long.parseLong(record.get("Deaths")))
                                .setRecoveredCount(Long.parseLong(record.get("Recovered")))
                                .setActiveCount(Long.parseLong(record.get("Active")))
                                .setCombinedStateCountryKey(record.get("Combined_Key"))
                                .build();

                dailyLocationCounts.add(covidLocationDailyCountsInfo);
            }
        }

        return dailyLocationCounts;
    }
}
