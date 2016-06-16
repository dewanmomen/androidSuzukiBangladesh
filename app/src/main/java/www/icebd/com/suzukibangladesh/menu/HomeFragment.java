package www.icebd.com.suzukibangladesh.menu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


import me.relex.circleindicator.CircleIndicator;
import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.app.Constants;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.notification.MainActivity;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;

import static com.google.android.gms.internal.zzir.runOnUiThread;


public class HomeFragment extends Fragment implements AsyncResponse {


    private Button btnMyBike,btnSpareParts,btnRequestServices,btnNews_Events,btnPromtoins,btnInviteFriends;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    String gallery_image [];
    int NUM_PAGES;
    CircleIndicator indicator;
    int  currentPage=0;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    ViewPager pager;

    Context context;
    CustomDialog customDialog;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("Home");
        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

        pref = context.getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();


        btnMyBike = (Button) rootView.findViewById(R.id.btn_my_parts);
        btnSpareParts = (Button) rootView.findViewById(R.id.btn_spare_parts);
        btnRequestServices = (Button) rootView.findViewById(R.id.btn_req_services);
        btnNews_Events = (Button) rootView.findViewById(R.id.btn_news_events);
        btnPromtoins = (Button) rootView.findViewById(R.id.btn_promotion);
        btnInviteFriends = (Button) rootView.findViewById(R.id.btn_invite_friends);

        btnMyBike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((FirstActivity)getActivity()).selectItem(2);


            }
        });
        btnSpareParts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((FirstActivity)getActivity()).selectItem(3);

            }
        });
        btnRequestServices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((FirstActivity)getActivity()).selectItem(4);

            }
        });
        btnNews_Events.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((FirstActivity)getActivity()).selectItem(5);

            }
        });
        btnPromtoins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((FirstActivity)getActivity()).selectItem(6);

            }
        });
        btnInviteFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((FirstActivity)getActivity()).selectItem(10);

            }
        });




        if (pager==null) {
            String auth_key = pref.getString("auth_key",null);

            if(auth_key!=null)
            {
                HashMap<String, String> postData = new HashMap<String, String>();
                postData.put("auth_key",auth_key);

                // getSupportFragmentManager().beginTransaction().replace(R.id.frag, fragmentS1).commit();
                customDialog = new CustomDialog(context);
                if(CheckNetworkConnection.isConnectionAvailable(context) == true)
                {
                    PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                    loginTask.execute(ConnectionManager.SERVER_URL + "getGallery");
                }
                else
                {
                    customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
                }

            }
            pager = (ViewPager) rootView.findViewById(R.id.pager);
            indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        }




        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((FirstActivity) activity).onSectionAttached(0);
    }

    @Override
    public void processFinish(String output) {

        try {
            JSONObject object = new JSONObject(output);
            String status_code = object.getString("status_code");
            String message = object.getString("message");
            if (status_code.equals("200"))
            {
                JSONArray gallery = object.getJSONArray("gallery");
                gallery_image =new String[gallery.length()];
                NUM_PAGES = gallery.length();

                for (int i = 0; i <gallery.length() ; i++) {
                    JSONObject galleryDetails = gallery.getJSONObject(i);
                    String g_image =galleryDetails.getString("g_image");
                    gallery_image[i]=g_image;
                    Log.i("Test",g_image);


                }
                if(gallery_image != null)
                {
                    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                    pager.setAdapter(new ImageAdapter((context)));

                    //viewpager.setAdapter(mPageAdapter);
                    indicator.setViewPager(pager);


                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {


                                @Override
                                public void run() {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    pager.setCurrentItem(currentPage++, true);
                                }
                            });
                        }
                    }, 500, 3000);
                    //pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

    private class ImageAdapter extends PagerAdapter {



        private String[] IMAGE_URLS = gallery_image;

        private LayoutInflater inflater;
        private DisplayImageOptions options;
        public ImageLoader imageLoader = null;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(null)
                    .showImageForEmptyUri(null)
                    .showImageOnFail(null)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            /*options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageOnLoading(null)
                    .showImageForEmptyUri(null)
                    .showImageOnFail(null)
                    .resetViewBeforeLoading(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();*/
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            MemoryCacheUtils.removeFromCache(IMAGE_URLS[position], imageLoader.getMemoryCache());
            //DiskCacheUtils.removeFromCache(bikeList.get(position).getThumble_img(), imageLoader.getDiskCache());

            imageLoader.displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    //Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
