package www.icebd.com.suzukibangladesh.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {

	Context context;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	public PreferenceUtil(Context context) {
		super();
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public String getAgentID() {
		
		return preferences.getString("AgentID", "0");
	}
	
	public void setAgentID(String agentID) {
		
		editor = preferences.edit();
		editor.putString("AgentID", agentID);
		editor.commit();
	}

	public String getAuthKey()
	{
		return preferences.getString("AuthKey", "0");
	}
	public void setAuthKey(String authKey) {

		editor = preferences.edit();
		editor.putString("AuthKey", authKey);
		editor.commit();
	}
	public int getPINstatus()
	{
		return preferences.getInt("PINstatus", 0);
	}
	public void setPINstatus(int piNstatus) {

		editor = preferences.edit();
		editor.putInt("PINstatus", piNstatus);
		editor.commit();
	}
	
	public void setFBButtonState(boolean state){
		editor = preferences.edit();
		editor.putBoolean("fbstate",state);
		editor.commit();
	}
	
	public Boolean getFBButtonState() {
		
		return preferences.getBoolean("fbstate",true);
	}
	
	public void setTwitterButtonState(boolean state){
		editor = preferences.edit();
		editor.putBoolean("twitterstate",state);
		editor.commit();
	}
	
	public Boolean getTwitterButtonState() {
		
		return preferences.getBoolean("twitterstate",true);
	}
	
}
