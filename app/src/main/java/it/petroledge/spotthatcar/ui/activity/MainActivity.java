package it.petroledge.spotthatcar.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import it.petroledge.spotthatcar.R;
import it.petroledge.spotthatcar.entity.CarEntity;
import it.petroledge.spotthatcar.ui.fragment.GalleryFragment;
import it.petroledge.spotthatcar.ui.fragment.ExtendedMapFragment;
import it.petroledge.spotthatcar.util.FileUtil;

public class MainActivity extends AppCompatActivity implements
        GalleryFragment.OnListFragmentInteractionListener,
        View.OnClickListener,
        LocationListener {

    private static final int WRITE_EXTERNAL_PERMISSION = 255;
    private static final int ACCESS_FINE_LOCATION_PERMISSION = 254;

    private static final String CAPTURED_PHOTO_URI = "CAPTURED_PHOTO_URI";
    private static final String LAST_KNOWN_LOCATION = "LAST_KNOWN_LOCATION";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Uri mFileUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private LocationManager mLocationManager;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton camera_fab = (FloatingActionButton) findViewById(R.id.camera_fab);
        if (camera_fab != null) {
            camera_fab.setOnClickListener(this);
        }

        mLocationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);


        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, WRITE_EXTERNAL_PERMISSION);
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mFileUri != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI, mFileUri.toString());
        }

        if (mLastKnownLocation != null) {
            savedInstanceState.putParcelable(LAST_KNOWN_LOCATION, mLastKnownLocation);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI)) {
            mFileUri = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI));
        }

        if (savedInstanceState.containsKey(LAST_KNOWN_LOCATION)) {
            mLastKnownLocation = savedInstanceState.getParcelable(LAST_KNOWN_LOCATION);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.removeUpdates(this);

            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                galleryAddPic(mFileUri);

                Intent intent = new Intent(this, CarDetailActivity.class);
                intent.putExtra(CarDetailActivity.IMAGE_PATH_URI, mFileUri.toString());
                intent.putExtra(CarDetailActivity.LOCATION, mLastKnownLocation);

                startActivity(intent);
            }
//            else if (resultCode != RESULT_CANCELED) {
//                // User cancelled the image capture
//                // Who cares ? ^_^
//            }
            else {
                // Image capture failed, advise user
                // I care!! O_O
            }
        }
    }

    private void galleryAddPic(Uri contentUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListFragmentInteraction(CarEntity item) {
        int x = 0;
        x++;
    }

    //region OnClick Listener

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFileUri = FileUtil.getOutputImageFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, ACCESS_FINE_LOCATION_PERMISSION);

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    //endregion

    //region Location Listener

    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //endregion

    //region Section Page Adapter

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ExtendedMapFragment();
                case 1:
                    return new GalleryFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "Gallery";
            }
            return null;
        }
    }

    //endregion
}
