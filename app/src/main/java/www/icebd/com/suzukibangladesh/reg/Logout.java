package www.icebd.com.suzukibangladesh.reg;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.menu.HomeFragment;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;


public class Logout extends Fragment implements View.OnClickListener, AsyncResponse {
    EditText password,email;
    TextView text;
    Button button;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    public static Logout newInstance() {
        Logout fragment = new Logout();
        return fragment;
    }

    public Logout() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logout, container,
                false);

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        text = (TextView)rootView.findViewById(R.id.text) ;
        button=(Button) rootView.findViewById(R.id.btn_logout);


        button.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (isNetworkAvailable()) {
            HashMap<String, String> postData = new HashMap<String, String>();
            String auth_key = pref.getString("auth_key",null);
            String user_id = pref.getString("user_id",null);
            String is_login = pref.getString("is_login","0");
            if (auth_key==null)
            {
                Toast.makeText(getActivity(),"Please Connect to the Internet and Restart the app",Toast.LENGTH_LONG).show();


            }
            else if(is_login.equals("0"))
            {
                Toast.makeText(getActivity(),"You are not logged In :)",Toast.LENGTH_LONG).show();


            }
            else{
                postData.put("auth_key",auth_key);
                postData.put("user_id",user_id);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
                loginTask.execute(ConnectionManager.SERVER_URL+"logout");

            }
        }
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
        JSONObject object = null;
        try {
            object = new JSONObject(output);
            String status_code = object.getString("status_code");
            String message = object.getString("message");
            FragmentManager fragmentManager = getFragmentManager();



            if (status_code.equals("200"))
            {
              //  Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

                // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                button.setVisibility(View.INVISIBLE);
                text.setVisibility(View.VISIBLE);

                editor.remove("user_id");
                editor.putString("is_login","0");
                Constant.isDetermin = -1; // for go to HOme UI
                editor.apply();




            }
            else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
