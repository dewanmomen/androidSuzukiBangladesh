package www.icebd.com.suzukibangladesh.maps;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.GPSTracker;
import www.icebd.com.suzukibangladesh.app.LatLong;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsListObject;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;
import www.icebd.com.suzukibangladesh.utilities.JsonParser;


public class MapsActivity extends android.support.v4.app.Fragment implements Filterable,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText searchByDistrict;
    TextView tv;
    double lat, lng;
    LatLng Address;

    APIFactory apiFactory;
    ProgressDialog progressDialog;
    CustomDialog customDialog;
    GetMapLoactionTask getMapLoactionTask;

    Context context;
    private GoogleMap mMap;
    String location_type, location_address, location_contact_person_name, location_contact_person_email, location_contact_person_phone;
    List<LatLng> latlngList = new ArrayList<LatLng>();

    List<MapsLocationObject.Locations> mapsLocationObjectList;
    GPSTracker gps;
    double latitude, longitude;
    private ItemFilter mFilter = new ItemFilter();

    private float currentZoom = -1;
    int timecounter = 0;
    private List<Marker> mMarkers = new ArrayList<Marker>();
    private Marker marker;
    private MapView mMapView;

    public static MapsActivity newInstance() {
        MapsActivity fragment = new MapsActivity();
        return fragment;
    }

    public MapsActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container,
                false);
        searchByDistrict = (EditText) rootView.findViewById(R.id.searchByDistrict);
        tv = (TextView) rootView.findViewById(R.id.textView3);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("Locations");
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);*/
        Typeface iconFont = FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME);
        tv.setText(getResources().getString(R.string.fa_search_icon));
        tv.setTypeface(iconFont);

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        HashMap<String, String> postData = new HashMap<String, String>();
        String auth_key = pref.getString("auth_key", null);
        if ((auth_key != null)) {
            postData.put("auth_key", auth_key);


            if (isNetworkAvailable()) {
                apiFactory = new APIFactory();
                customDialog = new CustomDialog(getActivity());
                getMapLoactionTask = new GetMapLoactionTask(auth_key);
                getMapLoactionTask.execute((Void) null);
            }

        } else {
            Toast.makeText(getActivity(), "Please Connect to the Internet and Restart the app", Toast.LENGTH_LONG).show();

        }

        searchByDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Text ["+s+"]");
                //MapsActivity.this.getFilter().filter(s.toString());

                String answerString = searchByDistrict.getText().toString().toLowerCase();
                if(mapsLocationObjectList != null)
                {
                    MapsLocationObject mapsLocationObject = new MapsLocationObject();
                    Iterator<MapsLocationObject.Locations> list = mapsLocationObjectList.iterator();
                    while (list.hasNext())
                    {
                        final MapsLocationObject.Locations obj_maps_location = (MapsLocationObject.Locations) list.next();
                        if (answerString.equals(obj_maps_location.getDistrict()))
                        {
                            LatLng dha_lat_lng = new LatLng(obj_maps_location.getLat(), obj_maps_location.getLng());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dha_lat_lng, 12.0f));


                        } else if (answerString.length() <= 0)
                        {
                            gps = new GPSTracker(context);
                            if (gps.canGetLocation())
                            {
                                moveToCurrentLocation(new LatLng(gps.getLatitude(), gps.getLongitude()));
                            }
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        return rootView;
    }
    @Override
    public Filter getFilter()
    {
        return mFilter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults result = new FilterResults();
            constraint = constraint.toString().toLowerCase();
            String answerString = searchByDistrict.getText().toString().toLowerCase();
            if(mapsLocationObjectList != null)
            {
                MapsLocationObject mapsLocationObject = new MapsLocationObject();
                Iterator<MapsLocationObject.Locations> list = mapsLocationObjectList.iterator();
                while (list.hasNext())
                {
                    final MapsLocationObject.Locations obj_maps_location = (MapsLocationObject.Locations) list.next();
                    if (answerString.equals(obj_maps_location.getDistrict()))
                    {
                        final LatLng dha_lat_lng = new LatLng(obj_maps_location.getLat(), obj_maps_location.getLng());
                        new Thread(){

                            public synchronized void run()
                            {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dha_lat_lng, 12.0f));
                            }
                        };

                    } else if (answerString.length() <= 0)
                    {
                        gps = new GPSTracker(context);
                        if (gps.canGetLocation())
                        {
                            moveToCurrentLocation(new LatLng(gps.getLatitude(), gps.getLongitude()));
                        }
                    }
                }
            }
            result.count = mapsLocationObjectList.size();
            result.values = mapsLocationObjectList;

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mapsLocationObjectList = (ArrayList<MapsLocationObject.Locations>) results.values;
            //notifyDataSetChanged();
        }

    }

    private void GetMyLocation() {
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    int gpsCounter = 0;
    LatLng loc;

    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {            // iAmHereMarker.remove();

            if (gpsCounter == 0) {

                gpsCounter = 1;
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("TAG", "Current Location: " + loc);

                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12.0f));
                }
            }

        }
    };

    public GoogleMap.OnCameraChangeListener getCameraChangeListener() {
        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                if (currentZoom != position.zoom) {
                    {
                        currentZoom = position.zoom;

                        if (timecounter == 1) {

                            if (currentZoom < 12) {
                                for (int i = 0; i < mMarkers.size(); i++) {
                                    mMarkers.get(i).setVisible(false);
                                }
                            } else {
                                for (int i = 0; i < mMarkers.size(); i++) {
                                    mMarkers.get(i).setVisible(true);
                                }
                            }
                        }


                    }

                }
            }

        };
    }

    private void isGpsEnable() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getContext(), "GPS is Enabled in your device", Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            GetMyLocation();
            mMap.setOnCameraChangeListener(getCameraChangeListener());
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Please Enable GPS First.")
                .setCancelable(false)
                .setPositiveButton("Goto To GPS Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(context, "Connected to Location Service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context, "Connection to Location Service Disrupted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Can not connect to Location Service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        isGpsEnable();
        //marker.showInfoWindow();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.marker_info_window, null);

                TextView tvTitle = ((TextView) v.findViewById(R.id.marker_title));
                tvTitle.setText(marker.getTitle());
                TextView tvSnippet = ((TextView) v.findViewById(R.id.marker_snippet));
                tvSnippet.setText(marker.getSnippet());

                return v;
            }
        });
    }

    public class GetMapLoactionTask extends AsyncTask<Void, Void, String> {
        private String RESULT = "OK";
        //private List<MapsLocationObject.Locations> returnJsonData;
        private ArrayList<NameValuePair> nvp2 = null;
        private InputStream response;
        private JsonParser jsonParser;

        private String methodName = "";
        private String auth_key;

        GetMapLoactionTask(String auth_key) {
            this.auth_key = auth_key;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(getActivity(), null, null);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (ConnectionManager.hasInternetConnection()) {
                    //auth_key = "b78c0c986e4a3d962cd220427bc8ff07";
                    nvp2 = apiFactory.getMapLocationInfo(auth_key);
                    methodName = "getLocation";
                    response = ConnectionManager.getResponseFromServer(methodName, nvp2);
                    jsonParser = new JsonParser();

                    System.out.println("server response : " + response);
                    mapsLocationObjectList = jsonParser.parseAPIMapLocationInfo(response);
                    System.out.println("return data in background : " + mapsLocationObjectList);

                } else {
                    RESULT = getString(R.string.error_no_internet);
                    return RESULT;
                }
                return RESULT;
            } catch (Exception ex) {
                //ex.printStackTrace();
                Log.e("APITask:", ex.getMessage());
                RESULT = getString(R.string.error_sever_connection);
                return RESULT;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (RESULT.equalsIgnoreCase("OK")) {
                try {
                    //finish();

                    if (mapsLocationObjectList.size() > 0 && mapsLocationObjectList != null) {
                        Toast.makeText(context, "inside do post", Toast.LENGTH_SHORT).show();
                        //returnJsonData = mapsLocationObjectList;
                        new PlaceAMarker(mapsLocationObjectList).execute();

                    } else {
                        System.out.println("return data in post execute : " + mapsLocationObjectList);
                        Toast.makeText(context, "Request Data Not Found, Please Try Again !", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e("APITask data error :", ex.getMessage());
                }
            } else {

                customDialog.alertDialog("ERROR", result);
            }
        }

        @Override
        protected void onCancelled() {
            getMapLoactionTask = null;
            progressDialog.dismiss();
        }
    }

    class PlaceAMarker extends AsyncTask<String, String, String> {

        private List<MapsLocationObject.Locations> returnJsonData;
        HashMap<String, String> strLatLngList = new HashMap<String, String>();
        private ArrayList<NameValuePair> nvp2 = null;
        private InputStream response;
        private JsonParser jsonParser;

        private String methodName = "";
        private String auth_key;

        PlaceAMarker(List<MapsLocationObject.Locations> returnJsonData) {
            this.returnJsonData = returnJsonData;
        }


        @Override
        protected String doInBackground(String... params) {

            System.out.println("object size: " + returnJsonData.size());
            Iterator<MapsLocationObject.Locations> list = returnJsonData.iterator();
            String[] strArr = null;
            while (list.hasNext()) {
                MapsLocationObject.Locations mapsLocation = (MapsLocationObject.Locations) list.next();
                System.out.println("address location: " + mapsLocation.getLocation_address());

                String strGetLatLng = getLatLng(mapsLocation.getLocation_address().replaceAll(" ", "%20"));

                if (strGetLatLng != null) {
                    strArr = strGetLatLng.split(",");
                    mapsLocation.setLat(Double.valueOf(strArr[0]));
                    mapsLocation.setLng(Double.valueOf(strArr[1]));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for (int i = 0; i < 1; i++) {
                Toast.makeText(getActivity(), "Red: Show Room\nBlue: Service Center", Toast.LENGTH_LONG).show();

            }

            if (strLatLngList != null) {
                MapsLocationObject mapsLocationObject = new MapsLocationObject();
                Iterator<MapsLocationObject.Locations> list = returnJsonData.iterator();
                while (list.hasNext()) {
                    final MapsLocationObject.Locations obj_maps_location = (MapsLocationObject.Locations) list.next();

                    System.out.println("Return Location Type: " + obj_maps_location.getLocation_type());
                    if (obj_maps_location.getLocation_type().contains("1")) {
                        //String key = (String) strLatLngList.get("1");
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(obj_maps_location.getLat(), obj_maps_location.getLng()))
                                .title(obj_maps_location.getLocation_name())
                                .snippet(obj_maps_location.getLocation_address() + "\n" + obj_maps_location.getLocation_contact_person_phone() + "\n"
                                        + "Show Room")
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        );
                    } else {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(obj_maps_location.getLat(), obj_maps_location.getLng()))
                                .title(obj_maps_location.getLocation_name())
                                .snippet(obj_maps_location.getLocation_address() + "\n" + obj_maps_location.getLocation_contact_person_phone() + "\n" + "Service Center")
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        );
                    }
                }
            } else {
                Toast.makeText(context, "Lat lng Data not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getLatLng(String address) {
        String uri = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address;
        System.out.println("url: " + uri);
        HttpGet httpGet = new HttpGet(uri);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        String rtnLatLng = null;

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();

            InputStream stream = entity.getContent();

            int byteData;
            while ((byteData = stream.read()) != -1) {
                stringBuilder.append((char) byteData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        double lat = 0.0, lng = 0.0;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            rtnLatLng = lat + "," + lng;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rtnLatLng;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        //Zoom in, animating the camera.

        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        //Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


    }
}
