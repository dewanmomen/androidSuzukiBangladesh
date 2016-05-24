package www.icebd.com.suzukibangladesh.menu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.app.GPSTracker;
import www.icebd.com.suzukibangladesh.dealer_list.Custom_dealer_list;
import www.icebd.com.suzukibangladesh.dealer_list.DealerSearchAdapter;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.maps.MapsActivity;
import www.icebd.com.suzukibangladesh.maps.MapsLocationObject;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;
import www.icebd.com.suzukibangladesh.utilities.JsonParser;


public class LocationsFragment extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<HashMap<String, String>> arrList;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    Context context;
    CustomDialog customDialog;

    private List<Custom_dealer_list> dealer_lists = new ArrayList<Custom_dealer_list>();
    private ListView lv;
    private ArrayAdapter<Custom_dealer_list> adapter;
    private ArrayAdapter<Custom_dealer_list> search_adapter;

    List<MapsLocationObject.Locations> mapsLocationObjectList;

    ProgressDialog progressDialog;
    APIFactory apiFactory;
    GetMapLoactionTask getMapLoactionTask;

    EditText searchByLocations;
    TextView tv;

    private DealerSearchAdapter dealerSearchAdapter;


    public static LocationsFragment newInstance() {
        LocationsFragment fragment = new LocationsFragment();
        return fragment;
    }

    public LocationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("Locations");

        searchByLocations = (EditText) rootView.findViewById(R.id.searchByLocations);
        tv = (TextView) rootView.findViewById(R.id.location_icon);

        Typeface iconFont = FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME);
        tv.setText(getResources().getString(R.string.l_search));
        tv.setTypeface(iconFont);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null).cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        String auth_key = pref.getString("auth_key", "empty");

        customDialog = new CustomDialog(getActivity());
        if (CheckNetworkConnection.isConnectedToInternet(context) == true) {
            if (!auth_key.equals("empty")) {

                // call async task here
                apiFactory = new APIFactory();
                customDialog = new CustomDialog(getActivity());
                getMapLoactionTask = new GetMapLoactionTask(auth_key);
                getMapLoactionTask.execute((Void) null);
            }
        } else {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }

        ((FirstActivity) getActivity()).setBackKeyFlag(true);
        ((FirstActivity) getActivity()).setWhichFragment(0);

        searchByLocations.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dealerSearchAdapter = new DealerSearchAdapter(getActivity(), dealer_lists);
                dealerSearchAdapter.getFilter().filter(s.toString());
                lv.setAdapter(dealerSearchAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    private void populateListView() {
        adapter = new DealerListAdapter();
        adapter.clear();
        if (mapsLocationObjectList != null) {
            Log.d("TAG", "Entered checking");
            MapsLocationObject mapsLocationObject = new MapsLocationObject();
            Iterator<MapsLocationObject.Locations> list = mapsLocationObjectList.iterator();
            while (list.hasNext()) {
                final MapsLocationObject.Locations obj_maps_location = (MapsLocationObject.Locations) list.next();
                dealer_lists.add(new Custom_dealer_list(obj_maps_location.getLocation_name(), obj_maps_location.getLocation_contact_person_name()
                        , obj_maps_location.getLocation_address(), obj_maps_location.getLocation_contact_person_phone()
                        , obj_maps_location.getLocation_type()));

            }
            lv = (ListView) getActivity().findViewById(R.id.list_of_dealer);
            lv.setAdapter(adapter);
        }
    }

    public class DealerListAdapter extends ArrayAdapter<Custom_dealer_list> {
        //ViewHolderItem viewHolder;

        Bundle bundle = new Bundle();
        public DealerListAdapter() {
            super(getActivity(), R.layout.custom_dealer_list, dealer_lists);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflater.inflate(R.layout.custom_dealer_list, parent, false);
            }

            Typeface iconFont = FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME);

            final Custom_dealer_list custom_dealer_list = dealer_lists.get(position);

            TextView shop_title = (TextView) itemView.findViewById(R.id.shop_title);
            TextView contact_person_icon = (TextView) itemView.findViewById(R.id.contact_person_icon);
            TextView contact_person = (TextView) itemView.findViewById(R.id.contact_person);
            TextView shop_address_icon = (TextView) itemView.findViewById(R.id.shop_address_icon);
            TextView shop_address = (TextView) itemView.findViewById(R.id.shop_address);
            TextView mobile_number_icon = (TextView) itemView.findViewById(R.id.mobile_number_icon);
            TextView mobile_number = (TextView) itemView.findViewById(R.id.mobile_number);
            TextView map_icon = (TextView) itemView.findViewById(R.id.map_icon);
            TextView show_on_map = (TextView) itemView.findViewById(R.id.show_on_map);
            TextView caller_icon = (TextView) itemView.findViewById(R.id.caller_icon);
            TextView caller = (TextView) itemView.findViewById(R.id.caller);
            TextView shop_type = (TextView) itemView.findViewById(R.id.shop_type);

            shop_title.setText(custom_dealer_list.getTitle());

            contact_person_icon.setText(getResources().getString(R.string.l_contact_person_icon));
            contact_person_icon.setTypeface(iconFont);
            contact_person.setText(custom_dealer_list.getContact_person());

            shop_address_icon.setText(getResources().getString(R.string.l_address));
            shop_address_icon.setTypeface(iconFont);
            shop_address.setText(custom_dealer_list.getAddress());

            mobile_number_icon.setText(getResources().getString(R.string.l_mobile_number));
            mobile_number_icon.setTypeface(iconFont);
            mobile_number.setText(custom_dealer_list.getMobile_number());

            map_icon.setText(getResources().getString(R.string.l_map));
            map_icon.setTypeface(iconFont);
            show_on_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapsActivity mapsActivity = new MapsActivity();
                    bundle.putString("s_title", custom_dealer_list.getTitle());
                    bundle.putInt("checking_key", 1);
                    mapsActivity.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, mapsActivity);
                    fragmentTransaction.commit();
                    /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MapsActivity.newInstance())
                            .commit();*/
                }
            });

            caller_icon.setText(getResources().getString(R.string.l_mobile_number));
            caller_icon.setTypeface(iconFont);
            caller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dialer_number = "tel:" + custom_dealer_list.getMobile_number();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(dialer_number));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    getActivity().startActivity(callIntent);
                }
            });

            if (custom_dealer_list.getShop_type().equals("1")) {
                shop_type.setText("Show Room");
            } else {
                shop_type.setText("Service Center");
            }

            return itemView;
        }

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
                        populateListView();

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
}
