package www.icebd.com.suzukibangladesh.spare_parts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.bikedetails.BikeDetails;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;
import www.icebd.com.suzukibangladesh.utilities.ListViewUtil;
import www.icebd.com.suzukibangladesh.utilities.Tools;


public class SparePartsMyCart extends Fragment
{
    Context context;
    CustomDialog customDialog;
    TextView textview_no_item_add_card;
    ListView spare_parts_cardlist;
    View view_under_spare_parts_cardlist;

    RelativeLayout spare_parts_table_footer;
    LinearLayout delivery_info_layout,home_deleivery_layout,collect_from_store_layout;
    TextView label_home_delivery,label_picup_point,txt_picup_point,left,right;
    RadioGroup radio_group_inside_outside_dhk;
    RadioButton radio_inside_dhaka,radio_outside_dhaka;
    EditText txt_deleivery_address;

    View view_delivery_line;
    TextView row_txt_delivery;
    TextView deliver_price;

    String[] targetArray, description_TargetArray;
    ArrayList<String> Description_array_forlist = new ArrayList<String>();

    ArrayAdapter<String> adapter, cart_adapter;

    //ListView stream_List;

    PopupWindow qytSelectionWindow;

    double total_price_footer,deliver_amount;
    TextView item_cart, total_price, total_with_tax, subtotal_tv, total_with_vat_tv;

    Display dispDefault =null;
    int value = 0, deviceTotalWidth, deviceTotalHeight;


    Button checkout_thisCart;

    public static SparePartsMyCart newInstance() {
        SparePartsMyCart fragment = new SparePartsMyCart();
        return fragment;
    }

    public SparePartsMyCart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spare_parts_my_cart, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("My Cart");
        setupUI(rootView.findViewById(R.id.shop_mother_layout));

        dispDefault = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        deviceTotalWidth = dispDefault.getWidth();
        deviceTotalHeight = dispDefault.getHeight();

        Bundle bundle = this.getArguments();
        int myInt = bundle.getInt("selectedTab", 0);

        setupUIInitialize(rootView);

        //Constant.delivery_category = null;
        //Constant.delivery_type = 0;
        deliver_amount = 0;
        radio_group_inside_outside_dhk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.radio_inside_dhaka:
                        Constant.delivery_category = "home_delivery";
                        deliver_amount = 120;
                        Constant.delivery_type = 1;
                        view_delivery_line.setVisibility(View.VISIBLE);
                        row_txt_delivery.setVisibility(View.VISIBLE);
                        deliver_price.setVisibility(View.VISIBLE);
                        deliver_price.setText(String.format("TK %.2f", deliver_amount));
                        SetFinalPrice();
                        //Toast.makeText(context,"radio clicked inside_dhaka", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_outside_dhaka:
                        Constant.delivery_category = "home_delivery";
                        deliver_amount = 150;
                        Constant.delivery_type = 2;
                        view_delivery_line.setVisibility(View.VISIBLE);
                        row_txt_delivery.setVisibility(View.VISIBLE);
                        deliver_price.setVisibility(View.VISIBLE);
                        deliver_price.setText(String.format("TK %.2f", deliver_amount));
                        SetFinalPrice();
                        //Toast.makeText(context,"radio clicked outside_dhaka", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        Constant.delivery_category = "home_delivery";
        home_deleivery_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Constant.delivery_type = 0;
                deliver_amount = 0;
                radio_group_inside_outside_dhk.clearCheck();
                home_deleivery_layout.setBackgroundColor(context.getResources().getColor(R.color.suzuki_blue_color));
                label_home_delivery.setTextColor(context.getResources().getColor(R.color.white));
                left.setTextColor(context.getResources().getColor(R.color.white));
                collect_from_store_layout.setBackgroundColor(context.getResources().getColor(R.color.line_grey));
                label_picup_point.setTextColor(context.getResources().getColor(R.color.black));
                right.setTextColor(context.getResources().getColor(R.color.black));
                view_delivery_line.setVisibility(View.GONE);
                row_txt_delivery.setVisibility(View.GONE);
                deliver_price.setVisibility(View.GONE);

                txt_picup_point.setVisibility(View.GONE);
                Constant.delivery_category = "home_delivery";

                radio_group_inside_outside_dhk.setVisibility(View.VISIBLE);
                txt_deleivery_address.setVisibility(View.VISIBLE);
            }
        });
        collect_from_store_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                collect_from_store_layout.setBackgroundColor(context.getResources().getColor(R.color.suzuki_blue_color));
                label_picup_point.setTextColor(context.getResources().getColor(R.color.white));
                right.setTextColor(context.getResources().getColor(R.color.white));
                home_deleivery_layout.setBackgroundColor(context.getResources().getColor(R.color.line_grey));
                label_home_delivery.setTextColor(context.getResources().getColor(R.color.black));
                left.setTextColor(context.getResources().getColor(R.color.black));
                view_delivery_line.setVisibility(View.GONE);
                row_txt_delivery.setVisibility(View.GONE);
                deliver_price.setVisibility(View.GONE);

                radio_group_inside_outside_dhk.setVisibility(View.GONE);
                txt_deleivery_address.setVisibility(View.GONE);

                txt_picup_point.setVisibility(View.VISIBLE);
                Constant.delivery_category = "pickup_point";
                Constant.delivery_type = 0;
                txt_picup_point.setText(context.getResources().getString(R.string.picup_delivery_text));
                SetFinalPrice();

            }
        });

        if(Constant.delivery_category.toString().equals("pickup_point") == true) // for saved data
        {
            System.out.println("inside pickup point");
            collect_from_store_layout.setBackgroundColor(context.getResources().getColor(R.color.suzuki_blue_color));
            label_picup_point.setTextColor(context.getResources().getColor(R.color.white));
            right.setTextColor(context.getResources().getColor(R.color.white));
            home_deleivery_layout.setBackgroundColor(context.getResources().getColor(R.color.line_grey));
            label_home_delivery.setTextColor(context.getResources().getColor(R.color.black));
            left.setTextColor(context.getResources().getColor(R.color.black));
            view_delivery_line.setVisibility(View.GONE);
            row_txt_delivery.setVisibility(View.GONE);
            deliver_price.setVisibility(View.GONE);

            radio_group_inside_outside_dhk.setVisibility(View.GONE);
            txt_deleivery_address.setVisibility(View.GONE);

            txt_picup_point.setVisibility(View.VISIBLE);
            Constant.delivery_category = "pickup_point";
            Constant.delivery_type = 0;
            txt_picup_point.setText(context.getResources().getString(R.string.picup_delivery_text));
            SetFinalPrice();
        }
        //else if(Constant.delivery_category.equals("home_delivery") == true)
        if(Constant.delivery_type == 1)
        {
            System.out.println("inside home delivery type 1");
            home_deleivery_layout.setBackgroundColor(context.getResources().getColor(R.color.suzuki_blue_color));
            label_home_delivery.setTextColor(context.getResources().getColor(R.color.white));
            left.setTextColor(context.getResources().getColor(R.color.white));
            collect_from_store_layout.setBackgroundColor(context.getResources().getColor(R.color.line_grey));
            label_picup_point.setTextColor(context.getResources().getColor(R.color.black));
            right.setTextColor(context.getResources().getColor(R.color.black));
            view_delivery_line.setVisibility(View.VISIBLE);
            row_txt_delivery.setVisibility(View.VISIBLE);
            deliver_price.setVisibility(View.VISIBLE);

            txt_picup_point.setVisibility(View.GONE);
            Constant.delivery_category = "home_delivery";

            radio_group_inside_outside_dhk.setVisibility(View.VISIBLE);
            txt_deleivery_address.setVisibility(View.VISIBLE);

                radio_inside_dhaka.setChecked(true);
                radio_outside_dhaka.setChecked(false);

            SetFinalPrice();
        }
        else if(Constant.delivery_type == 2)
        {
            System.out.println("inside home delivery type 2");
            home_deleivery_layout.setBackgroundColor(context.getResources().getColor(R.color.suzuki_blue_color));
            label_home_delivery.setTextColor(context.getResources().getColor(R.color.white));
            left.setTextColor(context.getResources().getColor(R.color.white));
            collect_from_store_layout.setBackgroundColor(context.getResources().getColor(R.color.line_grey));
            label_picup_point.setTextColor(context.getResources().getColor(R.color.black));
            right.setTextColor(context.getResources().getColor(R.color.black));
            view_delivery_line.setVisibility(View.VISIBLE);
            row_txt_delivery.setVisibility(View.VISIBLE);
            deliver_price.setVisibility(View.VISIBLE);

            txt_picup_point.setVisibility(View.GONE);
            Constant.delivery_category = "home_delivery";

            radio_group_inside_outside_dhk.setVisibility(View.VISIBLE);
            txt_deleivery_address.setVisibility(View.VISIBLE);


                radio_outside_dhaka.setChecked(true);
                radio_inside_dhaka.setChecked(false);
            SetFinalPrice();
        }

        CreateListforShopingCart(Constant.listMyCartObj);
        SetFinalPrice();

        checkout_thisCart = (Button) rootView.findViewById(R.id.checkout_thisCart);
        checkout_thisCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPayWithBKashUI();
            }
        });

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);
        return rootView;
    }
    private void setupUIInitialize(View rootView)
    {
        Typeface iconFont = FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME);
        textview_no_item_add_card = (TextView) rootView.findViewById(R.id.textview_no_item_add_card);
        spare_parts_cardlist = (ListView) rootView.findViewById(R.id.spare_parts_cardlist);
        view_under_spare_parts_cardlist = (View) rootView.findViewById(R.id.view_under_spare_parts_cardlist);


        spare_parts_table_footer = (RelativeLayout) rootView.findViewById(R.id.spare_parts_table_footer);

        //stream_List = (ListView) rootView.findViewById(R.id.shop_streamlist);
        //item_cart = (TextView) rootView.findViewById(R.id.iteam_at_cart);
        subtotal_tv =(TextView) rootView.findViewById(R.id.subtotal_tv);
        total_price = (TextView) rootView.findViewById(R.id.cart_totalPrice);

        total_with_vat_tv =(TextView) rootView.findViewById(R.id.total_with_vat_tv);

        total_with_tax =(TextView) rootView.findViewById(R.id.total_with_tax);

        delivery_info_layout = (LinearLayout)rootView.findViewById(R.id.delivery_info_layout);
        home_deleivery_layout = (LinearLayout)rootView.findViewById(R.id.home_deleivery_layout);
        collect_from_store_layout = (LinearLayout)rootView.findViewById(R.id.collect_from_store_layout);
        label_home_delivery = (TextView)rootView.findViewById(R.id.label_home_delivery);
        label_picup_point = (TextView)rootView.findViewById(R.id.label_picup_point);
        txt_picup_point = (TextView)rootView.findViewById(R.id.txt_picup_point);


        radio_group_inside_outside_dhk = (RadioGroup)rootView.findViewById(R.id.radio_group_inside_outside_dhk);
        radio_inside_dhaka = (RadioButton)rootView.findViewById(R.id.radio_inside_dhaka);
        radio_outside_dhaka = (RadioButton)rootView.findViewById(R.id.radio_outside_dhaka);
        txt_deleivery_address = (EditText) rootView.findViewById(R.id.txt_deleivery_address);
        left = (TextView)rootView.findViewById(R.id.left);
        left.setText(context.getResources().getString(R.string.fa_home_delivery));
        left.setTypeface(iconFont);
        right = (TextView)rootView.findViewById(R.id.right);
        right.setText(context.getResources().getString(R.string.fa_picup_point));
        right.setTypeface(iconFont);

        view_delivery_line = (View) rootView.findViewById(R.id.view_delivery_line);
        view_delivery_line.setVisibility(View.GONE);
        row_txt_delivery = (TextView) rootView.findViewById(R.id.row_txt_delivery);
        row_txt_delivery.setVisibility(View.GONE);
        deliver_price = (TextView) rootView.findViewById(R.id.deliver_price);
        deliver_price.setVisibility(View.GONE);
    }


    private void callPayWithBKashUI()
    {
        if( Constant.delivery_category.equals("home_delivery") )
        {
            if( radio_inside_dhaka.isChecked()==false && radio_outside_dhaka.isChecked()==false)
            {
                Toast.makeText(context,"Please Select Your Delivery Location !",Toast.LENGTH_LONG).show();
            }
            else if( txt_deleivery_address.getText().toString().length() == 0)
            {
                Toast.makeText(context,"Please Enter Your Delivery Address !",Toast.LENGTH_LONG).show();
            }
            else
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SparePartsPayments sparePartsPayments = new SparePartsPayments();
                Bundle bundle = new Bundle();
                bundle.putString("delivery_charge", String.valueOf(deliver_amount));
                bundle.putString("total_item_price", String.valueOf(total_price_footer));
                bundle.putString("gross_amount", String.valueOf(total_price_footer+deliver_amount));
                bundle.putString("delivery_address", txt_deleivery_address.getText().toString());

                sparePartsPayments.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, sparePartsPayments);
                fragmentTransaction.commit();
            }
        }
        else
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            SparePartsPayments sparePartsPayments = new SparePartsPayments();
            Bundle bundle = new Bundle();
            bundle.putString("delivery_charge", String.valueOf(deliver_amount));
            bundle.putString("total_item_price", String.valueOf(total_price_footer));
            bundle.putString("gross_amount", String.valueOf(total_price_footer+deliver_amount));
            bundle.putString("delivery_address", txt_deleivery_address.getText().toString());
            sparePartsPayments.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, sparePartsPayments);
            fragmentTransaction.commit();
        }
    }

    private void CreateListforShopingCart(
            final List<SparePartsListObject.SparePartsItem> listMyCartObj) {

        if(listMyCartObj.size() == 0)
        {
            textview_no_item_add_card.setText(context.getResources().getString(R.string.no_items_display_txt));
            textview_no_item_add_card.setVisibility(View.VISIBLE);
            //Item.setPadding(0, 10, 0, 0);
            spare_parts_table_footer.setVisibility(View.GONE);
        }
        else
        {
            textview_no_item_add_card.setVisibility(View.GONE);
            spare_parts_table_footer.setVisibility(View.VISIBLE);
        }

        Description_array_forlist.clear();

        for (int y = 0; y < listMyCartObj.size(); y++) {

            Description_array_forlist.add(listMyCartObj.get(y).getSpare_parts_name());
        }

        description_TargetArray = new String[Description_array_forlist.size()];
        Description_array_forlist.toArray(description_TargetArray);

        cart_adapter = new ArrayAdapter<String>(context,
                R.layout.row_shoppingcart, R.id.cart_header,
                description_TargetArray) {

            TextView NumberOfItem;

            @Override
            public View getView(final int position, View convertView,
                                ViewGroup parent) {
                View row = super.getView(position, convertView, parent);

                NumberOfItem = (TextView) row
                        .findViewById(R.id.number_of_item_inCart);

                NumberOfItem.setText("" + listMyCartObj.get(position).getaInteger());
                NumberOfItem.setTag(position);
                NumberOfItem.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        qytSelectionWindow = popupWindowForQtySelection(listMyCartObj, position, cart_adapter);
                        //qytSelectionWindow.showAsDropDown(v, -5, 0);
                        qytSelectionWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                    }
                });

                ImageView cart_item_delete = (ImageView) row.findViewById(R.id.cart_item_delete);

                cart_item_delete.setTag(position);
                cart_item_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listMyCartObj.get(position).setaInteger(0);
                        Constant.listMyCartObj.remove(position);
                        SetFinalPrice();
                        CreateListforShopingCart(Constant.listMyCartObj);
                        //changeShopButtonText();
                    }
                });

                TextView cart_header = (TextView) row.findViewById(R.id.cart_header);
                //utilClass.setCustomFont(getApplicationContext(),"Raleway-SemiBold.ttf",cart_header);
                String cartHeaderData = listMyCartObj.get(position).getSpare_parts_name();
                cart_header.setText(cartHeaderData);
				/*if(cartHeaderData.length()>21){
					cart_header.setText(cartHeaderData.substring(0, 22)+"..");
				}
				else {
					cart_header.setText(cartHeaderData);
				}*/

                TextView cart_item_des = (TextView) row.findViewById(R.id.cart_item_des);
                //utilClass.setCustomFont(getApplicationContext(),"Raleway-SemiBold.ttf",cart_item_des);
                String descriptionData = listMyCartObj.get(position).getSpare_parts_code();
                cart_item_des.setText(descriptionData);
				/*if(descriptionData.length()>50)
				{
					cart_item_des.setText(descriptionData.substring(0, 51)+"..");
				}
				else
				{
					cart_item_des.setText(descriptionData);
				}*/

                TextView price = (TextView) row
                        .findViewById(R.id.single_price_of_item);
                //utilClass.setCustomFont(getApplicationContext(),"Raleway-Regular.ttf",price);

                if(Tools.isDouble(Constant.listMyCartObj.get(position).getSpare_parts_price().trim())==true)
                {
                    double priceofTotalItem = ((Double
                            .parseDouble(Constant.listMyCartObj.get(position)
                                    .getSpare_parts_price().trim())) * (Constant.listMyCartObj
                            .get(position).getaInteger()));

                    //price.setText(String.format("%.2f", priceofTotalItem) + Html.fromHtml("&#x9f3;"));// for taka -> 	&#x9f3; for euro -> &#8364;
                    price.setText(String.format("TK %.2f", priceofTotalItem));
                }
                else
                {
                    //price.setText("0"+ Html.fromHtml("&#x9f3;"));
                    price.setText("TK 0");
                }

                cart_adapter.notifyDataSetChanged();

                return row;
            }
        };

        cart_adapter.notifyDataSetChanged();
        spare_parts_cardlist.setDivider(new ColorDrawable(Color.parseColor("#000000")));
        spare_parts_cardlist.setDividerHeight(1);
        spare_parts_cardlist.setAdapter(cart_adapter);
        ListViewUtil.setListViewHeightBasedOnChildren(spare_parts_cardlist);
		/*spare_parts_cardlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});*/

    }
    public void changeShopButtonText()
    {
        int sum = 0;
        for (SparePartsListObject.SparePartsItem s : Constant.listMyCartObj) {

            sum = sum + s.getaInteger();
        }

        if (Constant.listMyCartObj.size() == 0) {
            item_cart.setVisibility(View.INVISIBLE);
        } else {
            item_cart.setVisibility(View.VISIBLE);
            item_cart.setText(String.valueOf(sum));
            item_cart.setTextColor(Color.WHITE);
        }

    }

    //popupWindow For Qty Selection
    public PopupWindow popupWindowForQtySelection(final List<SparePartsListObject.SparePartsItem> listMyCartObj, final int pos, final ArrayAdapter<String> cart_adapter_inqty_selection)
    {
        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(context);

        // the drop down list is a list view
        final ListView qtyListView = new ListView(context);

        // set our adapter and pass our pop up window contents
        List<String> qtyData = new ArrayList<String>();
        for(int i=0;i<50;i++)
        {
            qtyData.add(String.valueOf(i));
        }

        final ArrayAdapter<String> qtyDataAdapter = new ArrayAdapter<String>(context,R.layout.row_item_qty, qtyData);
        //qtyDataAdapter.setDropDownViewResource(R.layout.profile_spinner_dropdown_item);
        qtyDataAdapter.notifyDataSetChanged();
        qtyListView.setAdapter(qtyDataAdapter);
        qtyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long rowId) {

                String str ="";
                str= qtyListView.getItemAtPosition(position).toString();

                //remove item from cart if 0 qty is selected
                if(Integer.valueOf(str)==0)
                {
                    listMyCartObj.get(pos).setaInteger(0);
                    Constant.listMyCartObj.remove(pos);
                    SetFinalPrice();
                    CreateListforShopingCart(Constant.listMyCartObj);
                    changeShopButtonText();
                }

                else
                {
                    listMyCartObj.get(pos).setaInteger(Integer.valueOf(str));
                    changeShopButtonText();

                    cart_adapter_inqty_selection.notifyDataSetChanged();
                    SetFinalPrice();
                }

                popupWindow.dismiss();
            }
        });

        int qtyPopUpWidth = (int) (deviceTotalWidth/1.25);
        int qtyPopUpHeight = (int) (deviceTotalHeight/2);
        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(qtyPopUpWidth);
        popupWindow.setHeight(qtyPopUpHeight);

        // set the list view as pop up window content
        popupWindow.setContentView(qtyListView);

        return popupWindow;
    }

    private void SetFinalPrice() {
        total_price_footer = 0;
        for (int k = 0; k < Constant.listMyCartObj.size(); k++) {
            total_price_footer = ((Double.parseDouble(Constant.listMyCartObj
                    .get(k).getSpare_parts_price().trim())) * (Constant.listMyCartObj
                    .get(k).getaInteger())) + total_price_footer;
        }

        //total_with_tax.setText(String.format("%.2f", total_price_footer) + Html.fromHtml("&#x9f3;"));
        if( Constant.delivery_type == 0)
        {
            total_with_tax.setText(String.format("TK %.2f", total_price_footer));
        }
        else if( Constant.delivery_type == 1) // inside dhaka = 120
        {
            total_with_tax.setText(String.format("TK %.2f", total_price_footer+120));
        }
        else if( Constant.delivery_type == 2) // outside dhaka = 150
        {
            total_with_tax.setText(String.format("TK %.2f", total_price_footer+150));
        }

        //double totalTaxAmount = (total_price_footer* Constant.taxPercentage)/100;
        //double totalAfterSubtractionTaxAmount = total_price_footer - totalTaxAmount;

        //total_price.setText(String.format("%.2f", totalAfterSubtractionTaxAmount) + Html.fromHtml("&#x9f3;"));

        //total_price.setText(String.format("%.2f", total_price_footer) + Html.fromHtml("&#x9f3;"));
        total_price.setText(String.format("TK %.2f", total_price_footer));
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
