package com.qualcomm.qnector;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BreadBoard extends Activity {
	private byte[] mImage;
	private static final String TAG = "BreadBoard";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_breadboard);
		Bundle bun = getIntent().getExtras();
		Log.d(TAG, "Intent passed == " + (bun != null));
		if(bun != null) {
			mImage = (byte[])(bun.get("breadboard"));
			Log.d(TAG, "Image passed == " + (mImage != null));
		}
	}
	
	
	
}
