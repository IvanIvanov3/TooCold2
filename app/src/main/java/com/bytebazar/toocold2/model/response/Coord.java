
package com.bytebazar.toocold2.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Coord {

    @SerializedName("lat")
    @Expose
    public double latitude;
    @SerializedName("lon")
    @Expose
    public double longitude;

}
