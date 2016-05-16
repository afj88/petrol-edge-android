package it.petroledge.spotthatcar.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.petroledge.spotthatcar.R;
import it.petroledge.spotthatcar.entity.CarEntity;
import it.petroledge.spotthatcar.manager.LoadedCarsEvent;

/**
 * Created by friz on 22/04/16.
 */
public class CarsMapAdapter implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private final Context mContext;
    private final View mInfoWindow;

    private GoogleMap mMap;
    private Map<String, String> mMarkerIdsMap;
    private String mLastId;
    private boolean mIsMapReady = false;
    private List<CarEntity> mCarEntityList;
//    private Set<PicassoMarker> markerSet = new HashSet<>();

    public CarsMapAdapter(Context context) {
        mContext = context;
        mInfoWindow = LayoutInflater.from(context).inflate(R.layout.bubble_marker_view, null);
        mMarkerIdsMap = new HashMap<>();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);

        mIsMapReady = true;

        drawOnMap();
    }

    @Subscribe
    public void onLoadedCarsEvent(LoadedCarsEvent event) {
        mCarEntityList = event.getPhotos();
        drawOnMap();
    }

    private void drawOnMap() {
        if (mIsMapReady && mCarEntityList != null) {
            mMarkerIdsMap = new HashMap<>();
            mMap.clear();

            for (CarEntity pe : mCarEntityList) {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(pe.getPosition());
                markerOptions.title(pe.getName());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.black_marker));
                Marker marker = mMap.addMarker(markerOptions);

                mMarkerIdsMap.put(marker.getId(), pe.getImageURI());
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCarEntityList.get(0).getPosition(), 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (marker.getId().equals(mLastId))
            return  mInfoWindow;

        mLastId = marker.getId();
        String uri = mMarkerIdsMap.get(marker.getId());

        ImageView iwPhotoHolder = (ImageView) mInfoWindow.findViewById(R.id.info_window_photo_holder);
//        if (iwPhotoHolder.getDrawable() == null) {
            Picasso.with(mContext).load(uri).into(iwPhotoHolder, new Callback() {
                @Override
                public void onSuccess() {
                    mInfoWindow.findViewById(R.id.info_window_loader).setVisibility(View.GONE);
//                    marker.showInfoWindow();
                }

                @Override
                public void onError() {

                }
            });
//        }
        return mInfoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
