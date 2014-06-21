package com.qualcomm.qnector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class QNector extends Activity {
	private final String TAG = "QNector.java";
	Camera camera;
	Preview preview;
	Button buttonClick;
	
	private static AlertDialog.Builder builder;
	private String[] fileList;
	private File selectedFile;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qnector);

		showFileSelectorDialog(this);
		
		preview = new Preview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);

		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preview.camera.takePicture(shutterCallback, rawCallback,
						jpegCallback);
			}
		});

		Log.d(TAG, "onCreate'd");
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				// write to local sandbox file system
				// outStream =
				// CameraDemo.this.openFileOutput(String.format("%d.jpg",
				// System.currentTimeMillis()), 0);
				// Or write to sdcard
				outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
				//outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};
	
	public Dialog showFileSelectorDialog(Context context) {
		File path = new File(Environment.getExternalStorageDirectory() + "/qnector/");
		try {
			path.mkdirs();
		} catch(SecurityException e) {
			Log.e(TAG, e.toString());
		}
		if(path.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File select = new File(dir, filename);
					return filename.contains(".sch") || select.isDirectory();
				}
			};
			fileList = path.list(filter);
		}
		else {
			fileList= new String[0];
		}
		
		Dialog dialog;
		builder = new AlertDialog.Builder(context);
		builder.setTitle("Select Schematic");
		if(fileList == null) {
			dialog = builder.create();
			return dialog;
		}
		builder.setItems(fileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selectedFile = new File(fileList[which]);
                updateSchematic();
            }
        });
		dialog = builder.show();
		return dialog;
	}
	
	private void updateSchematic(){
		//TODO on dialog selection
	}

}