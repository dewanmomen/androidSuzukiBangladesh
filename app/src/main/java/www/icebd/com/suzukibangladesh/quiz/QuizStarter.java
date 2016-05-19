package www.icebd.com.suzukibangladesh.quiz;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;

public class QuizStarter extends Activity implements AsyncResponse {

    private static final String TAG ="Suzuki Bangladesh" ;


    public static int questionsNo=0;
    ArrayList<HashMap<String, String>> arrList;
    List<Object> sections = new ArrayList <Object>();

    RadioButton rda,rdb,rdc,rdd;
    TextView txtQuestion, txtTitle,tv;
    Button btnNext;
    private Thread thread;
    boolean isRunning =false;
    boolean isQuizFinish =false;
    CountDownTimer countDownTimer = null;

    private static int index=0,callingIndex=0;

    JSONObject jsonObj ;
    JSONObject quizes ;
    String description ;
    String title ;
    String end ;
    String start;
    String question[]=new String[20] ,questionId[]=new String[20],op1[]=new String[20],op2[]=new String[20],op3[]=new String[20],op4[]=new String[20],opId1[]=new String[20],opId2[]=new String[20],opId3[]=new String[20],opId4[]=new String[20];

    Context context;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_quiz);

        context = getApplicationContext();
        pref = context.getSharedPreferences("SuzukiBangladeshPref", context.MODE_PRIVATE);
        editor = pref.edit();

        txtQuestion=(TextView)findViewById(R.id.textView1);
        txtTitle=(TextView)findViewById(R.id.textView);

        tv=(TextView)findViewById(R.id.tv);
        rda=(RadioButton)findViewById(R.id.radio0);
        rdb=(RadioButton)findViewById(R.id.radio1);
        rdc=(RadioButton)findViewById(R.id.radio2);
        rdd=(RadioButton)findViewById(R.id.radio3);
        btnNext=(Button)findViewById(R.id.button1);


            HashMap<String, String> postData = new HashMap<String, String>();
          /*  postData.put("mobile", "android");
            postData.put("blood_group",blood_group);
            postData.put("longitude",Double.toString(longitude));
            postData.put("latitude",Double.toString(latitude));*/
        String auth_key = pref.getString("auth_key",null);
        postData.put("auth_key",auth_key);
        postData.put("auth_key","3c227bbba98cd9360006d095558d09a9");

          /*PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
            loginTask.execute(ConnectionManager.SERVER_URL+"getquizDetail");*/

        //showQuizes();


    }


    @Override
    public void processFinish(String output) {


        try {
            JSONObject object = new JSONObject(output);
            String message =object.getString("message");

            if(message.equals("Success"))
            {
                JSONObject quizes =object.getJSONObject("quizes");
                String title=quizes.getString("title");
                Log.i("Test","title :"+title);

                JSONArray questions =quizes.getJSONArray("questions");
                txtTitle.setText(title);

                questionsNo= questions.length();

                for (int i = 0; i <questions.length() ; i++) {

                    HashMap<String, String> map = new HashMap();
                    JSONObject question_details=questions.getJSONObject(i);
                     question[i] = question_details.getString("question");
                     questionId[i] = question_details.getString("question_id");

                   // map.put("question",question);
                   // map.put("question_id",question_id);

                    Log.i("Test","question :"+question[i]);

                    JSONArray options = question_details.getJSONArray("options");
                    // for (int j = 0; j <options.length() ; j++) {
                    JSONObject options_details = options.getJSONObject(0);
                     op1[i] =options_details.getString("option");
                    Log.i("Test","option1 : "+ op1[i]);

                    JSONObject options_details1 = options.getJSONObject(1);
                    op2[i] =options_details1.getString("option");
                    Log.i("Test","option2 : "+ op2[i]);

                    JSONObject options_details2 = options.getJSONObject(2);
                    op3[i] =options_details2.getString("option");
                    Log.i("Test","option3 : "+ op3[i]);

                    JSONObject options_details3 = options.getJSONObject(3);
                    op4[i] =options_details3.getString("option");
                    Log.i("Test","option4 : "+ op4[i]);





                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        showQuizes(callingIndex);

    }

    void showQuizes(int indexNow)
    {

        //setContentView(R.layout.fragment_quiz);


        if (questionsNo>indexNow) {

            txtQuestion.setText(question[indexNow]);
            rda.setText(op1[indexNow]);
            rdb.setText(op2[indexNow]);
            rdc.setText(op3[indexNow]);
            rdd.setText(op4[indexNow]);

            reverseTimer(30);
        }
        else {
            txtQuestion.setText("Quiz Completed");
            rda.setVisibility(View.INVISIBLE);
            rdb.setVisibility(View.INVISIBLE);
            rdc.setVisibility(View.INVISIBLE);
            rdd.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
            btnNext.setText("Finish");
            isQuizFinish=true;

        }


    }

    public void reverseTimer(int Seconds){


        if (!isRunning) {
            isRunning=true;
            countDownTimer = new CountDownTimer(Seconds * 1000 + 1000, 1000) {



                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    tv.setText("TIME : " + String.format("%02d", minutes)
                            + ":" + String.format("%02d", seconds));
                }

                public void onFinish() {
                    tv.setText("Completed");
                    showQuizes(++callingIndex);
                }
            }.start();
        }
        else {
            countDownTimer.cancel();
            countDownTimer.start();

        }


    }

    public void nextBtnClicked(View view) {

        if(isQuizFinish)
        {
            finish();
           // Toast.makeText(QuizStarter.this,"You have finished quiz successfully");
        }
        showQuizes(++callingIndex);
    }
}
