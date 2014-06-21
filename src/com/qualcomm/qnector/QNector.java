package com.qualcomm.qnector;
import java.io.File;
import java.io.FilenameFilter;

import com.qconnector.schematicparser.ParseSchematic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class QNector extends Activity {
	private static String TAG = "QNector.java";
	private static AlertDialog.Builder builder;
    private String[] fileList;
    private File selectedFile;
    
	private Context context;
	private ActiveCanvas wksp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        wksp = new ActiveCanvas(this);
        
        setContentView(R.layout.activity_qnector);
        RelativeLayout a = (RelativeLayout) findViewById(R.id.bb);
        wksp.setBackgroundResource(R.drawable.breadboard);
        a.addView(wksp);
        
        showFileSelectorDialog(context);
        
        ImageButton openBttn = (ImageButton) findViewById(R.id.openBttn);
        openBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showFileSelectorDialog(context);
            }
        });
        ImageButton goBttn = (ImageButton) findViewById(R.id.goBttn);
        goBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	wksp.drawLines();
            }
        });
	}
	
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
					return filename.contains(".xml") || select.isDirectory();
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
                selectedFile = new File(Environment.getExternalStorageDirectory() + "/qnector/" + fileList[which]);
                updateSchematic();
            }
        });
		dialog = builder.show();
		return dialog;
	}
	
	private void updateSchematic(){
		Log.d(TAG, "update schematic");
		ParseSchematic.go(selectedFile);
		wksp.update();
	}
}
