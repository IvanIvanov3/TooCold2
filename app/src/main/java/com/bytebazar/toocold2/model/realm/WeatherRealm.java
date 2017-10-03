package com.bytebazar.toocold2.model.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public  class WeatherRealm extends RealmObject {

    @PrimaryKey
    private int id = 0;
    private long createTime;
    private String cityName;
    private int cityId;
    private double latitude;
    private double longitude;
    private RealmList<DailyWeatherRealm> dailyWeatherRealm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public RealmList<DailyWeatherRealm> getDailyWeatherRealm() {
        return dailyWeatherRealm;
    }

    public void setDailyWeatherRealm(RealmList<DailyWeatherRealm> dailyWeatherRealm) {
        this.dailyWeatherRealm = dailyWeatherRealm;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isTimeToUpdate(long currentTime) {
        return (currentTime - createTime) >= 1080000L;
    }
}
