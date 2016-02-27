package com.coollive;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.coollive.util.ViewUnbindHelper;
import com.parse.ParseAnalytics;


public class IntroActivity extends Activity {
    
    private String TAG = "IntroActivity";

	//LinearLayout layerIntro;

	Timer timerIntro;
	int deviceWidth;
	int deviceHeight;
	
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	setContentView(R.layout.activity_intro);
    	
		// Track app opens.
		ParseAnalytics.trackAppOpened(getIntent());	        	
    	
    	DisplayMetrics displayMetrics = new DisplayMetrics();

    	WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	windowManager.getDefaultDisplay().getMetrics(displayMetrics);

    	/*
    	
		layerIntro = (LinearLayout) findViewById(R.id.layerIntro);

    	BitmapFactory.Options options = new BitmapFactory.Options();
    	Bitmap resized;

    	options = new BitmapFactory.Options();
    	options.inScaled = false;

    	Bitmap src = BitmapFactory.decodeResource(getResources(),
    		R.drawable.splash_01, options);

    	resized = Bitmap.createScaledBitmap(src, deviceWidth, deviceHeight, true);

    	BitmapDrawable ob = new BitmapDrawable(getResources(), resized);
    	
    	layerIntro.setBackgroundDrawable(ob);
    	*/
    	
    	timerIntro = new Timer();
    	timerIntro.schedule(new TimerTask() {
    	    @Override
    	    public void run() {
    	    	timerIntroMethod();
    	    }
    	}, 300);
    		
		    	
    }



    private void timerIntroMethod() {
    	this.runOnUiThread(timerIntro_Tick);
    }

    private Runnable timerIntro_Tick = new Runnable() {
    	public void run() {

    		timerIntro.cancel();
    		timerIntro = null;
    		Intent i = new Intent(IntroActivity.this, MainActivity.class); 
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	startActivity(i);
	    	finish();
    	}
    };
    
    


    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    
    @Override
    protected void onDestroy() {
		super.onDestroy();
	    ViewUnbindHelper.unbindReferences(this, R.id.layerIntro);
    }
    
    
    @Override
    protected void onPause() {
      super.onPause();
    }    
    
}