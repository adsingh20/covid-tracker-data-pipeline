package io.ads.covidtrackerdatapipeline.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "CovidLocationDailyCounts")
public class CovidLocationDailyCountsInfo  {

    private String id;

    private String countCaptureDate;
    private String state;
    private String country;
    private String latitude;
    private String longitude;
    private long confirmedCount;
    private long deathCount;
    private long recoveredCount;
    private long activeCount;
    private String combinedStateCountryKey;

    private CovidLocationDailyCountsInfo(CovidLocationDailyCountsInfoBuilder builder) {
        this.id = builder.id;
        this.countCaptureDate = builder.countCaptureDate;
        this.state = builder.state;
        this.country = builder.country;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.confirmedCount = builder.confirmedCount;
        this.deathCount = builder.deathCount;
        this.recoveredCount = builder.recoveredCount;
        this.activeCount = builder.activeCount;
        this.combinedStateCountryKey = builder.combinedStateCountryKey;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "Count-Index")
    public String getCountCaptureDate() {
        return countCaptureDate;
    }

    public void setCountCaptureDate(String countCaptureDate) {
        this.countCaptureDate = countCaptureDate;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "Count-Index")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(long confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    public long getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(long deathCount) {
        this.deathCount = deathCount;
    }

    public long getRecoveredCount() {
        return recoveredCount;
    }

    public void setRecoveredCount(long recoveredCount) {
        this.recoveredCount = recoveredCount;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

    public String getCombinedStateCountryKey() {
        return combinedStateCountryKey;
    }

    public void setCombinedStateCountryKey(String combinedStateCountryKey) {
        this.combinedStateCountryKey = combinedStateCountryKey;
    }

    @Override
    public String toString() {
        return "CovidLocationDailyCountsInfo{" +
                "id='" + id + '\'' +
                ", countCaptureDate='" + countCaptureDate + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", confirmedCount=" + confirmedCount +
                ", deathCount=" + deathCount +
                ", recoveredCount=" + recoveredCount +
                ", activeCount=" + activeCount +
                ", combinedStateCountryKey='" + combinedStateCountryKey + '\'' +
                '}';
    }

    public static class CovidLocationDailyCountsInfoBuilder {
        private String id;

        private String countCaptureDate;
        private String state;
        private String country;
        private String latitude;
        private String longitude;
        private long confirmedCount;
        private long deathCount;
        private long recoveredCount;
        private long activeCount;
        private String combinedStateCountryKey;

        public CovidLocationDailyCountsInfoBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setCountCaptureDate(String countCaptureDate) {
            this.countCaptureDate = countCaptureDate;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setState(String state) {
            this.state = state;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setLatitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setLongitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setConfirmedCount(long confirmedCount) {
            this.confirmedCount = confirmedCount;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setDeathCount(long deathCount) {
            this.deathCount = deathCount;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setRecoveredCount(long recoveredCount) {
            this.recoveredCount = recoveredCount;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setActiveCount(long activeCount) {
            this.activeCount = activeCount;
            return this;
        }

        public CovidLocationDailyCountsInfoBuilder setCombinedStateCountryKey(String combinedStateCountryKey) {
            this.combinedStateCountryKey = combinedStateCountryKey;
            return this;
        }

        public CovidLocationDailyCountsInfo build() {
            return new CovidLocationDailyCountsInfo(this);
        }
    }
}

