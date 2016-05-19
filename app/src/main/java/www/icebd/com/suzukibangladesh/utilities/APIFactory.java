package www.icebd.com.suzukibangladesh.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class APIFactory {

	static ArrayList<NameValuePair> nameValuePairs;

	public ArrayList<NameValuePair> getAuthKeyInfo(String identifier,String platform)
	{
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("identifier", identifier));
		nameValuePairs.add(new BasicNameValuePair("platform", platform));

		return nameValuePairs;
	}
	public ArrayList<NameValuePair> setNotificationKeyInfo(String unique_device_id,String notification_key,String auth, String platform)
	{
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("unique_device_id", unique_device_id));
		nameValuePairs.add(new BasicNameValuePair("notification_key", notification_key));
		nameValuePairs.add(new BasicNameValuePair("auth", auth));
		nameValuePairs.add(new BasicNameValuePair("platform", platform));

		return nameValuePairs;
	}
	public ArrayList<NameValuePair> getMapLocationInfo(String auth_key)
	{
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("auth_key", auth_key));

		return nameValuePairs;
	}
	public ArrayList<NameValuePair> getBikeListInfo(String auth_key)
	{
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("auth_key", auth_key));

		return nameValuePairs;
	}
	public ArrayList<NameValuePair> getQuizResultInfo(String auth_key, String user_id, String quiz_id, ArrayList<HashMap<String, String>> quiz_answer)
	{

		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("auth_key", auth_key));
		nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
		nameValuePairs.add(new BasicNameValuePair("quiz_id", quiz_id));
		for(int i=0; i<quiz_answer.size(); i++)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map = quiz_answer.get(i);
			//params.add(new BasicNameValuePair(“quiz_answer[0][quiz_id]”, “1”));
			nameValuePairs.add(new BasicNameValuePair("quiz_answer["+i+"][question_id]", map.get("question_id")));
			nameValuePairs.add(new BasicNameValuePair("quiz_answer["+i+"][answer_id]", map.get("answer_id")));
		}


		return nameValuePairs;
	}

	public ArrayList<NameValuePair> getSparePartsListInfo(String auth_key)
	{
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("auth_key", auth_key));

		return nameValuePairs;
	}
	public ArrayList<NameValuePair> getMediaInfo(String auth_key)
	{
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("auth_key", auth_key));

		return nameValuePairs;
	}







}//end of main class