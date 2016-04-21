package it.petroledge.spotthatcar.model;

import com.google.gson.annotations.SerializedName;
import com.vincentbrison.openlibraries.android.dualcache.lib.SizeOf;

import java.util.Date;
import java.util.UUID;

/**
 * Created by alessandro on 27/03/16.
 */
public class CarModel {

    @SerializedName("id")
    private long id;
    @SerializedName("src")
    private String src;
    @SerializedName("cat")
    private String cat;
    @SerializedName("model")
    private String model;
    @SerializedName("lng")
    private double lng;
    @SerializedName("lat")
    private double mLat;
    @SerializedName("cat_color")
    private String mCategoryColorHex;
    @SerializedName("feed_date")
    private Date mFeedDate;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        this.mLat = lat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategoryColorHex() {
        return mCategoryColorHex;
    }

    public void setCategoryColorHex(String categoryColorHex) {
        this.mCategoryColorHex = categoryColorHex;
    }

    public Date getFeedDate() {
        return mFeedDate;
    }

    public void setFeedDate(Date feedDate) {
        this.mFeedDate = feedDate;
    }
}

