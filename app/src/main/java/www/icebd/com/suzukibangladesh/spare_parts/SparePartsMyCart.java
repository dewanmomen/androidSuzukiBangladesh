package www.icebd.com.suzukibangladesh.spare_parts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.bikedetails.BikeDetails;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
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

    String[] targetArray, description_TargetArray;
    ArrayList<String> Description_array_forlist = new ArrayList<String>();

    ArrayAdapter<String> adapter, cart_adapter;

    //ListView stream_List;

    PopupWindow qytSelectionWindow;

    double total_price_footer;
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
        dispDefault = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        deviceTotalWidth = dispDefault.getWidth();
        deviceTotalHeight = dispDefault.getHeight();



        //Bundle bundle = this.getArguments();
        //int myInt = bundle.getInt("selectedTab", 0);

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
    private void callPayWithBKashUI()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SparePartsPayments sparePartsPayments = new SparePartsPayments();
        Bundle bundle = new Bundle();
        bundle.putInt( "bike_id", 1);
        sparePartsPayments.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, sparePartsPayments);
        fragmentTransaction.commit();
    }

    private void CreateListforShopingCart(
            final List<SparePartsListObject.SparePartsItem> listMyCartObj) {

        if(listMyCartObj.size() == 0)
        {
            textview_no_item_add_card.setText(getResources().getString(R.string.no_items_display_txt));
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

                    price.setText(String.format("%.2f", priceofTotalItem) + Html.fromHtml("&#x9f3;"));// for taka -> 	&#x9f3; for euro -> &#8364;
                }
                else
                {
                    price.setText("0"+ Html.fromHtml("&#x9f3;"));
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

        total_with_tax.setText(String.format("%.2f", total_price_footer) + Html.fromHtml("&#x9f3;"));

        //double totalTaxAmount = (total_price_footer* Constant.taxPercentage)/100;
        //double totalAfterSubtractionTaxAmount = total_price_footer - totalTaxAmount;

        //total_price.setText(String.format("%.2f", totalAfterSubtractionTaxAmount) + Html.fromHtml("&#x9f3;"));
        total_price.setText(String.format("%.2f", total_price_footer) + Html.fromHtml("&#x9f3;"));
    }
}
