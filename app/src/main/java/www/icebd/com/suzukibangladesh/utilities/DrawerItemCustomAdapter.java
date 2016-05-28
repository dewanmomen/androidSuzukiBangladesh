package www.icebd.com.suzukibangladesh.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import www.icebd.com.suzukibangladesh.utilities.ObjectDrawerItem;
import www.icebd.com.suzukibangladesh.R;

/**
 * Created by Momen Dewan on 4/26/2016.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem>
{

    Context mContext;
    int layoutResourceId;
    ObjectDrawerItem data[] = null;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    ObjectDrawerItem folder;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data)
    {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        pref = mContext.getSharedPreferences("SuzukiBangladeshPref", mContext.MODE_PRIVATE);
        editor = pref.edit();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
        listItem.setMinimumHeight(20);

        Typeface iconFont = FontManager.getTypeface(this.getContext(), FontManager.FONTAWESOME);
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/SuzukiPROBold.otf");

        TextView imageViewIcon = (TextView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        if(position == 0)
        {
            listItem.setMinimumHeight(30);
            folder = data[data.length-1];
            //imageViewIcon.setText(folder.icon);
            //imageViewIcon.setTextSize(20);
            //imageViewIcon.setTypeface(custom_font);

            /*textViewName.setClickable(false);
            imageViewIcon.setClickable(false);
            imageViewIcon.setFocusableInTouchMode(false);
            textViewName.setFocusableInTouchMode(false);*/

            textViewName.setText(folder.name);
            imageViewIcon.setVisibility(View.GONE);
            textViewName.setTextSize(20);
            textViewName.setTypeface(custom_font);

        }
        /*else if(position == data.length-1)
        {
            //
        }*/
        else
        {
            folder = data[position-1];
            imageViewIcon.setText(folder.icon);
            imageViewIcon.setTypeface(iconFont);
            textViewName.setText(folder.name);
        }

        //imageViewIcon.setText(folder.icon);
        //imageViewIcon.setTypeface(iconFont);
        //textViewName.setText(folder.name);

        return listItem;
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        // return false if position == position you want to disable
        if(position == 0)
            return false;
        else
            return true;
    }

}
