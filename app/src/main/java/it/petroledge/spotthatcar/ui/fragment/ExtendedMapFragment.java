package it.petroledge.spotthatcar.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import org.greenrobot.eventbus.EventBus;

import it.petroledge.spotthatcar.ui.adapter.CarsMapAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {//@link ExtendedMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExtendedMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExtendedMapFragment extends SupportMapFragment{

    private CarsMapAdapter mCarsMapAdapter;
//    private OnFragmentInteractionListener mListener;

    public ExtendedMapFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExtendedMapFragment.
     */
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//       }

        mCarsMapAdapter = new CarsMapAdapter(this.getContext());
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getMapAsync(mCarsMapAdapter);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }
}
