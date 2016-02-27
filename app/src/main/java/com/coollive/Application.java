package com.coollive;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

public class Application extends android.app.Application {
	
	public Application() {
	}


	@Override
	public void onCreate() {
		
		super.onCreate();
		
		Parse.initialize(this, "X93ovRol16qKmO4DNujCrz0mTJEbhrC4lrOzPwLS", "hC6aZSweymvJS1glLWz6tGrv0t8d9NUXM7hGLpjF");
		PushService.setDefaultPushCallback(this, IntroActivity.class);
		/*
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback()
	    {
	        @Override
	        public void done(ParseException e)
	        {
	            PushService.setDefaultPushCallback(Application.this, IntroActivity.class);
				Log.d("com.parse.push", "PushService done.");
	        }
	    });

		ParsePush.subscribeInBackground("", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe for push", e);
				}
			}
		});
		*/
	}
}