package com.bytebazar.toocold2.model;

import com.bytebazar.toocold2.model.realm.WeatherRealm;

import io.reactivex.Observable;

public interface Model {

    Observable<WeatherRealm> getWeather();

    Observable<WeatherRealm> getWeatherFromServer(double latitude, double longitude);

    void getCoordinates();
}
