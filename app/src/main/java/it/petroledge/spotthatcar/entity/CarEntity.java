package it.petroledge.spotthatcar.entity;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import it.petroledge.spotthatcar.model.CarModel;

/**
 * Created by alessandro on 27/03/16.
 */
public class CarEntity {

    private CarModel mCarModel;
    private LatLng mPosition;
    private BitmapDescriptor mMarkerBitmapDescriptor;

    public LatLng getPosition() {
        return mPosition;
    }

    public CarEntity() {

    }

    public CarEntity(CarModel model) {
        this.mCarModel = model;
        mPosition = new LatLng(mCarModel.getLat(), mCarModel.getLng());

        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(mCarModel.getCategoryColorHex()), hsv);
        mMarkerBitmapDescriptor = BitmapDescriptorFactory.defaultMarker(hsv[0]);

    }

    public String getName() {
        return mCarModel.getModel();
    }

    public BitmapDescriptor getMarkerBitmapDescriptor() {
        return mMarkerBitmapDescriptor;
    }

    public String getImageURI() {
        return mCarModel.getSrc();
    }
}
