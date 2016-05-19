package www.icebd.com.suzukibangladesh.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.app.CheckNetworkConnection;
import www.icebd.com.suzukibangladesh.bikelist.BikeList;
import www.icebd.com.suzukibangladesh.bikelist.BikeListSwipeListAdapter;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;
import www.icebd.com.suzukibangladesh.utilities.APIFactory;
import www.icebd.com.suzukibangladesh.utilities.ConnectionManager;
import www.icebd.com.suzukibangladesh.utilities.CustomDialog;
import www.icebd.com.suzukibangladesh.utilities.FontManager;
import www.icebd.com.suzukibangladesh.utilities.JsonParser;


public class Quiz extends Fragment implements AsyncResponse, View.OnClickListener {
    private static final String TAG ="Suzuki Bangladesh" ;


    public static int questionsNo=0;
    ArrayList<HashMap<String, String>> arrList;
    List<Object> sections = new ArrayList <Object>();

    RadioButton rda,rdb,rdc,rdd;
    RadioGroup rdaGroupquiz;
    TextView question_count,txtQuestion, txtTitle,tv,timer_icon;
    Button btnNext;
    private Thread thread;
    boolean isRunning =false;
    boolean isQuizFinish =false;
    CountDownTimer countDownTimer = null;

    Typeface iconFont;

    private static int index=0,callingIndex=0;
    View rootView;
    LayoutInflater inflater_temp;
    ViewGroup container_temp;

    JSONObject jsonObj ;
    JSONObject quizes ;
    String description ;
    String title ;
    String end ;
    String start;
    String auth_key,quizTitle,quizId;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    String question[]=new String[20] ,answerId[]=new String[20],questionId[]=new String[20],op1[]=new String[20],op2[]=new String[20],op3[]=new String[20],op4[]=new String[20],opId1[]=new String[20],opId2[]=new String[20],opId3[]=new String[20],opId4[]=new String[20];

    APIFactory apiFactory;
    CustomDialog customDialog;
    ProgressDialog progressDialog;
    QizzesResultAsyncTask qizzesResultAsyncTask = null;

    Context context;

    public static Quiz newInstance() {
        Quiz fragment = new Quiz();
        return fragment;
    }

    public Quiz() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_quiz, container,
                false);

        context = getActivity().getApplicationContext();
        getActivity().setTitle("QUIZZES");
        container_temp=container;
        inflater_temp=inflater;

        iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

        question_count=(TextView)rootView.findViewById(R.id.question_count);
        question_count.setVisibility(View.VISIBLE);
        timer_icon = (TextView)rootView.findViewById(R.id.timer_icon);
        txtQuestion=(TextView)rootView.findViewById(R.id.textView1);
        txtTitle=(TextView)rootView.findViewById(R.id.textView);

        tv=(TextView)rootView.findViewById(R.id.tv);
        rda=(RadioButton)rootView.findViewById(R.id.radio0);
        rdb=(RadioButton)rootView.findViewById(R.id.radio1);
        rdc=(RadioButton)rootView.findViewById(R.id.radio2);
        rdd=(RadioButton)rootView.findViewById(R.id.radio3);
        btnNext=(Button)rootView.findViewById(R.id.button1);
        btnNext.setVisibility(View.VISIBLE);
        rdaGroupquiz = (RadioGroup)rootView.findViewById(R.id.radioGroup1);

        pref = getActivity().getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", getActivity().MODE_PRIVATE);
        editor = pref.edit();
        if(pref.getString("is_login","0").equals("1"))
        {
            //do nothing
        }
        else {
            Toast.makeText(getActivity(),"Please Login",Toast.LENGTH_LONG).show();

            ((FirstActivity)getActivity()).selectItem(11);
        }


        auth_key= pref.getString("auth_key","empty");
        //String user_id = pref.getString("user_id","0");
        customDialog = new CustomDialog(context);
        if(CheckNetworkConnection.isConnectionAvailable(context) == true)
        {
            if (!auth_key.equals("empty"))
            {
                HashMap<String, String> postData = new HashMap<String, String>();

                postData.put("auth_key", auth_key);
                //postData.put("user_id", user_id);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData);
                loginTask.execute(ConnectionManager.SERVER_URL + "getquizDetail");

            }
        }
        else
        {
            customDialog.alertDialog("ERROR", getString(R.string.error_no_internet));
        }
        btnNext.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void processFinish(String output)
    {
        Log.i("Test","quiz: "+output);
        try
        {
            JSONObject object = new JSONObject(output);
            String message =object.getString("message");
            String status_code =object.getString("status_code");
            boolean status = object.getBoolean("status");

            if(status == true)
            {
                JSONObject quizes =object.getJSONObject("quizes");
                String title=quizes.getString("title");
                quizId = quizes.getString("id");
                quizTitle=title;
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
                    opId1[i] = options_details.getString("option_id");
                    Log.i("Test","option1 : "+ op1[i]);

                    JSONObject options_details1 = options.getJSONObject(1);
                    op2[i] =options_details1.getString("option");
                    opId2[i] = options_details1.getString("option_id");
                    Log.i("Test","option2 : "+ op2[i]);

                    JSONObject options_details2 = options.getJSONObject(2);
                    op3[i] =options_details2.getString("option");
                    opId3[i] = options_details2.getString("option_id");
                    Log.i("Test","option3 : "+ op3[i]);

                    JSONObject options_details3 = options.getJSONObject(3);
                    op4[i] =options_details3.getString("option");
                    opId4[i] = options_details3.getString("option_id");
                    Log.i("Test","option4 : "+ op4[i]);

                }
            }
            else
            {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
            /*else if(status_code.equals("109"))
            {
                rootView = inflater_temp.inflate(R.layout.fragment_quiz, container_temp,
                        false);
            }*/
        } catch (JSONException e) {
            try {
                JSONObject answer = new JSONObject(output);
                Log.i("Test","Quiz Answer"+output);
                Toast.makeText(context, "Quizz Data not Found please Try again", Toast.LENGTH_LONG).show();
                question_count.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);

               // String
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        showQuizes(callingIndex);

    }

    void showQuizes(int indexNow)
    {

        //setContentView(R.layout.fragment_quiz);


        if (questionsNo > indexNow) {

            question_count.setText("Question "+(indexNow+1)+" of "+questionsNo);
            txtQuestion.setText(question[indexNow]);
            rda.setText(op1[indexNow]);
            rdb.setText(op2[indexNow]);
            rdc.setText(op3[indexNow]);
            rdd.setText(op4[indexNow]);
            timer_icon.setText(getResources().getString(R.string.fa_timer_icon));
            timer_icon.setTypeface(iconFont);
            reverseTimer(30);
        }
        else {
            question_count.setText("Quiz Completed");
            txtTitle.setText("");
            txtQuestion.setText("");
            rda.setVisibility(View.INVISIBLE);
            rdb.setVisibility(View.INVISIBLE);
            rdc.setVisibility(View.INVISIBLE);
            rdd.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
            timer_icon.setVisibility(View.INVISIBLE);
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
                    tv.setText(String.format("%02d", minutes)
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

    /*public void nextBtnClicked(View view) {


    }*/

    @Override
    public void onClick(View v) {
      //  answerId[callingIndex];
        if(isQuizFinish)
        {
            if(quizId != null)
            {
                HashMap<String, String> postData = new HashMap<String, String>();
                String user_id = pref.getString("user_id", "0");
                postData.put("auth_key", auth_key);
                Log.i("auth_key", auth_key);
                postData.put("user_id", user_id);
                Log.i("user_id", user_id);
                postData.put("quiz_id", quizId);
                Log.i("quiz_id", quizId);
                String quiz_answer = "[";
                ArrayList<HashMap<String, String>> qa = new ArrayList();

                for (int i = 0; i < questionsNo; i++)
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                /*quiz_answer =quiz_answer + "{\'question_id\':\'"+questionId[i]+"\',"
                        +"\'answer_id\':\'"+answerId[i]+"\'},";*/
                    map.put("question_id", questionId[i]);
                    map.put("answer_id", answerId[i]);
                    qa.add(map);
                    quiz_answer = quiz_answer + "{\'question_id\':\'" + questionId[i] + "\',"
                            + "\'answer_id\':\'" + answerId[i] + "\'},";

                    if (i == questionsNo - 1)
                    {
                        quiz_answer = quiz_answer.substring(0, quiz_answer.length() - 1);
                    }
                /*HashMap<String, String> map1 = new HashMap();
                map1.put("question_id",questionId[i]);
                map1.put("answer_id",answerId[i]);

                qa.add(map1);
*/
                }
                quiz_answer = quiz_answer + "]";
                Log.i("quiz_answer", quiz_answer);
                postData.put("quiz_answer", quiz_answer);
                //postData.put("quiz_answer",qa);

                //PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
                //loginTask.execute(ConnectionManager.SERVER_URL+"quizResult");
                System.out.println("auth key :" + auth_key);
                System.out.println("user_id :" + user_id);
                System.out.println("quizId :" + quizId);
                System.out.println("quiz_answer :" + quiz_answer);
                apiFactory = new APIFactory();
                qizzesResultAsyncTask = new QizzesResultAsyncTask(auth_key, user_id, quizId, qa);
                qizzesResultAsyncTask.execute((Void) null);
            }
        }
        else
        {
            if (rda.isChecked() == false && rdb.isChecked() == false && rdc.isChecked() == false && rdd.isChecked() == false)
            {
                Toast.makeText(context, "Please Choose Multiple Choice Answer", Toast.LENGTH_LONG).show();
            }
            else
            {
                //Toast.makeText(context, "radio click", Toast.LENGTH_LONG).show();
                rda.setChecked(false);
                rdb.setChecked(false);
                rdc.setChecked(false);
                rdd.setChecked(false);
                rdaGroupquiz.clearCheck();
                int selectId = rdaGroupquiz.getCheckedRadioButtonId();
                switch (selectId)
                {

                    case R.id.radio0:
                        answerId[callingIndex] = opId1[callingIndex];
                        break;
                    case R.id.radio1:
                        answerId[callingIndex] = opId2[callingIndex];
                        break;
                    case R.id.radio2:
                        answerId[callingIndex] = opId3[callingIndex];
                        break;
                    case R.id.radio3:
                        answerId[callingIndex] = opId4[callingIndex];
                        break;
                    default:
                        break;
                }
                showQuizes(++callingIndex);
            }
        }
    }


    public class QizzesResultAsyncTask extends AsyncTask<Void, Void, String>
    {
        private String RESULT = "OK";
        private ArrayList<NameValuePair> returnJsonData;
        private ArrayList<NameValuePair> nvp2=null;
        private String device_type = "1";
        private String device_token = "device_token";
        private String udid = "udid";
        private InputStream response;
        private JsonParser jsonParser;

        private String methodName = "";
        private String GCMkey = "";
        private String DeviceID = "udid";
        private String auth_key, user_id, quiz_id;
        ArrayList<HashMap<String, String>> quiz_answer;

        //UserLoginTask(String email, String password)
        QizzesResultAsyncTask(String auth_key,String user_id,String quiz_id,ArrayList<HashMap<String, String>> quiz_answer)
        {
            this.auth_key = auth_key;
            this.user_id = user_id;
            this.quiz_id = quiz_id;
            this.quiz_answer = quiz_answer;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(getActivity(), null, null);
        }
        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                if (ConnectionManager.hasInternetConnection())
                {
                    //auth_key = "5384145a9afea0b20f740ed835c0b9bd";
                    //user_id="11";
                    //quiz_id = "19";
                    //quiz_answer = "[{'question_id':'27','answer_id':'83'},{'question_id':'28','answer_id':'87'},{'question_id':'29','answer_id':'91'},{'question_id':'30','answer_id':'95'}]";
                    nvp2 = apiFactory.getQuizResultInfo(auth_key,user_id,quiz_id,quiz_answer);
                    methodName = "quizResult";
                    response = ConnectionManager.getResponseFromServer(methodName, nvp2);
                    jsonParser = new JsonParser();

                    System.out.println("server response : "+response);
                    returnJsonData = jsonParser.parseAPIgetQuizResultInfo(response);
                    System.out.println("return data : " + returnJsonData);

                }
                else
                {
                    RESULT = getString(R.string.error_no_internet);
                    return RESULT;
                }
                return RESULT;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Log.e("APITask:", ex.getMessage());
                RESULT = getString(R.string.error_sever_connection);
                return RESULT;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            progressDialog.dismiss();

            if(RESULT.equalsIgnoreCase("OK"))
            {
                try
                {
                    //finish();

                    if (returnJsonData.size() > 0 && returnJsonData != null && returnJsonData.get(0).getValue().equals("true") == true )
                    {
                        //preferenceUtil.setPINstatus(1);
                        Toast.makeText(getActivity(), returnJsonData.get(1).getValue().toString(), Toast.LENGTH_SHORT).show();
                        ((FirstActivity)getActivity()).selectItem(1);
                    } else
                    {
                        System.out.println("data return : " + returnJsonData);
                        Toast.makeText(getActivity(), returnJsonData.get(1).getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    Log.e("APITask data error :", ex.getMessage());
                }
            }
            else {

                customDialog.alertDialog("ERROR", result);
            }
        }
        @Override
        protected void onCancelled()
        {
            qizzesResultAsyncTask = null;
            progressDialog.dismiss();
        }
    }
}
