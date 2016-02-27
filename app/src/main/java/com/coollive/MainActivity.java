package com.coollive;

import java.util.Timer;
import java.util.TimerTask;

import net.htmlparser.jericho.Source;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coollive.util.ImageUtil;
import com.coollive.util.SystemUtil;


@SuppressLint({"NewApi", "SetJavaScriptEnabled"})
public class MainActivity extends Activity {

	private final static int DESIGN_BASE_WIDTH = 1600;
	private final static int DESIGN_BASE_HEIGHT = 2500;

	boolean isLiveScore = true; // 처음 화면이 livescore 이기 때문에 true 로 세팅함.
	boolean bPressedAction = false;

	LinearLayout layerMain;

	Timer timerContentSet;

	ImageView imgLogo;

	ImageView imgInfo;

	com.coollive.ToggleImageButton btnMenuLivescore;
	com.coollive.ToggleImageButton btnMenuForecast;
	com.coollive.ToggleImageButton btnMenuTrend;
	com.coollive.ToggleImageButton btnMenuAnalysisPic;

	LinearLayout livetab, buttonNamed, button7M;
	LinearLayout oddstab;

	ProgressDialog progressDialog;
	TextView board;
	Source source = null;

	WebView web;
	ImageView imgBanner;
	com.coollive.ToggleImageButton btnSubSoccer;
	com.coollive.ToggleImageButton btnSubBasketball;
	com.coollive.ToggleImageButton btnSubVolleyball;
	com.coollive.ToggleImageButton btnSubBaseball;
	com.coollive.ToggleImageButton btnSubIcehockey;

	boolean menuAnalytics = false;



	int deviceWidth;
	int deviceHeight;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		startActivity(new Intent(this, PopupNotiActivity.class));
		//startActivity(new Intent(this, SplashScreen.class));

		setContentView(R.layout.activity_main);


		DisplayMetrics displayMetrics = new DisplayMetrics();

		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);

		deviceWidth = displayMetrics.widthPixels;
		deviceHeight = displayMetrics.heightPixels;


		layerMain = (LinearLayout) findViewById(R.id.layerMain);
		imgLogo = (ImageView) findViewById(R.id.imgLogo);

		imgInfo = (ImageView) findViewById(R.id.imgInfo);

		btnMenuLivescore = (ToggleImageButton) findViewById(R.id.btnMenuLivescore);
		btnMenuForecast = (ToggleImageButton) findViewById(R.id.btnMenuForecast);
		btnMenuTrend = (ToggleImageButton) findViewById(R.id.btnMenuTrend);
		btnMenuAnalysisPic = (ToggleImageButton) findViewById(R.id.btnMenuAnalysisPic);
		int desiredHeightMenu = getYRatioVal(200);
		int desiredWidthMenuLiveScore = getYRatioVal(499);
		int desiredWidthMenuForecast = getYRatioVal(377);
		int desiredWidthMenuTrend = getYRatioVal(367);
		int desiredWidthMenuAnalysisPic = getYRatioVal(357);

		ImageUtil.setImageViewImg(this, btnMenuLivescore, R.drawable.menu_livescore, desiredWidthMenuLiveScore, desiredHeightMenu);
		ImageUtil.setImageViewImg(this, btnMenuForecast, R.drawable.menu_forecast, desiredWidthMenuForecast, desiredHeightMenu);
		ImageUtil.setImageViewImg(this, btnMenuTrend, R.drawable.menu_trend, desiredWidthMenuTrend, desiredHeightMenu);
		ImageUtil.setImageViewImg(this, btnMenuAnalysisPic, R.drawable.menu_analysis_pic, desiredWidthMenuAnalysisPic, desiredHeightMenu);


		livetab = (LinearLayout) findViewById(R.id.livetab);
		buttonNamed = (LinearLayout) findViewById(R.id.buttonNamed);
		button7M = (LinearLayout) findViewById(R.id.button7M);
		oddstab = (LinearLayout) findViewById(R.id.oddstab);

		btnSubSoccer = (ToggleImageButton) findViewById(R.id.btnSubSoccer);
		btnSubBasketball = (ToggleImageButton) findViewById(R.id.btnSubBasketball);
		btnSubVolleyball = (ToggleImageButton) findViewById(R.id.btnSubVolleyball);
		btnSubBaseball = (ToggleImageButton) findViewById(R.id.btnSubBaseball);
		btnSubIcehockey = (ToggleImageButton) findViewById(R.id.btnSubIcehockey);


		int desiredHeightSub = getYRatioVal(153);
		int desiredWidthSubSoccer = getYRatioVal(300);
		int desiredWidthSubBasketball  = getYRatioVal(330);
		int desiredWidthSubVolleyball  = getYRatioVal(321);
		int desiredWidthSubBaseball  = getYRatioVal(361);
		int desiredWidthSubIcehockey  = getYRatioVal(288);

		ImageUtil.setImageViewImg(this, btnSubSoccer, R.drawable.sub_soccer, desiredWidthSubSoccer, desiredHeightSub);
		ImageUtil.setImageViewImg(this, btnSubBasketball, R.drawable.sub_baseketball, desiredWidthSubBasketball, desiredHeightSub);
		ImageUtil.setImageViewImg(this, btnSubVolleyball, R.drawable.sub_volleyball, desiredWidthSubVolleyball, desiredHeightSub);
		ImageUtil.setImageViewImg(this, btnSubBaseball, R.drawable.sub_baseball, desiredWidthSubBaseball, desiredHeightSub);
		ImageUtil.setImageViewImg(this, btnSubIcehockey, R.drawable.sub_icehockey, desiredWidthSubIcehockey, desiredHeightSub);


		boolean bTablet = SystemUtil.isTabletDevice(this);

		web = (WebView) findViewById(R.id.web);
		imgBanner = (ImageView) findViewById(R.id.imageBanner);
		board = (TextView) findViewById(R.id.board);
		board.setSelected(true);
		SystemUtil.setTextSizeWithTabletSP(board, 20, bTablet);


		this.imgLogo.setOnClickListener(this.clickListener);
		this.imgInfo.setOnClickListener(this.clickListener);
		this.btnMenuLivescore.setOnClickListener(this.clickListener);
		this.buttonNamed.setOnClickListener(this.clickListener);
		this.button7M.setOnClickListener(this.clickListener);
		this.btnMenuForecast.setOnClickListener(this.clickListener);
		this.btnMenuTrend.setOnClickListener(this.clickListener);
		this.btnMenuAnalysisPic.setOnClickListener(this.clickListener);
		this.btnSubSoccer.setOnClickListener(this.clickListener);
		this.btnSubBasketball.setOnClickListener(this.clickListener);
		this.btnSubVolleyball.setOnClickListener(this.clickListener);
		this.btnSubBaseball.setOnClickListener(this.clickListener);
		this.btnSubIcehockey.setOnClickListener(this.clickListener);
		this.imgBanner.setOnClickListener(this.clickListener);
		//this.board.setOnClickListener(this.clickListener);


		this.web.getSettings().setJavaScriptEnabled(true);
		this.web.getSettings().setBuiltInZoomControls(true);
		this.web.getSettings().setDisplayZoomControls(false);
		this.web.getSettings().setSupportZoom(true);
		this.web.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		this.web.getSettings().setUserAgentString(null);
		this.web.getSettings().setLoadsImagesAutomatically(true);
		this.web.getSettings().setCacheMode(2);
		this.web.getSettings().setLoadWithOverviewMode(true);
		this.web.getSettings().setUseWideViewPort(true);
		this.web.setBackgroundColor(0);
		this.web.loadUrl("http://named.com/namedscore/football/");
		this.web.setWebViewClient(new TabWebViewClient());//null
		//setTimerContentSet(2000);

	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//ViewUnbindHelper.unbindReferences(this, R.id.layerMain);

	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}




	public int getXRatioVal(int input) {
		int ret = 0;

		ret = (deviceWidth * input) / DESIGN_BASE_WIDTH;

		return ret;
	}

	public int getYRatioVal(int input) {
		int ret = 0;

		ret = (deviceHeight * input) / DESIGN_BASE_HEIGHT;

		return ret;
	}


	View.OnClickListener clickListener = new View.OnClickListener() {

		public void onClick(View paramAnonymousView) {
			switch (paramAnonymousView.getId()) {

				case R.id.imgLogo:

					menuAnalytics = false;
					String strParamAnonymousView = getPackageName();
					try {
						startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + strParamAnonymousView)));
						return;
					} catch (ActivityNotFoundException localActivityNotFoundException)  {
						startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + strParamAnonymousView)));
						return;
					}

				case R.id.imgInfo:
					menuAnalytics = false;
					Intent iparamAnonymousView = new Intent(getApplicationContext(), AppInfoActivity.class);
					startActivity(iparamAnonymousView);

					return;

				case R.id.btnMenuLivescore:
					menuAnalytics = false;

					isLiveScore = true;
					livetab.setVisibility(View.VISIBLE);
					oddstab.setVisibility(View.GONE);
					web.loadUrl("http://named.com/namedscore/football/");

					btnMenuLivescore.setChecked(true);
					btnMenuForecast.setChecked(false);
					btnMenuTrend.setChecked(false);
					btnMenuAnalysisPic.setChecked(false);
					return;



				case R.id.buttonNamed:
					menuAnalytics = false;

					livetab.setVisibility(View.VISIBLE);
					oddstab.setVisibility(View.GONE);
					web.loadUrl("http://named.com/namedscore/football/");

					return;

				case R.id.button7M:
					menuAnalytics = false;

					livetab.setVisibility(View.VISIBLE);
					oddstab.setVisibility(View.GONE);
					web.loadUrl("http://free1.7m.cn/live.aspx?mark=kr&TimeZone=%2B0900&wordAd=&wadurl=http://&width=100%&cpageBgColor=FFFFFF&tableFontSize=12&cborderColor=333333&ctdColor1=EEEEEE&ctdColor2=FFFFFF&clinkColor=0044DD&cdateFontColor=FFFFFF&cdateBgColor=333333&scoreFontSize=12&cteamFontColor=000000&cgoalFontColor=FF0000&cgoalBgColor=FFFF99&cremarkFontColor=000000&cremarkBgColor=F7F8F3&Skins=6&teamWeight=400&scoreWeight=700&goalWeight=700&fontWeight=700&DSTbox=");
					return;

				// 경기예측
				case R.id.btnMenuForecast:
					menuAnalytics = false;
					livetab.setVisibility(View.GONE);
					oddstab.setVisibility(View.GONE);
					web.loadUrl("http://predict.7msport.com/kr/");

					btnMenuLivescore.setChecked(false);
					btnMenuForecast.setChecked(true);
					btnMenuTrend.setChecked(false);
					btnMenuAnalysisPic.setChecked(false);

					return;

				// 배당흐름
				case R.id.btnMenuTrend:
					menuAnalytics = false;
					livetab.setVisibility(View.GONE);
					oddstab.setVisibility(View.VISIBLE);
					web.loadUrl("http://www.hot-odds.co.kr:80/Widget/AllGames?sportIds=6046&oddType=EU&timeZone=GMTplus9&langId=1");

					btnMenuLivescore.setChecked(false);
					btnMenuForecast.setChecked(false);
					btnMenuTrend.setChecked(true);
					btnMenuAnalysisPic.setChecked(false);

					btnSubSoccer.setChecked(true);
					btnSubBaseball.setChecked(false);
					btnSubBasketball.setChecked(false);
					btnSubVolleyball.setChecked(false);
					btnSubIcehockey.setChecked(false);

					return;

				case R.id.btnSubSoccer:
					menuAnalytics = false;
					web.loadUrl("http://www.hot-odds.co.kr:80/Widget/AllGames?sportIds=6046&oddType=EU&timeZone=GMTplus9&langId=1");

					btnSubSoccer.setChecked(true);
					btnSubBaseball.setChecked(false);
					btnSubBasketball.setChecked(false);
					btnSubVolleyball.setChecked(false);
					btnSubIcehockey.setChecked(false);

					return;
				case R.id.btnSubBaseball:
					menuAnalytics = false;
					web.loadUrl("http://www.liveman.net/livesports/odpotal-4.php");

					btnSubSoccer.setChecked(false);
					btnSubBaseball.setChecked(true);
					btnSubBasketball.setChecked(false);
					btnSubVolleyball.setChecked(false);
					btnSubIcehockey.setChecked(false);

					return;
				case R.id.btnSubBasketball:
					menuAnalytics = false;
					web.loadUrl("http://www.hot-odds.co.kr:80/Widget/AllGames?sportIds=48242&oddType=EU&timeZone=GMTplus9&langId=1");

					btnSubSoccer.setChecked(false);
					btnSubBaseball.setChecked(false);
					btnSubBasketball.setChecked(true);
					btnSubVolleyball.setChecked(false);
					btnSubIcehockey.setChecked(false);

					return;
				case R.id.btnSubVolleyball:
					menuAnalytics = false;
					web.loadUrl("http://www.hot-odds.co.kr:80/Widget/AllGames?sportIds=154830&oddType=EU&timeZone=GMTplus9&langId=1");

					btnSubSoccer.setChecked(false);
					btnSubBaseball.setChecked(false);
					btnSubBasketball.setChecked(false);
					btnSubVolleyball.setChecked(true);
					btnSubIcehockey.setChecked(false);

					return;
				case R.id.btnSubIcehockey:
					menuAnalytics = false;
					web.loadUrl("http://www.hot-odds.co.kr:80/Widget/AllGames?sportIds=35232&oddType=EU&timeZone=GMTplus9&langId=1");

					btnSubSoccer.setChecked(false);
					btnSubBaseball.setChecked(false);
					btnSubBasketball.setChecked(false);
					btnSubVolleyball.setChecked(false);
					btnSubIcehockey.setChecked(true);

					return;

				case R.id.btnMenuAnalysisPic:
					menuAnalytics = true;
					livetab.setVisibility(View.GONE);
					oddstab.setVisibility(View.GONE);

					web.loadUrl("http://acegame119.cafe24.com/xe/");
					btnMenuAnalysisPic.setChecked(true);
					btnMenuLivescore.setChecked(false);
					btnMenuForecast.setChecked(false);
					btnMenuTrend.setChecked(false);
					btnMenuAnalysisPic.setChecked(true);

					return;

				case R.id.imageBanner :

					Intent intent  = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(getString(R.string.bannerURL)));

					startActivity(intent);

					return;


				default:
					return;
			}
		}
	};




	private class TabWebViewClient extends WebViewClient {

		private TabWebViewClient() {

		}

		@Override
		public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap) {
			/*
			if (paramString.contains("cafe24")) {
		        paramWebView.setScaleX(1.0F);
		        btnMenuLivescore.setChecked(false);
	        	btnMenuForecast.setChecked(false);
	        	btnMenuTrend.setChecked(false);
	        	btnMenuAnalysisPic.setChecked(true);
			}
			*/

			if (paramString.contains("named")) {
				paramWebView.setScaleX(1.18F);
				paramWebView.setScaleY(1.14F);
				paramWebView.setPivotX(1.18F);
				paramWebView.setPivotY(1.14F);
			} else {
				paramWebView.setScaleX(1.0F);
				paramWebView.setScaleY(1.0F);
				paramWebView.setPivotX(1.0F);
				paramWebView.setPivotY(1.0F);

			}

			if ((progressDialog == null) || (!progressDialog.isShowing())) {
				progressDialog = new ProgressDialog(MainActivity.this);




				progressDialog.setMessage("Page Loading...");
				progressDialog.show();
			}
		}

		@Override
		public void onPageFinished(WebView paramWebView, String paramString) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}



	@Override
	public void onBackPressed() {

		/*
		if ( bPressedAction ) 
			return;

		bPressedAction = true;
		*/
		/*
		
		endProcMsg("종료하시겠습니까?");
		*/
		super.onBackPressed();
		return;

	}




	// 엡 종료 안내를 알리는 메시지 박스
	private void endProcMsg(String msg) {


		//final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		//alert.setBackgroundDrawableResource(R.color.white);
		alert.setTitle("Cool Live");
		alert.setMessage(msg);
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				System.exit(0);

				for (int i = 0; i < 5; i++)
					System.gc();

			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				//bPressedAction = false;
			}
		});


		//alert.setIcon(R.drawable.ic_launcher);
		alert.show();
	}




	public void setTimerContentSet(int delay) {


		timerContentSet = new Timer();
		timerContentSet.schedule(new TimerTask() {
			@Override
			public void run() {

				if (timerContentSet!= null )
					timerContentSet.cancel();
				timerContentSet = null;
				timerContentSetMethod();
			}
		}, delay);

	}

	private void timerContentSetMethod() {
		if ( timerContentSetMethod_Tick != null )
			runOnUiThread(timerContentSetMethod_Tick);
	}

	private Runnable timerContentSetMethod_Tick = new Runnable() {
		public void run() {
			setContentsFromNetwork();
		}
	};


	private void setContentsFromNetwork() {

		if (menuAnalytics) {
			btnMenuAnalysisPic.setChecked(true);
		}

		setTimerContentSet(1000);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
			web.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}