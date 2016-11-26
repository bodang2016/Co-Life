package com.example.bodang.co_life.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bodang.co_life.Activities.MainActivity;
import com.example.bodang.co_life.Objects.User;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bodang.co_life.R;
import com.google.android.gms.vision.barcode.Barcode;


import java.util.ArrayList;


import static com.example.bodang.co_life.Activities.MainActivity.client;
import static com.example.bodang.co_life.R.menu.main;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private updateGrouplocationTask mupdateGrouplocationTask = null;
    private View rootView;
    MapView mMapView;
    double latitude;
    double longitude;
    String username;
    MarkerOptions markerOptions;
    Marker marker;


    private GoogleMap googleMap;
    protected Context context;

    // TODO: Rename and change types of parameters
    private View main;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //main = inflater.inflate(R.layout.fragment_list, container, false);
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);


//                LatLng sydney = new LatLng(-34, 151);
//                LatLng dublin = new LatLng(53.3498, -6.2603);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Sydney").snippet("Marker Description"));
//                googleMap.addMarker(new MarkerOptions().position(dublin).title("Dublin").snippet("Marker Description"));
//

                LatLng dublin = new LatLng(53.3498, -6.2603);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(dublin).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng latLng) {


//                        Location location = new Location("Test");
//                        location.setLatitude(latLng.latitude);
//                        location.setLongitude(latLng.longitude);
//                        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//                         markerOptions = new MarkerOptions()
//                                .position(newLatLng)
//                                .title(newLatLng.toString());
//
//                        googleMap.addMarker(markerOptions);


                        marker = googleMap.addMarker(new MarkerOptions()
                                .position(
                                        new LatLng(latLng.latitude,
                                                latLng.longitude)).title(latLng.toString())
                                .draggable(true).visible(true));
                        Toast.makeText(getActivity(), "the location is" + latLng.toString(),
                                Toast.LENGTH_LONG).show();


                    }
                });

                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        // TODO Auto-generated method stub
                        //Here your code
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        // TODO Auto-generated method stub
                        marker.remove();

                    }
                });

            }
        });


        return rootView;


        //return inflater.inflate(R.layout.fragment_map, container, false);
    }

    public void onResume() {
        super.onResume();
        mupdateGrouplocationTask = new updateGrouplocationTask(MainActivity.UnameValue);
        mupdateGrouplocationTask.execute((Void) null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href= "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a > for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class updateGrouplocationTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private ArrayList<User> groupList = null;

        public updateGrouplocationTask(String userName) {
            super();
            mUsername = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = client.Init();
            if (result == 1) {
                groupList = client.groupList(mUsername);
                if (!groupList.isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                for (int i = 0; i < groupList.size(); i++) {
                    //TO DO
                    latitude = groupList.get(i).getLatitude(); //这样获取
                    longitude = groupList.get(i).getLongtitude();
                    username = groupList.get(i).getUserId();

                    if (username != MainActivity.UnameValue) {
                        LatLng userlocation = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(userlocation).title(username).snippet("Last update location time:" + groupList.get(i).getTime()));

                    }

                    if (username == MainActivity.UnameValue) {
                        System.out.println("my" + MainActivity.UnameValue);
                        LatLng my = new LatLng(latitude, longitude);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(my).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    }


                }


                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        Toast.makeText(getActivity(), "the user is" + username,
                                Toast.LENGTH_LONG).show();
                    }
                });


            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mupdateGrouplocationTask = null;
        }
    }
}