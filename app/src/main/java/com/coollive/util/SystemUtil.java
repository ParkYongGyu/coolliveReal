package com.coollive.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SystemUtil
{
	
	private static final String TAG = "SystemUtil";
	
	private static final int SP_ADJUST_DENSITY_DEFAULT_WHEN_TABLET = -1;  // Galaxy Tab 10.1  
	private static final int SP_ADJUST_DENSITY_DEFAULT_NOT_TABLET = -5;   // Blue Stack Android 4.4.X
	private static final int SP_ADJUST_DENSITY_TV_WHEN_TABLET = -1;      
	private static final int SP_ADJUST_DENSITY_TV_NOT_TABLET = -5;       
	private static final int SP_ADJUST_DENSITY_HIGH_WHEN_TABLET = 0;    
	private static final int SP_ADJUST_DENSITY_HIGH_NOT_TABLET = -4;     
	private static final int SP_ADJUST_DENSITY_XHIGH_WHEN_TABLET = -3;   
	private static final int SP_ADJUST_DENSITY_XHIGH_NOT_TABLET = -7;    // SAMSUNG GALAXY S2 HD LTE Android 4.1.X, & NOTE2
	private static final int SP_ADJUST_DENSITY_400_WHEN_TABLET = -2;     
	private static final int SP_ADJUST_DENSITY_400_NOT_TABLET = -6;      
	private static final int SP_ADJUST_DENSITY_XXHIGH_WHEN_TABLET = 0;   
	private static final int SP_ADJUST_DENSITY_XXHIGH_NOT_TABLET = -6;    
	private static final int SP_ADJUST_DENSITY_XXXHIGH_WHEN_TABLET = 2;  
	private static final int SP_ADJUST_DENSITY_XXXHIGH_NOT_TABLET = -4;   

   /**
    * App<br>
    * Return version name.
    * @return (ex) 0.0.1 (if Fail) fail
    */
   public static String versionName()
   {
      PackageInfo info;
      try
      {
         info = ContextUtil.CONTEXT.getPackageManager().getPackageInfo(ContextUtil.CONTEXT.getPackageName(), PackageManager.GET_META_DATA);
         return info.versionName;
      } catch (NameNotFoundException e)
      {
         e.printStackTrace();
         return "fail";
      }
   }
   
   
   /**
    * App 버전 코드 �??��?���? <br>
    * Return version code.
    * @return (ex) 1 (if Fail) -1
    */
   public static int versionCode()
   {
      PackageInfo info;
      try
      {
         info = ContextUtil.CONTEXT.getPackageManager().getPackageInfo(ContextUtil.CONTEXT.getPackageName(), PackageManager.GET_META_DATA);
         return info.versionCode;
      } catch (NameNotFoundException e)
      {
         e.printStackTrace();
         return -1;
      }
   }
   
   
   /**
    * ?�� 모델 ?���? �??��?���? <br>
    * Return phone (model) name.
    * @return HTC Desire, GT-P7510
    */
   public static String modelName()
   {
      return Build.MODEL.toString();
   }
   
   
   /**
    * ?�� ?��?��번호 �??��?���? <br>
    * Return phoneNumber
    * @return
    */
   public static String phoneNumber()
   {
      TelephonyManager teleponyManager = (TelephonyManager) ContextUtil.CONTEXT.getSystemService(Context.TELEPHONY_SERVICE);
      return teleponyManager.getLine1Number();
   }
   
   
   /**
    * <br>
    * The inspection process is running at the top
    * @return true=foreground
    */
   public static boolean isForeground(Context context)
   {
      ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      List<RunningTaskInfo> list = activityManager.getRunningTasks(1);
      ComponentName cn = list.get(0).topActivity;
      String name = cn.getPackageName();
      
      return name.indexOf(context.getPackageName()) > -1;
   }
   
   
   /**
    * App?�� ?��?�� ?��?��?��?���? ?���? ?��?�� <br>
    * Check the app is alive
    * @return true=alive
    */
   public static boolean isAppAlive()
   {
      ActivityManager activityManager = (ActivityManager) ContextUtil.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
      List<RunningTaskInfo> list = activityManager.getRunningTasks(100);
      
      for (RunningTaskInfo task : list)
      {
         ComponentName cn = task.topActivity;
         String name = cn.getPackageName();
         if (name.indexOf(ContextUtil.CONTEXT.getPackageName()) > -1)
            return true;
      }
      return false;
   }
   
   
   /**
    * �??�� 최상?�� Activity?���?�? ?��?�� <br>
    * Inspect the highest activity in the left
    * @param Activity Name <br>
    *           Chat.class.getName() = com.caffein.testActivity
    * @return
    */
   public static boolean isTopActivity(String name)
   {
      ActivityManager activityManager = (ActivityManager) ContextUtil.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
      List<RunningTaskInfo> list = activityManager.getRunningTasks(1);
      ComponentName cn = list.get(0).topActivity;
      
      return cn.getClassName().equals(name);
   }
   
   
   /**
    * Base Activity?���?�? ?��?�� <br>
    * At the bottom of the stack check if the name of the database activity
    * @param Activity Name
    * @return
    */
   public static boolean isBaseActivity(String name)
   {
      ActivityManager activityManager = (ActivityManager) ContextUtil.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
      List<RunningTaskInfo> list = activityManager.getRunningTasks(1);
      ComponentName cn = list.get(0).baseActivity;
      
      return cn.getClassName().equals(name);
   }
   
   
   /**
    * Wi-Fi�? ?��결이 ?��?��?�� ?��?���? ?��?�� <br>
    * Is Wi-Fi connected?
    * @return true=connected
    */
   public static boolean isConnectedWiFi() {
	   boolean connected = false;
	   try {
		   ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
		   connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	   } catch ( NullPointerException ne ) {
		   connected = false;
		   ne.printStackTrace();
	   }
	   return connected;
   }
   
   
   /**
    * 모바?�� ?��?��?��?���? ?��결이 ?��?��?�� ?��?���? ?��?�� <br>
    * Is MobileNetwork connected?
    * @return true=connected
    */
   public static boolean isConnectedMobileNetwork()
   {
      boolean kResult = false;
      
      try
      {
         ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
         kResult = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return kResult;
   }
   
   
	public static boolean isNetConnected(Context context) {

	   ConnectivityManager cm =
		        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
	   NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	   boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
	   return isConnected;
	}
   

   /**
    * ?��?��?��?��?�� ?��결이 ?��?��?��?���? ?��?�� <br>
    * Is any network connected?
    * @return true=connected
    */
   public static boolean isConnectNetwork(Context context) {
	   return isNetConnected(context);
	   //return isConnectedWiFi() || isConnectedMobileNetwork();
   }
   
   
   /**
    * Android Id �??��?���? <br>
    * Return android_id<br />
    * (If factory reset, android_id is changed)
    * @param context
    * @return
    */
   public static String androidId(Context context)
   {
      return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
   }
   
   
   /**
    * Debuggable ?���? ?��?�� <br>
    * Return debuggable value in menifest
    * @return true=debuggable
    */
   public static boolean isDebuggable()
   {
      boolean kResult = false;
      
      if (ContextUtil.CONTEXT == null)
         kResult = false;
      else
         kResult = ((ContextUtil.CONTEXT.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
      
      return kResult;
   }
   
   
   /**
    * SD 카드�? ?��?���? ?���? ?��?�� <br>
    * Return Sd-Card device that you have.
    * @return ture=have
    */
   public static boolean isHasSDCard()
   {
      return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
   }
   
   
   /**
    * 진동?��리기 <br>
    * Vibrate
    * @param content
    * @param msec (time)
    */
   public static void vibrate(Context $context, int $msec)
   {
      try
      {
         Vibrator vibrator = (Vibrator) $context.getSystemService(Context.VIBRATOR_SERVICE);
         vibrator.vibrate($msec);
      } catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   


   public static boolean isTabletDevice(Context activityContext) {
		boolean device_large = false;
	
		int intScreenSize = activityContext.getResources().getConfiguration().screenLayout
			& Configuration.SCREENLAYOUT_SIZE_MASK;
	
		if (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE)
		    device_large = true;
	
		if (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE + 1)
		    device_large = true;
	
		if (device_large) {
		    DisplayMetrics metrics = new DisplayMetrics();
		    Activity activity = (Activity) activityContext;
		    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	
		    if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
			    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
			    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
			    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
			    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
			Log.v("MainActivity", "IsTabletDevice-True");
			return true;
		    }
		}
	
		Log.v("MainActivity", "IsTabletDevice-False");
	
		return false;
   }     
	

   
   /**
    * This method converts dp unit to equivalent pixels, depending on device density. 
    * 
    * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
    * @param context Context to get resources and device specific display metrics
    * @return A float value to represent px equivalent to dp depending on device density
    */
   public static float convertDpToPixel(float dp, Context context){
	   /*
       Resources resources = context.getResources();
       DisplayMetrics metrics = resources.getDisplayMetrics();
       float px = dp * (metrics.densityDpi / 160f);
       return px;
       */
	   return dp * context.getResources().getDisplayMetrics().density;
   }

   /**
    * This method converts device specific pixels to density independent pixels.
    * 
    * @param px A value in px (pixels) unit. Which we need to convert into db
    * @param context Context to get resources and device specific display metrics
    * @return A float value to represent dp equivalent to px value
    */
   public static float convertPixelsToDp(float px, Context context){
	   /*
       Resources resources = context.getResources();
       DisplayMetrics metrics = resources.getDisplayMetrics();
       float dp = px / (metrics.densityDpi / 160f);
       return dp;
       */
       return px / context.getResources().getDisplayMetrics().density;
   }   
   
   
   public static void setTextSizeWithTabletSP(TextView textView, int spSize, boolean bTablet) {
	   if(bTablet) {
		   textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
	   } else {
		   textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (spSize - 4));
	   }
   }
   
   public static void setTextSizeWithTabletSP(TextView textView, int spSize, boolean bTablet, int deviceWidth) {
	   if(bTablet) {
		   textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
	   } else {
		   if ( deviceWidth >= 768 ) {
			   textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (spSize - 4));
		   } else {
			   textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (spSize - 5));
		   }		   
	   }
   }
   
   public static void setTextSizeWithTabletSP(Button button, int spSize, boolean bTablet, int deviceWidth) {
	   if(bTablet) {
		   button.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
	   } else {
		   if ( deviceWidth >= 768 ) {
			   button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (spSize - 4));
		   } else {
			   button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (spSize - 5));
		   }		   
	   }
   }
   
   public static void setTextSizeWithTabletSP(EditText editText, int spSize, boolean bTablet) {
	   if(bTablet) {
		   editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
	   } else {
		   editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (spSize - 4));
	   }
   }
		
   

   public static void setTextSize(Button textView, int spSize, Activity activity, boolean bTablet) {
	   
	   DisplayMetrics metrics = new DisplayMetrics();
	   //Activity activity = (Activity) context;
	   activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

	   switch( metrics.densityDpi ) {
	     case DisplayMetrics.DENSITY_DEFAULT: // 160
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_DEFAULT_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_DEFAULT:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_DEFAULT_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_DEFAULT:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_TV: //213
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_TV_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_TV:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_TV_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_TV:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_HIGH: // 240
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_HIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_HIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_HIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_HIGH:      bTablet - " + bTablet);
	    	 }
           break;
	     case DisplayMetrics.DENSITY_XHIGH: // 320
	    	 if ( bTablet ) {
	    		 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XHIGH_WHEN_TABLET);
	    		 Log.d(TAG, "DisplayMetrics.DENSITY_XHIGH:      bTablet - " + bTablet);
	    	 } else {
	    		 // SAMSUNG GALAXY S2 HD LTE 
	    		 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XHIGH_NOT_TABLET);
	    		 Log.d(TAG, "DisplayMetrics.DENSITY_XHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_400: //400
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_400_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_400:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_400_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_400:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_XXHIGH: // 480
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXHIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_XXXHIGH: // 640
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXXHIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXXHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     default : 
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DEFAULT SWITCH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DEFAULT SWITCH:      bTablet - " + bTablet);
	    	 }
	    	 break;
            
	   }
   }
   
   
   public static void setTextSize(TextView textView, int spSize, Activity activity, boolean bTablet) {
	   
	   DisplayMetrics metrics = new DisplayMetrics();
	   //Activity activity = (Activity) context;
	   activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

	   switch( metrics.densityDpi ) {
	     case DisplayMetrics.DENSITY_DEFAULT: // 160
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_DEFAULT_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_DEFAULT:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_DEFAULT_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_DEFAULT:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_TV: //213
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_TV_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_TV:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_TV_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_TV:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_HIGH: // 240
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_HIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_HIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_HIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_HIGH:      bTablet - " + bTablet);
	    	 }
           break;
	     case DisplayMetrics.DENSITY_XHIGH: // 320
	    	 if ( bTablet ) {
	    		 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XHIGH_WHEN_TABLET);
	    		 Log.d(TAG, "DisplayMetrics.DENSITY_XHIGH:      bTablet - " + bTablet);
	    	 } else {
	    		 // SAMSUNG GALAXY S2 HD LTE 
	    		 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XHIGH_NOT_TABLET);
	    		 Log.d(TAG, "DisplayMetrics.DENSITY_XHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_400: //400
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_400_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_400:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_400_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_400:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_XXHIGH: // 480
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXHIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_XXXHIGH: // 640
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXXHIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXXHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     default : 
	    	 if ( bTablet ) {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DEFAULT SWITCH:      bTablet - " + bTablet);
	    	 } else {
		    	 textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DEFAULT SWITCH:      bTablet - " + bTablet);
	    	 }
	    	 break;
            
	   }
   }

   

   public static void setTextSize(EditText editText, int spSize, Activity activity, boolean bTablet) {
	   
	   DisplayMetrics metrics = new DisplayMetrics();
	   //Activity activity = (Activity) context;
	   activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);


	   switch( metrics.densityDpi ) {
	     case DisplayMetrics.DENSITY_DEFAULT: // 160
	    	 if ( bTablet ) {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_DEFAULT_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_DEFAULT:      bTablet - " + bTablet);
	    	 } else {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_DEFAULT_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_DEFAULT:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_TV: //213
	    	 if ( bTablet ) {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_TV_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_TV:      bTablet - " + bTablet);
	    	 } else {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_TV_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_TV:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_HIGH: // 240
	    	 if ( bTablet ) {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_HIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_HIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_HIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_HIGH:      bTablet - " + bTablet);
	    	 }
           break;
	     case DisplayMetrics.DENSITY_XHIGH: // 320
	    	 if ( bTablet ) {
	    		 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XHIGH_WHEN_TABLET);
	    		 Log.d(TAG, "DisplayMetrics.DENSITY_XHIGH:      bTablet - " + bTablet);
	    	 } else {
	    		 // SAMSUNG GALAXY S2 HD LTE 
	    		 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XHIGH_NOT_TABLET);
	    		 Log.d(TAG, "DisplayMetrics.DENSITY_XHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_400: //400
	    	 if ( bTablet ) {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_400_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_400:      bTablet - " + bTablet);
	    	 } else {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_400_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_400:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_XXHIGH: // 480
	    	 if ( bTablet ) {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXHIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     case DisplayMetrics.DENSITY_XXXHIGH: // 640
	    	 if ( bTablet ) {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXXHIGH:      bTablet - " + bTablet);
	    	 } else {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DENSITY_XXXHIGH:      bTablet - " + bTablet);
	    	 }
	    	 break;
	     default : 
	    	 if ( bTablet ) {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_WHEN_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DEFAULT SWITCH:      bTablet - " + bTablet);
	    	 } else {
		    	 editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize + SP_ADJUST_DENSITY_XXHIGH_NOT_TABLET);
		    	 Log.d(TAG, "DisplayMetrics.DEFAULT SWITCH:      bTablet - " + bTablet);
	    	 }
	    	 break;
            
	   }	   
	   
   }

}
