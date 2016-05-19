package www.icebd.com.suzukibangladesh.utilities;

import android.text.TextPaint;  
import android.text.style.URLSpan;  
  
public class URLSpanNoUnderline extends URLSpan {  
       
  public URLSpanNoUnderline(String url) {  
         
    super(url);  
      
  }  
    
  @Override   
  public void updateDrawState(TextPaint tp) {  
         
    super.updateDrawState(tp);  
    tp.setUnderlineText(false);  
      
  }  
    
}  