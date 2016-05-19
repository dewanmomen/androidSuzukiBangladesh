package www.icebd.com.suzukibangladesh.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class CustomDialog {
	
	Context mContext;
	AlertDialog.Builder adb;
	
	static String input = "";
	EditText etName;
	
	public CustomDialog(Context context) {
		super();
		this.mContext = context;
		
		adb = new AlertDialog.Builder(mContext);
	}
	
	public void alertDialog(String title, String message) {

		adb.setTitle(title);
		adb.setMessage(message);

		adb.setCancelable(true);

		adb.create();
		adb.show();
	}
	
	public void oneButtonDialog(String title, String message,
								DialogInterface.OnClickListener positiveListener) {

		adb.setTitle(title);
		adb.setMessage(message);

		adb.setCancelable(true);
		adb.setPositiveButton("OK", positiveListener);
		
		adb.create();
		adb.show();
	}
		
	public void twoButtonDialog(String title, String message,
								String positiveText, DialogInterface.OnClickListener positiveListener,
								String negativeText, DialogInterface.OnClickListener negativeListener) {
		
		adb.setCancelable(false);
		
		adb.setTitle(title);
        adb.setMessage(message);
            
        adb.setPositiveButton(positiveText, positiveListener);
        adb.setNegativeButton(negativeText, negativeListener);
        
        adb.create();
        adb.show();
	}
	
	public EditText inputTextDialog(String title, String message, String positiveText, String negativeText,
			DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

		adb.setTitle(title);
		adb.setMessage(message);
		
		etName = new EditText(mContext);
		adb.setView(etName);
		
		adb.setCancelable(false);
		
//		adb.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				input = etName.getText().toString();
//			}
//		});
//		
//		adb.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//			}
		
//		});
		
		adb.setPositiveButton(positiveText, positiveListener);
        adb.setNegativeButton(negativeText, negativeListener);
		
		adb.create();
		adb.show();
		
		return etName;
	}
}