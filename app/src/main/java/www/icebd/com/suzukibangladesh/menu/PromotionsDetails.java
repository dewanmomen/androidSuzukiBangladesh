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
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;


public class PromotionsDetails extends Fragment {

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    TextView title,description;//startDate,endDate;
    ImageView imageView;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    Context context;
    CustomDialog customDialog;

    public static PromotionsDetails newInstance() {
        PromotionsDetails fragment = new PromotionsDetails();
        return fragment;
    }

    public PromotionsDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_promotion_details, container,
                false);
        context = getActivity().getApplicationContext();
        Bundle bundle = this.getArguments();
        String whichView = bundle.getString("viewTitleName");
        getActivity().setTitle(whichView);
        String promo_title = bundle.getString( "promo_title" );
        String promo_desc = bundle.getString( "promo_desc" );
        String promo_image = bundle.getString( "promo_image" );

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

        title.setText(promo_title);
        description.setText(promo_desc);
        //startDate.setText(start_date);
        //endDate.setText(end_date);

        if(promo_image != null)
        {
            imageView.setVisibility(View.VISIBLE);
            imageLoader.displayImage(String.valueOf(Uri.parse(promo_image)), imageView);
        }
        else
        {
            Toast.makeText(context,"No Data Found, please Try Again !",Toast.LENGTH_SHORT).show();
        }


        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(5);
        return rootView;
    }


}
