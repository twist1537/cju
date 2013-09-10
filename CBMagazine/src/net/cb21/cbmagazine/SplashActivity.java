package net.cb21.cbmagazine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	private static final int DELAY_TIME = 2000;

	private int splashCount;
	private ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		imgView = (ImageView) findViewById(R.id.imgview_splash);
		
		splashCount = 0;

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (splashCount == 0) {
					splashCount++;
					imgView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
					imgView.setVisibility(View.INVISIBLE);
					new Handler().postDelayed(this, DELAY_TIME);
					return;
				}
				
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();

			}
		}, DELAY_TIME);
	}
}