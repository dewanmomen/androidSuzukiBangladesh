package www.icebd.com.suzukibangladesh.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.bikedetails.BikeDetails;
import www.icebd.com.suzukibangladesh.bikelist.BikeList;
import www.icebd.com.suzukibangladesh.bikelist.BikeListSwipeListAdapter;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.JsonParser;


public class MyBikeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private TextView no_bike_item;
    private BikeListSwipeListAdapter bikeListSwipeListAdapter;
    private List<BikeList.BikeItem> bikeList;

    private FetchBikeListTask fetchBikeListTask = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    APIFactory apiFactory;
    CustomDialog customDialog;
    ProgressDialog progressDialog;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;


    public static MyBikeFragment newInstance() {
        MyBikeFragment fragment = new MyBikeFragment();
        return fragment;
    }

    public MyBikeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bike, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("MY BIKE");
        pref = context.getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();
        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Log.e("Test : ","inside my bike list");

        listView = (ListView) view.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        no_bike_item = (TextView) view.findViewById(R.id.no_bike_item);
        no_bike_item.setVisibility(View.GONE);

        bikeList = new ArrayList<>();
        //bikeListSwipeListAdapter = new BikeListSwipeListAdapter(context, bikeList,MyBikeFragment.this);
        //listView.setAdapter(bikeListSwipeListAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        apiFactory = new APIFactory();
                                        customDialog = new CustomDialog(context);
                                        if(CheckNetworkConnection.isConnectionAvailable(context) == true)
                                        {
                                            fetchBikeListTask = new FetchBikeListTask(pref.getString("auth_key", null));
                                            fetchBikeListTask.execute((Void) null);
                                            //fetchBikeList();
                                        }
                                        else
                                        {
                                            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
                                        }
                                    }
                                }
        );

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);
        return view;
    }
    public void goBikeDetails(int bike_id)
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        BikeDetails fragmentBikeDetails = new BikeDetails();
        Bundle bundle = new Bundle();
        bundle.putInt( "bike_id", bike_id );
        fragmentBikeDetails.setArguments(bundle);
        ft.replace(R.id.container, fragmentBikeDetails);
        // ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onRefresh()
    {
        apiFactory = new APIFactory();
        customDialog = new CustomDialog(getActivity());
        fetchBikeListTask = new FetchBikeListTask(pref.getString("auth_key",null));
        fetchBikeListTask.execute((Void) null);
    }
    public class FetchBikeListTask extends AsyncTask<Void, Void, String>
    {
        private String RESULT = "OK";
        private ArrayList<BikeList> returnJsonData;
        private ArrayList<NameValuePair> nvp2=null;
        private String device_type = "1";
        private String device_token = "device_token";
        private String udid = "udid";
        private InputStream response;
        private JsonParser jsonParser;

        private String methodName = "";
        private String GCMkey = "";
        private String DeviceID = "udid";
        private String auth_key;

        //UserLoginTask(String email, String password)
        FetchBikeListTask(String auth_key)
        {
            this.auth_key = auth_key;
        }

        @Override
        protected void onPreExecute() {

            //progressDialog = ProgressDialog.show(getActivity(), null, null);
        }
        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                if (ConnectionManager.hasInternetConnection())
                {
                    //auth_key = "b78c0c986e4a3d962cd220427bc8ff07";
                    nvp2 = apiFactory.getBikeListInfo(auth_key);
                    methodName = "getBikeList";
                    response = ConnectionManager.getResponseFromServer(methodName, nvp2);
                    jsonParser = new JsonParser();

                    System.out.println("server response : "+response);
                    returnJsonData = jsonParser.parseAPIgetBikeListInfo(response);
                    System.out.println("return data : " + returnJsonData);

                }
                else
                {
                    RESULT = getString(R.string.error_no_internet);
                    return RESULT;
                }
                return RESULT;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Log.e("APITask:", ex.getMessage());
                RESULT = getString(R.string.error_sever_connection);
                return RESULT;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            //progressDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);
            if(RESULT.equalsIgnoreCase("OK"))
            {
                try
                {
                    //finish();
                    if (returnJsonData.size() > 0 && returnJsonData != null && returnJsonData.get(0).isStatus() == true )
                    {
                        //preferenceUtil.setPINstatus(1);
                        Toast.makeText(getActivity(), returnJsonData.get(0).getMessage(), Toast.LENGTH_SHORT).show();

                        bikeList = returnJsonData.get(0).getBikeItemsList();
                        bikeListSwipeListAdapter = new BikeListSwipeListAdapter(context, bikeList,MyBikeFragment.this);
                        listView.setAdapter(bikeListSwipeListAdapter);

                        bikeListSwipeListAdapter.notifyDataSetChanged();
                    } else
                    {
                        System.out.println("data return : " + returnJsonData);
                        Toast.makeText(getActivity(), "Request Data Not Found, Please Try Again !", Toast.LENGTH_SHORT).show();
                        listView.setVisibility(View.GONE);
                        no_bike_item.setVisibility(View.VISIBLE);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    Log.e("APITask data error :", ex.getMessage());
                }
            }
            else
            {
                customDialog.alertDialog("ERROR", result);
            }
        }
        @Override
        protected void onCancelled()
        {
            fetchBikeListTask = null;
            //progressDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
