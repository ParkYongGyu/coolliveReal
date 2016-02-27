package com.coollive;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PopupNotiActivity extends Activity implements View.OnClickListener {

	TextView btnClose;
	TextView tvNoti;

	public void onClick(View paramView) {
		
		switch (paramView.getId()) {
			default: 
				finish();
				return;
		}
		
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.popupnoti);
		this.tvNoti = ((TextView)findViewById(R.id.tv_noti));
		this.btnClose = ((TextView)findViewById(R.id.btn_close));
		this.tvNoti.setMovementMethod(new ScrollingMovementMethod());
		this.btnClose.setOnClickListener(this);
		new AsyncTaskGetNotification().execute(new Void[0]);
	}

	private class AsyncTaskGetNotification extends AsyncTask <Void, Void, Boolean> {
		Source source = null;
  
		private AsyncTaskGetNotification() {
			
		}
  
		protected Boolean doInBackground(Void... paramVarArgs) {
			try {
				this.source = new Source(new URL("http://acegame119.cafe24.com/xe/board_jtlm82/133"));
				return Boolean.valueOf(true);
			} catch (MalformedURLException paramVarArgs1) {
				paramVarArgs1.printStackTrace();
				return Boolean.valueOf(false);
			} catch (IOException paramVarArgs2) {
				paramVarArgs2.printStackTrace();
			}
			return Boolean.valueOf(false);
		}
  
		protected void onPostExecute(Boolean paramBoolean) {
	    
			if (paramBoolean.booleanValue()) {}
			
			try {
				String TAG = "Popup";
				String str = ((Element)this.source.getAllElements("div").get(15)).toString();
				PopupNotiActivity.this.tvNoti.setText(Html.fromHtml(str));
				super.onPostExecute(paramBoolean);
				return;
			} catch (Exception localException) {
				for (;;) {
					localException.printStackTrace();
				}
			}
		}
	  
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
}