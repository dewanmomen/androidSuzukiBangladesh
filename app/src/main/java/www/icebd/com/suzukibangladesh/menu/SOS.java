package www.icebd.com.suzukibangladesh.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.Manifest;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.utilities.Constant;


public class SOS extends Fragment implements View.OnClickListener {

    Button dialer;
    Context context;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public static SOS newInstance() {
        SOS fragment = new SOS();
        return fragment;
    }

    public SOS() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sos, container,
                false);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("SOS");
        dialer = (Button) rootView.findViewById(R.id.dialer);
        dialer.setOnClickListener(this);

        ((FirstActivity) getActivity()).setBackKeyFlag(true);
        ((FirstActivity) getActivity()).setWhichFragment(0);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onClick(View v) {

        try {
            if (checkPermission()) {
                //Toast.makeText(context, "Permission already granted.", Toast.LENGTH_LONG).show();
                callSOS();

            } else {
                requestPermission();
            }
            //callSOS();
            // Here, thisActivity is the current activity
            /*if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED)
            {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CALL_PHONE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callSOS();
                        }
                    }, 100);

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            Constant.MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CALL_PHONE)) {

            Toast.makeText(context, "Call Phone permission allows us to call a user. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            //requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);

        } else {
            //ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void callSOS() {
        String number = "tel:" + context.getResources().getString(R.string.sos_value);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(number));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //callIntent.setPackage("com.android.phone");
        //callIntent.setPackage("com.android.server.telecom");
        this.startActivity(callIntent);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    callSOS();
                } else {
                    Toast.makeText(context, "You must granted phone call dialer permission", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
