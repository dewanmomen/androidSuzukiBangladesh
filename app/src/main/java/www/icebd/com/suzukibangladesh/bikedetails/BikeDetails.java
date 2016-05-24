package www.icebd.com.suzukibangladesh.bikedetails;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.request.Quotation;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsPayments;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class BikeDetails extends Fragment implements AsyncResponse {
    TableLayout tableLayout;
    LinearLayout linearLayoutBikeFone;
    LinearLayout.LayoutParams lp;
    TextView bike_name_tv;
    TextView txt_bike_cc;
    Button btn_get_a_quote;
    WebView webView;

    String gallery_image[];
    String image_color[];
    int NUM_PAGES;
    Typeface iconFont;

    CircleIndicator indicator;
    int currentPage = 0;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    ViewPager pager;
    private String global_bike_name = "";

    Context context;
    CustomDialog customDialog;

    SharedPreferences pref;
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

    public static BikeDetails newInstance() {
        BikeDetails fragment = new BikeDetails();
        return fragment;
    }

    public BikeDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bike_details, container,
                false);

        context = getActivity().getApplicationContext();
        getActivity().setTitle("Bike Details");
        pref = context.getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

        Bundle bundle = this.getArguments();
        final int bike_id = bundle.getInt("bike_id");

        linearLayoutBikeFone = (LinearLayout) rootView.findViewById(R.id.linear_font_bike);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        webView = (WebView) rootView.findViewById(R.id.help_webview);

        Log.i("Bike ID: ", String.valueOf(bike_id));
        tableLayout = (TableLayout) rootView.findViewById(R.id.main_table);
        bike_name_tv = (TextView) rootView.findViewById(R.id.bike_name);
        txt_bike_cc = (TextView) rootView.findViewById(R.id.bike_cc);
        btn_get_a_quote = (Button) rootView.findViewById(R.id.btn_get_a_quote);

        HashMap<String, String> postData = new HashMap<String, String>();

        String auth_key = pref.getString("auth_key", null);
        postData.put("auth_key", auth_key);
        postData.put("bike_id", String.valueOf(bike_id));

        customDialog = new CustomDialog(getActivity());
        if (CheckNetworkConnection.isConnectionAvailable(context) == true) {
            PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
            loginTask.execute(ConnectionManager.SERVER_URL + "getBikeDetail");
        } else {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }
        btn_get_a_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Quotation quotation = new Quotation();
                Bundle bundle = new Bundle();
                bundle.putInt("bike_id", bike_id);
                bundle.putString("bike_name", bike_name_tv.getText().toString());
                bundle.putString("cc", txt_bike_cc.getText().toString());
                quotation.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, quotation);
                fragmentTransaction.commit();
            }
        });

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);

        ((FirstActivity) getActivity()).setBackKeyFlag(true);
        ((FirstActivity) getActivity()).setWhichFragment(1);

        return rootView;
    }

    @Override
    public void processFinish(String output) {
        Log.i("Test", "output : " + output);

        TableRow row = new TableRow(context);
        row.setLayoutParams(new TableRow.LayoutParams(300,
                TableLayout.LayoutParams.WRAP_CONTENT));

        try {
            JSONObject object = new JSONObject(output);
            String message = object.getString("message");
            String auth_key = object.getString("auth_key");
            boolean status = object.getBoolean("status");

            global_bike_name = "";
            //if(message.equals("Success"))
            if (status == true) {
                JSONObject bikeDetails = object.getJSONObject("bikeDetails");
                JSONArray basic = bikeDetails.getJSONArray("basic");
                JSONArray images = bikeDetails.getJSONArray("images");
                JSONObject basicObject = basic.getJSONObject(0);
                String bike_code = basicObject.getString("bike_code");
                String bike_id = basicObject.getString("bike_id");
                String bike_name = basicObject.getString("bike_name");
                String bike_cc = basicObject.getString("bike_cc");
                String thumble_img = basicObject.getString("thumble_img");
                String video_url = basicObject.getString("video_url");


                if (bike_name != null) {
                    bike_name_tv.setText(bike_name);
                    global_bike_name = bike_name;
                }
                if (bike_cc != null) {
                    txt_bike_cc.setText(bike_cc);
                }
                if (!video_url.equals("")) {

                    Log.i("url", video_url);

                    webView.setVisibility(View.VISIBLE);

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setDomStorageEnabled(true);

                    webView.loadUrl(video_url);
                    webView.setWebChromeClient(new WebChromeClient() {
                    });
                    webView.getSettings().setPluginState(WebSettings.PluginState.ON);

                    /*WebSettings w = webView.getSettings();
                    w.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    w.setPluginState(WebSettings.PluginState.ON);*/
                }

                JSONObject specification = bikeDetails.getJSONObject("specification");
                JSONArray Electrical = specification.getJSONArray("Electrical");
                JSONArray Suspension = specification.getJSONArray("Suspension");
                JSONArray Tyre_Size = specification.getJSONArray("Tyre-Size");
                JSONArray Brake = specification.getJSONArray("Brake");
                JSONArray Dimensions = specification.getJSONArray("Dimensions");
                JSONArray Engine = specification.getJSONArray("Engine");

               /* View space1 = new View(this);
                space1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                //   v.setBackgroundColor(Color.rgb(51, 51, 51));
                tableLayout.addView(space1);*/

                /*Engine Started from here */

                TextView engineTextView = new TextView(context);
                engineTextView.setText("Engine");
                engineTextView.setTextColor(getResources().getColor(R.color.white));
                row.addView(engineTextView);
                row.setPadding(26, 0, 0, 10);
                row.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_header_bg));//"#939393"
                tableLayout.addView(row);


                View space = new View(context);
                space.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 15));
                //   v.setBackgroundColor(Color.rgb(51, 51, 51));
                tableLayout.addView(space);


                for (int i = 0; i < Engine.length(); i++) {
                    JSONObject EngineObject = Engine.getJSONObject(i);
                    String specification_title = EngineObject.getString("specification_title");
                    String specification_value = EngineObject.getString("specification_value");

                    TableRow row_engine = new TableRow(context);
                    row_engine.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TextView engine_Title_TextView = new TextView(context);
                    engine_Title_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 1.2f));
                    TextView txt_culon = new TextView(context);
                    txt_culon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT));
                    TextView engine_Value_TextView = new TextView(context);
                    engine_Value_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 0.2f));

                    engine_Title_TextView.setTextColor(getResources().getColor(R.color.line_grey));
                    engine_Value_TextView.setTextColor(getResources().getColor(R.color.black));
                    txt_culon.setTextColor(getResources().getColor(R.color.black));
                    if ((i % 2) == 0) {
                        engine_Title_TextView.setText(" " + specification_title);
                        txt_culon.setText(": ");
                        engine_Value_TextView.setText(specification_value);
                        engine_Title_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        txt_culon.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        engine_Value_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));
                        //row_engine.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"

                    } else {
                        engine_Title_TextView.setText(" " + specification_title);
                        txt_culon.setText(":");
                        engine_Value_TextView.setText(specification_value);
                        row_engine.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    row_engine.addView(engine_Title_TextView);
                    row_engine.addView(txt_culon);
                    row_engine.addView(engine_Value_TextView);
                    row_engine.setPadding(26, 5, 26, 5);

                   /* if((i%2)==0)
                    row_engine.setBackgroundColor(Color.parseColor("#d8d8d8"));*/
                    tableLayout.addView(row_engine, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    //tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    Log.i("Test", "specification_title" + " : " + specification_title);
                    Log.i("Test", "specification_value" + " : " + specification_value);
                }

                 /*Engine end from here */


                View space1 = new View(context);
                space1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                //   v.setBackgroundColor(Color.rgb(51, 51, 51));
                tableLayout.addView(space1);


                 /*Electrical Started from here */

                TableRow rowelctrical = new TableRow(context);
                rowelctrical.setLayoutParams(new TableRow.LayoutParams(300,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView electricalTextView = new TextView(context);
                electricalTextView.setText("Electrical");
                electricalTextView.setTextColor(getResources().getColor(R.color.white));
                rowelctrical.addView(electricalTextView);
                rowelctrical.setPadding(26, 0, 0, 10);
                rowelctrical.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_header_bg));
                tableLayout.addView(rowelctrical);


                View space2 = new View(context);
                space2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space2);


                for (int i = 0; i < Electrical.length(); i++) {
                    JSONObject ElectricalObject = Electrical.getJSONObject(i);
                    String specification_title = ElectricalObject.getString("specification_title");
                    String specification_value = ElectricalObject.getString("specification_value");


                    TableRow dynamic_row = new TableRow(context);
                    dynamic_row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT, 1.5f));

                    TextView title_TextView = new TextView(context);
                    title_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 1.2f));
                    TextView txt_culon = new TextView(context);
                    txt_culon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT));
                    TextView value_TextView = new TextView(context);
                    value_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 0.2f));

                    title_TextView.setTextColor(getResources().getColor(R.color.line_grey));
                    value_TextView.setTextColor(getResources().getColor(R.color.black));
                    txt_culon.setTextColor(getResources().getColor(R.color.black));
                    if ((i % 2) == 0) {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        title_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        txt_culon.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        value_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));
                        //row_engine.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    } else {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        dynamic_row.setBackgroundColor(getResources().getColor(R.color.white));
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    }

                    dynamic_row.setPadding(26, 5, 26, 5);

                   /* if((i%2)==0)
                        dynamic_row.setBackgroundColor(Color.parseColor("#d8d8d8"));*/
                    tableLayout.addView(dynamic_row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    Log.i("Test", "specification_title " + " : " + specification_title);
                    Log.i("Test", "specification_value" + " : " + specification_value);
                }
                 /*Electrical End from here */

                View space3 = new View(context);
                space3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space3);


                /* Suspension started from here */
                TableRow row_susp = new TableRow(context);
                row_susp.setLayoutParams(new TableRow.LayoutParams(300,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView susp_TextView = new TextView(context);
                susp_TextView.setText("Suspension");
                susp_TextView.setTextColor(getResources().getColor(R.color.white));
                row_susp.addView(susp_TextView);
                row_susp.setPadding(26, 0, 0, 10);
                row_susp.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_header_bg));
                tableLayout.addView(row_susp);

                View space13 = new View(context);
                space13.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space13);

                for (int i = 0; i < Suspension.length(); i++) {
                    JSONObject SuspensionObject = Suspension.getJSONObject(i);
                    String specification_title = SuspensionObject.getString("specification_title");
                    String specification_value = SuspensionObject.getString("specification_value");

                    TableRow dynamic_row = new TableRow(context);
                    dynamic_row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TextView title_TextView = new TextView(context);
                    title_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 1.2f));
                    TextView txt_culon = new TextView(context);
                    txt_culon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT));
                    TextView value_TextView = new TextView(context);
                    value_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 0.2f));
                    //  title_TextView.setSingleLine(false);
                    //  title_TextView.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

                    value_TextView.setSingleLine(false);
                    value_TextView.setEllipsize(TextUtils.TruncateAt.END);
                    // value_TextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
                  /*  value_TextView.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
                            android.widget.TableRow.LayoutParams.WRAP_CONTENT, 1.0f));*/
                    value_TextView.setMaxLines(3);
                    // value_TextView.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                    title_TextView.setTextColor(getResources().getColor(R.color.line_grey));
                    value_TextView.setTextColor(getResources().getColor(R.color.black));
                    txt_culon.setTextColor(getResources().getColor(R.color.black));
                    if ((i % 2) == 0) {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        title_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        txt_culon.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        value_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));
                        //row_engine.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    } else {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        dynamic_row.setBackgroundColor(getResources().getColor(R.color.white));
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    }
                    dynamic_row.setPadding(26, 5, 26, 5);

                /*    if((i%2)==0)
                        dynamic_row.setBackgroundColor(Color.parseColor("#d8d8d8"));*/
                    tableLayout.addView(dynamic_row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    Log.i("Test", "specification_title " + " : " + specification_title);
                    Log.i("Test", "specification_value" + " : " + specification_value);
                }

                 /* Suspension end from here */

                View space4 = new View(context);
                space4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space4);


                 /* Tyre started from here */

                TableRow row_tyre = new TableRow(context);
                row_tyre.setLayoutParams(new TableRow.LayoutParams(300,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView tyre_TextView = new TextView(context);
                tyre_TextView.setText("Tyre");
                tyre_TextView.setTextColor(getResources().getColor(R.color.white));
                row_tyre.addView(tyre_TextView);
                row_tyre.setPadding(26, 0, 0, 10);
                row_tyre.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_header_bg));
                tableLayout.addView(row_tyre);

                View space14 = new View(context);
                space14.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space14);

                for (int i = 0; i < Tyre_Size.length(); i++) {
                    JSONObject Tyre_SizeObject = Tyre_Size.getJSONObject(i);
                    String specification_title = Tyre_SizeObject.getString("specification_title");
                    String specification_value = Tyre_SizeObject.getString("specification_value");

                    TableRow dynamic_row = new TableRow(context);
                    dynamic_row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TextView title_TextView = new TextView(context);
                    title_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 1.2f));
                    TextView txt_culon = new TextView(context);
                    txt_culon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT));
                    TextView value_TextView = new TextView(context);
                    value_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 0.2f));

                    title_TextView.setTextColor(getResources().getColor(R.color.line_grey));
                    value_TextView.setTextColor(getResources().getColor(R.color.black));
                    txt_culon.setTextColor(getResources().getColor(R.color.black));
                    if ((i % 2) == 0) {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        title_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        txt_culon.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        value_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));
                        //row_engine.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    } else {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        dynamic_row.setBackgroundColor(getResources().getColor(R.color.white));
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    }

                    dynamic_row.setPadding(26, 5, 26, 5);

                   /* if((i%2)==0)
                        dynamic_row.setBackgroundColor(Color.parseColor("#d8d8d8"));*/
                    tableLayout.addView(dynamic_row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    Log.i("Test", "specification_title " + " : " + specification_title);
                    Log.i("Test", "specification_value" + " : " + specification_value);
                }

                 /* Type end from here */
                View space5 = new View(context);
                space5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space5);

                 /* Brake started from here */
                TableRow row_brake = new TableRow(context);
                row_brake.setLayoutParams(new TableRow.LayoutParams(300,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView brake_TextView = new TextView(context);
                brake_TextView.setText("Brake");
                brake_TextView.setTextColor(getResources().getColor(R.color.white));
                row_brake.addView(brake_TextView);
                row_brake.setPadding(26, 0, 0, 10);
                row_brake.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_header_bg));
                tableLayout.addView(row_brake);

                View space15 = new View(context);
                space15.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space15);


                for (int i = 0; i < Brake.length(); i++) {
                    JSONObject BrakeObject = Brake.getJSONObject(i);
                    String specification_title = BrakeObject.getString("specification_title");
                    String specification_value = BrakeObject.getString("specification_value");

                    TableRow dynamic_row = new TableRow(context);
                    dynamic_row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TextView title_TextView = new TextView(context);
                    title_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 1.2f));
                    TextView txt_culon = new TextView(context);
                    txt_culon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT));
                    TextView value_TextView = new TextView(context);
                    value_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 0.2f));

                    title_TextView.setTextColor(getResources().getColor(R.color.line_grey));
                    value_TextView.setTextColor(getResources().getColor(R.color.black));
                    txt_culon.setTextColor(getResources().getColor(R.color.black));
                    if ((i % 2) == 0) {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        title_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        txt_culon.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        value_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));
                        //row_engine.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    } else {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        dynamic_row.setBackgroundColor(getResources().getColor(R.color.white));
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    }

                    dynamic_row.setPadding(26, 5, 26, 5);

                   /* if((i%2)==0)
                        dynamic_row.setBackgroundColor(Color.parseColor("#d8d8d8"));*/
                    tableLayout.addView(dynamic_row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    Log.i("Test", "specification_title " + " : " + specification_title);
                    Log.i("Test", "specification_value" + " : " + specification_value);
                }

                 /* Brake end from here */

                View space6 = new View(context);
                space6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space6);

                 /* Dimensions started from here */

                TableRow row_dimension = new TableRow(context);
                row_dimension.setLayoutParams(new TableRow.LayoutParams(300,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView dimension_TextView = new TextView(context);
                dimension_TextView.setText("Dimensions");
                dimension_TextView.setTextColor(getResources().getColor(R.color.white));
                row_dimension.addView(dimension_TextView);
                row_dimension.setPadding(26, 0, 0, 10);
                row_dimension.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_header_bg));
                tableLayout.addView(row_dimension);

                View space16 = new View(context);
                space16.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space16);

                for (int i = 0; i < Dimensions.length(); i++) {
                    JSONObject DimensionsObject = Dimensions.getJSONObject(i);
                    String specification_title = DimensionsObject.getString("specification_title");
                    String specification_value = DimensionsObject.getString("specification_value");

                    TableRow dynamic_row = new TableRow(context);
                    dynamic_row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TextView title_TextView = new TextView(context);
                    title_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 1.2f));
                    TextView txt_culon = new TextView(context);
                    txt_culon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT));
                    TextView value_TextView = new TextView(context);
                    value_TextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.FILL_PARENT, 0.2f));

                    title_TextView.setTextColor(getResources().getColor(R.color.line_grey));
                    value_TextView.setTextColor(getResources().getColor(R.color.black));
                    txt_culon.setTextColor(getResources().getColor(R.color.black));
                    if ((i % 2) == 0) {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        title_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        txt_culon.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        value_TextView.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));
                        //row_engine.setBackgroundColor(getResources().getColor(R.color.bike_details_spec_row_bg));//"#d8d8d8"
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    } else {
                        title_TextView.setText(" " +specification_title);
                        txt_culon.setText(":");
                        value_TextView.setText(specification_value);
                        dynamic_row.setBackgroundColor(getResources().getColor(R.color.white));
                        dynamic_row.addView(title_TextView);
                        dynamic_row.addView(txt_culon);
                        dynamic_row.addView(value_TextView);
                    }

                    dynamic_row.setPadding(26, 5, 26, 5);

                    /*if((i%2)==0)
                        dynamic_row.setBackgroundColor(Color.parseColor("#d8d8d8"));*/
                    tableLayout.addView(dynamic_row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    Log.i("Test", "specification_title " + " : " + specification_title);
                    Log.i("Test", "specification_value" + " : " + specification_value);
                }

                 /* Dimensions end from here */
                View space7 = new View(context);
                space7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 15));
                tableLayout.addView(space7);


                NUM_PAGES = images.length();
                gallery_image = new String[NUM_PAGES];
                image_color = new String[NUM_PAGES];
                TextView[] pairs = new TextView[NUM_PAGES];


                for (int i = 0; i < images.length(); i++) {

                    JSONObject imagesDetails = images.getJSONObject(i);
                    gallery_image[i] = imagesDetails.getString("large_image_link");
                    // gallery_image[i]=g_image;
                    Log.i("Test large", gallery_image[i]);

                    image_color[i] = imagesDetails.getString("image_color");
                    pairs[i] = new TextView(context);
                    pairs[i].setTextSize(15);
                    lp.setMargins(10, 0, 0, 0);
                    //lp.setMarginStart(5);
                    pairs[i].setLayoutParams(lp);
                    pairs[i].setId(i);
                    //pairs[i].setText((i + 1) + ": something");
                    pairs[i].setText(getResources().getString(R.string.fa_motorcycle));
                    pairs[i].setTypeface(iconFont);
                    //pairs[i].setTextColor( Color.BLACK );

                    try {
                        pairs[i].setTextColor(Color.parseColor(image_color[i].trim()));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    linearLayoutBikeFone.addView(pairs[i]);

                }
                if (gallery_image != null) {
                    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                    pager.setAdapter(new ImageAdapter((context)));
                }

            } else {
                Toast.makeText(context, "Data Not Found !", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    private class ImageAdapter extends PagerAdapter {


        private String[] IMAGE_URLS = gallery_image;
        public ImageLoader imageLoader = null;

        private LayoutInflater inflater;
        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(null)
                    .showImageForEmptyUri(null)
                    .showImageOnFail(null).cacheInMemory(false)
                    .cacheOnDisk(false).considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            /*options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
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
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image_bike_details, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

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
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            view.addView(imageLayout, 0);

            imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialogWithImage(IMAGE_URLS[position], global_bike_name);
                }
            });


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

        private void getAlertDialogWithImage(String img_uri, String product_name) {


            //Toast.makeText(context,img_uri,Toast.LENGTH_LONG).show();
            try {
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
                //photo.setAdjustViewBounds(true);
                photo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ImageView view = (ImageView) v;
                        System.out.println("matrix=" + savedMatrix.toString());
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                savedMatrix.set(matrix);
                                startPoint.set(event.getX(), event.getY());
                                mode = DRAG;
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                oldDist = spacing(event);
                                if (oldDist > 10f) {
                                    savedMatrix.set(matrix);
                                    midPoint(midPoint, event);
                                    mode = ZOOM;
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_POINTER_UP:
                                mode = NONE;
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (mode == DRAG) {
                                    matrix.set(savedMatrix);
                                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                                } else if (mode == ZOOM) {
                                    float newDist = spacing(event);
                                    if (newDist > 10f) {
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

                    @SuppressLint("FloatMath")
                    private float spacing(MotionEvent event) {
                        float x = event.getX(0) - event.getX(1);
                        float y = event.getY(0) - event.getY(1);

                        //return FloatMath.sqrt(x * x + y * y);\
                        return (float) Math.sqrt(x * x + y * y);
                    }

                    private void midPoint(PointF point, MotionEvent event) {
                        float x = event.getX(0) + event.getX(1);
                        float y = event.getY(0) + event.getY(1);
                        point.set(x / 2, y / 2);
                    }
                });


                ImageLoader.getInstance().displayImage(img_uri, photo);

                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParamsL = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(layoutParamsL);
                //layout.setGravity(Gravity.CENTER_HORIZONTAL);
                int widthPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
                int heightPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPX, heightPX);

                //photo.setAdjustViewBounds(true);
                photo.setLayoutParams(layoutParams);
                layout.addView(photo);

                //LayoutInflater factory = LayoutInflater.from(context);
                //final View view = factory.inflate(layout.getId(),null);
                AlertDialog.Builder alert =
                        new AlertDialog.Builder(getActivity()).
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
