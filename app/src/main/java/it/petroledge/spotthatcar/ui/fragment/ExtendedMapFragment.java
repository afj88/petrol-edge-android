package it.petroledge.spotthatcar.ui.fragment;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import it.petroledge.spotthatcar.entity.CarEntity;
import it.petroledge.spotthatcar.manager.LoadedCarsEvent;
import it.petroledge.spotthatcar.ui.view.PicassoMarker;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {//@link ExtendedMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExtendedMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExtendedMapFragment extends SupportMapFragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static final LatLng CISLAGO = new LatLng(45.6545658, 8.9673972);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;

    public ExtendedMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment ExtendedMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExtendedMapFragment newInstance() {
        ExtendedMapFragment fragment = new ExtendedMapFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//       }


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Subscribe
    public void onLoadedCarsEvent(LoadedCarsEvent event) {

        for (CarEntity pe : event.getPhotos()) {

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(pe.getPosition());
            markerOptions.title(pe.getName());
            Marker marker = mMap.addMarker(markerOptions);

            PicassoMarker picassoMarker = new PicassoMarker(marker);
            Picasso.with(getContext()).load(pe.getImageURI()).into(picassoMarker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(event.getPhotos().get(0).getPosition(), 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }
}
