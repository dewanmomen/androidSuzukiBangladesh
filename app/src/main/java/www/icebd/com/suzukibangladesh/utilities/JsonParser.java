package www.icebd.com.suzukibangladesh.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import android.util.Log;

import www.icebd.com.suzukibangladesh.bikelist.BikeList;
import www.icebd.com.suzukibangladesh.maps.MapsLocationObject;
import www.icebd.com.suzukibangladesh.menu.MediaLink;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsListObject;

public class JsonParser 
{
	//JSONParser jsonParser = new JSONParser();
	JSONObject jsonResponse = null;
	StringBuilder builder = null;
	InputStream is = null;
	JSONArray jarray = null;
	String jsonData = "";
	BufferedReader reader=null;
	
	private ArrayList<NameValuePair> jsonArrayList;
	public static MediaLink mediaLink;

	public ArrayList<NameValuePair> parseAPIgetAuthKeyInfo(InputStream json) throws Exception
	{
		jsonArrayList = new ArrayList<NameValuePair>();

		try
		{
			reader = new BufferedReader(new InputStreamReader(json, "iso-8859-1"), 8);
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				System.out.println("read line : "+line);
				builder.append(line + "\n");
			}
			json.close();

			jsonData = builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		try
		{
			jsonResponse = new JSONObject(jsonData);

			boolean status = jsonResponse.getBoolean("status");
			jsonArrayList.add(new BasicNameValuePair("status", String.valueOf(status)));

			String message = jsonResponse.getString("message");
			jsonArrayList.add(new BasicNameValuePair("message", message));
			int status_code = jsonResponse.getInt("status_code");
			jsonArrayList.add(new BasicNameValuePair("status_code", String.valueOf(status_code)));
			if(status == false)
			{
				jsonArrayList = null;
			}
			else
			{
				String auth_key = jsonResponse.getString("auth_key");
				jsonArrayList.add(new BasicNameValuePair("auth_key", auth_key));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			//checking...
		}
		// return JSON String
		return jsonArrayList;
	}
	public ArrayList<NameValuePair> parseAPIsetNotificationKeyInfo(InputStream json) throws Exception
	{
		jsonArrayList = new ArrayList<NameValuePair>();

		try
		{
			reader = new BufferedReader(new InputStreamReader(json, "iso-8859-1"), 8);
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				System.out.println("read line : "+line);
				builder.append(line + "\n");
			}
			json.close();

			jsonData = builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		try
		{
			jsonResponse = new JSONObject(jsonData);

			boolean status = jsonResponse.getBoolean("status");
			jsonArrayList.add(new BasicNameValuePair("status", String.valueOf(status)));

			String message = jsonResponse.getString("message");
			jsonArrayList.add(new BasicNameValuePair("message", message));
			int status_code = jsonResponse.getInt("status_code");
			jsonArrayList.add(new BasicNameValuePair("status_code", String.valueOf(status_code)));
			if(status == false)
			{
				jsonArrayList = null;
			}
			else
			{
				String auth_key = jsonResponse.getString("auth_key");
				jsonArrayList.add(new BasicNameValuePair("auth_key", auth_key));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			//checking...
		}
		// return JSON String
		return jsonArrayList;
	}
	public ArrayList<NameValuePair> parseAPIgetQuizResultInfo(InputStream json) throws Exception
	{
		jsonArrayList = new ArrayList<NameValuePair>();

		try
		{
			reader = new BufferedReader(new InputStreamReader(json, "iso-8859-1"), 8);
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				System.out.println("read line : "+line);
				builder.append(line + "\n");
			}
			json.close();

			jsonData = builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		try
		{
			jsonResponse = new JSONObject(jsonData);

			boolean status = jsonResponse.getBoolean("status");
			jsonArrayList.add(new BasicNameValuePair("status", String.valueOf(status)));

			String message = jsonResponse.getString("message");
			jsonArrayList.add(new BasicNameValuePair("message", message));
			int status_code = jsonResponse.getInt("status_code");
			jsonArrayList.add(new BasicNameValuePair("status_code", String.valueOf(status_code)));
			if(status == false)
			{
				jsonArrayList = null;
			}
			/*else
			{
				String auth_key = jsonResponse.getString("auth_key");
				jsonArrayList.add(new BasicNameValuePair("auth_key", auth_key));
			}*/
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			//checking...
		}
		// return JSON String
		return jsonArrayList;
	}

	public ArrayList<BikeList> parseAPIgetBikeListInfo(InputStream json) throws Exception
	{
		ArrayList<BikeList> arrBikeList = new ArrayList<BikeList>();
		ArrayList<BikeList.BikeItem> arrBikeItem = new ArrayList<BikeList.BikeItem>();
		String oneObjectsItem_status = null;

		String bike_code;
		String bike_id;
		String bike_name;
		String bike_cc;
		String bike_mileage;
		String thumble_img;

		try
		{
			reader = new BufferedReader(new InputStreamReader(json, "iso-8859-1"), 8);
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				//System.out.println("read line : "+line);
				builder.append(line + "\n");
			}
			json.close();

			jsonData = builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try
		{
			jsonResponse = new JSONObject(jsonData);
			BikeList obj_bikeList = new BikeList();

			boolean status = jsonResponse.getBoolean("status");
			obj_bikeList.setStatus(status);
			String message = jsonResponse.getString("message");
			obj_bikeList.setMessage(message);
			int status_code = jsonResponse.getInt("status_code");
			obj_bikeList.setStatus_code(status_code);

			if(status == false)
			{
				arrBikeList = null;
			}
			else
			{
				jarray = jsonResponse.getJSONArray("bikeList");

				for (int i = 0; i < jarray.length(); i++)
				{

					jsonResponse = jarray.getJSONObject(i);
					BikeList.BikeItem obj_bikeItem = obj_bikeList.new BikeItem();

					bike_code = jsonResponse.getString("bike_code");
					obj_bikeItem.setBike_code(bike_code);
					bike_id = jsonResponse.getString("bike_id");
					obj_bikeItem.setBike_id(bike_id);
					bike_name = jsonResponse.getString("bike_name");
					obj_bikeItem.setBike_name(bike_name);
					bike_cc = jsonResponse.getString("bike_cc");
					obj_bikeItem.setBike_cc(bike_cc);
					bike_mileage = jsonResponse.getString("bike_mileage");
					obj_bikeItem.setBike_mileage(bike_mileage);
					thumble_img = jsonResponse.getString("thumble_img");
					obj_bikeItem.setThumble_img(thumble_img);

					arrBikeItem.add(obj_bikeItem);
				}
				obj_bikeList.setBikeItemsList(arrBikeItem);// set bike items to array list
				arrBikeList.add(obj_bikeList);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		// return JSON String
		return arrBikeList;
	}
	public List<MapsLocationObject.Locations> parseAPIMapLocationInfo(InputStream json) throws Exception
	{
		List<MapsLocationObject.Locations> mapsLocationObjectList = new ArrayList<MapsLocationObject.Locations>();
		MapsLocationObject mapsLocationObject = new MapsLocationObject();
		String oneObjectsItem_status = null;


		try
		{
			reader = new BufferedReader(new InputStreamReader(json, "iso-8859-1"), 8);
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				System.out.println("read line : "+line);
				builder.append(line + "\n");
			}
			json.close();

			jsonData = builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try
		{
			jsonResponse = new JSONObject(jsonData);

			boolean status = jsonResponse.getBoolean("status");
			mapsLocationObject.setStatus(status);
			String message = jsonResponse.getString("message");
			mapsLocationObject.setMessage(message);
			int status_code = jsonResponse.getInt("status_code");
			mapsLocationObject.setStatus_code(status_code);
			if(status == false)
			{
				mapsLocationObjectList = null;
			}
			else
			{
				jarray = jsonResponse.getJSONArray("location");

				for (int i = 0; i < jarray.length(); i++)
				{
					jsonResponse = jarray.getJSONObject(i);
					MapsLocationObject.Locations obj_maps_location = mapsLocationObject.new Locations();

					String location_id = jsonResponse.getString("location_id");
					obj_maps_location.setLocation_id(location_id);
					String location_type = jsonResponse.getString("location_type");
					obj_maps_location.setLocation_type(location_type);
					String location_name = jsonResponse.getString("location_name").trim();
					obj_maps_location.setLocation_name(location_name);
					String location_address = jsonResponse.getString("location_address");
					obj_maps_location.setLocation_address(location_address);
					String location_contact_person_name = jsonResponse.getString("location_contact_person_name");
					obj_maps_location.setLocation_contact_person_name(location_contact_person_name);
					String location_contact_person_email = jsonResponse.getString("location_contact_person_email");
					obj_maps_location.setLocation_contact_person_email(location_contact_person_email);
					String location_contact_person_phone = jsonResponse.getString("location_contact_person_phone");
					obj_maps_location.setLocation_contact_person_phone(location_contact_person_phone);
					String district = jsonResponse.getString("district");
					obj_maps_location.setDistrict(district.toLowerCase());

					Log.d("TAG", "Location Address: " + location_address);

					mapsLocationObjectList.add(obj_maps_location);
				}
				//obj_sparePartsList.setSparePartsItemsList(arrSparePartsItem);// set spare parts items to array list
				//arrSparePartsList.add(obj_sparePartsList);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			//checking...
		}
		// return JSON String
		return mapsLocationObjectList;
	}
	public ArrayList<SparePartsListObject> parseAPIgetSparePartsListInfo(InputStream json) throws Exception
	{
		ArrayList<SparePartsListObject> arrSparePartsList = new ArrayList<SparePartsListObject>();
		ArrayList<SparePartsListObject.SparePartsItem> arrSparePartsItem = new ArrayList<SparePartsListObject.SparePartsItem>();
		String oneObjectsItem_status = null;

		String spare_parts_id;
		String spare_parts_name;
		String spare_parts_price;
		String spare_parts_code;
		String parts_type;
		String bike_name;
		String thumble_img;

		try
		{
			reader = new BufferedReader(new InputStreamReader(json, "iso-8859-1"), 8);
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				//System.out.println("read line : "+line);
				builder.append(line + "\n");
			}
			json.close();

			jsonData = builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try
		{
			jsonResponse = new JSONObject(jsonData);
			SparePartsListObject obj_sparePartsList = new SparePartsListObject();
			boolean status = jsonResponse.getBoolean("status");
			obj_sparePartsList.setStatus(status);
			String message = jsonResponse.getString("message");
			obj_sparePartsList.setMessage(message);
			int status_code = jsonResponse.getInt("status_code");
			obj_sparePartsList.setStatus_code(status_code);
			if(status == false)
			{
				arrSparePartsList = null;
			}
			else
			{
				jarray = jsonResponse.getJSONArray("spare");

				for (int i = 0; i < jarray.length(); i++)
				{
					jsonResponse = jarray.getJSONObject(i);
					SparePartsListObject.SparePartsItem obj_sparePartsItem = obj_sparePartsList.new SparePartsItem();

					spare_parts_id = jsonResponse.getString("spare_parts_id");
					obj_sparePartsItem.setSpare_parts_id(spare_parts_id);
					spare_parts_name = jsonResponse.getString("spare_parts_name");
					obj_sparePartsItem.setSpare_parts_name(spare_parts_name);

					spare_parts_price = jsonResponse.getString("spare_parts_price");
					obj_sparePartsItem.setSpare_parts_price(spare_parts_price);
					spare_parts_code = jsonResponse.getString("spare_parts_code");
					obj_sparePartsItem.setSpare_parts_code(spare_parts_code);
					parts_type = jsonResponse.getString("parts_type");
					obj_sparePartsItem.setParts_type(parts_type);
					bike_name = jsonResponse.getString("bike_name");
					obj_sparePartsItem.setBike_name(bike_name);
					thumble_img = jsonResponse.getString("large_image_name");
					obj_sparePartsItem.setThumble_img(thumble_img);

					arrSparePartsItem.add(obj_sparePartsItem);
				}
				obj_sparePartsList.setSparePartsItemsList(arrSparePartsItem);// set spare parts items to array list
				arrSparePartsList.add(obj_sparePartsList);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			//checking...
		}
		// return JSON String
		return arrSparePartsList;
	}
	public ArrayList<NameValuePair> parseAPIgetMediaInfo(InputStream json) throws Exception
	{
		jsonArrayList = new ArrayList<NameValuePair>();

		String media_id;
		String play_store;
		String fb;
		String app_store;


		try
		{
			reader = new BufferedReader(new InputStreamReader(json, "iso-8859-1"), 8);
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				System.out.println("read line : "+line);
				builder.append(line + "\n");
			}
			json.close();

			jsonData = builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try
		{
			jsonResponse = new JSONObject(jsonData);
			if(jsonData == null)
			{
				jsonArrayList = null;
			}
			else
			{
				boolean status = jsonResponse.getBoolean("status");
				jsonArrayList.add(new BasicNameValuePair("status", String.valueOf(status)));

				String message = jsonResponse.getString("message");
				jsonArrayList.add(new BasicNameValuePair("message", message));
				int status_code = jsonResponse.getInt("status_code");
				jsonArrayList.add(new BasicNameValuePair("status_code", String.valueOf(status_code)));
				if (status == false)
				{
					jsonArrayList = null;
				} else
				{
					jarray = jsonResponse.getJSONArray("link");

					for (int i = 0; i < jarray.length(); i++)
					{
						jsonResponse = jarray.getJSONObject(i);
						mediaLink = new MediaLink();
						media_id = jsonResponse.getString("media_id");
						mediaLink.setMedia_id(media_id);
						play_store = jsonResponse.getString("play_store");
						mediaLink.setPlay_store(play_store);

						fb = jsonResponse.getString("fb");
						mediaLink.setFb(fb);
						app_store = jsonResponse.getString("app_store");
						mediaLink.setApp_store(app_store);

						jsonArrayList.add(new BasicNameValuePair("media_id", media_id));
						jsonArrayList.add(new BasicNameValuePair("play_store", play_store));
						jsonArrayList.add(new BasicNameValuePair("fb", fb));
						jsonArrayList.add(new BasicNameValuePair("app_store", app_store));

						//jsonArrayList.add(mediaLink);
					}
				}
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			//checking...
		}
		// return JSON String
		return jsonArrayList;
	}
	

}
