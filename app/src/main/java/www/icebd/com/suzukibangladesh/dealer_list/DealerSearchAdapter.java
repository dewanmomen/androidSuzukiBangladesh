package www.icebd.com.suzukibangladesh.dealer_list;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.utilities.FontManager;

/**
 * Created by Acer on 5/24/2016.
 */
public class DealerSearchAdapter extends BaseAdapter implements Filterable {

    private List<Custom_dealer_list> mOriginalValues; // Original Values
    private List<Custom_dealer_list> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;
    Context context;
    private List<Custom_dealer_list> dealer_lists = new ArrayList<Custom_dealer_list>();

    public DealerSearchAdapter() {
    }

    public DealerSearchAdapter(Context context, List<Custom_dealer_list> mProductArrayList) {
        this.context = context;
        this.mOriginalValues = mProductArrayList;
        //this.mOriginalValues.addAll(mProductArrayList);
        this.mDisplayedValues = mProductArrayList;
        //inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        LinearLayout llContainer;
        TextView tvName, tvPrice;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.custom_dealer_list, parent, false);
        }

        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

        final Custom_dealer_list custom_dealer_list = mDisplayedValues.get(position);

        TextView shop_title = (TextView) itemView.findViewById(R.id.shop_title);
        TextView contact_person_icon = (TextView) itemView.findViewById(R.id.contact_person_icon);
        TextView contact_person = (TextView) itemView.findViewById(R.id.contact_person);
        TextView shop_address_icon = (TextView) itemView.findViewById(R.id.shop_address_icon);
        TextView shop_address = (TextView) itemView.findViewById(R.id.shop_address);
        TextView mobile_number_icon = (TextView) itemView.findViewById(R.id.mobile_number_icon);
        TextView mobile_number = (TextView) itemView.findViewById(R.id.mobile_number);
        TextView map_icon = (TextView) itemView.findViewById(R.id.map_icon);
        TextView caller_icon = (TextView) itemView.findViewById(R.id.caller_icon);
        TextView caller = (TextView) itemView.findViewById(R.id.caller);
        TextView shop_type = (TextView) itemView.findViewById(R.id.shop_type);

        shop_title.setText(custom_dealer_list.getTitle());

        contact_person_icon.setText(context.getResources().getString(R.string.l_contact_person_icon));
        contact_person_icon.setTypeface(iconFont);
        contact_person.setText(custom_dealer_list.getContact_person());

        shop_address_icon.setText(context.getResources().getString(R.string.l_address));
        shop_address_icon.setTypeface(iconFont);
        shop_address.setText(custom_dealer_list.getAddress());

        mobile_number_icon.setText(context.getResources().getString(R.string.l_mobile_number));
        mobile_number_icon.setTypeface(iconFont);
        mobile_number.setText(custom_dealer_list.getMobile_number());

        map_icon.setText(context.getResources().getString(R.string.l_map));
        map_icon.setTypeface(iconFont);

        caller_icon.setText(context.getResources().getString(R.string.l_mobile_number));
        caller_icon.setTypeface(iconFont);
        caller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dialer_number = "tel:" + custom_dealer_list.getMobile_number();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(dialer_number));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);
            }
        });

        if(custom_dealer_list.getShop_type().equals("1")){
            shop_type.setText("Show Room");
        }
        else{
            shop_type.setText("Service Center");
        }

        return itemView;
    }

    /*@Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_dealer_list, null);
            holder.llContainer = (LinearLayout) convertView.findViewById(R.id.llContainer);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            //holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mDisplayedValues.get(position).shop_title);
        //holder.tvPrice.setText(mDisplayedValues.get(position).price + "");

        holder.llContainer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            }
        });

        return convertView;
    }*/

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<Custom_dealer_list>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Custom_dealer_list> FilteredArrList = new ArrayList<Custom_dealer_list>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Custom_dealer_list>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase().trim();
                    Log.d("TAG","Constraint Value is: " + constraint);
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String shop_title = mOriginalValues.get(i).shop_title;
                        String contact_person = mOriginalValues.get(i).contact_person;
                        String address = mOriginalValues.get(i).address;
                        String mobile_number = mOriginalValues.get(i).mobile_number;
                        String shop_type = mOriginalValues.get(i).shop_type;
                        if (shop_title.toLowerCase().startsWith(constraint.toString()) || contact_person.toLowerCase().startsWith(constraint.toString())
                                || address.toLowerCase().startsWith(constraint.toString()) || mobile_number.toLowerCase().startsWith(constraint.toString())
                                || shop_type.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Custom_dealer_list(mOriginalValues.get(i).shop_title, mOriginalValues.get(i).contact_person, mOriginalValues.get(i).address
                                    , mOriginalValues.get(i).mobile_number, mOriginalValues.get(i).shop_type));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
