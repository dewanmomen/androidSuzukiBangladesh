package www.icebd.com.suzukibangladesh.utilities;

import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class Utils {

	//getting current date and time using Date class
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dateobj = new Date();
    
    //remove underline from link
    public void stripUnderlines(TextView tv)
    {  
        
        Spannable s = (Spannable)tv.getText();  
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);  
          
        for (URLSpan span: spans) {  
          int start = s.getSpanStart(span);  
          int end = s.getSpanEnd(span);  
          s.removeSpan(span);  
          span = new URLSpanNoUnderline(span.getURL());  
          s.setSpan(span, start, end, 0);  
            
        }  
          
        tv.setText(s);  
          
    }   
  
    //set custom fonts
    public void setCustomFont(Context cnt, String fontPath, TextView textView)
	{
		Typeface custom_title_font = Typeface.createFromAsset(cnt.getAssets(), fontPath);
		textView.setTypeface(custom_title_font);
	}
    
    //optional method
    public long lastLoginDays(String lastLogin, String cureentTime)
    {
    	long days =0;
    	 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

         Date d1 = null;
         Date d2 = null;

         try {
                 d1 = format.parse(lastLogin);
                 d2 = format.parse(cureentTime);

                 //in milliseconds
                 long diff = d2.getTime() - d1.getTime();

                 long diffSeconds = diff / 1000 % 60;
                 long diffMinutes = diff / (60 * 1000) % 60;
                 long diffHours = diff / (60 * 60 * 1000) % 24;
                 
                 long diffDays = diff / (24 * 60 * 60 * 1000);
                 
                 days=diffDays;
                 
                 return days;

         } catch (Exception e) {
                 e.printStackTrace();
         }
         return days;
    }
    
    
    public static int DayDifference(String day) {
		String dtStart = day;

		try {

			Date Today_Date = Calendar.getInstance().getTime();
			long Today = Today_Date.getTime();
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date deals_date = format.parse(dtStart);
			long deals_time = deals_date.getTime();
			long difference = Today-deals_time;
			int day_difference = (int) (difference / (1000 * 3600 * 24));
			return day_difference;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;

	}
    
	//get bitmap from url
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	//creatr top rounded corner
	public Bitmap getRoundedTopCornerBitmap(Bitmap bitmap, int radius) 
	{

	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;

	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = radius;

	    // final Rect topRightRect = new Rect(bitmap.getWidth()/2, 0, bitmap.getWidth(), bitmap.getHeight()/2);
	    final Rect bottomRect = new Rect(0, bitmap.getHeight()/2, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    // Fill in upper right corner
	    // canvas.drawRect(topRightRect, paint);
	    // Fill in bottom corners
	    canvas.drawRect(bottomRect, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
	}

}
