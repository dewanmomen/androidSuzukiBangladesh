package www.icebd.com.suzukibangladesh.reg;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.Tools;


public class ChangePassword extends Fragment implements View.OnClickListener, AsyncResponse {


    EditText oldPassword,newPassword,verify_password;

    Button button;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    Context context;
    CustomDialog customDialog;


    public static ChangePassword newInstance() {
        ChangePassword fragment = new ChangePassword();
        return fragment;
    }

    public ChangePassword() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.change_password, container,
                false);
        context = getActivity().getApplicationContext();
        setupUI(rootView.findViewById(R.id.parentChangePass));
        getActivity().setTitle("CHANGE PASSWORD");

        oldPassword = (EditText) rootView.findViewById(R.id.old_password);
        newPassword = (EditText) rootView.findViewById(R.id.new_password);
        verify_password = (EditText) rootView.findViewById(R.id.verify_password);
        button=(Button) rootView.findViewById(R.id.button);

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        button.setOnClickListener(this);

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);
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

        attemptChangePassword();

    }
    private void attemptChangePassword()
    {
        /*if (premiumJomaTask != null)
        {
            return;
        }*/

        // Reset errors.
        oldPassword.setError(null);
        newPassword.setError(null);
        verify_password.setError(null);

        // Store values at the time of the login attempt.
        String stroldPassword = oldPassword.getText().toString();
        String strnewPassword = newPassword.getText().toString();
        String strverify_password = verify_password.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if ( TextUtils.isEmpty(stroldPassword) )
        {
            oldPassword.setError(getString(R.string.error_field_required));
            focusView = oldPassword;
            cancel = true;
        }
        else if (!Tools.isPasswordValid(stroldPassword))
        {
            oldPassword.setError(getString(R.string.error_invalid_password));
            focusView = oldPassword;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(strnewPassword) )
        {
            newPassword.setError(getString(R.string.error_field_required));
            focusView = newPassword;
            cancel = true;
        }
        else if (!Tools.isPasswordValid(strnewPassword))
        {
            newPassword.setError(getString(R.string.error_invalid_password));
            focusView = newPassword;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(strverify_password) )
        {
            verify_password.setError(getString(R.string.error_field_required));
            focusView = verify_password;
            cancel = true;
        }
        else if (!Tools.isPasswordValid(strverify_password))
        {
            verify_password.setError(getString(R.string.error_invalid_password));
            focusView = verify_password;
            cancel = true;
        }
        else if (strnewPassword.equals(strverify_password) == false)
        {
            verify_password.setError(getString(R.string.error_pass_missmatch));
            focusView = verify_password;
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
            String user_id = pref.getString("user_id",null);
            customDialog = new CustomDialog(context);
            if(CheckNetworkConnection.isConnectionAvailable(context) == true)
            {
                if (auth_key != null)
                {
                    postData.put("auth_key", auth_key);
                    postData.put("old_password", oldPassword.getText().toString());
                    postData.put("new_password", newPassword.getText().toString());
                    postData.put("user_id", user_id);
                    PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                    loginTask.execute(ConnectionManager.SERVER_URL + "changePassword");
                }
            }
            else
            {
                customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
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
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

                // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .commit();

            }
            else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
