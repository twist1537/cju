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
	private ImageView ivKakao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.splash_activity);

//		ivKakao = (ImageView) findViewById(R.id.splash_activity_iv_kakao);
		
		splashCount = 0;

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				if (splashCount == 0) {
					splashCount++;
					ivKakao.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
					ivKakao.setVisibility(View.INVISIBLE);
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