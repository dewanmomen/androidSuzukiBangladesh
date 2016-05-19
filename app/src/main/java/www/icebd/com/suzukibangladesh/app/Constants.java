/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package www.icebd.com.suzukibangladesh.app;


import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.json.AsyncResponse;
import www.icebd.com.suzukibangladesh.json.PostResponseAsyncTask;

public final class Constants implements AsyncResponse {
	SharedPreferences pref ;
	SharedPreferences.Editor editor ;


	public static final String[] IMAGES = new String[] {
			"http://icebd.com/suzuki/uploads/3.png",
			"http://icebd.com/suzuki/uploads/2.png",
			"http://icebd.com/suzuki/uploads/1.png"
	};

	private Constants() {
		/*pref = getApplicationContext().getSharedPreferences("SuzukiBangladeshPref", MODE_PRIVATE);
		editor = pref.edit();
		 Log.i("Test","");
*/
		Log.i("Test","Inside constants");
	}


	@Override
	public void processFinish(String output) {

	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}
