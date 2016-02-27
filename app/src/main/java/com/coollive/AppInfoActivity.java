package com.coollive;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class AppInfoActivity extends Activity {
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.info);
		findViewById(R.id.act_info).setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				AppInfoActivity.this.finish();
			}
		});
	}
}
