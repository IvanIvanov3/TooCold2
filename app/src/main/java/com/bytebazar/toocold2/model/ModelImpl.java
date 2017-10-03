package com.bytebazar.toocold2.model;

import android.content.Context;
import android.content.Intent;

import com.bytebazar.toocold2.BuildConfig;
import com.bytebazar.toocold2.model.coordinates.LocationService;
import com.bytebazar.toocold2.Utils;
import com.bytebazar.toocold2.exceptions.EmptyDbException;
import com.bytebazar.toocold2.exceptions.NoInternetConnectionException;
import com.bytebazar.toocold2.model.mapper.RealmMapper;
import com.bytebazar.toocold2.model.realm.WeatherRealm;
import com.bytebazar.toocold2.model.response.HourlyWeather;
import com.bytebazar.toocold2.model.response.WeatherResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public final class ModelImpl implements Model {
    private static final String TAG = "ModelImpl";

    public static final String UNITS = "metric";

    final private RetrofitInterface retrofit;
    final private Realm realmUi;
    final private RealmMapper mapper;
    final private Context context;


    public ModelImpl(Context context) {
        this.context = context;
        Realm.init(context);
        mapper = new RealmMapper();
        realmUi = Realm.getDefaultInstance();
        this.retrofit = ApiModule.getApiWeather();
    }

    @Override
    public void getCoordinates() {
        stopLocationService();
    }

    @Override
    public Observable<WeatherRealm> getWeather() {
        WeatherRealm weatherRealm = readFromDb(realmUi);
        if (weatherRealm == null) {
            return Observable.error(new EmptyDbException());
        } else if (weatherRealm.isTimeToUpdate(System.currentTimeMillis())) {
            return getWeatherFromServer(weatherRealm.getLatitude(), weatherRealm.getLongitude());
        } else return wrapObservable(weatherRealm);
    }

    @Override
    public Observable<WeatherRealm> getWeatherFromServer(double latitude, double longitude) {

        return Observable.fromCallable(Utils::isInternetAvailable)
                .flatMap(isInternetAvailable -> {
                    if (isInternetAvailable) {
                        starLocationService();
                        return Observable.zip(getWeather(latitude, longitude)
                                , getWeatherToday(latitude, longitude), this::convertResponseToday)
                                .map(mapper)
                                .map(this::writeToDb);
                    } else
                        return Observable.error(new NoInternetConnectionException());
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    private void starLocationService() {
        context.stopService(new Intent(context, LocationService.class));
    }

    private void stopLocationService() {
        context.startService(new Intent(context, LocationService.class));
    }


    private WeatherResponse convertResponseToday(WeatherResponse weatherResponse, HourlyWeather hourlyWeather) {
        weatherResponse.list.add(0, hourlyWeather);
        return weatherResponse;
    }

    private Observable<WeatherRealm> wrapObservable(WeatherRealm weatherRealm) {
        return Observable.create(e -> {
            e.onNext(weatherRealm);
            e.onComplete();
        });
    }

    private Observable<HourlyWeather> getWeatherToday(double latitude, double longitude) {
        return retrofit.getWeatherToday(latitude, longitude, UNITS, Utils.setLanguageResponse(), BuildConfig.OPEN_WEATHER_MAP_API_KEY);
    }

    private Observable<WeatherResponse> getWeather(double latitude, double longitude) {
        return retrofit.getWeather(latitude, longitude, UNITS, Utils.setLanguageResponse(), BuildConfig.OPEN_WEATHER_MAP_API_KEY);
    }

    private WeatherRealm readFromDb(Realm realm) {
        return realm.where(WeatherRealm.class).findFirst();
    }

    private WeatherRealm writeToDb(WeatherRealm weatherRealm) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm.copyToRealmOrUpdate(weatherRealm));
        realm.close();
        return weatherRealm;
    }
}
