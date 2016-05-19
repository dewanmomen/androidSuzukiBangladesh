package www.icebd.com.suzukibangladesh.json;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.R;

public class Json_reader extends Activity implements AsyncResponse {

    TextView auth_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_reader);

        auth_key = (TextView)findViewById(R.id.auth_key);

        HashMap<String, String> postData = new HashMap<String, String>();
      /*  postData.put("unique_device_id","123");
        postData.put("notification_key","4");
        postData.put("platform","2");*/

        //postData.put("auth_key","eebc982aeafbfd31c93914150a67b606");
        //postData.put("bike_id","27");

       /* PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData );
        //loginTask.execute("http://icebd.com/suzuki/suzukiApi/server/getAuthKey");
        loginTask.execute("http://icebd.com/suzuki/suzukiApi/Server/getBikeDetail");*/


    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(this,output,Toast.LENGTH_LONG).show();
        Log.w("output", output);
        auth_key.setText(output);

    }
}
