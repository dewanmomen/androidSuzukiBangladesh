package www.icebd.com.suzukibangladesh.historyOfSuzuki;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.quiz.Quiz;
import www.icebd.com.suzukibangladesh.reg.ResetPassword;
import www.icebd.com.suzukibangladesh.reg.Signup;
import www.icebd.com.suzukibangladesh.request.RequestServices;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsMyCart;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;


public class HistoryOfSuzuki extends Fragment implements View.OnClickListener {
    WebView webView;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    Context context;
    CustomDialog customDialog;
    ProgressDialog progressDialog;

    private LinearLayout mlLayoutRequestError = null;
    private Handler mhErrorLayoutHide = null;

    private boolean mbErrorOccured = false;
    private boolean mbReloadPressed = false;

    public static HistoryOfSuzuki newInstance() {
        HistoryOfSuzuki fragment = new HistoryOfSuzuki();
        return fragment;
    }

    public HistoryOfSuzuki() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_of_suzuki, container,
                false);
        //setupUI(rootView.findViewById(R.id.parentLogin));
        context = getActivity().getApplicationContext();
        getActivity().setTitle("HISTORY OF SUZUKI");

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        ((Button) rootView.findViewById(R.id.btnRetry)).setOnClickListener(this);
        mlLayoutRequestError = (LinearLayout)rootView.findViewById(R.id.lLayoutRequestError);
        mhErrorLayoutHide = getErrorLayoutHideHandler();

        webView = (WebView) rootView.findViewById(R.id.webViewHistoryOfSuzuki);
        customDialog = new CustomDialog(getActivity());
        if(CheckNetworkConnection.isConnectedToInternet(context) == true)
        {
            webView.setWebViewClient(new MyWebViewClient());

            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(Constant.urlHistoryOfSuzuki);
            //webView.loadUrl("http://icebd.com/suzuki/admin/histor");
        }
        else
        {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);

        return rootView;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnRetry) {
            if (!mbErrorOccured) {
                return;
            }

            mbReloadPressed = true;
            webView.reload();
            mbErrorOccured = false;
        }
    }
    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //view.loadUrl(url);
            //return true;
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            progressDialog = ProgressDialog.show(getActivity(), null, null);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            progressDialog.dismiss();
            if (mbErrorOccured == false && mbReloadPressed) {
                hideErrorLayout();
                mbReloadPressed = false;
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {
            progressDialog.dismiss();
            mbErrorOccured = true;
            showErrorLayout();
            //super.onReceivedError(view, request, error);
        }
    }

    private void showErrorLayout() {
        mlLayoutRequestError.setVisibility(View.VISIBLE);
    }

    private void hideErrorLayout() {
        mhErrorLayoutHide.sendEmptyMessageDelayed(10000, 200);
    }

    private Handler getErrorLayoutHideHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mlLayoutRequestError.setVisibility(View.GONE);
                super.handleMessage(msg);
            }
        };
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
