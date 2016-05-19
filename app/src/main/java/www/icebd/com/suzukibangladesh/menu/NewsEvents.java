package www.icebd.com.suzukibangladesh.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;


public class NewsEvents extends Fragment implements AsyncResponse {
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    ArrayList<HashMap<String, String>> arrList;
    ListView list;
    ImageLoader imageLoader;
    private NewsEventsListAdapter newsEventsListAdapter = null;

    public static NewsEvents newInstance() {
        NewsEvents fragment = new NewsEvents();
        return fragment;
    }

    public NewsEvents() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_events, container,
                false);
        getActivity().setTitle("NEWS AND EVENTS");
        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        list = (ListView) rootView.findViewById(R.id.list);
        imageLoader = ImageLoader.getInstance();


        if(isNetworkAvailable())
        {
            Log.i("Test", "Network is available ");
            HashMap<String, String> postData = new HashMap<String, String>();
            String auth_key = pref.getString("auth_key","");
            Log.i("Test","Auth Key from shared preference "+auth_key);

            if ((auth_key!=""))
                postData.put("auth_key",auth_key);




            PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
            loginTask.execute(ConnectionManager.SERVER_URL+"newsList");

        }
        else {
            Toast.makeText(getActivity(),"Please connect to the Internet",Toast.LENGTH_LONG).show();
        }

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void processFinish(String output) {

        Log.i("Test",output);

        this.arrList = new ArrayList();

        try {
            JSONObject object = new JSONObject(output);
            String status_code = object.getString("status_code");
           /* String message = object.getString("message");
            String auth_key = object.getString("auth_key");
*/
            if(status_code.equals("200"))
            {
                JSONArray news = object.getJSONArray("news");

                for (int i = 0; i <news.length() ; i++) {

                    Log.i("Test","Inside news loop");

                    JSONObject newsDetails = news.getJSONObject(i);
                    HashMap<String, String> map = new HashMap();
                    map.put("news_event_id", newsDetails.getString("news_event_id"));
                    map.put("type", newsDetails.getString("type"));
                    map.put("news_event_title", newsDetails.getString("news_event_title"));
                    map.put("news_event_desc", newsDetails.getString("news_event_desc"));
                    map.put("news_event_img_url", newsDetails.getString("news_event_img_url"));
                    map.put("start_date", newsDetails.getString("start_date"));
                    map.put("end_date", newsDetails.getString("end_date"));

                    arrList.add(map);




                }




            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Test","Arraylist: "+arrList.toString());
        if (!arrList.isEmpty())
        {

            /*ListAdapter adapter = new SimpleAdapter( getActivity(), arrList,
                    R.layout.list_item_news, new String[] { "news_event_title", "news_event_desc","news_event_img_url"},
                    new int[] { R.id.txt_title, R.id.description, R.id.image_list });*/

            newsEventsListAdapter = new NewsEventsListAdapter(getActivity(), arrList, this);
            list.setAdapter(newsEventsListAdapter);
            //imageLoader.displayImage(String.valueOf(Uri.parse(arrList.get(4).toString())), imageView);


            //setListAdapter (adapter);
        }


    }
}
