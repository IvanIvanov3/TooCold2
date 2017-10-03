
package com.bytebazar.toocold2.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class HourlyWeather {

    @SerializedName("dt")
    @Expose
    public long time;
    @SerializedName("main")
    @Expose
    public Temp temp;
    @SerializedName("weather")
    @Expose
    public java.util.List<Description> weather = null;

}
