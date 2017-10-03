
package com.bytebazar.toocold2.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Temp {

    @SerializedName("temp")
    @Expose
    public double temp;
}
