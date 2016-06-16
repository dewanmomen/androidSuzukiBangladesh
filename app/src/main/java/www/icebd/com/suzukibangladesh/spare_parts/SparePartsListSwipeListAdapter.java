package www.icebd.com.suzukibangladesh.spare_parts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.util.ArrayList;
import java.util.List;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.FontManager;
import www.icebd.com.suzukibangladesh.utilities.Utils;


/**
 * Created by Momen Dewan on 4/26/2016.
 */
public class SparePartsListSwipeListAdapter extends BaseAdapter implements Filterable
{
    //private Activity activity;
    private LayoutInflater inflater;
    private List<SparePartsListObject.SparePartsItem> listSparePartsItem = null;
    private List<SparePartsListObject.SparePartsItem> listSparePartsItemSearch;
    private String[] bgColors;
    public ImageLoader imageLoader = null;
    DisplayImageOptions options;
    Utils utilClass = new Utils();
    SparePartsList sparePartsListFragment;

    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    Context context;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;

    /*****************************/
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    final int NONE = 0;
    final int DRAG = 1;
    final int ZOOM = 2;
    int mode = NONE;

    /*****************************/

    public SparePartsListSwipeListAdapter(Context context, List<SparePartsListObject.SparePartsItem> listSparePartsItem, SparePartsList sparePartsListFragment) {
        this.context = context;
        this.listSparePartsItem = listSparePartsItem;
        this.listSparePartsItemSearch = new ArrayList<SparePartsListObject.SparePartsItem>();
        this.listSparePartsItemSearch.addAll(listSparePartsItem);
        this.sparePartsListFragment = sparePartsListFragment;
        mInflater = LayoutInflater.from(context);
        //bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);

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
    }

    @Override
    public int getCount() {
        return listSparePartsItem.size();
    }

    @Override
    public Object getItem(int location) {
        return listSparePartsItem.get(location);
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
            convertView = inflater.inflate(R.layout.my_spare_parts_list_row, null);*/
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.my_spare_parts_list_row, null);
            holder = new Holder();
            Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

            holder.sparePartsImg = (ImageView) convertView.findViewById(R.id.id_spare_parts_img);
            holder.progressBar=(ProgressBar)convertView.findViewById(R.id.rn_progress);
            holder.txtSparePartsName = (TextView) convertView.findViewById(R.id.txt_spare_parts_name);
            holder.txtSparePartsBikeName = (TextView) convertView.findViewById(R.id.id_spareparts_bike_name);
            holder.txtSparePartsNumber = (TextView) convertView.findViewById(R.id.id_spareparts_number);
            holder.txtSparePartsPrice = (TextView) convertView.findViewById(R.id.id_spare_parts_price);

            holder.txtSparePartsAddToCart = (TextView) convertView.findViewById(R.id.add_to_cart_img);
            holder.txtSparePartsAddToCart.setTypeface(iconFont);


            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        //holder.bikeImg.setImageResource( bikeList.get(position).thumble_img );
        //holder.bikeImg.setImageIcon( Icon.createWithContentUri( bikeList.get(position).thumble_img ) );
        Log.v("Spare Parts Name : ",listSparePartsItem.get(position).getSpare_parts_name().toString());

        //BitmapDrawable drawableBitmap=new BitmapDrawable(Utils.getBitmapFromURL(listSparePartsItem.get(position).getThumble_img()));
        //holder.sparePartsImg.setBackgroundDrawable(drawableBitmap);

        holder.sparePartsImg.setImageResource(0);
        holder.sparePartsImg.setImageResource(android.R.color.transparent);
        //imageLoader.clearMemoryCache();
        //holder.sparePartsImg.setBackgroundColor(context.getResources().getColor(R.color.common_action_bar_splitter));

        MemoryCacheUtils.removeFromCache(listSparePartsItem.get(position).getThumble_img(), imageLoader.getMemoryCache());
        //DiskCacheUtils.removeFromCache(bikeList.get(position).getThumble_img(), imageLoader.getDiskCache());

        imageLoader.displayImage(listSparePartsItem.get(position).getThumble_img(), holder.sparePartsImg, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        //holder.progressBar.setProgress(0);
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
                /*}, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view,int current, int total) {
                        holder.progressBar.setProgress(Math.round(100.0f * current/ total));
                    }*/
                });

        holder.txtSparePartsName.setText( listSparePartsItem.get(position).getSpare_parts_name().toString() );
        holder.txtSparePartsBikeName.setText( listSparePartsItem.get(position).getBike_name().toString() );
        holder.txtSparePartsNumber.setText( listSparePartsItem.get(position).getSpare_parts_code().toString() );
        holder.txtSparePartsPrice.setText( "TK "+listSparePartsItem.get(position).getSpare_parts_price().toString() );
        holder.txtSparePartsName.setText( listSparePartsItem.get(position).getSpare_parts_name().toString() );

        holder.txtSparePartsAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = context.getSharedPreferences("SuzukiBangladeshPref", context.MODE_PRIVATE);
                editor = pref.edit();
                String user_id = pref.getString("user_id", "0");
                if(user_id.equals("0"))
                {
                    Toast.makeText(context, "Please Login to Add Item to My Cart !", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, listSparePartsItem.get(position).getSpare_parts_name().toString()+" Added to Cart", Toast.LENGTH_SHORT).show();
                    /*SparePartsListObject obj_sparePartsList = new SparePartsListObject();
                    SparePartsListObject.SparePartsItem obj_sparePartsItem = obj_sparePartsList.new SparePartsItem();

                    MyCartObject myCartObject = new MyCartObject(obj_sparePartsItem,qnt+1);
                    Constant.listMyCartObj.add(obj_sparePartsItem);
                    */
                    AddToGlobalArray(listSparePartsItem.get(position));
                }

            }
        });

        holder.sparePartsImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getAlertDialogWithImage(listSparePartsItem.get(position).getThumble_img(),listSparePartsItem.get(position).getSpare_parts_name().toString());
            }
        });



        return convertView;
    }
    public void AddToGlobalArray( SparePartsListObject.SparePartsItem Item) {

        if (Constant.listMyCartObj.size() == 0)
        {
            Item.setaInteger(1);
            Constant.listMyCartObj.add(Item);
            //shopActivity.changeShopButtonText();
        }
        else
        {
            boolean b = true;
            for (SparePartsListObject.SparePartsItem s : Constant.listMyCartObj) {
                if (s.getSpare_parts_id().equals(Item.getSpare_parts_id()))
                {
                    //Toast.makeText(context,context.getResources().getString(R.string.toast_shop_cart_membership_alert),Toast.LENGTH_LONG).show();
                    s.setaInteger(s.getaInteger() + 1);
                    b = false;
                    //break;
                }
            }
            if (b)
            {
                Item.setaInteger(Item.getaInteger() + 1);
                Constant.listMyCartObj.add(Item);
                //shopActivity.changeShopButtonText();
            }
        }

    }
    private void getAlertDialogWithImage(String img_uri,String product_name)
    {
        //Toast.makeText(context,img_uri,Toast.LENGTH_LONG).show();
        try
        {
                /*URL url = new URL(img_uri);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bmImg = BitmapFactory.decodeStream(is);*/

            ImageView photo = new ImageView(context);
            photo.setScaleType(ImageView.ScaleType.MATRIX);
            //photo.setImageBitmap(bmImg);
            photo.setRotation(90);
            photo.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View v, MotionEvent event)
                {
                    ImageView view = (ImageView) v;
                    System.out.println("matrix=" + savedMatrix.toString());
                    switch (event.getAction() & MotionEvent.ACTION_MASK)
                    {
                        case MotionEvent.ACTION_DOWN:
                            savedMatrix.set(matrix);
                            startPoint.set(event.getX(), event.getY());
                            mode = DRAG;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            oldDist = spacing(event);
                            if (oldDist > 10f)
                            {
                                savedMatrix.set(matrix);
                                midPoint(midPoint, event);
                                mode = ZOOM; }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            mode = NONE;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (mode == DRAG)
                            {
                                matrix.set(savedMatrix);
                                matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                            }
                            else if (mode == ZOOM)
                            {
                                float newDist = spacing(event);
                                if (newDist > 10f)
                                {
                                    matrix.set(savedMatrix);
                                    float scale = newDist / oldDist;
                                    matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                                }
                            }
                            break;
                    }
                    view.setImageMatrix(matrix);
                    return true;
                }
                @SuppressLint("FloatMath") private float spacing(MotionEvent event)
                {
                    float x = event.getX(0) - event.getX(1);
                    float y = event.getY(0) - event.getY(1);

                    //return FloatMath.sqrt(x * x + y * y);\
                    return (float)Math.sqrt(x * x + y * y);
                }
                private void midPoint(PointF point, MotionEvent event)
                {
                    float x = event.getX(0) + event.getX(1);
                    float y = event.getY(0) + event.getY(1);
                    point.set(x / 2, y / 2);
                }
            });


            ImageLoader.getInstance().displayImage(img_uri,photo);

            LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParamsL = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(layoutParamsL);
            layout.setGravity(Gravity.CENTER);
            int widthPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, context.getResources().getDisplayMetrics());
            int heightPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, context.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPX, heightPX);


            photo.setLayoutParams(layoutParams);
            layout.addView(photo);

            //LayoutInflater factory = LayoutInflater.from(context);
            //final View view = factory.inflate(layout.getId(),null);
            AlertDialog.Builder alert =
                    new AlertDialog.Builder(sparePartsListFragment.getActivity()).
                            setTitle(product_name).
                            setMessage(""). // this is quite ugly
                            setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setView(layout);
            //final AlertDialog alert = builder.create();
            alert.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public Filter getFilter()
    {
        return mFilter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            /*String filterString = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            SparePartsListObject obj_sparePartsList = new SparePartsListObject();
            List<SparePartsListObject.SparePartsItem> filterItems = new ArrayList<SparePartsListObject.SparePartsItem>();
            try
            {
                //int count = list.size();
                //final ArrayList<String> nlist = new ArrayList<String>(count);
                if (constraint != null && constraint.toString().length() > 0)
                {
                    String filterableString;
                    synchronized (this)
                    {
                        for (int i = 0; i < listSparePartsItem.size(); i++)
                        {

                            SparePartsListObject.SparePartsItem obj_sparePartsItem = listSparePartsItem.get(i);
                            filterableString = obj_sparePartsItem.getSpare_parts_name();
                            if (filterableString.toLowerCase().contains(filterString))
                            {
                                filterItems.add(obj_sparePartsItem);
                            }
                        }
                        result.count = filterItems.size();
                        result.values = filterItems;
                        //result.values = nlist;
                        //result.count = nlist.size();
                    }// end synchronized
                } else
                {
                    synchronized (this)
                    {
                        result.count = listSparePartsItem.size();
                        result.values = listSparePartsItem;
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                Log.e("search-err: ", ex.getMessage());
            }
            return result;*/
            FilterResults result = new FilterResults();
            constraint = constraint.toString().toLowerCase();
            listSparePartsItem.clear();
            if (constraint.length() == 0)
            {
                listSparePartsItem.addAll(listSparePartsItemSearch);
            }
            else
            {
                for (SparePartsListObject.SparePartsItem spi : listSparePartsItemSearch)
                {

                    //SparePartsListObject.SparePartsItem obj_sparePartsItem = listSparePartsItem.get(i);
                    if (spi.getSpare_parts_name().toLowerCase().contains(constraint))
                    {
                        listSparePartsItem.add(spi);
                    }
                }
            }
            result.count = listSparePartsItem.size();
            result.values = listSparePartsItem;

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listSparePartsItem = (ArrayList<SparePartsListObject.SparePartsItem>) results.values;
            notifyDataSetChanged();
        }

    }

    static class Holder
    {
        public ProgressBar progressBar;
        public ImageView sparePartsImg;
        public TextView txtSparePartsName;
        public TextView txtSparePartsBikeName;
        public TextView txtSparePartsNumber;
        public TextView txtSparePartsPrice;
        public TextView txtSparePartsAddToCart;
    }


}
