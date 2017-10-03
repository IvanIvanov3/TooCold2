package com.bytebazar.toocold2.model;

public final class CurrentValues<T, D> {
    public final T currentHourlyTemp;
    public final D currentHourlyDescription;

    public CurrentValues(T currentHourlyTemp, D currentHourlyDescription) {
        this.currentHourlyTemp = currentHourlyTemp;
        this.currentHourlyDescription = currentHourlyDescription;
    }
}

