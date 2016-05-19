package www.icebd.com.suzukibangladesh.reg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.icebd.com.suzukibangladesh.R;


public class AfterResetPassword extends Fragment {

    public static AfterResetPassword newInstance() {
        AfterResetPassword fragment = new AfterResetPassword();
        return fragment;
    }

    public AfterResetPassword() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reset_password_after, container,
                false);
        return rootView;
    }
}
