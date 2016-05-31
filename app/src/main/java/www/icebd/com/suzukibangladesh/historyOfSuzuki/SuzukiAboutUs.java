package www.icebd.com.suzukibangladesh.historyOfSuzuki;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.utilities.Constant;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;


public class SuzukiAboutUs extends Fragment
{
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

    public static SuzukiAboutUs newInstance() {
        SuzukiAboutUs fragment = new SuzukiAboutUs();
        return fragment;
    }

    public SuzukiAboutUs() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suzuki_about_us, container,
                false);
        //setupUI(rootView.findViewById(R.id.parentLogin));
        context = getActivity().getApplicationContext();
        getActivity().setTitle("About Us");

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();

        /*((Button) rootView.findViewById(R.id.btnRetry)).setOnClickListener(this);
        mlLayoutRequestError = (LinearLayout)rootView.findViewById(R.id.lLayoutRequestError);
        mhErrorLayoutHide = getErrorLayoutHideHandler()*/;

        webView = (WebView) rootView.findViewById(R.id.webViewSuzukiAoutUs);
        customDialog = new CustomDialog(getActivity());
        if(CheckNetworkConnection.isConnectedToInternet(context) == true)
        {
            webView.setWebViewClient(new MyWebViewClient());

            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(Constant.urlAboutUs);
        }
        else
        {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }

        ((FirstActivity)getActivity()).setBackKeyFlag(true);
        ((FirstActivity)getActivity()).setWhichFragment(0);

        return rootView;
    }
    /*@Override
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
    }*/
    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
            //return super.shouldOverrideUrlLoading(view, url);
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
            /*if (mbErrorOccured == false && mbReloadPressed) {
                hideErrorLayout();
                mbReloadPressed = false;
            }*/
            super.onPageFinished(view, url);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {
            progressDialog.dismiss();
            //Toast.makeText(getActivity(), "Your Internet Connection May not be active, Please Try Again " , Toast.LENGTH_LONG).show();
            /*mbErrorOccured = true;
            showErrorLayout();*/
            super.onReceivedError(view, request, error);
            loadError();

        }

        private void loadError() {
            String html = "<html><body><table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
                    + "<tr>"
                    + "<td><div style=\"color:red;font-size:20px;font-weight:bold;\">Your device don't have internet connection, Please Connect with Internet and Try Again !</font></div></td>"
                    + "</tr>" + "</table><html><body>";
            System.out.println("html " + html);

            String base64 = android.util.Base64.encodeToString(html.getBytes(),
                    android.util.Base64.DEFAULT);
            webView.loadData(base64, "text/html; charset=utf-8", "base64");
            System.out.println("loaded html");
        }
    }
    /*private void showErrorLayout() {
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
    }*/
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
