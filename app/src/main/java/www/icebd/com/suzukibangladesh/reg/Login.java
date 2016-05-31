package www.icebd.com.suzukibangladesh.reg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import www.icebd.com.suzukibangladesh.menu.MyBikeFragment;
import www.icebd.com.suzukibangladesh.quiz.Quiz;
import www.icebd.com.suzukibangladesh.request.RequestServices;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsMyCart;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsPayments;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.Tools;


public class Login extends Fragment implements View.OnClickListener, AsyncResponse {
    EditText password,email;
    Button button,forgotPass,signUp;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    Context context;
    CustomDialog customDialog;

    public static Login newInstance() {
        Login fragment = new Login();
        return fragment;
    }

    public Login() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container,
                false);
        setupUI(rootView.findViewById(R.id.parentLogin));
        context = getActivity().getApplicationContext();
        getActivity().setTitle("Login");


        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();


        email = (EditText) rootView.findViewById(R.id.login_email);
        password = (EditText) rootView.findViewById(R.id.login_password);

        button=(Button) rootView.findViewById(R.id.button);
        forgotPass=(Button) rootView.findViewById(R.id.btn_forgot_pass);
        signUp=(Button) rootView.findViewById(R.id.btn_sign_up);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                    fragmentManager.beginTransaction()
                            .replace(R.id.container, ResetPassword.newInstance())
                            .commit();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                fragmentManager.beginTransaction()
                        .replace(R.id.container, Signup.newInstance())
                        .commit();
            }
        });


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

        attemptLogin();

    }
    private void attemptLogin()
    {
        /*if (premiumJomaTask != null)
        {
            return;
        }*/

        // Reset errors.
        email.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();
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

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else
        {
            goToLoginTask(pref,strEmail,strPassword);
        }
    }
    public void goToLoginTask(SharedPreferences pref,String strEmail,String strPassword)
    {
        HashMap<String, String> postData = new HashMap<String, String>();
        String auth_key = pref.getString("auth_key",null);
        Log.i("Test","Auth Key from shared preference "+auth_key);
        CustomDialog customDialog = new CustomDialog(getActivity());
        if(CheckNetworkConnection.isConnectionAvailable(context) == true)
        {
            if (auth_key != null)
            {
                postData.put("auth_key",auth_key);
                postData.put("user_email",strEmail);
                postData.put("user_pass",strPassword);
                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                loginTask.execute(ConnectionManager.SERVER_URL+"login");
            }
        }
        else
        {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
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
        try {
            JSONObject object = new JSONObject(output);
            boolean status = object.getBoolean("status");
            String status_code = object.getString("status_code");
            String message = object.getString("message");


            FragmentManager fragmentManager = getFragmentManager();
            // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            // SharedPreferences.Editor editor = preferences.edit();
            // editor.putString("user_id",user_id);
            // editor.apply();

            if (status == true)
            {
                String auth_key = object.getString("auth_key");
                String user_id = object.getString("user_id");
                String user_name = object.getString("user_name");
                System.out.println("user name is: "+user_name);
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

                editor.putString("is_login","1");
                editor.putString("user_id",user_id);
                editor.putString("user_name",user_name);
                editor.commit();

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
/*                else if( Constant.isDetermin == 3)
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
}
