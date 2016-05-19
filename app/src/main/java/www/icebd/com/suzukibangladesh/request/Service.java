package www.icebd.com.suzukibangladesh.request;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.icebd.com.suzukibangladesh.R;


public class Service extends Fragment {

    public static Service newInstance() {
        Service fragment = new Service();
        return fragment;
    }

    public Service() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service, container,
                false);


        return rootView;
    }
}
