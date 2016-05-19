package www.icebd.com.suzukibangladesh.request;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.bikedetails.BikeDetails;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.menu.HomeFragment;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.Tools;


public class Quotation extends Fragment implements AsyncResponse, View.OnClickListener {

    Button submit_btn_quation;
    EditText name,email,cell,address;
    TextView bikeName, bike_cc;
    EditText say_Something;

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    int bike_id = 0;
    String bike_name;

    Context context;
    CustomDialog customDialog;


    public static Quotation newInstance() {
        Quotation fragment = new Quotation();
        return fragment;
    }

    public Quotation() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quotation, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("REQUEST FOR QUOTATION");
        setupUI(rootView.findViewById(R.id.main_quotation_lauout));

        say_Something = (EditText) rootView.findViewById(R.id.et_say_something_quotation);

        submit_btn_quation = (Button) rootView.findViewById(R.id.quotation_btn_submit);

        name = (EditText) rootView.findViewById(R.id.txt_name_quo);
        email = (EditText) rootView.findViewById(R.id.txt_email_quo);
        address = (EditText) rootView.findViewById(R.id.txt_address_quo);
        cell = (EditText) rootView.findViewById(R.id.txt_cell_no_quo);
        bikeName = (TextView) rootView.findViewById(R.id.txt_miles_quo);
        bike_cc = (TextView) rootView.findViewById(R.id.txt_cc_quo);


        submit_btn_quation.setOnClickListener(this);

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        Bundle bundle = this.getArguments();
        bike_id = bundle.getInt("bike_id");
        bike_name = bundle.getString("bike_name");
        String cc = bundle.getString("cc");
        bikeName.setText(bike_name);
        bike_cc.setText(cc);



        //name.setText(pref.getString("name",""));
        //email.setText(pref.getString("email",""));
        //address.setText(pref.getString("address",""));
        //cell.setText(pref.getString("mobile_no",""));
       // name.setText(pref.getString("name","Name not found"));


        return rootView;
    }

    @Override
    public void processFinish(String output) {

        Log.i("Test",output);

        try {
            JSONObject object = new JSONObject(output);
            String status_code = object.getString("status_code");
            String message = object.getString("message");
            FragmentManager fragmentManager = getFragmentManager();



            if (status_code.equals("200"))
            {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

                // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentManager.beginTransaction().replace(R.id.container, HomeFragment.newInstance()).commit();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();

                BikeDetails fragmentBikeDetails = new BikeDetails();
                Bundle bundle = new Bundle();
                bundle.putInt( "bike_id", bike_id );
                fragmentBikeDetails.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragmentBikeDetails);
                fragmentTransaction.commit();

            }
            else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        attemptSubmitQuotation();
    }
    private void attemptSubmitQuotation()
    {
        /*if (premiumJomaTask != null)
        {
            return;
        }*/

        // Reset errors.
        name.setError(null);
        email.setError(null);
        address.setError(null);
        cell.setError(null);

        // Store values at the time of the login attempt.
        String strName = name.getText().toString();
        String strEmail = email.getText().toString();
        String straddress = address.getText().toString();
        String strCell = cell.getText().toString();
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
        else if ( TextUtils.isEmpty(straddress) )
        {
            address.setError(getString(R.string.error_field_required));
            focusView = address;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(strCell) )
        {
            cell.setError(getString(R.string.error_field_required));
            focusView = cell;
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

            Log.i("Test","Name : "+pref.getString("name","not found"));
            String auth_key = pref.getString("auth_key",null);
            customDialog = new CustomDialog(context);
            if(CheckNetworkConnection.isConnectionAvailable(context) == true)
            {
                if (auth_key != null)
                {
                    postData.put("auth_key",auth_key);
                    postData.put("app_user_name",name.getText().toString());
                    postData.put("bike_id",String.valueOf(bike_id));
                    postData.put("bike_name",bike_name);
                    postData.put("app_user_email",email.getText().toString());
                    postData.put("app_user_phone",cell.getText().toString());
                    postData.put("app_user_address",address.getText().toString());
                    postData.put("app_user_comment",say_Something.getText().toString());

                    PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
                    loginTask.execute(ConnectionManager.SERVER_URL+"reqQuotation");
                }
            }
            else
            {
                customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
            }


        }
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
}
