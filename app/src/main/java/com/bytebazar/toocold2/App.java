package com.bytebazar.toocold2;

import android.app.Application;

import com.bytebazar.toocold2.model.coordinates.Coordinates;

import io.reactivex.subjects.PublishSubject;

public class App extends Application {

    private static PublishSubject<Coordinates>subject;

    @Override
    public void onCreate() {
        super.onCreate();
        subject=PublishSubject.create();
    }

    public static PublishSubject<Coordinates> getSubject() {
        return subject;
    }
}
