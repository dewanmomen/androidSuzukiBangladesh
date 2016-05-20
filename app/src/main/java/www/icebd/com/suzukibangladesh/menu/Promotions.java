package www.icebd.com.suzukibangladesh.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.reg.Login;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;


public class Promotions extends Fragment implements AsyncResponse {

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    TextView title,description;//startDate,endDate;
    ImageView imageView;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    Context context;
    CustomDialog customDialog;

    public static Promotions newInstance() {
        Promotions fragment = new Promotions();
        return fragment;
    }

    public Promotions() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_promotion, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("PROMOTIONS");

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null).cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        HashMap<String, String> postData = new HashMap<String, String>();
        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        title = (TextView) rootView.findViewById(R.id.txt_pro_title);
        description = (TextView) rootView.findViewById(R.id.txt_pro_descrip);
        //startDate = (TextView) rootView.findViewById(R.id.txt_pro_startdate);
        //endDate = (TextView) rootView.findViewById(R.id.txt_pro_enddate);
        imageView = (ImageView)rootView.findViewById(R.id.img_pro);
        imageView.setVisibility(View.GONE);

        String auth_key = pref.getString("auth_key","empty");

        customDialog = new CustomDialog(context);
        if(CheckNetworkConnection.isConnectedToInternet(context) == true)
        {
            if (!auth_key.equals("empty")) {
                postData.put("auth_key", auth_key);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                loginTask.execute(ConnectionManager.SERVER_URL + "promoInfo");
            }
        }
        else
        {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);
        return rootView;
    }

    @Override
    public void processFinish(String output) {

        Log.i("Test",output);

        try {
            JSONObject object = new JSONObject(output);
            String status_code = object.getString("status_code");
            String message = object.getString("message");
            String auth_key = object.getString("auth_key");


            if (status_code.equals("200"))
            {
               // Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

                JSONArray promotion = object.getJSONArray("promotion");
                JSONObject promotionDetails = promotion.getJSONObject(0);
                String promo_id = promotionDetails.getString("promo_id");
                String promo_title = promotionDetails.getString("promo_title");
                String promo_desc = promotionDetails.getString("promo_desc");
                String promo_image = promotionDetails.getString("promo_image");
                String start_date = promotionDetails.getString("start_date");
                String end_date = promotionDetails.getString("end_date");

                Log.i("Test","title "+promo_title);

                title.setText(promo_title);
                description.setText(promo_desc);
                //startDate.setText(start_date);
                //endDate.setText(end_date);

                if(promo_image != null)
                {
                    imageView.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(String.valueOf(Uri.parse(promo_image)), imageView, options,
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    //holder.progressBar.setProgress(0);
                                    //holder.progressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                }
                            }, new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view,int current, int total) {
                                    //holder.progressBar.setProgress(Math.round(100.0f * current/ total));
                                }
                            });
                }
                else
                {
                    Log.i("image url not found", promo_image);
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
