package www.icebd.com.suzukibangladesh.request;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;
import www.icebd.com.suzukibangladesh.utilities.Tools;


public class RequestServices extends Fragment implements AsyncResponse, View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Spinner dropdown_bike_name;
    Button submit;
    RadioGroup service_type1;
    CheckBox engine, electrical, suspension, while_tyre, brake, speedo_motor, gear, clutch_plate, oil_filter, body_parts;
    String auth_key;
    String[] bikeId;
    EditText userComments;
    TextView text_right;
    Context context;
    CustomDialog customDialog;
    Button parts_change,repair;

    int selectedPartsChangeAndRepair = 0;


    public static RequestServices newInstance() {
        RequestServices fragment = new RequestServices();
        return fragment;

    }

    public RequestServices() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service, container,
                false);
        getActivity().setTitle("Service");
        context = getActivity().getApplicationContext();
        setupUI(rootView.findViewById(R.id.parentRequestService));

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();


        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        //text_right = (TextView) rootView.findViewById(R.id.txt_email_right);
        //text_right.setTypeface(iconFont);


        HashMap<String, String> postData = new HashMap<String, String>();


        dropdown_bike_name = (Spinner) rootView.findViewById(R.id.txt_dropdown);
        submit = (Button) rootView.findViewById(R.id.btn_service_submit);
        service_type1 = (RadioGroup) rootView.findViewById(R.id.rdo_grp_service_type_1);
        //service_type2 = (RadioGroup) rootView.findViewById(R.id.rdo_grb_service_type_2);

        parts_change = (Button) rootView.findViewById(R.id.parts_change);
        parts_change.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                parts_change.setPressed(true);
                parts_change.setBackgroundColor(getResources().getColor(R.color.suzuki_blue_color));
                repair.setBackgroundColor(getResources().getColor(R.color.service_spinner_bg));
                selectedPartsChangeAndRepair = 1;
                return true;
            }
        });
        repair = (Button) rootView.findViewById(R.id.repair);
        repair.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                repair.setPressed(true);
                repair.setBackgroundColor(getResources().getColor(R.color.suzuki_blue_color));
                parts_change.setBackgroundColor(getResources().getColor(R.color.service_spinner_bg));
                selectedPartsChangeAndRepair = 2;
                return true;
            }
        });

        engine = (CheckBox) rootView.findViewById(R.id.chk_engine);
        electrical = (CheckBox) rootView.findViewById(R.id.chk_electrical);
        suspension = (CheckBox) rootView.findViewById(R.id.chk_suspension);
        while_tyre = (CheckBox) rootView.findViewById(R.id.chk_while_tyre);
        brake = (CheckBox) rootView.findViewById(R.id.chk_brake);
        speedo_motor = (CheckBox) rootView.findViewById(R.id.chk_speedo_motor);
        gear = (CheckBox) rootView.findViewById(R.id.chk_gear);
        clutch_plate = (CheckBox) rootView.findViewById(R.id.chk_clutch_plate);
        oil_filter = (CheckBox) rootView.findViewById(R.id.chk_oil_filter);
        body_parts = (CheckBox) rootView.findViewById(R.id.chk_body_parts);
        userComments = (EditText) rootView.findViewById(R.id.et_say_something);


        auth_key = pref.getString("auth_key", "empty");
        customDialog = new CustomDialog(getActivity());
        if (CheckNetworkConnection.isConnectedToInternet(context) == true) {
            if (!auth_key.equals("empty")) {
                postData.put("auth_key", auth_key);
                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                loginTask.execute(ConnectionManager.SERVER_URL + "getBikeList");

            }
        } else {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }

        submit.setOnClickListener(this);

        ((FirstActivity) getActivity()).setBackKeyFlag(true);
        ((FirstActivity) getActivity()).setWhichFragment(0);

        return rootView;
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

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
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onClick(View v)
    {

        attemptSubmitRequestService();

    }
    private void attemptSubmitRequestService()
    {

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (dropdown_bike_name.getSelectedItem().toString().trim().equals("Bike Model"))
        {
            Toast.makeText(context, "Bike Model Name not selected", Toast.LENGTH_SHORT).show();
            focusView = dropdown_bike_name;
            cancel = true;
        }
        else if ( selectedPartsChangeAndRepair == 0 )
        {
            Toast.makeText(context, "Please Select Parts Change or Repair", Toast.LENGTH_SHORT).show();
            focusView = parts_change;
            cancel = true;
        }
        else if (engine.isChecked() == false && electrical.isChecked() == false && suspension.isChecked() == false &&
                while_tyre.isChecked() == false && brake.isChecked() == false && speedo_motor.isChecked() == false &&
                gear.isChecked() == false && clutch_plate.isChecked() == false && oil_filter.isChecked() == false &&
                body_parts.isChecked() == false)
        {
            Toast.makeText(context, "Please select at least one Item under Parts Change or Repair", Toast.LENGTH_SHORT).show();
            focusView = engine;
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
            // postData.put("mobile","android");
            // postData.put("mobile","android");
            String user_id = pref.getString("user_id", "Not found");

            if (!auth_key.equals("empty"))
            {
                postData.put("auth_key", auth_key);
                Log.i("Test", "auth_key :" + auth_key);

                postData.put("app_user_id", user_id);
                Log.i("Test", "app_user_id :" + user_id);
                //   postData.put("app_user_id",user_id);

                int position = dropdown_bike_name.getSelectedItemPosition();
                postData.put("bike_id", bikeId[position - 1]);
                Log.i("Test", "bike_id :" + bikeId[position - 1]);
                postData.put("bike_name", dropdown_bike_name.getSelectedItem().toString());
                // postData.put("bike_name","GS150R");
                Log.i("Test", "bike_name :" + dropdown_bike_name.getSelectedItem().toString());

                int selected_service_1 = service_type1.getCheckedRadioButtonId();
                String value_service1 = "";
                switch (selected_service_1) {
                    case R.id.free:
                        value_service1 = "free";
                        break;
                    case R.id.paid:
                        value_service1 = "paid";
                        break;
                    case R.id.warranty:
                        value_service1 = "warranty";
                        break;
                    default:
                        break;
                }

                postData.put("service_type", value_service1);
                Log.i("Test", "service_type :" + value_service1);


                //int selected_service_2 = service_type2.getCheckedRadioButtonId();
                String value_service2 = "";
                if(selectedPartsChangeAndRepair == 1)
                {
                    value_service2 = "parts_change";
                }
                else if(selectedPartsChangeAndRepair == 1)
                {
                    value_service2 = "repair";
                }
                /*switch (selected_service_2) {
                    case R.id.parts_change:
                        value_service2 = "parts_change";
                        break;
                    case R.id.repair:
                        value_service2 = "repair";
                        break;
                    default:
                        break;
                }*/

                postData.put("servicing_type", value_service2);
                Log.i("Test", "servicing_type :" + value_service2);

                String service_option = "";

                if (engine.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = engine.getText().toString();
                    } else {
                        service_option = service_option + "," + engine.getText().toString();
                    }
                }

                if (electrical.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = electrical.getText().toString();
                    } else {
                        service_option = service_option + "," + electrical.getText().toString();
                    }
                }
                if (suspension.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = suspension.getText().toString();
                    } else {
                        service_option = service_option + "," + suspension.getText().toString();
                    }
                }
                if (while_tyre.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = while_tyre.getText().toString();
                    } else {
                        service_option = service_option + "," + while_tyre.getText().toString();
                    }
                }
                if (brake.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = brake.getText().toString();
                    } else {
                        service_option = service_option + "," + brake.getText().toString();
                    }
                }
                if (speedo_motor.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = speedo_motor.getText().toString();
                    } else {
                        service_option = service_option + "," + speedo_motor.getText().toString();
                    }
                }
                if (gear.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = gear.getText().toString();
                    } else {
                        service_option = service_option + "," + gear.getText().toString();
                    }
                }
                if (clutch_plate.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = clutch_plate.getText().toString();
                    } else {
                        service_option = service_option + "," + clutch_plate.getText().toString();
                    }
                }
                if (oil_filter.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = oil_filter.getText().toString();
                    } else {
                        service_option = service_option + "," + oil_filter.getText().toString();
                    }
                }
                if (body_parts.isChecked()) {
                    if (service_option.equals("")) {
                        service_option = body_parts.getText().toString();
                    } else {
                        service_option = service_option + "," + body_parts.getText().toString();
                    }
                }

                postData.put("service_option", service_option);
                Log.i("Test", "service_option :" + service_option);

                postData.put("cust_comment", userComments.getText().toString());
                Log.i("Test", "cust_comment :" + userComments.getText().toString());

                if (CheckNetworkConnection.isConnectedToInternet(context) == true) {
                    PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                    loginTask.execute(ConnectionManager.SERVER_URL + "reqService");
                }
                else
                {
                    customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
                }
            }
            else {
                Toast.makeText(getActivity(), "Auth key not found,Please restart the app Again", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void processFinish(String output) {
        Log.i("Test", output);


        try {
            Log.i("Test", "Enter");
            JSONObject object = new JSONObject(output);
            String message = "";
            message = object.getString("message");

            // Log.i("Test", "Enter");

            if (message.equals("Successful")) {
                Log.i("Test", "I am successful");
                JSONArray bikeList = object.getJSONArray("bikeList");
                String[] string = new String[bikeList.length() + 1];
                string[0] = "Bike Model";
                bikeId = new String[bikeList.length()];
                // ArrayList<String> mylist = new ArrayList<String>();

                for (int i = 0; i < bikeList.length(); i++) {
                    JSONObject bikeDetail = bikeList.getJSONObject(i);
                    String bike_name = bikeDetail.getString("bike_name");
                    String bike_cc = bikeDetail.getString("bike_cc");
                    // mylist.add(bike_name+"/"+bike_cc);
                    string[i + 1] = bike_name + "/" + bike_cc;
                    bikeId[i] = bikeDetail.getString("bike_id");
                    Log.i("Test", bike_name + "/" + bike_cc);


                }



                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, string)
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_bike_name, string)
                {
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setGravity(Gravity.CENTER);
                        ((TextView) v).setTextSize(16);
                        return v;

                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {

                        View v = super.getDropDownView(position, convertView, parent);

                        ((TextView) v).setGravity(Gravity.LEFT);

                        return v;

                    }
                };

                dropdown_bike_name.setAdapter(adapter);

            } else if (message.equals("Request failed please try again")) {
                Toast.makeText(getActivity(), "Request failed please try again", Toast.LENGTH_LONG).show();
            } else if (message.equals("Your request has been sent successfully")) {
                Toast.makeText(getActivity(), "Your request has been sent successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), FirstActivity.class);
                startActivity(intent);

            } else {
                Log.i("Test", message);
            }

        } catch (JSONException e) {
            //   Log.i("Test", "Enter");

            try {
                JSONObject ob = new JSONObject(output);
                Log.i("Test", "Enter last try");
                String message = ob.getString("message");
                if (message.equals("Request failed please try again")) {
                    Toast.makeText(getActivity(), "Request failed please try again", Toast.LENGTH_LONG).show();
                } else if (message.equals("Your request has been sent successfully")) {
                    Toast.makeText(getActivity(), "Your request has been sent successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), FirstActivity.class);
                    startActivity(intent);

                } else {
                    Log.i("Test", message);
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.i("Test", "Enter last catch");
            }
            e.printStackTrace();
        }

        //   Log.i("Test", "Enter");


    }


}
