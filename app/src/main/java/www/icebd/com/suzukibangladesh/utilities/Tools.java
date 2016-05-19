package www.icebd.com.suzukibangladesh.utilities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Tools
{

	public static boolean isEmailValid(String email)
	{
		//TODO: Replace this with your own logic
		//return email.contains("@");
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	public static boolean isPasswordValid(String password)
	{
		//TODO: Replace this with your own logic
		return password.length() >= 6;
	}
	public static String DateFormator(String dateString, String fromFormate,
			String toFormate) {
		String result = "";
		SimpleDateFormat stringFormatter = new SimpleDateFormat(fromFormate);
		SimpleDateFormat desireFormarte = new SimpleDateFormat(toFormate);

		try {

			Date date = stringFormatter.parse(dateString);
			//System.out.println(date);
			//System.out.println(desireFormarte.format(date));
			result = desireFormarte.format(date);

		} catch (ParseException e) {
			result = null;
			e.printStackTrace();
		}

		return result;

	}
	
	public static boolean isDateTimeGreater(String today, String compareDate, String dateFormat)
	{
		try{

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date1 = sdf.parse(today);
            Date date2 = sdf.parse(compareDate);

            if(date2.compareTo(date1)>=0){
            	
                //System.out.println("greater");
                return true;
            }else{
               // System.out.println("not greater");
                return false;
            }

        }catch(ParseException ex){
            ex.printStackTrace();
        }
		return false;		
	}
	
	public static String getDateTime( Calendar cal){
		
        return cal.get(Calendar.YEAR) +"-" +(cal.get(Calendar.MONTH)+1) + "-" +cal.get(Calendar.DATE)+" "
        		+cal.get(Calendar.HOUR_OF_DAY) +":" +(cal.get(Calendar.MINUTE)) + ":" + cal.get(Calendar.SECOND);
	}
	
	public static int stringToInteger(String str)
	{
		int result;
		try
		{
			result = Integer.parseInt(str);
		}
		catch(Exception e)
		{
			result = -1;
		}
		return result;
	}
	
	public static boolean isDouble( String str )
	{
	    try{
	        Double.parseDouble( str );
	        return true;
	    }
	    catch( Exception e ){
	        return false;
	    }
	}
	
	 public static boolean isDigits(String str) {
        if (StringUtils.isEmpty(str)) {
           return false;
       }
       for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
               return false;
           }
        }
       return true;
    }

}
