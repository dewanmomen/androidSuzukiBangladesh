package www.icebd.com.suzukibangladesh.spare_parts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.Manifest;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.JsonParser;
import www.icebd.com.suzukibangladesh.utilities.Tools;

import static com.google.android.gms.internal.zzir.runOnUiThread;


public class SparePartsPayments extends Fragment{

    Button dialer;
    Context context;

    EditText txt_trx_id;
    Button checkout_thisCart;

    APIFactory apiFactory;
    CustomDialog customDialog;
    ProgressDialog progressDialog;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;

    String delivery_charge = "";
    String total_item_price = "";
    String gross_amount = "";
    String delivery_address = "";

    SparePartsPurchaseTask sparePartsPurchaseTask = null;

    public static SparePartsPayments newInstance() {
        SparePartsPayments fragment = new SparePartsPayments();
        return fragment;
    }

    public SparePartsPayments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spare_parts_payments, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("Spare Parts Purchase");
        setupUI(rootView.findViewById(R.id.payment_mother_layout));

        Bundle bundle = getArguments();
        delivery_charge = bundle.getString("delivery_charge");
        total_item_price = bundle.getString("total_item_price");
        gross_amount = bundle.getString("gross_amount");
        delivery_address = bundle.getString("delivery_address");

        pref = context.getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        txt_trx_id = (EditText)rootView.findViewById(R.id.txt_trx_id);
        checkout_thisCart = (Button)rootView.findViewById(R.id.checkout_thisCart);
        checkout_thisCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                attemptPurchase();
            }
        });

        //dialer = (Button) rootView.findViewById(R.id.dialer);
        //dialer.setOnClickListener(this);
        Constant.goToSparePartOrMyCart = 1;
        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(15);
        return rootView;
    }
    private void attemptPurchase()
    {
        txt_trx_id.setError(null);

        // Store values at the time of the login attempt.
        String strTransNumber = txt_trx_id.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if ( TextUtils.isEmpty(strTransNumber) )
        {
            txt_trx_id.setError(getString(R.string.error_field_required));
            focusView = txt_trx_id;
            cancel = true;
        }


        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else
        {
            apiFactory = new APIFactory();
            customDialog = new CustomDialog(getActivity());
            String auth_key = pref.getString("auth_key", null);
            String user_id = pref.getString("user_id", "0");
            String transaction_id = txt_trx_id.getText().toString();


            if(CheckNetworkConnection.isConnectionAvailable(context) == true)
            {
                sparePartsPurchaseTask = new SparePartsPurchaseTask(auth_key,delivery_charge,delivery_address,user_id,total_item_price,gross_amount,transaction_id,Constant.delivery_type,Constant.listMyCartObj);
                sparePartsPurchaseTask.execute((Void) null);
                //fetchBikeList();
            }
            else
            {
                customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
            }
        }
    }
    public class SparePartsPurchaseTask extends AsyncTask<Void, Void, String>
    {
        private String RESULT = "OK";
        private ArrayList<NameValuePair> returnJsonData;
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
        String delivery_charge;
        String delivery_address;
        String user_id;
        String total_item_price;
        String gross_amount;
        String transaction_id;
        int delivery_type;
        List<SparePartsListObject.SparePartsItem>  listMyCartObj;

        //UserLoginTask(String email, String password)
        SparePartsPurchaseTask(String auth_key, String delivery_charge,String delivery_address, String user_id, String total_item_price, String gross_amount, String transaction_id, int delivery_type, List<SparePartsListObject.SparePartsItem>  listMyCartObj)
        {
            this.auth_key = auth_key;
            this.delivery_charge = delivery_charge;
            this.delivery_address = delivery_address;
            this.user_id = user_id;
            this.total_item_price = total_item_price;
            this.gross_amount = gross_amount;
            this.transaction_id = transaction_id;
            this.delivery_type = delivery_type;
            this.listMyCartObj = listMyCartObj;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(getActivity(), null, null);
        }
        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                if (ConnectionManager.hasInternetConnection())
                {
                    //auth_key = "b78c0c986e4a3d962cd220427bc8ff07";
                    nvp2 = apiFactory.goSparePartsPurchaseInfo(auth_key,delivery_charge,delivery_address,user_id,total_item_price,gross_amount,transaction_id,Constant.delivery_type,Constant.listMyCartObj);
                    methodName = "purchase";
                    response = ConnectionManager.getResponseFromServer(methodName, nvp2);
                    jsonParser = new JsonParser();

                    System.out.println("server response : "+response);
                    returnJsonData = jsonParser.parseAPIgetSparePartsPurchaseInfo(response);
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
            progressDialog.dismiss();
            //swipeRefreshLayout.setRefreshing(false);
            if(RESULT.equalsIgnoreCase("OK"))
            {
                try
                {
                    //finish();

                    if (returnJsonData != null && returnJsonData.get(1).getValue().equals("200") == true )
                    {

                        Constant.listMyCartObj = null;
                        Constant.delivery_type = 0; // 0 = pickup point, 1 = inside_dhaka, 2 = outside_dhaka;
                        Constant.delivery_category = "";
                        Toast.makeText(getActivity(), "Purchase Successfully, You will get an Email Confirmation", Toast.LENGTH_SHORT).show();
                        ((FirstActivity)getActivity()).selectItem(3);
                    }
                    else
                    {
                        /*if( returnJsonData.get(1).getValue().equals("502") || returnJsonData.get(1).getValue().equals("503") || returnJsonData.get(1).getValue().equals("504") ||
                                returnJsonData.get(1).getValue().equals("505") || returnJsonData.get(1).getValue().equals("507") || returnJsonData.get(1).getValue().equals("508") )
                        {
                            Toast.makeText(getActivity(), "Invalid Transaction ID, Please Enter Correct Transaction ID !", Toast.LENGTH_SHORT).show();
                        }
                        else if( returnJsonData.get(1).getValue().equals("500") || returnJsonData.get(1).getValue().equals("501") || returnJsonData.get(1).getValue().equals("506") )
                        {
                            Toast.makeText(getActivity(), "Operation Failed, Please Try Again !", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Please Try Again !", Toast.LENGTH_SHORT).show();
                        }*/
                        System.out.println("data return : " + returnJsonData);
                        Toast.makeText(getActivity(), returnJsonData.get(2).getValue(), Toast.LENGTH_LONG).show();
                    }

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    Log.e("APITask data error :", ex.getMessage());
                }
            }
            else {

                customDialog.alertDialog("ERROR", result);
            }
        }
        @Override
        protected void onCancelled()
        {
            sparePartsPurchaseTask = null;
            progressDialog.dismiss();

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((MainActivity) activity).onSectionAttached(1);
    }



}
