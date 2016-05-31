package www.icebd.com.suzukibangladesh.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.icebd.com.suzukibangladesh.R;


public class InviteFriends extends Fragment {

    Context context;

    public static InviteFriends newInstance() {
        InviteFriends fragment = new InviteFriends();
        return fragment;
    }

    public InviteFriends() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_friends, container,
                false);

        context = getActivity().getApplicationContext();

        MediaLink mediaLink = new MediaLink();
        String shareBody = "Welcome to Suzuki Bangladesh Official Mobile App\n" +
                "\n" +
                mediaLink.getPlay_store() +
                "\n" +
                mediaLink.getApp_store() +
                "\n" +
                mediaLink.getFb();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_using)));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((MainActivity) activity).onSectionAttached(1);
    }
}
