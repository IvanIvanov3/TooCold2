package com.bytebazar.toocold2;

import android.util.Log;

import com.bytebazar.toocold2.exceptions.EmptyDbException;
import com.bytebazar.toocold2.exceptions.NoInternetConnectionException;
import com.bytebazar.toocold2.model.ModelImpl;
import com.bytebazar.toocold2.model.Model;
import com.bytebazar.toocold2.model.realm.WeatherRealm;
import com.bytebazar.toocold2.view.ViewWeather;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public final class Presenter {
    private static final String TAG = "Presenter";

    final private ViewWeather view;
    final private Model model;
    final private CompositeDisposable disposable = new CompositeDisposable();

    public Presenter(ViewWeather view, ModelImpl model) {
        this.view = view;
        this.model = model;
    }

    public void destroy() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void getWeatherData() {
        view.showLoading();
        subscribe(model.getWeather());
    }

    public void getWeatherData(double latitude, double longitude) {
        view.showLoading();
        subscribe(model.getWeatherFromServer(latitude, longitude));
    }

    private void subscribe(Observable<WeatherRealm> observable) {
        observable.subscribe(new Observer<WeatherRealm>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(WeatherRealm weatherRealm) {
                view.hideLoading();
                view.showWeather(weatherRealm);
            }

            @Override
            public void onError(Throwable e) {
                view.hideLoading();
                if (e instanceof EmptyDbException) {
                    view.showCoordinateChooser();
                } else if (e instanceof NoInternetConnectionException) {
                    view.showInternetError();
                } else Log.e(TAG, "onError: ", e);
                disposable.clear();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getCoordinates() {
        Disposable subscribe = App.getSubject().subscribe(coordinates
                -> subscribe(model.getWeatherFromServer(coordinates.latitude, coordinates.longitude)));
        model.getCoordinates();
        disposable.add(subscribe);
    }
}
