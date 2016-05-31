package www.icebd.com.suzukibangladesh.menu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import www.icebd.com.suzukibangladesh.R;


public class SocialMedia extends Fragment {
    Context context;


    public static SocialMedia newInstance() {
        SocialMedia fragment = new SocialMedia();
        return fragment;
    }

    public SocialMedia() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_social_media, container,
                false);
        context = getActivity().getApplicationContext();

        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);


        return rootView;
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            //System.out.println("facebook version code: "+versionCode);

            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + context.getResources().getString(R.string.facebook_page_address);
            } else { //older versions of fb app
                return context.getResources().getString(R.string.facebook_url_schemes);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //Log.e("err facebook: ",e.getMessage());
            return context.getResources().getString(R.string.facebook_page_address); //normal web url
        }
    }

}
