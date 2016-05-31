package www.icebd.com.suzukibangladesh.reg;

/**
 * Created by Nasir on 11/19/2015.
 */
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.menu.HomeFragment;
import www.icebd.com.suzukibangladesh.menu.MyBikeFragment;
import www.icebd.com.suzukibangladesh.quiz.Quiz;
import www.icebd.com.suzukibangladesh.request.RequestServices;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsMyCart;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.Tools;

/**
 * Created by Nasir on 11/11/2015.
 */
public class Signup extends Fragment implements View.OnClickListener, AsyncResponse {

    EditText name,address,mobile_no,email,password,thana;
    String blood_group;
    Button button;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    Context context;
    CustomDialog customDialog;


    public static Signup newInstance() {
        Signup fragment = new Signup();
        return fragment;
    }

    public Signup() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container,
                false);
        context = getActivity().getApplicationContext();
        setupUI(rootView.findViewById(R.id.parentSignup));
        getActivity().setTitle("Sign Up");

        name = (EditText) rootView.findViewById(R.id.name);
        address = (EditText) rootView.findViewById(R.id.address);
        mobile_no = (EditText) rootView.findViewById(R.id.mobile_no);
        email = (EditText) rootView.findViewById(R.id.email);
        password = (EditText) rootView.findViewById(R.id.password);
        thana = (EditText)rootView.findViewById(R.id.thana);
        button=(Button) rootView.findViewById(R.id.button);

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();


        button.setOnClickListener(this);
       // button.performClick();

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(12);
        return rootView;
       // return  null;
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {

        attemptSignup();
    }

    private void attemptSignup()
    {
        /*if (premiumJomaTask != null)
        {
            return;
        }*/

        // Reset errors.
        name.setError(null);
        email.setError(null);
        password.setError(null);
        mobile_no.setError(null);
        address.setError(null);
        thana.setError(null);

        // Store values at the time of the login attempt.
        String strName = name.getText().toString();
        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();
        String strmobile_no = mobile_no.getText().toString();
        String straddress = address.getText().toString();
        String strthana = thana.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if ( TextUtils.isEmpty(strName) )
        {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(strEmail) )
        {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }
        else if (!Tools.isEmailValid(strEmail))
        {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(strPassword) )
        {
            password.setError(getString(R.string.error_field_required));
            focusView = password;
            cancel = true;
        }
        else if (!Tools.isPasswordValid(strPassword))
        {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(strmobile_no) )
        {
            mobile_no.setError(getString(R.string.error_field_required));
            focusView = mobile_no;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(straddress) )
        {
            address.setError(getString(R.string.error_field_required));
            focusView = address;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(strthana) )
        {
            thana.setError(getString(R.string.error_field_required));
            focusView = thana;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else
        {
            HashMap<String, String> postData = new HashMap<String, String>();
            String auth_key = pref.getString("auth_key","");
            customDialog = new CustomDialog(getActivity());
            if(CheckNetworkConnection.isConnectionAvailable(context) == true)
            {
                if (auth_key != null)
                {
                    postData.put("auth_key",auth_key);
                    postData.put("app_user_name", name.getText().toString());
                    postData.put("app_user_email", email.getText().toString());
                    postData.put("app_user_address", address.getText().toString());
                    postData.put("app_user_phone", mobile_no.getText().toString());
                    postData.put("app_user_password", password.getText().toString() );
                    postData.put("app_user_thana",  thana.getText().toString() );

                    PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
                    loginTask.execute(ConnectionManager.SERVER_URL+"registerUser");
                }
            }
            else
            {
                customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
            }
        }
    }

    @Override
    public void processFinish(String result) {

        Log.i("Test","result: "+result);
        //761607ecb8f9cb0cbb45136b30c69d06

        try {
            JSONObject object = new JSONObject(result);
            String status_code = object.getString("status_code");
            String message = object.getString("message");
            String auth_key = object.getString("auth_key");
            //String user_id = object.getString("user_id");

            FragmentManager fragmentManager = getFragmentManager();
           // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
           // SharedPreferences.Editor editor = preferences.edit();
           // editor.putString("user_id",user_id);
           // editor.apply();

            if (status_code.equals("200"))
            {
                String user_id = object.getString("user_id");
                String user_name = object.getString("user_name");
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                editor.putString("user_name",user_name);
                //editor.putString("email",email.getText().toString());
                //editor.putString("address",address.getText().toString());
                //editor.putString("mobile_no",mobile_no.getText().toString());
                //editor.putString("thana",thana.getText().toString());
                editor.putString("user_id",user_id);
                editor.putString("is_login","1");
                editor.commit();

                /*if(CheckNetworkConnection.isConnectionAvailable(context) == true)
                {
                    Login.newInstance().goToLoginTask(pref,email.getText().toString(),password.getText().toString());
                }
                else
                {
                    customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
                }*/
                ((FirstActivity)getActivity()).setDrawerAdapter();
                if( Constant.isDetermin == 1)// for requrstFor service
                {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    RequestServices requestServices = new RequestServices();
                    fragmentTransaction.replace(R.id.container, requestServices);
                    fragmentTransaction.commit();
                }
                else if( Constant.isDetermin == 2)// for quizz
                {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    Quiz quiz = new Quiz();
                    fragmentTransaction.replace(R.id.container, quiz);
                    fragmentTransaction.commit();
                }
                /*else if( Constant.isDetermin == 3)
                {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    SparePartsMyCart sparePartsMyCart = new SparePartsMyCart();
                    fragmentTransaction.replace(R.id.container, sparePartsMyCart);
                    fragmentTransaction.commit();
                }*/
                else
                {
                    Intent intent = new Intent(getActivity(), FirstActivity.class);
                    startActivity(intent);
                }

            }
            else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
