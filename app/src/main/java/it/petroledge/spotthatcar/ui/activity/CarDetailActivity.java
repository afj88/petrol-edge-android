package it.petroledge.spotthatcar.ui.activity;

import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.maps.MapView;

import java.io.IOException;

import it.petroledge.spotthatcar.R;

public class CarDetailActivity extends AppCompatActivity {

    public static final String IMAGE_PATH_URI = "IMAGE_PATH_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        Intent intent = getIntent();
        Uri fileUri = Uri.parse(intent.getStringExtra(IMAGE_PATH_URI));

        ImageView carImageView = (ImageView) findViewById(R.id.car_detail_image_view);
        MapView mapView = (MapView) findViewById(R.id.car_detail_map_view);

        if (carImageView != null) {
            carImageView.setImageURI(fileUri);

            try {
                ExifInterface exifInterface = new ExifInterface(fileUri.toString());

                float[] latLong = new float[2];
                if (exifInterface.getLatLong(latLong)) {
                    int x = 0;
                    x++;

                } else {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }
}
