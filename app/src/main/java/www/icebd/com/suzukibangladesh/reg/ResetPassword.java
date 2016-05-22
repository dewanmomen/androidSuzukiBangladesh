package www.icebd.com.suzukibangladesh.reg;

/**
 * Created by Nasir on 11/19/2015.
 */
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.Tools;


public class ResetPassword extends Fragment implements View.OnClickListener, AsyncResponse {

    EditText email;
    Button btnReset;
    FragmentActivity activity;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    Context context;
    CustomDialog customDialog;

    public static ResetPassword newInstance() {
        ResetPassword fragment = new ResetPassword();
        return fragment;
    }

    public ResetPassword() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reset_password, container,
                false);
        context = getActivity().getApplicationContext();
        setupUI(rootView.findViewById(R.id.parentResetPass));
        getActivity().setTitle("FORGOT PASSWORD");

        email=(EditText) rootView.findViewById(R.id.reset_email);
        btnReset = (Button) rootView.findViewById(R.id.button);

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        btnReset.setOnClickListener(this);

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(12);

        return rootView;
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
        attemptForgotPassword();

    }

    private void attemptForgotPassword()
    {
        /*if (premiumJomaTask != null)
        {
            return;
        }*/

        // Reset errors.
        email.setError(null);

        // Store values at the time of the login attempt.
        String strEmail = email.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if ( TextUtils.isEmpty(strEmail) )
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

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else
        {
            HashMap<String, String> postData = new HashMap<String, String>();
            String auth_key = pref.getString("auth_key",null);
            //String user_id = pref.getString("user_id",null);
            customDialog = new CustomDialog(getActivity());
            if(CheckNetworkConnection.isConnectionAvailable(context) == true)
            {
                if (auth_key != null)
                {
                    postData.put("auth_key",auth_key);
                    postData.put("user_email",email.getText().toString());
                    //postData.put("user_id", user_id);
                    PostResponseAsyncTask loginTask = new PostResponseAsyncTask(ResetPassword.this,postData);
                    loginTask.execute(ConnectionManager.SERVER_URL+"forgetPassword");
                }
            }
            else
            {
                customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
            }
        }
    }
    @Override
    public void processFinish(String output) {

        Log.i("Test","After forgot password"+ output);

        JSONObject object = null;
        try {
            object = new JSONObject(output);
            String status_code = object.getString("status_code");
            String message = object.getString("message");
            FragmentManager fragmentManager = getFragmentManager();



            if (status_code.equals("200"))
            {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

                // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AfterResetPassword.newInstance())
                        .commit();

            }
            else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
