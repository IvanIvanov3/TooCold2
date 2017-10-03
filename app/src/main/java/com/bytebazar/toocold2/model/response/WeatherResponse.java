
package com.bytebazar.toocold2.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class WeatherResponse {

    @SerializedName("cnt")
    @Expose
    public int cnt;
    @SerializedName("list")
    @Expose
    public java.util.List<HourlyWeather> list = null;
    @SerializedName("city")
    @Expose
    public City city;

}
