package www.icebd.com.suzukibangladesh.request;

import android.content.SharedPreferences;
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

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;


public class RFSNotificationFragment extends Fragment {

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    TextView txt_header,txt_body,txt_footer;//startDate,endDate;
    ImageView imageView;

    String headerText = "";
    String bodyText = "";
    String footerText = "";
    String img_url = "";

    ImageLoader imageLoader;

    public static RFSNotificationFragment newInstance() {
        RFSNotificationFragment fragment = new RFSNotificationFragment();
        return fragment;
    }

    public RFSNotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rfs_notification, container,
                false);

        Bundle bundle = getArguments();
        String whichView = bundle.getString("viewTitleName");
        headerText = bundle.getString("headerText");
        bodyText = bundle.getString("bodyText");
        footerText = bundle.getString("footerText");
        img_url = bundle.getString("img_url");
        getActivity().setTitle(whichView);

        imageLoader = ImageLoader.getInstance();

        HashMap<String, String> postData = new HashMap<String, String>();
        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        txt_header = (TextView) rootView.findViewById(R.id.txt_header);
        txt_body = (TextView) rootView.findViewById(R.id.txt_body);
        txt_footer = (TextView) rootView.findViewById(R.id.txt_footer);
        //startDate = (TextView) rootView.findViewById(R.id.txt_pro_startdate);
        //endDate = (TextView) rootView.findViewById(R.id.txt_pro_enddate);
        imageView = (ImageView)rootView.findViewById(R.id.img_pro);
        if(img_url != null)
        {
            imageView.setVisibility(View.VISIBLE);
            imageLoader.displayImage(String.valueOf(Uri.parse(img_url)), imageView);
        }
        else
        {
            imageView.setVisibility(View.GONE);
            //Toast.makeText(context,"No Data Found, please Try Again !",Toast.LENGTH_SHORT).show();
            Log.i("Image :", "No image url found");
        }

        txt_header.setText(headerText);
        txt_body.setText(bodyText);
        txt_footer.setText(footerText);

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(16);
        return rootView;
    }

}
