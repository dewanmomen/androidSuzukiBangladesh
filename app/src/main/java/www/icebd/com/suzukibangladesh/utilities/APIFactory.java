package www.icebd.com.suzukibangladesh.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import www.icebd.com.suzukibangladesh.spare_parts.SparePartsListObject;

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
	public ArrayList<NameValuePair> getQuizResultInfo(String auth_key, String user_id, String quiz_id, ArrayList<HashMap<String, String>> quiz_answer) throws Exception
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
	public ArrayList<NameValuePair> goSparePartsPurchaseInfo(String auth_key, String delivery_charge,String delivery_address, String user_id, String total_item_price, String gross_amount, String transaction_id, int delivery_type, List<SparePartsListObject.SparePartsItem> listMyCartObj) throws Exception
	{

		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("auth_key", auth_key));
		nameValuePairs.add(new BasicNameValuePair("delivery_charge", delivery_charge));
		nameValuePairs.add(new BasicNameValuePair("delivery_address", delivery_address));
		nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
		nameValuePairs.add(new BasicNameValuePair("total_item_price", total_item_price));
		nameValuePairs.add(new BasicNameValuePair("gross_amount", gross_amount));
		nameValuePairs.add(new BasicNameValuePair("transaction_id", transaction_id));
		nameValuePairs.add(new BasicNameValuePair("delivery_type", String.valueOf(delivery_type)));
		for(int i=0; i<listMyCartObj.size(); i++)
		{
			//SparePartsListObject obj_sparePartsList = new SparePartsListObject();

			//HashMap<String, String> map = new HashMap<String, String>();
			SparePartsListObject.SparePartsItem obj_sparePartsItem =  listMyCartObj.get(i);
			//params.add(new BasicNameValuePair(“quiz_answer[0][quiz_id]”, “1”));
			nameValuePairs.add(new BasicNameValuePair("spare_parts_pay_item["+i+"][spare_parts_id]", obj_sparePartsItem.getSpare_parts_id() ));
			nameValuePairs.add(new BasicNameValuePair("spare_parts_pay_item["+i+"][quantity]", String.valueOf(obj_sparePartsItem.getaInteger())));
			nameValuePairs.add(new BasicNameValuePair("spare_parts_pay_item["+i+"][price]", obj_sparePartsItem.getSpare_parts_price()));
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