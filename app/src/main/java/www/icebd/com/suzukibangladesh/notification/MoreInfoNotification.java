package www.icebd.com.suzukibangladesh.notification;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import www.icebd.com.suzukibangladesh.R;

// Called when the notification is clicked on in the task bar
public class MoreInfoNotification extends Activity {
    TextView rmnder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_notific);

        String value = getIntent().getStringExtra("value");
        Bundle body = getIntent().getBundleExtra("body");
        rmnder = (TextView) findViewById(R.id.textView);
        rmnder.setText(value +"\n" +body);

    }
}