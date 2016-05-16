package it.petroledge.spotthatcar.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import it.petroledge.spotthatcar.R;

public class CarDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String IMAGE_PATH_URI = "IMAGE_PATH_URI";
    public static final String LOCATION = "LOCATION";

    private Uri mFileUri;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        Intent intent = getIntent();
        mFileUri = Uri.parse(intent.getStringExtra(IMAGE_PATH_URI));
        mLocation = intent.getParcelableExtra(LOCATION);

        ImageView carImageView = (ImageView) findViewById(R.id.car_detail_image_view);
        MapView mapView = (MapView) findViewById(R.id.car_detail_map_view);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }

        mapView.onCreate(savedInstanceState);

        if (carImageView != null) {
            carImageView.setImageURI(mFileUri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MapView mapView = (MapView) findViewById(R.id.car_detail_map_view);

        if (mapView != null) {
            mapView.getMapAsync(this);
        }

        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mLocation != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            markerOptions.position(latLng);

            googleMap.addMarker(markerOptions);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(30), 1500, null);
        }
    }
}
