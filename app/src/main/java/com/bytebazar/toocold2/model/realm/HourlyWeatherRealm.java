package com.bytebazar.toocold2.model.realm;

import io.realm.RealmObject;

public  class HourlyWeatherRealm extends RealmObject {
    public HourlyWeatherRealm() {
    }

    private long time;
    private int temp;
    private int id;
    private String main;
    private String description;
    private String icon;

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFormattedTemp() {
        return getTemp()+"\u00B0";
    }
}
