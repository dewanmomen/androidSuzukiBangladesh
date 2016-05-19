package www.icebd.com.suzukibangladesh.utilities;

//import demo.gogolf.com.R;

import java.util.ArrayList;
import java.util.List;

import www.icebd.com.suzukibangladesh.spare_parts.MyCartObject;
import www.icebd.com.suzukibangladesh.spare_parts.SparePartsListObject;

public class Constant {

	public static String definedIdentifier = "qr965stm#o8%82/hfj821gkp@ui0t";
	public static String notificationKey = null;
	//new right menu items
	public static final String[] right_menu_items = { "Membership Card", "My Profile", "Sign Out"};
	
	//public static final Integer[] right_menu_images = { R.drawable.ic_card_xdpi,R.drawable.menu_myprofile_normal, R.drawable.menu_logout_normal};
	public static final double taxPercentage = 24.0;
	public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;

	public static List<SparePartsListObject.SparePartsItem> listMyCartObj = new ArrayList<SparePartsListObject.SparePartsItem>();
	public static int isDetermin = 0;
	//public static boolean isSparePartsPurchase = false;
	//public static boolean isQuizzes = false;

	public static String urlHistoryOfSuzuki = "http://icebd.com/suzuki/admin/history";
	public static String urlAboutUs = "http://icebd.com/suzuki/admin/aboutUs";

}
