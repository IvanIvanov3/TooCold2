package com.bytebazar.toocold2.model.realm;

import android.support.annotation.DrawableRes;

import com.bytebazar.toocold2.model.CurrentValues;

import io.realm.RealmList;
import io.realm.RealmObject;

public  class DailyWeatherRealm extends RealmObject {

    private String day;
    @DrawableRes
    private int icon;
    private int dayTempAverage;
    private RealmList<HourlyWeatherRealm> hourlyWeather;

    public DailyWeatherRealm() {
    }

    public String getDayTempAverage() {
        return getFormattedTemp(dayTempAverage);
    }

    public void setDayTempAverage(int dayTempAverage) {
        this.dayTempAverage = dayTempAverage;
    }

    public RealmList<HourlyWeatherRealm> getHourlyWeather() {
        return hourlyWeather;
    }

    public void setHourlyWeather(RealmList<HourlyWeatherRealm> hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    private String getFormattedTemp(int temp) {
        return temp + "\u00B0";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public CurrentValues<String, String> getCurrentValues() {
        final long currentTime = System.currentTimeMillis();
        for (HourlyWeatherRealm hourlyWeatherRealm : hourlyWeather) {
            if (currentTime - hourlyWeatherRealm.getTime() <= 10800000) {
                return new CurrentValues<>(getFormattedTemp(hourlyWeatherRealm.getTemp())
                        + "", hourlyWeatherRealm.getDescription());
            }
        }
        return new CurrentValues<>("", "");
    }
}
