package com.bytebazar.toocold2.model;

import com.bytebazar.toocold2.model.response.HourlyWeather;
import com.bytebazar.toocold2.model.response.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    String LATITUDE = "lat";
    String LONGITUDE = "lon";
    String UNITS = "units";
    String APPID = "APPID";
    String LANG = "lang";

    @GET("forecast?")
    Observable<WeatherResponse> getWeather(
            @Query(LATITUDE) double latitude
            , @Query(LONGITUDE) double longitude
            , @Query(UNITS) String units
            , @Query(LANG) String lang
            , @Query(APPID) String appid);


    @GET("weather?")
    Observable<HourlyWeather> getWeatherToday(
            @Query(LATITUDE) double latitude
            , @Query(LONGITUDE) double longitude
            , @Query(UNITS) String units
            , @Query(LANG) String lang
            , @Query(APPID) String appid);
}
