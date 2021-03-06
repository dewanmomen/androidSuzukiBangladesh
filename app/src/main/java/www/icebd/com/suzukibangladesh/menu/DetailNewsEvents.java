package www.icebd.com.suzukibangladesh.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;


public class DetailNewsEvents extends Fragment {

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    TextView title,description;//startDate,endDate;
    ImageView imageView;

    ImageLoader imageLoader;
    Context context;
    ProgressBar progressBar;
    DisplayImageOptions options;

    public static DetailNewsEvents newInstance() {
        DetailNewsEvents fragment = new DetailNewsEvents();
        return fragment;
    }

    public DetailNewsEvents() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details_news_events, container,
                false);
        context = getActivity().getApplicationContext();
        Bundle bundle = this.getArguments();
        String whichView = bundle.getString("viewTitleName");
        getActivity().setTitle(whichView);
        String news_event_title = bundle.getString( "news_event_title" );
        String news_event_desc = bundle.getString( "news_event_desc" );
        String news_event_img = bundle.getString( "news_event_img" );

        imageLoader = ImageLoader.getInstance();
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        HashMap<String, String> postData = new HashMap<String, String>();
        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        title = (TextView) rootView.findViewById(R.id.txt_pro_title);
        description = (TextView) rootView.findViewById(R.id.txt_pro_descrip);
        //startDate = (TextView) rootView.findViewById(R.id.txt_pro_startdate);
        //endDate = (TextView) rootView.findViewById(R.id.txt_pro_enddate);
        imageView = (ImageView)rootView.findViewById(R.id.img_pro);

        title.setText(news_event_title);
        description.setText(news_event_desc);
        //startDate.setText(start_date);
        //endDate.setText(end_date);

        if(news_event_img != null)
        {
            imageView.setVisibility(View.VISIBLE);
            imageLoader.displayImage(String.valueOf(Uri.parse(news_event_img)), imageView,options,
            new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view,int current, int total) {
                    progressBar.setProgress(Math.round(100.0f * current/ total));
                }
            });
        }
        else
        {
            imageView.setVisibility(View.GONE);
            //Toast.makeText(context,"No Data Found, please Try Again !",Toast.LENGTH_SHORT).show();
            Log.i("Image :", "No image url found");
        }


        String auth_key = pref.getString("auth_key","empty");


        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(4);
        return rootView;
    }


}
