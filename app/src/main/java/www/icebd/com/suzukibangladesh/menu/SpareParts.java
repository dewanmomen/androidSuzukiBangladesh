package www.icebd.com.suzukibangladesh.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.bikelist.BikeList;
import www.icebd.com.suzukibangladesh.bikelist.BikeListSwipeListAdapter;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsList;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsMyCart;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;


public class SpareParts extends Fragment
{
    Context context;

    LinearLayout spare_parts_tab_layout;
    TextView font_spare_parts;
    TextView txt_spare_parts;

    LinearLayout my_cart_tab_layout;
    TextView font_my_cart;
    TextView txt_my_cart;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private BikeListSwipeListAdapter bikeListSwipeListAdapter;
    private List<BikeList.BikeItem> bikeList;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private SparePartsList sparePartsList;
    private SparePartsMyCart sparePartsMyCart;

    APIFactory apiFactory;
    CustomDialog customDialog;
    ProgressDialog progressDialog;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    public static SpareParts newInstance() {
        SpareParts fragment = new SpareParts();
        return fragment;
    }

    public SpareParts() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spare_parts, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("SPARE PARTS");
        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        spare_parts_tab_layout = (LinearLayout) view.findViewById(R.id.spare_parts_tab_layout);
        font_spare_parts = (TextView) view.findViewById(R.id.font_spare_parts);
        font_spare_parts.setTypeface(iconFont);
        txt_spare_parts = (TextView) view.findViewById(R.id.txt_spare_parts);
        spare_parts_tab_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                spare_parts_tab_layout.setBackgroundColor(getResources().getColor(R.color.suzuki_blue_color));
                font_spare_parts.setTextColor(getResources().getColor(R.color.white));
                txt_spare_parts.setTextColor(getResources().getColor(R.color.white));

                my_cart_tab_layout.setBackgroundColor(getResources().getColor(R.color.line_grey));
                font_my_cart.setTextColor(getResources().getColor(R.color.black));
                txt_my_cart.setTextColor(getResources().getColor(R.color.black));

                goToSparePartsListFragment(0);
            }
        });

        my_cart_tab_layout = (LinearLayout) view.findViewById(R.id.my_cart_tab_layout);
        font_my_cart = (TextView) view.findViewById(R.id.font_my_cart);
        font_my_cart.setTypeface(iconFont);
        txt_my_cart = (TextView) view.findViewById(R.id.txt_my_cart);
        my_cart_tab_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                my_cart_tab_layout.setBackgroundColor(getResources().getColor(R.color.suzuki_blue_color));
                font_my_cart.setTextColor(getResources().getColor(R.color.white));
                txt_my_cart.setTextColor(getResources().getColor(R.color.white));

                spare_parts_tab_layout.setBackgroundColor(getResources().getColor(R.color.line_grey));
                font_spare_parts.setTextColor(getResources().getColor(R.color.black));
                txt_spare_parts.setTextColor(getResources().getColor(R.color.black));

                goToSparePartsAddToCartFragment(0);
            }
        });
        initTop();

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);

        return view;
    }
    private void initTop()
    {
        sparePartsList = new SparePartsList();
        Bundle bundle = new Bundle();
        bundle.putInt("selectedTab", 0);
        sparePartsList.setArguments(bundle);

        fragmentTransaction.replace(R.id.spare_parts_main_fragmentArea, sparePartsList);
        //fragmentTransaction.addToBackStack("A");
        //fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.commit();
    }
    public void goToSparePartsListFragment(int a) {

        fragmentTransaction = fragmentManager.beginTransaction();
        sparePartsList = new SparePartsList();
        Bundle bundle = new Bundle();
        bundle.putInt("selectedTab", a);
        sparePartsList.setArguments(bundle);
        fragmentTransaction.replace(R.id.spare_parts_main_fragmentArea, sparePartsList);
        // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void goToSparePartsAddToCartFragment(int a) {
        //Toast.makeText(getActivity(), "inside method", Toast.LENGTH_SHORT).show();
        fragmentTransaction = fragmentManager.beginTransaction();
        sparePartsMyCart = new SparePartsMyCart();
        Bundle bundle = new Bundle();
        bundle.putInt("selectedTab", a);
        sparePartsMyCart.setArguments(bundle);
        fragmentTransaction.replace(R.id.spare_parts_main_fragmentArea, sparePartsMyCart);
        // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
