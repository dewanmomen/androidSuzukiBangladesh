package www.icebd.com.suzukibangladesh.json;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import www.icebd.com.suzukibangladesh.app.Constants;
import www.icebd.com.suzukibangladesh.bikedetails.BikeDetails;
import www.icebd.com.suzukibangladesh.maps.MapsActivity;
import www.icebd.com.suzukibangladesh.menu.BottomHomeFragment;
import www.icebd.com.suzukibangladesh.menu.HomeFragment;
import www.icebd.com.suzukibangladesh.menu.NewsEvents;
import www.icebd.com.suzukibangladesh.menu.Promotions;
import www.icebd.com.suzukibangladesh.notification.Notification;
import www.icebd.com.suzukibangladesh.quiz.Quiz;
import www.icebd.com.suzukibangladesh.reg.Login;
import www.icebd.com.suzukibangladesh.reg.ChangePassword;
import www.icebd.com.suzukibangladesh.reg.Logout;
import www.icebd.com.suzukibangladesh.reg.ResetPassword;
import www.icebd.com.suzukibangladesh.reg.Signup;
import www.icebd.com.suzukibangladesh.request.Quotation;
import www.icebd.com.suzukibangladesh.request.RequestServices;

/**
 * Created by Oum Saokosal, the author of KosalGeek on 9/6/15.
 * Get More Free Source Codes at https://github.com/kosalgeek
 * Subscribe my Youtube channel https://youtube.com/c/Kosalgeekvideos
 * Follow Me on Twitter https://twitter.com/kosalgeek
 */
public class PostResponseAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;

    private AsyncResponse delegate;
    private Context context;
    private HashMap<String, String> postData = new HashMap<String, String>();
    private String loadingMessage = "Loading...";


    //Constructor
    public PostResponseAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
        this.context = (Context)delegate;
    }

    public PostResponseAsyncTask(FragmentActivity delegate, HashMap<String, String> postData){
        this.delegate = (AsyncResponse) delegate;
        this.context = (Context)delegate;
        this.postData = postData;
    }

    public PostResponseAsyncTask(AsyncResponse delegate, String loadingMessage){
        this.delegate = delegate;
        this.context = (Context)delegate;
        this.loadingMessage = loadingMessage;
    }

    public PostResponseAsyncTask(AsyncResponse delegate, HashMap<String, String> postData, String loadingMessage){
        this.delegate = delegate;
        this.context = (Context)delegate;
        this.postData = postData;
        this.loadingMessage = loadingMessage;
    }

    public PostResponseAsyncTask(Signup signup, HashMap<String, String> postData) {
        this.delegate=signup;
        this.context=signup.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(ResetPassword resetPassword, HashMap<String, String> postData) {
        this.delegate=resetPassword;
        this.context=resetPassword.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(Login login, HashMap<String, String> postData) {
        this.delegate=login;
        this.context=login.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(ChangePassword changePassword, HashMap<String, String> postData) {
        this.delegate=changePassword;
        this.context=changePassword.getContext();
        this.postData=postData;

    }

    public PostResponseAsyncTask(Logout logout, HashMap<String, String> postData) {
        this.delegate=logout;
        this.context=logout.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(Quotation quotation, HashMap<String, String> postData) {
        this.delegate=quotation;
        this.context=quotation.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(RequestServices requestServices, HashMap<String, String> postData) {
        this.delegate=requestServices;
        this.context=requestServices.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(BikeDetails bikeDetails, HashMap<String, String> postData) {
        this.delegate=bikeDetails;
        this.context=bikeDetails.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(Constants constants, HashMap<String, String> postData) {
        this.delegate=constants;
        this.context=this.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(HomeFragment homeFragment, HashMap<String, String> postData) {
        this.delegate=homeFragment;
        this.context=homeFragment.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(BottomHomeFragment bottomHomeFragment, HashMap<String, String> postData) {
        this.delegate=bottomHomeFragment;
        this.context=bottomHomeFragment.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(MapsActivity mapsActivity, HashMap<String, String> postData) {
        //this.delegate=mapsActivity;
        this.context=mapsActivity.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(Quiz quiz, HashMap<String, String> postData) {
        this.delegate=quiz;
        this.context=quiz.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(Promotions promotions, HashMap<String, String> postData) {
        this.delegate=promotions;
        this.context=promotions.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(NewsEvents newsEvents, HashMap<String, String> postData) {
        this.delegate=newsEvents;
        this.context=newsEvents.getContext();
        this.postData=postData;
    }

    public PostResponseAsyncTask(Notification notification, HashMap<String, String> postData) {

        this.delegate=notification;
        this.context=notification.getContext();
        this.postData=postData;
    }


    //End Constructor

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(loadingMessage);
        progressDialog.show();
        super.onPreExecute();
    }//onPreExecute

    @Override
    protected String doInBackground(String... urls){

        String result = "";

        for(int i = 0; i <= 0; i++){

            result = invokePost(urls[i], postData);
        }

        return result;
    }//doInBackground

    private String invokePost(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

                Log.i("PostResponseAsyncTask", responseCode + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }//performPostCall

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }//getPostDataString

    @Override
    protected void onPostExecute(String result) {

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        result = result.trim();

        delegate.processFinish(result);
    }//onPostExecute

    //Setter and Getter
    public String getLoadingMessage() {
        return loadingMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public HashMap<String, String> getPostData() {
        return postData;
    }

    public void setPostData(HashMap<String, String> postData) {
        this.postData = postData;
    }

    public Context getContext() {
        return context;
    }

    public AsyncResponse getDelegate() {
        return delegate;
    }

    //End Setter & Getter
}