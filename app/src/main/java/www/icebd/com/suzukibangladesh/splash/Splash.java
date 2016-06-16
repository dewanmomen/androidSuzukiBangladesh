package www.icebd.com.suzukibangladesh.splash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.menu.DetailNewsEvents;
import www.icebd.com.suzukibangladesh.menu.PromotionsDetails;
import www.icebd.com.suzukibangladesh.notification.Notification;
import www.icebd.com.suzukibangladesh.notification.QuickstartPreferences;
import www.icebd.com.suzukibangladesh.notification.RegistrationIntentService;
import www.icebd.com.suzukibangladesh.notification.WakeLocker;
import www.icebd.com.suzukibangladesh.request.RFSNotificationFragment;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsListObject;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsListSwipeListAdapter;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.JsonParser;

import static com.google.android.gms.internal.zzir.runOnUiThread;


public class Splash extends AppCompatActivity
{
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 5000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    APIFactory apiFactory;
    CustomDialog customDialog;
    ProgressDialog progressDialog;
    private MediaTask mediaTask;
    final Context context = this;
    private AuthKeyTask authKeyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        pref = getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", MODE_PRIVATE);
        editor = pref.edit();

        //gotToFragment();
        String notification_key = pref.getString("gcm_registration_token", null);
        if (notification_key == null)
        {
            //progressDialog = ProgressDialog.show(getApplicationContext(),null,null);
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // Toast.makeText(this,"Received Notification ",Toast.LENGTH_LONG).show();
                    //Toast.makeText(Splash.this, "Received Notification ", Toast.LENGTH_LONG).show();
                    //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    //progressDialog.dismiss();
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

                    //String newMessage = intent.getExtras().getString("message");
                    //System.out.println("received Notification message: "+newMessage);
                    // Waking up mobile if it is sleeping
                    //WakeLocker.acquire(getApplicationContext());

                    /**
                     * Take appropriate action on this message
                     * depending upon your app requirement
                     * For now i am just displaying it on the screen
                     * */

                    // Showing received message
                    //lblMessage.append(newMessage + "\n");
                    //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

                    // Releasing wake lock
                    //WakeLocker.release();
                    Log.i("Test", "I am from onReceive end");
                }
            };
            // Registering BroadcastReceiver
            registerReceiver();

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(Splash.this, RegistrationIntentService.class);
                startService(intent);
            }
        }


        final ImageView iv = (ImageView) findViewById(R.id.img_logo);
        //  final ImageView iv1 = (ImageView) findViewById(R.id.img_txt);
        //final ImageView imgText = (ImageView) findViewById(R.id.imageViewText);


        //   final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation an1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        final Animation an3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);
        //final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);


        //  iv1.startAnimation(an1);
        iv.startAnimation(an1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               /* iv1.setVisibility(ImageView.VISIBLE);
                 iv.setVisibility(ImageView.INVISIBLE);*/
               // iv1.startAnimation(an3);
                iv.startAnimation(an3);
            }
        }, 3000);// delay in milliseconds (200)

        apiFactory = new APIFactory();
        customDialog = new CustomDialog(context);
        if(CheckNetworkConnection.isConnectedToInternet(this) == true)
        {
            try
            {
                //finish();
                //pref = getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", MODE_PRIVATE);
                //editor = pref.edit();
                //String notification_key = pref.getString("gcm_registration_token", null);
                //editor.putString("running", "no");
                //editor.apply();

                String auth_key = pref.getString("auth_key",null);
                if (auth_key == null)
                {
                    String identifier = Constant.definedIdentifier;
                    String platform = "1";
                    if(CheckNetworkConnection.isConnectedToInternet(context) == true)
                    {
                        authKeyTask = new AuthKeyTask(identifier,platform);
                        authKeyTask.execute((Void) null);
                    }
                    else
                    {
                        customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
                    }
                }
                else
                {
                    mediaTask = new MediaTask(pref.getString("auth_key", null));
                    mediaTask.execute((Void) null);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Log.i("exception: ", ex.getMessage());
            }

            /*new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        finish();
                        pref = getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", MODE_PRIVATE);
                        editor = pref.edit();
                        //String notification_key = pref.getString("gcm_registration_token", null);
                        editor.putString("running", "no");
                        editor.apply();

                        String auth_key = pref.getString("auth_key",null);
                        //String notification_key = pref.getString("gcm_registration_token",null);
                        Log.i("Test","GCM registration token :"+pref.getString("gcm_registration_token",null));
                        if (auth_key == null)
                        {
                            String android_id = Settings.Secure.getString(Splash.this.getContentResolver(),
                                    Settings.Secure.ANDROID_ID);

                            Log.i("Test","Android ID : "+android_id);
                            Log.i("Test","Notification key : "+pref.getString("gcm_registration_token",null));
                            //Log.i("Test","Auth_key : "+auth_key);

                            String unique_device_id = android_id;
                            String notification_key = pref.getString("gcm_registration_token",null);
                            String platform = "1";
                            if(CheckNetworkConnection.isConnectedToInternet(context) == true)
                            {
                                authKeyTask = new AuthKeyTask(unique_device_id,notification_key,platform);
                                authKeyTask.execute((Void) null);
                            }
                            else
                            {
                                customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
                            }
                        }
                        else
                        {
                            mediaTask = new MediaTask(pref.getString("auth_key", null));
                            mediaTask.execute((Void) null);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        Log.i("exception: ", ex.getMessage());
                    }
                }
            }, 6000);// delay in milliseconds (200)
            */
        }
        else
        {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public class AuthKeyTask extends AsyncTask<Void, Void, String>
    {
        private String RESULT = "OK";
        private ArrayList<NameValuePair> returnJsonData;
        private ArrayList<NameValuePair> nvp2=null;
        private InputStream response;
        private JsonParser jsonParser;

        private String methodName = "";
        private String identifier, platform;

        //UserLoginTask(String email, String password)
        AuthKeyTask(String identifier,String platform)
        {
            this.identifier = identifier;
            this.platform = platform;
        }

        @Override
        protected void onPreExecute() {

            //progressDialog = ProgressDialog.show(context, null, null);
        }
        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                if (ConnectionManager.hasInternetConnection())
                {
                    //auth_key = "b78c0c986e4a3d962cd220427bc8ff07";
                    nvp2 = apiFactory.getAuthKeyInfo(identifier,platform);
                    methodName = "getAuthKey";
                    response = ConnectionManager.getResponseFromServer(methodName, nvp2);
                    jsonParser = new JsonParser();

                    System.out.println("server response : "+response);
                    returnJsonData = jsonParser.parseAPIgetAuthKeyInfo(response);
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
                //ex.printStackTrace();
                Log.e("APITask:", ex.getMessage());
                RESULT = getString(R.string.error_sever_connection);
                return RESULT;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            //progressDialog.dismiss();
            if(RESULT.equalsIgnoreCase("OK"))
            {
                try
                {
                    //finish();

                    if (returnJsonData.size() > 0 && returnJsonData != null && returnJsonData.get(0).getValue().equals("true") == true )
                    {
                        //preferenceUtil.setPINstatus(1);
                        //Toast.makeText(getApplicationContext(), returnJsonData.get(1).getValue(), Toast.LENGTH_SHORT).show();
                        String auth_key = returnJsonData.get(3).getValue();
                        editor.putString("auth_key",auth_key);
                        editor.commit();
                        Log.i("Test","auth_key found ="+auth_key);
                        if(!auth_key.equals("null"))
                        {
                            mediaTask = new MediaTask(pref.getString("auth_key", null));
                            mediaTask.execute((Void) null);
                        }
                    } else
                    {
                        //System.out.println("data return : " + returnJsonData);
                        Toast.makeText(getApplicationContext(), "Request Data Not Found, Please Try Again !", Toast.LENGTH_SHORT).show();
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
            authKeyTask = null;
            //progressDialog.dismiss();
        }
    }
    public class MediaTask extends AsyncTask<Void, Void, String>
    {
        private String RESULT = "OK";
        private ArrayList<NameValuePair> returnJsonData;
        private ArrayList<NameValuePair> nvp2=null;
        private InputStream response;
        private JsonParser jsonParser;

        private String methodName = "";
        private String auth_key;

        //UserLoginTask(String email, String password)
        MediaTask(String auth_key)
        {
            this.auth_key = auth_key;
        }

        @Override
        protected void onPreExecute() {

            //progressDialog = ProgressDialog.show(context, null, null);
        }
        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                if (ConnectionManager.hasInternetConnection())
                {
                    //auth_key = "b78c0c986e4a3d962cd220427bc8ff07";
                    nvp2 = apiFactory.getMediaInfo(auth_key);
                    methodName = "getMedia";
                    response = ConnectionManager.getResponseFromServer(methodName, nvp2);
                    jsonParser = new JsonParser();

                    System.out.println("server response : "+response);
                    returnJsonData = jsonParser.parseAPIgetMediaInfo(response);
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
                //ex.printStackTrace();
                Log.e("APITask:", ex.getMessage());
                RESULT = getString(R.string.error_sever_connection);
                return RESULT;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            //progressDialog.dismiss();
            if(RESULT.equalsIgnoreCase("OK"))
            {
                try
                {
                    finish();

                    if (returnJsonData.size() > 0 && returnJsonData != null && returnJsonData.get(0).getValue().equals("true") == true )
                    {
                        //preferenceUtil.setPINstatus(1);
                        //Toast.makeText(getApplicationContext(), returnJsonData.get(1).getValue(), Toast.LENGTH_SHORT).show();
                        Intent i;
                        i = new Intent(getBaseContext(), FirstActivity.class);
                        startActivity(i);

                    } else
                    {
                        //System.out.println("data return : " + returnJsonData);
                        Toast.makeText(getApplicationContext(), "Request Data Not Found, Please Try Again !", Toast.LENGTH_SHORT).show();
                    }
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
            mediaTask = null;
            //progressDialog.dismiss();
        }
    }
    private void gotToFragment()
    {
        Intent intent = getIntent();
        String NotificationType = intent.getAction();

        System.out.println("From Push Notification Type : "+NotificationType);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String picture = intent.getStringExtra("picture");
        // promotions, request_for_quotation, request_for_service, quiz_publish, quiz_result, news, event
        try
        {
            if(NotificationType != null)
            {
                if (NotificationType.equals("promotions"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment fragment = new PromotionsDetails().newInstance();
                    Bundle bundle = new Bundle();

                    bundle.putString("viewTitleName", "PROMOTIONS DETAILS");

                    bundle.putString( "promo_title", title );
                    bundle.putString( "promo_desc", message );
                    bundle.putString( "promo_image", picture == null ? null : picture );
                    //bundle.putString( "news_event_start_date", arrList.get(position).get("news_event_title").toString() );
                    //bundle.putString( "news_event_end_date", arrList.get(position).get("news_event_title").toString() );
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
                else if (NotificationType.equals("request_for_quotation"))
                {
                    //((FirstActivity)context).selectItem(4);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment fragment = new RFSNotificationFragment().newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("viewTitleName", "Requested Quotation Notification");
                    bundle.putString("img_url", picture == null ? null : picture);
                    bundle.putString("headerText", title.toString());
                    bundle.putString("bodyText", message);
                    //bundle.putString("footerText",footerText);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();

                }
                else if (NotificationType.equals("request_for_service"))
                {
                    //((FirstActivity)context).selectItem(4);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment fragment = new RFSNotificationFragment().newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("viewTitleName","Requested Service Notification");
                    bundle.putString("img_url", picture == null ? null : picture);
                    bundle.putString("headerText", title.toString());
                    bundle.putString("bodyText", message);
                    //bundle.putString("footerText",footerText);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
                else if (NotificationType.equals("quiz_publish"))
                {
                    ((FirstActivity)context).selectItem(7);
                }
                else if (NotificationType.equals("quiz_result"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment fragment = new RFSNotificationFragment().newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("viewTitleName","Quizzes Result Published");
                    bundle.putString("img_url", picture == null ? null : picture);
                    bundle.putString("headerText", title.toString());
                    bundle.putString("bodyText", message);
                    //bundle.putString("footerText",footerText);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
                else if (NotificationType.equals("news"))
                {
                    //((FirstActivity)context).selectItem(5);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment fragment  = new DetailNewsEvents().newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("viewTitleName","News");
                    bundle.putString( "news_event_title", title );
                    bundle.putString( "news_event_desc", message );
                    bundle.putString( "news_event_img", picture == null ? null : picture );
                    //bundle.putString( "news_event_start_date", arrList.get(position).get("news_event_title").toString() );
                    //bundle.putString( "news_event_end_date", arrList.get(position).get("news_event_title").toString() );
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
                else if (NotificationType.equals("event"))
                {
                    //((FirstActivity)context).selectItem(5);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment fragment  = new DetailNewsEvents().newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("viewTitleName","Events");
                    bundle.putString( "news_event_title", title );
                    bundle.putString( "news_event_desc", message );
                    bundle.putString( "news_event_img",  picture == null ? null : picture );
                    //bundle.putString( "news_event_start_date", arrList.get(position).get("news_event_title").toString() );
                    //bundle.putString( "news_event_end_date", arrList.get(position).get("news_event_title").toString() );
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
            }
            else
            {
                Log.d("INTENT check", "Intent was null");
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}
