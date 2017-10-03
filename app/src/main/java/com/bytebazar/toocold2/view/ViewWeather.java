package com.bytebazar.toocold2.view;

import com.bytebazar.toocold2.model.realm.WeatherRealm;

public interface ViewWeather {
    void showWeather(WeatherRealm weatherRealm);

    void showLoading();

    void showCoordinateChooser();

    void showInternetError();

    void hideLoading();
}
