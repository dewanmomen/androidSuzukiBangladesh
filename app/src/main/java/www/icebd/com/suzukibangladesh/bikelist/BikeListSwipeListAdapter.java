package www.icebd.com.suzukibangladesh.bikelist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.bikedetails.BikeDetails;
import www.icebd.com.suzukibangladesh.menu.MyBikeFragment;
import www.icebd.com.suzukibangladesh.utilities.Utils;


/**
 * Created by Momen Dewan on 4/26/2016.
 */
public class BikeListSwipeListAdapter extends BaseAdapter
{
    //private Activity activity;
    private LayoutInflater inflater;
    private List<BikeList.BikeItem> bikeList;
    private String[] bgColors;
    public ImageLoader imageLoader = null;
    DisplayImageOptions options;
    Utils utilClass = new Utils();
    MyBikeFragment myBikeFragment;

    Context context;

    public BikeListSwipeListAdapter(Context context, List<BikeList.BikeItem> bikeList,MyBikeFragment myBikeFragment) {
        this.context = context;
        this.bikeList = bikeList;
        this.myBikeFragment = myBikeFragment;
        //bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null).cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        return bikeList.size();
    }

    @Override
    public Object getItem(int location) {
        return bikeList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*if (convertView == null)
            convertView = inflater.inflate(R.layout.my_bikelist_row, null);*/
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.my_bikelist_row, null);
            holder = new Holder();
            holder.bikeImg = (ImageView) convertView.findViewById(R.id.id_bike_img);
            holder.progressBar=(ProgressBar)convertView.findViewById(R.id.rn_progress);
            holder.txtEngine_cc = (TextView) convertView.findViewById(R.id.engine_cc);
            holder.txtMileage_kilo = (TextView) convertView.findViewById(R.id.mileage_kilo);
            holder.txtBike_name = (TextView) convertView.findViewById(R.id.bike_name);


            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        //holder.bikeImg.setImageResource( bikeList.get(position).thumble_img );
        //holder.bikeImg.setImageIcon( Icon.createWithContentUri( bikeList.get(position).thumble_img ) );
        Log.v("Engine : ",bikeList.get(position).getBike_cc().toString());
        imageLoader.displayImage(bikeList.get(position).getThumble_img(), holder.bikeImg, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.progressBar.setProgress(0);
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view,int current, int total) {
                        holder.progressBar.setProgress(Math.round(100.0f * current/ total));
                    }
                });
        Log.v("Engine : ",bikeList.get(position).getBike_cc().toString());
        holder.txtEngine_cc.setText( bikeList.get(position).getBike_cc().toString() );
        holder.txtMileage_kilo.setText( bikeList.get(position).getBike_mileage().toString() );
        holder.txtBike_name.setText( bikeList.get(position).getBike_name().toString() );

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked "+bikeList.get(position).getBike_id(), Toast.LENGTH_LONG).show();

                //MyBikeFragment fa = (MyBikeFragment) context.getSupportFragmentManager().findFragmentById(R.id.container);
                //MyBikeFragment fa = new MyBikeFragment();
                FragmentManager fragmentManager = myBikeFragment.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                BikeDetails fragmentBikeDetails = new BikeDetails();
                Bundle bundle = new Bundle();
                bundle.putInt( "bike_id", Integer.parseInt( bikeList.get(position).getBike_id()) );
                fragmentBikeDetails.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragmentBikeDetails);
                fragmentTransaction.commit();
                //myBikeFragment.goBikeDetails(Integer.parseInt( bikeList.get(position).getBike_id()));
            }
        });



        //String color = bgColors[position % bgColors.length];
        //serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }
    static class Holder
    {
        public ProgressBar progressBar;
        public ImageView bikeImg;
        public TextView txtEngine_cc;
        public TextView txtMileage_kilo;
        public TextView txtBike_name;
    }

}
