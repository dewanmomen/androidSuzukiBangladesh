package www.icebd.com.suzukibangladesh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.historyOfSuzuki.HistoryOfSuzuki;
import www.icebd.com.suzukibangladesh.historyOfSuzuki.SuzukiAboutUs;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.maps.MapsActivity;
import www.icebd.com.suzukibangladesh.menu.DetailNewsEvents;
import www.icebd.com.suzukibangladesh.menu.HomeFragment;
import www.icebd.com.suzukibangladesh.menu.InviteFriends;
import www.icebd.com.suzukibangladesh.menu.MediaLink;
import www.icebd.com.suzukibangladesh.menu.NewsEvents;
import www.icebd.com.suzukibangladesh.menu.PromotionsDetails;
import www.icebd.com.suzukibangladesh.notification.Notification;
import www.icebd.com.suzukibangladesh.notification.QuickstartPreferences;
import www.icebd.com.suzukibangladesh.reg.Login;
import www.icebd.com.suzukibangladesh.menu.MyBikeFragment;
import www.icebd.com.suzukibangladesh.menu.Promotions;
import www.icebd.com.suzukibangladesh.quiz.Quiz;
import www.icebd.com.suzukibangladesh.request.RFSNotificationFragment;
import www.icebd.com.suzukibangladesh.request.RequestServices;
import www.icebd.com.suzukibangladesh.menu.SOS;
import www.icebd.com.suzukibangladesh.menu.SocialMedia;
import www.icebd.com.suzukibangladesh.menu.SpareParts;
import www.icebd.com.suzukibangladesh.reg.ChangePassword;
import www.icebd.com.suzukibangladesh.reg.Logout;
import www.icebd.com.suzukibangladesh.reg.ResetPassword;
import www.icebd.com.suzukibangladesh.reg.Signup;
import www.icebd.com.suzukibangladesh.request.Quotation;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsMyCart;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.DrawerItemCustomAdapter;
import www.icebd.com.suzukibangladesh.utilities.FontManager;
import www.icebd.com.suzukibangladesh.utilities.JsonParser;
import www.icebd.com.suzukibangladesh.utilities.ObjectDrawerItem;


public class FirstActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AsyncResponse

{
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    NavigationView navigationView;

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    final Context context = this;
    CustomDialog customDialog;
    APIFactory apiFactory;
    SetNotificationTask setNotificationTask;

    private boolean backKeyFlag = false;
    public boolean isBackKeyFlag() {
        return backKeyFlag;
    }

    public void setBackKeyFlag(boolean backKeyFlag) {
        this.backKeyFlag = backKeyFlag;
    }

    private int whichFragment = 0;
    public int getWhichFragment() {
        return whichFragment;
    }

    public void setWhichFragment(int whichFragment) {
        this.whichFragment = whichFragment;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);

        pref = getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", MODE_PRIVATE);
        editor = pref.edit();

        gotToFragment();

        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerListener(toggle);
        }
        toggle.syncState();

        setDrawerAdapter();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectItem(position);
            }
        });

        //Select home by default

        String auth_key = pref.getString("auth_key",null);
        String noti_key = pref.getString("gcm_registration_token",null);
        Log.i("Test","GCM registration token :"+noti_key);

        if (auth_key != null && noti_key == null)
        {
            HashMap<String, String> postData = new HashMap<String, String>();

            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.i("Test","Android ID : "+android_id);

            String unique_device_id = android_id;
            String platform = "1";
            apiFactory = new APIFactory();
            customDialog = new CustomDialog(context);
            if(CheckNetworkConnection.isConnectionAvailable(context) == true)
            {
                setNotificationTask = new SetNotificationTask(unique_device_id,Constant.notificationKey,auth_key,platform);
                setNotificationTask.execute((Void) null);
                //PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                //loginTask.execute(ConnectionManager.SERVER_URL+"setNotificationKey");
            }
            else
            {
                customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
            }

        }

        selectItem(1);
    /*    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uid = telephonyManager.getDeviceId();
        Log.i("Test",uid);*/


    }
    public void setDrawerAdapter()
    {
        ObjectDrawerItem[] drawerItem;
        if(pref.getString("is_login","0").equals("1")== true)
        {

            drawerItem = new ObjectDrawerItem[15];

            drawerItem[0] = new ObjectDrawerItem(getResources().getString(R.string.fa_home), mNavigationDrawerItemTitles[0]);
            drawerItem[1] = new ObjectDrawerItem(getResources().getString(R.string.fa_motorcycle), mNavigationDrawerItemTitles[1]);
            drawerItem[2] = new ObjectDrawerItem(getResources().getString(R.string.fa_spare_parts), mNavigationDrawerItemTitles[2]);
            drawerItem[3] = new ObjectDrawerItem(getResources().getString(R.string.fa_request_service), mNavigationDrawerItemTitles[3]);
            drawerItem[4] = new ObjectDrawerItem(getResources().getString(R.string.fa_news_events), mNavigationDrawerItemTitles[4]);
            drawerItem[5] = new ObjectDrawerItem(getResources().getString(R.string.fa_promotions), mNavigationDrawerItemTitles[5]);
            drawerItem[6] = new ObjectDrawerItem(getResources().getString(R.string.fa_quizzes), mNavigationDrawerItemTitles[6]);
            drawerItem[7] = new ObjectDrawerItem(getResources().getString(R.string.fa_phone), mNavigationDrawerItemTitles[7]);
            drawerItem[8] = new ObjectDrawerItem(getResources().getString(R.string.fa_invite_friends), mNavigationDrawerItemTitles[8]);
            drawerItem[9] = new ObjectDrawerItem(getResources().getString(R.string.fa_facebook_square), mNavigationDrawerItemTitles[9]);


            drawerItem[10] = new ObjectDrawerItem(getResources().getString(R.string.fa_history_of_suzuki), "HISTORY OF SUZUKI");
            drawerItem[11] = new ObjectDrawerItem(getResources().getString(R.string.fa_about_us), "ABOUT US");
            drawerItem[12] = new ObjectDrawerItem(getResources().getString(R.string.fa_change_pass), mNavigationDrawerItemTitles[10]);
            drawerItem[13] = new ObjectDrawerItem(getResources().getString(R.string.fa_sign_out), mNavigationDrawerItemTitles[12]);
            drawerItem[14] = new ObjectDrawerItem("SUZUKI", "");
        }
        else
        {
            drawerItem = new ObjectDrawerItem[14];

            drawerItem[0] = new ObjectDrawerItem(getResources().getString(R.string.fa_home), mNavigationDrawerItemTitles[0]);
            drawerItem[1] = new ObjectDrawerItem(getResources().getString(R.string.fa_motorcycle), mNavigationDrawerItemTitles[1]);
            drawerItem[2] = new ObjectDrawerItem(getResources().getString(R.string.fa_spare_parts), mNavigationDrawerItemTitles[2]);
            drawerItem[3] = new ObjectDrawerItem(getResources().getString(R.string.fa_request_service), mNavigationDrawerItemTitles[3]);
            drawerItem[4] = new ObjectDrawerItem(getResources().getString(R.string.fa_news_events), mNavigationDrawerItemTitles[4]);
            drawerItem[5] = new ObjectDrawerItem(getResources().getString(R.string.fa_promotions), mNavigationDrawerItemTitles[5]);
            drawerItem[6] = new ObjectDrawerItem(getResources().getString(R.string.fa_quizzes), mNavigationDrawerItemTitles[6]);
            drawerItem[7] = new ObjectDrawerItem(getResources().getString(R.string.fa_phone), mNavigationDrawerItemTitles[7]);
            drawerItem[8] = new ObjectDrawerItem(getResources().getString(R.string.fa_invite_friends), mNavigationDrawerItemTitles[8]);
            drawerItem[9] = new ObjectDrawerItem(getResources().getString(R.string.fa_facebook_square), mNavigationDrawerItemTitles[9]);

            drawerItem[10] = new ObjectDrawerItem(getResources().getString(R.string.fa_history_of_suzuki), "HISTORY OF SUZUKI");
            drawerItem[11] = new ObjectDrawerItem(getResources().getString(R.string.fa_about_us), "ABOUT US");
            drawerItem[12] = new ObjectDrawerItem(getResources().getString(R.string.fa_sign_in), mNavigationDrawerItemTitles[11]);
            drawerItem[13] = new ObjectDrawerItem("SUZUKI", "");
        }




        //drawerItem[13] = new ObjectDrawerItem(getResources().getString(R.string.fa_home), mNavigationDrawerItemTitles[13]);


        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
    }

    public void selectItem(int position) {

        Fragment fragment = null;

        Log.i("userid : ",pref.getString("user_id","0"));
        int userid = Integer.valueOf(pref.getString("user_id","0"));

        switch (position) {
            case 1:
                fragment = new HomeFragment().newInstance();
                break;
            case 2:
                fragment = new MyBikeFragment().newInstance();
                break;
            case 3:
                fragment = new SpareParts().newInstance();
                break;
            case 4:
                if(pref.getString("is_login","0").equals("1") && userid >0)
                {
                    fragment = new RequestServices().newInstance();
                }
                else {
                    // for login
                    //Toast.makeText(context,"Please Login",Toast.LENGTH_LONG).show();
                    Constant.isDetermin = 1;// requestForService
                    selectItem(13);
                }

                break;
            case 5:
                fragment = new NewsEvents().newInstance();
                break;
            case 6:
                fragment = new Promotions().newInstance();
                break;
            case 7:
                if(pref.getString("is_login","0").equals("1") && userid >0)
                {
                    Log.i("userid login: ","inside");
                    fragment = new Quiz().newInstance();
                }
                else {
                    // for login
                    //Toast.makeText(context,"Please Login",Toast.LENGTH_LONG).show();
                    Constant.isDetermin = 2;// quizzes
                    selectItem(13);
                }

                break;
            case 8:
                fragment = new SOS().newInstance();
                break;
            case 9:
                //fragment = new InviteFriends().newInstance();
                try {
                    JsonParser jsonParser = new JsonParser();
                    String shareBody = "Welcome to Suzuki Bangladesh Official Mobile Apps\n" +
                            "\n" +
                            jsonParser.mediaLink.getPlay_store()+"\n" +
                            "\n" +
                            jsonParser.mediaLink.getApp_store()+"\n" +
                            "\n" +
                            jsonParser.mediaLink.getFb();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));


                }catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")));
                }
                break;
            case 10:
                //fragment = new SocialMedia().newInstance();
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                break;

            case 11:
                //Toast.makeText(context,"history of suzuki",Toast.LENGTH_LONG).show();
                fragment = new HistoryOfSuzuki().newInstance();
                break;
            case 12:
                //Toast.makeText(context,"About US",Toast.LENGTH_LONG).show();
                fragment = new SuzukiAboutUs().newInstance();
                break;
            case 13:
                if(pref.getString("is_login","0").equals("1")== true)//logged in
                {
                    //Toast.makeText(context,"Change Password Page",Toast.LENGTH_LONG).show();
                    fragment = new ChangePassword().newInstance();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                else// login
                {
                    //Toast.makeText(context,"Login Page",Toast.LENGTH_LONG).show();
                    fragment = new Login().newInstance();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                break;
            case 14:
                if(pref.getString("is_login","0").equals("1")== true)
                {
                    //fragment = new Logout().newInstance();
                    goLogout();
                }
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //getActionBar().setTitle(mNavigationDrawerItemTitles[position]);
            //setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
    }
    private void goLogout()
    {
        customDialog = new CustomDialog(context);
        if(CheckNetworkConnection.isConnectionAvailable(context) == true)
        {
            HashMap<String, String> postData = new HashMap<String, String>();
            String auth_key = pref.getString("auth_key",null);
            String user_id = pref.getString("user_id",null);
            String is_login = pref.getString("is_login","0");
            if (auth_key != null)
            {
                postData.put("auth_key",auth_key);
                postData.put("user_id",user_id);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
                loginTask.execute(ConnectionManager.SERVER_URL+"logout");
            }
        }
        else
        {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }

    }
    @Override
    public void processFinish(String output) //logout async task
    {
        Log.i("Test",output);
        JSONObject object = null;
        try {
            object = new JSONObject(output);
            String status_code = object.getString("status_code");
            String message = object.getString("message");

            if (status_code.equals("200"))
            {
                editor.remove("user_id");
                editor.putString("is_login","0");
                Constant.isDetermin = 0; // for go to HOme UI
                editor.apply();
                setDrawerAdapter();
                selectItem(1);
            }
            else
            {
                Toast.makeText( context,message,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            packageManager.getPackageInfo("com.facebook.katana", 0);
            System.out.println("facebook version code: "+versionCode);

            return getResources().getString(R.string.facebook_url_schemes);
            /*if (versionCode >= 3002850) { //newer versions of fb app
                System.out.println("facebook new version");
                return "fb://facewebmodal/f?href=" + getResources().getString(R.string.facebook_page_address);
            } else { //older versions of fb app
                System.out.println("facebook old version");
                return getResources().getString(R.string.facebook_url_schemes);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("err facebook: ",e.getMessage());
            return getResources().getString(R.string.facebook_page_address); //normal web url
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
        * which fragment:
        * 0 == home
        * 1 == my bike
        * 2 == PremiumTakaJomaFragment
        * 3 == FlexiLoadJomaFragment
         */
        super.onKeyDown(keyCode, event);
        //Toast.makeText(FirstActivity.this, "back key pressed", Toast.LENGTH_SHORT).show();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isBackKeyFlag()) {

                    if(getWhichFragment() == 0)
                    {
                        selectItem(1);
                        setBackKeyFlag(false);

                    }
                    else if(getWhichFragment() == 1) {
                        selectItem(2);
                        setBackKeyFlag(false);
                    }
                    else if(getWhichFragment() == 15){
                        //selectItem(15);
                        Fragment fragment = new SparePartsMyCart().newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        setBackKeyFlag(false);
                    }
                    else if(getWhichFragment() == 4){
                        selectItem(5);
                        setBackKeyFlag(false);
                    }
                    else if(getWhichFragment() == 5){
                        selectItem(6);
                        setBackKeyFlag(false);
                    }
                    else if(getWhichFragment() == 12){
                        Fragment fragment = new Login().newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        setBackKeyFlag(false);
                    }
                    else if(getWhichFragment() == 16){
                        Fragment fragment = new Notification().newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        setBackKeyFlag(false);
                    }
                    //setBackKeyFlag(true);
                    //setWhichFragment(0);
                } else {
                    // android.os.Process.killProcess(android.os.Process.myPid());

                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    this.startActivity(i);
                }

            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifiction) {


            fragmentManager.beginTransaction()
                    .replace(R.id.container, Notification.newInstance())
                    .commit();
            return true;


        }
        else if (id==R.id.action_find_location)
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MapsActivity.newInstance())
                    .commit();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

       // return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        FragmentManager fragmentManager = getSupportFragmentManager();

        int id = item.getItemId();

        if (id == R.id.nav_my_bike) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MyBikeFragment.newInstance())
                    .commit();
        } else if (id == R.id.nav_home) {

            fragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .commit();

        } else if (id == R.id.nav_spare_parts) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SpareParts.newInstance())
                    .commit();

        } else if (id == R.id.nav_request_services) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, RequestServices.newInstance())
                    .commit();

        } else if (id == R.id.nav_news_events) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Quotation.newInstance())
                    .commit();

        } else if (id == R.id.nav_promotions) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Promotions.newInstance())
                    .commit();

        }else if (id == R.id.nav_quizzes) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Quiz.newInstance())
                    .commit();

        } else if (id == R.id.nav_sos) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SOS.newInstance())
                    .commit();

        } else if (id == R.id.nav_invite_friends) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, InviteFriends.newInstance())
                    .commit();

        }
        else if (id == R.id.nav_social_media) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SocialMedia.newInstance())
                    .commit();

        }else if (id == R.id.nav_logout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Logout.newInstance())
                    .commit();

        }else if (id == R.id.nav_change_password) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ChangePassword.newInstance())
                    .commit();

        }else if (id == R.id.nav_reset_password) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ResetPassword.newInstance())
                    .commit();

        }

        else if (id == R.id.nav_login) {


          /*  fragmentManager.beginTransaction()
                    .replace(R.id.container, Login.newInstance())
                    .commit();*/
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Login.newInstance())
                    .commit();

        }
        else if (id == R.id.nav_sign_up) {


          /*  fragmentManager.beginTransaction()
                    .replace(R.id.container, Login.newInstance())
                    .commit();*/
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Signup.newInstance())
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*public void onSectionAttached(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();



        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MyBikeFragment.newInstance())
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }*/


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class SetNotificationTask extends AsyncTask<Void, Void, String>
    {
        private String RESULT = "OK";
        private ArrayList<NameValuePair> returnJsonData;
        private ArrayList<NameValuePair> nvp2=null;
        private InputStream response;
        private JsonParser jsonParser;

        private String methodName = "";
        private String unique_device_id, notification_key, auth,platform;

        //UserLoginTask(String email, String password)
        SetNotificationTask(String unique_device_id,String notification_key, String auth,String platform)
        {
            this.unique_device_id = unique_device_id;
            this.notification_key = notification_key;
            this.auth = auth;
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
                    nvp2 = apiFactory.setNotificationKeyInfo(unique_device_id,notification_key,auth,platform);
                    methodName = "setNotificationKey";
                    response = ConnectionManager.getResponseFromServer(methodName, nvp2);
                    jsonParser = new JsonParser();

                    System.out.println("server response : "+response);
                    returnJsonData = jsonParser.parseAPIsetNotificationKeyInfo(response);
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
                        Toast.makeText(getApplicationContext(), returnJsonData.get(1).getValue(), Toast.LENGTH_SHORT).show();
                        editor.putString("gcm_registration_token",Constant.notificationKey);
                        editor.apply();
                        //String auth_key = returnJsonData.get(3).getValue();
                        //editor.putString("auth_key",auth_key);
                        //editor.commit();

                    }
                    else
                    {
                        System.out.println("data return : " + returnJsonData);
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
            setNotificationTask = null;
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
                    System.out.println("inside news");
                    //selectItem(2);
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
