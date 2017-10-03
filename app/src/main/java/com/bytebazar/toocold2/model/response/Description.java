
package com.bytebazar.toocold2.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Description {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("main")
    @Expose
    public String main;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("icon")
    @Expose
    public String icon;

}
