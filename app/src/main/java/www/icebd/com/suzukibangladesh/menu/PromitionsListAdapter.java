package www.icebd.com.suzukibangladesh.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.utilities.Utils;


/**
 * Created by Momen Dewan on 4/26/2016.
 */
public class PromitionsListAdapter extends BaseAdapter
{
    //private Activity activity;
    private LayoutInflater inflater;
    ArrayList<HashMap<String, String>> arrList;
    private String[] bgColors;
    public ImageLoader imageLoader = null;
    DisplayImageOptions options;
    Utils utilClass = new Utils();
    Promotions promotions;

    Context context;

    public PromitionsListAdapter(Context context, ArrayList<HashMap<String, String>> arrList, Promotions promotions) {
        this.context = context;
        this.arrList = arrList;
        this.promotions = promotions;
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
        return arrList.size();
    }

    @Override
    public Object getItem(int location) {
        return arrList.get(location);
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
            convertView = inflater.inflate(R.layout.list_item_promotions, null);
            holder = new Holder();
            holder.image_list = (ImageView) convertView.findViewById(R.id.image_list);
            holder.txt_title=(TextView)convertView.findViewById(R.id.txt_title);
            holder.end_date=(TextView)convertView.findViewById(R.id.end_date);
            holder.description = (TextView) convertView.findViewById(R.id.description);


            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        Log.v("news title : ",arrList.get(position).get("promo_title").toString());
        imageLoader.displayImage(arrList.get(position).get("promo_image"), holder.image_list, options,
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

        holder.txt_title.setText( arrList.get(position).get("promo_title").toString() );
        holder.description.setText( arrList.get(position).get("promo_desc").toString() );
        holder.end_date.setText( "Valid till: "+arrList.get(position).get("end_date").toString() );


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = promotions.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                PromotionsDetails promotionsDetails = new PromotionsDetails();
                Bundle bundle = new Bundle();

                bundle.putString("viewTitleName", "PROMOTIONS DETAILS");

                bundle.putString( "promo_title", arrList.get(position).get("promo_title").toString() );
                bundle.putString( "promo_desc", arrList.get(position).get("promo_desc").toString() );
                bundle.putString( "promo_image", arrList.get(position).get("promo_image").toString() );
                //bundle.putString( "news_event_start_date", arrList.get(position).get("news_event_title").toString() );
                //bundle.putString( "news_event_end_date", arrList.get(position).get("news_event_title").toString() );
                promotionsDetails.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, promotionsDetails);
                fragmentTransaction.commit();
            }
        });



        //String color = bgColors[position % bgColors.length];
        //serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }
    static class Holder
    {
        //public ProgressBar progressBar;

        public TextView txt_title;
        public TextView description;
        public TextView end_date;
        public ImageView image_list;
    }

}
