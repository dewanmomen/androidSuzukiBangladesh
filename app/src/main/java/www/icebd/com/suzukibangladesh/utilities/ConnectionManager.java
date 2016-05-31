package www.icebd.com.suzukibangladesh.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;



import android.util.Log;

public class ConnectionManager {
	
	//public static String SERVER_URL = "http://icebd.com/suzuki/suzukiApi/server/";
	public static String SERVER_URL = "http://iceapps.org/suzuki/suzukiApi/Server/";

	private static String response = null;
	private final static int GETRequest = 1;
	private final static int POSTRequest = 2;

	public String makeWebServiceCall(String url, int requestMethod) {
		return this.makeWebServiceCall(SERVER_URL, requestMethod, null);
	}

	public static String makeWebServiceCall(String urlAddress, int requestMethod,
									 HashMap<String, String> params) {
		URL url;
		String response = "";
		try {
			url = new URL(urlAddress);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15001);
			conn.setConnectTimeout(15001);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if (requestMethod == POSTRequest) {
				conn.setRequestMethod("POST");
			} else if (requestMethod == GETRequest) {
				conn.setRequestMethod("GETRequest");
			}
			if (params != null)
			{

				OutputStream ostream = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(ostream, "UTF-8"));
				StringBuilder requestresult = new StringBuilder();
				boolean first = true;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (first)
						first = false;
					else
						requestresult.append("&");
					requestresult.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
					requestresult.append("=");
					requestresult.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				}
				writer.write(requestresult.toString());

				writer.flush();
				writer.close();
				ostream.close();
			}
			int reqResponseCode = conn.getResponseCode();

			if (reqResponseCode == HttpURLConnection.HTTP_OK) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					response += line;
				}
			} else {
				response = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static InputStream getResponseFromServer(String methodName, ArrayList<NameValuePair> postData) throws Exception
	{
		//String response = null;
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost postRequest = new HttpPost(SERVER_URL+methodName);
		//System.out.println("server url :"+SERVER_URL+methodName);
		postRequest.setEntity(new UrlEncodedFormEntity(postData));
		//System.out.println("post data :"+postData);
		HttpResponse httpResponse = httpClient.execute(postRequest);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		//boolean status = httpResponse.getStatusLine(
		//System.out.println("staus code :"+statusCode);
		if(statusCode == HttpStatus.SC_OK)
		{
			//System.out.println("inside staus code :"+statusCode);
			HttpEntity entity = httpResponse.getEntity();
			if(entity != null)
			{
				//response = EntityUtils.toString(entity);
				return entity.getContent();
			}
		}else
		{
			HttpEntity entity = httpResponse.getEntity();
			return entity.getContent();
		}
		return null;
	}
	
	public static boolean hasInternetConnection()
	{
		try
		{
			HttpURLConnection urlConnection = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
			urlConnection.setRequestProperty("User-Agent", "Test");
			urlConnection.setRequestProperty("Connection", "close");
			urlConnection.setConnectTimeout(15000);
			urlConnection.connect();

			return (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (MalformedURLException ex)
		{
			Log.e("ConnectionManager:", ex.getMessage());
		}
		catch (IOException ex)
		{
			Log.e("ConnectionManager:", ex.getMessage());
		}
		return false;
	}
}
